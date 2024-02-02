FROM eclipse-temurin:21-jdk AS build
VOLUME /tmp
COPY target/*.jar Inclockgnito-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Inclockgnito-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080