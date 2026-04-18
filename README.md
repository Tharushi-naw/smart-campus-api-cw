
# Smart Campus API

**5COSC022W - Client-Server Architectures Coursework**

A JAX-RS RESTful API for managing smart campus rooms, sensors, and sensor reading history.

---

## Overview

This project was developed for the **Smart Campus** coursework scenario. The API allows facilities staff and automated systems to manage rooms, register sensors, retrieve sensor data, and maintain historical sensor readings.

The implementation follows RESTful design principles using:

- resource-based endpoints
- versioned API structure
- appropriate HTTP methods and status codes
- nested sub-resources
- structured JSON error handling
- request and response logging

---

## API Design

The API uses a versioned entry point:

```text
/api/v1
```

The main resource collections are:

* `rooms`
* `sensors`
* `sensor readings`

### Key design features

* **Discovery endpoint** at `GET /api/v1`
* **Room management** for creating, retrieving, and deleting rooms
* **Sensor management** for registering and retrieving sensors
* **Query parameter filtering** for sensor type searches
* **Sub-resource locator pattern** for sensor reading history
* **Automatic `currentValue` updates** when a new reading is added
* **Custom exception mapping** for clean JSON error responses
* **Logging filters** for API observability

---

## Technology Stack

| Technology           | Purpose                         |
| -------------------- | ------------------------------- |
| Java 8               | Core programming language       |
| Maven                | Dependency and build management |
| JAX-RS (Jersey 2.32) | REST API implementation         |
| Apache Tomcat 9      | Servlet container / server      |
| Postman              | API testing                     |
| GitHub               | Source code hosting             |

---

## Data Storage

This project does **not** use a database.

All data is stored in memory using Java collections such as:

* `Map`
* `List`
* `ArrayList`
* `LinkedHashMap`

---

## Project Structure

```text
src/main/java/com/mycompany/smartcampusapi
├── SmartCampusApplication.java
├── exceptions
│   ├── GlobalExceptionMapper.java
│   ├── LinkedResourceNotFoundException.java
│   ├── LinkedResourceNotFoundExceptionMapper.java
│   ├── ResourceNotFoundException.java
│   ├── ResourceNotFoundExceptionMapper.java
│   ├── RoomNotEmptyException.java
│   ├── RoomNotEmptyExceptionMapper.java
│   ├── SensorUnavailableException.java
│   └── SensorUnavailableExceptionMapper.java
├── filters
│   └── ApiLoggingFilter.java
├── models
│   ├── ErrorMessage.java
│   ├── Room.java
│   ├── Sensor.java
│   └── SensorReading.java
├── resources
│   ├── DiscoveryResource.java
│   ├── RoomResource.java
│   ├── SensorReadingResource.java
│   └── SensorResource.java
└── store
    └── InMemoryStore.java
```

---

## Base URL

```text
http://localhost:8080/SmartCampusApi/api/v1
```

---

## How to Build and Run

### Requirements

* NetBeans
* Apache Tomcat 9
* Java 8
* Maven
* Postman

### Step-by-step Instructions

1. Clone this repository to your local machine.
2. Open the project in **NetBeans**.
3. Make sure **Apache Tomcat 9** is configured as the server.
4. Open `pom.xml` and allow Maven dependencies to download.
5. Right-click the project and select **Clean and Build**.
6. Right-click the project and select **Run**.
7. Test the API using Postman or a browser.

Use this API entry point:

```text
http://localhost:8080/SmartCampusApi/api/v1
```

> **Note:** NetBeans may automatically open the default root project page in the browser. The actual coursework API starts at `/api/v1`.

---

## Endpoint Summary

### Discovery

| Method | Endpoint  | Description                             |
| ------ | --------- | --------------------------------------- |
| GET    | `/api/v1` | Returns API metadata and resource links |

### Rooms

| Method | Endpoint                 | Description       |
| ------ | ------------------------ | ----------------- |
| GET    | `/api/v1/rooms`          | Get all rooms     |
| POST   | `/api/v1/rooms`          | Create a new room |
| GET    | `/api/v1/rooms/{roomId}` | Get a room by ID  |
| DELETE | `/api/v1/rooms/{roomId}` | Delete a room     |

### Sensors

| Method | Endpoint                     | Description            |
| ------ | ---------------------------- | ---------------------- |
| GET    | `/api/v1/sensors`            | Get all sensors        |
| GET    | `/api/v1/sensors?type=CO2`   | Filter sensors by type |
| GET    | `/api/v1/sensors/{sensorId}` | Get a sensor by ID     |
| POST   | `/api/v1/sensors`            | Create a new sensor    |

### Sensor Readings

| Method | Endpoint                              | Description                      |
| ------ | ------------------------------------- | -------------------------------- |
| GET    | `/api/v1/sensors/{sensorId}/readings` | Get reading history for a sensor |
| POST   | `/api/v1/sensors/{sensorId}/readings` | Add a new reading to a sensor    |

---

## Sample `curl` Commands

### 1. Discovery endpoint

```bash
curl -X GET http://localhost:8080/SmartCampusApi/api/v1
```

### 2. Get all rooms

```bash
curl -X GET http://localhost:8080/SmartCampusApi/api/v1/rooms
```

### 3. Create a new room

```bash
curl -X POST http://localhost:8080/SmartCampusApi/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"roomId\":\"SCI-105\",\"roomName\":\"Science Seminar Room\",\"capacity\":25}"
```

### 4. Get a specific room

```bash
curl -X GET http://localhost:8080/SmartCampusApi/api/v1/rooms/LIB-301
```

### 5. Delete a room with no assigned sensors

```bash
curl -X DELETE http://localhost:8080/SmartCampusApi/api/v1/rooms/SCI-105
```

### 6. Get all sensors

```bash
curl -X GET http://localhost:8080/SmartCampusApi/api/v1/sensors
```

### 7. Filter sensors by type

```bash
curl -X GET "http://localhost:8080/SmartCampusApi/api/v1/sensors?type=CO2"
```

### 8. Create a new sensor

```bash
curl -X POST http://localhost:8080/SmartCampusApi/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"sensorId\":\"OCC-001\",\"sensorType\":\"Occupancy\",\"sensorStatus\":\"ACTIVE\",\"currentValue\":12,\"roomId\":\"ENG-201\"}"
```

### 9. Get reading history for a sensor

```bash
curl -X GET http://localhost:8080/SmartCampusApi/api/v1/sensors/TEMP-001/readings
```

### 10. Add a reading to an active sensor

```bash
curl -X POST http://localhost:8080/SmartCampusApi/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d "{\"value\":26.7}"
```

---

## Testing

The API was tested using **Postman**.

Testing covered:

* successful `GET`, `POST`, and `DELETE` operations
* room creation and protected room deletion
* sensor creation with linked room validation
* sensor filtering using query parameters
* nested sensor reading operations
* structured JSON error responses
* request and response logging

---

## Error Handling

The API uses custom exception mappers to return structured JSON error responses instead of raw server stack traces.

### Implemented error scenarios

* **409 Conflict**
  Returned when attempting to delete a room that still has sensors assigned.

* **422 Unprocessable Entity**
  Returned when creating a sensor with a `roomId` that does not exist.

* **403 Forbidden**
  Returned when attempting to add a reading to a sensor in `MAINTENANCE` state.

* **404 Not Found**
  Returned when a requested room or sensor does not exist.

* **500 Internal Server Error**
  Returned by the global exception mapper for unexpected runtime failures.

---

## Logging

The API includes a logging filter that records:

* incoming HTTP method
* request URI
* outgoing response status code

This improves observability and makes debugging easier during testing.

---

## Notes

* This project uses **JAX-RS only**.
* No database technology is used.
* All application data is stored in memory.

---

## Author

**W.A.T.N. Nethmi**  
**IIT Number:** 20221962  
**UoW Number:** w1985668

---

## Report Answers

### Part 1.1 – Project and Application Configuration

This project was implemented as a Maven web application using JAX-RS (Jersey) and Apache Tomcat. The API entry point is versioned through `@ApplicationPath("/api/v1")`, which makes the service easier to maintain, extend, and document in future versions.

In many standard JAX-RS setups, resource classes are commonly treated as request-scoped, meaning a new resource instance is created for each incoming request, although the exact lifecycle can depend on the runtime and configuration. This means the framework often creates a fresh resource object for each request rather than using one shared singleton resource instance for all requests. However, even when the resource objects themselves are request-scoped, the in-memory data used by the application is still shared application state because it is stored in static maps and lists inside `InMemoryStore`.

This architectural decision matters because shared in-memory structures can still be modified by multiple requests at the same time. If two requests update the same map or list concurrently, race conditions, lost updates, or inconsistent data may occur. To reduce that risk, synchronized blocks were used around important write operations such as creating and deleting rooms and sensors. This helps preserve consistency and prevents corruption of the shared state.

### Part 1.2 – Discovery Endpoint

The discovery endpoint at `GET /api/v1` returns useful API metadata such as the API name, version, administrative contact, and links to the main resource collections. This makes the API more self-descriptive and easier for clients to understand.

Hypermedia, or HATEOAS, is considered an advanced RESTful design feature because it allows the server to guide the client through available resources and actions using links within responses. This is beneficial because the client does not need to depend only on external static documentation or hard-coded URLs. Instead, clients can discover important paths directly from the API response itself. This reduces coupling between client and server and makes the API easier to evolve over time.

### Part 2.1 – Room Resource Implementation

When returning a list of rooms, one option is to return only room IDs, while another is to return the full room objects.

Returning only IDs can reduce the amount of data sent across the network, which may be helpful in very large systems or when bandwidth efficiency is the main concern. However, it also increases client-side work because the client must make extra requests to retrieve the details for each room.

Returning the full room objects increases the size of the response, but it is more convenient for the client because the useful information is available immediately in one request. In this project, each room object is relatively small, so returning the full room objects is practical. It simplifies testing, reduces the number of additional requests, and makes the API easier to use from the client side.

### Part 2.2 – Room Deletion and Safety Logic

The DELETE operation is idempotent in this implementation because sending the same delete request multiple times does not continue changing the server state after the first successful deletion. If the room exists and has no linked sensors, the first DELETE request removes it. A repeated DELETE for the same room may then return `404 Not Found`, but the server state does not change any further.

The HTTP status code returned may therefore differ between attempts. The first request may return success, while later requests may return `404 Not Found`. However, idempotency is concerned with the effect on the server state, not with returning the exact same response each time. Since repeated DELETE requests do not create any additional change after the first successful deletion, the operation is still idempotent.

### Part 3.1 – Sensor Resource and Integrity

The `@Consumes(MediaType.APPLICATION_JSON)` annotation tells JAX-RS that the method expects the request body to be in JSON format. If a client sends another format such as `text/plain` or `application/xml`, the framework will not match the request properly to the expected media type for JSON deserialization.

In many cases, JAX-RS will reject the request with **415 Unsupported Media Type** because the request content type does not match `@Consumes(MediaType.APPLICATION_JSON)`. The framework may also be unable to map the request body correctly into the `Sensor` Java object. This behaviour is important because it enforces a clear contract between the client and server. Since the endpoint expects JSON fields matching the `Sensor` model, sending another format breaks that contract and prevents correct processing.

### Part 3.2 – Filtered Retrieval and Search

Using `@QueryParam` for filtering is better than putting the filter value inside the path because query parameters are intended for optional filtering and searching on a collection.

The path should primarily identify the resource hierarchy itself, for example `/sensors` or `/sensors/{sensorId}`. A query parameter such as `/sensors?type=CO2` clearly indicates that the client is still asking for the sensor collection, but with an optional filter applied.

This design is also more flexible because more filters can be added later without changing the core URI structure. For example, the API could later support something like `/sensors?type=CO2&status=ACTIVE`. That is much cleaner and more scalable than creating many path-based search endpoints.

### Part 4.1 – Sub-Resource Locator Pattern

The Sub-Resource Locator pattern helps make the API cleaner and easier to maintain. In this project, `SensorResource` handles the main sensor operations, while `SensorReadingResource` handles the nested reading history for a specific sensor.

This separation is useful because reading-related logic can become more detailed and complex over time. By delegating that logic to a dedicated class, the main sensor resource remains focused on sensor creation, retrieval, and filtering. The reading resource then focuses specifically on reading history and the insertion of new readings within the context of one sensor.

If every nested route were defined inside one large controller-style class, the code would become harder to read, extend, test, and maintain. The sub-resource locator makes the structure more modular and better reflects the natural relationship between a sensor and its readings.

### Part 5.2 – Dependency Validation

HTTP `422 Unprocessable Entity` is more semantically accurate than `404 Not Found` when a client sends a valid JSON payload, but the `roomId` inside that payload refers to a room that does not exist.

A `404` usually means that the target resource identified by the request URI itself was not found. In this case, the request URI is valid and the endpoint exists. The problem is not the URI, but the meaning of one field inside the request body. Since the structure of the request is valid but the server cannot successfully process the semantic meaning of the submitted data, `422` is the more accurate response.

### Part 5.4 – Global Safety Net

Exposing raw Java stack traces to API consumers is a cybersecurity risk because stack traces reveal internal technical details that should remain private. An attacker could learn class names, package names, method names, file names, line numbers, framework details, and the internal structure of the application.

This information can help attackers fingerprint the technology stack and identify possible weak points. For example, knowing exactly where an exception occurred or which framework classes are involved may make targeted attacks easier. A global `ExceptionMapper<Throwable>` prevents this by returning a generic `500 Internal Server Error` response to the client while keeping the technical details available only in server-side logs for debugging.

### Part 5.5 – API Request and Response Logging Filters

Using JAX-RS filters for logging is better than manually placing `Logger.info()` statements inside every resource method because logging is a cross-cutting concern. It affects the whole API in a similar way, so it should be handled centrally.

A request filter can log the incoming HTTP method and URI before the request reaches the resource method. A response filter can log the final status code after the request has been processed. This provides consistent logging for both successful and failed requests without repeating the same logging code in every endpoint.

This also keeps the resource classes cleaner and easier to maintain because business logic stays separate from infrastructure concerns such as observability. It makes future changes easier, since the logging behaviour can be updated in one place instead of many different resource classes.

