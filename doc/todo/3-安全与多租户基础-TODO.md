# 第 3 章 安全与多租户基础 TODO 清单（AI Agent 实施指引）

> 本章聚焦：**认证、授权、多租户隔离、基础用户体系与安全网关**。
> 该章节的实现，将跨越 `backend-system`（用户/角色/权限/租户基础领域）、`backend-boot`（安全配置）、以及部分 `backend-common` 能力的具体落地。
> 要求：
>
> - 细粒度到类/方法级，适合 AI Agent 逐条实现；
> - 全程 TDD：先写测试（接口 / 过滤器 / 领域规则），再写实现；
> - 所有安全相关逻辑必须有清晰注释，说明安全假设与边界；
> - 严格对齐 `doc/design/2.安全设计详细说明.md` 与 `README.md` 中对安全与多租户的描述。

---

## 3.1 模块与包结构范围

**涉及主要模块与包：**

- `backend-system`：`io.github.faustofanb.admin.system`
  - `domain`：User / Tenant / Role / Permission / Menu 等领域模型与规则；
  - `infra`：用户与权限相关的 Repository 接口（Jimmer）；
  - `app`：与认证/授权相关的应用服务（登录、加载用户权限等）；
  - `adapter.rest`：认证接口（登录/刷新/登出）。
- `backend-boot`：
  - `io.github.faustofanb.admin.boot.security`：Spring Security 配置、过滤器链、PasswordEncoder；
- `backend-common`（已有）：
  - `common.context`：`AppContext` / `AppContextHolder`；
  - `common.error` / `common.exception`：错误码与异常；
  - `common.api`：`ApiResponse`。

AI Agent 在实现本章内容时，应优先按以下顺序：

1. 领域与数据模型（User/Tenant/Role/Permission 基础字段与关系）。
2. 安全配置（Spring Security FilterChain、PasswordEncoder、JWT 工具）。
3. 登录/刷新 Token 接口与 TDD。
4. AppContext Filter 与多租户强制过滤。
5. RBAC 权限检查与 `@PreAuthorize` 集成。

---

## 3.2 领域与数据模型（`backend-system.domain`）

### 3.2.1 租户实体 `Tenant`

**目标**：表达一个租户的最小必要信息，用于账号归属与租户状态控制。

**类：** `io.github.faustofanb.admin.system.domain.model.Tenant`

**TODO（字段与方法）**：

1. 使用 Jimmer 实体或聚合建模（此处先从领域模型角度列出）：

   - [ ] 字段：
     - [ ] `Long id` —— 主键；
     - [ ] `String code` —— 租户编码，唯一；
     - [ ] `String name` —— 租户名称；
     - [ ] `TenantStatus status` —— 状态（`ACTIVE`, `DISABLED`, `PENDING` 等枚举）；
     - [ ] `Instant createdAt` / `Instant updatedAt`；
   - [ ] 领域方法示例：
     - [ ] `public void disable()` —— 禁用租户；
     - [ ] `public void enable()` —— 启用租户；
   - **TDD**：
     - [ ] `TenantTest.shouldSwitchStatusBetweenActiveAndDisabled()`：
       - 创建 Tenant，调用 `disable()`/`enable()` 并断言状态变化。

2. 枚举 `TenantStatus`：
   - [ ] 枚举值：`ACTIVE`, `DISABLED`, `PENDING`;
   - [ ] 方法：`public boolean isActive()`；
   - **TDD**：
     - [ ] `TenantStatusTest.shouldIdentifyActiveStatus()`。

> 与数据库/Jimmer 映射细节可在后续数据访问章节细化，此处关注领域行为与安全相关字段。

---

### 3.2.2 用户实体 `User`

**目标**：表达系统用户的基础信息与安全相关字段（密码散列、状态等），用于认证与授权。

**类：** `io.github.faustofanb.admin.system.domain.model.User`

**TODO**：

1. 字段：
   - [ ] `Long id`；
   - [ ] `Long tenantId`；
   - [ ] `String username`；
   - [ ] `String passwordHash`；
   - [ ] `String email`（可选）；
   - [ ] `String phone`（可选）；
   - [ ] `UserStatus status`（`ACTIVE`, `LOCKED`, `DISABLED` 等）；
   - [ ] `int failedLoginAttempts`；
   - [ ] `Instant lastLoginAt`（可选）；
2. 行为方法：

   - [ ] `public void onLoginSuccess()`：
     - 将 `failedLoginAttempts` 置零；
     - 更新 `lastLoginAt`；
   - [ ] `public void onLoginFailed()`：
     - `failedLoginAttempts++`；
     - 当失败次数超过配置阈值时，将 `status` 标记为 `LOCKED`（具体逻辑可依赖配置值，暂可作为参数传入）；
   - [ ] `public boolean isActive()`：
     - 同时检查租户是否有效（此处先占位，实际应查询 Tenant 状态或者通过服务协作）。
   - **TDD**：
     - [ ] `UserTest.shouldResetFailedAttemptsOnLoginSuccess()`；
     - [ ] `UserTest.shouldIncreaseFailedAttemptsOnLoginFailed()`；
     - [ ] `UserTest.shouldLockUserAfterTooManyFailures()`。

3. 枚举 `UserStatus`：
   - [ ] 值：`ACTIVE`, `LOCKED`, `DISABLED`, `PENDING`；
   - [ ] 方法：`public boolean canLogin()`；
   - **TDD**：
     - [ ] `UserStatusTest.shouldDetermineLoginEligibility()`。

---

### 3.2.3 角色与权限实体（简化版）

**类：**

- `Role`：`io.github.faustofanb.admin.system.domain.model.Role`
- `Permission`：`io.github.faustofanb.admin.system.domain.model.Permission`

**TODO（只列安全基础所需字段）**：

1. `Role`：
   - [ ] 字段：`Long id`, `Long tenantId`, `String code`, `String name`, `RoleType type`；
   - [ ] 角色类型枚举 `RoleType`：`SYSTEM_ADMIN`, `TENANT_ADMIN`, `NORMAL` 等；
2. `Permission`：

   - [ ] 字段：`Long id`, `Long tenantId`, `String code`, `String expression`；
   - [ ] `expression` 如 `perm:sys:user:list` 或 `api:/api/v1/users:GET`；

3. 关系：

   - [ ] 用户-角色：多对多（在 Jimmer 或 DB 层实现，此处只备注 TODO）；
   - [ ] 角色-权限：多对多。

4. TDD：
   - 领域层此处逻辑较薄，可用 `RolePermissionModelTest` 之类测试最基础的 getter/setter 或构造逻辑即可，重点放在授权服务上。

---

## 3.3 用户与权限查询服务（`backend-system.app`）

### 3.3.1 用户查询服务 `UserQueryService`

**类：** `io.github.faustofanb.admin.system.app.query.UserQueryService`

**TODO**：

1. 方法：

   - [ ] `public Optional<User> findByTenantAndUsername(String tenantCodeOrId, String username)`；
     - 根据租户标识 + 用户名查询用户；
     - 内部需调用对应 Repository；
     - 注意多租户隔离：只查当前租户；
   - [ ] `public Optional<User> findById(Long tenantId, Long userId)`；

2. TDD：
   - [ ] `UserQueryServiceTest.shouldFindUserByTenantAndUsername()`（可用内存替身 Repository 或 Mock）。

---

### 3.3.2 权限加载服务 `UserPermissionService`

**类：** `io.github.faustofanb.admin.system.app.query.UserPermissionService`

**TODO**：

1. 方法：

   - [ ] `public Set<String> loadPermissions(Long tenantId, Long userId)`；
     - 返回当前用户拥有的权限编码集合（如 `perm:sys:user:list`）；
   - [ ] `public Set<String> loadRoleCodes(Long tenantId, Long userId)`；

2. TDD：
   - [ ] `UserPermissionServiceTest.shouldLoadPermissionsForUser()`；
   - [ ] `UserPermissionServiceTest.shouldLoadRoleCodesForUser()`。

> 这些服务会在 Spring Security 的 UserDetailsService 或自定义认证逻辑中被调用。

---

## 3.4 Spring Security 基础配置（`backend-boot.security`）

### 3.4.1 密码编码器 `PasswordEncoderConfig`

**类：** `io.github.faustofanb.admin.boot.security.PasswordEncoderConfig`

**TODO**：

1. 方法：
   - [ ] `@Bean public PasswordEncoder passwordEncoder()`：
     - 推荐使用 `BCryptPasswordEncoder`；
   - **TDD**：
     - [ ] `PasswordEncoderConfigTest.shouldEncodeAndMatchPassword()`：
       - 注入 `PasswordEncoder`，对同一明文多次编码，断言结果不同但都能匹配原文。

---

### 3.4.2 SecurityFilterChain 配置 `SecurityConfig`

**类：** `io.github.faustofanb.admin.boot.security.SecurityConfig`

**TODO**：

1. 定义 `@Bean SecurityFilterChain securityFilterChain(HttpSecurity http)`：

   - [ ] 禁用 CSRF（如采用前后端分离 + JWT）；
   - [ ] 配置基于 JWT 的认证过滤器链；
   - [ ] 对 `/api/v1/auth/login`, `/api/v1/auth/refresh` 等端点允许匿名访问；
   - [ ] 其他业务接口要求认证。
   - **TDD（集成测试）**：
     - [ ] `SecurityConfigTest.shouldAllowAnonymousAccessToLogin()`：
       - 使用 MockMvc/WebTestClient 验证登录接口匿名可访问（无需实现完整逻辑，可返回 404/占位）；
     - [ ] `SecurityConfigTest.shouldRequireAuthForProtectedEndpoints()`。

2. 集成方法级授权：
   - [ ] 启用 `@EnableMethodSecurity` 或等价配置；
   - [ ] 确保 `@PreAuthorize` 能正常生效。

---

## 3.5 JWT 工具与令牌服务

### 3.5.1 JWT 工具类 `JwtTokenService`

**类：** `io.github.faustofanb.admin.boot.security.JwtTokenService`

**TODO（方法级）**：

1. 配置：从配置文件读取密钥、过期时间等（此处只写 TODO 占位）。

2. 方法：

   - [ ] `public String generateAccessToken(Long userId, Long tenantId, Set<String> roles, Set<String> scopes)`：
     - 生成 AccessToken，claims 至少包含：`sub`（userId）、`tid`（tenantId）、`roles`、`scopes`、`iat`、`exp`、`jti`；
   - [ ] `public String generateRefreshToken(Long userId, Long tenantId)`；
   - [ ] `public JwtClaims parseAndValidate(String token)`：
     - 解析 JWT 并校验签名与过期时间，返回一个内部 `JwtClaims` 结构；
   - [ ] `public boolean isExpired(String token)`；

3. 定义 `record JwtClaims(Long userId, Long tenantId, Set<String> roles, Set<String> scopes, Instant issuedAt, Instant expiresAt, String jti)`。

4. **TDD**：
   - [ ] `JwtTokenServiceTest.shouldGenerateAndParseAccessToken()`；
   - [ ] `JwtTokenServiceTest.shouldFailOnInvalidSignature()`；
   - [ ] `JwtTokenServiceTest.shouldDetectExpiration()`。

> 具体使用哪种 JWT 库（jjwt / nimbus-jose-jwt / spring-security-oauth2-jose）可在实现时再选，这里只列接口与行为。

---

## 3.6 认证 Filter 与 AppContext 落地

### 3.6.1 JWT 认证过滤器 `JwtAuthenticationFilter`

**类：** `io.github.faustofanb.admin.boot.security.JwtAuthenticationFilter`

**TODO**：

1. 继承或组合 `OncePerRequestFilter`。
2. 核心方法：
   - [ ] `protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)`：
     - 从 Header 中提取 Bearer Token；
     - 调用 `JwtTokenService.parseAndValidate()` 获取 `JwtClaims`；
     - 基于 `JwtClaims` 构造 Spring Security `Authentication` 对象放入 `SecurityContextHolder`；
     - 同时构造 `AppContext` 并放入 `AppContextHolder`；
     - 调用下游 Filter；
     - 最后在 `finally` 中清理 `AppContextHolder`。
   - **TDD（集成/组件测试）**：
     - [ ] `JwtAuthenticationFilterTest.shouldSetSecurityContextAndAppContextOnValidToken()`；
     - [ ] `JwtAuthenticationFilterTest.shouldSkipWhenNoTokenPresent()`；
     - [ ] `JwtAuthenticationFilterTest.shouldRejectInvalidToken()`（可通过设置响应状态或抛异常）。

---

### 3.6.2 多租户强制过滤（Jimmer TenantFilter 或 BaseRepository）

**TODO（设计级别，具体实现可放在 infra 层）**：

1. 为数据库访问强制追加 `tenant_id = currentTenantId` 条件：
   - [ ] 选型 1：Jimmer 全局 TenantFilter；
   - [ ] 选型 2：统一 BaseRepository 封装在查询处追加条件。
2. 在本 TODO 文档中记下约束：
   - [ ] 所有 Repository 在实现时不得绕过多租户条件；
   - [ ] 低级 API（原生 SQL）必须显式传入 tenantId 并在代码审查时重点关注。
3. **TDD 思路**（待在数据访问章节实现）：
   - [ ] `TenantIsolationTest.shouldOnlyReadDataFromCurrentTenant()`：
     - 准备多租户数据；
     - 设置不同 `AppContext` 后查询，验证只返回对应租户记录。

---

## 3.7 登录与刷新 Token 接口（`backend-system.adapter.rest`）

### 3.7.1 DTO 定义

**类：**

- `LoginRequest`：`io.github.faustofanb.admin.system.adapter.rest.auth.LoginRequest`
- `LoginResponse`：`io.github.faustofanb.admin.system.adapter.rest.auth.LoginResponse`
- `RefreshTokenRequest`：`...RefreshTokenRequest`

**TODO**：

1. `LoginRequest` 字段：
   - [ ] `String tenantCode`；
   - [ ] `String username`；
   - [ ] `String password`；
2. `LoginResponse` 字段：
   - [ ] `String accessToken`；
   - [ ] `String refreshToken`；
   - [ ] 可选：`long expiresIn`；
3. `RefreshTokenRequest` 字段：
   - [ ] `String refreshToken`；
4. 所有 DTO 使用 `record` + 清晰的 Javadoc 注释说明含义。

---

### 3.7.2 认证应用服务 `AuthApplicationService`

**类：** `io.github.faustofanb.admin.system.app.command.AuthApplicationService`

**TODO（方法级）**：

1. `public LoginResponse login(LoginRequest request)`：

   - 步骤：
     - [ ] 根据租户编码加载租户并校验租户状态；
     - [ ] 根据租户与用户名查询用户；
     - [ ] 使用 `PasswordEncoder` 比对密码；
     - [ ] 校验用户状态（锁定/禁用等）；
     - [ ] 调用 `UserPermissionService` 加载角色与权限；
     - [ ] 调用 `JwtTokenService` 生成 AccessToken 与 RefreshToken；
     - [ ] 更新用户登录信息（`onLoginSuccess`）；
     - [ ] 记录登录审计（可在后续审计模块细化，此处记 TODO）。
   - 异常：
     - [ ] 用户不存在或密码错误 → 抛出带 `AUTH_BAD_CREDENTIALS` 的 `BusinessException`；
     - [ ] 租户或用户被禁用 → 抛出对应错误码。
   - **TDD**：
     - [ ] `AuthApplicationServiceTest.shouldLoginSuccessfully()`；
     - [ ] `AuthApplicationServiceTest.shouldFailOnBadCredentials()`；
     - [ ] `AuthApplicationServiceTest.shouldFailWhenTenantDisabled()`。

2. `public LoginResponse refreshToken(RefreshTokenRequest request)`：
   - 步骤：
     - [ ] 验证 RefreshToken；
     - [ ] 加载用户与租户，校验状态；
     - [ ] 重新签发 AccessToken（以及新的 RefreshToken，视策略而定）；
   - **TDD**：
     - [ ] `AuthApplicationServiceTest.shouldRefreshTokenSuccessfully()`；
     - [ ] `AuthApplicationServiceTest.shouldRejectExpiredOrInvalidRefreshToken()`。

---

### 3.7.3 认证控制器 `AuthController`

**类：** `io.github.faustofanb.admin.system.adapter.rest.auth.AuthController`

**TODO**：

1. 使用 `@RestController` + `@RequestMapping("/api/v1/auth")`；
2. 方法：
   - [ ] `@PostMapping("/login") public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request)`；
   - [ ] `@PostMapping("/refresh") public ApiResponse<LoginResponse> refresh(@RequestBody @Valid RefreshTokenRequest request)`；
3. **TDD（集成测试）**：
   - [ ] `AuthControllerTest.shouldReturnTokensOnValidLogin()`；
   - [ ] `AuthControllerTest.shouldReturnErrorOnInvalidLogin()`；
   - [ ] `AuthControllerTest.shouldRefreshToken()`。

---

## 3.8 RBAC 方法级授权集成

### 3.8.1 权限表达式与 `@PreAuthorize`

**TODO**：

1. 在 `backend-system` 中定义：
   - [ ] 简单的权限常量类 `SystemPermissions`（如 `perm:sys:user:list` 等），以减少魔法字符串；
2. 定义自定义 `PermissionEvaluator`：

   - [ ] 类：`io.github.faustofanb.admin.boot.security.RbacPermissionEvaluator`；
   - [ ] 方法：
     - [ ] `boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission)`；
     - [ ] `boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission)`；
   - [ ] 实现逻辑：从 Authentication 中读取权限集合，判断是否包含给定权限码；
   - **TDD**：
     - [ ] `RbacPermissionEvaluatorTest.shouldGrantAccessWhenPermissionPresent()`；
     - [ ] `RbacPermissionEvaluatorTest.shouldDenyAccessWhenPermissionMissing()`。

3. 在一个示例业务接口上使用 `@PreAuthorize("hasAuthority('perm:sys:user:list')")`，并编写集成测试验证授权行为（可在后续 system 模块章节展开，这里只记 TODO）。

---

## 3.9 TDD 与安全性专项要求

**总体要求**：

1. 所有认证/授权/多租户相关方法必须有测试覆盖：

   - 登录成功/失败、密码错误、用户锁定、租户禁用等分支；
   - JWT 解析失败/过期等异常路径；
   - AppContext 是否在请求结束后被正确清理；
   - 不同租户间的数据不可被交叉访问（在数据访问章节实现）。

2. 安全注释：

   - 在关键类（JwtAuthenticationFilter、AuthApplicationService、RbacPermissionEvaluator 等）顶部编写 Javadoc，说明：
     - 该组件承担的安全责任；
     - 依赖的上游/下游组件；
     - 可能的误用风险与防御措施。

3. 覆盖率建议：

   - 认证与权限相关类的覆盖率建议 ≥ 85%；
   - 对异常路径、边界条件（空 token、篡改 token、跨租户尝试）必须有单测。

4. 与设计文档保持一致：
   - 若在实现中对登录流程、JWT claims、权限表达式等有任何调整，必须同步更新 `doc/design/2.安全设计详细说明.md` 与对应 TODO 文档。

---

> 本章完成后，系统将具备：
>
> - 统一的用户/租户/角色/权限基础模型；
> - 可用的登录/刷新 Token 接口；
> - 基于 JWT + Spring Security 的认证链路；
> - `AppContext` 在 HTTP 请求中的完整落地；
> - 基础 RBAC 授权能力。
>
> 随后即可在系统模块与其他业务模块上，基于这些安全与多租户能力继续迭代。
