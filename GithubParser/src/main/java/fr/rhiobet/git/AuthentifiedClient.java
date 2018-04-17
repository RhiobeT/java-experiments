package fr.rhiobet.git;

import org.eclipse.egit.github.core.client.GitHubClient;

/**
 * This class defines a user authenticated with a GitHub token.
 * 
 * Only a single instance is needed, and should be used when declaring objects using the GitHub API.
 */
public class AuthentifiedClient {
  
  private GitHubClient client;
  
  /**
   * Constructs an AuthentifiedClient using a GitHub API token
   * @param token the token to use with the GitHub API
   */
  public AuthentifiedClient(String token) {
    this.client = new GitHubClient();
    this.client.setOAuth2Token(token);
  }
  
  protected GitHubClient getClient() {
    return this.client;
  }

}
