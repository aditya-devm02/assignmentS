# Personal Finance Manager

A Spring Boot application for managing personal finances, including transactions, categories, savings goals, and financial reports.

## Features

- User Authentication with JWT
- Transaction Management
- Category Management
- Savings Goals Tracking
- Monthly and Yearly Financial Reports
- RESTful API

## Tech Stack

- Java 17
- Spring Boot 3.2.3
- Spring Security
- JWT Authentication
- PostgreSQL
- Maven
- JUnit 5
- JaCoCo for Code Coverage

## API Documentation

The API documentation is available at `/swagger-ui.html` when running the application.

## Getting Started

1. Clone the repository
2. Configure your database in `application.properties`
3. Run `mvn clean install`
4. Run the application using `mvn spring-boot:run`

## Testing

Run tests with coverage:
```bash
mvn clean test jacoco:report
```

## Deployment

The application is configured for deployment on Render.com with PostgreSQL database. 