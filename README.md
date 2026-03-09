# PatientManager

PatientManager is a distributed microservices-based application built with **Java 21** and **Spring Boot 3/4**, designed to manage patient data, authentication, billing, and analytics in a robust, scalable architecture.

![PatientManager Architecture Diagram](https://lh3.googleusercontent.com/gg/AMW1TPoUEPcLq-UjG_j0C1O92NGltJNomtRrD_B9RkpLJ20s5nMpRvDrR7AIB5Yw-AtStILTBseQZs_laV4MnpnEpFx_y0aTNt7BOFTeMSAy-pNxl9Fxtk0-rr8J_cp7tTGC4-IHHWvOTjMMGw7jbSauCgK24Y8IvK414Eo6CrnCvrJYmKa7Lbo-9nLL67c35fF3-Md7JMUqy4PYQDaAhM0UWOR9ALA2-taVHhH-7XT_oW_LDbK2Ee6tf1UkhMg81WDV3jxQLdwaT1n2kMc5Uxqj9yiqDZ9OxHC492F7xrLaEFXaCRZdCHjZItdootEwSOpSG5qrvurIN4nPrANqUB3bvWQ=s1600-rj)

## Architecture & Microservices

The system is composed of several independent services communicating via REST, gRPC, and event-driven patterns (Kafka).

### 1. API Gateway (`api-gateway`)
- **Role**: Serves as the single entry point for all external client requests, routing traffic to the appropriate backend microservices.
- **Tech**: Spring Boot, Spring Cloud Gateway (WebFlux).

### 2. Auth Service (`auth-service`)
- **Role**: Handles user authentication, authorization, and token generation.
- **Tech**: Spring Boot, Spring Security, JWT (io.jsonwebtoken), Spring Data JPA, PostgreSQL, H2 (for testing).

### 3. Patient Service (`patient-service`)
- **Role**: Manages core patient records and profiles. It is the central domain service.
- **Tech**: Spring Boot, Spring Data JPA, PostgreSQL, Spring Kafka (for event-driven communication), gRPC (for inter-service calls), OpenAPI/Swagger.

### 4. Billing Service (`billing-service`)
- **Role**: Handles invoice generation, payments, and billing for patient services.
- **Tech**: Spring Boot, gRPC (for high-performance internal communication with other services).

### 5. Analytics Service (`analytics-service`)
- **Role**: Aggregates data and provides reporting and analytics capabilities.
- **Tech**: Spring Boot (Maven standard setup).

### 6. Infrastructure & Deployment (`infrastructure`)
- **Role**: Contains Infrastructure-as-Code (IaC) to provision cloud resources.
- **Tech**: AWS CDK (Java SDK), LocalStack (for local AWS service emulation).

---

## Tech Stack Summary

- **Language**: Java 21
- **Framework**: Spring Boot (3.5.x / 4.0.x), Spring Cloud
- **Inter-service Communication**:
  - REST (API Gateway to services)
  - gRPC (Internal service-to-service e.g., Patient to Billing)
  - Apache Kafka (Asynchronous event streaming)
- **Database**: PostgreSQL (Production), H2 (Testing)
- **Security**: Spring Security + JWT
- **Documentation**: Springdoc OpenAPI (Swagger UI)
- **Infrastructure**: AWS CDK, LocalStack

---

## Prerequisites

To run this project locally, ensure you have the following installed:

1. **Java 21** (JDK 21)
2. **Maven 3.9+** (or use the provided `mvnw` wrappers)
3. **Docker & Docker Compose** (for PostgreSQL, Kafka, LocalStack)
4. **AWS CLI** (configured locally for LocalStack)
5. **Node.js & AWS CDK CLI** (`npm install -g aws-cdk`)

---

## Getting Started (Local Development)

### 1. Start Local Infrastructure

If you haven't already, start your local dependencies (Database, Kafka). You may need to spin up your own Docker containers or use the provided LocalStack scripts.

To deploy AWS infrastructure locally via LocalStack:
```bash
cd infrastructure
./localstack-deploy.sh
```
*(This script sets up mock AWS credentials and deploys the `patient-management` CloudFormation stack to LocalStack at `http://localhost:4566`)*

### 2. Build the Microservices

You can build all services using Maven from their respective directories. For example:

```bash
# Build Auth Service
cd auth-service
./mvnw clean install -DskipTests

# Build Patient Service
cd ../patient-service
./mvnw clean install -DskipTests
```
*(Note: Because gRPC proto generation is bound to the compile phase, running `mvn clean compile` is recommended before running to generate the stubs).*

### 3. Run the Services

Start each service individually using Spring Boot:

**Terminal 1:**
```bash
cd api-gateway
./mvnw spring-boot:run
```

**Terminal 2:**
```bash
cd auth-service
./mvnw spring-boot:run
```

**Terminal 3:**
```bash
cd patient-service
./mvnw spring-boot:run
```

*(Repeat for `billing-service` and `analytics-service`)*

### 4. Accessing the APIs

- **API Gateway**: Typically runs on port `8080` (check `application.yml`). All REST requests should be routed here.
- **Swagger Documentation**: Accessible directly on the respective services (e.g., `http://localhost:<PORT>/swagger-ui.html` for `patient-service`).

---

## Testing

To run the integration and unit tests for any service:

```bash
cd <service-name>
./mvnw test
```

The project includes an `integration-tests` module for cross-service end-to-end testing.

## gRPC Code Generation

If you modify the `.proto` files, you must regenerate the gRPC Java code:

```bash
cd patient-service
./mvnw clean compile
```
This leverages the `os-maven-plugin` and `protobuf-maven-plugin` to generate the correct stubs for your OS.
