# 多租户后台管理系统

[![Project Status](https://img.shields.io/badge/Status-In%20Progress-yellow)](doc/PROJECT_STATUS.md)

> 📊 **[查看详细开发进度 (Project Status)](doc/PROJECT_STATUS.md)**

> 统一 README（面向开发与架构），与 `docs/design` 下的各章节详细设计配套使用。

---

## 1. 项目简介

多租户后台管理系统是基于 **Java 21 + Spring Boot 3.x** 的企业级后台基础平台，提供：

- 多租户用户体系：租户 / 用户 / 组织 / 菜单 / 角色 / 权限（RBAC + 可扩 ABAC）
- 资源管理：统一文件 / 图片 / 文档存储与访问控制（后端：RustFS）
- Excel 批处理：大批量导入 / 导出（EasyExcel，流式、分批）
- 任务调度：分布式调度与任务管理（PowerJob）
- 流程编排（可选）：复杂多步骤业务流（LiteFlow）
- 分布式事务与一致性：本地事务 + Outbox + RocketMQ + Seata（预备）
- 缓存与限流：Redis/Redisson + Sentinel（多维限流、熔断）
- 可观测性：Promtail/Loki/Grafana（日志）+ Micrometer/Prometheus（指标）+ OpenTelemetry（Trace）
- 平滑演化：从单体多模块 → 多服务（Dubbo RPC + 事件驱动）

本项目既可作为实际业务后台，也可作为“现代 Java 企业应用脚手架”。

---

## 2. 总体架构

### 2.1 技术栈

- 语言与框架
  - Java 21（虚拟线程）
  - Spring Boot 3.x
  - Maven 多模块
  - Jimmer（ORM）
  - Spring Security（安全）
  - Dubbo（RPC）
  - LiteFlow（流程编排，可选）
- 中间件
  - PostgreSQL（主写从读）
  - Redis + Redisson（缓存 / 分布式锁 / 限流器）
  - RocketMQ（事件总线）
  - Seata（分布式事务预备）
  - RustFS / MinIO / S3（统一 BlobStorage 抽象）
  - PowerJob（任务调度）
  - Sentinel（限流 / 熔断）
  - Spring Cloud Gateway（API 网关）
  - WebSocket / SSE（实时进度推送）
- 可观测性
  - Promtail + Loki + Grafana（日志）
  - Micrometer + Prometheus（指标）
  - OpenTelemetry（Trace）
- 前端
  - Vben Admin 5（Vue 3 + TypeScript）

### 2.2 分层架构

```text
[ 前端：Vben Admin ]
        │ REST / JSON / WebSocket
        ▼
[ 网关层：Spring Cloud Gateway ]
  ├─ 认证 / 限流 / 路由
  └─ SSE 推送通道
        │
        ▼
[ 接口层 ]
  ├─ REST Controller
  └─ Dubbo RPC Endpoint (初期 injvm)

[ 应用层 ]
  ├─ Command Handler（写）
  └─ Query Handler（读）

[ 领域层 ]
  ├─ 聚合根 / 实体 / 值对象
  ├─ 领域服务
  └─ 领域事件

[ 基础设施层 ]
  ├─ Jimmer + PostgreSQL
  ├─ Redis + Redisson
  ├─ RocketMQ + Outbox
  ├─ RustFS
  ├─ PowerJob
  ├─ Sentinel
  ├─ Seata
  ├─ Logging & Metrics
  └─ OpenTelemetry

[ 适配与集成 ]
  ├─ rpc-api / rpc-impl（Dubbo 契约）
  ├─ infra-integration（中间件封装）
  └─ transaction-support（事务 / 一致性工具）
```

---

## 3. 模块结构

### 3.1 Maven 模块

```text
pom.xml (parent)
 ├─ backend-common/         # 通用：异常、DTO/VO、AppContext、工具
 ├─ backend-core/           # 领域模型、命令/查询、领域服务、事件
 ├─ backend-infra/          # Redis、RocketMQ、Sentinel、监控、OTel 封装
 ├─ backend-transaction/    # Seata、Outbox、一致性工具
 ├─ backend-rpc-api/        # Dubbo 契约接口 + DTO
 ├─ backend-rpc-impl/       # Dubbo Provider 实现（调用应用服务）
 ├─ backend-gateway/        # Spring Cloud Gateway 网关
 ├─ backend-boot/           # 启动入口
 └─ module/
     ├─ backend-system/             # 用户/租户/组织/菜单/角色/权限
     ├─ backend-file/            # 资源与文件管理（RustFS）
     ├─ backend-batch/           # Excel 导入/导出
     ├─ backend-schedule/        # 任务调度（PowerJob Worker & 任务模型）
     └─ backend-audit/           # 日志 / 审计查询 API（封装 Loki/DB）
```

每个业务模块内部一般结构：

```text
io.github.faustofan.admin.<module>
  ├─ app        # 应用层：command / query
  ├─ domain     # 领域层：model / service / event
  ├─ infra      # 基础设施：repository / client / mq 等
  ├─ adapter    # 接口适配：rest / rpc
  └─ config     # 模块配置
```

---

## 4. 关键能力概览（对应详细设计章节）

> 详细说明请参见 `docs/design` 下对应章节。

### 4.1 安全设计（第 2 章）

- JWT（Access/Refresh），claims 中包含 `sub`（userId）、`tid`（tenantId）、`roles`、`scopes`
- RBAC + 可扩 ABAC
- 多租户隔离：
  - 默认共享 Schema（`tenant_id` 隔离）
  - 支持混合模式：关键租户可配置独立 Schema 或 Database（通过动态数据源路由）
  - AppContext(tenantId,userId,requestId,traceId) 贯穿 HTTP / RPC / 任务
  - 缓存 Key 与限流规则均加租户维度
- 审计：
  - 审计表 `t_audit_action` + Loki JSON 日志双存储
- 安全防护：
  - 登录防暴力破解，接口限流（Sentinel + Redisson）
  - 敏感字段加密、日志脱敏

### 4.2 性能与高并发（第 3 章）

- Java 21 虚拟线程：所有 IO 密集路径使用虚拟线程执行
- PostgreSQL：
  - 读写分离（主写从读）
  - 键集分页 / 覆盖索引 / 避免深度 offset
- 并发控制：
  - 聚合根强制启用 `@Version` 乐观锁，防止并发覆写
- 缓存：
  - 本地 Caffeine + Redis/Redisson 多级缓存
- 限流与降级：
  - Sentinel 流控、熔断、热点参数（按租户）
- 批处理：
  - Excel 导入：流式读取 + 分批校验 + 批量写库

### 4.3 分布式事务与一致性（第 4 章）

- 默认模式：本地事务 + Outbox + RocketMQ → 最终一致
- 优化方案：
  - 优先使用 RocketMQ 事务消息（Transaction Message），省去 Outbox 表轮询，实现零延迟
  - 兜底方案：`t_outbox` 表 + CDC/定时扫描（针对不支持事务消息的场景）
- 消费端必须幂等：
  - 业务去重键 / Redis SetNX / 去重表
- Seata：
  - 已集成 DataSourceProxy，必要场景可启用 AT/SAGA

### 4.4 缓存与限流策略（第 5 章）

- Redis/Redisson：
  - Key 规范：`t:{tenantId}:{domain}:{entity}:{id}`
  - 分布式锁（上传去重等）
  - RateLimiter：如 `excel:import:{tenantId}`
- Sentinel：
  - 资源命名：`api:METHOD:/path`、`flow:{code}`、`flow-node:{id}`
  - QPS/并发限流 + RT 降级 + 热点参数
- 模式：Cache-Aside + Outbox 事件驱动刷新

### 4.5 资源与文件管理（第 6 章）

- 存储抽象：
  - 定义 `BlobStorage` 接口，实现 S3/MinIO/RustFS 适配，避免绑定特定厂商
- 元数据表 `t_resource_meta`（租户 + storeKey + sha256 + ACL 等）
- 上传：
  - 计算 sha256 → 分布式锁 → 查重（秒传） → BlobStorage 写入 → 元数据入库 → 事件 `ResourceUploaded`
- 下载：
  - ACL 校验（private/public/role-based） → 签名 URL 或流式返回
- 清理：
  - 逻辑删除（status=DELETED）+ 定时物理删除

### 4.6 日志与可观测性（第 7 章）

- 日志：
  - JSON 格式 → 文件 → Promtail → Loki → Grafana
  - 包含 ts / level / logger / traceId / tenantId / userId / path / latency / err
- 指标（Micrometer → Prometheus）：
  - http.server.requests / jvm.\* / 自定义业务指标
- Trace（OpenTelemetry）：
  - HTTP / Dubbo / RocketMQ / PowerJob / LiteFlow 全链路追踪
- 提供多套 Grafana Dashboard 和告警规则

### 4.7 任务调度（第 8 章）

- PowerJob Server 独立部署，主应用作为 Worker
- 领域模型：
  - `t_scheduled_task`（任务定义）
  - `t_task_execution`（执行记录）
- 常用任务：
  - Outbox 扫描出站
  - 资源清理
  - Excel 后处理
  - 缓存预热 / 报表生成
- 所有任务要求幂等 + 可观测（日志 + 指标）

### 4.8 Excel 批处理（第 9 章）

- 导入：
  - `t_import_task` 管理任务状态
  - EasyExcel 流式读取，按 1000 行分批
  - 多层校验（模板 / 行 / 业务 / 跨行）
  - 分批事务写库 + 错误行记录 + 错误报告导出（资源模块）
- 导出：
  - 分页查询 + EasyExcel 写出 → 可上传到资源模块
- 进度反馈：
  - 导入/导出任务通过 WebSocket 或 SSE (Server-Sent Events) 实时推送进度条
  - 前端监听 `task:{taskId}` 频道更新 UI
- 限流：
  - 每租户导入频率限流（Redisson RateLimiter + Sentinel）

### 4.9 RPC 与接口契约（第 10 章）

- `rpc-api`：统一 Dubbo 契约与 DTO
- 初期协议：`injvm`（单体内调用），配置“引用传递 (Pass by Reference)”避免序列化开销
- 未来演进：可无缝切为 Dubbo 远程协议 + 注册中心
- 典型 Facade：
  - `UserTenantPermissionFacade`
  - `ResourceFacade`
  - `ExcelFacade` 等
- Dubbo Filter 透传 AppContext（tenantId/userId/traceId/requestId）
- 超时与重试：
  - 读接口可短超时 + 少量重试
  - 写接口不自动重试，交给事件/补偿

### 4.10 事件总线与异步处理（第 11 章）

- 事件分层：
  - **领域事件 (Domain Event)**：JVM 内部 Spring Event，事务内/后同步执行，解耦模块内逻辑
  - **集成事件 (Integration Event)**：跨服务边界，走 Outbox/事务消息 + RocketMQ
- Topic：
  - `user.events` / `tenant.events` / `resource.events` / `permission.events` / `excel.events`
- 出站任务：批量从 `t_outbox` 读取 → 发送 RocketMQ → 更新状态
- 消费端：
  - 幂等处理
  - 错误/重试/死信队列处理
- 用途：
  - 缓存刷新
  - 资源后处理（缩略图/索引）
  - 导入完成通知等

### 4.11 LiteFlow 流程编排（第 12 章，可选）

- 流程定义表 `t_flow_definition`（tenantId + flowCode + dsl_text + version）
- 自定义上下文 `FlowCtx`，包含 AppContext + 流程数据
- 典型流程：
  - 租户开通（初始化角色/菜单/资源等）
  - Excel 导入多步骤校验与持久化
- 与事务/Outbox/Sentinel/可观测性深度联动

### 4.12 TDD 与质量保证（第 14 章）

- 测试金字塔：
  - 单元 → 组件 → 集成 → E2E → 性能/安全/混沌
- TDD 流程：先写测试，再实现，再重构
- 工具：
  - JUnit5、Testcontainers、Mockito、Jacoco
- CI Gate：
  - 单元 + 组件测试必过
  - 静态分析（Checkstyle/SpotBugs/Sonar 等）
  - 覆盖率门槛（全局 70%+，关键模块更高）

### 4.13 演化路线与微服务拆分（第 15 章）

- 阶段 0：单体稳定期（当前）
- 阶段 1：拆 Resource / Logging 为独立服务
- 阶段 2：拆 system（system）为 `system-service`
- 阶段 3：拆 Flow / Excel 等扩展服务
- 通过：
  - Dubbo RPC + 事件总线
  - 统一 API Gateway/BFF
  - Outbox/事件一致性
    实现平滑迁移

---

## 5. 目录结构与文档索引

### 5.1 代码目录（示例）

```text
.
├─ README.md
├─ pom.xml
├─ common/
├─ core/
├─ infra-integration/
├─ transaction-support/
├─ rpc-api/
├─ rpc-impl/
├─ module/
│  ├─ system/
│  ├─ resource/
│  ├─ excel/
│  ├─ scheduler/
│  ├─ logging/
│  └─ flow-engine/
└─ docs/
   └─ design/
      ├─ 01-overview-and-conventions.md
      ├─ 02-security-design.md
      ├─ 03-performance-and-high-concurrency.md
      ├─ 04-transaction-consistency.md
      ├─ 05-cache-and-rate-limit.md
      ├─ 06-resource-and-rustfs-design.md
      ├─ 07-logging-and-observability.md
      ├─ 08-scheduler-powerjob-design.md
      ├─ 09-excel-easyexcel-design.md
      ├─ 10-rpc-dubbo-design.md
      ├─ 11-event-bus-mq.md
      ├─ 12-liteflow-orchestration-design.md
      ├─ 14-tdd-and-quality.md
      └─ 15-evolution-and-microservices.md
```

> 可以根据实际命名调整，但建议保持编号与章节一致，方便索引。

---

## 6. 本地开发快速上手（建议）

> 具体命令和配置会根据你最终仓库确定后再细化，这里给出推荐流程：

1. 准备环境：
   - JDK 21
   - Maven 3.9+
   - Docker + Docker Compose（用于本地跑 Postgres / Redis / RocketMQ / PowerJob / Loki / Prometheus 等）
2. 启动基础服务（示意）：
   - `docker compose up -d postgres redis rocketmq powerjob loki promtail prometheus grafana`
3. 初始化数据库：
   - 执行 Flyway/Liquibase 迁移脚本（待在 `docs/db` 中提供）
4. 启动应用：
   - `mvn spring-boot:run`（或通过 IDE 启动主模块）
5. 访问：
   - 后端健康检查：`http://localhost:8080/actuator/health`
   - Prometheus 指标：`http://localhost:8080/actuator/prometheus`
   - 前端（Vben）：单独仓库或子目录，对应配置 `VITE_GLOB_API_URL` 指向后端

---

## 7. 开发规范（简要）

- 严格区分：
  - `domain`（纯业务逻辑，不依赖框架）
  - `app`（用例编排，薄逻辑）
  - `infra`（访问 DB/缓存/MQ/外部服务）
  - `adapter`（REST/RPC 接口）
- 所有跨模块调用优先：
  - RPC（通过 `rpc-api`）
  - 事件（通过 Outbox + MQ）
- 所有写操作必须：
  - 明确事务边界
  - 明确是否需要 Outbox 事件
- 所有新功能必须：
  - 至少附带单元测试
  - 关键路径附带组件/集成测试
- 安全相关：
  - 不得直接从请求中信任 `tenantId`，统一从 AppContext 读取
  - 日志禁止输出密码、Token 明文等敏感信息

---

## 8. 进一步阅读

- 架构总览与约定：`docs/design/01-overview-and-conventions.md`
- 安全：`docs/design/02-security-design.md`
- 性能与高并发：`docs/design/03-performance-and-high-concurrency.md`
- 分布式事务与一致性：`docs/design/04-transaction-consistency.md`
- 缓存与限流：`docs/design/05-cache-and-rate-limit.md`
- 资源与文件管理：`docs/design/06-resource-and-rustfs-design.md`
- 日志与可观测性：`docs/design/07-logging-and-observability.md`
- 任务调度：`docs/design/08-scheduler-powerjob-design.md`
- Excel 批处理：`docs/design/09-excel-easyexcel-design.md`
- RPC 与接口契约：`docs/design/10-rpc-dubbo-design.md`
- 事件总线与异步处理：`docs/design/11-event-bus-mq.md`
- 流程编排引擎：`docs/design/12-liteflow-orchestration-design.md`
- TDD 与质量保证：`docs/design/14-tdd-and-quality.md`
- 演化路线与微服务拆分：`docs/design/15-evolution-and-microservices.md`
