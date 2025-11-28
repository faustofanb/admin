# 第 5 章 分布式事务与一致性 TODO 清单（AI Agent 实施指引）

> 本章聚焦：**本地事务 + Outbox + MQ 的最终一致性方案**，以及与 Seata 的集成边界。
> 主要目标：为跨模块/跨服务的业务流程提供可靠的一致性保证，同时保持高性能与可观测性。
> 涉及模块：
>
> - `backend-transaction`：事务与一致性支持的核心实现；
> - `backend-infra`：与 RocketMQ、定时调度（PowerJob）等基础设施的对接；
> - 各业务模块（system/resource/excel/...）：调用侧如何正确使用 Outbox API。
>
> 要求：
>
> - 细粒度到类/方法级，强调 Outbox 写入、扫描出站、重试/幂等等关键行为；
> - 全程 TDD：对出站流程、幂等键、生效/失败状态迁移写充分单元/组件测试；
> - 清晰注释：每个事务边界都要写明“写库 + 写 outbox”在哪一层完成；
> - 严格对齐 `doc/design/4.分布式事务与一致性策略.md` 与 README 中的一致性说明。

---

## 5.1 Outbox 模式总体设计范围

**核心思想回顾（仅文档）**：

1. 写入端：业务服务在本地事务中同时写业务表与 Outbox 表（`t_outbox`）。
2. 出站端：独立任务（如 PowerJob 任务）周期性扫描 Outbox，发送 MQ 消息（RocketMQ），然后更新 Outbox 状态。
3. 消费端：各服务/模块订阅 MQ 消息，基于幂等键与业务规则处理，保证“至少一次 + 幂等处理”。

TODO 文档需要指导 AI Agent 在 `backend-transaction` 中实现：

- Outbox 表的领域模型与 Repository；
- 出站任务应用服务（扫描、发送、更新状态、重试）；
- 幂等键策略与去重存储；
- 与本地业务事务的集成模式（如何在应用层调用 Outbox API）。

---

## 5.2 Outbox 表领域模型与存储层

### 5.2.1 Outbox 领域模型 `OutboxMessage`

**类：** `io.github.faustofanb.admin.transaction.outbox.domain.OutboxMessage`

**TODO（字段与方法）**：

1. 字段设计（对应 `t_outbox` 表）：

   - [ ] `Long id` —— 主键；
   - [ ] `Long tenantId` —— 租户 ID（必填，保持多租户隔离）；
   - [ ] `String aggregateType` —— 领域聚合类型，如 `"User"`、`"Resource"`；
   - [ ] `String aggregateId` —— 聚合 ID（字符串形式，兼容不同 ID 类型）；
   - [ ] `String eventType` —— 事件类型，如 `"UserCreated"`；
   - [ ] `String payload` —— 序列化后的 JSON 负载；
   - [ ] `String headers`（可选）—— 序列化的附加元数据；
   - [ ] `OutboxStatus status` —— `PENDING`, `PUBLISHED`, `FAILED` 等；
   - [ ] `int retryCount` —— 已重试次数；
   - [ ] `Instant nextAttemptAt` —— 下次重试时间；
   - [ ] `Instant createdAt`；
   - [ ] `Instant updatedAt`；

2. 领域行为方法：

   - [ ] `public void markPublished()`：
     - 将 `status` 设为 `PUBLISHED`，更新 `updatedAt`；
   - [ ] `public void markFailedAndScheduleRetry(Duration backoff)`：
     - 将 `status` 设为 `FAILED` 或 `PENDING_RETRY`（视设计而定）；
     - `retryCount++`；
     - 更新 `nextAttemptAt = now + backoff`；
   - [ ] `public boolean canRetry(int maxRetries, Instant now)`：
     - 返回 `retryCount < maxRetries && (nextAttemptAt == null || !nextAttemptAt.isAfter(now))`；
   - **TDD**：
     - [ ] `OutboxMessageTest.shouldMarkPublished()`；
     - [ ] `OutboxMessageTest.shouldScheduleRetryWithBackoff()`；
     - [ ] `OutboxMessageTest.shouldDetermineRetryEligibility()`。

3. 枚举 `OutboxStatus`：
   - [ ] 枚举值：`PENDING`, `PUBLISHED`, `FAILED`（可根据设计扩展如 `DEAD`）；
   - [ ] 方法：`public boolean isTerminal()`（如 `PUBLISHED`/`DEAD` 为终态）。

---

### 5.2.2 Outbox Repository 接口

**接口：** `io.github.faustofanb.admin.transaction.outbox.domain.OutboxRepository`

**TODO（方法级）**：

1. 写入接口（供业务事务内调用）：

   - [ ] `void save(OutboxMessage message)`；
   - [ ] `void saveAll(Collection<OutboxMessage> messages)`；

2. 读取待出站消息：

   - [ ] `List<OutboxMessage> findPendingBatch(int batchSize, Instant now)`：
     - 查询 `status = PENDING` 且 `nextAttemptAt <= now` 的前 `batchSize` 条记录；

3. 更新状态：

   - [ ] `void markPublished(Collection<Long> ids)`；
   - [ ] `void markFailed(Collection<Long> ids, Duration backoff, int maxRetries)`；

4. **TDD**（在接入 Jimmer 或 DB 之后）：
   - [ ] `OutboxRepositoryTest.shouldSaveAndLoadPendingMessages()`；
   - [ ] `OutboxRepositoryTest.shouldUpdateStatusAndRetryFields()`。

> 本节主要约定接口与行为，具体实现可使用 Jimmer Repository，在后续数据访问章节展开。

---

## 5.3 业务侧写入 Outbox 的统一 API

### 5.3.1 出站事件抽象 `IntegrationEvent`

**类：** `io.github.faustofanb.admin.transaction.outbox.api.IntegrationEvent`

**TODO**：

1. 使用 `sealed interface` 或抽象基类表达通用字段：

   - [ ] 字段：
     - [ ] `String aggregateType()`；
     - [ ] `String aggregateId()`；
     - [ ] `String eventType()`；
     - [ ] `Instant occurredAt()`；
   - 可选：
     - [ ] 静态工厂/辅助方法用于构造带 payload 的事件。

2. 具体业务事件（如 `UserCreatedEvent`、`ResourceUploadedEvent`）可在各模块中实现，本章 TODO 只定义公共接口与基础实现说明。

---

### 5.3.2 Outbox 记录工厂 `OutboxFactory`

**类：** `io.github.faustofanb.admin.transaction.outbox.api.OutboxFactory`

**TODO（方法级）**：

1. 方法：
   - [ ] `public OutboxMessage fromEvent(Long tenantId, IntegrationEvent event, Object payload)`：
     - 使用统一 JSON 序列化工具（后续在 infra 中指定，如 Jackson）将 payload 转为字符串；
     - 填充 `aggregateType/aggregateId/eventType/tenantId/payload/status=PENDING/retryCount=0/createdAt=now`；
   - **TDD**：
     - [ ] `OutboxFactoryTest.shouldCreatePendingOutboxMessageFromEvent()`：
       - 断言字段映射正确，状态为 PENDING，重试计数为 0。

---

### 5.3.3 业务事务与 Outbox 的组合助手 `TransactionalOutboxService`

**类：** `io.github.faustofanb.admin.transaction.outbox.api.TransactionalOutboxService`

**目标**：为业务用例提供“写业务表 + 写 Outbox”的统一模板方法，减少重复样板代码。

**TODO（方法级）**：

1. 依赖：

   - [ ] `PlatformTransactionManager` 或 Spring `@Transactional` 机制；
   - [ ] `OutboxRepository`；

2. 方法：

   - [ ] `public <T> T executeInTransactionWithOutbox(Supplier<T> businessLogic, List<IntegrationEvent> events)`：
     - 在一个本地事务中：
       1. 执行 `businessLogic.get()`（写业务表）；
       2. 将 `events` 通过 `OutboxFactory` 转为 `OutboxMessage`，使用 `OutboxRepository.saveAll` 持久化；
     - 返回业务逻辑的结果；
   - [ ] 重载：`public void executeInTransactionWithOutbox(Runnable businessLogic, List<IntegrationEvent> events)`；
   - **TDD**：
     - [ ] `TransactionalOutboxServiceTest.shouldWriteBusinessAndOutboxInOneTransaction()`：
       - 可用内存仓储/事务模拟验证调用顺序与异常回滚行为（例如业务逻辑抛异常时不要写 Outbox）。

3. 约定：
   - 业务模块优先通过该服务提交需要对外交付的集成事件，而不是自己手写事务 + Outbox 写入。

---

## 5.4 出站任务应用服务（扫描 Outbox → MQ）

### 5.4.1 MQ 发送抽象接口 `MqProducer`

**接口：** `io.github.faustofanb.admin.transaction.outbox.spi.MqProducer`

**TODO**：

1. 方法：

   - [ ] `void send(String topic, String tag, String key, String body)`；
   - 说明：
     - `topic` 和 `tag` 应使用 `backend-common` 中的 `MqTopics` 常量；
     - `key` 可用作幂等键或消息键（如 `aggregateType + aggregateId + eventType`）。

2. 在 TODO 文档中注明：
   - 具体实现类（如 `RocketMqProducer`）在 `backend-infra` 中实现，后续章节细化；
   - 此处只关注接口方法与行为。

---

### 5.4.2 出站任务服务 `OutboxDispatchService`

**类：** `io.github.faustofanb.admin.transaction.outbox.app.OutboxDispatchService`

**TODO（方法级）**：

1. 依赖：

   - [ ] `OutboxRepository`；
   - [ ] `MqProducer`；
   - [ ] 参数配置类 `OutboxDispatchProperties`（见下）；
   - [ ] 可选：`Clock` 或时间工具，方便测试。

2. 配置属性类 `OutboxDispatchProperties`：

   - [ ] 包：`io.github.faustofanb.admin.transaction.outbox.config`；
   - [ ] 字段：
     - [ ] `int batchSize`（每次扫描条数，默认例如 100）；
     - [ ] `int maxRetries`；
     - [ ] `Duration initialBackoff`；
     - [ ] `Duration maxBackoff`；
   - **TDD**：
     - [ ] `OutboxDispatchPropertiesTest.shouldBindConfiguration()`。

3. 核心方法：

   - [ ] `public void dispatchOnce()`：
     - 步骤：
       1. 获取当前时间 `now`；
       2. 调用 `outboxRepository.findPendingBatch(batchSize, now)` 获取待处理消息；
       3. 对每条消息：
          - 构造 `topic/tag/key/body`（可在 `OutboxMessage` 或辅助类中提供转换逻辑）；
          - 调用 `mqProducer.send(...)`；
          - 若发送成功，调用 `message.markPublished()` 或登记待批量更新；
          - 若发送异常，根据 `maxRetries` 与 backoff 策略调用 `message.markFailedAndScheduleRetry(...)`；
       4. 批量调用 `outboxRepository.markPublished(...)` 与 `markFailed(...)` 更新状态。
   - **TDD**：
     - [ ] `OutboxDispatchServiceTest.shouldPublishPendingMessages()`：
       - 使用假实现/Mock 的 `OutboxRepository` 与 `MqProducer` 验证：
         - 仅 `PENDING` 且到期的消息被发送；
         - 成功后被标记为 `PUBLISHED`。
     - [ ] `OutboxDispatchServiceTest.shouldScheduleRetryOnFailure()`：
       - 模拟 `MqProducer.send` 抛出异常；
       - 验证对应消息重试计数与 `nextAttemptAt`、状态更新。

4. 幂等性考虑（发送端）：
   - 本端主要负责“至少发送一次”；
   - 消费端幂等性在另一个章节 TODO 中详细说明，这里只在注释中强调：
     - MqProducer 实现可考虑使用消息 key 与 RocketMQ 的重复消息语义协同。

---

### 5.4.3 调度入口与 PowerJob 集成占位

**类：** `OutboxDispatchJob`

**TODO**：

1. 包：`io.github.faustofanb.admin.transaction.outbox.job`；
2. 方法：

   - [ ] `public void execute()`：
     - 简单调用 `outboxDispatchService.dispatchOnce()`；
   - **说明**：
     - 具体与 PowerJob 的集成（注解/SDK）在任务调度章节展开，此处只定义一个可供调度器调用的简单入口方法。

3. **TDD 占位**：
   - [ ] `OutboxDispatchJobTest.shouldDelegateToDispatchService()`。

---

## 5.5 消费端幂等性与去重策略

### 5.5.1 幂等键生成器 `IdempotencyKeyGenerator`

**类：** `io.github.faustofanb.admin.transaction.idempotency.IdempotencyKeyGenerator`

**TODO**：

1. 方法：
   - [ ] `public static String forOutboxMessage(OutboxMessage message)`：
     - 组合 `tenantId + aggregateType + aggregateId + eventType` 等字段生成幂等键；
   - [ ] 可选：`public static String forCustom(String... parts)`；
2. **TDD**：
   - [ ] `IdempotencyKeyGeneratorTest.shouldGenerateDeterministicKey()`；
   - [ ] `IdempotencyKeyGeneratorTest.shouldDifferForDifferentMessages()`。

---

### 5.5.2 幂等存储抽象 `IdempotencyStore`

**接口：** `io.github.faustofanb.admin.transaction.idempotency.IdempotencyStore`

**TODO**：

1. 方法：
   - [ ] `boolean tryPut(String key, Duration ttl)`：
     - 原子性要求：
       - 若 key 不存在，则写入并返回 true；
       - 若 key 已存在，则返回 false（表示已处理过）。
2. 实现说明：
   - 典型实现基于 Redis `SETNX` 或 Redisson 的 `RBucket.trySet`；
   - 具体实现类在 `backend-infra` 中实现（如 `RedisIdempotencyStore`），本章只定义接口与语义。
3. **TDD 占位**：
   - [ ] `IdempotencyStoreContractTest.shouldAllowFirstPutButRejectSecond()`（可在具体实现时继承该约定测试）。

---

### 5.5.3 消费端处理模板 `IdempotentEventHandler`

**类：** `io.github.faustofanb.admin.transaction.idempotency.IdempotentEventHandler`

**TODO**：

1. 目标：为消费端提供一个模板方法，统一处理"先幂等检查，再执行业务逻辑"的模式。
2. 方法：
   - [ ] `public <T> void handle(String idempotencyKey, Duration ttl, Runnable businessLogic)`：
     - 步骤：
       1. 调用 `idempotencyStore.tryPut(idempotencyKey, ttl)`；
       2. 若返回 false，则直接返回（表示已处理）；
       3. 若为 true，则执行业务逻辑；
   - **TDD**：
     - [ ] `IdempotentEventHandlerTest.shouldSkipWhenKeyAlreadyExists()`；
     - [ ] `IdempotentEventHandlerTest.shouldRunLogicWhenFirstTime()`。

> 业务消费端（各模块的 MQ Consumer）应优先通过此模板来处理消息，避免重复造轮子。

---

## 5.6 与 Seata 的集成边界（占位）

> 设计文档中提到：主要采用 Outbox + MQ 最终一致，Seata AT/SAGA 仅在必要场景启用。

**TODO（设计与接口级）**：

1. 在 `backend-transaction` 中定义 Seata 相关辅助类/注解占位：

   - [ ] 如 `@SeataTransactionalBoundary` 注解（可选），用于标记需要 Seata 管理的事务边界；
   - 具体实现与配置在实际引入 Seata 依赖时细化。

2. 文档约定：
   - [ ] 绝大多数业务场景使用 Outbox；
   - [ ] 仅在跨资源强一致要求极高、且中间件支持 Seata 的少数场景启用 Seata；
   - [ ] TODO 文档中要求开发时优先考虑 Outbox 模式，必要时在设计评审中专门讨论是否引入 Seata。

---

## 5.7 TDD 与一致性相关质量要求

1. 所有 Outbox 相关类必须有充分的单元/组件测试覆盖：

   - 状态迁移（PENDING → PUBLISHED/FAILED）；
   - 重试计数与 backoff 计算；
   - 出站服务在发送异常时的行为；
   - 幂等键生成与幂等存储逻辑。

2. 推荐为出站流程增加一个小型“集成流水线”测试（可后置）：

   - 使用内存 DB + Fake MqProducer + InMemoryIdempotencyStore；
   - 从创建事件 → 写 Outbox → 调用 `dispatchOnce()` → 模拟消费端幂等处理，验证全链路行为。

3. 日志与可观测性要求：
   - Outbox 出站任务需打结构化日志，包含：`tenantId`、`messageId`、`eventType`、`retryCount`、结果（成功/失败原因）；
   - 建议打指标（成功数、失败数、重试数）供 Prometheus 采集（可在监控章节详细规定）。

---

> 本章完成后，系统将具备：
>
> - 标准化的 Outbox 领域模型与 Repository；
> - 统一的“业务写库 + Outbox 写入”事务模板；
> - 可配置的出站任务服务与 MQ 发送抽象；
> - 消费端幂等处理模板与去重存储抽象；
> - 明确的 Seata 使用边界与优先级。
>
> 后续业务模块（system/resource/excel/schedule 等）都应在需要对外交付集成事件时，优先使用本章定义的 API 与模式。
