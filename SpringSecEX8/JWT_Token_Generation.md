# 🔑 JWT Token Generation - Spring Security Implementation

A complete beginner-friendly guide to understanding JWT (JSON Web Token) implementation in Spring Boot Security.

---

## 📋 Table of Contents

1. [What is JWT?](#-what-is-jwt)
2. [Why Use JWT Instead of Basic Auth?](#-why-use-jwt-instead-of-basic-auth)
3. [JWT Structure Explained](#-jwt-structure-explained)
4. [Project Evolution](#-project-evolution)
5. [New Dependencies Added](#-new-dependencies-added)
6. [JWT Token Generation Flow](#-jwt-token-generation-flow)
7. [Code Implementation Details](#-code-implementation-details)
8. [API Endpoints](#-api-endpoints)
9. [Testing with Postman](#-testing-with-postman)
10. [Key Concepts](#-key-concepts)
11. [What's Next?](#-whats-next)

---

## 🎯 What is JWT?

**JWT (JSON Web Token)** is a compact, URL-safe token format used for securely transmitting information between parties as a JSON object.

### Simple Analogy:
Think of JWT like a **concert ticket**:
- ✅ You buy a ticket once (login and get token)
- ✅ You show the ticket every time you enter (send token with each request)
- ✅ The ticket has your information and expiry date
- ✅ Security checks the ticket's authenticity without calling the box office each time

### JWT Token Example:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcwNzMxNjgwMCwiZXhwIjoxNzA3MzIwNDAwfQ.8kZq3Y-vN5xH9K5rR8fL3mP6wQ2sT7uV1xW4yZ0aB1c
```

---

## 🤔 Why Use JWT Instead of Basic Auth?

| Feature | Basic Auth | JWT Token |
|---------|------------|-----------|
| **Credentials Sent** | Username + Password every time | Only during login |
| **Security** | ❌ Credentials exposed in every request | ✅ Token contains no credentials |
| **Stateless** | ✅ Yes | ✅ Yes |
| **Scalability** | ⚠️ Moderate | ✅ Excellent |
| **Mobile Apps** | ❌ Not ideal | ✅ Perfect |
| **Expiration** | ❌ No built-in expiry | ✅ Has expiration time |
| **Performance** | ⚠️ DB query every request | ✅ No DB query needed |

### The Problem with Basic Auth:
```
Every Request:
┌────────────────────────────────────────┐
│ GET /students                          │
│ Authorization: Basic dXNlcjE6dUAxMjM=  │  ← Username:Password sent EVERY time
└────────────────────────────────────────┘
```

### The JWT Solution:
```
Login Once:
┌────────────────────────────────────────┐
│ POST /login                            │
│ Body: {"username":"user1","password":"u@123"} │
└────────────────────────────────────────┘
         ↓
Response: eyJhbGciOiJIUzI1NiJ9... (JWT Token)

Then Use Token:
┌────────────────────────────────────────┐
│ GET /students                          │
│ Authorization: Bearer eyJhbGc...       │  ← Only token, no password!
└────────────────────────────────────────┘
```

---

## 🧩 JWT Structure Explained

A JWT consists of **3 parts** separated by dots (`.`):

```
HEADER.PAYLOAD.SIGNATURE
```

### Example JWT Token:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcwNzMxNjgwMCwiZXhwIjoxNzA3MzIwNDAwfQ.8kZq3Y-vN5xH9K5rR8fL3mP6wQ2sT7uV1xW4yZ0aB1c
```

---

### Part 1: HEADER (Algorithm & Token Type)
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
- **alg:** Algorithm used for signing (HMAC SHA-256)
- **typ:** Type of token (JWT)

**Encoded (Base64URL):** `eyJhbGciOiJIUzI1NiJ9`

---

### Part 2: PAYLOAD (Claims/Data)
```json
{
  "sub": "user1",           // Subject (username)
  "iat": 1707316800,        // Issued At (timestamp)
  "exp": 1707320400         // Expiration Time (timestamp)
}
```
- **sub (Subject):** The user this token belongs to
- **iat (Issued At):** When the token was created
- **exp (Expiration):** When the token expires

**Encoded (Base64URL):** `eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcwNzMxNjgwMCwiZXhwIjoxNzA3MzIwNDAwfQ`

---

### Part 3: SIGNATURE (Verification)
```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret_key
)
```

**Purpose:** Ensures the token hasn't been tampered with

**Encoded:** `8kZq3Y-vN5xH9K5rR8fL3mP6wQ2sT7uV1xW4yZ0aB1c`

---

### How JWT Prevents Tampering:

❌ **Attacker tries to change payload:**
```
Original: {"sub":"user1","exp":1707320400}
Hacker changes to: {"sub":"admin","exp":9999999999}
```

✅ **Server detects tampering:**
```java
// Server re-calculates signature with secret key
// New signature doesn't match original signature
// Token is REJECTED!
```

**Only the server with the secret key can create valid signatures!** 🔐

---

## 📈 Project Evolution

### Phase 1: Basic Authentication (Section 33)
```
Username + Password → Spring Security → Database Check → Access Granted
```

### Phase 2: BCrypt Password Encoding
```
Password → BCrypt Hashing → Store in DB
Login → BCrypt Compare → Access Granted
```

### Phase 3: JWT Token Generation (Current)
```
Login → Verify Credentials → Generate JWT Token → Return Token
Use Token → Validate Token (Next Step) → Access Granted
```

---

## 📦 New Dependencies Added

### pom.xml - JWT Dependencies

```xml
<!-- JWT API -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.13.0</version>
    <scope>compile</scope>
</dependency>

<!-- JWT Implementation -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.13.0</version>
    <scope>runtime</scope>
</dependency>

<!-- JWT Jackson (JSON Processing) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.13.0</version>
    <scope>runtime</scope>
</dependency>
```

### What Each Dependency Does:

| Dependency | Purpose |
|------------|---------|
| **jjwt-api** | Core JWT API interfaces |
| **jjwt-impl** | Actual implementation of JWT operations |
| **jjwt-jackson** | JSON processing for JWT payload |

---

## 🔄 JWT Token Generation Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 1: User Registration                                              │
│  ────────────────────────────────────────────────────────────────────  │
│  Client → POST /register                                                │
│  Body: {"id": 1, "username": "user1", "password": "u@123"}             │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 2: UserController receives request                               │
│  ────────────────────────────────────────────────────────────────────  │
│  @PostMapping("/register")                                              │
│  public Users register(@RequestBody Users user)                         │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 3: UserService processes registration                            │
│  ────────────────────────────────────────────────────────────────────  │
│  1. Encode password using BCrypt:                                       │
│     "u@123" → "$2a$10$xK7h8..."  (hashed)                              │
│  2. Save to database                                                    │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  DATABASE: users table                                                  │
│  ┌─────┬──────────┬─────────────────────────────────────────┐         │
│  │ id  │ username │ password                                 │         │
│  ├─────┼──────────┼─────────────────────────────────────────┤         │
│  │ 1   │ user1    │ $2a$10$xK7h8...  (BCrypt hash)          │         │
│  └─────┴──────────┴─────────────────────────────────────────┘         │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                        ✅ User Registered Successfully!

═════════════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 4: User Login                                                     │
│  ────────────────────────────────────────────────────────────────────  │
│  Client → POST /login                                                   │
│  Body: {"username": "user1", "password": "u@123"}                      │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 5: UserController receives login request                         │
│  ────────────────────────────────────────────────────────────────────  │
│  @PostMapping("/login")                                                 │
│  public String login(@RequestBody Users user)                           │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 6: UserService.verify() - Authentication                         │
│  ────────────────────────────────────────────────────────────────────  │
│  authenticationManager.authenticate(                                    │
│      new UsernamePasswordAuthenticationToken(                           │
│          "user1", "u@123"                                               │
│      )                                                                  │
│  )                                                                      │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 7: AuthenticationManager → AuthenticationProvider                │
│  ────────────────────────────────────────────────────────────────────  │
│  DaoAuthenticationProvider:                                             │
│  1. Calls MyUserDetailsService.loadUserByUsername("user1")             │
│  2. Fetches user from database                                          │
│  3. Compares passwords using BCrypt:                                    │
│     - Input: "u@123"                                                    │
│     - DB Hash: "$2a$10$xK7h8..."                                        │
│     - BCrypt.matches() → TRUE ✅                                        │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 8: Authentication Successful → Generate JWT Token                │
│  ────────────────────────────────────────────────────────────────────  │
│  if (authentication.isAuthenticated()) {                                │
│      return jwtService.generateToken("user1");                          │
│  }                                                                      │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 9: JWTService.generateToken() - Token Creation                   │
│  ────────────────────────────────────────────────────────────────────  │
│  1. Create empty claims map                                             │
│  2. Build JWT:                                                          │
│     - Subject: "user1"                                                  │
│     - Issued At: Current timestamp (e.g., 1707316800)                  │
│     - Expiration: Current time + 1 hour (1707320400)                   │
│  3. Sign with Secret Key (HMAC SHA-256)                                │
│  4. Compact to string                                                   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  STEP 10: Return JWT Token to Client                                   │
│  ────────────────────────────────────────────────────────────────────  │
│  Response Body:                                                         │
│  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcwNzMxNjgwMC... │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                    ✅ Client stores token for future requests!
```

---

## 💻 Code Implementation Details

### 1️⃣ JWTService Class

**File:** `service/JWTService.java`

```java
@Service
public class JWTService {

    private String secretkey = "";

    // Constructor: Generate Secret Key
    public JWTService() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSha256");
        SecretKey sk = keyGen.generateKey();
        byte[] encoded = sk.getEncoded();
        secretkey = java.util.Base64.getEncoder().encodeToString(encoded);
    }

    // Generate JWT Token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)                                    // Add custom claims (empty for now)
                .subject(username)                               // Set subject (username)
                .issuedAt(new Date(System.currentTimeMillis()))  // Issue time
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .and()
                .signWith(getKey())                              // Sign with secret key
                .compact();                                      // Convert to string
    }

    // Get Secret Key for Signing
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

---

### 📝 Line-by-Line Explanation

#### Secret Key Generation (Constructor):

```java
public JWTService() throws NoSuchAlgorithmException {
    KeyGenerator keyGen = KeyGenerator.getInstance("HmacSha256");
```
- **KeyGenerator:** Java's built-in class for generating cryptographic keys
- **"HmacSha256":** Algorithm for creating symmetric keys (same key for signing and verifying)

```java
    SecretKey sk = keyGen.generateKey();
```
- Generates a random 256-bit secret key

```java
    byte[] encoded = sk.getEncoded();
    secretkey = java.util.Base64.getEncoder().encodeToString(encoded);
```
- Converts the secret key to Base64 string for storage
- Example output: `"xK7h8vN5qP2wR9sT1uV4yZ6aB3cD5eF7gH8jK9mL0nO1pQ2sT3uV4wX5yZ6a"`

**Why Base64?**
- Secret keys are binary data (bytes)
- Base64 converts binary to readable text
- Easier to store and transmit

---

#### Token Generation Method:

```java
public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
```
- **Claims:** Additional data you want to store in the token
- Currently empty, but you can add: `claims.put("role", "ADMIN")`

```java
    return Jwts.builder()
```
- **Jwts.builder():** Starts building a JWT token

```java
        .claims()
        .add(claims)
```
- Adds custom claims (currently empty)

```java
        .subject(username)
```
- **Subject:** The primary identifier (username)
- This is who the token belongs to

```java
        .issuedAt(new Date(System.currentTimeMillis()))
```
- **issuedAt (iat):** Timestamp when token was created
- Example: `1707316800` (February 7, 2024, 10:00:00)

```java
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
```
- **Expiration (exp):** When the token expires
- `1000` = milliseconds to seconds
- `60` = seconds to minutes
- `60` = minutes to hours
- **Result:** Token valid for 1 hour

```java
        .and()
        .signWith(getKey())
```
- **signWith():** Signs the token with the secret key
- Creates the SIGNATURE part of JWT

```java
        .compact();
```
- Combines HEADER + PAYLOAD + SIGNATURE
- Returns final JWT string

---

#### Get Secret Key Method:

```java
private Key getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretkey);
```
- Decodes the Base64 secret key back to bytes

```java
    return Keys.hmacShaKeyFor(keyBytes);
}
```
- Converts bytes to a Key object for signing

---

### 2️⃣ UserService Class

**File:** `service/UserService.java`

```java
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register new user
    public Users register(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // Verify login and generate token
    public String verify(Users user) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "Invalid user credentials";
        }
    }
}
```

---

### 📝 Line-by-Line Explanation

#### Register Method:

```java
public Users register(Users user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
```
- **BCryptPasswordEncoder:** Hashes the password before storing
- Example: `"u@123"` → `"$2a$10$xK7h8vN5qP2wR9sT1uV4yZ..."`

```java
    return userRepo.save(user);
}
```
- Saves user to database with hashed password

---

#### Verify Method (Login):

```java
public String verify(Users user) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
    );
```
- **authenticationManager:** Spring Security's authentication manager
- **UsernamePasswordAuthenticationToken:** Wraps username and password
- **authenticate():** Verifies credentials against database
  - Internally calls `MyUserDetailsService.loadUserByUsername()`
  - Compares password using BCrypt

```java
    if (authentication.isAuthenticated()) {
        return jwtService.generateToken(user.getUsername());
```
- If authentication successful, generate JWT token

```java
    } else {
        return "Invalid user credentials";
    }
}
```
- If authentication fails, return error message

---

### 3️⃣ UserController Class

**File:** `controller/UserController.java`

```java
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return userService.verify(user);
    }
}
```

---

### 📝 Explanation

#### Register Endpoint:

```java
@PostMapping("/register")
public Users register(@RequestBody Users user)
```
- **@PostMapping:** Handles POST requests to `/register`
- **@RequestBody:** Converts JSON request to `Users` object
- Returns the registered user

---

#### Login Endpoint:

```java
@PostMapping("/login")
public String login(@RequestBody Users user)
```
- **@PostMapping:** Handles POST requests to `/login`
- **@RequestBody:** Converts JSON request to `Users` object
- Returns JWT token string

---

### 4️⃣ SecurityConfig Updates

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
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login","/register").permitAll()  // Allow without auth
                        .anyRequest().authenticated())                       // Others need auth
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

---

### 📝 Key Changes

#### Permit /login and /register:

```java
.requestMatchers("/login","/register").permitAll()
```
- **permitAll():** These endpoints don't require authentication
- Users can register and login without being authenticated first

---

#### BCrypt Password Encoder:

```java
provider.setPasswordEncoder(new BCryptPasswordEncoder());
```
- Replaces `NoOpPasswordEncoder` (plain text)
- Uses BCrypt for secure password hashing

---

#### AuthenticationManager Bean:

```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}
```
- Creates `AuthenticationManager` bean
- Needed for manual authentication in `UserService.verify()`

---

## 🌐 API Endpoints

| Method | Endpoint | Description | Auth Required | Request Body | Response |
|--------|----------|-------------|---------------|--------------|----------|
| POST | `/register` | Register new user | ❌ No | `{"id": 1, "username": "user1", "password": "u@123"}` | User object |
| POST | `/login` | Login and get JWT token | ❌ No | `{"username": "user1", "password": "u@123"}` | JWT token string |
| GET | `/students` | Get all students | ✅ Yes | None | Student list |
| GET | `/` | Hello message | ✅ Yes | None | "Hello World!" |

---

## 🧪 Testing with Postman

### Test 1: Register a New User

1. **Open Postman**
2. **Create POST Request:**
   - URL: `http://localhost:8080/register`
   - Method: `POST`
3. **Set Headers:**
   - `Content-Type: application/json`
4. **Request Body (JSON):**
   ```json
   {
       "id": 1,
       "username": "user1",
       "password": "u@123"
   }
   ```
5. **Send Request**

**Expected Response (200 OK):**
```json
{
    "id": 1,
    "username": "user1",
    "password": "$2a$10$xK7h8vN5qP2wR9sT1uV4yZ..."
}
```

**Database After Registration:**
```sql
SELECT * FROM users;

 id | username | password
----+----------+---------------------------------------
 1  | user1    | $2a$10$xK7h8vN5qP2wR9sT1uV4yZ...
```

---

### Test 2: Login and Get JWT Token

1. **Create POST Request:**
   - URL: `http://localhost:8080/login`
   - Method: `POST`
2. **Set Headers:**
   - `Content-Type: application/json`
3. **Request Body (JSON):**
   ```json
   {
       "username": "user1",
       "password": "u@123"
   }
   ```
4. **Send Request**

**Expected Response (200 OK):**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcwNzMxNjgwMCwiZXhwIjoxNzA3MzIwNDAwfQ.8kZq3Y-vN5xH9K5rR8fL3mP6wQ2sT7uV1xW4yZ0aB1c
```

**Copy this token!** You'll use it for accessing protected endpoints.

---

### Test 3: Decode JWT Token (Optional - For Learning)

Visit: **https://jwt.io/**

Paste your token to see:

**HEADER:**
```json
{
  "alg": "HS256"
}
```

**PAYLOAD:**
```json
{
  "sub": "user1",
  "iat": 1707316800,
  "exp": 1707320400
}
```

**SIGNATURE:**
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  your-256-bit-secret
) secret base64 encoded
```

---

### Test 4: Access Protected Endpoint (Still uses Basic Auth for now)

**Note:** Token validation is not implemented yet, so we still use Basic Auth.

1. **Create GET Request:**
   - URL: `http://localhost:8080/students`
   - Method: `GET`
2. **Authorization Tab:**
   - Type: `Basic Auth`
   - Username: `user1`
   - Password: `u@123`
3. **Send Request**

**Expected Response (200 OK):**
```json
[
    {"id": 1, "name": "John Doe", "marks": 120},
    {"id": 2, "name": "Jane Smith", "marks": 130},
    {"id": 3, "name": "Alice Johnson", "marks": 150}
]
```

---

## 📖 Key Concepts

### 1. What is a Secret Key?

**Secret Key** is a cryptographic key used to sign and verify JWT tokens.

**Analogy:** Think of it like a **signature stamp**:
- Only you have the stamp (secret key)
- You stamp documents (sign JWT tokens)
- Others can verify it's your stamp, but can't create it themselves

**In our code:**
```java
KeyGenerator keyGen = KeyGenerator.getInstance("HmacSha256");
SecretKey sk = keyGen.generateKey();
```
- Generates a random 256-bit key
- Stored in `secretkey` variable
- Used for signing and verifying tokens

---

### 2. What is HMAC SHA-256?

**HMAC (Hash-based Message Authentication Code)** is a cryptographic algorithm.

**How it works:**
```
HMAC-SHA256(message, secret_key) = signature
```

**Example:**
```java
Message: {"sub":"user1","iat":1707316800,"exp":1707320400}
Secret Key: xK7h8vN5qP2wR9sT1uV4yZ...
↓
Signature: 8kZq3Y-vN5xH9K5rR8fL3mP6wQ2sT7uV1xW4yZ0aB1c
```

**Properties:**
- ✅ Same input → Same output (deterministic)
- ✅ Different input → Completely different output
- ❌ Can't reverse-engineer the secret key from signature
- ✅ Very fast to compute

---

### 3. What is Base64 Encoding?

**Base64** converts binary data to text using 64 characters (A-Z, a-z, 0-9, +, /).

**Why use it?**
- JWT tokens are transmitted over HTTP (text-based)
- Binary data can't be sent in URLs or HTTP headers
- Base64 makes it URL-safe

**Example:**
```
Original: {"alg":"HS256"}
Base64: eyJhbGciOiJIUzI1NiJ9
```

**⚠️ Important:** Base64 is **NOT encryption!**
- Anyone can decode it
- It's just encoding for transmission
- Security comes from the signature, not encoding

---

### 4. Token Expiration

**Why tokens expire:**
- ✅ Limits damage if token is stolen
- ✅ Forces re-authentication periodically
- ✅ Allows revoking access after certain time

**In our code:**
```java
.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
```

**Breakdown:**
- `System.currentTimeMillis()` = Current time in milliseconds
- `1000` = 1 second
- `60` = 1 minute
- `60` = 1 hour
- **Total:** Token expires 1 hour after creation

**After expiration:**
- Token becomes invalid
- User must login again to get a new token

---

### 5. Claims in JWT

**Claims** are statements about an entity (user) and additional data.

**Types of claims:**

| Type | Examples | Purpose |
|------|----------|---------|
| **Registered** | `sub`, `iat`, `exp`, `iss`, `aud` | Standard JWT claims |
| **Public** | `name`, `email`, `role` | Publicly defined claims |
| **Private** | `userId`, `department` | Custom claims for your app |

**In our code:**
```java
Map<String, Object> claims = new HashMap<>();
claims.put("role", "ADMIN");        // Custom claim (not implemented yet)
claims.put("department", "IT");     // Custom claim (not implemented yet)

return Jwts.builder()
        .claims()
        .add(claims)
        .subject(username)              // Registered claim (sub)
        .issuedAt(new Date(...))        // Registered claim (iat)
        .expiration(new Date(...))      // Registered claim (exp)
```

**Current payload:**
```json
{
  "sub": "user1",
  "iat": 1707316800,
  "exp": 1707320400
}
```

**With custom claims:**
```json
{
  "sub": "user1",
  "iat": 1707316800,
  "exp": 1707320400,
  "role": "ADMIN",
  "department": "IT"
}
```

---

### 6. Stateless Authentication

**Stateless** means the server doesn't store session data.

**Traditional Session-based (Stateful):**
```
Login → Server creates session → Stores in memory/database
         ↓
     SessionID: abc123
         ↓
Client gets cookie with SessionID
         ↓
Every request → Server looks up session in database
```

**JWT-based (Stateless):**
```
Login → Server generates JWT token → Returns to client
         ↓
Client stores token (localStorage/sessionStorage)
         ↓
Every request → Client sends token → Server validates signature
                                      ↓
                                  No database lookup!
```

**Benefits:**
- ✅ No database queries for every request
- ✅ Easy to scale (no shared session storage)
- ✅ Perfect for microservices

---

### 7. BCrypt Password Encoding

**BCrypt** is a one-way hashing algorithm for passwords.

**How it works:**
```
Password: u@123
         ↓
BCrypt Hash: $2a$10$xK7h8vN5qP2wR9sT1uV4yZ...
```

**Structure of BCrypt hash:**
```
$2a$10$xK7h8vN5qP2wR9sT1uV4yZ6aB3cD5eF7gH8jK9mL0nO1pQ
 │  │  │                                              │
 │  │  └─ Salt (random 22 chars)                     │
 │  └─ Cost factor (10 = 2^10 = 1024 rounds)         │
 └─ Algorithm version (2a)                            │
                                                      │
                                            Hash (31 chars)
```

**Why BCrypt?**
- ✅ One-way: Can't reverse-engineer the password
- ✅ Salt: Same password → Different hash (protects against rainbow tables)
- ✅ Adaptive: Can increase cost factor as computers get faster

**Verification:**
```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

// During registration
String hash = encoder.encode("u@123");
// Result: $2a$10$xK7h8vN5qP2wR9sT1uV4yZ...

// During login
boolean matches = encoder.matches("u@123", "$2a$10$xK7h8vN5qP2wR9sT1uV4yZ...");
// Result: true
```

---

## ⚠️ Current Limitations

### 1. Token Validation Not Implemented Yet

**Current State:**
- ✅ Token generation works
- ❌ Token validation not implemented
- ⚠️ Still using Basic Auth for protected endpoints

**What's Missing:**
- JWT Filter to intercept requests
- Extract token from `Authorization` header
- Validate token signature
- Extract username from token
- Set authentication in SecurityContext

---

### 2. No Refresh Token

**Current:**
- Token expires after 1 hour
- User must login again

**Better Approach:**
- **Access Token:** Short-lived (15 minutes)
- **Refresh Token:** Long-lived (7 days)
- Use refresh token to get new access token without login

---

### 3. Secret Key Regenerated on Every Restart

**Current:**
```java
public JWTService() throws NoSuchAlgorithmException {
    KeyGenerator keyGen = KeyGenerator.getInstance("HmacSha256");
    SecretKey sk = keyGen.generateKey();  // New key every time!
    // ...
}
```

**Problem:**
- Every time the application restarts, a new secret key is generated
- Old tokens become invalid

**Solution:**
- Store secret key in `application.properties`
- Use same key across restarts

**Example:**
```properties
jwt.secret=xK7h8vN5qP2wR9sT1uV4yZ6aB3cD5eF7gH8jK9mL0nO1pQ2sT3uV4wX5yZ6a
jwt.expiration=3600000
```

---

## 🎓 What's Next?

### Phase 4: JWT Token Validation (Coming Soon)

**What we'll implement:**

1. **JwtFilter (Custom Filter)**
   - Intercepts every request
   - Extracts JWT token from `Authorization: Bearer <token>` header
   - Validates token signature
   - Extracts username from token
   - Sets authentication in SecurityContext

2. **Update JWTService**
   - Add `extractUsername(String token)` method
   - Add `validateToken(String token, UserDetails userDetails)` method
   - Add `extractClaim(String token, Function<Claims, T> claimsResolver)` method
   - Add `extractAllClaims(String token)` method
   - Add `isTokenExpired(String token)` method

3. **Update SecurityConfig**
   - Add JwtFilter before UsernamePasswordAuthenticationFilter
   - Configure filter chain

4. **Testing**
   - Login → Get token
   - Use token in `Authorization: Bearer <token>` header
   - Access protected endpoints without Basic Auth

---

### Future Enhancements:

5. **Refresh Token Implementation**
   - Add refresh token endpoint
   - Implement refresh token rotation
   - Store refresh tokens in database

6. **Role-based Authorization**
   - Add roles to JWT claims
   - Implement `@PreAuthorize` annotations
   - Restrict endpoints based on roles

7. **Token Blacklisting**
   - Implement logout functionality
   - Store revoked tokens in Redis
   - Check blacklist during validation

---

## 📚 Summary

### What We Implemented:

| Component | Purpose | Status |
|-----------|---------|--------|
| **JWTService** | Generate JWT tokens | ✅ Complete |
| **Secret Key Generation** | Create cryptographic key | ✅ Complete |
| **Token Structure** | Header + Payload + Signature | ✅ Complete |
| **BCrypt Encoding** | Hash passwords securely | ✅ Complete |
| **Registration Endpoint** | Create new users | ✅ Complete |
| **Login Endpoint** | Authenticate and get token | ✅ Complete |
| **SecurityConfig** | Allow /login and /register | ✅ Complete |

### What We'll Implement Next:

| Component | Purpose | Status |
|-----------|---------|--------|
| **JwtFilter** | Validate tokens on requests | ⏳ Pending |
| **Token Validation** | Verify signature and expiry | ⏳ Pending |
| **Extract Claims** | Get username from token | ⏳ Pending |
| **Bearer Token Auth** | Replace Basic Auth | ⏳ Pending |

---

## 🔗 Learning Path

1. ✅ **Section 33:** Database Authentication with Basic Auth
2. ✅ **Section 34:** BCrypt Password Encoding
3. ✅ **Section 35:** JWT Token Generation (Current)
4. ➡️ **Section 36:** JWT Token Validation (Next)
5. ➡️ **Section 37:** Refresh Tokens
6. ➡️ **Section 38:** Role-based Authorization
7. ➡️ **Section 39:** OAuth2 / Social Login

---

## 🎯 Key Takeaways

1. **JWT is stateless** - Server doesn't store session data
2. **JWT has 3 parts** - Header, Payload, Signature
3. **Signature prevents tampering** - Only server with secret key can create valid tokens
4. **Tokens expire** - Built-in security feature
5. **Base64 is not encryption** - Anyone can decode, but can't forge signature
6. **BCrypt is one-way** - Can't reverse-engineer passwords
7. **Secret key is critical** - Keep it secret, never expose it

---

**Happy Learning! 🚀**

*Created for Spring Boot Security Learning - JWT Token Generation*

---

## 📌 Quick Reference

### Generate Token Flow:
```
Login → Verify Password → Generate Token → Return to Client
```

### Token Structure:
```
eyJhbGciOiJIUzI1NiJ9    ← HEADER (Base64)
.
eyJzdWIiOiJ1c2VyMSI... ← PAYLOAD (Base64)
.
8kZq3Y-vN5xH9K5rR8f... ← SIGNATURE (HMAC-SHA256)
```

### Key Classes:
- **JWTService:** Token generation
- **UserService:** Authentication and user management
- **UserController:** REST endpoints
- **SecurityConfig:** Security configuration

### Dependencies:
- `jjwt-api` - JWT API
- `jjwt-impl` - JWT Implementation
- `jjwt-jackson` - JSON Processing

---

**End of JWT Token Generation Guide**
