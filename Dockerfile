FROM eclipse-temurin:17-jre-alpine
RUN adduser --system --group springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/pets-service.jar
COPY ${JAR_FILE} pets-service.jar
ENTRYPOINT ["java","-jar", \
#"-DSPRING_PROFILES_ACTIVE=docker", \
#"-DTZ=America/Denver", \
#"-DBASIC_AUTH_USR_PETSDATABASE=some_username", \
#"-DBASIC_AUTH_PWD_PETSDATABASE=some_password", \
#"-DBASIC_AUTH_USR=another_username", \
#"-DBASIC_AUTH_PWD=another_password", \
"/pets-service.jar"]
# provide environment variables in docker-compose
