FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder

WORKDIR /workspace


COPY . ./user-service




RUN mvn -f user-service/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre


WORKDIR /app


COPY --from=builder /workspace/user-service/target/*.jar user-service.jar

ENTRYPOINT ["java", "-jar", "user-service.jar"]
