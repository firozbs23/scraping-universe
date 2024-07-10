# Use OpenJDK 21 slim as the base image
FROM openjdk:21-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the app directory
COPY target/scraping-universe-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application uses
EXPOSE 8080
# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
