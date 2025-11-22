# 项目开发进度追踪 (Project Status)

本文档用于追踪项目的实际开发进度，基于 `doc/todo/` 目录下的详细执行计划。

## 总体进度

- [x] **Phase 1: 基础架构 (Foundation)**
- [x] **Phase 2: 系统模块初始化 (System Setup)**
- [x] **Phase 3: 系统核心 RBAC (System RBAC)**
- [x] **Phase 4: 认证与授权 (Auth)**
- [ ] **Phase 5: 网关与 RPC (Gateway & RPC)**
- [ ] **Phase 6: 文件服务 (File)**
- [ ] **Phase 7: 审计日志 (Audit)**
- [ ] **Phase 8: 批处理与调度 (Batch & Schedule)**
- [ ] **Phase 9: 启动与整合 (Boot)**

---

## 详细任务清单

### Phase 1: 基础架构 (Foundation)

> 对应文档: `doc/todo/01_foundation.md`

- [x] **1.1 backend-common**
  - [x] `Result<T>` 统一响应
  - [x] `AppContext` 上下文
  - [x] `BizException` & `ErrorCode`
  - [x] `JsonUtils` 工具类
- [x] **1.2 backend-core**
  - [x] `BaseEntity` (Jimmer)
  - [x] `TreeEntity`
  - [x] `DomainEvent`
  - [x] CQRS (`Command`, `Query`)
- [x] **1.3 backend-infra**
  - [x] `RedissonConfig`
  - [x] `GlobalExceptionHandler`
  - [x] `JacksonConfig`

### Phase 2: 系统模块初始化 (System Setup)

> 对应文档: `doc/todo/02_system_module_setup.md`

- [x] **2.1 模块创建**
  - [x] 创建 `backend-system` 模块
  - [x] 配置 POM 依赖
- [x] **2.2 基础实体**
  - [x] `SysDictType` (字典类型)
  - [x] `SysDictData` (字典数据)
  - [x] `SysConfig` (参数配置)
- [x] **2.3 业务逻辑**
  - [x] `DictService` 接口与实现
  - [x] `ConfigService` 接口与实现

### Phase 3: 系统核心 RBAC (System RBAC)

> 对应文档: `doc/todo/03_system_rbac.md`

- [x] **3.1 组织架构**
  - [x] `SysDept` (部门实体)
  - [x] `SysPost` (岗位实体)
  - [x] `SysDeptRepository`
  - [x] `SysPostRepository`
- [x] **3.2 权限模型**
  - [x] `SysMenu` (菜单实体)
  - [x] `SysRole` (角色实体)
  - [x] `SysUser` (用户实体)
  - [x] `SysMenuRepository`
  - [x] `SysRoleRepository`
  - [x] `SysUserRepository`
- [x] **3.3 业务逻辑**
  - [x] `SysUserService`
  - [x] `SysMenuService`
  - [x] `SysDeptService`

### Phase 4: 认证与授权 (Auth)

> 对应文档: `doc/todo/04_system_auth.md`

- [x] **4.1 基础配置**
  - [x] 引入 Security & Redis & JWT 依赖
  - [x] `LoginUser` 模型 (UserDetails)
- [x] **4.2 认证核心**
  - [x] `TokenService` (JWT 生成/解析/Redis存储)
  - [x] `SysLoginService` (登录逻辑)
  - [x] `UserDetailsServiceImpl` (加载用户与权限)
- [x] **4.3 Security 集成**
  - [x] `JwtAuthenticationTokenFilter` (过滤器)
  - [x] `AuthenticationEntryPointImpl` (异常处理)
  - [x] `SecurityConfig` (安全配置链)
- [x] **4.4 接口层**
  - [x] `SysLoginController` (登录/登出/用户信息/路由)

### Phase 5: 网关与 RPC (Gateway & RPC)

> 对应文档: `doc/todo/05_gateway_rpc.md`

- [ ] **5.1 RPC API 定义**
- [ ] **5.2 RPC 实现**
- [ ] **5.3 Gateway 路由**

### Phase 6: 文件服务 (File)

> 对应文档: `doc/todo/06_file.md`

- [ ] **6.1 MinIO 集成**
- [ ] **6.2 文件上传下载接口**

### Phase 7: 审计日志 (Audit)

> 对应文档: `doc/todo/07_audit.md`

- [ ] **7.1 操作日志实体**
- [ ] **7.2 AOP 记录日志**

### Phase 8: 批处理与调度 (Batch & Schedule)

> 对应文档: `doc/todo/08_batch_schedule.md`

- [ ] **8.1 PowerJob 集成**
- [ ] **8.2 Excel 导入导出**

### Phase 9: 启动与整合 (Boot)

> 对应文档: `doc/todo/09_boot.md`

- [ ] **9.1 启动类**
- [ ] **9.2 配置文件**
- [ ] **9.3 联调测试**
