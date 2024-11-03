# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Argument pour le fichier JAR
ARG JAR_FILE=target/5ArcTIC3-G4-devops-1.0-SNAPSHOT.jar

# Copy the Spring Boot jar file to the container
COPY ${JAR_FILE} /app/5ArcTIC3-G4-devops-1.0-SNAPSHOT.jar

# Expose the application port
EXPOSE 8082

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/5ArcTIC3-G4-devops-1.0-SNAPSHOT.jar"]
