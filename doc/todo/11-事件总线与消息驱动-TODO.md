# 第 11 章 事件总线与消息驱动 TODO 清单（AI Agent 实施指引）

> 本章对应 `doc/design/11.事件总线与消息驱动.md`，聚焦：
>
> - 基于 Outbox + RocketMQ 的领域事件 / 集成事件发布与订阅；
> - 统一的事件模型、Topic/Tag 规范（结合 `MqTopics`）；
> - 消费端幂等、防重与错误处理；
> - 与多租户、安全、事务、一致性和可观测性的协同。
>
> 要求：
>
> - 事件生产应在本地事务中配合 Outbox 完成（见第 5 章），不直接在业务代码中调用 MQ 客户端；
> - 消费端必须是幂等的，且对错误具备重试和死信处理策略；
> - 所有事件处理逻辑必须可测试（单测 + 组件测试），并有清晰日志与指标。

---

## 11.1 事件模型与 Topic/Tag 规范

### 11.1.1 事件分类与命名

**TODO（文档约定）**：

1. 事件分类：

   - 领域事件（Domain Event）：聚合内部发生的业务事实，如 `UserCreated`, `RoleAssigned`；
   - 集成事件（Integration Event）：跨服务/模块传播的可观测事件，如 `UserCreatedIntegrationEvent`，通常与领域事件一一对应但可裁剪字段；

2. 命名规范：

   - 领域事件类名粒度偏领域，如 `UserLockedEvent`；
   - 集成事件类名加 `IntegrationEvent` 后缀，如 `UserLockedIntegrationEvent`；

3. Topic/Tag 对齐 `MqTopics`：

   - 用户相关事件：`MqTopics.USER_EVENTS` + Tag 如 `"created"`, `"locked"`；
   - 权限/角色事件：`MqTopics.PERMISSION_EVENTS`；
   - 资源/文件事件：`MqTopics.RESOURCE_EVENTS`；
   - Excel/任务事件：`MqTopics.EXCEL_EVENTS`；

4. **TDD 建议**：
   - 为关键事件列出参数化测试，验证 Topic/Tag 命名符合约定。

---

### 11.1.2 集成事件基类 `IntegrationEventBase`

> 在第 5 章中已有 `IntegrationEvent` 抽象，本节为跨模块通用字段提供默认实现。

**类：** `io.github.faustofanb.admin.events.IntegrationEventBase`

**TODO**：

1. 字段：

   - [ ] `String eventId`（全局唯一 ID，如 UUID）；
   - [ ] `Instant occurredAt`；
   - [ ] `String aggregateType`；
   - [ ] `String aggregateId`；

2. 构造与工厂方法：

   - [ ] 构造函数接收聚合信息并生成 `eventId/occurredAt`；

3. 与 `IntegrationEvent` 接口对齐：

   - [ ] 实现 `aggregateType() / aggregateId() / eventType() / occurredAt()` 等方法；

4. **TDD 占位**：
   - [ ] `IntegrationEventBaseTest.shouldPopulateDefaultFields()`。

---

## 11.2 事件发布：从领域事件到 Outbox

### 11.2.1 领域事件发布器 `DomainEventPublisher`

**接口：** `io.github.faustofanb.admin.events.DomainEventPublisher`

**TODO（方法级）**：

1. 方法：

   - [ ] `void publish(Object domainEvent)`；
   - [ ] `<E> void publish(Collection<E> domainEvents)`；

2. 语义：

   - 在应用服务层使用，对领域事件集合进行统一处理；
   - 内部将领域事件转换为集成事件，并通过 `TransactionalOutboxService` 写入 Outbox；

3. **TDD 占位**：
   - [ ] `DomainEventPublisherTest.shouldDelegateToOutboxFactoryAndRepository()`（使用内存假实现）。

---

### 11.2.2 领域 → 集成事件映射器 `DomainToIntegrationEventMapper`

**接口：** `io.github.faustofanb.admin.events.DomainToIntegrationEventMapper`

**TODO**：

1. 方法：

   - [ ] `Optional<IntegrationEvent> map(Object domainEvent)`：
     - 不同模块可实现自己的映射逻辑；

2. 默认实现示例：

   - `SystemDomainEventMapper`：处理用户、租户、权限领域事件；

3. **TDD 占位**：
   - [ ] `SystemDomainEventMapperTest.shouldMapUserCreatedEventToIntegrationEvent()`。

---

### 11.2.3 应用服务中集成

**TODO（文档约定）**：

1. 应用服务模式：

   - 业务操作中聚合内部产生领域事件（可通过领域模型自身维护 `List<Object> domainEvents`）；
   - 应用服务在事务提交前/后（依据设计）调用 `DomainEventPublisher.publish(aggregate.getDomainEvents())`；

2. 测试建议：
   - 使用 TDD 验证：在用户注册成功后，Outbox 中产生对应 `UserCreatedIntegrationEvent` 记录。

---

## 11.3 事件消费基础设施

### 11.3.1 RocketMQ 客户端封装 `RocketMqConsumerFactory`

**类：** `io.github.faustofanb.admin.events.infra.RocketMqConsumerFactory`

**TODO（方法级）**：

1. 依赖与配置：

   - [ ] MQ 地址、消费组名称、重试策略通过配置属性绑定（如 `app.mq.*`）；

2. 方法：

   - [ ] `public <T> void registerListener(String topic, String tag, Class<T> payloadType, MessageListener<T> listener)`；
   - `MessageListener<T>` 为函数式接口：`void onMessage(T payload, MessageHeaders headers)`；

3. **TDD 占位**（在有 MQ Mock 时实现）：
   - [ ] `RocketMqConsumerFactoryTest.shouldRegisterListenerWithCorrectTopicAndTag()`。

> 具体 RocketMQ SDK API 接入时再细化，此处作为抽象 TODO。

---

### 11.3.2 事件处理接口 `IntegrationEventHandler`

**接口：** `io.github.faustofanb.admin.events.IntegrationEventHandler<T extends IntegrationEvent>`

**TODO**：

1. 方法：

   - [ ] `Class<T> payloadType()`；
   - [ ] `void handle(T event, IntegrationEventContext context)`；

2. `IntegrationEventContext`：

   - 字段：`String messageId`、`String topic`、`String tag`、`String key`、`Long tenantId`、`int retryCount`；

3. **TDD 占位**：
   - [ ] `IntegrationEventContextTest.shouldExposeMessageMetadata()`。

---

### 11.3.3 消费端适配器 `MqListenerAdapter`

**类：** `io.github.faustofanb.admin.events.infra.MqListenerAdapter`

**TODO（方法级）**：

1. 依赖：

   - [ ] `IntegrationEventHandler<?> handler`；
   - [ ] `IdempotentEventHandler`（第 5 章中定义，用于幂等处理）；
   - [ ] `IdempotencyKeyGenerator`；

2. 行为：

   - 从 MQ 原始消息中解析 payload 与 headers；
   - 基于 `tenantId/aggregateId/eventType` 生成幂等键；
   - 调用 `idempotentEventHandler.handle(idempotencyKey, ttl, () -> handler.handle(event, context))`；

3. 错误处理：

   - 对业务异常与系统异常进行分类，决定是否让 MQ 重试或投递到死信队列（设计文档中如有约定需对齐）；

4. **TDD 占位**：
   - [ ] `MqListenerAdapterTest.shouldInvokeHandlerViaIdempotentWrapper()`；
   - [ ] `MqListenerAdapterTest.shouldGenerateIdempotencyKeyFromMessage()`。

---

## 11.4 典型事件消费者 TODO

### 11.4.1 用户与权限相关事件消费者

**示例类**：

- `UserCreatedEventHandler`
- `UserLockedEventHandler`
- `RoleAssignedEventHandler`

**TODO（方法级与约定）**：

1. 每个 Handler 实现 `IntegrationEventHandler<...>`：

   - 依赖对应的应用服务（如 `UserSyncService`, `PermissionSyncService`）；
   - 在 `handle` 方法中执行业务逻辑（如：创建本地只读视图、刷新缓存等）。

2. 测试建议：

   - 使用内存实现或 Mock 应用服务，验证收到事件后调用了正确的业务方法；

3. 注册：
   - 在配置类中使用 `RocketMqConsumerFactory.registerListener` 注册到相应 topic/tag。

---

### 11.4.2 Excel 与任务相关事件消费者

> 配合第 9 章 Excel 批处理与第 8 章任务调度。

**示例类**：

- `ExcelTaskCompletedEventHandler`

**TODO**：

1. 在任务完成时（第 9 章中）通过 Outbox 发布 `ExcelTaskCompletedIntegrationEvent`；
2. 消费端收到后：

   - 更新前端通知中心或发送站内消息（如有通知模块）；
   - 清理部分缓存或进行后续数据处理；

3. **TDD 占位**：
   - [ ] `ExcelTaskCompletedEventHandlerTest.shouldNotifyDownstreamOnCompletion()`。

---

## 11.5 多租户、安全与可观测性

**TODO（文档约定）**：

1. 多租户：

   - MQ 消息 payload 或 headers 中必须携带 `tenantId`；
   - 消费端在处理前将 `tenantId` 注入 `AppContext`，确保所有数据访问走租户隔离路径；

2. 安全：

   - 事件中不应包含敏感数据（如密码、完整 Token、隐私信息）；
   - 若需要传用户标识，优先使用内部 ID 或脱敏字段；

3. 可观测性：
   - 使用结构化日志记录事件消费结果，包括：`traceId`、`tenantId`、`topic`、`tag`、`eventType`、`result`、`latency`；
   - 指标：
     - 每类事件消费成功/失败计数：如 `admin_events_consumed_total{eventType=...,result=...}`；
     - 消费延迟分布：`admin_events_consume_latency_seconds`。

---

## 11.6 错误处理、重试与死信

**TODO（文档约定）**：

1. 错误分类：

   - 可重试错误：临时性下游错误（DB/Redis/MQ 短暂不可用等）；
   - 不可重试错误：参数异常、幂等冲突、业务规则拒绝等；

2. 行为：

   - 对可重试错误，让 MQ 按既定策略重投；
   - 对不可重试错误，记录错误信息并避免无限重试，可通过死信队列或错误存档表记录；

3. 死信处理：

   - 设计专门的死信消费任务（可由调度模块触发），对重要事件进行人工或自动补偿；

4. **TDD 建议**：
   - 模拟不同类型异常，验证 `MqListenerAdapter` 对重试/死信的分支处理逻辑。

---

> 本章完成后，系统将具备：
>
> - 统一的事件发布与消费抽象；
> - 与 Outbox、任务调度、Excel 批处理等模块的清晰协作路径；
> - 带幂等、防重与可观测性的事件驱动基础设施，为后续微服务拆分与扩展做好准备。
