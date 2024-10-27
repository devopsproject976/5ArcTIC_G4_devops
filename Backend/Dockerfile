# Use OpenJDK 11 on Alpine
FROM openjdk:11-jdk-alpine

# Set the working directory
WORKDIR /app

# Add the JAR file from the target directory to the container
ADD ./target/5ArcTIC3-G4-devops-1.0.jar /app/5ArcTIC3-G4-devops.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Entry point for running the application
ENTRYPOINT ["java", "-jar", "/app/5ArcTIC3-G4-devops.jar"]

