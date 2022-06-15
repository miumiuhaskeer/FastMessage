#FROM tomcat:8.5
#COPY target/demo-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/FastMessage.war
#CMD ["catalina.sh", "run"]

FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]