package plugintest.tools;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

public class WorkspaceManipulation {

  
  /**
   * Gets a valid java resource from the ProjectExplorer's UI
   * 
   * Will return the selected java file as a ICompilationUnit<br />
   * Please note that there is no verification done to check the file is indeed a .java file, use with caution
   * @return the selected .java file in the ProjectExplorer's UI, null if there was an issue
   */
  public static ICompilationUnit getSelectionFromProjectExplorer() {
    ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
    ISelection selection = selectionService.getSelection("org.eclipse.ui.navigator.ProjectExplorer"); 
    if (selection instanceof IStructuredSelection) {
      Object selected = ((IStructuredSelection) selection).getFirstElement();
      if (selected instanceof IAdaptable) {
        return (ICompilationUnit) Platform.getAdapterManager().getAdapter(selected, ICompilationUnit.class);
      }
    }
    
    return null;
  }
  
  
  /**
   * Saves the changes done to a compilation unit in a copy, located in the specified directory
   * 
   * Please note this method will only work with .java files
   * @param compilationUnit the compilation unit to be saved
   * @param destinationFolder the folder in which to do the save operation
   * @return true if the save operation was successful, false otherwise
   */
  public static boolean saveCompilationUnitAsCopy(CompilationUnit compilationUnit, String destinationFolder) {
    // We first need to get the resource linked to the compilation unit, useful to access the file structure
    ICompilationUnit resourceUnit;
    // We check the resource actually comes from a .java file (could be a .class file)
    if (compilationUnit.getJavaElement() instanceof ICompilationUnit) {
      resourceUnit = (ICompilationUnit) compilationUnit.getJavaElement();
    } else {
      System.err.println("Error: The specified compilationUnit comes from type "
                         + compilationUnit.getJavaElement().getClass() + ", should be ICompilationUnit");
      return false;
    }
    
    // The IDocument object is needed to compute the diff between the source file and the current compilation unit
    IDocument document;
    try {
      document = new Document(resourceUnit.getSource());
    } catch (JavaModelException e) {
      System.err.println("Error: CompilationUnit " + resourceUnit.getElementName() + " has no source");
      e.printStackTrace();
      return false;
    }
    
    // Here we compute the diff between the source file and the current compilation unit
    TextEdit textEdit = compilationUnit.rewrite(document, null);
    
    // In order to save a compilation unit in the specified folder, we need to add it to the project's classpath
    IJavaProject project = resourceUnit.getJavaProject();
    IFolder folder = project.getProject().getFolder(destinationFolder);
    if (!WorkspaceManipulation.addSourceToClasspath(project, folder)) {
      return false;
    }
    
    // Here we make the copy that we will use later
    ICompilationUnit newCompilationUnit = WorkspaceManipulation.copyCompilationUnit(resourceUnit, folder);
    if (newCompilationUnit == null) {
      WorkspaceManipulation.removeSourceFromClasspath(project, folder);
      return false;
    }
    
    // We set the copy as a working copy, apply the changes, then commit them
    try {
      newCompilationUnit.becomeWorkingCopy(null);
      newCompilationUnit.applyTextEdit(textEdit, null);
      newCompilationUnit.commitWorkingCopy(true, null);
    } catch (JavaModelException e) {
      System.err.println("Error: Unable to save the changes to compilationUnit '"
                         + resourceUnit.getElementName() + "'");
      e.printStackTrace();
      WorkspaceManipulation.removeSourceFromClasspath(project, folder);
      return false;
    }
    
    // At last, we remove the destination folder from the classpath
    WorkspaceManipulation.removeSourceFromClasspath(project, folder);
    return true;
  }
  
 
  /**
   * Makes a copy of a java resource in the specified folder
   * 
   * The package tree will also be copied<br />
   * Please note that the copy will fail if the folder is not in the resource's project's classpath
   * @param resource the resource we want to copy
   * @param destinationFolder the folder in which we want to copy
   * @return the created copy, or null if it failed
   */
  public static ICompilationUnit copyCompilationUnit(ICompilationUnit resource, IFolder destinationFolder) {
    // We first get the project of the resource
    IJavaProject project = resource.getJavaProject();
    
    // We create the destination folder if it doesn't exist yet
    if (!destinationFolder.exists()) {
      try {
        destinationFolder.create(true, true, null);
      } catch (CoreException e) {
        System.err.println("Error: Unable to create the specified destinationFolder '" + destinationFolder + "'");
        e.printStackTrace();
        return null;
      }
    }
    
    // Here we get a root of packages from the destination folder
    IPackageFragmentRoot root = project.getPackageFragmentRoot(destinationFolder);
    
    // We get the current resource's package directly from the source code
    IPackageDeclaration declarations[];
    try {
      declarations = resource.getPackageDeclarations();
    } catch (JavaModelException e) {
      System.err.println("Error: Unable to access the source CompilationUnit '" + resource.getElementName() + "'");
      e.printStackTrace();
      return null;
    }
    String packageName;
    if (declarations.length == 0) {
      packageName = "";
    } else {
      packageName = declarations[0].getElementName();
    }
    
    // We now create the resource's package inside the destination folder
    IPackageFragment packageFragment;
    try {
      packageFragment = root.createPackageFragment(packageName, true, null);
    } catch (JavaModelException e) {
      System.err.println("Error: Unable to create the destination package '" + packageName + "'");
      e.printStackTrace();
      return null;
    }
    
    // At last, we create the copy of the resource in the new package
    try {
      return packageFragment.createCompilationUnit(resource.getElementName(), resource.getSource(), true, null);
    } catch (JavaModelException e) {
      System.err.println("Error: Unable to create the destination CompilationUnit '"
                         + resource.getElementName() + "'");
      e.printStackTrace();
      return null;
    }
  }
  
  
  /**
   * Adds a new sources directory to a java project's classpath
   * @param project the project to edit
   * @param newSource the directory to add to the classpath
   * @return true if the add was successful, false otherwise
   */
  public static boolean addSourceToClasspath(IJavaProject project, IFolder newSource) {
    // We first convert the folder path to a classpath entry
    IClasspathEntry generatedEntry = JavaCore.newSourceEntry(newSource.getFullPath());
    
    // We get all the existing classpath entries from the project
    IClasspathEntry entries[];
    try {
      entries = project.getRawClasspath();
    } catch (JavaModelException e) {
      System.err.println("Error: Unable to access the specified project's classpath '"
                         + project.getElementName() + "'");
      e.printStackTrace();
      return false;
    }
    
    // We then copy them in a larger array, and add the new one
    IClasspathEntry newEntries[] = new IClasspathEntry[entries.length + 1];
    System.arraycopy(entries, 0, newEntries, 0, entries.length);
    newEntries[entries.length] = generatedEntry;
    
    // At last, we set the new array as the project's classpath
    try {
      project.setRawClasspath(newEntries, null);
    } catch (JavaModelException e) {
      System.err.println("Error: Unable to edit the specified project's classpath ('"
                         + project.getElementName() + "'");
      e.printStackTrace();
      return false;
    }
    
    return true;
  }
  
  
  /**
   * Removes a sources directory from a java project's classpath
   * @param project the project to edit
   * @param source the directory to remove from the classpath
   * @return true if the operation was successful, false otherwise
   */
  public static boolean removeSourceFromClasspath(IJavaProject project, IFolder source) {
    // We first get the list of classpath entries from the project
    IClasspathEntry entries[];
    try {
      entries = project.getRawClasspath();
    } catch (JavaModelException e) {
      System.err.println("Error: Unable to access the specified project's classpath '" + project.getElementName() + "'");
      e.printStackTrace();
      return false;
    }
    
    // Here, we check if the entry we want to remove is actually in the classpath
    int i; // If an entry is found, 'i' will be its index
    boolean found = false;
    IPath path = source.getFullPath();
    for (i = 0; i < entries.length; i++) {
      if (entries[i].getPath().equals(path)) {
        found = true;
        break;
      }
    }
   
    if (found) {
      // If the entry was found, we copy the previous and following ones in a smaller array
      IClasspathEntry newEntries[] = new IClasspathEntry[entries.length - 1];
      System.arraycopy(entries, 0, newEntries, 0, i);
      System.arraycopy(entries, i+1, newEntries, i, entries.length-i-1);
      
      // At last, we set the new array as the project's classpath
      try {
        project.setRawClasspath(newEntries, null);
      } catch (JavaModelException e) {
        System.err.println("Error: Unable to edit the specified project's classpath ('" + project.getElementName() + "'");
        e.printStackTrace();
        return false;
      }
    }
    
    return true;
  }

}
