# Personal Finance Manager

A comprehensive personal finance management system built with Spring Boot that helps users track their expenses, manage budgets, and achieve their financial goals.


 **Live API URL (copy to use):**  
`https://assignments-2.onrender.com/api`


## Features

- User authentication and authorization using JWT
- Expense tracking and categorization
- Budget management
- Financial goal setting and tracking
- Transaction history and reporting
- Secure data storage with PostgreSQL


## 📸 Screenshots

![Screenshot 1](https://drive.google.com/uc?export=view&id=10F94Cd1b2gEy1Dw87Gh6xeW-Zywr09xa)
![Screenshot 2](https://drive.google.com/uc?export=view&id=164kdzeeJWcC1KSL62ZyByz8XZzrpEmWv)

## Tech Stack

- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT for authentication
- Maven for dependency management
- JUnit 5 and Mockito for testing
- JaCoCo for code coverage

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher
- Docker (optional, for containerized deployment)

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd personal-finance-manager
   ```

2. Configure PostgreSQL:
   - Create a new database named `finance_manager`
   - Update `application.properties` with your database credentials

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Docker Deployment

1. Build the Docker image:
   ```bash
   docker build -t finance-manager .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 finance-manager
   ```

## API Documentation

### Authentication

#### Register User
- **POST** `/api/auth/register`
- **Request Body:**
  ```json
  {
    "username": "string",
    "email": "string",
    "password": "string"
  }
  ```

#### Login
- **POST** `/api/auth/login`
- **Request Body:**
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response:** JWT token

### Expenses

#### Create Expense
- **POST** `/api/expenses`
- **Headers:** `Authorization: Bearer <token>`
- **Request Body:**
  ```json
  {
    "amount": "number",
    "category": "string",
    "description": "string",
    "date": "date"
  }
  ```

#### Get Expenses
- **GET** `/api/expenses`
- **Headers:** `Authorization: Bearer <token>`
- **Query Parameters:**
  - `startDate` (optional)
  - `endDate` (optional)
  - `category` (optional)

### Budgets

#### Create Budget
- **POST** `/api/budgets`
- **Headers:** `Authorization: Bearer <token>`
- **Request Body:**
  ```json
  {
    "category": "string",
    "amount": "number",
    "period": "string"
  }
  ```

#### Get Budgets
- **GET** `/api/budgets`
- **Headers:** `Authorization: Bearer <token>`

## Design Decisions

### Architecture
- Layered architecture following Spring Boot best practices
- Clear separation of concerns with distinct layers:
  - Controllers: Handle HTTP requests and responses
  - Services: Implement business logic
  - Repositories: Manage data access
  - Entities: Represent database models
  - DTOs: Transfer data between layers

### Security
- JWT-based authentication for stateless security
- Password encryption using BCrypt
- Role-based access control
- Input validation using Spring Validation

### Database
- PostgreSQL for robust data storage
- JPA for object-relational mapping
- Optimized queries with proper indexing
- Transaction management for data consistency

### Testing
- Comprehensive unit tests with JUnit 5
- Integration tests for critical flows
- Mockito for mocking dependencies
- JaCoCo for code coverage (minimum 80%)

### Error Handling
- Global exception handling
- Custom exception classes for different scenarios
- Consistent error response format
- Detailed logging for debugging

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request



---

## 🚀 Features

- **User Authentication** (JWT-based)
- **Transaction Management** (CRUD)
- **Category Management** (custom and default)
- **Savings Goals Tracking**
- **Monthly & Yearly Financial Reports**
- **RESTful API** with clear endpoints
- **Role-based Security**
- **Comprehensive Test Coverage** (JUnit, JaCoCo)



---

## 📫 Contact

For questions or support, open an issue or contact [adityakumarr6907@gmail.com](mailto:adityakumarr6907@gmail.com).

--- 
