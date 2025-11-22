# Project Implementation TODO List

This document outlines the step-by-step implementation plan for the Multi-Tenant Admin System.
**Note to AI Agents:** Follow these steps sequentially. Each step contains a specific prompt to be used for execution.

---

## Phase 1: Foundation (Common, Core, Infra)

### 1.1 Setup `backend-common`

- **Goal**: Establish common utilities, DTOs, and context holders.
- **Details**:
  - Create `io.github.faustofanb.admin.common.model.Result<T>` (Standard API response).
  - Create `io.github.faustofanb.admin.common.context.AppContext` (Holds `tenantId`, `userId`, `traceId` using `ThreadLocal`).
  - Create `io.github.faustofanb.admin.common.exception.BizException` and `ErrorCode` enum.
  - Create `io.github.faustofanb.admin.common.util.JsonUtils` (Jackson wrapper).
- **AI Prompt**:
  ```text
  Implement the basic infrastructure in `backend-common`.
  1. Create a generic `Result<T>` class for API responses with fields: code, message, data, traceId.
  2. Create an `AppContext` class using `TransmittableThreadLocal` to store current request context (tenantId, userId, username, roles). Add static methods to get/set/clear.
  3. Create a `BizException` runtime exception and a global `ErrorCode` interface/enum.
  4. Configure Jackson `ObjectMapper` in a utility class `JsonUtils` to handle Java 8 dates (JSR310) and ignore null values.
  ```

### 1.2 Setup `backend-core` (DDD Base)

- **Goal**: Define base classes for DDD and Jimmer.
- **Details**:
  - Define `BaseEntity` interface (Jimmer) with `createdTime`, `updatedTime`.
  - Define `AggregateRoot` marker interface.
  - Define `DomainEvent` abstract class (fields: `eventId`, `occurredOn`, `tenantId`).
- **AI Prompt**:
  ```text
  Set up the DDD base classes in `backend-core`.
  1. Create a `BaseEntity` interface compatible with Jimmer ORM, including standard audit fields (`created_time`, `updated_time`, `created_by`, `updated_by`).
  2. Create a `DomainEvent` base class for the Event Bus, containing `eventId` (UUID), `occurredOn` (LocalDateTime), and `tenantId`.
  3. Create a `Command` and `Query` marker interface for CQRS pattern.
  ```

### 1.3 Setup `backend-infra` (Middleware Integration)

- **Goal**: Configure Redis, Redisson, and basic Web MVC config.
- **Details**:
  - Configure `RedissonClient` bean.
  - Create `RedisUtils` helper.
  - Global Exception Handler (`@RestControllerAdvice`).
  - Jackson Configuration (Spring Boot config).
- **AI Prompt**:
  ```text
  Implement infrastructure configurations in `backend-infra`.
  1. Create a `RedissonConfig` class to initialize `RedissonClient` from `application.yml`.
  2. Create a `GlobalExceptionHandler` using `@RestControllerAdvice` to handle `BizException` and standard exceptions, returning `Result.fail()`.
  3. Ensure `backend-infra` depends on `backend-common`.
  ```

---

## Phase 2: system Module (Identity & Access Management)

### 2.1 system Domain Layer (Jimmer Entities)

- **Goal**: Define `User`, `Tenant`, `Role`, `Permission` entities.
- **Ref**: `doc/design/2.安全设计详细说明.md`
- **Details**:
  - `Tenant`: id, name, code, status, expire_time.
  - `User`: id, tenant_id, username, password_hash, email, phone, status.
  - `Role`: id, tenant_id, code, name.
  - `Permission`: id, code, name, type (MENU/BUTTON).
  - Associations: User-Role (Many-to-Many), Role-Permission (Many-to-Many).
- **AI Prompt**:
  ```text
  Implement the system domain layer in `module/backend-system` using Jimmer.
  1. Create `Tenant` entity.
  2. Create `User` entity with a unique constraint on `(tenant_id, username)`.
  3. Create `Role` and `Permission` entities.
  4. Define Many-to-Many relationships: `User` <-> `Role`, `Role` <-> `Permission`.
  5. Ensure all entities implement `BaseEntity` from `backend-core`.
  ```

### 2.2 system Infrastructure Layer (Repositories)

- **Goal**: Create Jimmer Repositories.
- **Details**:
  - `UserRepository`, `TenantRepository`, etc.
  - Implement `findByUsername(tenantId, username)`.
- **AI Prompt**:
  ```text
  Create Jimmer repositories in `module/backend-system`.
  1. Create `UserRepository` extending `JRepository<User, Long>`. Add method `findByTenantIdAndUsername`.
  2. Create `TenantRepository`, `RoleRepository`, `PermissionRepository`.
  ```

### 2.3 system Security Implementation (Spring Security + JWT)

- **Goal**: Secure the application.
- **Ref**: `doc/design/2.安全设计详细说明.md`
- **Details**:
  - `JwtTokenProvider`: Generate/Validate tokens (HS256).
  - `JwtAuthenticationFilter`: Parse token -> Populate `SecurityContext` & `AppContext`.
  - `UserDetailsServiceImpl`: Load user from DB.
  - `SecurityConfig`: Chain configuration (disable CSRF, stateless session).
- **AI Prompt**:
  ```text
  Implement Spring Security in `module/backend-system`.
  1. Create `JwtTokenProvider` to generate tokens with claims: `sub` (userId), `tid` (tenantId), `roles`.
  2. Implement `CustomUserDetailsService` to load user data from `UserRepository`.
  3. Create `JwtAuthenticationFilter` to intercept requests, validate JWT, and set `SecurityContextHolder` AND `AppContext`.
  4. Configure `SecurityFilterChain` to allow public access to `/auth/login` and require authentication for others.
  ```

### 2.4 system Application & Interface Layer

- **Goal**: Login and User Management APIs.
- **Details**:
  - `AuthService`: Handle login (password check), return Token.
  - `AuthController`: POST `/auth/login`.
  - `UserController`: CRUD for users.
- **AI Prompt**:
  ```text
  Implement system application logic and REST APIs.
  1. Create `AuthService` with a `login(LoginCommand cmd)` method. Verify password using BCrypt. Return JWT.
  2. Create `AuthController` exposing `/auth/login`.
  3. Create `UserController` with basic CRUD methods. Ensure all write operations use `@Transactional`.
  ```

---

## Phase 3: Gateway & RPC

### 3.1 RPC API Definition

- **Goal**: Define interfaces for cross-module communication.
- **Ref**: `doc/design/10.RPC与服务调用.md`
- **Details**:
  - `backend-rpc-api`: `UserRpcService`, `TenantRpcService`.
  - DTOs: `UserDTO`, `TenantDTO`.
- **AI Prompt**:
  ```text
  Define RPC interfaces in `backend-rpc-api`.
  1. Create `UserDTO` and `TenantDTO` (POJOs).
  2. Create `UserRpcService` interface with methods: `getUserById(Long tenantId, Long userId)`, `validateTenant(Long tenantId)`.
  ```

### 3.2 RPC Implementation

- **Goal**: Implement RPC interfaces in `backend-rpc-impl`.
- **Details**:
  - Implement `UserRpcService` using `UserRepository` (or `UserService`).
  - Use `@DubboService` (or Spring Bean if In-JVM).
- **AI Prompt**:
  ```text
  Implement RPC services in `backend-rpc-impl`.
  1. Create `UserRpcServiceImpl` implementing `UserRpcService`.
  2. Inject `UserRepository` from `backend-system` (ensure module dependency is set) to fetch data and map to DTOs.
  ```

### 3.3 Gateway Configuration

- **Goal**: Setup Spring Cloud Gateway.
- **Details**:
  - Route configuration (forward `/api/system/**` to `backend-boot`).
  - Global Filter: Extract JWT from header, validate (optional at gateway), pass to downstream.
- **AI Prompt**:
  ```text
  Configure `backend-gateway`.
  1. Add dependencies for Spring Cloud Gateway.
  2. Configure `application.yml` to route paths like `/api/**` to the backend service (lb://admin-backend).
  3. Implement a Global Filter to log request paths and potentially validate JWT structure (lightweight check).
  ```

---

## Phase 4: Resource Module (File Management)

### 4.1 Resource Domain

- **Goal**: Manage file metadata.
- **Ref**: `doc/design/6.资源与文件管理.md`
- **Details**:
  - Entity `ResourceMeta`: `tenantId`, `storeKey`, `sha256`, `status` (UPLOADING, ACTIVE).
- **AI Prompt**:
  ```text
  Implement Resource domain in `module/backend-file`.
  1. Create `ResourceMeta` entity with fields: `originalName`, `storeKey`, `mimeType`, `size`, `sha256`, `status` (Enum), `tenantId`.
  2. Create `ResourceRepository`.
  ```

### 4.2 Storage Abstraction

- **Goal**: Abstract file storage.
- **Details**:
  - Interface `BlobStorage`: `upload(key, stream)`, `getUrl(key)`.
  - Implementation `MinioBlobStorage` (or `LocalStorage` for dev).
- **AI Prompt**:
  ```text
  Implement storage abstraction in `module/backend-file`.
  1. Define `BlobStorage` interface with methods: `putObject`, `getObjectUrl`, `deleteObject`.
  2. Implement a `LocalStorage` version for development (saving files to a local directory).
  ```

### 4.3 Resource Application & API

- **Goal**: Upload/Download flow.
- **Details**:
  - `ResourceService`: `initUpload` (create meta), `completeUpload` (update status).
  - `ResourceController`: Upload endpoint.
- **AI Prompt**:
  ```text
  Implement Resource APIs in `module/backend-file`.
  1. Create `ResourceService` to handle file upload logic:
     - Check SHA256 for deduplication.
     - Save `ResourceMeta` with status UPLOADING.
     - Upload to `BlobStorage`.
     - Update status to ACTIVE.
  2. Create `ResourceController` with a POST `/resources/upload` endpoint.
  ```

---

## Phase 5: Advanced Features (Audit, Batch, Schedule)

### 5.1 Audit Module

- **Goal**: Record user actions.
- **Ref**: `doc/design/7.日志与可观测性详细设计.md`
- **Details**:
  - Entity `AuditLog`.
  - Aspect `@Audit` to intercept methods and save logs asynchronously.
- **AI Prompt**:
  ```text
  Implement Audit logging in `module/backend-audit`.
  1. Create `AuditLog` entity (userId, tenantId, action, resource, ip, timestamp).
  2. Create an `@Audit` annotation.
  3. Create an AOP Aspect to intercept methods annotated with `@Audit`, capture result/exception, and save `AuditLog` asynchronously.
  ```

### 5.2 Batch & Schedule

- **Goal**: Async tasks.
- **Ref**: `doc/design/8.任务调度详细设计.md`
- **Details**:
  - `backend-schedule`: PowerJob worker config.
  - `backend-batch`: EasyExcel listener for import.
- **AI Prompt**:
  ```text
  Setup Batch and Schedule modules.
  1. In `module/backend-schedule`, configure PowerJob Worker (or a simple Spring Scheduler for now).
  2. In `module/backend-batch`, create a generic `ExcelImportListener` using EasyExcel to handle batch data insertion.
  ```

---

## Phase 6: Boot & Integration

### 6.1 Application Entry

- **Goal**: Run the system.
- **Details**:
  - `backend-boot`: `AdminApplication.java`.
  - `application.yml`: Merge configs from all modules.
- **AI Prompt**:
  ```text
  Finalize the `backend-boot` module.
  1. Create the main class `AdminApplication` with `@SpringBootApplication`.
  2. Ensure it scans all packages `io.github.faustofanb.admin`.
  3. Consolidate `application.yml` with DB, Redis, and Server configurations.
  ```

### 6.2 Verification

- **Goal**: Ensure it works.
- **AI Prompt**:
  ```text
  Create an integration test in `backend-boot`.
  1. Test the full flow:
     - Register/Login to get Token.
     - Use Token to upload a file.
     - Verify Audit log is created.
  ```
