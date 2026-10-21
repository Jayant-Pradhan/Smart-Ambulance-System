# 🚑 Smart Ambulance System

An event-driven **microservices** system that finds the nearest suitable ambulance and hospital bed for an emergency, tracks the trip, and notifies the caller.

Built with **Java 21, Spring Boot, Spring Cloud (Eureka, Gateway), Apache Kafka, MySQL, Docker, JUnit 5 and Mockito**.

---

## 📌 Problem Statement

In an emergency, manually finding a free ambulance and a hospital with a suitable bed wastes critical minutes. This project automates that process: it captures the emergency, selects the nearest ambulance with enough capacity, picks the nearest hospital with the right bed type, tracks the trip, and sends an email confirmation to the caller.

## ✨ Key Features

- **Event-driven design:** services communicate through Kafka events and do not call each other over REST.
- **Nearest ambulance selection:** filters AVAILABLE ambulances by patient capacity, then picks the closest using the **Haversine formula**.
- **Condition-based hospital and bed selection:** breathing difficulty → `VENTILATOR`, unconscious → `ICU`, otherwise `GENERAL`. Only active hospitals with an active *Emergency* department are considered.
- **Bed lifecycle with validation:** `AVAILABLE → RESERVED → OCCUPIED → AVAILABLE`, plus `OUT_OF_SERVICE`. Invalid transitions are rejected.
- **Trip tracking:** `ASSIGNED, EN_ROUTE, ARRIVED, PATIENT_PICKED_UP, HOSPITAL_REACHED, COMPLETED, CANCELLED`.
- **Email notification** to the caller using Spring Mail.
- **Service discovery** (Eureka) and a single entry point (**API Gateway**) with CORS for a React frontend.
- **Validation and error handling:** Bean Validation, DTO + mapper layers, global exception handlers, unique constraints for duplicates.
- **Tests:** JUnit 5, Mockito, MockMvc, parameterized tests. About **68% code coverage** (JaCoCo).


## 🖥️ Frontend (Demo UI)

A simple React UI to demo the backend APIs (create emergency, view assignment, track trip).
The UI was generated with AI assistance. The main focus of this project is the **Java backend and microservices architecture**.

---

## 🏗️ Architecture

```
                    ┌──────────────┐
   Client / React → │  API Gateway │ (8087)
                    └──────┬───────┘
                           │  (routes via Eureka)
   ┌──────────┬────────────┼─────────────┬─────────────┬──────────────┐
   │          │            │             │             │              │
Emergency  Ambulance   Assignment     Hospital      Tracking     Notification
 (8081)     (8082)       (8083)        (8084)        (8085)        (8086)
   │          │            │             │             │              │
   └──────────┴────────────┴──── Apache Kafka ─────────┴──────────────┘

   Eureka Server (8761)  |  MySQL (smart_ambulance_system)
```

### Services

| Module | Port | Responsibility |
|---|---|---|
| 01-emergency-service | 8081 | Create, get, cancel emergency; publishes emergency events |
| 02-ambulance-service | 8082 | Register ambulances, update status and location |
| 03-ambulance-assignment-service | 8083 | Select the nearest suitable ambulance and create the assignment |
| 04-hospital-service | 8084 | Manage hospitals, departments, beds; select hospital and reserve a bed |
| 05-tracking-service | 8085 | Store ambulance location and trip status |
| 06-notification-service | 8086 | Send email notifications |
| 07-api-gateway | 8087 | Single entry point, routing, CORS |
| 08-eureka-server | 8761 | Service registry |

### Kafka Topics

| Topic | Produced by | Consumed by |
|---|---|---|
| `emergency.details` | Emergency Service | Assignment, Hospital, Tracking, Notification |
| `ambulance.register` | Ambulance Service | Assignment |
| `ambulance.status.changed` | Ambulance Service | Assignment |
| `ambulance.assigned` | Assignment Service | Hospital, Tracking, Notification |
| `hospital.selected` | Hospital Service | (published for other services) |
| `tracking.details` | Tracking Service | Notification |

Each service uses its own consumer group, so every service receives every event. The `emergencyId` is used as the message key to keep events of one emergency in order.

### Main Flow

1. Client sends `POST /api/emergency` through the gateway.
2. Emergency Service validates, saves to MySQL, and publishes to `emergency.details`.
3. Assignment Service keeps a local copy of emergency and ambulance data built from events. Hospital Service selects a hospital and reserves a bed. Notification Service emails the caller.
4. `POST /api/assignments/emergency/{id}` selects the nearest suitable ambulance and publishes `ambulance.assigned`.
5. Tracking APIs update the ambulance location and trip status.

---

## 🛠️ Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot, Spring Data JPA (Hibernate), Spring Web MVC, Spring Mail
- **Microservices:** Spring Cloud Netflix Eureka, Spring Cloud Gateway (WebFlux), Spring Cloud LoadBalancer
- **Messaging:** Apache Kafka (JSON serializer / deserializer)
- **Database:** MySQL
- **Build and Containers:** Maven, Docker
- **Testing:** JUnit 5, Mockito, MockMvc (`@WebMvcTest`), JaCoCo
- **Others:** Lombok, Bean Validation

---

## 📡 API Reference

Direct service URLs are shown. Through the gateway, routes follow the Eureka service id, for example `http://localhost:8087/01-emergency-service/api/emergency`.

### Emergency Service (8081) `/api/emergency`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/emergency` | Create an emergency |
| GET | `/api/emergency/{id}` | Get emergency by id |
| GET | `/api/emergency` | Get all emergencies |
| PATCH | `/api/emergency/{id}/cancel` | Cancel an emergency |
| GET | `/api/emergency/{id}/status` | Get emergency status |

**Sample request**
```json
{
  "callerName": "Jayant",
  "callerMobile": "8114616376",
  "callerEmail": "caller@example.com",
  "emergencyType": "ACCIDENT",
  "patientCount": 2,
  "latitude": 20.2961,
  "longitude": 85.8245,
  "unconscious": true,
  "breathingDifficulty": false,
  "severeBleeding": false,
  "trappedInVehicle": false,
  "additionalDetails": "Road accident near main road"
}
```

### Ambulance Service (8082) `/api/ambulance`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/ambulance` | Register an ambulance |
| GET | `/api/ambulance/{id}` | Get ambulance by id |
| GET | `/api/ambulance` | Get all ambulances |
| PATCH | `/api/ambulance/{id}/status` | Update status |
| PATCH | `/api/ambulance/{id}/location` | Update location |

### Assignment Service (8083) `/api/assignments`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/assignments/emergency/{emergencyId}` | Assign nearest suitable ambulance |
| GET | `/api/assignments/emergency/{emergencyId}` | Get assignment by emergency |
| GET | `/api/assignments/{assignmentId}` | Get assignment by id |
| PATCH | `/api/assignments/{assignmentId}/status` | Update assignment status |
| GET | `/api/assignments` | Get all assignments |
| GET | `/api/assignments/status/{status}` | Filter by status |

### Hospital Service (8084)
| Method | Endpoint | Description |
|---|---|---|
| POST / GET / PATCH | `/api/hospitals`, `/api/hospitals/{id}` | Create, read, update hospitals |
| PATCH | `/api/hospitals/{id}/deactivate` | Deactivate a hospital |
| POST / GET / PATCH | `/api/departments`, `/api/departments/{id}` | Manage departments |
| POST | `/api/beds` | Create a bed |
| GET | `/api/beds`, `/api/beds/{id}` | Get beds |
| GET | `/api/beds/hospital/{hospitalId}` | Beds of a hospital |
| GET | `/api/beds/hospital/{hospitalId}/available?bedType=ICU` | Available beds by type |
| PATCH | `/api/beds/{id}/reserve`, `/occupy`, `/release` | Bed lifecycle |
| PATCH | `/api/beds/{id}/out-of-service`, `/available`, `/deactivate` | Bed maintenance |

### Tracking Service (8085) `/api/tracking`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tracking/location` | Update ambulance location |
| GET | `/api/tracking/emergency/{emergencyId}` | Tracking by emergency |
| GET | `/api/tracking/ambulance/{ambulanceId}` | Tracking by ambulance |
| PATCH | `/api/tracking/emergency/{emergencyId}/status?tripStatus=ARRIVED` | Update trip status |
| GET | `/api/tracking` | All tracking records |

### Notification Service (8086) `/api/notification`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/notification` | Create a notification record |
| GET | `/api/notification/{id}` | Get by id |
| GET | `/api/notification` | Get all |
| GET | `/api/notification/emergency/{emergencyId}` | By emergency |
| GET | `/api/notification/status/{status}` | By status |

---

## ⚙️ Getting Started

### Prerequisites
- Java 21 and Maven
- MySQL (database: `smart_ambulance_system`)
- Apache Kafka
- Docker (optional)

### Environment Variables

| Variable | Used by | Purpose |
|---|---|---|
| `DB_PASSWORD` | All DB services | MySQL password |
| `CORS_ALLOWED_ORIGINS` | API Gateway | Allowed frontend origins (default: localhost 3000 / 5173) |
| `MAIL_PASSWORD` | Notification Service | Gmail app password (never commit this) |

> Never commit passwords or app passwords. Keep them in environment variables.

### Run Locally

1. Create the database:
```sql
   CREATE DATABASE smart_ambulance_system;
```
2. In each service's `application.properties`, use the **localhost** settings (they are commented in the files):
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/smart_ambulance_system
   spring.kafka.bootstrap-servers=localhost:9092
   eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```
3. Start in this order: **Eureka Server → Kafka → other services → API Gateway**.
```bash
   cd 08-eureka-server && mvn spring-boot:run
   cd 01-emergency-service && mvn spring-boot:run
   # repeat for the other services
```
4. Open Eureka dashboard at `http://localhost:8761`.

### Run with Docker

The default `application.properties` use Docker hostnames: `kafka:29092`, `eureka-server:8761` and `host.docker.internal:3306` for MySQL.

```bash
# build the jar of a service
mvn clean package

# build the image (example: emergency service)
docker build -t smart-ambulance/emergency-service .

# run on a shared network with the right hostnames
docker run -d --network <your-network> -e DB_PASSWORD=<password> -p 8081:8081 smart-ambulance/emergency-service
```

Every service has its own `Dockerfile` (base image `eclipse-temurin:21-jre`).

---

## 🧪 Testing

```bash
mvn test
```

- Service layer: JUnit 5 + Mockito
- Controllers: MockMvc / `@WebMvcTest`
- Kafka producer tests, selection strategy tests (null and invalid coordinates, parameterized)
- Coverage report: `target/site/jacoco/index.html` (about 68% coverage)

---

## 🔒 Known Limitations and Roadmap

This is a learning project, and these are the planned improvements:

- [ ] Spring Security + JWT at the gateway
- [ ] Transactional Outbox pattern, retries and Dead Letter Topics for Kafka
- [ ] Locking / `@Transactional` for ambulance and bed allocation (avoid race conditions)
- [ ] Trigger ambulance assignment automatically from the emergency event
- [ ] Status feedback events so Ambulance Service stays in sync with assignments
- [ ] Real road distance and ETA using a Maps API instead of straight-line distance
- [ ] Live tracking with WebSocket / Redis, with history stored separately
- [ ] Database per service with Flyway migrations
- [ ] Pagination, Swagger / OpenAPI docs, Testcontainers integration tests

---

## 👤 Author

**Jayant**
Java Developer (Fresher)
