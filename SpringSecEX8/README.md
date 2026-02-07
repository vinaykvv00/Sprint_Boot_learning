# 🔐 Spring Security with Database Authentication - Section 33

A complete beginner-friendly guide to understanding Spring Boot Security with Database Authentication using JPA and PostgreSQL.

---

## 📋 Table of Contents

1. [Project Overview](#-project-overview)
2. [Technologies Used](#-technologies-used)
3. [Project Architecture](#-project-architecture)
4. [Folder Structure](#-folder-structure)
5. [Layer-by-Layer Explanation](#-layer-by-layer-explanation)
6. [How Authentication Works](#-how-authentication-works)
7. [Database Setup](#-database-setup)
8. [API Endpoints](#-api-endpoints)
9. [Testing with Postman](#-testing-with-postman)
10. [Key Concepts for Beginners](#-key-concepts-for-beginners)

---

## 🎯 Project Overview

This project demonstrates how to implement **database-based authentication** in a Spring Boot application. Instead of hardcoding usernames and passwords in memory, we store user credentials in a **PostgreSQL database** and authenticate users against it.

### What We Built:
- A REST API with secured endpoints
- User authentication from PostgreSQL database
- Custom `UserDetailsService` implementation
- Stateless session management (no cookies)
- Basic HTTP Authentication

---

## 🛠 Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 4.0.0 | Main framework for building the application |
| **Spring Security** | (included) | Provides authentication and authorization |
| **Spring Data JPA** | (included) | ORM for database operations |
| **Hibernate** | (included) | JPA implementation for database mapping |
| **PostgreSQL** | 42.7.8 | Relational database to store users |
| **Maven** | - | Dependency management and build tool |
| **Java** | 21 | Programming language |

---

## 🏗 Project Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              CLIENT (Postman)                           │
│                    Sends HTTP Request with Basic Auth                   │
│                    (Username + Password in Header)                      │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         SPRING SECURITY FILTER                          │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  SecurityFilterChain                                             │   │
│  │  - Intercepts all requests                                       │   │
│  │  - Checks if user is authenticated                               │   │
│  │  - Uses Basic HTTP Authentication                                │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      AUTHENTICATION PROVIDER                            │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  DaoAuthenticationProvider                                       │   │
│  │  - Uses UserDetailsService to load user from DB                  │   │
│  │  - Compares password using PasswordEncoder                       │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         SERVICE LAYER                                   │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  MyUserDetailsService                                            │   │
│  │  - Implements UserDetailsService interface                       │   │
│  │  - Loads user from database by username                          │   │
│  │  - Returns UserPrinciple object                                  │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                        REPOSITORY LAYER                                 │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  UserRepo (JpaRepository)                                        │   │
│  │  - findByUsername(String username)                               │   │
│  │  - Communicates with PostgreSQL Database                         │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         DATABASE (PostgreSQL)                           │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  Table: users                                                    │   │
│  │  ┌────────┬──────────────┬──────────────┐                       │   │
│  │  │   id   │   username   │   password   │                       │   │
│  │  ├────────┼──────────────┼──────────────┤                       │   │
│  │  │   1    │    user1     │    u@123     │                       │   │
│  │  │   2    │    user2     │    u2@123    │                       │   │
│  │  └────────┴──────────────┴──────────────┘                       │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                    ✅ If Authenticated → Access Controller
                    ❌ If Not Authenticated → 401 Unauthorized
```

---

## 📁 Folder Structure

```
SpringSecEX7/
│
├── 📄 pom.xml                          # Maven dependencies configuration
├── 📄 README.md                        # This documentation file
│
└── src/
    └── main/
        ├── java/com/telusko/SpringSecEX/
        │   │
        │   ├── 📄 SpringSecExApplication.java    # 🚀 Main entry point
        │   │
        │   ├── 📁 config/                        # ⚙️ CONFIGURATION LAYER
        │   │   └── 📄 SecurityConfig.java        # Security configuration
        │   │
        │   ├── 📁 controller/                    # 🎮 CONTROLLER LAYER (REST APIs)
        │   │   ├── 📄 HelloController.java       # Simple hello endpoint
        │   │   └── 📄 StudentController.java     # Student CRUD endpoints
        │   │
        │   ├── 📁 model/                         # 📦 MODEL LAYER (Entities/DTOs)
        │   │   ├── 📄 Users.java                 # User entity (JPA)
        │   │   ├── 📄 UserPrinciple.java         # UserDetails implementation
        │   │   └── 📄 Student.java               # Student model
        │   │
        │   ├── 📁 repo/                          # 💾 REPOSITORY LAYER (Database Access)
        │   │   └── 📄 UserRepo.java              # User repository interface
        │   │
        │   └── 📁 service/                       # 🔧 SERVICE LAYER (Business Logic)
        │       └── 📄 MyUserDetailsService.java  # Custom UserDetailsService
        │
        └── resources/
            └── 📄 application.properties         # App configuration
```

---

## 📚 Layer-by-Layer Explanation

### 1️⃣ Main Application Class

**File:** `SpringSecExApplication.java`

```java
@SpringBootApplication
public class SpringSecExApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecExApplication.class, args);
    }
}
```

**What it does:**
- 🚀 **Entry point** of the Spring Boot application
- `@SpringBootApplication` = `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`
- Starts the embedded Tomcat server and loads all beans

---

### 2️⃣ Configuration Layer

**File:** `config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http
                .csrf(customizer -> customizer.disable())           // Disable CSRF for REST APIs
                .authorizeHttpRequests(request -> request
                    .anyRequest().authenticated())                   // All requests need authentication
                .httpBasic(Customizer.withDefaults())               // Use Basic Auth
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // No sessions
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }
}
```

**What each annotation/method does:**

| Annotation/Method | Purpose |
|-------------------|---------|
| `@Configuration` | Marks this class as a source of bean definitions |
| `@EnableWebSecurity` | Enables Spring Security's web security support |
| `@Autowired` | Injects the UserDetailsService bean automatically |
| `@Bean` | Creates and registers a bean in Spring container |
| `.csrf().disable()` | Disables CSRF protection (needed for REST APIs) |
| `.authorizeHttpRequests()` | Configures which URLs need authentication |
| `.httpBasic()` | Enables HTTP Basic Authentication |
| `.sessionCreationPolicy(STATELESS)` | No server-side session (good for REST APIs) |
| `DaoAuthenticationProvider` | Authenticates using UserDetailsService |
| `NoOpPasswordEncoder` | No password encoding (⚠️ only for learning!) |

---

### 3️⃣ Model Layer (Entities)

#### **File:** `model/Users.java` - JPA Entity

```java
@Entity                              // Marks as JPA entity (database table)
public class Users {
    @Id                              // Primary key
    private int id;
    private String username;
    private String password;
    
    // Getters and Setters...
}
```

**What it does:**
- 📦 Represents the `users` table in PostgreSQL
- `@Entity` tells Hibernate to create/map this class to a database table
- `@Id` marks the primary key field
- Hibernate automatically creates the table if `spring.jpa.hibernate.ddl-auto=update`

**Database Table Created:**
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255)
);
```

---

#### **File:** `model/UserPrinciple.java` - UserDetails Implementation

```java
public class UserPrinciple implements UserDetails {

    private Users user;
    
    public UserPrinciple(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    
    @Override
    public boolean isAccountNonLocked() { return true; }
    
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    
    @Override
    public boolean isEnabled() { return true; }
}
```

**What it does:**
- 🔐 **Bridge between our `Users` entity and Spring Security**
- Implements `UserDetails` interface (required by Spring Security)
- Spring Security uses this to get username, password, and authorities

**Why do we need this?**
- Spring Security doesn't know about our `Users` class
- `UserDetails` is what Spring Security understands
- `UserPrinciple` wraps our `Users` and provides it in the format Spring Security expects

---

#### **File:** `model/Student.java` - Simple Model

```java
public class Student {
    private int id;
    private String name;
    private int marks;
    
    // Constructor, Getters, Setters...
}
```

**What it does:**
- 📦 Simple POJO (Plain Old Java Object) for Student data
- Not a JPA entity (no `@Entity`) - just used for API responses
- Stored in memory (List), not in database

---

### 4️⃣ Repository Layer

**File:** `repo/UserRepo.java`

```java
public interface UserRepo extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
}
```

**What it does:**
- 💾 **Data Access Layer** - communicates with the database
- Extends `JpaRepository<Users, Integer>`:
  - `Users` = Entity type
  - `Integer` = Primary key type
- Gets **FREE CRUD methods** from JpaRepository:
  - `save()`, `findById()`, `findAll()`, `delete()`, etc.

**Custom Query Method:**
```java
Users findByUsername(String username);
```
- Spring Data JPA **automatically generates the SQL query**!
- Method name `findByUsername` → `SELECT * FROM users WHERE username = ?`

**This is the magic of Spring Data JPA!** 🪄

---

### 5️⃣ Service Layer

**File:** `service/MyUserDetailsService.java`

```java
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found " + username);
        }
        
        return new UserPrinciple(user);
    }
}
```

**What it does:**
- 🔧 **Business Logic Layer** for authentication
- `@Service` marks it as a Spring service bean
- Implements `UserDetailsService` interface (required by Spring Security)
- `loadUserByUsername()` is called by Spring Security during authentication

**Flow:**
1. User sends username/password
2. Spring Security calls `loadUserByUsername(username)`
3. We fetch user from database using `repo.findByUsername()`
4. If found, wrap it in `UserPrinciple` and return
5. If not found, throw `UsernameNotFoundException`

---

### 6️⃣ Controller Layer

#### **File:** `controller/HelloController.java`

```java
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }
}
```

**What it does:**
- 🎮 Simple REST endpoint
- `@RestController` = `@Controller` + `@ResponseBody`
- Returns plain text response

---

#### **File:** `controller/StudentController.java`

```java
@RestController
public class StudentController {

    private List<Student> students = List.of(
        new Student(1, "John Doe", 120),
        new Student(2, "Jane Smith", 130),
        new Student(3, "Alice Johnson", 150)
    );

    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}
```

**What it does:**
- 🎮 REST API for Student operations
- `@GetMapping("/students")` - GET request to fetch all students
- `@PostMapping("/students")` - POST request to add a student
- `@RequestBody` - Converts JSON request body to Java object

---

## 🔄 How Authentication Works

### Step-by-Step Flow:

```
┌──────────────────────────────────────────────────────────────────────────┐
│  STEP 1: Client sends request with Basic Auth header                     │
│  ────────────────────────────────────────────────────────────────────── │
│  GET /students HTTP/1.1                                                  │
│  Authorization: Basic dXNlcjE6dUAxMjM=   (base64 of "user1:u@123")      │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  STEP 2: SecurityFilterChain intercepts the request                      │
│  ────────────────────────────────────────────────────────────────────── │
│  - Decodes Base64 header to get username and password                    │
│  - username = "user1", password = "u@123"                                │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  STEP 3: DaoAuthenticationProvider authenticates                         │
│  ────────────────────────────────────────────────────────────────────── │
│  - Calls userDetailsService.loadUserByUsername("user1")                  │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  STEP 4: MyUserDetailsService loads user from database                   │
│  ────────────────────────────────────────────────────────────────────── │
│  - Calls repo.findByUsername("user1")                                    │
│  - SQL: SELECT * FROM users WHERE username = 'user1'                     │
│  - Returns Users object with id=1, username="user1", password="u@123"    │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  STEP 5: Wrap in UserPrinciple and return                                │
│  ────────────────────────────────────────────────────────────────────── │
│  - new UserPrinciple(user) is returned                                   │
│  - Spring Security gets username, password, authorities from it          │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  STEP 6: Password comparison                                             │
│  ────────────────────────────────────────────────────────────────────── │
│  - DaoAuthenticationProvider compares:                                   │
│    - Password from request: "u@123"                                      │
│    - Password from database: "u@123"                                     │
│  - Using NoOpPasswordEncoder (plain text comparison)                     │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  STEP 7: Result                                                          │
│  ────────────────────────────────────────────────────────────────────── │
│  ✅ If passwords match → Request proceeds to Controller                  │
│  ❌ If passwords don't match → 401 Unauthorized response                 │
└──────────────────────────────────────────────────────────────────────────┘
```

---

## 🗄 Database Setup

### 1. Install PostgreSQL

Download from: https://www.postgresql.org/download/

### 2. Create Database

```sql
CREATE DATABASE Springboot_db;
```

### 3. Create Users Table (Auto-created by Hibernate)

The table is automatically created because of:
```properties
spring.jpa.hibernate.ddl-auto=update
```

### 4. Insert Test Users

```sql
INSERT INTO users (id, username, password) VALUES (1, 'user1', 'u@123');
INSERT INTO users (id, username, password) VALUES (2, 'user2', 'u2@123');
```

### Configuration (`application.properties`)

```properties
# Application Name
spring.application.name=SpringSecEX

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/Springboot_db
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update    # Auto-create/update tables
spring.jpa.show-sql=true                 # Show SQL queries in console
```

---

## 🌐 API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/` | Hello World message | ✅ Yes |
| GET | `/students` | Get all students | ✅ Yes |
| POST | `/students` | Add a new student | ✅ Yes |
| GET | `/csrf-token` | Get CSRF token | ✅ Yes |

---

## 🧪 Testing with Postman

### Step 1: Open Postman

### Step 2: Create a GET Request
- URL: `http://localhost:8080/students`

### Step 3: Add Basic Authentication
1. Go to **Authorization** tab
2. Select **Type:** `Basic Auth`
3. Enter credentials:
   - **Username:** `user1`
   - **Password:** `u@123`

### Step 4: Send Request

**Expected Response (200 OK):**
```json
[
    {"id": 1, "name": "John Doe", "marks": 120},
    {"id": 2, "name": "Jane Smith", "marks": 130},
    {"id": 3, "name": "Alice Johnson", "marks": 150}
]
```

**If wrong credentials (401 Unauthorized):**
```json
{
    "timestamp": "2026-01-14T...",
    "status": 401,
    "error": "Unauthorized",
    "path": "/students"
}
```

---

## 📖 Key Concepts for Beginners

### 1. What is Spring Security?
Spring Security is a framework that provides authentication (who are you?) and authorization (what can you do?) for Spring applications.

### 2. What is Basic Authentication?
- Client sends username:password encoded in Base64 in the `Authorization` header
- Format: `Authorization: Basic base64(username:password)`
- Simple but not secure without HTTPS

### 3. What is JPA?
- **Java Persistence API** - a specification for ORM (Object-Relational Mapping)
- Maps Java objects to database tables
- No need to write SQL queries manually

### 4. What is Hibernate?
- The most popular **implementation** of JPA
- Handles the actual database operations
- Spring Boot uses Hibernate by default

### 5. What is Spring Data JPA?
- Sits on top of JPA/Hibernate
- Provides repository interfaces with CRUD operations
- Generates SQL queries from method names automatically

### 6. What is a Bean?
- An object managed by Spring's IoC (Inversion of Control) container
- Created using `@Bean`, `@Component`, `@Service`, `@Repository`, `@Controller`
- Spring injects them where needed using `@Autowired`

### 7. What is @Autowired?
- Tells Spring to inject a dependency automatically
- Spring finds the matching bean and injects it
- No need to manually create objects with `new`

### 8. Stateless vs Stateful Sessions
- **Stateful:** Server stores user session (uses cookies)
- **Stateless:** Server doesn't store session, client sends credentials every time
- REST APIs typically use stateless authentication

---

## ⚠️ Important Notes for Production

1. **Never use NoOpPasswordEncoder in production!**
   - Use `BCryptPasswordEncoder` instead
   - Always hash passwords before storing

2. **Always use HTTPS**
   - Basic Auth sends credentials in Base64 (easily decoded)
   - HTTPS encrypts the entire request

3. **Consider using JWT tokens**
   - More secure than Basic Auth
   - Better for scalable applications

---

## 🎓 Summary

| Layer | Class | Purpose |
|-------|-------|---------|
| **Entry Point** | `SpringSecExApplication` | Starts the application |
| **Config** | `SecurityConfig` | Configures security rules |
| **Controller** | `StudentController`, `HelloController` | Handles HTTP requests |
| **Service** | `MyUserDetailsService` | Business logic for authentication |
| **Repository** | `UserRepo` | Database access |
| **Model** | `Users`, `UserPrinciple`, `Student` | Data structures |

---

## 🔗 Learning Path

1. ✅ **Section 33:** Database Authentication (This project)
2. ➡️ **Next:** Password Encoding with BCrypt
3. ➡️ **Next:** JWT Token Authentication
4. ➡️ **Next:** Role-based Authorization
5. ➡️ **Next:** OAuth2 / Social Login

---

**Happy Learning! 🚀**

*Created for Spring Boot Security Learning - Section 33*

