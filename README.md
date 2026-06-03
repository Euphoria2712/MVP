# KUANTO

Plataforma de inteligencia artificial enfocada en recetas, comparación de precios y localización de supermercados mediante arquitectura de microservicios.

---

# Tecnologías

* Java 17
* Spring Boot 3
* Spring Cloud
* Maven
* MySQL
* MongoDB
* OpenRouter API
* JWT
* Eureka Server
* API Gateway
* OpenFeign

---

# Arquitectura

El proyecto utiliza una arquitectura basada en microservicios.

| Servicio          | Puerto | Función                    |
| ----------------- | ------ | -------------------------- |
| config-server     | 8888   | Configuración centralizada |
| eureka-server     | 8761   | Service Discovery          |
| api-gateway       | 8080   | Gateway principal          |
| user-service      | 8081   | Usuarios y autenticación   |
| ai-service        | 8082   | Integración IA             |
| intent-service    | 8083   | Orquestador                |
| location-service  | 8085   | GPS y sucursales           |
| recipe-service    | 8086   | Catálogo de recetas        |
| response-service  | 8087   | Respuesta final            |
| freshmart-service | 8088   | Comparación de precios     |

---

# Requisitos

Antes de ejecutar el proyecto debes tener instalado:

* Java 17
* Maven 3.8+
* MySQL 8
* MongoDB 6+
* Git

---

# Clonar repositorio

```bash
git clone https://github.com/Euphoria2712/MVP.git
cd MVP
```

---

# Configuración

## 1. Crear bases de datos MySQL

```sql
CREATE DATABASE kuanto_users;
CREATE DATABASE kuanto_ai;
```

---

## 2. Configurar OpenRouter

Editar:

```plaintext
config-server/src/main/resources/configs/ai-service.yml
```

Agregar tu API Key:

```yml
openrouter:
  api-key: TU_API_KEY
```

---

# Orden de ejecución

Los servicios deben levantarse en este orden:

1. config-server
2. eureka-server
3. api-gateway
4. user-service
5. ai-service
6. recipe-service
7. freshmart-service
8. location-service
9. intent-service
10. response-service

---

# Ejecutar servicios

Desde cada microservicio:

```bash
./mvnw spring-boot:run
```

o

```bash
mvn spring-boot:run
```

---

# Verificar Eureka

Abrir:

```plaintext
http://localhost:8761
```

Todos los servicios deben aparecer como `UP`.

---

# Registrar Usuario
```http
POST http://localhost:8081/api/auth/register
```

```plaintext
  "nombre": "nombre",
  "apellido": "apelllido",
  "email": "nombre@test.cl",
  "password": "mi_password_segura",
  "ciudad": "Santiago",
  "presupuesto": "medio",
  "supermercadoFav": "Lider"
```
# Iniciar Sesion
```http
POST http://localhost:8080/api/auth/login
```
```plaintext
  "email": "nombre@test.cl",
  "password": "mi_password_segura"
```

---

# Endpoint principal

```http
POST http://localhost:8080/api/response
```

Header:

```Header
Content-Type : application/json
X-User-Id: 00000000-0000-0000-0000-000000000001
Authorization : Bearer Token-Aqui
```

Body:

```json
{
  "mensaje": "quiero hacer un pie de limón",
  "userLat": -33.3617,
  "userLng": -70.7292,
  "radiusKm": 15
}
```

---

# Problemas comunes

## 503 Service Unavailable

Verificar que todos los servicios estén levantados en Eureka.

---

## OpenRouter 429

El modelo gratuito alcanzó el límite de requests.

---

## jwt.secret error

Levantar `config-server` antes del resto.

---

# Estado del proyecto

MVP funcional desarrollado con arquitectura de microservicios utilizando Spring Boot, Spring Cloud y OpenFeign.
