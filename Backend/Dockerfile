FROM openjdk:17-jdk-alpine

WORKDIR /app
ARG JAR_FILE
COPY target/${JAR_FILE} /app/5Arctic3-G4-AichaNciri.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/your-spring-app.jar"]