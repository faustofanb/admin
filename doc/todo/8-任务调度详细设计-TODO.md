# 第 8 章 任务调度详细设计 TODO 清单（AI Agent 实施指引）

> 本章对应 `doc/design/8.任务调度详细设计.md`，聚焦：
>
> - 基于 PowerJob 的统一任务调度与执行模型；
> - 定时任务（如数据同步、报表生成、过期清理等）与一次性异步任务；
> - 与多租户、安全、Outbox、Excel 批处理等模块的协作；
> - 调度任务的可观测性（日志、指标、Tracing）。
>
> 要求：
>
> - 不在任务处理器中写业务 SQL，统一调用应用服务；
> - 所有任务应是幂等的，可重试；
> - 任务执行需有清晰的日志与 Metrics，便于排查问题。

---

## 8.1 调度总体建模与职责边界

**设计约定（文档说明）**：

1. 调度角色划分：

   - 调度平台（PowerJob Server）：负责任务的调度、分片与重试策略；
   - 后端服务（本项目）：通过 SDK 注册具体任务处理器，并实现执行逻辑；
   - 业务模块：通过应用服务暴露“可被调度调用”的方法。

2. 任务分类：

   - 周期性任务：如清理过期数据、同步外部系统数据、重试 Outbox 消息等；
   - 一次性异步任务：如大批量计算、历史数据迁移（可选）；
   - 业务批处理任务：如 Excel 导入/导出执行（与第 9 章协同）。

3. 与虚拟线程/并发控制：
   - 调度任务默认运行在线程池中（可考虑虚拟线程池）；
   - 对高并发任务增加 BackPressure 控制与限流（参考第 4、6 章）。

---

## 8.2 调度配置与基础设施

### 8.2.1 调度配置属性 `SchedulerProperties`

**类：** `io.github.faustofanb.admin.scheduler.SchedulerProperties`

**TODO**：

1. 字段示例：

   - [ ] `boolean enabled`（是否启用调度功能）；
   - [ ] `String powerJobServerAddress`；
   - [ ] `String appName`；
   - [ ] `int workerThreads`；

2. 绑定配置前缀：

   - [ ] `@ConfigurationProperties("app.scheduler")`；

3. **TDD 占位**：
   - [ ] `SchedulerPropertiesTest.shouldBindConfigurationProperties()`。

---

### 8.2.2 PowerJob 客户端配置 `PowerJobClientConfig`

**类：** `io.github.faustofanb.admin.scheduler.PowerJobClientConfig`

**TODO（方法级）**：

1. 依赖：

   - [ ] `SchedulerProperties`；

2. Bean 定义（具体类名按 PowerJob SDK 而定）：

   - [ ] `public SomePowerJobClient powerJobClient()`：
     - 使用 `powerJobServerAddress`、`appName` 等创建并配置客户端；
   - [ ] 基于虚拟线程或专用线程池的执行器配置（如需要）。

3. **TDD 占位**：
   - [ ] `PowerJobClientConfigTest.shouldCreateClientWhenEnabled()`。

> 具体 PowerJob SDK API 细节在接入时确认，此处以 TODO 形式保留。

---

## 8.3 统一任务处理接口与基类

### 8.3.1 任务处理接口 `ScheduledTaskHandler`

**接口：** `io.github.faustofanb.admin.scheduler.ScheduledTaskHandler`

**TODO（方法级）**：

1. 方法：

   - [ ] `String getName()`：任务唯一名称（便于日志与 Metrics）；
   - [ ] `void execute(ScheduledTaskContext context)`：执行任务；

2. `ScheduledTaskContext`：

   - 包：`io.github.faustofanb.admin.scheduler`
   - 字段：
     - [ ] `Long tenantId`（可选，部分任务按租户执行）；
     - [ ] `String paramsJson`（调度参数，如时间范围、任务类型等）；
   - 方法：
     - [ ] 便捷解析 `paramsJson` 为目标 DTO 的方法；

3. **TDD 占位**：
   - [ ] `ScheduledTaskContextTest.shouldParseParamsJson()`。

---

### 8.3.2 抽象基类 `AbstractScheduledTaskHandler`

**类：** `io.github.faustofanb.admin.scheduler.AbstractScheduledTaskHandler`

**TODO**：

1. 提供模板方法：

   - [ ] `public final void execute(ScheduledTaskContext context)`：
     - 记录开始时间；
     - 调用抽象方法 `doExecute(context)`；
     - 捕获异常，记录日志与 Metrics；
   - [ ] `protected abstract void doExecute(ScheduledTaskContext context)`；

2. Metrics 与日志集成：

   - 使用 `MetricsHelper` 记录执行次数、成功/失败数与耗时；
   - 使用 `LoggingContext` 填充 traceId/tenantId 等信息。

3. **TDD 占位**：
   - [ ] `AbstractScheduledTaskHandlerTest.shouldRecordSuccessMetrics()`；
   - [ ] `AbstractScheduledTaskHandlerTest.shouldRecordFailureMetricsOnException()`。

---

## 8.4 典型系统任务处理器 TODO

### 8.4.1 Outbox 扫描任务 `OutboxDispatchJobHandler`

> 对应第 5 章 Outbox 的出站任务。

**类：** `io.github.faustofanb.admin.scheduler.OutboxDispatchJobHandler`

**TODO（方法级）**：

1. 依赖：

   - [ ] `OutboxDispatchService`（第 5 章定义的出站任务服务）；

2. 行为：

   - [ ] 在 `doExecute` 中调用 `outboxDispatchService.dispatchOnce()`；
   - [ ] 可根据上下文参数决定扫描批量大小或特定 tenant；

3. **TDD 占位**：
   - [ ] `OutboxDispatchJobHandlerTest.shouldDelegateToDispatchService()`。

---

### 8.4.2 Excel 批处理任务执行器 `ExcelTaskJobHandler`

> 配合第 9 章 Excel 批处理，将 Excel 导入/导出的 `execute(taskId)` 挂到调度体系下。

**类：** `io.github.faustofanb.admin.scheduler.ExcelTaskJobHandler`

**TODO（方法级）**：

1. 依赖：

   - [ ] `ExcelImportExecutor`；
   - [ ] `ExcelExportExecutor`；

2. 参数约定：

   - `ScheduledTaskContext.paramsJson` 中携带：`taskId`、`taskType`（IMPORT/EXPORT）、`tenantId` 等；

3. 行为：

   - [ ] 在 `doExecute` 中解析参数，加载对应 `ExcelTask`；
   - [ ] 根据任务类型调用 `importExecutor.execute(taskId)` 或 `exportExecutor.execute(taskId)`；

4. **TDD 占位**：
   - [ ] `ExcelTaskJobHandlerTest.shouldDispatchToCorrectExecutor()`。

---

### 8.4.3 数据清理与归档任务

> 例如清理过期 Token、删除历史日志、归档操作记录等。

**类示例**：

- `ExpiredTokenCleanupJobHandler`
- `AuditLogArchiveJobHandler`

**TODO（方法级与约定）**：

1. 均继承 `AbstractScheduledTaskHandler`，在 `doExecute` 中调用对应应用服务方法：

   - [ ] `TokenCleanupService.cleanupExpiredTokens()`；
   - [ ] `AuditLogService.archiveOldLogs(Duration retention)`；

2. 注意：

   - 任务应支持分批清理，避免一次性删除过多数据；
   - 加强日志与 Metrics（记录清理数量）。

3. **TDD 占位**：
   - [ ] `ExpiredTokenCleanupJobHandlerTest.shouldCallCleanupService()`；
   - [ ] `AuditLogArchiveJobHandlerTest.shouldArchiveOldLogs()`。

---

## 8.5 多租户与安全约束

**TODO（文档约定）**：

1. 多租户：

   - 对于按租户运行的任务，应从 `ScheduledTaskContext` 中读取 `tenantId` 并设置到 AppContext 中；
   - 严禁在单租户任务中跨租户操作数据。

2. 安全：
   - 调度调用内部应用服务时不需要用户凭证，但需在审计日志中记录“系统任务”身份；
   - 对于可能被外部触发的“临时任务创建接口”，必须做权限校验。

---

## 8.6 与 Outbox、Excel、缓存和可观测性的协同

**TODO（文档约定）**：

1. Outbox：

   - 重试失败 Outbox 消息可以作为单独调度任务实现（例如按固定频率扫描 FAILED 状态 Outbox）；

2. Excel：

   - Excel 导入/导出任务通过 `ExcelTaskJobHandler` 统一接入调度体系；
   - 调度任务执行过程中使用 Excel 模块的日志与 Metrics 约定。

3. 缓存：

   - 某些任务可用于刷新缓存（如预热权限缓存、清理过期 Key），注意控制频率与限流。

4. 可观测性：
   - 每个 `ScheduledTaskHandler` 均应在执行前后记录日志，包含任务名称、参数、耗时、结果；
   - 使用 Metrics 为各类任务记录成功/失败次数与耗时分布，支持在 Grafana 中按任务维度查看健康状况。

---

> 本章完成后，系统将具备：
>
> - 统一的任务调度抽象与 PowerJob 集成基础；
> - 标准化的任务处理接口与模板基类；
> - 常见系统任务（Outbox 扫描、Excel 执行、数据清理等）的实现指引；
> - 明确的多租户、安全与可观测性约束。
