FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder

WORKDIR /workspace

COPY . ./config-server

RUN mvn -f config-server/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /workspace/config-server/target/*.jar config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]