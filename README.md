# ToDo List Microservices

Un proyecto personal de práctica para implementar una arquitectura de microservicios completa para una aplicación de gestión de tareas (ToDo List) con autenticación OAuth2/OpenID Connect, desarrollado con Spring Boot y Angular.

**Autor:** Marco Villanueva
**Repositorio:** [https://github.com/Marco-Villanueva20/to-do-list-microservices.git](https://github.com/Marco-Villanueva20/to-do-list-microservices.git)

Este proyecto es una práctica personal para aprender y experimentar con microservicios, Spring Cloud, OAuth2 y desarrollo full-stack.

## 🏗️ Arquitectura

Este proyecto implementa una arquitectura de microservicios que incluye:

- **API Gateway**: Punto de entrada único para todas las solicitudes
- **Config Server**: Gestión centralizada de configuraciones
- **Eureka Server**: Servicio de registro y descubrimiento
- **OAuth2 Server**: Servidor de autorización y autenticación
- **ToDo Service**: Servicio de gestión de tareas
- **User Service**: Servicio de gestión de usuarios
- **Frontend Angular**: Interfaz de usuario

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Cloud 2025.0.0**
- **Spring Security OAuth2**
- **Spring Data JPA**
- **Spring Data Redis**
- **MySQL Database**
- **Redis Database** (Caching)
- **Resilience4j** (Circuit Breaker, Retry)
- **Netflix Eureka** (Service Discovery)
- **Spring Cloud Gateway**

### Frontend
- **Angular 20**
- **Angular OAuth2 OIDC**
- **Bootstrap** (para estilos)
- **TypeScript**

## 📁 Estructura del Proyecto

```
to-do-microservicio/
├── api-gateway/           # API Gateway con Spring Cloud Gateway
├── config-server/         # Servidor de configuración centralizado
├── config-repo/           # Repositorio de configuraciones
├── eureka-server/         # Servicio de registro Eureka
├── oauth2-server/         # Servidor de autorización OAuth2
├── to-do-service/         # Servicio de tareas ToDo
├── user-service/          # Servicio de usuarios
├── proyecto-oauth2/       # Frontend Angular
└── README.md
```

## 🚀 Puesta en Marcha

### Opción 1: Despliegue con Docker (Recomendado)

El proyecto incluye configuración completa con Docker Compose para facilitar el despliegue.

#### Prerrequisitos
- **Docker** y **Docker Compose** instalados

#### Inicio con Docker
1. Desde la raíz del proyecto, ejecuta:
```bash
docker-compose up --build
```
Esto iniciará todos los servicios automáticamente:
- MySQL Database (puerto 3307)
- Redis Database (puerto 6379)
- Config Server (puerto 8888)
- OAuth2 Server (puerto 9000)
- Eureka Server (puerto 8761)
- API Gateway (puerto 8080)
- User Service (puerto dinámico)
- ToDo Service (puerto dinámico)
- Frontend Angular (puerto 4200, requiere inicio manual)

2. Para el frontend, en una terminal separada:
```bash
cd proyecto-oauth2
npm install
ng serve
```

### Opción 2: Inicio Manual

#### Prerrequisitos

- **Java 17** o superior
- **Maven 3.6+**
- **Node.js 18+** y **npm**
- **MySQL 8.0+**
- **Git**

### Configuración de Base de Datos

1. Crear la base de datos MySQL:
```sql
CREATE DATABASE tododb;
```

2. Las tablas se crearán automáticamente mediante JPA con `hibernate.ddl-auto=update` (para oauth2-server) y scripts SQL (para los otros servicios).

### Configuración del Config Server

El servidor de configuración lee las configuraciones desde `config-repo/`. Asegúrate de que la ruta en `config-server/src/main/resources/application.properties` apunte correctamente:

```properties
spring.cloud.config.server.native.search-locations=file:///D:/to-do-microservicio/config-repo
```

### Inicio de Servicios

**Importante**: Iniciar los servicios en el siguiente orden:

1. **Config Server** (puerto 8888):
```bash
cd config-server
mvn spring-boot:run
```

2. **Eureka Server** (puerto 8761):
```bash
cd eureka-server
mvn spring-boot:run
```

3. **OAuth2 Server** (puerto 9000):
```bash
cd oauth2-server
mvn spring-boot:run
```

4. **API Gateway** (puerto 8080):
```bash
cd api-gateway
mvn spring-boot:run
```

5. **ToDo Service** (puerto dinámico):
```bash
cd to-do-service
mvn spring-boot:run
```

6. **User Service** (puerto dinámico):
```bash
cd user-service
mvn spring-boot:run
```

7. **Frontend Angular** (puerto 4200):
```bash
cd proyecto-oauth2
npm install
ng serve
```

## 🔐 Autenticación OAuth2

### Flujo de Autenticación

1. El usuario accede a `http://localhost:4200`
2. Al hacer clic en "Login", es redirigido al OAuth2 Server (`http://localhost:9000`)
3. Después de autenticarse, recibe un JWT token
4. El token se usa para acceder a los servicios protegidos a través del API Gateway

### Configuración OAuth2

- **Issuer URI**: `http://localhost:9000`
- **Client ID**: `angular-app`
- **Scopes**: `openid profile read write`
- **Grant Type**: Authorization Code Flow

## 📡 Endpoints Principales

### API Gateway (puerto 8080)
- Todas las rutas se prefijan automáticamente con el nombre del servicio
- Ejemplo: `http://localhost:8080/to-do-service/api/todo`

### OAuth2 Server (puerto 9000)
- `/oauth2/authorize` - Autorización
- `/oauth2/token` - Obtención de tokens
- `/oauth2/jwks` - Claves públicas JWT

### Eureka Server (puerto 8761)
- Dashboard: `http://localhost:8761`

### Frontend (puerto 4200)
- `http://localhost:4200` - Página principal
- `http://localhost:4200/tasks` - Lista de tareas (requiere autenticación)

## 🛡️ Seguridad

- **JWT Tokens** para autenticación stateless
- **Resource Server** en servicios backend
- **CORS** configurado en API Gateway
- **Circuit Breaker** con Resilience4j en ToDo Service
- **Redis Caching** para mejorar rendimiento en ToDo Service
- **Service Discovery** seguro con Eureka

## 🔄 Circuit Breaker y Resiliencia

El ToDo Service incluye configuración de Resilience4j:

- **Circuit Breaker**: Se abre si >50% de llamadas fallan en las últimas 5
- **Retry**: Máximo 3 intentos con 2 segundos de espera
- **Health Check**: Integrado con Spring Boot Actuator

## 🧪 Testing

### Backend
```bash
mvn test
```

### Frontend
```bash
ng test
```

## 📊 Monitoreo

- **Eureka Dashboard**: `http://localhost:8761` - Ver servicios registrados
- **Actuator Endpoints**: Disponibles en cada servicio (ej: `http://localhost:8080/actuator/health`)
- **Circuit Breaker Metrics**: `/actuator/health` incluye estado del circuit breaker

## 🐛 Solución de Problemas

### Problemas Comunes

1. **Config Server no encuentra archivos**: Verificar la ruta en `application.properties`
2. **Servicios no se registran en Eureka**: Verificar configuración de Eureka en `config-repo`
3. **Error de CORS**: Verificar configuración en API Gateway
4. **Token inválido**: Verificar `issuer-uri` en servicios resource server

### Logs
- Logs de aplicación en archivos `.log` en cada directorio de servicio
- Logs de OAuth2 en `oauth2-server.log`

## 🤝 Contribución

Como este es un proyecto personal de práctica, las contribuciones son bienvenidas pero no esperadas. Si encuentras algo útil o quieres mejorar algo:

1. Fork el proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la Licencia MIT.

## 📞 Contacto

**Marco Villanueva**
- GitHub: [Marco-Villanueva20](https://github.com/Marco-Villanueva20)
- Proyecto: [ToDo List Microservices](https://github.com/Marco-Villanueva20/to-do-list-microservices.git)

Si tienes preguntas sobre la implementación o quieres discutir aspectos técnicos del proyecto, no dudes en contactarme.

---

**Nota**: Asegúrate de que todos los servicios estén ejecutándose antes de probar la aplicación completa. La arquitectura de microservicios requiere que todos los componentes estén disponibles para el funcionamiento correcto.
