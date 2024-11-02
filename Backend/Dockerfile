FROM openjdk:17-jdk-alpine

# Install MySQL client
RUN apk update && apk add --no-cache mariadb-client

WORKDIR /app


COPY target/5ArcTIC3-G4-devops-1.0.jar /app/

# List the contents of /app to verify the JAR file is present
RUN ls -l /app

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/5ArcTIC3-G4-devops-1.0.jar"]