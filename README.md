# Personal Finance Manager

A robust Spring Boot application for managing personal finances, including transactions, categories, savings goals, and financial reports.

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

## 📸 Screenshots

![Screenshot 1](https://drive.google.com/uc?export=view&id=10F94Cd1b2gEy1Dw87Gh6xeW-Zywr09xa)
![Screenshot 2](https://drive.google.com/uc?export=view&id=164kdzeeJWcC1KSL62ZyByz8XZzrpEmWv)

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Security**
- **JWT Authentication**
- **PostgreSQL**
- **Maven**
- **JUnit 5**
- **JaCoCo** (Code Coverage)

---

## 📦 Getting Started (Local Development)

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/assignmentS.git
   cd assignmentS
   ```

2. **Configure the Database**
   - By default, the app uses H2 for local development.
   - To use PostgreSQL locally, update `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
     spring.datasource.username=your_user
     spring.datasource.password=your_password
     spring.datasource.driver-class-name=org.postgresql.Driver
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Build the Project**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   The API will be available at [http://localhost:8080/api](http://localhost:8080/api)

---

## 🧪 Testing

- **Run all tests with coverage:**
  ```bash
  mvn clean test jacoco:report
  ```
- **View coverage report:**  
  Open `target/site/jacoco/index.html` in your browser.

- **API Integration Test Script:**  
  Use the provided test script to verify your deployment:
  ```bash
  bash financial_manager_tests.sh http://localhost:8080/api
  ```

---

## 🌐 Deployment (Render.com)

### 1. **Provision a PostgreSQL Database**
- Create a free PostgreSQL instance on Render.

### 2. **Deploy the App**
- Connect your GitHub repo to Render.
- Choose **Web Service** and **Docker** as the environment.
- Set the following environment variables in Render:
  ```
  SPRING_PROFILES_ACTIVE=prod
  PORT=8080
  JWT_SECRET=your_jwt_secret
  JWT_EXPIRATION=86400000
  SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<database>
  SPRING_DATASOURCE_USERNAME=<username>
  SPRING_DATASOURCE_PASSWORD=<password>
  ```

### 3. **Build & Start Commands**
- **Build Command:** (leave blank for Docker)
- **Start Command:** (leave blank for Docker)

### 4. **Accessing the API**
- Your API will be live at:  
  `https://<your-app-name>.onrender.com/api`

---

## 🔗 API Endpoints

| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| POST   | `/api/auth/register`          | Register a new user          |
| POST   | `/api/auth/login`             | Login and receive JWT        |
| POST   | `/api/auth/logout`            | Logout (JWT blacklist)       |
| GET    | `/api/categories`             | List categories              |
| POST   | `/api/categories`             | Create a category            |
| DELETE | `/api/categories/{id}`        | Delete a category            |
| GET    | `/api/transactions`           | List transactions            |
| POST   | `/api/transactions`           | Create a transaction         |
| ...    | ...                           | ...                          |

> **Note:** All endpoints are prefixed with `/api`.

---

## 🩺 Health Check

To add a health check endpoint, you can create a simple controller:
```java
@RestController
@RequestMapping("/api")
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
```
Then access: `GET /api/health`

---



## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## 📫 Contact

For questions or support, open an issue or contact [your-email@example.com](adityakumarr6907@gmail.com).

--- 
