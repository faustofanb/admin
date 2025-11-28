# 第 7 章 日志与可观测性 TODO 清单（AI Agent 实施指引）

> 本章聚焦：**结构化日志、指标（Metrics）、分布式追踪（Tracing）、审计日志**，
> 目标是让系统在高并发与多租户场景下具有良好的可观测性与问题排查能力。
>
> 要求：
>
> - 与 `doc/design/7.日志与可观测性详细设计.md` 完整对齐；
> - 日志必须是结构化、可查询（适配 Loki/Grafana）；
> - 指标基于 Micrometer，兼容 Prometheus；
> - Tracing 兼容 OpenTelemetry，支持跨服务 TraceId 透传；
> - 为关键业务提供审计日志能力。

---

## 7.1 日志基础设施与 MDC 约定

> 部分基础内容在第 1 章/`backend-infra` 中已有占位，这里补充完整 TODO 与约定。

### 7.1.1 日志上下文键约定 `LogFields`

**类：** `io.github.faustofanb.admin.logging.LogFields`

**TODO**：

1. 定义常用 MDC/日志上下文字段常量：

   - [ ] `String TRACE_ID = "traceId"`；
   - [ ] `String SPAN_ID = "spanId"`；
   - [ ] `String TENANT_ID = "tenantId"`；
   - [ ] `String USER_ID = "userId"`；
   - [ ] `String REQUEST_ID = "requestId"`；
   - [ ] `String CLIENT_ID = "clientId"`；
   - [ ] `String REMOTE_IP = "remoteIp"`；
   - [ ] `String METHOD = "method"`；
   - [ ] `String URI = "uri"`；

2. **TDD**：

   - [ ] `LogFieldsTest.shouldContainExpectedFieldNames()`（简单常量断言即可）。

---

### 7.1.2 MDC 工具类 `LoggingContext`

> 在第 1 章中已有初步 TODO，这里补充关键行为。

**类：** `io.github.faustofanb.admin.logging.LoggingContext`

**TODO（方法级）**：

1. 上下文填充方法：

   - [ ] `public static void putTraceAndSpan(String traceId, String spanId)`；
   - [ ] `public static void putTenantAndUser(Long tenantId, Long userId)`；
   - [ ] `public static void putRequest(String requestId, String method, String uri, String remoteIp)`；

2. 清理方法：

   - [ ] `public static void clear()`：清除所有 MDC 中与本项目相关的键；

3. 线程切换支持：

   - [ ] `public static Map<String, String> capture()`：抓取当前 MDC 映射；
   - [ ] `public static void restore(Map<String, String> context)`：在新线程中恢复；

4. **TDD**：

   - [ ] `LoggingContextTest.shouldPutAndClearContext()`；
   - [ ] `LoggingContextTest.shouldCaptureAndRestoreMdc()`。

---

### 7.1.3 结构化日志配置与格式

> 不要求在 TODO 中写具体 logback XML，但要约定格式与关键字段。

**TODO（文档级约定）**：

1. 日志输出格式：

   - 使用 JSON Layout（例如 logback-json 或 spring-boot 内置 JSON 支持）；
   - 必须包含字段：timestamp、level、logger、message、`traceId`、`tenantId`、`userId`、`requestId`、`thread`；
   - 预留 `tags` 或 `labels` 字段便于 Loki 查询。

2. 日志级别策略：

   - INFO：正常业务流程关键节点（如订单创建成功、任务完成）；
   - WARN：可疑但未失败的情况（如重试、限流触发）；
   - ERROR：真正的失败或系统异常；
   - DEBUG：开发/排障用，生产慎用（可按 logger 单独打开）。

3. 对接 Loki：

   - 在 infra 模块中配置 Promtail 抓取 JSON 日志并推送到 Loki（配置细节可在运维文档中给出，此处略）。

---

## 7.2 HTTP 请求日志与审计

### 7.2.1 Web 日志过滤器 `HttpLoggingFilter`

**类：** `io.github.faustofanb.admin.logging.web.HttpLoggingFilter`

**TODO（方法级）**：

1. 功能：

   - 为每个 HTTP 请求生成 `requestId`（可复用 `RequestIdGenerator`）；
   - 从请求头中提取 `traceId`/`spanId`（如果已存在）或创建新的；
   - 提取 `tenantId`、`userId` 等（若 SecurityContext/AppContext 已就绪）；
   - 填充 MDC，并在日志中记录请求方法、URI、状态码、耗时。

2. 核心方法：

   - [ ] `doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)`：
     - 记录开始时间；
     - 调用 `LoggingContext.putTraceAndSpan`、`putTenantAndUser`、`putRequest`；
     - 执行 `chain.doFilter`；
     - 在 finally 中清理 MDC，并输出结构化访问日志记录。

3. **TDD 占位**：

   - [ ] `HttpLoggingFilterTest.shouldLogRequestAndResponse()`（使用 MockMvc 或 MockHttpServletRequest/Response）。

---

### 7.2.2 审计日志服务 `AuditLogService`

**类：** `io.github.faustofanb.admin.logging.audit.AuditLogService`

**TODO（方法级）**：

1. 审计事件模型：

   - **类：** `AuditEvent`（可在同包内定义）：
     - 字段：`id`、`tenantId`、`userId`、`action`、`resourceType`、`resourceId`、`detail`、`ip`、`userAgent`、`createdAt`；

2. 服务方法：

   - [ ] `public void record(AuditEvent event)`：
     - 将事件写入数据库或发送到审计专用日志队列（具体实现待定，可先写 DB 表 t_audit_log）；
   - [ ] 便捷方法：`public void record(String action, String resourceType, String resourceId, String detail)`：
     - 从 AppContext/MDC 中自动补全 tenantId/userId/ip 等字段。

3. **TDD 占位**：

   - [ ] `AuditLogServiceTest.shouldFillContextFieldsFromAppContext()`；
   - [ ] `AuditLogServiceTest.shouldPersistAuditEvent()`（结合 Repository 测试）。

4. 业务使用约定：

   - 在关键操作（登录、权限修改、数据导出、删除重要资源等）调用 `AuditLogService.record(...)`。

---

## 7.3 Metrics 指标体系

> 使用 Micrometer 曝露指标，Prometheus 负责抓取。

### 7.3.1 指标命名与标签规范

**TODO（文档约定）**：

1. 命名规则：

   - 统一前缀：`admin_`；
   - 计数器：`admin_<domain>_<action>_total`；
   - Gauge：`admin_<domain>_<resource>_gauge`；
   - Timer：`admin_<domain>_<operation>_seconds`。

2. 常用标签：

   - `tenantId`（必要时，为控制基数可限制为 "default" 或分组）；
   - `service`（如 `admin-backend`）；
   - `endpoint` 或 `operation`；
   - `result`（success/fail）。

---

### 7.3.2 公共指标帮助类 `MetricsHelper`

**类：** `io.github.faustofanb.admin.metrics.MetricsHelper`

**TODO（方法级）**：

1. 依赖：

   - [ ] `MeterRegistry`（Micrometer）；

2. 方法：

   - [ ] `public Counter counter(String name, Tags tags)`；
   - [ ] `public Timer timer(String name, Tags tags)`；
   - [ ] `public void recordSuccess(String domain, String operation)`：内部构造 name/tags 并递增计数器；
   - [ ] `public void recordFailure(String domain, String operation, Throwable error)`：记录失败计数与异常类型标签；

3. **TDD**：

   - [ ] `MetricsHelperTest.shouldCreateAndIncrementCounter()`；
   - [ ] `MetricsHelperTest.shouldRecordTimer()`。

---

### 7.3.3 应用级关键指标

**TODO（文档级场景）**：

1. 认证与授权：

   - 登录成功/失败次数：`admin_auth_login_total{result=success|fail}`；
   - Token 刷新次数；

2. 任务与批处理：

   - Excel 导入任务成功/失败次数与耗时；
   - PowerJob 任务执行结果与重试次数；

3. 缓存与限流：

   - 缓存命中/未命中率（可通过命中/miss 计数器间接观察）；
   - 限流拒绝次数（与第 6 章对齐，如 `admin_rate_limit_rejected_total`）。

---

## 7.4 分布式追踪（Tracing）

> 使用 OpenTelemetry，将 TraceId/SpanId 与日志、指标关联起来。

### 7.4.1 Tracing 配置类 `TracingConfig`

> 在第 1 章中已有占位，补充关键 TODO。

**类：** `io.github.faustofanb.admin.tracing.TracingConfig`

**TODO**：

1. 创建 OpenTelemetry SDK（或使用 Spring Boot OTel 自动配置）：

   - 定义 `OpenTelemetry` Bean、`Tracer` Bean；
   - 配置 OTLP 导出器指向 APM（如 Tempo/Jaeger/Zipkin）。

2. 与日志集成：

   - 配置将 TraceId、SpanId 注入 MDC（可通过 `Slf4J`/OTel bridge）。

3. **TDD 占位**：

   - [ ] `TracingConfigTest.shouldExposeTracerBean()`（简单的 Spring Context 测试）。

---

### 7.4.2 应用追踪辅助 `TraceHelper`

**类：** `io.github.faustofanb.admin.tracing.TraceHelper`

**TODO（方法级）**：

1. 依赖：

   - [ ] `Tracer tracer`；

2. 方法：

   - [ ] `public <T> T inSpan(String spanName, Supplier<T> supplier)`：
     - 创建 span，执行 supplier，并在 finally 中结束 span；
   - [ ] `public void inSpan(String spanName, Runnable runnable)`；

3. **TDD**：

   - [ ] `TraceHelperTest.shouldStartAndEndSpan()`（可通过 MockTracer 或计数方式验证）。

4. 业务约定：

   - 针对关键操作（如大型导入、复杂查询），可在应用服务层包上一层 span，便于追踪性能瓶颈。

---

## 7.5 与多租户/安全/性能的协同约定

**TODO（文档约定）**：

1. 多租户：

   - 所有日志与指标均应尽可能带上 `tenantId` 标签或字段；
   - 对高基数问题进行评估，必要时做分组或采样。

2. 安全：

   - 日志中严禁输出敏感信息（密码、完整 Token、个人隐私数据等）；
   - Token 等安全字段仅可输出前后若干字符用于排查，或使用 hash 形式。

3. 性能：

   - 避免在高频路径中构建过于复杂的日志消息；
   - 对超高频操作（如内层循环）禁止打印日志，改用指标统计。

---

## 7.6 测试与演练

**TODO（文档与实践）**：

1. Chaos/演练：

   - 模拟部分下游异常（DB、MQ、Redis），验证日志与指标是否足以帮助定位问题；
   - 模拟高并发压测时，检查 Loki/Grafana 仪表盘可否看到瓶颈与错误热点。

2. 日志采样策略：

   - 为某些高频操作配置采样（仅记录 ERROR 或部分 INFO 日志）；
   - 配合 tracing 采样率设置。

3. 定期复盘：

   - 在生产事故或演练后，评估日志/指标/Tracing 是否足够，若不足则在本章 TODO 中补充相应指标或日志点。

---

> 本章完成后，系统将具备：
>
> - 统一的结构化日志与 MDC 上下文管理；
> - 稳定的 Metrics 体系支撑性能与错误监控；
> - OpenTelemetry 驱动的分布式追踪能力；
> - 对关键操作的审计日志记录，为安全与合规提供支撑。
