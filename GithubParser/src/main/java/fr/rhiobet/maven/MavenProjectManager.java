package fr.rhiobet.maven;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.invoker.PrintStreamHandler;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import fr.rhiobet.git.Repository;

/**
 * This class is used to manage Maven projects
 */
public class MavenProjectManager {

  private List<Repository> gitRepositories;
  private List<File> managedProjects;
  private String projectRoot;
  private Invoker invoker;
  
  
  /**
   * Constructs a MavenProjectManager
   * @param mavenHome the directory in which Maven is installed
   * @param projectRoot the directory in which to manage the projects
   */
  public MavenProjectManager(String mavenHome, String projectRoot) {
    this.gitRepositories = new ArrayList<>();
    this.managedProjects = new ArrayList<>();
    this.projectRoot = projectRoot;
    this.invoker = new DefaultInvoker();
    try {
      new File(projectRoot).mkdirs();
      File errorLogFile = new File(projectRoot + "/error.log");
      File outputLogFile = new File(projectRoot + "/out.log");
      errorLogFile.createNewFile();
      outputLogFile.createNewFile();
      this.invoker.setMavenHome(new File(mavenHome))
              .setErrorHandler(new PrintStreamHandler(new PrintStream(errorLogFile), true))
              .setOutputHandler(new PrintStreamHandler(new PrintStream(outputLogFile), true));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  
  /**
   * Adds git repositories to the list of projects managed by this instance.
   * <br />
   * In order to actually download these projects, a call to this method should be followed by
   * {@link #downloadGitProjects()}
   * @param repositories the repositories to add
   */
  public void addProjectsFromGitRepositories(List<Repository> repositories) {
    this.gitRepositories.addAll(repositories);
  }
  
 
  /**
   * Downloads the git projects managed by this instance.
   * 
   * Nothing is printed on the standard output.
   */
  public void downloadGitProjects() {
    this.downloadGitProjects(false);
  }
  
  
  /**
   * Downloads the git projects managed by this instance.
   * @param showProgress whether to print the current progress on standard output or not
   */
  public void downloadGitProjects(boolean showProgress) {
    if (showProgress) {
      System.out.println("Starting download...");
    }
    for (Repository repository : this.gitRepositories) {
      File directory = new File(this.projectRoot + "/" + repository.getName());
      if (directory.exists()) {
        if (showProgress) {
          System.out.println(repository.getName() + " already exists");
        }
        this.managedProjects.add(directory);
      } else {
        try {
          if (showProgress) {
            System.out.println("Downloading " + repository.getName() + "... ");
          }
          Git.cloneRepository().setCloneSubmodules(true)
                  .setURI(repository.getUrl()).setDirectory(directory).call();
          if (showProgress) {
            System.out.println("OK");
          }
          this.managedProjects.add(directory);
        } catch (GitAPIException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  
  /**
   * Builds all the projects managed by this instance.
   * 
   * Nothing is printed on the standard output.
   * <br />
   * Git projects should be downloaded before calling this method.
   * <br />
   * The projects that can't be built will be deleted both from the list of managed projects and from the disk
   */
  public void buildProjects() {
    this.buildProjects(false);
  }
  
  
  /**
   * Builds all the projects managed by this instance.
   * <br />
   * Git projects should be downloaded before calling this method.
   * <br />
   * The projects that can't be built will be deleted both from the list of managed projects and from the disk
   * @param showProgress whether to print the current progress on standard output or not
   */
  public void buildProjects(boolean showProgress) {
    if (showProgress) {
      System.out.println("Starting building...");
    }
    for (int i = 0; i < this.managedProjects.size(); i++) {
      File pomFile = new File(this.managedProjects.get(i).getAbsolutePath() + "/pom.xml");
      InvocationRequest request = new DefaultInvocationRequest();
      request.setPomFile(pomFile);
      request.setGoals(Arrays.asList("clean", "install"));
      request.setBatchMode(true);
      try {
        if (showProgress) {
          System.out.println("Building " + this.managedProjects.get(i).getName() + "... ");
        }
        InvocationResult result = this.invoker.execute(request);
        if (result.getExitCode() == 0) {
          if (showProgress) {
            System.out.println("OK");
          }
        } else {
          if (showProgress) {
            System.out.println("NOK");
          }
          Files.walk(this.managedProjects.get(i).toPath()).sorted(Comparator.reverseOrder())
                  .map(Path::toFile).forEach(File::delete);
          this.managedProjects.remove(i--);
        }
      } catch (MavenInvocationException | IOException e) {
        e.printStackTrace();
      }
    }
  }
  
}
