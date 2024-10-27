# Use a multi-stage build to reduce the final image size
FROM maven:3.8.6-openjdk-11-slim AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY ./pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY ./src ./src

# Build the application
RUN mvn clean package

# Use a lighter base image for the final image
FROM openjdk:11-jdk-slim

# Set the working directory for the final image
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/5ArcTIC3-G4-devops-1.0.jar ./5ArcTIC3-G4-devops.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "./5ArcTIC3-G4-devops.jar"]
