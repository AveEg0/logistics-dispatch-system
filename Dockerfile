FROM eclipse-temurin:26
LABEL authors="karma"
WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]