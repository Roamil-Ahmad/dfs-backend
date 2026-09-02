# syntax=docker/dockerfile:1
# ---- Build stage: compile the WAR (pom targets Java 13; toolchain is JDK 17) ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.war app.war
EXPOSE 9994
ENTRYPOINT ["java","-jar","app.war"]
