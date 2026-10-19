# CRUD Productos

Store inventory web application. It exposes a REST API to create, read, update and delete products (CRUD), stores them in PostgreSQL, and includes a simple web page (HTML, CSS and JavaScript) to manage them from the browser.

## Technologies

- Java 25
- Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA)
- PostgreSQL
- Maven (wrapper included)
- HTML, CSS and vanilla JavaScript for the frontend

## Requirements

- JDK 25
- PostgreSQL running on `localhost:5432`

## Setup

1. Create the database:

   ```sql
   CREATE DATABASE crud_web_db;
   ```

2. Open `src/main/resources/application.properties` and set your own PostgreSQL credentials:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/crud_web_db
   spring.datasource.username=YOUR_USER
   spring.datasource.password=YOUR_PASSWORD
   ```

   The `productos` table is created automatically on the first run (`spring.jpa.hibernate.ddl-auto=update`).

## Run

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Then open <http://localhost:8080> in your browser.

## Web interface

- Product cards with name, description, price and stock
- Search by name or description
- Click a card to see its detail, edit it or delete it
- Create new products with the "Nuevo producto" button
- Stock labels: "Agotado" (0), "Stock bajo" (less than 10)

## REST API

Base URL: `/api/productos`

| Method | Endpoint              | Description            | Success response |
| ------ | --------------------- | ---------------------- | ---------------- |
| GET    | `/api/productos`      | List all products      | `200 OK`         |
| GET    | `/api/productos/{id}` | Get a product by id    | `200 OK`         |
| POST   | `/api/productos`      | Create a product       | `201 Created`    |
| PUT    | `/api/productos/{id}` | Update a product       | `200 OK`         |
| DELETE | `/api/productos/{id}` | Delete a product       | `204 No Content` |

### Product format

```json
{
  "nombre": "Laptop",
  "descripcion": "15 inch, 16 GB RAM",
  "precio": 899.99,
  "stock": 12
}
```

### Validation rules

- `nombre`: required, maximum 100 characters
- `descripcion`: optional, maximum 255 characters
- `precio`: required, cannot be negative
- `stock`: required, cannot be negative

### Error response

Errors return the corresponding HTTP status and a JSON body:

```json
{
  "estado": 404,
  "error": "Not Found",
  "mensaje": "No existe un producto con id 99"
}
```

| Status | When                                                        |
| ------ | ----------------------------------------------------------- |
| 400    | Invalid data, malformed JSON, or a non-numeric id in the URL |
| 404    | The product does not exist                                  |

## Project structure

```
src/main/java/com/tienda/crudproductos
├── controller   REST endpoints (ProductoController)
├── service      Business logic and validation (ProductoService)
├── repository   Database access (ProductoRepository)
├── model        JPA entity (Producto)
└── exception    Custom exceptions and error handler (ManejadorErrores)

src/main/resources
├── application.properties
└── static       Frontend (index.html, styles.css, app.js)
```

## Author

Your name here
