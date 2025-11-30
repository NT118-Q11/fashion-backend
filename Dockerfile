# Multi-stage build for Spring Boot application

# Stage 1: Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copy Gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Copy source code
COPY src ./src

# Build application
RUN ./gradlew build -x test --no-daemon

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check (simple HTTP check using curl - available in base image)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/api/auth/register || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]

