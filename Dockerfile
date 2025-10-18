# Stage 1: Build
FROM gradle:8.9-jdk21 AS build

WORKDIR /app

# Copy Gradle files
COPY . .

# Download dependencies
RUN gradle dependencies --no-daemon

# Build the application
RUN gradle bootJar --no-daemon

# Stage 2: Runtime
FROM openjdk:21

WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar","/app/app.jar"]
