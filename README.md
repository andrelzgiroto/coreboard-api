# CoreBoard

> An internal management API platform for technical assistance shops and IT stores — built with engineering decisions that go beyond the tutorial.
 
---

## The Problem & The Solution

Technical assistance shops deal with a constant flow of service orders, customer interactions, employee assignments, and financial tracking — all at the same time, often without proper tooling.

CoreBoard was built to centralize that operational flow: from customer registration and service order creation, through payment processing and delivery, up to a real-time metrics dashboard that gives managers a clear picture of what is happening financially and operationally.

The goal was never just to build a working API. The goal was to make decisions that hold up under real-world conditions — and be able to explain every single one of them.
 
---

## Architectural Highlights

### 1. Rich Domain Model — No Anemic Entities

The Service Order lifecycle is a strict state machine:

```
OPEN → IN_PROCESS → FINISHED → DELIVERED
                             ↘ CANCELLED
```

Each transition (`start()`, `finish()`, `deliver()`, `cancel()`) lives directly inside the `ServiceOrder` entity. Every method validates its own preconditions before allowing a state change.

This is a deliberate application of the **Rich Domain Model** pattern from DDD. Business rules live where the data lives. The Service layer orchestrates — it does not decide what is valid. An invalid state is impossible to reach.
 
---

### 2. Database-Driven Metrics Dashboard

The metrics dashboard tracks financial and operational performance across configurable filters (date range, employee, customer). Every aggregation — `SUM`, `AVG`, `COUNT`, `GROUP BY` — is resolved directly in the database via **pure JPQL** and **interface projections**.

No collections are loaded into Java to be aggregated in memory. The database does what databases are built for. The API receives the result.
 
---

### 3. DRY Dynamic Filtering with Specifications

The Service Order and Payment Receipt listing endpoints support 10+ optional filters simultaneously. Instead of building one query method per filter combination, the entire filter logic is composed at runtime using `Specification.allOf`.

Each predicate is built independently and only applied when its corresponding parameter is present. Any combination of filters, one method, zero duplication.
 
---

### 4. Strict Schema Versioning with Flyway

The database schema is never managed by Hibernate. Flyway handles all migrations in versioned, ordered SQL files.

- `ddl-auto: validate` — Hibernate verifies the schema matches the entities. It never alters it.
- Indexes are created explicitly on the columns that are actually filtered: `service_order_status`, `created_at`, `finished_at`, `customer_id`, `assigned_employee_id`.
- Foreign key constraints are named semantically (`fk_service_orders_customer`), not auto-generated.
  The schema survives restarts, environment changes, and new team members without surprises.

---

### 5. Real Security & Error Handling

**Authentication:**
- Stateless JWT with employee role embedded in the payload — no database hit per request.
- Google OAuth2 integration with real token validation via the Google API client.
  **Authorization:**
- Role-based access control (`ADMIN`, `OPERATOR`) enforced at the method level via `@PreAuthorize`.
  **HTTP Configuration:**
- `open-in-view` is disabled. The JPA session closes at the service layer boundary. Lazy loading is predictable and controlled.
  **Error Handling:**
- Global exception handler (`@RestControllerAdvice`) follows **RFC 7807 Problem Details**, returning structured, consistent error responses across the entire API.
---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Database | PostgreSQL |
| ORM | Spring Data JPA + Hibernate |
| Migrations | Flyway |
| Security | Spring Security — JWT + Google OAuth2 |
| Mapping | MapStruct |
| Validation | Jakarta Validation API |
| Documentation | Springdoc OpenAPI (Swagger UI) |
 
---

## Core Domains

**Auth**
Standard credential login and Google OAuth2 login. Both return a signed JWT token.

**Customers**
Registration and management with CPF uniqueness validation.

**Employees**
Role-based management. Admins can create, update, and delete employees. Operators have restricted access.

**Service Orders**
The core domain. Supports creation, employee assignment, budget management, and strict lifecycle transitions. Cancellation is blocked once the order is finished or delivered.

**Payment Receipts**
Financial tracking linked directly to service orders. Supports PIX, cash, debit, and credit card. Payment is only allowed on finished orders — delivery requires confirmed payment.

**Metrics Dashboard**
Three dedicated endpoints:

- `GET /api/metrics` — Consolidated financial and operational overview (total generated, received, pending, average ticket, execution time, overdue orders).
- `GET /api/metrics/employees` — Productivity and revenue ranking per employee, paginated.
- `GET /api/metrics/customers` — Revenue and order volume ranking per customer, paginated.
  All endpoints accept optional filters: `from`, `to`, `assignedEmployeeId`, `customerId`.

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker
### 1. Clone the repository

```bash
git clone https://github.com/andrelzgiroto/coreboard-api.git
cd coreboard-api
```

### 2. Configure environment variables

```bash
cp .env.example .env
```

Open `.env` and fill in your values:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/coreboard_db
DATABASE_USER=coreboard_user
DATABASE_PASSWORD=coreboard_pass
JWT_SECRET=your-secret-key
GOOGLE_CLIENT_ID=your-google-client-id
```

### 3. Start the database

```bash
docker-compose up -d
```

Flyway will automatically run all migrations on the first application startup.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.
 
---

## API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

All endpoints are documented with request/response examples and authentication requirements.
 
---

## Project Structure

```
src/main/java/com/coreboard/api/
├── config/             # Security and application configuration
├── controller/         # HTTP layer — routing and request handling
├── domain/
│   ├── dto/            # Request and response records
│   ├── entity/         # JPA entities with domain behavior
│   ├── enums/          # Status and type definitions
│   └── view/           # Interface projections for metrics queries
├── exception/          # Global exception handler and custom exceptions
├── mapper/             # MapStruct mappers
├── repository/         # Spring Data repositories and JPQL queries
│   └── specification/  # Dynamic filter predicates
├── security/           # JWT filter, token service, user details
├── service/            # Business orchestration layer
└── util/               # Shared utilities
```
 
---

## Author

**André Giroto**
[github.com/andrelzgiroto](https://github.com/andrelzgiroto)