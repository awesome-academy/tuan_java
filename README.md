# TripGo API

A mock travel booking REST API built with **Spring Boot**, **Spring Data JPA**, **Spring Security**, **MySQL**, and **Flyway**.

## Tech Stack

* Java 21
* Spring Boot 4
* Spring Data JPA / Hibernate
* Spring Security / JWT
* MySQL
* Flyway
* MapStruct
* Maven
* Swagger / OpenAPI
* Thymeleaf

## Prerequisites

Install:

* Java 21
* MySQL
* Git

Check Java version:

```bash
java -version
```

The project includes Maven Wrapper, so Maven does not need to be installed globally.


## Database Setup

Create a MySQL database:

```sql
CREATE DATABASE trip_go;
```

Flyway will automatically create/update the database schema when the application starts.

## Environment Variables

The application requires database credentials and a JWT secret.

Create a `.env` file in the project root:

```dotenv
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=tripgo-local-secret-key-at-least-32-bytes
```

Make sure `.env` is ignored by Git:

```gitignore
.env
```

The application configuration should reference these variables:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/trip_go
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate

spring.flyway.enabled=true

app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=PT1H
```

> Spring Boot does not automatically load `.env`.

On macOS/Linux, load the environment variables before running:

```bash
set -a
source .env
set +a
```

Alternatively, configure `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` in your IDE Run Configuration.

## Run Application

### macOS / Linux

```bash
./mvnw spring-boot:run
```

### Windows

```bash
mvnw.cmd spring-boot:run
```

The application will start at:

```text
http://localhost:8080
```

## Database Migration

Flyway migrations are located at:

```text
src/main/resources/db/migration/
```

They are executed automatically when the application starts.

The startup flow is:

```text
Start Application
      ↓
Flyway Migration
      ↓
Hibernate Schema Validation
      ↓
Application Ready
```

## API Documentation

After starting the application, open Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

For protected APIs:

1. Call `/api/v1/auth/login`
2. Copy the returned JWT
3. Click **Authorize** in Swagger UI
4. Enter the JWT
5. Call protected endpoints

## Build

Run tests and build:

```bash
./mvnw clean verify
```

Create the JAR:

```bash
./mvnw clean package
```

Run the packaged application:

```bash
java -jar target/tripgo-api-0.0.1-SNAPSHOT.jar
```

## Notes

* Do not commit `.env`, database passwords, or JWT secrets.
* Database schema changes should be made through **Flyway migrations**.
* Hibernate is configured with `ddl-auto=validate`, not `create` or `update`.
* This project is intended for learning and demonstration purposes.
