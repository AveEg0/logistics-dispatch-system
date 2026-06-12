FROM gradle:9.5.0-jdk26 AS build

WORKDIR /app
COPY . .

RUN gradle clean bootJar


FROM eclipse-temurin:26
LABEL authors="karma"
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]