# 🐾 FurryHub – Pet Care Booking Platform

FurryHub is a **microservices-based platform** for pet owners to discover services and book appointments with providers (groomers, walkers, vets, etc.).  
It uses **Spring Boot, PostgreSQL, Kafka, and API Gateway** with JWT authentication.

---

## 🚀 Features

- 🔑 **Auth Service** – Register/login users, JWT-based authentication.
- 📋 **Catalog Service** – Manage providers, service types, provider–service mapping.
- 🐕 **Pet Service** – Manage pets for each user.
- 📅 **Booking Service** – Create/accept/cancel/complete bookings with validation.
- 🔔 **Notification Service** – Stores booking-related notifications for users/providers.
- 🌐 **API Gateway** – Secure, single entry point for all APIs.
- 🔍 **Eureka Server (optional)** – Service discovery.

---

## 🛠 Tech Stack

- **Java 21**, **Spring Boot 3.5**
- **Spring Cloud Gateway**
- **Spring Security (JWT)**
- **Spring Data JPA / Hibernate**
- **PostgreSQL** + **PostGIS** for provider locations
- **Apache Kafka** (event-driven messaging)
- **Lombok**, **Feign Client**, **MapStruct**

---

## 📦 Microservices Overview

| Service                  | Port | Description |
|--------------------------|------|-------------|
| **auth-service**         | 8081 | User authentication & JWT |
| **catalog-service**      | 8083 | Providers & service types |
| **pet-service**          | 8086 | Manage user pets |
| **booking-service**      | 8084 | Booking lifecycle |
| **notification-service** | 8085 | User/provider notifications |
| **api-gateway**          | 8080 | Gateway entry point |
| **eureka-server**        | 8761 | Service discovery (optional) |

---

## 🗄️ Database Setup

We use **PostgreSQL**. Each microservice has its own DB:

- `authdb`
- `catalogdb`
- `petdb`
- `bookingdb`
- `notificationdb`

### Enable PostGIS for CatalogDB
```sql
\c catalogdb;
CREATE EXTENSION IF NOT EXISTS postgis;
- **PostGIS** extension is enabled for storing provider locations as geographic points.
```

```⚙️ Setup Instructions
git clone https://github.com/your-username/furryhub.git
cd furryhub
```

2. Start PostgreSQL

```Make sure PostgreSQL is running locally. Create DBs:
CREATE DATABASE authdb;
CREATE DATABASE catalogdb;
CREATE DATABASE petdb;
CREATE DATABASE bookingdb;
CREATE DATABASE notificationdb;
```

3. Start Kafka (command for macbook)
   ```
   bin/kafka-server-start.sh config/kraft/server.properties

5. Run Services
```From project root:
mvn spring-boot:run -pl auth-service
mvn spring-boot:run -pl catalog-service
mvn spring-boot:run -pl pet-service
mvn spring-boot:run -pl booking-service
mvn spring-boot:run -pl notification-service
mvn spring-boot:run -pl api-gateway
```

## 🔑 API Endpoints

### Auth
- **POST** `/auth/register`
- **POST** `/auth/login`

### Catalog
- **POST** `/providers`
- **GET** `/providers`
- **POST** `/service-types`

### Pet
- **POST** `/pets`
- **GET** `/pets`

### Booking
- **POST** `/bookings`
- **PUT** `/bookings/{id}/accept`
- **PUT** `/bookings/{id}/complete`
- **PUT** `/bookings/{id}/cancel`
- **GET** `/bookings/my`

### Notifications
- **GET** `/notifications`
- **PUT** `/notifications/{id}/read`

---

## 🔔 Event-Driven Booking Flow

1. **BOOKING_CREATED** → when a user books.  
2. **BOOKING_ACCEPTED** → when the provider accepts.  
3. **BOOKING_COMPLETED** → when the provider marks complete.  
4. **BOOKING_CANCELLED** → when user/provider cancels.  

> Each event is published to **Kafka**, consumed by **Notification Service**, and stored for the respective user/provider.

---

## 🧪 Testing with Postman

This repo includes ready-to-use Postman collections.

### Import into Postman
1. Go to **Postman → Import**.
2. Import both files from `/postman` folder:
   - `furryhub.postman_collection.json`
   - `furryhub.postman_environment.json`

### Usage
- Select the **FurryHub Local** environment.
- Register/Login via `Auth` requests.
- Copy the JWT (done automatically in Postman via variable).
- Test **Providers, Pets, Bookings, Notifications** end-to-end.

### Files
- **Postman Collection** → `/postman/furryhub.postman_collection.json`
- **Environment Variables** → `/postman/furryhub.postman_environment.json`
