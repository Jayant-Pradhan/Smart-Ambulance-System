Smart Ambulance System

A microservices-based backend system designed to manage emergency requests, ambulance assignment, hospital availability, trip tracking, and notifications.

Overview

The Smart Ambulance System helps coordinate the emergency response workflow through independent Spring Boot microservices. It provides APIs for creating emergencies, managing ambulances, assigning suitable ambulances, checking hospital bed availability, tracking trips, and sending notifications.

Architecture

The project uses a microservices architecture in which each service handles a specific responsibility.

High-level workflow:

Emergency Request → Ambulance Service → Assignment Service → Hospital Service → Tracking Service → Notification Service

API requests are routed through the API Gateway, while Eureka supports service discovery.

Microservices

Service

Responsibility

Emergency Service

Create and manage emergency requests

Ambulance Service

Manage ambulance information and availability

Ambulance Assignment Service

Handle ambulance assignment to emergencies

Hospital Service

Manage hospitals and bed availability

Tracking Service

Manage trip and tracking information

Notification Service

Send emergency-related notifications

API Gateway

Provide a common entry point for API requests

Eureka Server

Service registration and discovery

Technology Stack
Language: Java 21

Backend: Spring Boot, Spring Web

Architecture: Microservices

Service Discovery: Netflix Eureka

API Gateway: Spring Cloud Gateway

Database: MySQL

Messaging: Apache Kafka

Frontend: React, Vite

Containerization: Docker

## Local Development Ports

| Component | Port |
|---|---:|
| React UI | 3000 |
| Emergency Service | 8081 |
| Ambulance Service | 8082 |
| Assignment Service | 8083 |
| Hospital Service | 8084 |
| Tracking Service | 8085 |
| Notification Service | 8086 |
| API Gateway | 8087 |
| Eureka Server | 8761 |
| Kafka | 9092 |
| Zookeeper | 2181 |

These are the project's configured local development ports.

Running the Project

Install Java 21, Maven, Node.js, Docker Desktop, and Git.

Configure MySQL databases and credentials for the backend services.

Configure Kafka, and Zookeeper according to the project configuration.

Start the Eureka Server and backend microservices.

Start the API Gateway.

Start the React frontend.

Use each service's Maven wrapper (mvnw or mvnw.cmd) to build and run the Spring Boot applications. Use the frontend's package scripts to run the React application.

Local frontend URL: http://localhost:3000

Eureka dashboard: http://localhost:8761

Configuration and Security

Keep database passwords, Gmail App Passwords,and other credentials outside source control. Configure them through environment variables or a local environment configuration file that is excluded by .gitignore.

Do not commit real passwords, tokens, or private keys to GitHub.

Future Improvements

Deploy the services to a public hosting environment.

Add monitoring and centralized logging.

Improve automated integration testing.

Add production-ready authentication and authorization where required.

Author

Jayant Pradhan

GitHub: Jayant-Pradhan
