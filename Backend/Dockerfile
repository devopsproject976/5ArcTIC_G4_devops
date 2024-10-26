# Use OpenJDK 17 on Alpine
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Define build argument
ARG JAR_FILE

# Copy the dynamic Spring Boot jar file to the container
COPY ${JAR_FILE} 5ArcTIC3-G4-devopss-1.0.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Entry point for running the application
ENTRYPOINT ["java", "-jar", "/app/5ArcTIC3-G4-devopss.jar"]







