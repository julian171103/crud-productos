# CRUD Products

A web application for managing a product inventory. It provides a REST API to create, read, update, and delete products (CRUD), stores product data in PostgreSQL, and includes a web interface built with HTML, CSS, and vanilla JavaScript.

## Technologies

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Maven Wrapper
- HTML, CSS, and vanilla JavaScript

## Requirements

- JDK 25
- PostgreSQL running on `localhost:5432`

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/julian171103/crud-productos.git
cd crud-productos
```

### 2. Create the database

Connect to PostgreSQL and execute:

```sql
CREATE DATABASE crud_web_db;
```

### 3. Configure database credentials

The application reads the PostgreSQL username and password from environment variables.

Configure these variables in your environment or in the IntelliJ run configuration:

- `DB_USERNAME`: your PostgreSQL username
- `DB_PASSWORD`: your PostgreSQL password

The database connection URL is:

```text
jdbc:postgresql://localhost:5432/crud_web_db
```

Do not commit real database credentials to the repository.

Hibernate manages the database schema according to the JPA configuration in `application.properties`. The `products` table is used to store product records.

## Run the application

On Windows, run:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS, run:

```bash
./mvnw spring-boot:run
```

Open the application in your browser:

http://localhost:8080

## Web interface

The web interface allows users to:

- View product cards with name, description, price, and stock.
- Search products by name or description.
- Select a product to view its details.
- Create new products.
- Edit existing products.
- Delete products.
- View stock status labels:
   - `Agotado`: stock is zero.
   - `Stock bajo`: stock is below 10.

## REST API

Base URL: `/api/products`

| Method | Endpoint | Description | Success response |
|---|---|---|---|
| GET | `/api/products` | List all products | `200 OK` |
| GET | `/api/products/{id}` | Get a product by ID | `200 OK` |
| POST | `/api/products` | Create a product | `201 Created` |
| PUT | `/api/products/{id}` | Update a product | `200 OK` |
| DELETE | `/api/products/{id}` | Delete a product | `204 No Content` |

### Product JSON format

Example request body for creating a product:

```json
{
  "name": "Laptop",
  "description": "15 inch, 16 GB RAM",
  "price": 899.99,
  "stock": 12
}
```

### Validation rules

- `name`: required, maximum 100 characters.
- `description`: optional, maximum 255 characters.
- `price`: required, cannot be negative.
- `stock`: required, cannot be negative.

### Error responses

Errors return an appropriate HTTP status and a JSON response. For example:

```json
{
  "estado": 404,
  "error": "Not Found",
  "mensaje": "No existe un producto con id 99"
}
```

| Status | Description |
|---|---|
| `400 Bad Request` | Invalid data, malformed JSON, or a non-numeric ID in the URL. |
| `404 Not Found` | The requested product does not exist. |

## Project structure

```text
src/
├── main/
│   ├── java/com/store/productcrud/
│   │   ├── controller/   REST API controllers
│   │   ├── service/      Business logic and validation
│   │   ├── repository/   Database access
│   │   ├── model/        JPA entities
│   │   └── exception/    Custom exceptions and error handling
│   └── resources/
│       ├── application.properties
│       └── static/
│           ├── index.html
│           ├── styles.css
│           └── app.js
└── test/
    └── java/             Automated tests
```

## Testing

On Windows, run:

```powershell
.\mvnw.cmd test
```

On Linux or macOS, run:

```bash
./mvnw test
```

Make sure the required environment variables and database configuration are available when running tests that need PostgreSQL.

## Author

Julián Guevara
