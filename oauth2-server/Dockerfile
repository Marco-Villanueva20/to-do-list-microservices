FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder

WORKDIR /workspace

COPY . ./oauth2-server

RUN mvn -f oauth2-server/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /workspace/oauth2-server/target/*.jar oauth2-server.jar

ENTRYPOINT ["java", "-jar", "oauth2-server.jar"]