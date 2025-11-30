# Fashion Backend - Hướng Dẫn Chi Tiết Dự Án

## 📋 Mục Lục
1. [Tổng Quan Dự Án](#tổng-quan-dự-án)
2. [Công Nghệ & Framework](#công-nghệ--framework)
3. [Database](#database)
4. [Design Patterns](#design-patterns)
5. [Kiến Trúc Hệ Thống](#kiến-trúc-hệ-thống)
6. [Cấu Trúc Project](#cấu-trúc-project)
7. [Luồng Hoạt Động](#luồng-hoạt-động)
8. [Best Practices](#best-practices)

---

## 🎯 Tổng Quan Dự Án

**Fashion Backend** là một RESTful API backend được xây dựng bằng Spring Boot, phục vụ cho ứng dụng thời trang. Dự án hiện tại tập trung vào:
- Authentication & Authorization (JWT + OAuth2)
- User Management
- Security với Spring Security
- NoSQL Database (MongoDB)

---

## 🛠 Công Nghệ & Framework

### 1. **Core Framework**
- **Spring Boot 3.5.6**: Framework chính cho Java application
  - Auto-configuration
  - Embedded server (Tomcat)
  - Production-ready features
  
- **Java 25**: Ngôn ngữ lập trình
  - Modern Java features
  - Strong typing
  - Object-oriented programming

- **Gradle**: Build automation tool
  - Dependency management
  - Build lifecycle
  - Plugin system

### 2. **Security Stack**
- **Spring Security**: Framework bảo mật
  - Authentication & Authorization
  - CSRF protection
  - CORS configuration
  - Password encoding

- **JWT (JSON Web Token)**: Token-based authentication
  - Library: `jjwt` version 0.11.5
  - Stateless authentication
  - Token expiration handling

- **OAuth2 Client**: Social login (Google)
  - Third-party authentication
  - OAuth2 flow implementation

- **BCrypt**: Password hashing
  - One-way encryption
  - Salt generation
  - Cost factor configuration

### 3. **Data Layer**
- **Spring Data MongoDB**: ORM cho MongoDB
  - Repository abstraction
  - Query methods
  - Automatic schema mapping

- **MongoDB**: NoSQL database
  - Document-based storage
  - Flexible schema
  - Horizontal scaling

### 4. **Messaging & Caching**
- **RabbitMQ (AMQP)**: Message queue
  - Asynchronous processing
  - Event-driven architecture
  - Message routing

- **Redis Reactive**: Caching layer
  - In-memory data store
  - Reactive programming
  - Session management

### 5. **Utilities**
- **Spring Validation**: Data validation
  - Bean Validation (Jakarta)
  - Custom validators
  - Error handling

- **Spring Mail**: Email service
  - SMTP integration
  - Email templates
  - Async email sending

- **Spring Boot DevTools**: Development tools
  - Hot reload
  - LiveReload
  - Property defaults

---

## 🗄 Database

### **MongoDB Configuration**

#### **Nơi cấu hình kết nối MongoDB**

Kết nối MongoDB được cấu hình tại:

**1. File `application.properties`** (dòng 2):
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/userDatabase
```

**2. Spring Boot Auto-Configuration** (tự động):
- Khi Spring Boot khởi động, nó tự động phát hiện:
  - Dependency `spring-boot-starter-data-mongodb` trong `build.gradle`
  - Property `spring.data.mongodb.uri` trong `application.properties`
  - Repository interface extends `MongoRepository` (như `UserRepository`)
  
- Spring Boot tự động tạo:
  - `MongoClient` bean để kết nối MongoDB
  - `MongoTemplate` bean để thao tác với database
  - Connection pool và các cấu hình cần thiết

**3. Không cần file config Java riêng**:
- Không cần tạo `@Configuration` class cho MongoDB
- Spring Boot tự động xử lý tất cả thông qua auto-configuration

#### Connection String
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/fashion-mongodb
```

**Cấu trúc connection string**:
- `mongodb://` - Protocol
- `localhost:27017` - Host và port
- `fashion-mongodb` - Tên database

**Cho MongoDB Atlas (cloud)**:
```properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/<dbname>
```

#### Database Details
- **Database Name**: `fashion-mongodb`
- **Collection**: `users` (được định nghĩa trong `@Document(collection = "users")`)
- **Port**: 27017 (default MongoDB port)
- **Connection**: Tự động kết nối khi application start

#### Document Structure
```java
@Document(collection = "users")
public class User {
    @Id
    private String id;              // MongoDB ObjectId
    
    @Indexed(unique = true)
    private String email;           // Unique email index
    
    @Indexed(unique = true)
    private String username;        // Unique username index
    
    private String password;        // BCrypt hashed password
}
```

#### **Cách Spring Boot tự động kết nối MongoDB**

**Luồng hoạt động khi application start**:

```
1. Spring Boot khởi động
   ↓
2. Spring Boot Auto-Configuration phát hiện:
   - Dependency: spring-boot-starter-data-mongodb
   - Property: spring.data.mongodb.uri
   - Repository: UserRepository extends MongoRepository
   ↓
3. Spring Boot tự động tạo beans:
   - MongoClient (kết nối tới MongoDB)
   - MongoDatabaseFactory
   - MongoTemplate (để thao tác với database)
   ↓
4. Kết nối được thiết lập với MongoDB
   ↓
5. Repository interfaces được implement tự động
   ↓
6. Application sẵn sàng sử dụng MongoDB
```

**Các class liên quan**:
- `UserRepository` extends `MongoRepository<User, String>`
  - Spring Data MongoDB tự động tạo implementation
  - Các method như `save()`, `findById()`, `existsByEmail()` được implement tự động
  
- `User` entity với `@Document(collection = "users")`
  - Spring Data MongoDB map class này với collection "users" trong MongoDB

**Nếu muốn cấu hình thủ công** (không khuyến khích, chỉ khi cần custom):

```java
@Configuration
public class MongoConfig {
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
    
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "fashion-mongodb");
    }
}
```

Nhưng với Spring Boot, **không cần** làm điều này vì auto-configuration đã xử lý!

#### Indexes
- **Email Index**: Unique constraint để đảm bảo email không trùng lặp
- **Username Index**: Unique constraint để đảm bảo username không trùng lặp
- **ID Index**: Auto-generated bởi MongoDB

#### MongoDB vs SQL
| Feature | MongoDB (NoSQL) | SQL Database |
|---------|----------------|--------------|
| Schema | Flexible/Dynamic | Fixed Schema |
| Data Model | Document-based | Table-based |
| Relationships | Embedded/References | Foreign Keys |
| Scalability | Horizontal | Vertical/Horizontal |
| Query Language | MongoDB Query | SQL |
| ACID | Limited | Full ACID |

---

## 🎨 Design Patterns

### 1. **Repository Pattern**

**Mục đích**: Tách biệt data access logic khỏi business logic

**Implementation**:
```java
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
}
```

**Lợi ích**:
- Abstraction layer cho database operations
- Dễ dàng thay đổi database implementation
- Testable với mock repositories
- Spring Data tự động generate implementation

**Vị trí**: `repository/UserRepository.java`

---

### 2. **Service Layer Pattern**

**Mục đích**: Tách business logic khỏi controller và repository

**Structure**:
```java
// Interface
public interface UserService {
    User register(UserRegistrationRequest request);
}

// Implementation
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // Business logic here
}
```

**Lợi ích**:
- Separation of concerns
- Reusability
- Testability
- Transaction management

**Vị trí**: 
- Interface: `service/UserService.java`
- Implementation: `service/UserServiceImpl.java`

---

### 3. **Dependency Injection (DI) Pattern**

**Mục đích**: Inversion of Control (IoC) - dependencies được inject từ bên ngoài

**Implementation**:
```java
// Constructor Injection (Preferred)
public UserServiceImpl(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
}
```

**Các loại DI trong Spring**:
1. **Constructor Injection** (Được sử dụng trong project)
   - Recommended by Spring
   - Immutable dependencies
   - Required dependencies

2. **Field Injection** (Không dùng)
   - `@Autowired` trên field
   - Khó test
   - Không recommended

3. **Setter Injection**
   - Optional dependencies
   - Flexible configuration

**Lợi ích**:
- Loose coupling
- Testability
- Maintainability
- Single Responsibility

---

### 4. **DTO (Data Transfer Object) Pattern**

**Mục đích**: Tách biệt data structure cho API requests/responses khỏi domain entities

**Implementation**:
```java
public class UserRegistrationRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
```

**Lợi ích**:
- API contract stability
- Validation separation
- Security (không expose internal fields)
- Versioning support

**Vị trí**: `dto/UserRegistrationRequest.java`

---

### 5. **Controller-Advice Pattern (Global Exception Handler)**

**Mục đích**: Centralized exception handling

**Implementation**:
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(...) { }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(...) { }
}
```

**Lợi ích**:
- Consistent error responses
- DRY principle
- Centralized error handling
- Better error messages

**Vị trí**: `config/GlobalExceptionHandler.java`

---

### 6. **Strategy Pattern**

**Mục đích**: Định nghĩa family of algorithms và làm chúng interchangeable

**Implementation**:
```java
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // Có thể thay đổi thành: Argon2PasswordEncoder, Pbkdf2PasswordEncoder
    }
}
```

**Lợi ích**:
- Algorithm interchangeability
- Open/Closed Principle
- Runtime algorithm selection

**Vị trí**: `config/PasswordConfig.java`

---

### 7. **Builder Pattern**

**Mục đích**: Construct complex objects step by step

**Implementation** (Spring Security):
```java
http
    .csrf(csrf -> csrf.ignoringRequestMatchers(...))
    .authorizeHttpRequests(authz -> authz
        .requestMatchers(...).permitAll()
        .anyRequest().authenticated()
    )
    .cors(cors -> cors.configurationSource(...));
```

**Lợi ích**:
- Fluent API
- Step-by-step configuration
- Readable code

**Vị trí**: `config/SecurityConfig.java`

---

### 8. **Layered Architecture Pattern**

**Mục đích**: Tổ chức code thành các layers với trách nhiệm rõ ràng

**Structure**:
```
┌─────────────────────────────────┐
│   Controller Layer              │  ← HTTP requests/responses
│   (AuthController)             │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Service Layer                 │  ← Business logic
│   (UserService/UserServiceImpl) │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Repository Layer              │  ← Data access
│   (UserRepository)              │
└──────────────┬──────────────────┘
               │
┌──────────────▼──────────────────┐
│   Database (MongoDB)             │  ← Data storage
└─────────────────────────────────┘
```

**Lợi ích**:
- Separation of concerns
- Maintainability
- Testability
- Scalability

---

### 9. **Configuration Pattern**

**Mục đích**: Externalize configuration và tạo beans

**Implementation**:
```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(...) { }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() { }
}
```

**Lợi ích**:
- Centralized configuration
- Bean management
- Environment-specific configs

**Vị trí**: `config/` package

---

## 🏗 Kiến Trúc Hệ Thống

### **Request Flow**

```
1. Client Request
   ↓
2. Security Filter Chain (Spring Security)
   - CSRF check
   - CORS check
   - Authentication check
   ↓
3. Controller Layer
   - Request validation
   - Parameter binding
   ↓
4. Service Layer
   - Business logic
   - Transaction management
   - Password encoding
   ↓
5. Repository Layer
   - Database operations
   - Query execution
   ↓
6. MongoDB Database
   - Data persistence
   ↓
7. Response Flow (ngược lại)
   - Exception handling (nếu có)
   - Response serialization
   - HTTP response
```

### **Security Architecture**

```
┌─────────────────────────────────────────┐
│         Client (Frontend)                │
└──────────────┬──────────────────────────┘
               │ HTTP Request
               ↓
┌─────────────────────────────────────────┐
│      Spring Security Filter Chain        │
│  ┌──────────────────────────────────┐   │
│  │ 1. CSRF Protection                │   │
│  │ 2. CORS Configuration             │   │
│  │ 3. Authentication Filter          │   │
│  │ 4. Authorization Filter           │   │
│  └──────────────────────────────────┘   │
└──────────────┬──────────────────────────┘
               │
               ↓
┌─────────────────────────────────────────┐
│         Controller Endpoints            │
│  - /api/auth/register (Public)         │
│  - /api/auth/login (Public)            │
│  - /api/** (Protected)                 │
└─────────────────────────────────────────┘
```

### **Authentication Flow**

#### **1. Registration Flow**
```
Client → POST /api/auth/register
  ↓
Controller validates DTO
  ↓
Service checks email/username exists
  ↓
Service hashes password (BCrypt)
  ↓
Repository saves to MongoDB
  ↓
Response: 201 Created
```

#### **2. Login Flow (JWT)**
```
Client → POST /api/auth/login
  ↓
Controller validates credentials
  ↓
Service loads user from DB
  ↓
Service verifies password (BCrypt)
  ↓
Service generates JWT token
  ↓
Response: JWT token + user info
```

#### **3. OAuth2 Flow (Google)**
```
Client → GET /oauth2/authorization/google
  ↓
Redirect to Google login
  ↓
User authenticates with Google
  ↓
Google redirects with code
  ↓
Spring exchanges code for token
  ↓
Service creates/updates user
  ↓
Generate JWT token
  ↓
Response: JWT token
```

---

## 📁 Cấu Trúc Project

```
fashion-backend/
├── build.gradle                    # Dependencies & build config
├── settings.gradle                 # Project settings
├── gradlew / gradlew.bat          # Gradle wrapper
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── NT5118/Q11_backend/demo/
│   │   │       ├── DemoApplication.java          # Main entry point
│   │   │       │
│   │   │       ├── config/                       # Configuration classes
│   │   │       │   ├── SecurityConfig.java       # Spring Security config
│   │   │       │   ├── PasswordConfig.java       # Password encoder bean
│   │   │       │   └── GlobalExceptionHandler.java  # Exception handling
│   │   │       │
│   │   │       ├── controller/                   # REST Controllers
│   │   │       │   └── AuthController.java       # Authentication endpoints
│   │   │       │
│   │   │       ├── service/                       # Business logic
│   │   │       │   ├── UserService.java          # Service interface
│   │   │       │   └── UserServiceImpl.java      # Service implementation
│   │   │       │
│   │   │       ├── repository/                    # Data access
│   │   │       │   └── UserRepository.java       # MongoDB repository
│   │   │       │
│   │   │       ├── model/                         # Domain entities
│   │   │       │   └── User.java                 # User entity
│   │   │       │
│   │   │       └── dto/                           # Data Transfer Objects
│   │   │           └── UserRegistrationRequest.java
│   │   │
│   │   └── resources/
│   │       └── application.properties             # Application config
│   │
│   └── test/                                      # Test files
│       └── java/
│           └── NT5118/Q11_backend/demo/
│               └── DemoApplicationTests.java
│
└── README.md
```

### **Package Structure Explanation**

| Package | Responsibility | Examples |
|---------|---------------|----------|
| `config/` | Configuration beans, security, exception handling | `SecurityConfig`, `PasswordConfig` |
| `controller/` | REST endpoints, HTTP handling | `AuthController` |
| `service/` | Business logic, transactions | `UserService`, `UserServiceImpl` |
| `repository/` | Data access, database operations | `UserRepository` |
| `model/` | Domain entities, database documents | `User` |
| `dto/` | Data transfer objects, API contracts | `UserRegistrationRequest` |

---

## 🔄 Luồng Hoạt Động

### **1. Application Startup**

```
1. Spring Boot Application starts
   ↓
2. @SpringBootApplication scans packages
   ↓
3. Auto-configuration kicks in:
   - MongoDB connection
   - Spring Security setup
   - JWT configuration
   - OAuth2 client setup
   ↓
4. @Configuration classes load:
   - SecurityConfig → SecurityFilterChain
   - PasswordConfig → BCryptPasswordEncoder
   ↓
5. Beans are created and injected
   ↓
6. Server starts on port 8080
```

### **2. User Registration Flow**

```java
// 1. Client sends request
POST /api/auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}

// 2. SecurityConfig allows public access
.requestMatchers("/api/auth/register").permitAll()

// 3. AuthController receives request
@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest request)

// 4. Validation happens (Jakarta Validation)
@NotBlank, @Email, @Size annotations

// 5. UserService.register() is called
- Check email exists: userRepository.existsByEmail()
- Check username exists: userRepository.existsByUsername()
- Hash password: passwordEncoder.encode()
- Create User entity
- Save to MongoDB: userRepository.save()

// 6. Response sent
201 Created: {"message": "User registered successfully"}
```

### **3. Exception Handling Flow**

```
Exception occurs
  ↓
GlobalExceptionHandler catches it
  ↓
@ExceptionHandler method matches exception type
  ↓
Error response formatted
  ↓
HTTP error response sent to client
```

**Exception Types Handled**:
- `MethodArgumentNotValidException` → Validation errors
- `IllegalArgumentException` → Business logic errors (409 Conflict)
- `DataIntegrityViolationException` → Database constraint violations (409 Conflict)
- `Exception` → Generic errors (500 Internal Server Error)

---

## ✅ Best Practices

### **1. Security Best Practices**

✅ **Đã áp dụng**:
- Password hashing với BCrypt
- JWT token authentication
- CSRF protection (selective)
- CORS configuration
- Input validation

⚠️ **Cần cải thiện**:
- JWT secret nên dùng environment variable
- HTTPS trong production
- Rate limiting cho authentication endpoints
- Password strength requirements
- Account lockout after failed attempts

### **2. Code Organization**

✅ **Đã áp dụng**:
- Layered architecture
- Separation of concerns
- Interface-based design
- Constructor injection
- DTO pattern

### **3. Error Handling**

✅ **Đã áp dụng**:
- Global exception handler
- Consistent error responses
- Validation error messages
- HTTP status codes

### **4. Database Best Practices**

✅ **Đã áp dụng**:
- Unique indexes
- Document structure
- Repository pattern

⚠️ **Cần cải thiện**:
- Connection pooling configuration
- Index optimization
- Query performance monitoring
- Database migration strategy

### **5. API Design**

✅ **Đã áp dụng**:
- RESTful conventions
- HTTP status codes
- Request/Response DTOs
- Validation

⚠️ **Cần cải thiện**:
- API versioning
- Pagination
- Filtering & sorting
- API documentation (Swagger/OpenAPI)

---

## 🔧 Configuration Details

### **application.properties**

```properties
# Application
spring.application.name=demo
server.port=8080

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/fashion-mongodb

# JWT
app.jwt.secret=myVerySecretKeyForJWTTokenThatShouldBeAtLeast32Characters
app.jwt.expiration=86400000  # 24 hours in milliseconds

# OAuth2 Google
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}

# Logging
logging.level.org.springframework.security=DEBUG
```

### **Security Configuration**

```java
// Public endpoints (no authentication required)
/api/auth/register
/api/auth/login

// Protected endpoints (authentication required)
/api/** (all other endpoints)
```

### **CORS Configuration**

```java
Allowed Origins: 
- http://localhost:3000 (Frontend)
- http://localhost:8080 (Backend)

Allowed Methods: GET, POST, PUT, DELETE, OPTIONS
Allowed Headers: *
Allow Credentials: true
```

---

## 📊 Dependencies Summary

### **Core Dependencies**
- `spring-boot-starter-web` - Web MVC
- `spring-boot-starter-security` - Security
- `spring-boot-starter-data-mongodb` - MongoDB
- `spring-boot-starter-validation` - Validation

### **Security Dependencies**
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` - JWT
- `spring-boot-starter-oauth2-client` - OAuth2

### **Additional Dependencies**
- `spring-boot-starter-amqp` - RabbitMQ
- `spring-boot-starter-data-redis-reactive` - Redis
- `spring-boot-starter-mail` - Email

### **Development Dependencies**
- `spring-boot-devtools` - Development tools
- `spring-boot-starter-test` - Testing

---

## 🚀 Next Steps & Recommendations

### **Immediate Improvements**
1. ✅ Add JWT token generation service
2. ✅ Implement login endpoint
3. ✅ Add refresh token mechanism
4. ✅ Email verification
5. ✅ Password reset functionality

### **Future Enhancements**
1. API documentation với Swagger/OpenAPI
2. Unit tests và Integration tests
3. Logging strategy (Logback/SLF4J)
4. Monitoring và metrics (Actuator)
5. Docker containerization
6. CI/CD pipeline
7. API versioning
8. Rate limiting
9. Caching strategy
10. Message queue implementation

---

## 📚 Tài Liệu Tham Khảo

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [JWT.io](https://jwt.io/)
- [OAuth2 Specification](https://oauth.net/2/)

---

**Tác giả**: Fashion Backend Team  
**Cập nhật**: 2024  
**Version**: 0.0.1-SNAPSHOT

