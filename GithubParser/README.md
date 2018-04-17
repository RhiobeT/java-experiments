This program is mainly used to apply analysis on random Java projects, found on GitHub.

What it does at the moment is:
  - Finding several Maven compatible Java projects from GitHub, with different stars ratings
  - Downloading the found projects
  - Building them and removing the non working ones

  
In order to use this program, you will need a valid GitHub API token.
Please note that a valid token without any permission is enough (as it serves only for the authentication process).

You are encouraged to use the available docker release:
  - Load the image "github\_parser.docker" with the command "docker load github\_parser.docker"
  - Copy, edit and launch the "github\_parser.sh.example" file



