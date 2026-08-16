# Vehicle Rental System

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Eureka%20%7C%20Gateway-6DB33F?logo=spring&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-events-FF6600?logo=rabbitmq&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-persistence-4169E1?logo=postgresql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-Kotlin%20DSL-02303A?logo=gradle&logoColor=white)

A compact Spring Cloud microservices platform for managing vehicles, customers, rentals,
pricing, and asynchronous notifications.

The project is intentionally implemented as independently deployable services, with
Eureka for service discovery, Spring Cloud Gateway as the public entry point, and
RabbitMQ for asynchronous communication between services.

## Architecture

```mermaid
flowchart LR
    Client[REST client] --> Gateway[API Gateway :8080]

    Gateway -. discover services .-> Eureka[Eureka :8761]

    Gateway --> Customer[Customer Service]
    Gateway --> Vehicle[Vehicle Service]
    Gateway --> Rental[Rental Service]
    Gateway --> Pricing[Pricing Service]

    Rental --> Pricing
    Rental -->|RentalCreated| Rabbit[(RabbitMQ)]

    Rabbit --> Vehicle
    Rabbit --> Notify[Notification Service]

    Customer --> CustomerDB[(PostgreSQL)]
    Vehicle --> VehicleDB[(PostgreSQL)]
    Rental --> RentalDB[(PostgreSQL)]
    Notify --> NotificationDB[(PostgreSQL)]
````

The API Gateway is the single public entry point. Eureka maintains the service registry
and allows services to be discovered without hardcoded host addresses.

REST is used for synchronous request/response operations, while RabbitMQ handles
asynchronous rental events. Each data-owning service manages its own persistence model
and does not access another service's database directly.

## Services

| Service                | Responsibility                             |
| ---------------------- | ------------------------------------------ |
| `api-gateway`          | Public API entry point and request routing |
| `discovery-server`     | Eureka service registry                    |
| `customer-service`     | Customer management                        |
| `vehicle-service`      | Vehicle management and availability        |
| `rental-service`       | Rental lifecycle and event publishing      |
| `pricing-service`      | Rental price calculation                   |
| `notification-service` | Asynchronous notification processing       |

## Rental flow

1. A client sends a rental request through the API Gateway.
2. Rental Service validates the customer and vehicle and requests the rental price.
3. The rental is persisted with a `PENDING` status.
4. Rental Service publishes a `RentalCreated` event to RabbitMQ.
5. Vehicle Service consumes the event and reserves the vehicle.
6. Notification Service independently consumes the event and records a notification.
7. The vehicle becomes available again when the rental is completed.

The services remain independently deployable and communicate through REST or RabbitMQ
rather than sharing application code or database tables.

## Design decisions

### Spring Cloud Gateway

The Gateway provides a single entry point for external clients and routes requests
to services discovered through Eureka.

### Eureka

Eureka acts as the service registry. Services register themselves on startup and
the Gateway uses service discovery instead of hardcoded service addresses.

### RabbitMQ

RabbitMQ is used for events where the producer should not need to wait for downstream
consumers. `RentalCreated` is consumed independently by Vehicle and Notification
services.

### Service-owned persistence

Each service owns its persistence model. Services never query another service's
tables directly.

### Independent Gradle builds

Every deployable service is a standalone Gradle project with its own
`build.gradle.kts` and `settings.gradle.kts`.

## Features

* Customer management API
* Vehicle management and availability filtering
* Rental lifecycle management
* Category-based rental pricing
* Weekly and monthly rental discounts
* RabbitMQ event publishing and consumption
* Eureka service discovery
* Spring Cloud API Gateway
* PostgreSQL persistence
* Docker Compose local environment
* Testcontainers integration tests

## Technology stack

| Area           | Technology                              |
| -------------- | --------------------------------------- |
| Language       | Java 21                                 |
| Framework      | Spring Boot 3.5                         |
| Cloud          | Spring Cloud Gateway, Netflix Eureka    |
| HTTP           | Spring Web                              |
| Persistence    | Spring Data JPA, Hibernate              |
| Database       | PostgreSQL                              |
| Messaging      | RabbitMQ, Spring AMQP                   |
| Testing        | JUnit, Spring Boot Test, Testcontainers |
| Build          | Gradle Kotlin DSL                       |
| Infrastructure | Docker Compose                          |

## Prerequisites

* JDK 21
* Docker Engine with Docker Compose
* Gradle 8.14+ or the included Gradle wrapper

## Quick start

Start the complete environment:

```bash
docker compose up --build
```

The main endpoints are:

| Service             | URL                      |
| ------------------- | ------------------------ |
| API Gateway         | `http://localhost:8080`  |
| Eureka              | `http://localhost:8761`  |
| RabbitMQ Management | `http://localhost:15672` |

RabbitMQ Management uses the local development credentials:

```text
guest / guest
```

### Build an individual service

Each service can be built independently:

```powershell
.\gradlew.bat -p rental-service build
```

Or using a locally installed Gradle:

```bash
cd rental-service
gradle build
```

## API

### Create a customer

```http
POST /customers
Content-Type: application/json
```

```json
{
  "firstName": "Ada",
  "lastName": "Lovelace",
  "email": "ada@example.com",
  "phone": "+48 555 010 010"
}
```

### Create a vehicle

```http
POST /vehicles
Content-Type: application/json
```

```json
{
  "registrationNumber": "W0 FF001",
  "make": "Toyota",
  "model": "RAV4",
  "category": "SUV",
  "dailyRate": 70
}
```

### Find available vehicles

```http
GET /vehicles?category=SUV&status=AVAILABLE
```

### Calculate rental price

```bash
curl "http://localhost:8080/pricing-service/pricing/calculate?vehicleCategory=SUV&startDate=2026-08-20&endDate=2026-08-25"
```

### Create a rental

```http
POST /rentals
Content-Type: application/json
```

The request contains the customer ID, vehicle ID, start date, and end date.

The rental is persisted as `PENDING` and a `RentalCreated` event is published to
RabbitMQ for downstream processing.

## Configuration

| Variable        | Default                                                  | Description         |
| --------------- | -------------------------------------------------------- | ------------------- |
| `DB_URL`        | `jdbc:postgresql://localhost:5432/vehicle_rental_system` | PostgreSQL JDBC URL |
| `DB_USERNAME`   | `vehicle_rental_system`                                  | PostgreSQL username |
| `DB_PASSWORD`   | `vehicle_rental_system`                                  | PostgreSQL password |
| `RABBITMQ_HOST` | `localhost`                                              | RabbitMQ hostname   |
| `EUREKA_URL`    | `http://localhost:8761/eureka`                           | Eureka registry URL |

## Testing

Run an individual service's test suite:

```powershell
.\gradlew.bat -p customer-service test
.\gradlew.bat -p vehicle-service test
.\gradlew.bat -p rental-service test
.\gradlew.bat -p notification-service test
.\gradlew.bat -p pricing-service test
```

Tests use Testcontainers to start disposable PostgreSQL and RabbitMQ instances where
required. Docker must be running.

Gateway and Discovery can be built and tested without the infrastructure.

## Project structure

```text
vehicle-rental-system/
├── api-gateway/
│   ├── src/main/java/dev/asyncluna/rental/gateway/       # API Gateway
│   └── src/main/resources/                              # application.yaml
├── discovery-server/
│   ├── src/main/java/dev/asyncluna/rental/discovery/    # Eureka registry
│   └── src/main/resources/                              # application.yaml
├── customer-service/
│   ├── src/main/java/dev/asyncluna/rental/customer/     # Customer API and persistence
│   ├── src/main/resources/                              # application.yaml
│   └── src/test/java/dev/asyncluna/rental/customer/     # Testcontainers tests
├── vehicle-service/
│   ├── src/main/java/dev/asyncluna/rental/vehicle/      # Vehicle API and persistence
│   ├── src/main/resources/                              # application.yaml
│   └── src/test/java/dev/asyncluna/rental/vehicle/      # Testcontainers tests
├── rental-service/
│   ├── src/main/java/dev/asyncluna/rental/rental/       # Rental API, persistence and events
│   ├── src/main/resources/                              # application.yaml
│   └── src/test/java/dev/asyncluna/rental/rental/       # Testcontainers tests
├── pricing-service/
│   ├── src/main/java/dev/asyncluna/rental/pricing/      # Pricing API and rules
│   ├── src/main/resources/                              # application.yaml
│   └── src/test/java/dev/asyncluna/rental/pricing/      # Service tests
├── notification-service/
│   ├── src/main/java/dev/asyncluna/rental/notification/ # RabbitMQ consumer
│   ├── src/main/resources/                              # application.yaml
│   └── src/test/java/dev/asyncluna/rental/notification/ # Testcontainers tests
├── docker-compose.yml                                    # PostgreSQL and RabbitMQ
├── gradlew / gradlew.bat                                # Shared Gradle wrapper
├── README.md
└── LICENSE
```

## License

This project is licensed under the [MIT License](LICENSE).
