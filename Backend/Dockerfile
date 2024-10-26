# Use OpenJDK 17 on Alpine
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the dynamic Spring Boot jar file to the container
COPY ./target/5ArcTIC3-G4-devopss-1.0.jar /app/5ArcTIC3-G4-devopss.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Entry point for running the application
ENTRYPOINT ["java", "-jar", "/app/5ArcTIC3-G4-devopss.jar"]
