# 第 10 章 RPC 与服务调用 TODO 清单（AI Agent 实施指引）

> 本章对应 `doc/design/10.RPC与服务调用.md`，聚焦：
>
> - 基于 Dubbo 的服务暴露与调用规范；
> - 统一的 RPC 接口风格与 DTO 约定；
> - 多租户与安全上下文的透传（tenantId/userId/traceId 等）；
> - 调用超时、重试、幂等与降级策略；
> - 与事件总线、Outbox、流程编排的协同。
>
> 要求：
>
> - 严格区分“内部 RPC 接口”和“外部 HTTP API”，RPC 接口仅供内部服务间调用；
> - RPC DTO 不直接暴露领域对象，使用专门的接口层模型；
> - 所有 RPC 调用必须有超时、重试和日志/指标埋点。

---

## 10.1 RPC 接口分层与命名约定

**TODO（文档约定）**：

1. 分层：

   - 接口定义层：`io.github.faustofanb.admin.rpc.api.*`（纯接口 + DTO）；
   - 实现层：`io.github.faustofanb.admin.rpc.provider.*`（适配应用服务，`@DubboService`）；
   - 客户端层：`io.github.faustofanb.admin.rpc.client.*`（`@DubboReference` 封装，供本地应用使用）。

2. 命名：

   - 接口名以 `*RpcService` 结尾，如 `UserQueryRpcService`；
   - DTO 名以 `*RpcRequest` / `*RpcResponse` / `*RpcDto` 结尾；

3. 多租户与安全：
   - 每次 RPC 调用必须透传 `tenantId`、`userId`、`traceId` 等上下文，默认通过 Dubbo Attachment 机制实现；
   - 服务端落地时将这些上下文注入 `AppContext` 与 `LoggingContext`。

---

## 10.2 上下文透传与拦截器

### 10.2.1 RPC 上下文键约定 `RpcContextKeys`

**类：** `io.github.faustofanb.admin.rpc.context.RpcContextKeys`

**TODO**：

1. 常量：

   - [ ] `String TENANT_ID = "x-tenant-id"`；
   - [ ] `String USER_ID = "x-user-id"`；
   - [ ] `String TRACE_ID = "x-trace-id"`；
   - [ ] `String CLIENT_ID = "x-client-id"`；

2. **TDD**：
   - [ ] `RpcContextKeysTest.shouldContainExpectedKeys()`。

---

### 10.2.2 客户端过滤器 `RpcClientContextFilter`

**类：** `io.github.faustofanb.admin.rpc.context.RpcClientContextFilter`

> 在发起 RPC 调用前，将当前线程的 `AppContext` / MDC 信息写入 Dubbo 的 RpcContext Attachments。

**TODO（方法级）**：

1. 依赖：

   - [ ] `AppContextHolder`；

2. 关键逻辑：

   - 在 `invoke` 之前：
     - 从 `AppContextHolder` 读取 `tenantId`、`userId`、`clientId`；
     - 从 MDC 或 `LoggingContext` 读取 `traceId`；
     - 调用 Dubbo 提供的 API（如 `RpcContext.getContext().setAttachment(...)`）写入；

3. **TDD 占位**：
   - [ ] `RpcClientContextFilterTest.shouldWriteContextToAttachments()`（可用 Dubbo MockContext 或自定义封装测试）。

---

### 10.2.3 服务端过滤器 `RpcServerContextFilter`

**类：** `io.github.faustofanb.admin.rpc.context.RpcServerContextFilter`

> 在接收 RPC 请求时，从 Attachments 中恢复上下文并注入 `AppContext` 与 MDC。

**TODO（方法级）**：

1. 依赖：

   - [ ] `AppContextHolder`；
   - [ ] `LoggingContext`；

2. 关键逻辑：

   - 从 RpcContext Attachments 中读取 `tenantId/userId/traceId/clientId`；
   - 构造或更新 `AppContext`，并存入 `AppContextHolder`；
   - 调用 `LoggingContext.putTenantAndUser`、`putTraceAndSpan` 等方法；
   - 在调用完成后清理上下文；

3. **TDD 占位**：
   - [ ] `RpcServerContextFilterTest.shouldRestoreContextFromAttachments()`；
   - [ ] `RpcServerContextFilterTest.shouldClearContextAfterInvocation()`。

---

## 10.3 统一 RPC DTO 与错误处理

### 10.3.1 通用响应包装 `RpcResponse<T>`

> 与 HTTP API 的 `ApiResponse<T>` 类似，但用于内部 RPC 场景，可适当简化字段。

**类：** `io.github.faustofanb.admin.rpc.dto.RpcResponse<T>`

**TODO**：

1. 字段：

   - [ ] `boolean success`；
   - [ ] `String code`；
   - [ ] `String message`；
   - [ ] `T data`；

2. 静态工厂：

   - [ ] `RpcResponse<T> ok(T data)`；
   - [ ] `RpcResponse<T> fail(String code, String message)`；

3. 与 `ErrorCode` 对齐：

   - RPC 错误码优先复用 `ErrorCode` 体系；

4. **TDD 占位**：
   - [ ] `RpcResponseTest.shouldCreateSuccessAndFailureResponses()`。

---

### 10.3.2 错误转换 `RpcExceptionTranslator`

**类：** `io.github.faustofanb.admin.rpc.error.RpcExceptionTranslator`

**TODO（方法级）**：

1. 方法：

   - [ ] `public <T> RpcResponse<T> toRpcResponse(Throwable ex)`：
     - 将 `BusinessException`、`ValidationException`、`SystemException` 转换为统一的 `RpcResponse`；

2. **TDD 占位**：
   - [ ] `RpcExceptionTranslatorTest.shouldTranslateBusinessException()`；
   - [ ] `RpcExceptionTranslatorTest.shouldTranslateSystemException()`。

---

## 10.4 Dubbo RPC 接口示例 TODO

### 10.4.1 用户查询 RPC 接口 `UserQueryRpcService`

**接口：** `io.github.faustofanb.admin.rpc.api.UserQueryRpcService`

**TODO（方法级）**：

1. DTO：

   - `UserSummaryRpcDto`：`id`、`username`、`displayName`、`status`、`tenantId`；
   - `UserDetailRpcDto`：在 Summary 基础上增加邮箱、手机号等安全可暴露字段；

2. 方法：

   - [ ] `RpcResponse<UserDetailRpcDto> findById(Long userId)`；
   - [ ] `RpcResponse<List<UserSummaryRpcDto>> listByIds(List<Long> userIds)`；

3. 提供者实现：

   - 类：`UserQueryRpcServiceImpl`（包：`io.github.faustofanb.admin.rpc.provider`）；
   - 依赖：`UserQueryService`（本地应用服务）；
   - 在实现中调用应用服务并将领域对象映射为 DTO；

4. **TDD 占位**：
   - [ ] `UserQueryRpcServiceImplTest.shouldReturnUserDetail()`。

---

### 10.4.2 权限/角色 RPC 接口 `PermissionRpcService`

**接口：** `io.github.faustofanb.admin.rpc.api.PermissionRpcService`

**TODO**：

1. DTO：

   - `PermissionRpcDto`：`code`、`name`、`type`（menu/button/api）、`tenantId`；

2. 方法示例：

   - [ ] `RpcResponse<List<PermissionRpcDto>> listPermissionsByUser(Long userId)`；

3. 说明：
   - 供其他服务在做权限决策时查询本系统权限定义/授权信息。

---

## 10.5 调用端封装与超时/重试策略

### 10.5.1 客户端封装 `UserQueryRpcClient`

**类：** `io.github.faustofanb.admin.rpc.client.UserQueryRpcClient`

**TODO（方法级）**：

1. 依赖：

   - [ ] `UserQueryRpcService`（`@DubboReference` 注入）；

2. 方法：

   - [ ] `public Optional<UserDetailRpcDto> getUserDetail(Long userId)`：
     - 调用 `rpcService.findById` 并处理 `RpcResponse`；
     - 记录调用耗时与结果 Metrics；

3. **TDD 占位**：
   - [ ] `UserQueryRpcClientTest.shouldReturnEmptyWhenRpcFail()`。

---

### 10.5.2 Dubbo 配置与策略

**TODO（文档约定）**：

1. 超时：

   - 默认超时时间（如 1s~3s），根据接口重要性可配置；

2. 重试：

   - 对非幂等接口谨慎启用重试；
   - 对读接口可按 Dubbo 规则设置有限次数重试（例如 1~2 次）；

3. 降级：

   - 在客户端封装中对 RPC 失败提供合理的降级策略（如返回空集合、使用缓存数据等）。

4. Metrics 与日志：
   - 使用 `MetricsHelper` 记录每个 RPC 接口的调用次数、失败次数、平均耗时；
   - 日志中打印关键信息（接口名、调用方、traceId、错误码等）。

---

## 10.6 与事件总线与流程编排的协同

**TODO（文档约定）**：

1. 事件驱动 vs RPC：

   - 对于查询/强一致写操作优先使用 RPC；
   - 对于异步通知或松耦合集成，优先使用 Outbox + 事件总线（见第 5、11 章）。

2. 流程编排：
   - 在复杂业务流程中（见第 12 章），编排层同时调度本地应用服务、RPC 调用与事件发布；
   - TODO 文档中要求编排层不要直接依赖 Dubbo 细节，而是依赖 `*RpcClient` 封装。

---

> 本章完成后，系统将具备：
>
> - 一致的 Dubbo RPC 接口与 DTO 规范；
> - 可靠的多租户与安全上下文透传机制；
> - 具备超时、重试、Metrics 与日志的调用侧封装；
> - 与事件总线和流程编排协同的服务调用基础。
