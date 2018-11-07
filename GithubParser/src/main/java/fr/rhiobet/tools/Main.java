package fr.rhiobet.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import fr.rhiobet.git.AuthentifiedClient;
import fr.rhiobet.git.JavaRepositorySearcher;
import fr.rhiobet.git.Repository;
import fr.rhiobet.maven.MavenProjectManager;

public class Main {

  public static void main(String args[]) {
    
    String apiToken = null;
    String mavenHome = System.getenv("M2_HOME");
    String projectsRoot = "./projects";
    String projectsNumber = null;
    String strategy = null;
    String filterDependencies = null;

    // First we get all of the necessary data from the config file
    Properties properties = new Properties();
    try {
      properties.loadFromXML(new FileInputStream("settings.xml"));
      apiToken = properties.getProperty("GITHUB_API_TOKEN");
      mavenHome = properties.getProperty("MAVEN_HOME");
      projectsRoot = properties.getProperty("PROJECT_ROOT");
      projectsNumber = properties.getProperty("NUMBER_WANTED");
      strategy = properties.getProperty("STRATEGY");
      filterDependencies = properties.getProperty("FILTER_DEPENDENCIES");
    } catch (IOException e) {
      System.out.println("Couldn't find 'settings.xml', using default settings.");
    }

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

    searcher.setFilterDependencies(filterDependencies);
    
    if (strategy.equals("random")) {
      System.out.println("Searching...");
      // Random search is essentially just getting the latest updated projects, without caring for stars
      searcher.find(projectsNumberInt, false, true);
    } else if (strategy.equals("stars")) {
      // This mode will get projects that have the best star ratings
      searcher.find(projectsNumberInt, true, true);
    } else if (strategy.equals("repartition")) {
      // This mode will get projects that have very different stars rating
      searcher.findUsingStarsRepartition(projectsNumberInt, true, true);
    } else if (strategy.equals("hybrid")) {
      // This mode will use both random and repartition for half of the set each
      System.out.println("First search...");
      searcher.findUsingStarsRepartition(projectsNumberInt/2, true, true);
      System.out.println("Second search...");
      searcher.find(projectsNumberInt/2, false, true);
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
