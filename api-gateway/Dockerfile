FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder

WORKDIR /workspace

COPY . ./api-gateway

RUN mvn -f api-gateway/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /workspace/api-gateway/target/*.jar api-gateway.jar

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]