FROM openjdk:17-jdk-alpine

# Install MySQL client
RUN apk update && apk add --no-cache mariadb-client

WORKDIR /app

ARG JAR_FILE

COPY target/${JAR_FILE} /app/5ArcTIC3-G4-devops.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/5ArcTIC3-G4-devops.jar"]