FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/report-viewer-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]