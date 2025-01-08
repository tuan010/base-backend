FROM openjdk:17

ARG JAR_FILE=target/backend-service-little-pig.jar

COPY ${JAR_FILE} backend-service-little-pig.jar

ENTRYPOINT ["java", "-jar", "backend-service-little-pig.jar"]

EXPOSE 8080