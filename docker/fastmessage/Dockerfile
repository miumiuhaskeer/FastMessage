FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]