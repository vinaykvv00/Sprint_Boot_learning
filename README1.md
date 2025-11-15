# Sprint_Boot_learning – Consolidated Learning README (README1)

This repository contains 5 incremental Spring / Spring Boot sample projects. They progress from a minimal Boot app through classic (XML) Spring, then annotation‑based Boot with DI, and finally layered REST + JPA CRUD. This README explains: what each folder demonstrates, core concepts, architectural evolution, what to commit vs ignore, and suggested next steps.

---
## Index of Projects
| Folder | Name | Focus |
|--------|------|-------|
| 1_DemoApp | Basic Spring Boot App | Auto configuration + simple REST controller |
| 2_DemoSpring | Classic Spring (XML) | Manual ApplicationContext, XML bean config, DI basics |
| 3_myApp | Boot + Annotation DI | Component scanning, @Autowired, @Qualifier, @Primary |
| 4_SimpleWebApp | Multi Controller | Basic REST endpoints, intro layering idea |
| 5_SimpleWebApp2 | Layered CRUD App | Controller, Service, Repository, Entity (JPA), Lombok |

---
## 1. 1_DemoApp – Minimal Spring Boot
**Files:** `DemoAppApplication.java`, `Hello.java`

Concepts:
- `@SpringBootApplication` = `@Configuration + @EnableAutoConfiguration + @ComponentScan`.
- Embedded server (Tomcat by default) starts with `SpringApplication.run(...)`.
- `@RestController` + `@RequestMapping("/")` returns a simple string (implicitly JSON if an object; plain text here).

Why it matters: Shows zero XML, zero manual context, Boot’s opinionated defaults.

Try it (Windows CMD):
```
cd 1_DemoApp
mvnw spring-boot:run
curl http://localhost:8080/
```

---
## 2. 2_DemoSpring – Classic Spring (Non‑Boot)
**Files:** `spring.xml`, `App.java`, `Dev`, `Laptop`, `Desktop`, `Computer`.

Concepts:
- Manual context creation: `new ClassPathXmlApplicationContext("spring.xml")`.
- XML bean definitions: `<bean id="dev" class="org.example.Dev" autowire="byName" />`.
- Interface (`Computer`) + polymorphic implementations (`Laptop`, `Desktop`).
- Dependency Injection (DI) styles hinted: constructor, setter, field (some commented examples).
- Primary bean selection done via `<bean id="com" ... primary="true">` (Spring recognizes `primary="true"`).

Learning Point: Understand IoC vs manually `new` objects. You ask the container for beans; container resolves dependencies.

Good to notice: No annotations; wiring by XML. Helps appreciate Boot’s auto configuration and component scanning later.

---
## 3. 3_myApp – Annotation Based DI in Boot
**Files:** `MyAppApplication.java`, `Dev`, `Laptop`, `Desktop`, `Computer`

Concepts:
- `@Component` registers beans automatically during component scanning (driven by `@SpringBootApplication` root package).
- Field Injection with `@Autowired` (works but generally less testable than constructor injection). Shown alternatives commented.
- `@Primary` on `Laptop` to break ambiguity (both `Laptop` and `Desktop` implement `Computer`).
- `@Qualifier("laptop")` on the autowired field forces a specific bean, overriding primary if necessary.

Recommended improvement: Prefer constructor injection for immutability & easier testing:
```java
@Component
public class Dev {
    private final Computer comp;
    public Dev(@Qualifier("laptop") Computer comp) { this.comp = comp; }
    public void build() { comp.compile(); }
}
```

---
## 4. 4_SimpleWebApp – Basic REST Endpoints
**Files:** `HomeController`, `LoginController`, `SimpleWebAppApplication`

Concepts:
- Multiple controllers under `controller/` package.
- Simple string responses (could evolve to JSON DTOs or return `ResponseEntity`).
- Introduces multiple routes (`/`, `/about`, `/login`).

Potential next steps here:
- Add a `service` layer for business logic.
- Add model/DTO classes.
- Centralized exception handling via `@ControllerAdvice`.
- Validation with `jakarta.validation` annotations.

---
## 5. 5_SimpleWebApp2 – Layered CRUD + JPA
**Files:** `ProductController`, `ProductService`, `ProductRepo`, `Product`, `SimpleWebAppApplication`

Concepts:
- Layered architecture: Controller → Service → Repository → Database.
- `@Entity` JPA mapping using `jakarta.persistence`.
- Spring Data JPA: `ProductRepo extends JpaRepository<Product, Integer>` gives CRUD without boilerplate.
- Lombok: `@Data`, `@AllArgsConstructor` reduce boilerplate (remember to have Lombok dependency & IDE plugin).
- RESTful endpoints using HTTP verbs: GET, POST, PUT, DELETE, path variables, request bodies.

Endpoints (assuming default port 8080):
```
GET    /products          # list all products
POST   /products          # create product (JSON body)
PUT    /products/{id}     # update product
DELETE /products/{id}     # delete product
```
Sample JSON:
```json
{ "prodId": 101, "prodName": "iPhone", "price": 50000 }
```
Sample curl:
```
curl -X POST http://localhost:8080/products -H "Content-Type: application/json" -d "{\"prodId\":101,\"prodName\":\"iPhone\",\"price\":50000}" 
curl http://localhost:8080/products
```

Suggested improvements:
- Use DTOs to separate API layer from entity (`ProductRequest`, `ProductResponse`).
- Add validation: `@NotBlank`, `@Min`, etc.
- Wrap responses with `ResponseEntity` and proper HTTP status codes.
- Handle not found: check existence before update/delete.
- Add transactions (usually service methods, Spring Data defaults single operations, but multi-step operations need `@Transactional`).
- Remove `@Component` from entity (not needed; `@Entity` is enough). Beans are usually services/repositories.
- Avoid using entity directly for update; merge manually or use partial update patch semantics.

---
## Core Spring / Spring Boot Concepts Across Projects
1. Inversion of Control (IoC) & Dependency Injection (DI): You delegate object creation & wiring to the container.
2. Bean Discovery: XML (`spring.xml`) vs Annotation (`@Component`, scanning from main class package downward).
3. Autowiring Resolution Order:
   - Type match
   - If multiple beans of same type: `@Primary` first, else need `@Qualifier`
4. Bean Scope (not shown yet): Defaults to singleton. Others: `prototype`, `request`, `session`.
5. REST Controllers: `@RestController` combines `@Controller + @ResponseBody`.
6. Spring Data JPA: Automatically implements repository interfaces. Reduces boilerplate and integrates with transaction management.
7. Lombok: Generates getters/setters/constructors at compile time – improves readability but requires awareness of hidden code.
8. Layered Architecture: Separation of concerns – each layer has a single responsibility.
9. Configuration vs Convention: Boot favors convention (auto configuration) over explicit XML / manual setup.
10. Qualifiers & Primary: Resolve ambiguity in DI when multiple candidates exist.

---
## Suggested Architecture Evolution (Text Diagram)
```
Client (Browser / Mobile / Curl)
        |
        v
[Controller Layer]  -> Request mapping, validation, DTO mapping
        |
        v
[Service Layer]     -> Business rules, transactions, orchestration
        |
        v
[Repository Layer]  -> Persistence abstraction (Spring Data JPA)
        |
        v
[Database]          -> Tables managed by JPA / Hibernate
```
Add supporting cross‑cutting layers later:
- Security: Spring Security filters before controller.
- Observability: Logging, metrics, tracing.
- Exception Handling: Global handler returns consistent error JSON.

---
## What to Commit vs Ignore (Git Hygiene)
Commit (keep versioned):
- `pom.xml` (and any parent or module poms)
- `src/` directories (Java + resources)
- `README*.md` files
- `.gitignore`
- Maven wrapper scripts: `mvnw`, `mvnw.cmd`, `.mvn/wrapper/*` (so others can build without local Maven)

Ignore (build / IDE artifacts):
- `target/` directories in each module (compiled classes, generated sources)
- IDE metadata: `.idea/`, `*.iml`, `.classpath`, `.project`, `.settings/`
- OS cruft: `.DS_Store`, `Thumbs.db`
- Logs: `*.log`
- Compiled bytecode: `*.class`
- Temporary files: `*.swp`, `*.tmp`
- Lombok delomboked output (if ever used)

Example root `.gitignore` (enhanced):
```
# Maven
**/target/
!*/src/main/**
!*/src/test/**

# IDEs
.idea/
*.iml
.classpath
.project
.settings/

# OS
.DS_Store
Thumbs.db

# Logs and compiled
*.log
*.class

# Temp
*.swp
*.tmp

# Copilot migration files
.idea/copilot.data.migration.*
```
(Already present: adjust as needed.)

After cleaning staged files (if you accidentally committed target):
```
git rm -r --cached **/target
```

---
## Quality & Best Practices Checklist Moving Forward
| Area | Current Status | Next Improvement |
|------|----------------|------------------|
| DI Style | Field injection shown | Switch to constructor injection |
| Entity Design | Basic Product entity | Add validation & remove @Component |
| Error Handling | None | Add global `@ControllerAdvice` |
| Responses | Raw objects / strings | Use DTO + `ResponseEntity` |
| Transactions | Implicit single ops | Annotate multi-step service methods with `@Transactional` |
| Tests | Minimal default | Add unit tests for service & controller slices |
| Validation | Not implemented | Add `jakarta.validation` annotations + `@Valid` |
| Security | Not present | Introduce Spring Security basics (authN, authZ) |

---
## Learning Path – Suggested Next README Topics (README2, README3, ...)
1. Spring Bean Scopes & Lifecycle (`InitializingBean`, `@PostConstruct`, `@PreDestroy`).
2. Configuration Properties (`@ConfigurationProperties` vs `@Value`).
3. Spring Profiles (dev/test/prod separation).
4. Database Migration Tools (Flyway or Liquibase).
5. Spring Security – stateless JWT vs session.
6. Caching (`@Cacheable`, Redis integration).
7. Testing Pyramid – JUnit, Mockito, Testcontainers for integration tests.
8. Observability – Actuator endpoints, Micrometer metrics.
9. Resilience – Retry (`spring-retry`), Circuit breaker (Resilience4j).
10. Performance – Pagination in repositories, N+1 query detection.

---
## Common Pitfalls to Watch Out For
- Field injection makes tests harder – prefer constructor injection.
- Exposing entities directly can couple API to DB schema; create DTOs.
- Forgetting to handle `Optional` results (e.g., findById) leads to `NoSuchElementException`.
- Silent failures when multiple beans of same interface exist without qualifier.
- Lombok hides code – be mindful of equals/hashCode when using entities.
- Lack of input validation allows invalid data persisted.
- Missing error responses produce generic 500 messages; add custom exception handling.

---
## How To Extend Product CRUD Properly
1. Add `ProductController` responses with HTTP status codes:
   - POST → 201 Created + Location header.
   - DELETE → 204 No Content.
2. Validate input:
```java
public record ProductRequest(@NotNull Integer prodId,
                             @NotBlank String prodName,
                             @Min(1) int price) {}
```
3. Map DTO → Entity inside Service (or use MapStruct).
4. Handle not found:
```java
Product p = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
```
5. Add `@ControllerAdvice` to translate exceptions to JSON errors.

---
## ASCII Overview of Dependency Flow (5_SimpleWebApp2)
```
ProductController
    | (calls)
    v
ProductService
    | (delegates)
    v
ProductRepo (Spring Data JPA)
    | (uses)
    v
Hibernate / JPA Provider
    | (persists)
    v
Database (tables representing Product)
```

---
## Quick Build & Run for Any Module
From repo root (choose a module):
```
cd 5_SimpleWebApp2
mvnw clean spring-boot:run
```
Stop: Ctrl + C.

Run tests:
```
cd 5_SimpleWebApp2
mvnw test
```

---
## Summary
You’ve progressed from:
- Manual bean wiring (XML) → Annotation component scanning.
- Simple controller → Layered REST + persistence.
- Basic DI → Qualifiers & Primary resolution.

Keep iterating README files as you learn new concepts. This `README1.md` is the foundation; future files can dive deeper into each advanced topic.

Happy coding & learning! Feel free to refine this README as the codebase evolves.

