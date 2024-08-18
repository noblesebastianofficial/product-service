# Stage 1: Build stage
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY product-service-open-api.yaml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM alpine/java:21-jdk
LABEL maintainer="noblesebastiank@gmail.com"

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar webapp.jar

# Expose the port the app runs on (if applicable)
EXPOSE 8080

# Command to run the application
CMD ["java","-jar","webapp.jar"]