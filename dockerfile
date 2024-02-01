#
# Build stage
#
FROM eclipse-temurin:21-jdk AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:21-jdk-slim
COPY --from=build /target/ClockIn-Out-Backend-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]