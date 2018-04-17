package fr.rhiobet.git;

import org.eclipse.egit.github.core.SearchRepository;

/**
 * This class is used to store the useful data for a repository
 */
public class Repository {
  
  private String name;
  private String url;
  private int stars;
  
  
  /**
   * Constructs a Repository from the specified data
   * @param name the name of this repository
   * @param url the URL of this repository
   * @param stars the number of stars of this repository
   */
  public Repository(String name, String url, int stars) {
    this.name = name;
    this.url = url;
    this.stars = stars;
  }
  
  
  protected Repository(SearchRepository searchRepository) {
    this(searchRepository.getName(), searchRepository.getUrl(), searchRepository.getWatchers());
  }


  /**
   * Returns the name of this repository
   * @return the name of this repository
   */
  public String getName() {
    return this.name;
  }


  /**
   * Returns the URL of this repository
   * @return the URL of this repository
   */
  public String getUrl() {
    return this.url;
  }


  /**
   * Returns the number of stars of this repository
   * @return the number of stars of this repository
   */
  public int getStars() {
    return this.stars;
  }


  @Override
  public String toString() {
    return name + ": " + url + " " + stars + " stars";
  }

}
