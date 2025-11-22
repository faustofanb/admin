# 第三阶段：安全与认证授权 (Phase 3: Security & Auth)

本阶段目标是构建基于 Spring Security 和 JWT 的无状态认证体系。由于引入了 **Spring Cloud Gateway**，安全架构分为两层：
1. **网关层 (Gateway)**：负责统一鉴权、Token 解析与转发、全局跨域处理 (基于 WebFlux)。
2. **应用层 (Backend)**：负责细粒度权限控制 (RBAC)、方法级安全 (基于 Servlet)。

## 3.1 JWT 基础设施 (Common)

- [ ] **3.1.1 引入依赖**
  - 在 `common` 模块引入 `jjwt` 或 `nimbus-jose-jwt`。

- [ ] **3.1.2 定义 JwtProperties**
  - 配置类绑定 `app.security.jwt`:
    - `secret-key` (HMAC) 或 `public-key/private-key` (RSA)。
    - `access-token-ttl` (如 1小时)。
    - `refresh-token-ttl` (如 7天)。
    - `issuer`。

- [ ] **3.1.3 编写 JwtTokenProvider**
  - 方法 `createAccessToken(userId, tenantId, roles, scopes)`。
  - 方法 `createRefreshToken(userId)`。
  - 方法 `validateToken(token)`。
  - 方法 `getClaims(token)`。

## 3.2 网关层安全配置 (Gateway - WebFlux)

> 网关作为统一入口，负责第一道防线：验证 Token 有效性，并将用户信息透传给后端。

- [ ] **3.2.1 引入依赖**
  - 在 `gateway` 模块引入 `spring-boot-starter-security`。

- [ ] **3.2.2 实现 GlobalAuthFilter (或 SecurityWebFilterChain)**
  - **方案 A (推荐 - 轻量级)**: 实现 `GlobalFilter`。
    1. 拦截所有请求 (排除 `/auth/login`, `/public/**`)。
    2. 从 Header 提取 `Authorization: Bearer ...`。
    3. 调用 `JwtTokenProvider.validateToken()`。
    4. 如果无效，直接返回 401 (WebFlux 方式写入 Response)。
    5. 如果有效，解析 Claims，将 `X-Tenant-Id`, `X-User-Id`, `X-Roles` 写入 Request Mutation Header，传递给后端。
  
  - **方案 B (标准 Spring Security)**: 配置 `SecurityWebFilterChain`。
    1. 配置 `ServerHttpSecurity`。
    2. 实现 `ServerAuthenticationConverter` 提取 Token。
    3. 实现 `ReactiveAuthenticationManager` 验证 Token。
    4. 配置 `pathMatchers("/auth/**").permitAll()`。

- [ ] **3.2.3 配置全局 CORS (网关层)**
  - 在 Gateway 统一处理跨域，后端服务可以关闭 CORS 或只允许来自 Gateway 的请求。

## 3.3 应用层安全配置 (Backend - Servlet)

> 后端服务接收网关转发的请求，主要负责基于角色的访问控制 (RBAC)。

- [ ] **3.3.1 实现 AuthenticationFilter (信任模式)**
  - 继承 `OncePerRequestFilter`。
  - 逻辑变化:
    - **不再** 解析 JWT (网关已解析)。
    - **改为** 读取网关透传的 Header (`X-User-Id`, `X-Roles` 等)。
    - 构建 `PreAuthenticatedAuthenticationToken`。
    - 设置 `SecurityContextHolder`。
    - (可选) 如果为了双重安全，也可以再次校验 JWT，但会有性能损耗。

- [ ] **3.3.2 配置 SecurityFilterChain**
  - 禁用 CSRF, Session。
  - 所有请求 `authenticated()` (因为网关已经放行了白名单，到达后端的请求理论上都应是经过认证的，或者后端也配置同样的白名单)。
  - 配合 `AuthenticationFilter`。

- [ ] **3.3.3 自定义异常处理**
  - `AuthenticationEntryPoint` (401)。
  - `AccessDeniedHandler` (403)。

## 3.4 认证接口实现 (Backend)

- [ ] **3.4.1 创建 AuthController**
  - 位置: `module/user-tenant-permission`。
  - 接口:
    - `POST /auth/login`: 登录逻辑。
    - `POST /auth/refresh`: 刷新 Token。
  - **注意**: 这些接口在网关层需要配置为 `permitAll`。

- [ ] **3.4.2 实现 AuthService**
  - 校验用户、密码。
  - 生成 Token。

## 3.5 细粒度权限控制 (Backend)

- [ ] **3.5.1 启用方法级安全**
  - `@EnableMethodSecurity`。

- [ ] **3.5.2 实现 PermissionEvaluator**
  - 从 `SecurityContext` (由 Header 构建) 获取用户角色/权限。
  - 判断 `@PreAuthorize("hasPermission('sys:user:list')")`。
