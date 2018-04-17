package fr.rhiobet.tools;

import java.util.List;

import fr.rhiobet.git.AuthentifiedClient;
import fr.rhiobet.git.JavaRepositorySearcher;
import fr.rhiobet.git.Repository;
import fr.rhiobet.maven.MavenProjectManager;

public class Main {

  public static void main(String args[]) {
    String apiToken = System.getenv("GITHUB_API_TOKEN");
    String mavenHome = System.getenv("MAVEN_HOME");
    String projectsRoot = System.getenv("GITHUB_PARSER_PROJECT_ROOT");
    String projectsNumber = System.getenv("NUMBER_WANTED");
    
    int projectsNumberInt;
    try {
      projectsNumberInt = Integer.parseInt(projectsNumber);
    } catch (Exception e) {
      projectsNumberInt = 100;
    }
   
    JavaRepositorySearcher searcher;
    if (apiToken != null) {
      AuthentifiedClient client = new AuthentifiedClient(apiToken);
      searcher = new JavaRepositorySearcher(client);
    } else {
      searcher = new JavaRepositorySearcher();
    }
    searcher.findUsingStarsRepartition(projectsNumberInt, true, true);
    System.out.println();
    
    List<Repository> results = searcher.getResults();
    System.out.println("Number of Maven projects: " + results.size());
    for (Repository repository : results) {
      System.out.println(repository);
    }
    System.out.println();
    
    MavenProjectManager mavenProjectManager = new MavenProjectManager(mavenHome, projectsRoot);
    mavenProjectManager.addProjectsFromGitRepositories(results);
    mavenProjectManager.downloadGitProjects(true);
    System.out.println();
    mavenProjectManager.buildProjects(true);
  }
  
}
