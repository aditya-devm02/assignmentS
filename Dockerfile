# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port
EXPOSE 8080

# Set the entry point
ENTRYPOINT ["java", "-jar", "target/personal-finance-manager-1.0.0.jar"] 