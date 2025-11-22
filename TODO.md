# Project TODO List

下面这份待办清单是专门给“AI coding agent/写代码机器人”用的，尽量拆成清晰的小任务，便于你后面把它们变成具体 PR 或工具调用。你可以按模块分配，也可以一条条喂给 agent。

---

## 一、基础项目骨架

- [ ] 1. 创建 Maven 多模块工程骨架
  - 根 `pom.xml`：设定 Java 21、Spring Boot 3、依赖管理（Spring Cloud / Dubbo / Jimmer / Testcontainers 等版本）。
  - 子模块：
    - `common`
    - `core`
    - `infra-integration`
    - `transaction-support`
    - `rpc-api`
    - `rpc-impl`
    - `gateway` (Spring Cloud Gateway)
    - `module/user-tenant-permission`
    - `module/resource`
    - `module/excel`
    - `module/scheduler`
    - `module/logging`
    - `module/flow-engine`（可选）
  - 配置父子模块依赖关系（哪些只依赖接口，避免循环依赖）。
- [ ] 2. 创建 Spring Boot 启动模块
  - 建立主应用（如 `app-boot` 或直接在根启动）
  - 配置基础：
    - 数据源（PostgreSQL）
    - Redis
    - Web（Spring MVC）
    - Actuator
    - Flyway（指向 `db/migrations`）
- [ ] 3. 接入 Flyway 并加载已有迁移脚本
  - 将 `V1__init_core_schema.sql` ~ `V4__init_powerjob_tables.sql` 放入 `src/main/resources/db/migrations`。
  - 配置 `spring.flyway.*`，确保项目启动时自动迁移。

## 二、基础设施与通用组件

- [ ] 4. 定义 AppContext 与上下文传播机制
  - 在 `common` 中创建 `AppContext` record（tenantId/userId/requestId/traceId）。
  - 创建 `AppContextHolder`（ThreadLocal 封装 + 虚拟线程安全用法）。
  - 定义一个 Servlet Filter：
    - 从 JWT/请求头读取用户与租户信息；
    - 生成 requestId；
    - 关联 traceId；
    - 设置到 AppContextHolder。
- [ ] 5. 接入日志（Logback）与 JSON 输出
  - 定义统一日志格式（包含 traceId/tenantId/userId 等字段）。
  - 提供 Logback 配置：
    - 控制台 + 滚动文件；
    - 使用 logstash-logback-encoder 输出 JSON。
- [ ] 6. 接入 Prometheus 指标与 Actuator
  - 在主应用中引入 micrometer-registry-prometheus。
  - 开启 `/actuator/prometheus`。
  - 设置默认 tag：`application`、`env`。
- [ ] 7. OpenTelemetry 基础配置
  - 引入 OTel SDK（或 Spring OTel Starter）。
  - 配置 HTTP server span、JDBC span、Redis span。
  - 实现一个简单的 tracing filter，将 traceId 写入 AppContext 与 MDC。
- [ ] 8. WebSocket 与 SSE 基础支持
  - 在 `gateway` 或 `common` 中配置 WebSocket 路由转发。
  - 提供 SSE (Server-Sent Events) 工具类，用于向前端推送进度。

## 三、安全与认证授权

- [ ] 8. Spring Security 基础配置
  - 配置无状态 JWT 鉴权过滤器链。
  - 保护 `/api/v1/**` 需要认证，`/auth/**` 匿名允许。
- [ ] 9. JWT 工具类与配置
  - 在 `common` 或 `infra-integration`：
    - 定义生成/解析 JWT 的服务：支持 AccessToken/RefreshToken。
    - JWT claims 至少包含 `sub`、`tid`、`roles`、`scopes`。
    - 提供密钥配置与 keyId(kid) 支持。
- [ ] 10. 登录/刷新/登出接口骨架
  - 在 `user-tenant-permission` 的 adapter.rest 中：
    - `POST /auth/login`（返回 Access/RefreshToken）。
    - `POST /auth/refresh`。
  - 对接用户表（先用简单实现或 stub）。
- [ ] 11. 方法级权限注解与权限检查
  - 全局启用 `@PreAuthorize`。
  - 提供一个 PermissionEvaluator 或自定义注解，用于检查权限码（如 `perm:sys:user:list`）。

## 四、多租户 & 数据访问

- [ ] 12. 集成 Jimmer 实体与基础仓储
  - 根据迁移脚本创建 Jimmer 实体（tenant/user/role/permission/menu/resource/import_task 等）。
  - 提供通用 BaseRepository（支持 `tenant_id` 自动注入 / 过滤）。
- [ ] 13. 全局多租户过滤器
  - 在 Jimmer 配置中注册逻辑删除/多租户过滤器：
    - 每次查询自动加上当前 `tenant_id`。
    - 确保 agent 在 repository 中不需要每处都手写 tenantId 条件。

## 五、Outbox 与事件总线

- [ ] 14. Outbox 实体与仓储
  - 基于 `t_outbox` 建立 Jimmer 实体。
  - 定义 OutboxRepository 接口，支持：
    - 保存事件记录（NEW）；
    - 按批量查询待发送记录；
    - 标记 SENT/RETRYING/FAILED。
- [ ] 15. 统一 IntegrationEvent 构造工具
  - 定义一个 `IntegrationEvent` 类和序列化工具（Jackson）。
  - 提供从领域事件 → IntegrationEvent → OutboxRecord 的转换服务。
- [ ] 16. RocketMQ Producer 基础封装
  - 在 `infra-integration` 中：
    - 配置 RocketMQTemplate 或官方 Producer。
    - 封装一个 `EventPublisher`，根据事件类型决定 Topic/Tag。
- [ ] 17. Outbox 出站任务实现（PowerJob Processor 或定时任务）
  - 在 `scheduler` 模块中：
    - 创建 `OutboxDispatchProcessor`：
      - 批量拉取 NEW/RETRYING 记录；
      - 调用 MQ 发送；
      - 更新状态与 retry_count。

## 六、业务模块接口骨架

- [ ] 18. User/Tenant/Permission REST 接口骨架
  - 创建以下 Controller（仅签名与基本参数校验，内部先 TODO）：
    - `/api/v1/tenants`：列表、创建、详情、更新、删除。
    - `/api/v1/users`：分页查询、创建、详情、更新、删除。
    - `/api/v1/roles`：角色列表、创建、关联权限。
    - `/api/v1/permissions`：权限查询。
- [ ] 19. Resource 模块接口骨架
  - 在 `module/resource`：
    - 定义 `BlobStorage` 接口（上传/下载/删除）。
    - 实现 `MinioStorage` 或 `S3Storage` 适配器。
    - `POST /api/v1/resources/upload`（multipart，返回 ResourceDTO）。
    - `GET /api/v1/resources/{id}`（元数据）。
    - `GET /api/v1/resources/{id}/download`（文件流）。
  - 定义 `ResourceService` 接口。
- [ ] 20. Excel 模块接口骨架
  - 在 `module/excel`：
    - `POST /api/v1/excel/import`（multipart 上传 Excel，返回 ImportTaskDTO）。
    - `GET /api/v1/excel/import/tasks/{taskId}`（任务状态）。
    - `GET /api/v1/excel/progress/{taskId}` (SSE 端点，用于实时进度推送)。
  - 定义 `ExcelImportService`。
- [ ] 21. 调度管理接口骨架
  - 在 `module/scheduler`：
    - `GET /api/v1/schedules`（分页查看）
    - `POST /api/v1/schedules`（创建任务）
    - `PUT /api/v1/schedules/{id}/enable`/`disable`
    - `POST /api/v1/schedules/{id}/runOnce`
- [ ] 22. 审计查询接口骨架
  - 在 `module/logging`：
    - `GET /api/v1/logs/audit`：根据时间范围/动作查询 `t_audit_action`。

## 七、限流与缓存

- [ ] 23. 接入 Redis/Redisson
  - 配置 RedissonClient Bean。
  - 提供简单封装：
    - `CacheService`（高频 KV 读写）
    - `LockService`（获取/释放分布式锁）。
- [ ] 24. Sentinel 集成与基本规则加载
  - 集成 Sentinel Web 适配（Spring Cloud Alibaba 或官方 Starter）。
  - 编写一个简单配置类，将常用资源名注册（如登录接口、上传接口）。
  - 给关键接口加 `@SentinelResource` 注解与 blockHandler 模板。

## 八、可观测性与指标埋点

- [ ] 25. 统一封装业务指标
  - 在 `infra-integration` 中：
    - 提供 `MetricsRecorder`（封装 Micrometer）：
      - 资源上传次数与时延；
      - Excel 导入次数与时延；
      - Outbox 出站次数等。
    - 在对应 Service 中插入计时/计数调用。
- [ ] 26. 审计写入封装
  - 创建 `AuditService`：
    - 对外暴露 `recordAction(tenantId, userId, action, targetType, targetId, detail)`。
    - 在登录、用户修改、资源操作等地方统一调用。

## 九、测试与 CI 支撑骨架

- [ ] 27. 引入测试依赖和基础配置
  - 在父 POM 配置：
    - JUnit 5、AssertJ、Mockito、Testcontainers、Spring Boot Test。
- [ ] 28. 编写至少一个示例单元测试 & 集成测试
  - 如：
    - User 聚合的领域测试；
    - 简单 REST Controller 集成测试（MockMvc）+ H2 或 Testcontainers PostgreSQL。
- [ ] 29. 配置 GitHub Actions 或 CI 脚本骨架
  - `mvn -B -ntp verify`
  - Flyway migrate + 测试
  - 将测试报告/覆盖率作为工件输出（Jacoco）。

## 十、对接 OpenAPI 文档

- [ ] 30. 接入 Springdoc 或 Swagger
  - 使用 springdoc-openapi-starter-webmvc-ui。
  - 绑定已有 `docs/api/openapi.yaml`：
    - 配置 `/v3/api-docs` 与 `/swagger-ui.html`。
  - 确保主要接口（login/tenants/users/resources/excel/import 等）都有注释信息（@Operation/@Parameter）。

---

> 提示：你可以按上述编号，逐个给 AI agent 提任务。
