# 第四阶段：多租户与数据访问 (Phase 4: Multi-tenancy & Data Access)

本阶段目标是利用 Jimmer ORM 实现高效的数据访问，并构建严密的多租户隔离机制。

## 4.1 Jimmer 实体与仓储

- [ ] **4.1.1 定义 BaseEntity**

  - 在 `core` 模块中定义 `BaseEntity` 接口或抽象类。
  - 包含通用字段:
    - `id` (主键)
    - `createdTime`
    - `updatedTime`
    - `createdBy`
    - `updatedBy`
  - 配置 Jimmer 的自动填充 (DraftInterceptor) 处理审计字段。

- [ ] **4.1.2 定义多租户实体基类**

  - 定义 `TenantAware` 接口，包含 `tenantId` 方法。
  - 确保所有业务实体 (User, Role, Resource 等) 实现此接口。

- [ ] **4.1.3 创建核心实体**

  - 根据 `V1__init_core_schema.sql` 创建 Jimmer 实体:
    - `SysTenant`
    - `SysUser`
    - `SysRole`
    - `SysMenu`
    - `SysPermission`
  - 配置关联关系 (OneToMany, ManyToOne, ManyToMany)。

- [ ] **4.1.4 创建 Repository**
  - 为每个实体创建 Repository 接口，继承 `JRepository<Entity, ID>`。
  - 编写自定义查询方法 (Jimmer SQL DSL)。

## 4.2 多租户隔离机制

- [ ] **4.2.1 配置全局租户过滤器**

  - 在 Jimmer 配置中注册全局过滤器 `TenantFilter`。
  - 逻辑:
    - 实现 `Filter<TenantAware>`。
    - 在 `filter` 方法中，从 `AppContextHolder` 获取当前 `tenantId`。
    - 如果 `tenantId` 存在，则自动追加 `where tenant_id = ?` 条件。
    - 处理“忽略租户”场景 (如系统管理员跨租户查询)，可通过 `Jimmer.setFilterEnabled(false)` 或特殊 Context 标记。

- [ ] **4.2.2 验证隔离性**
  - 编写集成测试:
    1. 模拟租户 A 的 Context，插入数据。
    2. 模拟租户 B 的 Context，查询数据，断言查不到租户 A 的数据。
    3. 模拟无租户 Context (或系统管理员)，断言能查到所有数据 (如果允许)。

## 4.3 数据库迁移完善

- [ ] **4.3.1 完善 Flyway 脚本**

  - 检查 `db/migration` 下的脚本是否包含所有必要的索引。
  - 重点检查: `tenant_id` 相关的联合索引 (如 `idx_user_tenant_username`)，这对性能至关重要。

- [ ] **4.3.2 数据初始化 (可选)**
  - 创建 `V99__init_demo_data.sql`。
  - 插入默认租户、超级管理员账号、基础菜单和权限数据，方便开发调试。
