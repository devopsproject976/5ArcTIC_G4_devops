FROM openjdk:17-jdk-alpine

ENV NEXUS_USERNAME=admin
ENV NEXUS_PASSWORD=0000

WORKDIR /app

EXPOSE 8089

RUN apk add --no-cache curl \
    && curl -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} -O http://192.168.81.131:8081/repository/maven-snapshots/tn/esprit/5Arctic3-G4-devops-1.0.jar

ENTRYPOINT ["java", "-jar", "5Arctic3-G4-devops-1.0.jar"]