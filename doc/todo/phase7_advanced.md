# 第七阶段：高级特性与完善 (Phase 7: Advanced Features)

本阶段关注系统的稳定性、安全性增强以及工程化质量的提升。

## 7.1 缓存与限流 (Cache & Rate Limiting)

- [ ] **7.1.1 封装 CacheService**

  - 提供统一的缓存操作接口 (Get/Put/Evict)。
  - 实现多级缓存逻辑 (Caffeine + Redis)。
  - 使用 Spring Cache 注解 (`@Cacheable`) 优化读性能 (如菜单树、配置项)。

- [ ] **7.1.2 集成 Sentinel**

  - 引入 `spring-cloud-starter-alibaba-sentinel`。
  - 配置 Nacos 或 Apollo 作为规则数据源 (可选，初期可本地配置)。
  - **网关限流**: 在 `gateway` 模块配置 Sentinel Gateway Adapter，对 API 分组进行限流。
  - **热点参数限流**: 对 `login` 接口 (按 IP)、`import` 接口 (按 Tenant) 配置热点规则。

- [ ] **7.1.3 分布式锁**
  - 封装 `LockService` (基于 Redisson)。
  - 在资源上传 (防并发上传同一文件)、Excel 任务提交等场景应用。

## 7.2 审计与安全增强

- [ ] **7.2.1 实现 AuditService**

  - 异步记录操作日志 (`t_audit_action`)。
  - 关键操作 (登录、增删改) 通过 AOP 自动拦截并记录。

- [ ] **7.2.2 敏感数据脱敏**
  - 定义 `@Sensitive` 注解和 Jackson Serializer。
  - 对手机号、身份证等字段进行脱敏输出。

## 7.3 测试与质量保证

- [ ] **7.3.1 单元测试**

  - 为核心 Domain Service 编写 JUnit 5 测试。
  - 使用 Mockito 模拟 Repository 和外部依赖。

- [ ] **7.3.2 集成测试**

  - 使用 `Testcontainers` 启动 PostgreSQL 和 Redis 容器。
  - 测试 Repository 层 SQL 正确性。
  - 测试 Controller 层端到端流程 (`MockMvc`)。

- [ ] **7.3.3 性能测试准备**
  - 编写 JMeter 或 Gatling 脚本。
  - 模拟多租户登录、高并发查询场景。

## 7.4 文档与 CI/CD

- [ ] **7.4.1 OpenAPI 文档**

  - 集成 `springdoc-openapi`。
  - 完善 Controller 和 DTO 的 `@Schema`, `@Operation` 注解。
  - 配置 Swagger UI。

- [ ] **7.4.2 CI/CD 脚本**
  - 编写 GitHub Actions workflow (`.github/workflows/maven.yml`)。
  - 包含: 编译 -> 单元测试 -> 集成测试 -> 构建 Docker 镜像。
