package fr.rhiobet.tools;

import java.util.List;

import fr.rhiobet.git.AuthentifiedClient;
import fr.rhiobet.git.JavaRepositorySearcher;
import fr.rhiobet.git.Repository;
import fr.rhiobet.maven.MavenProjectManager;

public class Main {

  public static void main(String args[]) {
    // First we get all of the necessary data from system variables
    String apiToken = System.getenv("GITHUB_API_TOKEN");
    String mavenHome = System.getenv("MAVEN_HOME");
    String projectsRoot = System.getenv("PROJECT_ROOT");
    String projectsNumber = System.getenv("NUMBER_WANTED");
    String strategy = System.getenv("STRATEGY");
    
    // If the number of projects wasn't specified, we set it to 100 by default
    int projectsNumberInt;
    try {
      projectsNumberInt = Integer.parseInt(projectsNumber);
    } catch (Exception e) {
      projectsNumberInt = 100;
    }
   
    // If the Github API token wasn't specified, the user will stay as a guest
    JavaRepositorySearcher searcher;
    if (apiToken != null) {
      AuthentifiedClient client = new AuthentifiedClient(apiToken);
      searcher = new JavaRepositorySearcher(client);
    } else {
      searcher = new JavaRepositorySearcher();
    }
    
    // If the strategy wasn't specified, it is set to random by default
    if (strategy == null) {
      strategy = "random";
    }
    
    if (strategy.equals("random")) {
      System.out.println("Searching...");
      // Random search is essentially just getting the latest updated projects, without caring for stars
      searcher.find(projectsNumberInt, true);
    } else if (strategy.equals("stars")) {
      // This mode will get projects that have different stars rating
      searcher.findUsingStarsRepartition(projectsNumberInt, true, true);
    } else if (strategy.equals("hybrid")) {
      // This mode will use both of the previous modes for half of the set each
      System.out.println("First search...");
      searcher.findUsingStarsRepartition(projectsNumberInt/2, true, true);
      System.out.println("Second search...");
      searcher.find(projectsNumberInt/2, true);
    } else {
      System.err.println("Unknown strategy");
      System.exit(1);
    }
    System.out.println();
    
    // Here we get and print the results of the search
    List<Repository> results = searcher.getResults();
    System.out.println("Number of Maven projects: " + results.size());
    for (Repository repository : results) {
      System.out.println(repository);
    }
    System.out.println();
    
    MavenProjectManager mavenProjectManager = new MavenProjectManager(mavenHome, projectsRoot);
    // We download the found projects here
    mavenProjectManager.addProjectsFromGitRepositories(results);
    mavenProjectManager.downloadGitProjects(true);
    System.out.println();
    // Finally we build the downloaded projects
    mavenProjectManager.buildProjects(true);
  }
  
}
