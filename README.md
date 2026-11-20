# EcommerceAppMicroService

# EcommerceAppMicroService — Comprehensive Project Analysis

> **Analysis date:** 2026-11-20  
> **Root path:** `C:\Users\dhruv\Java\EcommerceAppMicroService`  
> **Legend:** ✅ Verified from source code/config | ⚠️ Risk / inconsistency | 🔍 Inferred from context

---

## Table of Contents

1. [Repository Overview](#1-repository-overview)
2. [Complete File & Folder Inventory](#2-complete-file--folder-inventory)
3. [Infrastructure Services (Docker)](#3-infrastructure-services-docker)
4. [Core Microservices](#4-core-microservices)
   - 4.1 [Config Server](#41-config-server)
   - 4.2 [Eureka Discovery Server](#42-eureka-discovery-server)
   - 4.3 [API Gateway](#43-api-gateway)
   - 4.4 [User Service](#44-user-service)
   - 4.5 [Product Service](#45-product-service)
   - 4.6 [Order Service](#46-order-service)
   - 4.7 [Notification Service](#47-notification-service)
5. [Additional / Experimental Modules](#5-additional--experimental-modules)
   - 5.1 [OAuth2 Auth-Code Flow Demo](#51-oauth2-auth-code-flow-demo)
   - 5.2 [Kafka Producer/Consumer Demo](#52-kafka-producerconsumer-demo)
   - 5.3 [Loki Fundamentals (Greenhouse App)](#53-loki-fundamentals-greenhouse-app)
   - 5.4 [Prometheus Fundamentals](#54-prometheus-fundamentals)
6. [Inter-Service Communication Map](#6-inter-service-communication-map)
7. [End-to-End Workflows](#7-end-to-end-workflows)
8. [Observability Stack](#8-observability-stack)
9. [Security Architecture](#9-security-architecture)
10. [Persistence & Data Layer](#10-persistence--data-layer)
11. [Messaging Architecture](#11-messaging-architecture)
12. [Postman Workspace Artifacts](#12-postman-workspace-artifacts)
13. [Auxiliary, Generated, and Log Files](#13-auxiliary-generated-and-log-files)
14. [Risks, Inconsistencies & Missing Pieces](#14-risks-inconsistencies--missing-pieces)
15. [Architecture Summary Diagram](#15-architecture-summary-diagram)

---

## 1. Repository Overview

This is a **Spring Boot 4.1.2 / Spring Cloud 2025.1.4** microservices e-commerce application. It is structured as a **monorepo** containing:

- **7 core Spring Boot microservices** (Config Server, Eureka, Gateway, User, Product, Order, Notification)
- **Infrastructure** managed via a root `docker-compose.yml` (PostgreSQL, pgAdmin, RabbitMQ, Redis, Kafka/Zookeeper, Keycloak)
- **3 additional learning/experimental modules** (OAuth2 demo, Kafka demo, Loki/Prometheus observability demos)
- **Postman workspace** scaffolding (mostly empty)
- **Root-level log files** for three services

All Java services use **Java 17**, **Lombok**, **Spring Cloud Config** (native profile), **Spring Cloud Netflix Eureka**, **Spring Cloud Bus (AMQP)**, **Micrometer + Zipkin** distributed tracing, and **Prometheus** metrics.

---

## 2. Complete File & Folder Inventory

```
EcommerceAppMicroService/
├── .gitignore                          # Ignores *.class, *.log, *.jar, *.war, etc.
├── LICENSE                             # Project license
├── README.md                           # Minimal (only contains project title)
├── docker-compose.yml                  # Infrastructure containers (see §3)
│
├── configserver/configserver/          # Spring Cloud Config Server (port 8888)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   ├── HELP.md
│   ├── src/main/java/com/demo/configserver/
│   │   └── ConfigserverApplication.java
│   └── src/main/resources/
│       ├── application.yaml
│       └── config/                     # Served config files (native profile)
│           ├── gateway-service.yaml
│           ├── order-service.yaml
│           ├── product-service.yaml
│           └── user-service.yaml
│
├── eureka/eureka/                      # Netflix Eureka Server (port 8761)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   ├── HELP.md
│   └── src/main/java/com/demo/eureka/
│       └── EurekaApplication.java
│
├── gateway/gateway/                    # Spring Cloud Gateway (port 8080)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   ├── HELP.md
│   └── src/main/java/com/ecommerce/gateway/
│       ├── GatewayApplication.java
│       ├── GatewayConfig.java          # Route definitions + Redis rate limiter
│       ├── JwtAuthFilter.java          # JWT filter (DISABLED — @Component commented out)
│       └── LoggingFilter.java          # Global logging filter (ACTIVE)
│
├── user/user/                          # User Service (port 8082)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   ├── HELP.md
│   └── src/main/java/com/ecommerce/user/
│       ├── UserApplication.java
│       ├── controller/UserController.java
│       ├── service/UserService.java
│       ├── repository/UserRepository.java
│       ├── model/User.java, Address.java, UserRole.java
│       └── dto/UserRequest.java, UserResponse.java, AddressDTO.java
│
├── product/product/                    # Product Service (port 8081)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   ├── HELP.md
│   └── src/main/java/com/ecommerce/product/
│       ├── ProductApplication.java
│       ├── controller/ProductController.java
│       ├── service/ProductService.java
│       ├── repository/ProductRepository.java
│       ├── model/Product.java
│       └── dto/ProductRequest.java, ProductResponse.java
│
├── order/order/                        # Order Service (port 8083)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   ├── HELP.md
│   ├── logs/order-service.log          # Local log file (gitignored)
│   └── src/main/java/com/ecommerce/order/
│       ├── OrderApplication.java
│       ├── controller/OrderController.java, CartController.java
│       ├── service/OrderService.java, CartService.java
│       ├── repository/OrderRepository.java, CartItemRepositry.java  [typo]
│       ├── model/Order.java, OrderItem.java, CartItem.java, OrderStatus.java
│       ├── dto/OrderResponse.java, OrderItemDTO.java, CartItemRequest.java,
│       │       OrderCreatedEvent.java, ProductResponse.java, UserResponse.java,
│       │       AddressDTO.java, UserRole.java
│       ├── config/RabbitMQConfiguration.java
│       └── cleints/  [typo — should be "clients"]
│           ├── ProductServiceClient.java
│           ├── ProductServiceClientConfig.java
│           ├── UserServiceClient.java
│           └── UserServiceClientConfig.java
│
├── notification/notification/          # Notification Service (port 8084)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   ├── HELP.md
│   └── src/main/java/com/ecommerce/notification/
│       ├── NotificationApplication.java
│       ├── OrderEventConsumer.java     # RabbitMQ listener
│       └── RabbitMQConfiguration.java
│
├── logs/                               # Root-level rolling log files (gitignored)
│   ├── order-service.log
│   ├── order-service.log.2026-11-07.0.gz … .2026-11-13.0.gz  (7 archived files)
│   ├── product-service.log
│   ├── product-service.log.2026-11-07.0.gz … .2026-11-13.0.gz (7 archived files)
│   ├── user-service.log
│   └── user-service.log.2026-11-07.0.gz … .2026-11-13.0.gz   (7 archived files)
│
├── postman/                            # Postman workspace artifacts
│   ├── collections/                    # (empty)
│   ├── documents/                      # (empty)
│   ├── environments/                   # (empty)
│   ├── flows/                          # (empty)
│   ├── globals/workspace.globals.yaml  # Empty globals (values: [])
│   ├── mocks/                          # (empty)
│   └── specs/                          # (empty)
│
└── additional/                         # Standalone learning/demo modules
    ├── authcodeflow/
    │   ├── authcodeflow/               # OAuth2 client-credentials client (port 8088)
    │   │   ├── pom.xml
    │   │   └── src/main/java/com/demo/authcodeflow/
    │   │       ├── AuthcodeflowApplication.java
    │   │       ├── HomeController.java
    │   │       ├── SecurityConfig.java
    │   │       └── Service2Class.java
    │   ├── resource/                   # OAuth2 resource server (port 8089)
    │   │   ├── pom.xml
    │   │   └── src/main/java/com/demo/resource/
    │   │       ├── ResourceApplication.java
    │   │       ├── DataController.java
    │   │       └── SecurityConfig.java
    │   └── target/                     # Stale Maven build artifacts (wrong level)
    │
    ├── kafka/
    │   ├── producer/producer/          # Spring Cloud Stream Kafka producer (port 8085)
    │   │   ├── pom.xml
    │   │   └── src/main/java/com/demo/producer/
    │   │       ├── ProducerApplication.java
    │   │       ├── KafkaProducer.java  # @RestController commented out
    │   │       ├── KafkaProducerStreams.java
    │   │       ├── KafkaTopicConfig.java
    │   │       ├── FunctionClass.java
    │   │       └── RiderLocation.java
    │   └── consumer/consumer/          # Spring Cloud Stream Kafka consumer (port 8086)
    │       ├── pom.xml
    │       └── src/main/java/com/demo/consumer/
    │           ├── ConsumerApplication.java
    │           ├── KafkaConsumer.java  # All @KafkaListener methods commented out
    │           ├── KafkaConsumerNew.java
    │           └── RiderLocation.java
    │
    ├── loki-fundamentals/
    │   ├── config.alloy                # Grafana Alloy log-scraping config
    │   ├── docker-compose.yml          # Alloy + Loki + Grafana stack
    │   ├── loki-config.yaml            # Loki server config
    │   └── greenhouse/                 # Python Flask microservices demo app
    │       ├── dockerfile              # FROM python:3.12
    │       ├── docker-compose-micro.yml
    │       ├── requirements.txt        # Flask, SQLAlchemy, OpenTelemetry, SocketIO, etc.
    │       ├── init.sql                # Creates user_service_db and plant_service_db
    │       ├── main_app.py             # Flask orchestrator (port 5005→5000)
    │       ├── user_service.py         # Flask user service (port 5001)
    │       ├── plant_service.py        # Flask plant service (port 5002)
    │       ├── simulation_service.py   # Flask simulation + SocketIO (port 5003)
    │       ├── websocket_service.py    # Flask WebSocket service (port 5004)
    │       ├── bug_service.py          # Fault-injection service (port 5010)
    │       ├── static/
    │       └── templates/
    │
    └── prometheus-fundamentals/
        ├── docker-compose.yml          # Alloy + Loki + Grafana + Prometheus + Zipkin
        ├── grafana/datasources/
        │   ├── datasources.yml         # Prometheus + Loki datasources (Loki URL is wrong)
        │   └── ds.yaml                 # Loki-only datasource (correct URL)
        ├── logging/
        │   ├── config.alloy            # Alloy log-scraping config (identical to loki-fundamentals)
        │   └── loki-config.yaml        # Loki config (identical to loki-fundamentals)
        └── prometheus/
            └── prometheus.yml          # Scrapes product (8081), user (8082), order (8083)
```

---

## 3. Infrastructure Services (Docker)

### 3.1 Root `docker-compose.yml`

All containers share a single bridge network named **`postgres`**. No core Java microservices are containerized here — they run as local JVM processes.

| Container | Image | Host Port(s) | Purpose | Restart Policy |
|---|---|---|---|---|
| `postgres-container` | `postgres:14` | `5432:5432` | Primary relational DB for product and order services | `unless-stopped` |
| `pgadmin_container` | `dpage/pgadmin4` | `5051:80` | pgAdmin web UI | `unless-stopped` |
| `my-rabbitmq` | `rabbitmq:3-management` | `5672:5672`, `15672:15672` | Message broker for Spring Cloud Bus + order events | `unless-stopped` |
| `redis_cache` | `redis:7.4-alpine` | `6379:6379` | Redis for Gateway rate limiting | `unless-stopped` |
| `zookeeper` | `confluentinc/cp-zookeeper:latest` | `2181:2181` | Kafka coordination | (none) |
| `kafka` | `confluentinc/cp-kafka:7.5.0` | `9092:9092` | Kafka broker (Kafka demo only) | (none) |
| `keycloak-server` | `quay.io/keycloak/keycloak:latest` | `8443:8080` | Identity provider (OAuth2 demo) | (none) |

**Volumes declared:** `postgres`, `pgadmin`, `rabbitmq`, `keycloak`

**Key observations:**
- ✅ PostgreSQL uses a named volume `postgres` for data persistence.
- ✅ pgAdmin credentials are configurable via env vars `PGADMIN_DEFAULT_EMAIL` / `PGADMIN_DEFAULT_PASSWORD` with defaults.
- ✅ RabbitMQ credentials: `user` / `password` (hardcoded in compose and all service configs).
- ⚠️ Kafka `KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092` — Kafka is only reachable from the **host machine**, not from other Docker containers. Any containerized service connecting to Kafka will fail.
- ⚠️ Keycloak runs in `start-dev` mode (not production-safe). Bootstrap admin credentials set via `KC_BOOTSTRAP_ADMIN_USERNAME` / `KC_BOOTSTRAP_ADMIN_PASSWORD`.
- ⚠️ **MySQL** service is fully commented out; PostgreSQL is the active choice.
- ⚠️ **No Dockerfiles exist** for any of the core Java microservices. They are run locally only.
- ⚠️ Keycloak is not configured to use PostgreSQL (those lines are commented out), so it uses its embedded H2 database — data is lost on container restart.
- ⚠️ No health checks defined for any container.
- ⚠️ No startup ordering (`depends_on`) between infrastructure containers.
- ⚠️ `rabbitmq` and `keycloak` volumes are declared but the containers do not mount them — the volume declarations are unused.

### 3.2 `additional/loki-fundamentals/docker-compose.yml`

Standalone observability stack: **Grafana Alloy** (ports 12345, 4317, 4318) + **Loki** (port 3100) + **Grafana** (port 3000). Alloy mounts `../../logs` (the root `logs/` directory) read-only as `/logs-parent` to scrape service log files. Network: `loki`.

### 3.3 `additional/prometheus-fundamentals/docker-compose.yml`

Extended observability stack: Alloy + Loki + Grafana + **Prometheus** (port 9090) + **Zipkin** (port 9411). Prometheus scrapes `/actuator/prometheus` on ports 8081, 8082, 8083 via `host.docker.internal`. Network: `loki`.

### 3.4 `additional/loki-fundamentals/greenhouse/docker-compose-micro.yml`

Python Flask microservices demo. Attaches to the external network `loki-fundamentals_loki` (must be created by running the parent `loki-fundamentals/docker-compose.yml` first). Runs 6 Python services + PostgreSQL 12 with a health check.

---

## 4. Core Microservices

### 4.1 Config Server

| Property | Value |
|---|---|
| **Artifact** | `com.demo:configserver:0.0.1-SNAPSHOT` |
| **Main class** | `com.demo.configserver.ConfigserverApplication` |
| **Annotations** | `@SpringBootApplication`, `@EnableConfigServer` |
| **Port** | `8888` |
| **Spring profile** | `native` (serves config from local filesystem) |
| **Config search path** | `file:///C:\Users\dhruv\Java\EcommerceAppMicroService\configserver\configserver\src\main\resources\config` |
| **Eureka** | Registers with Eureka at `https://localhost:8761` |
| **Spring Cloud Bus** | Connected to RabbitMQ (`localhost:5672`, `user`/`password`) for config refresh broadcasts |
| **Encryption** | `encrypt.key` read from environment variable `${KEY}` |
| **Actuator** | All endpoints exposed; shutdown enabled |

**Served configuration files:**

| File | Target Service | Key Settings |
|---|---|---|
| `gateway-service.yaml` | `gateway-service` | Routes, port 8080, Zipkin endpoint `http://localhost:9411/api/v2/spans` |
| `order-service.yaml` | `order-service` | PostgreSQL `orderdb`, RabbitMQ, Resilience4j, port 8083, Zipkin, log file path |
| `product-service.yaml` | `product-service` | PostgreSQL `productdb`, RabbitMQ, port 8081, Zipkin, log file path |
| `user-service.yaml` | `user-service` | MongoDB Atlas URI (cloud, contains credentials), RabbitMQ, port 8082, Zipkin, log file path |

⚠️ **Critical:** The config search path is an **absolute Windows path hardcoded** in `application.yaml`. This will fail on any other machine or in a Docker container.

⚠️ **Critical:** The MongoDB Atlas URI in `user-service.yaml` contains a **plaintext connection string with embedded credentials**. This should be encrypted using the `encrypt.key` mechanism or externalized to environment variables.

⚠️ **High:** The Eureka URL in `configserver/application.yaml` uses `https://localhost:8761` but Eureka runs plain HTTP. This will cause TLS handshake failures when Config Server tries to register with Eureka.

---

### 4.2 Eureka Discovery Server

| Property | Value |
|---|---|
| **Artifact** | `com.demo:eureka:0.0.1-SNAPSHOT` |
| **Main class** | `com.demo.eureka.EurekaApplication` |
| **Annotations** | `@SpringBootApplication`, `@EnableEurekaServer` |
| **Port** | `8761` |
| **Self-registration** | Disabled (`register-with-eureka: false`, `fetch-registry: false`) |
| **Package** | `com.demo.eureka` (different from `com.ecommerce.*` used by other services) |

✅ Standard standalone Eureka server. No custom configuration beyond port and self-registration suppression. No security configured — the dashboard is open.

---

### 4.3 API Gateway

| Property | Value |
|---|---|
| **Artifact** | `com.ecommerce:gateway:0.0.1-SNAPSHOT` |
| **Main class** | `com.ecommerce.gateway.GatewayApplication` |
| **Port** | `8080` (from Config Server `gateway-service.yaml`) |
| **Framework** | Spring Cloud Gateway (WebFlux / reactive) |
| **Config import** | `optional:configserver:http://localhost:8888` |
| **Eureka client** | Registered; uses `lb://` URIs for load-balanced routing |
| **Dependencies** | `spring-cloud-starter-gateway-server-webflux`, `spring-cloud-starter-netflix-eureka-client`, `spring-boot-starter-data-redis-reactive`, `spring-cloud-starter-config`, `spring-boot-starter-zipkin` |

**Route Table** (defined in **both** `GatewayConfig.java` Java DSL and `gateway-service.yaml` YAML — duplicated):

| Route ID | Path Pattern | Target |
|---|---|---|
| `product-service` | `/api/products/**` | `lb://PRODUCT-SERVICE` |
| `user-service` | `/api/users/**` | `lb://USER-SERVICE` |
| `order-service` | `/api/orders/**`, `/api/cart/**` | `lb://ORDER-SERVICE` |
| `eureka-server` | `/eureka/main` | `http://localhost:8761` (rewrite to `/`) |
| `eureka-server-static` | `/eureka/**` | `http://localhost:8761` |

**Filters & Security:**
- ✅ `LoggingFilter` (`@Component`, active): logs every incoming request path via SLF4J INFO.
- ⚠️ `JwtAuthFilter` (`@Component` **commented out**): JWT Bearer token validation is **disabled**. All routes are currently unauthenticated.
- ✅ `RedisRateLimiter` bean configured (1 req/s per hostname), but the rate limiter filter is **commented out** on the user-service route — rate limiting is not applied anywhere.
- ✅ Zipkin tracing: 100% sampling probability, endpoint `http://localhost:9411/api/v2/spans`.
- ✅ Actuator: all endpoints exposed, shutdown enabled.

⚠️ **High:** Routes are defined in **both** `GatewayConfig.java` (Java DSL) and `gateway-service.yaml` (YAML). Both are active simultaneously, resulting in **duplicate routes** for every path. This can cause double-routing or unexpected behavior.

---

### 4.4 User Service

| Property | Value |
|---|---|
| **Artifact** | `com.ecommerce:user:0.0.1-SNAPSHOT` |
| **Main class** | `com.ecommerce.user.UserApplication` |
| **Port** | `8082` |
| **Database** | MongoDB Atlas (`userdb` database, `user_table` collection) |
| **Eureka** | Registers as `USER-SERVICE` |
| **Config import** | `optional:configserver:http://localhost:8888` |
| **Dependencies** | `spring-boot-starter-data-mongodb`, `spring-cloud-starter-bus-amqp`, `spring-boot-starter-zipkin`, `spring-boot-starter-actuator`, `micrometer-registry-prometheus`, `spring-cloud-starter-config`, `spring-cloud-starter-netflix-eureka-client` |

**Controller:** `UserController` — `@RequestMapping("/api/users")`, `@RefreshScope`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{id}` | Get user by MongoDB `_id` (String) |
| `POST` | `/api/users` | Create user (default role: `CUSTOMER`) |
| `PUT` | `/api/users/{id}` | Update user fields |

**Domain Model:**
- `User` (`@Document(collection = "user_table")`): `id` (String/ObjectId), `firstName` (JSON: `firstname`), `lastname`, `email` (unique indexed), `phone`, `role` (enum: `CUSTOMER`/`ADMIN`, default `CUSTOMER`), `address` (embedded `Address`), `createdAt`, `updatedAt`
- `Address`: embedded document — `street`, `city`, `state`, `country`, `zipcode`. Also has an unused `Long id` field (leftover from JPA era).
- `UserRole`: enum `{CUSTOMER, ADMIN}`

**Repository:** `UserRepository extends MongoRepository<User, String>`

**Key observations:**
- ✅ `@RefreshScope` on controller enables live config refresh via Spring Cloud Bus.
- ✅ Extensive commented-out JPA/PostgreSQL code — service was migrated from PostgreSQL to MongoDB Atlas.
- ✅ `micrometer-registry-prometheus` for Prometheus metrics at `/actuator/prometheus`.
- ⚠️ `Address.id` (Long) is a leftover from JPA and serves no purpose in MongoDB.
- ⚠️ No input validation annotations (`@Valid`, `@NotNull`, etc.) on request DTOs.
- ⚠️ No DELETE endpoint for users.
- ⚠️ `spring-boot-h2console` dependency present but H2 is not used (MongoDB is active).
- ⚠️ `UserRequest` does not include a `role` field — role cannot be set on creation (always defaults to `CUSTOMER`).

---

### 4.5 Product Service

| Property | Value |
|---|---|
| **Artifact** | `com.ecommerce:product:0.0.1-SNAPSHOT` |
| **Main class** | `com.ecommerce.product.ProductApplication` |
| **Port** | `8081` |
| **Database** | PostgreSQL (`productdb` on `localhost:5432`, user `dhruvamalik05`) |
| **Eureka** | Registers as `PRODUCT-SERVICE` |
| **Config import** | `optional:configserver:http://localhost:8888` |
| **Dependencies** | `spring-boot-starter-data-jpa`, `postgresql`, `spring-cloud-starter-bus-amqp`, `spring-boot-starter-zipkin`, `spring-boot-starter-actuator`, `micrometer-registry-prometheus`, `spring-cloud-starter-config`, `spring-cloud-starter-netflix-eureka-client` |

**Controller:** `ProductController` — `@RequestMapping("/api/products")`, `@RefreshScope`

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/products` | Create product |
| `PUT` | `/api/products/{id}` | Update product |
| `GET` | `/api/products` | List all active products |
| `GET` | `/api/products/{id}` | Get active product by ID |
| `DELETE` | `/api/products/{id}` | Soft-delete (sets `active=false`) |
| `GET` | `/api/products/search?keyword=` | Search by name (active + in-stock, case-insensitive) |

**Domain Model:**
- `Product` (`@Entity(name = "product_table")`): `id` (Long, auto-increment), `name`, `description`, `price` (BigDecimal), `stockQuantity` (Integer), `category`, `imageUrl`, `active` (Boolean, default `true`), `createdAt`, `updatedAt`

**Repository:** `ProductRepository extends JpaRepository<Product, Long>`
- `findByActiveTrue()` — returns `Collection<Object>` ⚠️ (type-safety bug; should be `List<Product>`)
- `searchProduct(keyword)` — JPQL case-insensitive LIKE on `name`, filters `active=true` and `stockQuantity > 0`
- `findByIdAndActiveTrue(Long id)`

**Key observations:**
- ✅ Soft-delete pattern (products are never physically deleted).
- ✅ `micrometer-registry-prometheus` for Prometheus metrics.
- ⚠️ `findByActiveTrue()` returns `Collection<Object>` — causes an unchecked cast in `ProductService.getProduct()`.
- ⚠️ `updateProduct()` calls `.orElseThrow()` with no exception message — throws `NoSuchElementException` if product not found.
- ⚠️ No stock quantity management (no decrement on order placement).
- ⚠️ Commented-out MySQL dependency — migrated to PostgreSQL.

---

### 4.6 Order Service

| Property | Value |
|---|---|
| **Artifact** | `com.ecommerce:order:0.0.1-SNAPSHOT` |
| **Main class** | `com.ecommerce.order.OrderApplication` |
| **Port** | `8083` |
| **Database** | PostgreSQL (`orderdb` on `localhost:5432`, user `dhruvamalik05`) |
| **Eureka** | Registers as `ORDER-SERVICE` |
| **Config import** | `optional:configserver:http://localhost:8888` |
| **Dependencies** | `spring-boot-starter-data-jpa`, `postgresql`, `spring-cloud-starter-bus-amqp`, `spring-boot-starter-zipkin`, `spring-boot-starter-actuator`, `micrometer-registry-prometheus`, `spring-cloud-starter-config`, `spring-cloud-starter-netflix-eureka-client`, `spring-cloud-starter-circuitbreaker-resilience4j`, `resilience4j-spring-boot4:2.4.0` |

**Controllers:**

`CartController` — `@RequestMapping("/api/cart")`, `@RefreshScope`

| Method | Path | Required Header | Description |
|---|---|---|---|
| `POST` | `/api/cart` | `X-User-ID` | Add item to cart |
| `DELETE` | `/api/cart/{productId}` | `X-User-ID` | Remove item from cart |
| `GET` | `/api/cart` | `X-User-ID` | Get cart contents |

`OrderController` — `@RequestMapping("/api/orders")`, `@RefreshScope`

| Method | Path | Required Header | Description |
|---|---|---|---|
| `POST` | `/api/orders` | `X-User-ID` | Create order from current cart |

**Domain Models:**
- `Order` (`@Entity(name = "order_table")`): `id` (Long), `userId` (String), `totalAmount` (BigDecimal), `orderStatus` (enum, default `PENDING`), `items` (OneToMany → `OrderItem`, cascade ALL), `createdAt`, `updatedAt`
- `OrderItem` (`@Entity`): `id` (Long), `productId` (String), `quantity` (Integer), `price` (BigDecimal), `order` (ManyToOne)
- `CartItem` (`@Entity`): `id` (Long), `userId` (String), `productId` (String), `quantity` (Integer), `price` (BigDecimal), `createdAt`, `updateAt` [typo in field name]
- `OrderStatus`: enum `{PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED}`

**Service Logic:**

`CartService.addToCart(userId, cartItemRequest)`:
1. Calls `ProductServiceClient.getProductById(productId)` → HTTP GET `lb://PRODUCT-SERVICE/api/products/{id}`
2. Validates `product.stockQuantity >= requested quantity`
3. Calls `UserServiceClient.getUserById(userId)` → HTTP GET `lb://USER-SERVICE/api/users/{id}`
4. Upserts `CartItem` in PostgreSQL (increments quantity if item already exists)
5. Publishes `Map{orderId: productId, status: "Added to Cart"}` to RabbitMQ exchange `order.exchange`, routing key `order.tracking`

`OrderService.createOrder(userId)`:
1. Fetches cart items via `CartService.getCart(userId)` → SELECT from `cart_item` WHERE `userId=?`
2. Calculates `totalAmount` by summing `CartItem.price`
3. Creates `Order` with status `CONFIRMED` and `OrderItem` list
4. Saves to PostgreSQL (`order_table`, `order_item`)
5. Clears cart via `CartService.clearCart(userId)` → DELETE from `cart_item` WHERE `userId=?`
6. Publishes `OrderCreatedEvent{orderId, userId, status, items, totalAmount, createdAt}` to RabbitMQ exchange `order.exchange`, routing key `order.tracking`

**HTTP Clients (Spring 6 `@HttpExchange` / `RestClient`):**
- `ProductServiceClient`: `GET /api/products/{id}` → base URL `http://product-service` (Eureka load-balanced via `@LoadBalanced RestClient.Builder`)
- `UserServiceClient`: `GET /api/users/{id}` → base URL `http://user-service` (Eureka load-balanced)
- Both clients use `defaultStatusHandler` to suppress 4xx errors (returns `Optional.empty()` on 4xx)

**RabbitMQ Configuration (`RabbitMQConfiguration.java`):**
- Exchange: `order.exchange` (TopicExchange, durable) — value from `${rabbitmq.exchange.name}`
- Queue: `order.queue` (durable) — value from `${rabbitmq.queue.name}`
- Routing key: `order.tracking` — value from `${rabbitmq.routing.name}`
- Message converter: `JacksonJsonMessageConverter`

**Resilience4j (configured in `order-service.yaml`, annotations commented out):**
- Circuit breaker `productService`: sliding window 10, min calls 5, failure threshold 50%, wait 10s, auto half-open
- Retry `retryBreaker`: max 1 attempt, 500ms wait
- Rate limiter `retryLimiter`: 2 requests per 4s, 0 timeout

**Key observations:**
- ⚠️ `CartService.removeFromCart()` always returns `false` regardless of success.
- ⚠️ `OrderService.createOrder()` calls `Optional.empty()` without `return` when cart is empty — execution continues instead of short-circuiting.
- ⚠️ `@CircuitBreaker` and `@Retry` annotations on `CartService.addToCart()` are **commented out** — resilience patterns are configured but not applied.
- ⚠️ `CartItemRepositry` — typo in class name (missing 'o').
- ⚠️ `cleints` — typo in package name.
- ⚠️ No stock decrement in Product Service when an order is placed.
- ⚠️ `X-User-ID` header is passed manually by the client — no gateway-level user identity injection (JWT filter is disabled).
- ⚠️ `CartItem.updateAt` — typo in field name (should be `updatedAt`).

---

### 4.7 Notification Service

| Property | Value |
|---|---|
| **Artifact** | `com.ecommerce:notification:0.0.1-SNAPSHOT` |
| **Main class** | `com.ecommerce.notification.NotificationApplication` |
| **Port** | `8084` |
| **Database** | None |
| **Eureka** | **Not registered** (no Eureka client dependency in pom.xml) |
| **Config Server** | **Not connected** (no config import in application.yaml) |
| **Dependencies** | `spring-boot-starter-amqp`, `spring-boot-starter-webmvc` |

**Consumer:** `OrderEventConsumer`
- `@RabbitListener(queues = "${rabbitmq.queue.name}")` → listens on `order.queue`
- Handler: `handleOrderEvent(Map<String, Object> orderEvent)` — currently only calls `System.out.println(orderEvent)`

**RabbitMQ Configuration:** Same exchange/queue/routing key as Order Service (`order.exchange`, `order.queue`, `order.tracking`). `RabbitTemplate` bean is commented out (consumer-only service).

**Key observations:**
- ⚠️ No actual notification logic (email, SMS, push) — only `System.out.println`.
- ⚠️ Not registered with Eureka — cannot be discovered by other services.
- ⚠️ Not connected to Config Server — configuration is fully local in `application.yaml`.
- ⚠️ **Type mismatch:** The consumer expects `Map<String, Object>` but `OrderService` publishes `OrderCreatedEvent` (a POJO). The `addToCart` message (a Map) will deserialize correctly, but the `createOrder` message (POJO serialized as JSON) may fail deserialization into `Map<String, Object>` depending on Jackson's type handling. This is a likely runtime issue.
- ⚠️ No Spring Cloud Bus dependency — cannot receive config refresh events.
- ⚠️ No Micrometer/Prometheus dependency — no metrics exposed.

---

## 5. Additional / Experimental Modules

### 5.1 OAuth2 Auth-Code Flow Demo

Two standalone Spring Boot apps demonstrating OAuth2 **client credentials** flow with Keycloak. These are **learning demos** not integrated with the main ecommerce system.

**`authcodeflow` (port 8088)** — OAuth2 client + resource server:
- Registers Keycloak client `oauth2-client-credentials` with `client_credentials` grant type
- Realm: `oauth-demo`, token URI: `http://localhost:8443/realms/oauth-demo/protocol/openid-connect/token`
- `HomeController.GET /proxy` → calls `Service2Class.fetchData()` which obtains a token and calls `http://localhost:8089/data`
- `SecurityConfig` also configures this app as a resource server (validates JWT from same Keycloak realm)
- `CommandLineRunner` in `SecurityConfig` fetches a token and calls `/data` on startup (demo behavior)
- `service2.url=http://localhost:8089`

**`resource` (port 8089)** — OAuth2 resource server:
- `DataController.GET /data` → returns `"Hello from service resource"`
- `SecurityConfig`: all requests require authentication, validated via JWT from `http://localhost:8443/realms/oauth-demo`

⚠️ Client secret is stored in plaintext in `application.properties`.

---

### 5.2 Kafka Producer/Consumer Demo

Two standalone Spring Boot apps demonstrating **Spring Cloud Stream** with Kafka. **Learning demos** not integrated with the main ecommerce system.

**`producer` (port 8085):**
- `KafkaProducerStreams`: `Supplier<RiderLocation> sendRiderLocation` — Spring Cloud Stream polls this supplier and publishes `RiderLocation{riderId="rider123", lat=16.7, lon=12.3}` to topic `my-topic-new` automatically
- `KafkaProducer`: `POST /api/send` — `@RestController` annotation is commented out (inactive)
- `FunctionClass`: `Function<String,String> upperCase` — HTTP function exposed at `/uppercase` via `spring-cloud-starter-function-web`
- `KafkaTopicConfig`: creates topic `my-new-topic-2` (3 partitions, replication factor 1) — note: different topic name from the stream binding
- Broker: `localhost:9092`

**`consumer` (port 8086):**
- `KafkaConsumerNew`: `Consumer<RiderLocation> processRiderLocation` — receives from `my-topic-new`, consumer group `rider-location-consumers`, prints to stdout
- `KafkaConsumer`: all `@KafkaListener` methods commented out (inactive)

---

### 5.3 Loki Fundamentals (Greenhouse App)

A Python Flask microservices demo application for learning Grafana Loki log aggregation. All services are built from the same `dockerfile` (`FROM python:3.12`).

**Services:**

| Service | Port | Responsibility | Database |
|---|---|---|---|
| `main_app` | 5005→5000 | Flask orchestrator; routes to all other services | None |
| `user_service` | 5001 | User signup/login/logout with password hashing | PostgreSQL `user_service_db` |
| `plant_service` | 5002 | Plant CRUD; triggers simulation on add | PostgreSQL `plant_service_db` |
| `simulation_service` | 5003 | Real-time plant sensor simulation via SocketIO threads | None |
| `websocket_service` | 5004 | WebSocket gateway for plant add events | None |
| `bug_service` | 5010 | Fault injection — randomly triggers bugs in other services every 10s | None |
| `db` | 5432 | PostgreSQL 12 with health check (`pg_isready`) | — |

**Key features:**
- OpenTelemetry instrumentation (`opentelemetry-exporter-otlp`) for traces
- Flask-SocketIO for real-time plant sensor updates
- `bug_service` demonstrates intentional fault injection for observability training — when `bug_mode=true`, it calls `/trigger_bug` on a random service every 10 seconds
- Attaches to external network `loki-fundamentals_loki` (must run parent compose first)
- `init.sql` creates `user_service_db` and `plant_service_db` databases on PostgreSQL startup

---

### 5.4 Prometheus Fundamentals

Observability stack for the main Java microservices:

- **Prometheus** (`prom/prometheus:v2.44.0`) scrapes `/actuator/prometheus` on product (8081), user (8082), order (8083) every 3 seconds via `host.docker.internal`
- **Grafana** (`grafana/grafana:11.6.0`) provisioned with both Prometheus and Loki datasources; anonymous admin access enabled
- **Zipkin** (`openzipkin/zipkin:latest`) for distributed tracing (port 9411)
- **Grafana Alloy** (`grafana/alloy:v1.7.5`) for log collection from `../../logs` directory

⚠️ `datasources.yml` references `http://gateway:3100` for Loki — incorrect; Loki container is named `loki`, not `gateway`. The `ds.yaml` file correctly uses `http://loki:3100`. The `datasources.yml` file will fail to connect to Loki.

---

## 6. Inter-Service Communication Map

```
                    ┌─────────────────────────────────────────────────────┐
                    │              Spring Cloud Config Server              │
                    │                   (port 8888)                        │
                    │         Serves: gateway, user, product, order        │
                    └──────────────────────┬──────────────────────────────┘
                                           │ HTTP (config fetch on startup)
                    ┌──────────────────────▼──────────────────────────────┐
                    │              Eureka Discovery Server                 │
                    │                   (port 8761)                        │
                    └──────────────────────┬──────────────────────────────┘
                                           │ Registration
              ┌────────────────────────────┼────────────────────────────┐
              │                            │                            │
    ┌─────────▼──────────┐    ┌────────────▼──────────┐   ┌────────────▼──────────┐
    │   Product Service  │    │    User Service        │   │   Order Service       │
    │   (port 8081)      │    │    (port 8082)         │   │   (port 8083)         │
    │   PostgreSQL:      │    │    MongoDB Atlas:      │   │   PostgreSQL:         │
    │   productdb        │    │    userdb              │   │   orderdb             │
    └────────────────────┘    └────────────────────────┘   └────────────┬──────────┘
              ▲                          ▲                               │
              │ lb://PRODUCT-SERVICE     │ lb://USER-SERVICE             │ AMQP publish
              │ GET /api/products/{id}   │ GET /api/users/{id}           │ exchange: order.exchange
              └──────────────────────────┴───────────────────────────────┘
                                                                         │
                                                              ┌──────────▼──────────┐
                                                              │ Notification Service │
                                                              │   (port 8084)        │
                                                              │ AMQP consumer        │
                                                              │ queue: order.queue   │
                                                              └─────────────────────┘

External Client
     │
     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         API Gateway (port 8080)                              │
│  /api/products/** → lb://PRODUCT-SERVICE                                     │
│  /api/users/**    → lb://USER-SERVICE                                        │
│  /api/orders/**   → lb://ORDER-SERVICE                                       │
│  /api/cart/**     → lb://ORDER-SERVICE                                       │
│  /eureka/**       → http://localhost:8761                                    │
└─────────────────────────────────────────────────────────────────────────────┘

Spring Cloud Bus (RabbitMQ):
  Config Server ──AMQP──► Gateway, User, Product, Order (config refresh broadcast)
```

---

## 7. End-to-End Workflows

### 7.1 User Registration
```
Client → POST /api/users (via Gateway :8080)
  → Gateway routes to lb://USER-SERVICE
  → UserController.createuser()
  → UserService.adduser()
    → Sets role = CUSTOMER (default)
    → Maps UserRequest → User entity
  → UserRepository.save() → MongoDB Atlas (userdb.user_table)
  ← 200 "Successfully added"
```

### 7.2 Product Creation
```
Client → POST /api/products (via Gateway :8080)
  → Gateway routes to lb://PRODUCT-SERVICE
  → ProductController.createProduct()
  → ProductService.createProduct()
    → Maps ProductRequest → Product entity (active=true)
  → ProductRepository.save() → PostgreSQL (productdb.product_table)
  ← 200 ProductResponse{id, name, price, stockQuantity, category, ...}
```

### 7.3 Add to Cart
```
Client → POST /api/cart (via Gateway :8080)
         Headers: X-User-ID: <userId>
         Body: {productId: Long, quantity: Integer}
  → Gateway routes to lb://ORDER-SERVICE
  → CartController.addToCart()
  → CartService.addToCart()
    → ProductServiceClient.getProductById(productId)
        → HTTP GET http://product-service/api/products/{id}  [Eureka lb]
        → Returns ProductResponse or null on 4xx
    → Validate: product != null AND product.stockQuantity >= quantity
    → UserServiceClient.getUserById(userId)
        → HTTP GET http://user-service/api/users/{id}  [Eureka lb]
        → Returns UserResponse or null on 4xx
    → Validate: user != null
    → Upsert CartItem in PostgreSQL (orderdb.cart_item)
        → If exists: increment quantity, recalculate price
        → If new: insert with price = product.price * quantity
    → RabbitMQ publish:
        exchange=order.exchange, key=order.tracking
        body=Map{orderId: productId, status: "Added to Cart"}
  ← 200 "Successfully added to cart"
     OR 400 "Product out of stock or product not found or user not found"
```

### 7.4 Place Order
```
Client → POST /api/orders (via Gateway :8080)
         Headers: X-User-ID: <userId>
  → Gateway routes to lb://ORDER-SERVICE
  → OrderController.createOrder()
  → OrderService.createOrder()
    → CartService.getCart(userId)
        → SELECT * FROM cart_item WHERE userId=?
    → Calculate totalAmount = SUM(cartItem.price)
    → Create Order{userId, status=CONFIRMED, totalAmount}
    → Create OrderItem list from CartItems
    → OrderRepository.save(order) → PostgreSQL (orderdb.order_table + order_item)
    → CartService.clearCart(userId)
        → DELETE FROM cart_item WHERE userId=?
    → RabbitMQ publish:
        exchange=order.exchange, key=order.tracking
        body=OrderCreatedEvent{orderId, userId, status=CONFIRMED, items, totalAmount, createdAt}
  ← 200 OrderResponse{id, totalAmount, status, items, createdAt}

Async (RabbitMQ):
  → Notification Service OrderEventConsumer receives from order.queue
  → System.out.println(event)  [no real notification sent]
```

### 7.5 Config Refresh
```
Admin → POST /actuator/busrefresh (on any service or Config Server)
  → Spring Cloud Bus broadcasts refresh event via RabbitMQ
  → All @RefreshScope beans in Gateway, User, Product, Order reload config from Config Server
```

### 7.6 OAuth2 Client Credentials Demo (additional)
```
authcodeflow app startup:
  → CommandLineRunner runs
  → OAuth2AuthorizedClientManager.authorize("keycloak-client")
      → POST http://localhost:8443/realms/oauth-demo/protocol/openid-connect/token
         body: client_id=oauth2-client-credentials, client_secret=..., grant_type=client_credentials
      → Returns JWT access token
  → GET http://localhost:8089/data  [with Bearer token]
      → resource app validates JWT against Keycloak JWKS
      → Returns "Hello from service resource"
  → Prints response to stdout

GET /proxy (runtime):
  → Same flow as above, returns resource response to caller
```

---

## 8. Observability Stack

| Concern | Technology | Endpoint / Config |
|---|---|---|
| **Distributed Tracing** | Micrometer + Zipkin | All services send to `http://localhost:9411/api/v2/spans`; 100% sampling probability |
| **Metrics** | Micrometer + Prometheus | `/actuator/prometheus` on ports 8081, 8082, 8083 |
| **Log Aggregation** | Grafana Loki + Alloy | Alloy scrapes `logs/*.log` files; pushes to Loki `:3100` |
| **Dashboards** | Grafana | Port 3000; anonymous admin access (no auth) |
| **Log Files** | Logback rolling | `logs/${spring.application.name}.log`; 10MB max, 7-day history |
| **Health** | Spring Actuator | All endpoints exposed; circuit breaker health on order service |

**Log configuration** (product, user, order services — from Config Server):
- Root level: `INFO`
- Package-specific: `TRACE` for `com.ecommerce.*`
- Rolling policy: max 10MB per file, 7 days history
- Output path: `logs/${spring.application.name}.log` (relative to working directory)

**Alloy log scraping** (`config.alloy`):
- Docker container logs via Docker socket
- System logs: `/var/log/*.log`
- App logs: `/logs/*.log` (inside container)
- Parent app logs: `/logs-parent/*.log` (mounted from `../../logs` = root `logs/` directory)
- All forwarded to Loki at `http://loki:3100/loki/api/v1/push`
- Static label `env=production` added to all log entries

**Prometheus scrape targets** (`prometheus.yml`):
- `product-service`: `host.docker.internal:8081/actuator/prometheus` every 3s
- `user-service`: `host.docker.internal:8082/actuator/prometheus` every 3s
- `order-service`: `host.docker.internal:8083/actuator/prometheus` every 3s

⚠️ The root `logs/` directory contains log files from previous runs (Nov 7–13, 2026), confirming the services were run from the project root directory. These `.gz` files are gitignored but present on disk.

---

## 9. Security Architecture

### Current State (Production Services)
- ⚠️ **No authentication enforced** on any route. `JwtAuthFilter` is commented out (`//@Component`).
- ⚠️ **No authorization** — any client can call any endpoint without credentials.
- ⚠️ User identity is passed via `X-User-ID` header — trivially spoofable by any client.
- ✅ Redis rate limiter is configured but not applied to any route.
- ✅ Keycloak is running in Docker (port 8443) but not integrated with the gateway or any service.
- ✅ Config Server has `encrypt.key: ${KEY}` — supports symmetric encryption of config values via `/encrypt` and `/decrypt` endpoints.

### OAuth2 Demo (additional/authcodeflow)
- ✅ Demonstrates client credentials flow with Keycloak realm `oauth-demo`
- ✅ Resource server validates JWT from Keycloak JWKS endpoint
- ⚠️ Client secret is stored in plaintext in `application.properties`

### Encryption
- ✅ Config Server has `encrypt.key: ${KEY}` — supports symmetric encryption
- ⚠️ MongoDB Atlas credentials in `user-service.yaml` are **not encrypted** despite the mechanism being available

---

## 10. Persistence & Data Layer

| Service | Database | Type | Schema Strategy | Tables/Collections |
|---|---|---|---|---|
| User | MongoDB Atlas (cloud) | NoSQL | Schema-less | `user_table` |
| Product | PostgreSQL `productdb` | Relational | `ddl-auto: update` | `product_table` |
| Order | PostgreSQL `orderdb` | Relational | `ddl-auto: update` | `order_table`, `order_item`, `cart_item` |
| Notification | None | — | — | — |
| Config Server | None (native FS) | — | — | — |
| Eureka | In-memory | — | — | — |

**PostgreSQL connection details** (from Config Server):
- Host: `localhost:5432`
- Username: `dhruvamalik05` (credentials in plaintext in config files)
- `productdb` and `orderdb` databases must be created manually before startup

⚠️ `ddl-auto: update` is used in both product and order service configs. This can cause data loss or schema corruption on model changes. Should be `validate` in production.

⚠️ Product and Order services share the same PostgreSQL instance but use different databases. These databases must be created manually — there is no init script.

⚠️ User service uses MongoDB Atlas (cloud) — requires network access and valid credentials at startup.

⚠️ PostgreSQL credentials are stored in plaintext in `product-service.yaml` and `order-service.yaml`.

---

## 11. Messaging Architecture

### RabbitMQ (Primary — Order Events)

| Component | Exchange | Queue | Routing Key | Direction |
|---|---|---|---|---|
| Order Service | `order.exchange` (Topic, durable) | `order.queue` (durable) | `order.tracking` | Publisher |
| Notification Service | `order.exchange` (Topic, durable) | `order.queue` (durable) | `order.tracking` | Consumer |

**Messages published by Order Service:**
1. On `addToCart`: `Map{orderId: productId, status: "Added to Cart"}` — note: `orderId` key actually contains the `productId` value (misleading naming)
2. On `createOrder`: `OrderCreatedEvent{orderId, userId, status, items, totalAmount, createdAt}` (POJO serialized as JSON)

⚠️ **Type mismatch:** `OrderEventConsumer` expects `Map<String, Object>` but `OrderService` publishes `OrderCreatedEvent` (a POJO). The `addToCart` message (a Map) will deserialize correctly. The `createOrder` message (POJO) will also deserialize into a Map via Jackson, but the consumer will receive a raw map rather than a typed object — no actual type error, but the consumer cannot access typed fields.

### Spring Cloud Bus (Config Refresh)

All services with `spring-cloud-starter-bus-amqp` use RabbitMQ to propagate config refresh events. Triggering `/actuator/busrefresh` on any service broadcasts to all connected services. Notification Service does **not** participate (no bus dependency).

### Kafka (Demo Only — `additional/kafka`)

The `additional/kafka` module demonstrates Spring Cloud Stream with Kafka. Topic: `my-topic-new`. Broker: `localhost:9092`. Not connected to the main ecommerce system.

---

## 12. Postman Workspace Artifacts

The `postman/` directory contains the Postman workspace scaffold for the **`ecom gateway 2`** workspace (UUID: `6730269e-6f62-4ff0-9bbc-8b9b488613ae`).

| Path | Status | Notes |
|---|---|---|
| `postman/globals/workspace.globals.yaml` | Empty (`values: []`) | No global variables defined |
| `postman/collections/` | Empty | No collections saved to filesystem |
| `postman/environments/` | Empty | No environments saved |
| `postman/flows/` | Empty | No flows |
| `postman/mocks/` | Empty | No mock servers |
| `postman/specs/` | Empty | No API specs |
| `postman/documents/` | Empty | No documents |

**Active Postman tabs** (open in the app at time of analysis):
- `localhost:8080/api/users` — User service via gateway
- `localhost:8080/api/cart` — Cart service via gateway
- `localhost:8080/api/orders` — Order service via gateway
- `localhost:8085/api/send` — Kafka producer send endpoint (inactive — controller commented out)
- `localhost:8085/uppercase` — Kafka producer function endpoint
- `localhost:8088/proxy` — OAuth2 authcodeflow proxy endpoint
- `http://localhost:8083/actuator/refresh` — Order service config refresh

---

## 13. Auxiliary, Generated, and Log Files

| File/Directory | Category | Purpose |
|---|---|---|
| `*/target/` | **Generated** | Maven build output (compiled classes, JARs). Not committed (gitignored). Safe to delete. |
| `configserver/target/`, `order/target/` | **Stale generated** | Stale Maven build artifacts at the wrong directory level (outside the actual module). Likely from running `mvn` in the wrong directory. |
| `additional/authcodeflow/target/` | **Stale generated** | Same issue — stale artifacts at wrong level. |
| `*/mvnw`, `*/mvnw.cmd` | **Build scripts** | Maven wrapper scripts. Allow building without a local Maven install. |
| `*/HELP.md` | **Generated** | Spring Initializr-generated help file. No project-specific content. Can be deleted. |
| `logs/*.log` | **Runtime logs** | Rolling log files from order, product, user services. Gitignored. Active during runtime. |
| `logs/*.log.*.gz` | **Archived logs** | Logback-compressed daily rolled logs (Nov 7–13, 2026). Gitignored. Should not be committed. |
| `order/order/logs/order-service.log` | **Runtime log** | Duplicate log location inside the order module. Gitignored. |
| `.gitignore` (root) | **VCS config** | Ignores `*.class`, `*.log`, `*.jar`, `*.war`, `*.zip`, `*.tar.gz`, `*.rar`. |
| `*/.gitignore` (per-module) | **VCS config** | Standard Spring Boot gitignore per module. |
| `LICENSE` | **Legal** | Project license file. |
| `README.md` | **Documentation** | Minimal — only contains the project title. No setup instructions. |
| `postman/globals/workspace.globals.yaml` | **Postman** | Empty globals file. |
| `additional/loki-fundamentals/greenhouse/static/` | **Web assets** | Static files for the greenhouse Flask app (templates/CSS/JS). |
| `additional/loki-fundamentals/greenhouse/templates/` | **Web assets** | Jinja2 HTML templates for the greenhouse Flask app. |
| `additional/prometheus-fundamentals/grafana/datasources/ds.yaml` | **Duplicate** | Duplicate of the Loki datasource definition; also defined in `datasources.yml`. |

---

## 14. Risks, Inconsistencies & Missing Pieces

### Critical Risks

| # | Risk | Location | Severity |
|---|---|---|---|
| R1 | **No authentication** — `JwtAuthFilter` is commented out; all API routes are open to any caller | `gateway/JwtAuthFilter.java` | 🔴 Critical |
| R2 | **Plaintext MongoDB credentials** in config file committed to repository | `configserver/.../config/user-service.yaml` | 🔴 Critical |
| R3 | **Hardcoded absolute Windows path** in Config Server — breaks on any other machine or in Docker | `configserver/.../application.yaml` | 🔴 Critical |
| R4 | **`ddl-auto: update`** in product and order service configs — risk of schema corruption | `product-service.yaml`, `order-service.yaml` | 🔴 Critical |

### High Risks

| # | Risk | Location | Severity |
|---|---|---|---|
| R5 | **Duplicate gateway routes** — defined in both Java DSL and YAML; both are active simultaneously | `GatewayConfig.java`, `gateway-service.yaml` | 🟠 High |
| R6 | **`OrderService.createOrder()` logic bug** — `Optional.empty()` called without `return` when cart is empty; execution continues | `OrderService.java:34` | 🟠 High |
| R7 | **`CartService.removeFromCart()` always returns `false`** regardless of success | `CartService.java` | 🟠 High |
| R8 | **Eureka URL uses `https`** in Config Server but Eureka runs plain HTTP — TLS handshake failure | `configserver/application.yaml` | 🟠 High |
| R9 | **Kafka `KAFKA_ADVERTISED_LISTENERS: localhost`** — containers cannot reach Kafka broker | `docker-compose.yml` | 🟠 High |
| R10 | **No stock decrement** when items are added to cart or orders are placed | `CartService.java`, `OrderService.java` | 🟠 High |
| R11 | **`X-User-ID` header is client-supplied** — no server-side identity verification; trivially spoofable | `CartController.java`, `OrderController.java` | 🟠 High |
| R12 | **Resilience4j annotations commented out** — circuit breaker and retry configured but not applied | `CartService.java` | 🟠 High |
| R13 | **Plaintext PostgreSQL credentials** in config files | `product-service.yaml`, `order-service.yaml` | 🟠 High |

### Medium Risks

| # | Risk | Location | Severity |
|---|---|---|---|
| R14 | `ProductRepository.findByActiveTrue()` returns `Collection<Object>` — unsafe cast in service | `ProductRepository.java`, `ProductService.java` | 🟡 Medium |
| R15 | `ProductService.updateProduct()` throws `NoSuchElementException` without message on missing product | `ProductService.java` | 🟡 Medium |
| R16 | Keycloak uses embedded H2 — data lost on container restart | `docker-compose.yml` | 🟡 Medium |
| R17 | No input validation (`@Valid`, `@NotNull`) on any DTO | All controllers | 🟡 Medium |
| R18 | `Address.id` (Long) leftover from JPA in MongoDB model — unused field | `user/model/Address.java` | 🟡 Medium |
| R19 | Grafana `datasources.yml` references `http://gateway:3100` (wrong container name for Loki) | `prometheus-fundamentals/grafana/datasources/datasources.yml` | 🟡 Medium |
| R20 | `rabbitmq` and `keycloak` volumes declared in compose but containers do not mount them | `docker-compose.yml` | 🟡 Medium |
| R21 | Notification Service not registered with Eureka and not connected to Config Server | `notification/pom.xml`, `application.yaml` | 🟡 Medium |
| R22 | `UserRequest` has no `role` field — role cannot be set on user creation | `user/dto/UserRequest.java` | 🟡 Medium |

### Low / Info

| # | Risk | Location | Severity |
|---|---|---|---|
| R23 | `CartItemRepositry` — typo in class name | `CartItemRepositry.java` | 🟢 Low |
| R24 | `cleints` — typo in package name | `order/.../cleints/` | 🟢 Low |
| R25 | `CartItem.updateAt` — typo in field name | `CartItem.java` | 🟢 Low |
| R26 | No DELETE endpoint for users | `UserController.java` | 🟢 Low |
| R27 | `spring-boot-h2console` dependency in User Service (unused) | `user/pom.xml` | 🟢 Low |
| R28 | Stale `target/` directories at wrong levels | `configserver/target/`, `order/target/`, `additional/authcodeflow/target/` | 🟢 Info |
| R29 | `README.md` is essentially empty — no setup instructions | `README.md` | 🟢 Info |
| R30 | Postman workspace has no collections, environments, or specs saved | `postman/` | 🟢 Info |
| R31 | `KafkaTopicConfig` creates `my-new-topic-2` but producer streams to `my-topic-new` — topic name mismatch | `KafkaTopicConfig.java`, `application.yaml` | 🟢 Low |

---

## 15. Architecture Summary Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                       ECOMMERCE MICROSERVICES ARCHITECTURE                       │
│                                                                                  │
│  ┌──────────────┐    ┌──────────────┐                                            │
│  │ Config Server│    │Eureka Server │                                            │
│  │  :8888       │◄───┤  :8761       │◄── All services register here             │
│  │ (native FS)  │    │ (discovery)  │                                            │
│  └──────┬───────┘    └──────────────┘                                            │
│         │ config served to all services on startup                               │
│         ▼                                                                        │
│  ┌──────────────────────────────────────────────────────────────────────────┐   │
│  │                         API Gateway  :8080                                │   │
│  │  (Spring Cloud Gateway WebFlux — reactive, load-balanced routing)        │   │
│  │  /api/products/** → PRODUCT-SERVICE                                       │   │
│  │  /api/users/**    → USER-SERVICE                                          │   │
│  │  /api/orders/**   → ORDER-SERVICE                                         │   │
│  │  /api/cart/**     → ORDER-SERVICE                                         │   │
│  └──────────────────────────────────────────────────────────────────────────┘   │
│                              ▲ External HTTP                                     │
│                                                                                  │
│  ┌──────────────────────────────────────────────────────────────────────────┐   │
│  │                       Business Microservices                              │   │
│  │                                                                           │   │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────┐  │   │
│  │  │  User Service   │  │ Product Service  │  │     Order Service       │  │   │
│  │  │    :8082        │  │    :8081         │  │        :8083            │  │   │
│  │  │  MongoDB Atlas  │  │  PostgreSQL      │  │  PostgreSQL (orderdb)   │  │   │
│  │  │  (cloud)        │  │  (productdb)     │  │  Calls User + Product   │  │   │
│  │  └─────────────────┘  └─────────────────┘  │  via Eureka lb HTTP     │  │   │
│  │                                             └──────────┬──────────────┘  │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                            │ AMQP publish        │
│                                                            ▼                     │
│  ┌──────────────────────────────────────────────────────────────────────────┐   │
│  │                    Infrastructure (Docker Compose)                        │   │
│  │                                                                           │   │
│  │  PostgreSQL:5432  RabbitMQ:5672/15672  Redis:6379  Keycloak:8443         │   │
│  │  Kafka:9092       Zookeeper:2181       pgAdmin:5051                      │   │
│  └──────────────────────────────────────────────────────────────────────────┘   │
│                                                            │ AMQP consume        │
│                                                            ▼                     │
│  ┌──────────────────────────────────────────────────────────────────────────┐   │
│  │  Notification Service :8084  (RabbitMQ consumer — stdout only, stub)     │   │
│  └──────────────────────────────────────────────────────────────────────────┘   │
│                                                                                  │
│  ┌──────────────────────────────────────────────────────────────────────────┐   │
│  │              Observability (separate docker-compose)                      │   │
│  │  Prometheus:9090  Grafana:3000  Loki:3100  Zipkin:9411  Alloy:12345      │   │
│  └──────────────────────────────────────────────────────────────────────────┘   │
│                                                                                  │
│  ┌──────────────────────────────────────────────────────────────────────────┐   │
│  │              Additional / Learning Modules (not integrated)               │   │
│  │  OAuth2 Demo: authcodeflow:8088 ↔ resource:8089 ↔ Keycloak:8443         │   │
│  │  Kafka Demo:  producer:8085 → my-topic-new → consumer:8086               │   │
│  │  Greenhouse:  Python Flask microservices + Loki observability demo        │   │
│  └──────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

*This report was generated by comprehensive static analysis of all source files, configuration, build descriptors, Docker assets, and Postman artifacts in the repository. No services were executed during analysis. All findings are based on code and configuration inspection. Findings marked ✅ are directly verified from source; findings marked 🔍 are inferred from context.*
