# ---- Build stage: compile and package the Spring Boot jar ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Run stage: slim JRE image that only carries the jar ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/drumhub-backend-0.0.1-SNAPSHOT.jar app.jar

# Render injects $PORT; bind Spring Boot to it (falls back to 8080 locally).
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
