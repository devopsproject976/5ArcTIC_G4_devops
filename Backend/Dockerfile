# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Define build argument
ARG JAR_FILE

# Copy the dynamic Spring Boot jar file to the container
COPY target/DevOps_Project-1.0.jar /app/DevOps_Project-1.0.jar

# Expose the application port (you can update the port if needed)
EXPOSE 8082

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/DevOps_Project-1.0.jar"]
