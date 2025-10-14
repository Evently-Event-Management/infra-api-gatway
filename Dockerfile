# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:21-slim

# Install curl for health checks
RUN apt-get update && apt-get install -y curl --no-install-recommends && rm -rf /var/lib/apt/lists/*

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the gateway runs on (as defined in your application.yml)
EXPOSE 8088

# Add the health check instruction, updated for port 8088
# NOTE: You may need to change the path '/actuator/health' to your actual health endpoint.
HEALTHCHECK --interval=30s --timeout=10s --retries=5 \
  CMD curl -f http://localhost:8088/api/gateway/health || exit 1

# Set the command to run the application when the container starts
ENTRYPOINT ["java","-jar","/app.jar"]
