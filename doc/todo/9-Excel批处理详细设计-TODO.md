# 第 9 章 Excel 批处理详细设计 TODO 清单（AI Agent 实施指引）

> 本章对应 `doc/design/9.Excel批处理详细设计.md`，聚焦：
>
> - 大批量 Excel 导入（异步任务、分片处理、失败收集与回写）；
> - Excel 导出（分页拉取数据、流式写出、保护内存）；
> - 任务生命周期管理（提交、排队、执行、完成、失败、取消）；
> - 与多租户、安全、缓存、日志与可观测性的协同。
>
> 要求：
>
> - 不在 Controller 中直接操作 Excel，全部通过应用服务与领域服务封装；
> - 任务本身必须是**可观测的**：有进度、错误摘要、审计记录；
> - 与第 4 章（虚拟线程 & 数据访问）、第 6 章（缓存 & 限流）、第 7 章（日志 & 可观测性）保持一致。

---

## 9.1 Excel 任务总体建模

### 9.1.1 任务类型与状态枚举

**枚举类：**

- `io.github.faustofanb.admin.excel.domain.ExcelTaskType`
- `io.github.faustofanb.admin.excel.domain.ExcelTaskStatus`

**TODO**：

1. `ExcelTaskType`：

   - [ ] 枚举值示例（根据业务可扩展）：
     - `USER_IMPORT`, `USER_EXPORT`, `PERMISSION_IMPORT`, `PERMISSION_EXPORT`, `GENERIC_IMPORT`, `GENERIC_EXPORT`；
   - [ ] 字段：`code`、`description`；
   - [ ] 方法：`public static ExcelTaskType fromCode(String code)`（非法 code 抛 `BusinessException`）。

2. `ExcelTaskStatus`：

   - [ ] 枚举值：`PENDING`, `RUNNING`, `SUCCESS`, `FAILED`, `CANCELLED`;
   - [ ] 可选字段：`boolean terminal`；
   - [ ] 方法：`public boolean isTerminal()`。

3. **TDD**：
   - [ ] `ExcelTaskTypeTest.shouldMapCodeCorrectly()`；
   - [ ] `ExcelTaskStatusTest.shouldRecognizeTerminalStatus()`。

---

### 9.1.2 Excel 任务聚合根 `ExcelTask`

**类：** `io.github.faustofanb.admin.excel.domain.ExcelTask`

**TODO（字段与行为）**：

1. 字段：

   - [ ] `Long id`；
   - [ ] `Long tenantId`；
   - [ ] `Long createdBy`（用户 ID）；
   - [ ] `ExcelTaskType type`；
   - [ ] `ExcelTaskStatus status`；
   - [ ] `String paramsJson`（导入/导出参数，如过滤条件、模板 ID 等）；
   - [ ] `int successCount`；
   - [ ] `int failCount`；
   - [ ] `String errorSummary`（失败摘要，限制长度）；
   - [ ] `String resultFileUrl`（导出成功后文件地址）；
   - [ ] `Instant createdAt`；
   - [ ] `Instant startedAt`；
   - [ ] `Instant finishedAt`；

2. 领域行为：

   - [ ] `public void markRunning()`：
     - 仅在当前为 `PENDING` 时允许；
     - 设置 `status = RUNNING`，`startedAt = now`；
   - [ ] `public void updateProgress(int successCountDelta, int failCountDelta)`：
     - 累加计数；
   - [ ] `public void markSuccess(String resultFileUrl)`：
     - 设置 `status = SUCCESS`、`resultFileUrl`、`finishedAt = now`、`progress = 100`；
   - [ ] `public void markFailed(String errorSummary)`：
     - `status = FAILED`，`errorSummary` 截断到最大长度，`finishedAt = now`；
   - [ ] `public void markCancelled(String reason)`：
     - 仅允许从 `PENDING`/`RUNNING` 取消；
     - 设置 `status = CANCELLED`，记录 `errorSummary` 或单独 `cancelReason` 字段；

3. **TDD**：
   - [ ] `ExcelTaskTest.shouldTransitionFromPendingToRunning()`；
   - [ ] `ExcelTaskTest.shouldMarkSuccessAndSetResultUrl()`；
   - [ ] `ExcelTaskTest.shouldMarkFailedAndTruncateErrorSummary()`；
   - [ ] `ExcelTaskTest.shouldNotAllowIllegalStatusTransitions()`。

---

### 9.1.3 任务仓储接口 `ExcelTaskRepository`

**接口：** `io.github.faustofanb.admin.excel.domain.ExcelTaskRepository`

**TODO（方法级）**：

1. 基本方法：

   - [ ] `ExcelTask save(ExcelTask task)`；
   - [ ] `Optional<ExcelTask> findByIdAndTenant(Long id, Long tenantId)`；

2. 查询待执行任务：

   - [ ] `List<ExcelTask> findPendingTasks(int limit)`：
     - 按 `PENDING` 且创建时间排序，限制批量数；

3. 查询中/终态统计（可选）：

   - [ ] `Page<ExcelTask> findByTenantAndCreatedBy(Long tenantId, Long userId, PageRequest pageRequest)`；

4. **TDD 占位**：
   - [ ] `ExcelTaskRepositoryTest.shouldFindPendingTasks()`。

---

## 9.2 Excel 导入：模板与解析

### 9.2.1 导入模板描述 `ExcelImportTemplate`

**类：** `io.github.faustofanb.admin.excel.imports.ExcelImportTemplate`

**TODO**：

1. 字段：

   - [ ] `ExcelTaskType taskType`；
   - [ ] `List<ExcelImportColumn> columns`；
   - [ ] `boolean allowDuplicate`（是否允许重复行）；

2. 嵌套类/独立类 `ExcelImportColumn`：

   - [ ] 字段：`String headerName`、`String fieldName`、`boolean required`、`String regex`（格式校验）、`String dictCode`（字典映射可选）；

3. **TDD**：
   - [ ] `ExcelImportTemplateTest.shouldValidateRequiredColumns()`（仅文档占位，可在实现时细化）。

### 9.2.2 文件上传与任务创建 `ExcelImportApplicationService`

**类：** `io.github.faustofanb.admin.excel.imports.ExcelImportApplicationService`

**TODO（方法级）**：

1. 依赖：

   - [ ] `ExcelTaskRepository`；
   - [ ] 文件存储服务（在资源模块中定义的 BlobStorage 抽象，如 `FileStorageService`）；
   - [ ] `AuditLogService`；

2. 请求/响应 DTO：

   - `CreateImportTaskResponse`：`taskId`；

3. 方法：

   - [ ] `public CreateImportTaskResponse createImportTask(CreateImportTaskRequest request)`：
     - 校验 `taskType` 与模板存在性；
     - 将上传文件保存到存储（返回文件 URL）；
     - 创建 `ExcelTask`（status=PENDING，记录 fileUrl/params），持久化；
     - 记录审计日志（谁在何时提交了哪种导入）；
     - 返回 `taskId`。

4. **TDD 占位**：
   - [ ] `ExcelImportApplicationServiceTest.shouldCreatePendingTask()`；
   - [ ] `ExcelImportApplicationServiceTest.shouldAuditImportSubmission()`。

### 9.2.3 导入执行器 `ExcelImportExecutor`

> 真正做行级读取、校验、落库的组件，通常作为异步任务执行。

**类：** `io.github.faustofanb.admin.excel.imports.ExcelImportExecutor`

**TODO（方法级）**：

1. 依赖：

   - [ ] `ExcelTaskRepository`；
   - [ ] 行处理器 SPI（见下 `ExcelRowHandler`）；
   - [ ] 读 Excel 的工具（可考虑 EasyExcel/Apache POI，在 infra 模块中实现具体读写）；
   - [ ] 日志与指标：`MetricsHelper`、`AuditLogService`；

2. 行处理 SPI 接口 `ExcelRowHandler`

   - 包：`io.github.faustofanb.admin.excel.imports.spi`；
   - [ ] 方法：`void handleRow(ExcelTask task, Map<String, String> rowData, ExcelImportContext context)`；
   - 由不同业务（用户导入、权限导入等）提供具体实现；

3. 执行方法：

   - [ ] `public void execute(Long taskId)`：
     - 加载 `ExcelTask`（校验 tenant）；
     - 将状态从 `PENDING` 置为 `RUNNING`，记录开始时间；
     - 分批读取 Excel 行（可配置 chunk size，如 500 行一批）；
     - 每行调用对应 `ExcelRowHandler`；
     - 统计成功/失败行数，更新 `successCount/failCount/progress`；
     - 对失败行可记录错误明细（写文件或单独表），并更新 `errorSummary`（只存摘要）；
     - 全部处理完后 `markSuccess` 或 `markFailed`，并记录 Metrics 与审计；

4. **TDD 占位**：

   - [ ] `ExcelImportExecutorTest.shouldUpdateTaskStatusAndCounts()`；
   - [ ] `ExcelImportExecutorTest.shouldInvokeRowHandlerForEachRow()`；
   - [ ] `ExcelImportExecutorTest.shouldHandleExceptionsAndMarkFailed()`。

5. 与任务调度：
   - 通过 PowerJob 或内部线程池调度 `execute(taskId)`，在任务调度章节细化；
   - 建议通过 Outbox/事件通知前端导入完成（配合第 5 章）。

---

### 9.3.1 导出参数与模板 `ExcelExportTemplate`

**类：** `io.github.faustofanb.admin.excel.exports.ExcelExportTemplate`

**TODO**：

1. 字段：

   - [ ] `ExcelTaskType taskType`；
   - [ ] `List<ExcelExportColumn> columns`；
   - [ ] `String fileNamePattern`（例如 `"user-export-{yyyyMMddHHmmss}.xlsx"`）；

2. 嵌套类 `ExcelExportColumn`：

   - [ ] `String headerName`、`String fieldName`、`String dictCode`、`Function<Object, String> formatter`（实现时可用策略封装）；

3. **TDD 占位**：
   - [ ] `ExcelExportTemplateTest.shouldSetupColumnsAndFileNamePattern()`。

---

### 9.3.2 导出任务创建 `ExcelExportApplicationService`

**类：** `io.github.faustofanb.admin.excel.exports.ExcelExportApplicationService`

**TODO（方法级）**：

1. 依赖：

   - [ ] `ExcelTaskRepository`；
   - [ ] `AuditLogService`；

2. DTO：

   - `CreateExportTaskRequest`：`taskType`、`params`（筛选条件）、`fileNameHint` 等；
   - `CreateExportTaskResponse`：`taskId`；

3. 方法：

   - [ ] `public CreateExportTaskResponse createExportTask(CreateExportTaskRequest request)`：
     - 校验 `taskType` 与导出模板可用性；
     - 创建 `ExcelTask`（type=EXPORT，status=PENDING，记录 params），持久化；
     - 记录审计日志；

4. **TDD 占位**：
   - [ ] `ExcelExportApplicationServiceTest.shouldCreatePendingExportTask()`。

---

### 9.3.3 导出执行器 `ExcelExportExecutor`

**类：** `io.github.faustofanb.admin.excel.exports.ExcelExportExecutor`

**TODO（方法级）**：

1. 依赖：

   - [ ] `ExcelTaskRepository`；
   - [ ] 领域查询服务（如 `UserQueryService` 等，通过 SPI 接口抽象）；
   - [ ] Excel 写出工具（EasyExcel/POI）；
   - [ ] 文件存储服务 `FileStorageService`；
   - [ ] `MetricsHelper`、`AuditLogService`；

2. SPI 接口 `ExcelExportQuery`

   - 包：`io.github.faustofanb.admin.excel.exports.spi`；
   - [ ] 方法：`Slice<Map<String, Object>> querySlice(ExcelTask task, ExcelExportContext context, Pageable pageable)`；

3. 执行方法：

   - [ ] `public void execute(Long taskId)`：
     - 加载并锁定 `ExcelTask`；
     - 标记为 `RUNNING`；
     - 使用分页/游标方式拉取数据（防止一次性加载全部）；
     - 边查询边写入 Excel 流（避免占用太多内存）；
     - 完成后将文件上传至对象存储，获取 URL；
     - 调用 `task.markSuccess(fileUrl)`，持久化；
     - 记录 Metrics 与审计日志；

4. **TDD 占位**：
   - [ ] `ExcelExportExecutorTest.shouldStreamDataIntoExcelAndUpdateTask()`；
   - [ ] `ExcelExportExecutorTest.shouldHandleQueryErrorsAndMarkFailed()`。

---

## 9.4 任务查询与取消接口

### 9.4.1 任务查询 `ExcelTaskQueryService`

**类：** `io.github.faustofanb.admin.excel.app.ExcelTaskQueryService`

**TODO（方法级）**：

1. 依赖：

   - [ ] `ExcelTaskRepository`；

2. 方法：

   - [ ] `public Page<ExcelTaskSummaryDto> pageMyTasks(PageRequest pageRequest)`：
     - 根据当前租户与用户查询其 Excel 任务；
   - [ ] `public Optional<ExcelTaskDetailDto> findById(Long taskId)`；

3. DTO 字段：

   - Summary：`id`、`type`、`status`、`progress`、`createdAt`、`finishedAt`；
   - Detail：Summary + `successCount`、`failCount`、`errorSummary`、`resultFileUrl`；

4. **TDD 占位**：
   - [ ] `ExcelTaskQueryServiceTest.shouldPageTasksByCurrentUser()`；
   - [ ] `ExcelTaskQueryServiceTest.shouldReturnDetailWithCountsAndUrl()`。

---

### 9.4.2 任务取消 `ExcelTaskCommandService`

**类：** `io.github.faustofanb.admin.excel.app.ExcelTaskCommandService`

**TODO（方法级）**：

1. 方法：

   - [ ] `public void cancelTask(Long taskId)`：
     - 校验任务属于当前租户与用户；
     - 若任务处于 `PENDING` 或 `RUNNING` 状态，则调用 `task.markCancelled(reason)`；
     - 对于正在执行中的任务，需要与调度器配合（例如通过共享状态或中断标记）；

2. **TDD 占位**：
   - [ ] `ExcelTaskCommandServiceTest.shouldCancelPendingTask()`；
   - [ ] `ExcelTaskCommandServiceTest.shouldRejectCancelWhenTerminal()`。

---

## 9.5 与多租户、安全、缓存、可观测性的协同

**TODO（文档约定）**：

1. 多租户：

   - 所有 ExcelTask 必须带 `tenantId`，查询与执行时均基于租户隔离；

2. 安全：

   - 只允许具备相应权限的用户创建/查看/取消任务；
   - 导出的文件 URL 需具备适当的访问控制或短期有效期；

3. 缓存：

   - 任务进度可缓存到 Redis（键使用 `RedisKeyPrefixes.tenantExcelImport` 或类似前缀），加速前端轮询；

4. 可观测性：
   - 使用 `MetricsHelper` 统计每种任务类型的提交数、成功/失败数、平均耗时；
   - 使用审计日志记录导入/导出的发起与结果（谁、何时、什么类型、多少数据）。

---

## 9.6 进度推送与前端实时显示（WebSocket / SSE）

> 在 9.5 中约定了进度缓存（Redis），本节补充**实时推送**，
> 使前端可以通过 WebSocket 或 SSE 实时更新进度条。

### 9.6.1 进度 DTO 与通知抽象

**DTO：** `ExcelTaskProgressDto`

- 包：`io.github.faustofanb.admin.excel.progress.ExcelTaskProgressDto`

**TODO**：

1. 字段：

   - [ ] `Long taskId`；
   - [ ] `ExcelTaskStatus status`；
   - [ ] `int progress`；
   - [ ] `int successCount`；
   - [ ] `int failCount`；
   - [ ] `String errorSummary`；

2. 静态工厂：

   - [ ] `public static ExcelTaskProgressDto fromTask(ExcelTask task)`：
     - 从 `ExcelTask` 聚合复制各字段；

3. **TDD 占位**：

   - [ ] `ExcelTaskProgressDtoTest.shouldMapFromTask()`。

**接口：** `ExcelProgressNotifier`

- 包：`io.github.faustofanb.admin.excel.progress.ExcelProgressNotifier`

**TODO（方法级）**：

1. 方法：

   - [ ] `void notifyProgress(Long tenantId, Long userId, ExcelTaskProgressDto progress)`：
     - 供执行器在任务状态/进度变更时调用；

2. 行为约定：

   - 不保证投递成功，只是“尽力而为”的推送；
   - 始终以 DB/Redis 中的进度数据为权威源。

---

### 9.6.2 SSE 实现占位 `SseExcelProgressNotifier`

**类：** `io.github.faustofanb.admin.excel.progress.SseExcelProgressNotifier`

**TODO（方法级与协作）**：

1. 依赖：

   - [ ] `SseEmitterRegistry`（自定义，用于管理每个用户/任务的 SSE 连接）；
   - [ ] 日志工具；

2. 行为：

   - 在 `notifyProgress` 中，根据 `tenantId`、`userId`、`taskId` 查找对应的 `SseEmitter` 列表；
   - 将 `ExcelTaskProgressDto` 序列化为 JSON，通过 `SseEmitter.send` 推送；
   - 处理发送异常（如连接断开），从注册表中移除失效 emitter；

3. **TDD 占位**：

   - [ ] `SseExcelProgressNotifierTest.shouldSendProgressToRegisteredEmitters()`；
   - [ ] `SseExcelProgressNotifierTest.shouldRemoveEmitterOnSendFailure()`。

**SSE 注册表：** `SseEmitterRegistry`

- 包：`io.github.faustofanb.admin.excel.progress`

**TODO**：

1. 方法：

   - [ ] `void register(Long tenantId, Long userId, Long taskId, SseEmitter emitter)`；
   - [ ] `List<SseEmitter> getEmitters(Long tenantId, Long userId, Long taskId)`；
   - [ ] `void remove(SseEmitter emitter)`；

2. **TDD 占位**：

   - [ ] `SseEmitterRegistryTest.shouldRegisterAndLookupEmitters()`。

**SSE 控制器：** `ExcelProgressSseController`

- 包：`io.github.faustofanb.admin.excel.progress`

**TODO（方法级）**：

1. 端点：

   - [ ] `@GetMapping("/api/excel/tasks/{taskId}/progress/sse")`：
     - 校验当前用户对该任务有访问权限（租户 + 用户匹配）；
     - 创建 `SseEmitter`，注册到 `SseEmitterRegistry`；
     - 可立即发送当前进度快照（从 Redis 或 `ExcelTaskQueryService` 获取）；
     - 返回 `SseEmitter` 给前端维持长连接；

2. **TDD 占位**：

   - [ ] `ExcelProgressSseControllerTest.shouldRegisterEmitterAndSendInitialProgress()`。

---

### 9.6.3 WebSocket 实现占位 `WebSocketExcelProgressNotifier`

> 若项目使用 STOMP/WebSocket，可提供平行实现，前端通过订阅 topic 收到进度。

**类：** `io.github.faustofanb.admin.excel.progress.WebSocketExcelProgressNotifier`

**TODO**：

1. 依赖：

   - [ ] `SimpMessagingTemplate`（Spring WebSocket）；

2. 行为：

   - 在 `notifyProgress` 中构造 destination，例如：
     - `/topic/excel/{tenantId}/{userId}/{taskId}`；
   - 使用 `messagingTemplate.convertAndSend(destination, progressDto)` 推送；

3. **TDD 占位**：

   - [ ] `WebSocketExcelProgressNotifierTest.shouldSendProgressToDestination()`。

---

### 9.6.4 执行器中的调用约定

> 衔接 9.2.3 与 9.3.3：在任务执行过程中适时推送进度。

**TODO（扩展已有执行器逻辑）**：

1. 在 `ExcelImportExecutor.execute` 与 `ExcelExportExecutor.execute` 中：

   - 在以下时机调用 `excelProgressNotifier.notifyProgress(...)`：
     - 任务从 `PENDING` → `RUNNING`；
     - 每处理完一批记录（如每 500 行）更新一次进度；
     - 任务结束：`SUCCESS`、`FAILED` 或 `CANCELLED`；

2. 与缓存的一致性：

   - 在更新 `ExcelTask` 与 Redis 进度后，再调用 `ExcelProgressNotifier`；
   - 前端若发现 SSE/WebSocket 中断，可回退到轮询 `GET /api/excel/tasks/{taskId}` 或进度缓存接口；

3. **TDD 占位**：

   - [ ] 在原有 `ExcelImportExecutorTest` / `ExcelExportExecutorTest` 中增加：
     - `shouldNotifyProgressOnStatusChange()`；

---

## 9.7 与任务调度和 Outbox 的集成

**TODO（文档约定）**：

1. 调度：

   - Excel 导入/导出执行器应由任务调度模块（如 PowerJob）触发；
   - 建议为每个任务类型定义独立的任务处理器，以便扩展与隔离失败；

2. Outbox：
   - 当任务状态从 RUNNING → SUCCESS/FAILED 时，可向 Outbox 写入事件（如 `ExcelTaskCompletedEvent`），
     由事件总线通知前端或其他服务（对应第 5 章 Outbox 设计）。

---

> 本章完成后，系统将具备：
>
> - 通用的 Excel 导入/导出任务建模与生命周期管理；
> - 统一的导入/导出模板与 SPI 扩展点，便于各业务模块挂载自己的 Excel 能力；
> - 可靠的批处理执行器，与多租户、安全、缓存和可观测性紧密集成。
