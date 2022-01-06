# pets-service

This REST API is implemented to execute business logic for the web application. This app takes request from the UI,
calls pets-database if needed and processes the information to return the result to the UI.

To run the app, we need to supply the following environment variables:

* Active Profile
    * SPRING_PROFILES_ACTIVE (development, docker, production)
* Application security details:
    * BASIC_AUTH_PWD (username to enforce spring security)
    * BASIC_AUTH_USR (password to enforce spring security)
* MongoDB Database Details:
    * BASIC_AUTH_PWD_PETSDATABASE (auth username of pets-database)
    * BASIC_AUTH_USR_PETSDATABASE (auth password of pets-database)
* The final run command looks like this:
    * java -jar -DSPRING_PROFILES_ACTIVE=development -DBASIC_AUTH_USR=some_username -DBASIC_AUTH_PWD=some_password
      -DBASIC_AUTH_PWD_PETSDATABASE=another_username -DBASIC_AUTH_USR_PETSDATABASE=another_password JARFILE.jar

This app is one of the five apps that form the PETS (Personal Expenses Tracking System) application:

* https://github.com/bibekaryal86/pets-database
* https://github.com/bibekaryal86/pets-service (this)
* https://github.com/bibekaryal86/pets-authenticate
* https://github.com/bibekaryal86/pets-gateway
* https://github.com/bibekaryal86/pets-spa

This app is deployed in Google Cloud Project. The GCP configurations are found in the `gcp` folder in the project root.
To deploy to GCP, we need to copy the jar file to that folder and use gcloud app deploy terminal command.

* App Test Link: https://pets-service.appspot.com/pets-service/tests/ping
