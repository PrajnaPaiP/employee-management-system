# Employee Management System

A RESTful Employee Management System built using **Java, Spring Boot, Spring Data JPA, and MySQL**. This project provides APIs to manage employee records with CRUD operations, search functionality, pagination, sorting, input validation, and centralized exception handling.

## Project Overview

The Employee Management System is a backend application designed to manage employee information efficiently through REST APIs.

It follows a layered architecture with separate controller, service, repository, entity, DTO, and exception-handling components.

## Technologies Used

| Category | Technologies |
|---|---|
| Language | Java |
| Backend Framework | Spring Boot |
| Data Access | Spring Data JPA, Hibernate |
| Database | MySQL |
| API Architecture | REST APIs |
| API Documentation | Swagger / OpenAPI |
| Validation | Jakarta Bean Validation |
| Testing | JUnit 5, Mockito, MockMvc |
| Code Coverage | JaCoCo |
| Build Tool | Gradle |
| IDE | IntelliJ IDEA |
| API Testing | Postman |

## Features

- Create, retrieve, update, and delete employee records.
- Retrieve all employees.
- Retrieve an employee by ID.
- Search employees by first name or last name.
- Search employees by department.
- Pagination and sorting.
- DTO-based request and response handling.
- Input validation using Jakarta Validation.
- Duplicate email validation.
- Custom exception handling.
- Centralized exception responses.
- Swagger UI API documentation.
- Unit testing with JUnit 5 and Mockito.
- Controller integration testing with MockMvc.
- Code coverage reporting using JaCoCo.

## Project Architecture

The application follows a layered architecture:

```text
Employee Management System
│
├── controller
│   └── EmployeeController.java
│
├── service
│   ├── EmployeeService.java
│   └── EmployeeServiceImpl.java
│
├── repository
│   └── EmployeeRepository.java
│
├── entity
│   └── Employee.java
│
├── dto
│   ├── EmployeeRequest.java
│   └── EmployeeResponse.java
│
├── exception
│   ├── EmployeeNotFoundException.java
│   ├── DuplicateEmailException.java
│   └── GlobalExceptionHandler.java
│
├── config
│   └── OpenApiConfig.java
│
└── DemoApplication.java
```

### Architecture Responsibilities

- **Controller:** Handles incoming HTTP requests and API responses.
- **Service:** Contains employee-related business logic.
- **Repository:** Communicates with the MySQL database using Spring Data JPA.
- **Entity:** Represents the employee database table.
- **DTO:** Separates API request and response objects from database entities.
- **Exception:** Handles application errors centrally.
- **Config:** Configures Swagger/OpenAPI documentation.

## Database Configuration

Database: `employee_db`

The application uses MySQL for storing employee information.

Create the database:

```sql
CREATE DATABASE employee_db;
```

Configure the database connection in `application.properties`:

```properties
spring.application.name=employee-management-system

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/employee_db}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=${PORT:8080}
```

For local development, the default settings use MySQL on localhost with the `root` username and an empty password. Configure environment variables when using different credentials.

## REST API Endpoints

Base URL:

```text
http://localhost:8080/api/employees
```

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees` | Create employee |
| GET | `/api/employees` | Retrieve all employees |
| GET | `/api/employees/{id}` | Retrieve employee by ID |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |
| GET | `/api/employees/search/name?name=John` | Search by name |
| GET | `/api/employees/search/department?department=IT` | Search by department |
| GET | `/api/employees/page?page=0&size=5&sortBy=id&direction=asc` | Pagination and sorting |

## Sample API Request

### Create Employee

**POST** `/api/employees`

Request body:

```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@gmail.com",
  "department": "IT",
  "salary": 50000.00
}
```

### Sample Response

```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@gmail.com",
  "department": "IT",
  "salary": 50000.00
}
```

## API Documentation

The project uses Swagger UI and OpenAPI for interactive API documentation.

After starting the application, access:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Installation and Setup

### Prerequisites

- Java JDK
- IntelliJ IDEA or another Java IDE
- MySQL Server
- Git

### 1. Clone the repository

```bash
git clone https://github.com/PrajnaPaiP/employee-management-system.git
```

### 2. Navigate to the project

```bash
cd employee-management-system
```

### 3. Configure MySQL

Create the `employee_db` database and configure the database connection using environment variables or the defaults in `application.properties`.

### 4. Run the application

On Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

The application starts on:

```text
http://localhost:8080
```

## Testing

The project includes:

- Unit tests using JUnit 5 and Mockito.
- Controller integration tests using Spring Boot Test and MockMvc.

Run all tests:

```powershell
.\gradlew.bat clean test
```

Generate the JaCoCo coverage report:

```powershell
.\gradlew.bat test jacocoTestReport
```

Coverage report location:

```text
build/reports/jacoco/test/html/index.html
```

### Current Test Coverage

The latest generated JaCoCo report showed:

- Instruction coverage: 84%
- Branch coverage: 100%

Coverage values may change as the code and tests evolve.

## Future Enhancements

Potential improvements for future versions:

- Spring Security and role-based access control.
- JWT authentication.
- Docker containerization.
- Cloud deployment.
- Frontend integration using React.

## Author

**Prajna Pai**

GitHub: [PrajnaPaiP](https://github.com/PrajnaPaiP)

Project Repository: [Employee Management System](https://github.com/PrajnaPaiP/employee-management-system)
