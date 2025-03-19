# Sử dụng image chính thức của Maven để build
FROM maven:3.9.1-eclipse-temurin-17 AS build-stage

WORKDIR /app

# Copy toàn bộ source code
COPY . .

# Build ứng dụng Spring Boot
RUN mvn clean package -DskipTests

# Sử dụng JRE 17 cho production
FROM eclipse-temurin:17-jre-alpine AS production-stage

WORKDIR /app

# Copy file JAR từ build-stage
COPY --from=build-stage /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
