# Scalol Frontend application
## An image sharing and chat application

## How to install

Clone the repo, import the project as a SBT project in your IDE.

Open a command prompt in the root directory, run ```sbt``` and once the project is loaded and all plugins and dependencies are downloaded, run ```~fastOptJS``` and open ```index.html``` through your IDE.

Alternatively, you can visit a working instance of the application here: https://akessonhenrik.github.io/Scalol-frontend/

Note that deploying on github-pages requires all http requests to be secured. Our current server uses autosigned certificates, so you will need to allow the server https://nixme.ddns.net before you can use the application, both locally and on gh-pages.