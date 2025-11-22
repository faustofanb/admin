# 阶段四：身份认证服务 (System Auth)

本阶段目标是基于 `backend-system` 的用户数据，实现安全的身份认证机制（Login, Token, Permission Check）。

## 4.1 模块配置 (backend-system)

### 4.1.1 依赖调整
- 在 `backend-system/pom.xml` 中引入 `spring-boot-starter-security`。
- 引入 Redis 相关依赖（如果尚未引入）。

---

## 4.2 认证核心

### 4.2.1 用户详情 `LoginUser`
- **路径**: `io.github.faustofanb.admin.module.system.model.LoginUser`
- **实现**: `UserDetails`
- **字段**:
  - `SysUser user`: 系统用户实体。
  - `Set<String> permissions`: 权限列表。
  - `Long tenantId`.

### 4.2.2 认证服务 `SysLoginService`
- **路径**: `io.github.faustofanb.admin.module.system.service.SysLoginService`
- **逻辑**:
  - `login(username, password)`:
    1. 校验验证码 (可选)。
    2. 调用 `AuthenticationManager.authenticate`。
    3. 认证成功生成 JWT。
    4. 记录登录日志 (`SysLoginLog` -> 异步保存)。

### 4.2.3 Token 服务 `TokenService`
- **功能**:
  - 创建 Token (JWT)。
  - 解析 Token。
  - 刷新 Token。
  - 存储在线用户信息到 Redis (`login_tokens:uuid`)。

---

## 4.3 Security 集成

### 4.3.1 过滤器 `JwtAuthenticationTokenFilter`
- **逻辑**:
  - 获取 Header 中的 Token。
  - 解析并校验。
  - 从 Redis 获取 `LoginUser`。
  - 构建 `UsernamePasswordAuthenticationToken` 放入 `SecurityContextHolder`。

### 4.3.2 权限处理器
- `AuthenticationEntryPoint`: 处理未登录访问。
- `AccessDeniedHandler`: 处理无权访问。

### 4.3.3 配置类 `SecurityConfig`
- **配置**:
  - 允许匿名访问 `/auth/login`, `/auth/register`, `/captchaImage`.
  - 其他请求需认证。
  - 注入 `BCryptPasswordEncoder`。

---

## 4.4 接口层

### 4.4.1 认证接口 `SysLoginController`
- **API**:
  - `POST /login`: 登录，返回 token。
  - `GET /getInfo`: 获取当前用户信息（包含角色、权限、头像等）。
  - `GET /getRouters`: 获取动态路由（菜单树）。
  - `POST /logout`: 登出。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第四阶段：身份认证服务。
请在 `backend-system` 模块中执行以下操作：

1. **依赖配置**:
   - 引入 Security 和 Redis 相关依赖。

2. **核心模型**:
   - 创建 `LoginUser` 实现 `UserDetails` 接口，封装 `SysUser` 和权限集合。

3. **认证逻辑**:
   - 实现 `TokenService`：负责 JWT 的生成、解析和 Redis 存储（实现服务端状态管理，支持踢人）。
   - 实现 `SysLoginService`：处理登录逻辑，调用 `AuthenticationManager`，记录登录日志。
   - 实现 `UserDetailsServiceImpl`：根据用户名查询 `SysUser`（调用 `SysUserService`），并加载权限（调用 `SysMenuService`）。

4. **Security 配置**:
   - 实现 `JwtAuthenticationTokenFilter`。
   - 配置 `SecurityConfig`，定义公开接口和认证规则。
   - 自定义认证失败和鉴权失败的处理器。

5. **接口**:
   - 创建 `SysLoginController`，提供登录、获取用户信息、获取路由、登出接口。

请确保登录成功后，`/getRouters` 接口能正确返回基于用户角色的菜单树，供前端 Vben5 渲染。
```
