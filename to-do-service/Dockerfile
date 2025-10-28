# Proceso para crear una imagen
# Etapa 1: Compilar con Maven
FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder

# Crea una carpeta dentro de la imagen llamada /workspace.
# WORKDIR crea la carpeta si no existe y cambia el contexto de trabajo a ella (similar a un cd).
WORKDIR /workspace

# Copia todo lo que está al mismo nivel que este Dockerfile desde el host hacia la imagen,
# dentro de una nueva carpeta llamada to-do-service (ubicada en /workspace gracias al WORKDIR).
COPY . ./to-do-service

# Compila el proyecto usando el pom.xml del módulo y omite los tests.
RUN mvn -f to-do-service/pom.xml clean package -DskipTests

# Etapa 2: Ejecutar con una imagen más liviana (solo JRE)
FROM eclipse-temurin:21-jre

# Crea una carpeta dentro de la imagen llamada /app
WORKDIR /app

# Copia el JAR compilado desde el builder
COPY --from=builder /workspace/to-do-service/target/*.jar to-do-service.jar

# Ejecuta el JAR dentro de la imagen 
ENTRYPOINT ["java", "-jar", "to-do-service.jar"]

# docker build -t dawwi/to-do.service:1.0 -f to-do-service/Dockerfile .

#cd to-do-service
# docker build -t dawwi/to-do-service:1.0 .
