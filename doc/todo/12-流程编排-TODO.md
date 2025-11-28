# 第 12 章 流程编排 TODO 清单（AI Agent 实施指引）

> 本章对应 `doc/design/12.流程编排.md`，聚焦：
>
> - 跨模块/跨服务的业务流程编排（如租户开通、用户初始化、批量授权等）；
> - 编排层与领域服务、RPC、事件总线、任务调度的协同；
> - 补偿/回滚策略与幂等设计；
> - 与多租户、安全、可观测性的整体约束。
>
> 要求：
>
> - 严格区分“编排层”和“领域应用服务”，编排层只负责调用顺序与策略，不直接写领域规则；
> - 流程必须是可观测且可恢复的：有状态、有日志、有重试与补偿能力；
> - 优先使用本地事务 + Outbox + 事件驱动，必要时才引入分布式事务框架。

---

## 12.1 编排层总体设计

**TODO（文档约定）**：

1. 分层位置：

   - 编排组件放在 `io.github.faustofanb.admin.orchestration.*` 包下；
   - 依赖于：应用服务、`*RpcClient`、事件发布（`DomainEventPublisher` / Outbox）、任务调度入口；
   - 不依赖于 Web 层、Dubbo 细节或持久化细节。

2. 使用场景示例：

   - 租户开通：创建租户 → 初始化管理员用户 → 初始化权限/菜单 → 发送欢迎通知；
   - 批量授权：为一批用户分配角色/权限，并在失败时有补偿策略；
   - 跨模块操作：资源创建 + 权限绑定 + 缓存预热。

3. 流程编排风格：
   - 轻量级编排：使用编排服务类手写步骤、清晰直观；
   - 如有必要可集成第三方编排引擎（如 LiteFlow），但本章以代码层编排为主。

---

## 12.2 流程定义与上下文模型

### 12.2.1 流程上下文 `FlowContext`

**类：** `io.github.faustofanb.admin.orchestration.core.FlowContext`

**TODO**：

1. 字段：

   - [ ] `Long tenantId`；
   - [ ] `Long operatorId`（操作人用户 ID）；
   - [ ] `String traceId`；
   - [ ] `Map<String, Object> attributes`（保存中间结果，按需使用）；

2. 方法：

   - [ ] 便捷 put/get：`<T> T get(String key, Class<T> type)`、`void put(String key, Object value)`；

3. **TDD 占位**：
   - [ ] `FlowContextTest.shouldStoreAndRetrieveAttributes()`。

---

### 12.2.2 步骤接口 `FlowStep`

**接口：** `io.github.faustofanb.admin.orchestration.core.FlowStep`

**TODO（方法级）**：

1. 方法：

   - [ ] `String getName()`；
   - [ ] `void execute(FlowContext context)`；

2. 语义：

   - 每个步骤完成一件相对独立的业务操作（创建租户、创建用户、初始化菜单等）；

3. **TDD 占位**：
   - [ ] `FlowStepTest.shouldExposeNameAndExecute()`（可通过简单实现测试）。

---

### 12.2.3 编排模板基类 `AbstractFlowOrchestrator`

**类：** `io.github.faustofanb.admin.orchestration.core.AbstractFlowOrchestrator`

**TODO**：

1. 字段：

   - [ ] `List<FlowStep> steps`；

2. 模板方法：

   - [ ] `public final void execute(FlowContext context)`：
     - 遍历步骤顺序执行；
     - 对每个步骤记录开始/结束日志与耗时；
     - 捕获异常并进行错误处理（见 12.3 节）；

3. 抽象方法（可选）：

   - [ ] `protected void beforeExecute(FlowContext context)`；
   - [ ] `protected void afterExecute(FlowContext context)`；

4. **TDD 占位**：
   - [ ] `AbstractFlowOrchestratorTest.shouldExecuteStepsInOrder()`；
   - [ ] `AbstractFlowOrchestratorTest.shouldStopOnException()`。

---

## 12.3 错误处理与补偿策略

### 12.3.1 补偿步骤接口 `CompensableFlowStep`

**接口：** `io.github.faustofanb.admin.orchestration.core.CompensableFlowStep`

> 适用于需要在部分失败时进行补偿（如撤销已创建资源）的步骤。

**TODO**：

1. 继承 `FlowStep` 并新增：

   - [ ] `void compensate(FlowContext context)`；

2. 补偿语义：

   - 仅在部分流程失败且需要回滚时调用；

3. **TDD 占位**：
   - [ ] `CompensableFlowStepTest.shouldInvokeCompensateOnFailure()`（结合 Orchestrator 行为）。

---

### 12.3.2 支持补偿的编排器 `CompensatingFlowOrchestrator`

**类：** `io.github.faustofanb.admin.orchestration.core.CompensatingFlowOrchestrator`

**TODO（方法级）**：

1. 继承 `AbstractFlowOrchestrator`，扩展：

   - 在执行步骤序列时记录已成功执行的 `CompensableFlowStep` 列表；
   - 遇到异常时按逆序调用这些步骤的 `compensate` 方法；

2. 日志与指标：

   - 记录补偿开始/结束、每个补偿步骤结果；

3. **TDD 占位**：
   - [ ] `CompensatingFlowOrchestratorTest.shouldCompensateInReverseOrderOnFailure()`。

---

## 12.4 典型流程编排 TODO

### 12.4.1 租户开通流程 `TenantProvisionFlow`

**类：** `io.github.faustofanb.admin.orchestration.tenant.TenantProvisionFlow`

**TODO（方法级）**：

1. 使用 `CompensatingFlowOrchestrator`，包含步骤示例：

   - `CreateTenantStep`：创建租户记录；
   - `CreateAdminUserStep`：创建管理员用户；
   - `InitPermissionStep`：初始化默认角色/权限/菜单；
   - `NotifyTenantCreatedStep`：通过事件总线/通知服务告知租户创建成功；

2. 各步骤调用的依赖：

   - 租户应用服务、用户应用服务、权限应用服务、事件发布服务等；

3. 补偿示例：

   - 若在 `InitPermissionStep` 失败，可尝试删除已创建的用户与租户（根据业务决策）；

4. **TDD 占位**：
   - [ ] `TenantProvisionFlowTest.shouldExecuteAllStepsOnSuccess()`；
   - [ ] `TenantProvisionFlowTest.shouldCompensateWhenMiddleStepFail()`。

---

### 12.4.2 批量授权流程 `BatchGrantRoleFlow`

**类：** `io.github.faustofanb.admin.orchestration.auth.BatchGrantRoleFlow`

**TODO**：

1. 流程示例：

   - 校验请求与权限；
   - 为一批用户批量绑定角色；
   - 刷新缓存（权限/菜单）或发布权限变更事件；

2. 注意事项：

   - 批量操作要分片处理，避免单次事务过大；
   - 对已部分成功的用户，可记录结果并在前端提示；一般不做全局回滚；

3. **TDD 占位**：
   - [ ] `BatchGrantRoleFlowTest.shouldHandlePartialSuccess()`。

---

## 12.5 与 RPC、事件总线、任务调度的协同

**TODO（文档约定）**：

1. 调用模式：

   - 编排层通过 `*RpcClient` 调用远程服务，避免直接依赖 Dubbo 注解；
   - 对远程调用结果进行统一的错误分类与重试控制；

2. 事件驱动：

   - 在流程关键节点发布集成事件（如租户开通完成、批量授权完成），使用 Outbox 模式保证一致性；

3. 调度集成：

   - 某些长耗时流程可拆分为多个子任务，由任务调度模块（第 8 章）按阶段触发编排；

4. Excel/批处理：
   - 流程可与 Excel 导入任务结合，例如：
     - 从 Excel 导入用户 → 编排层执行用户创建与授权 → 通过事件/进度接口更新前端状态。

---

## 12.6 多租户、安全与可观测性

**TODO（文档约定）**：

1. 多租户：

   - 编排入口必须携带 `tenantId`，在 `FlowContext` 与 `AppContext` 中设置；
   - 所有步骤实现均不得跨租户操作数据；

2. 安全：

   - 使用 `operatorId` 记录操作人，写入审计日志；
   - 对外暴露的编排入口（如租户开通 API）必须有严格权限控制；

3. 可观测性：
   - 为每个流程编排创建 Tracing span（使用 `TraceHelper`），包含流程名称与关键标签；
   - 使用 Metrics 统计流程执行次数、成功/失败率、平均耗时；
   - 日志中记录每个步骤的开始/结束、输入关键参数与结果。

---

> 本章完成后，系统将具备：
>
> - 明确的流程编排层与领域服务分工；
> - 可扩展的流程与步骤模型（含补偿能力）；
> - 与 RPC、事件总线、任务调度、Excel 批处理等模块协同的跨模块编排能力；
> - 满足多租户、安全与可观测性要求的流程执行基础。
