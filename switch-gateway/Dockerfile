# syntax=docker/dockerfile:1
# ---- Build stage: compile the WAR on Java 17 + Maven 3.9.9 ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q clean package -DskipTests

# ---- Runtime stage: run the Spring Boot WAR standalone on Java 17 JRE ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.war app.war
# Standardized service port (overridden via SERVER_PORT from docker-compose)
EXPOSE 9995
ENTRYPOINT ["java","-jar","app.war"]
