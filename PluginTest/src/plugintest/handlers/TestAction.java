package plugintest.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import plugintest.tools.WorkspaceManipulation;

/**
 * A basic handler, expected to be called via an action in the right click menu of a .java file
 */
public class TestAction extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ICompilationUnit resource = WorkspaceManipulation.getSelectionFromProjectExplorer();
    if (resource != null) {
      // The ASTParser will be used to generate an AST from the resource (as a CompilationUnit)
      ASTParser parser = ASTParser.newParser(AST.JLS9);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setSource(resource);
      parser.setResolveBindings(true);
      
      // Here we generate the AST
      CompilationUnit cu = (CompilationUnit) parser.createAST(null);
      
      // We link our visitor to the AST
      cu.accept(new MethodDeclarationFinder());
      
      // This makes the AST editable
      cu.recordModifications();
      
      // Here is a node factory
      AST factory = cu.getAST();
      
      // Basic example to add a new import declaration to the AST
      ImportDeclaration importDeclaration = factory.newImportDeclaration();
      importDeclaration.setName(factory.newName(new String[] {"java", "util", "Set"}));
      cu.imports().add(importDeclaration);
      
      // At last we write the changes to a copy, created in the directory "generated"
      WorkspaceManipulation.saveCompilationUnitAsCopy(cu, "generated");
    }
     
    return null;
  }

 
  /**
   * Basically a visitor that finds and prints the methods declared in a class
   */
  static final class MethodDeclarationFinder extends ASTVisitor {
    @Override
    public boolean visit (final MethodDeclaration method) {
      System.out.println(method.toString());
      return super.visit(method);
    }
  }

}
