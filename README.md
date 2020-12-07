# pets-service

TODO: ADD TESTS

** Migrated to Public Repository after removing credentials. **

This REST API is implemented to execute business logic for PETS app

This app takes request from the UI, calls pets-database if needed
and processes the information to return the result to the UI

To run the App:

(1) From IDE, gradlew bootrun

(2) From Jar, java -jar JAR_NAME (provide environment variables) For example: java -jar -Dspring.profiles.active=development JAR_NAME

The App is deployed to Google App Engine. The app.yaml configuration file can be found on src/main/appengine folder.
To deploy the app to GCP, copy the app jar from build/libs folder to the appengine folder, and from the appengine folder
use gcloud SDK commands: (1) gcloud init (2) gcloud app deploy app.yaml.

The GCP specific configurations are based on active spring profile of production
mainly in application-production.yml and logback.yml

Three apps from the following repos need to be running together:
* https://github.com/bibekaryal86/pets-database
* https://github.com/bibekaryal86/pets-service (this)
* https://github.com/bibekaryal86/pets-ui-mpa

Deployed to:
* GCP: https://pets-service.appspot.com/pets-service/tests/ping
