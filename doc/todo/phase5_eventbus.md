# 第五阶段：事件总线与异步处理 (Phase 5: Event Bus & Async)

本阶段目标是构建可靠的事件驱动架构，优先使用 RocketMQ 事务消息，并以 Outbox 模式作为兜底，确保数据最终一致性。

## 5.1 消息模型定义

- [ ] **5.1.1 定义 IntegrationEvent**

  - 在 `common` 或 `core` 中定义集成事件基类。
  - 字段: `eventId`, `traceId`, `tenantId`, `source`, `occurredOn`, `eventType`.
  - 使用 Jackson 注解确保序列化兼容性。

- [ ] **5.1.2 规划 Topic 与 Tag**
  - 在配置文件或常量类中定义 Topic:
    - `TOPIC_USER`
    - `TOPIC_RESOURCE`
    - `TOPIC_EXCEL`
  - 定义 Tag 枚举: `USER_CREATED`, `RES_UPLOADED` 等。

## 5.2 RocketMQ 事务消息实现 (首选方案)

- [ ] **5.2.1 封装 TransactionProducer**

  - 在 `infra-integration` 中封装支持事务消息的 Producer。
  - 提供 `sendInTransaction(String topic, String tag, Object payload, Runnable localTransaction)` 方法。

- [ ] **5.2.2 实现 TransactionListener**
  - 实现 RocketMQ 的 `TransactionListener`。
  - `executeLocalTransaction`: 执行传入的 `localTransaction` (业务 DB 操作)。
  - `checkLocalTransaction`: 回查逻辑 (需要业务表支持反查，或者依赖 Outbox 表作为回查依据)。
    - _策略_: 为了简化回查，可以在本地事务中顺便写入一条“事务状态记录”或直接利用 Outbox 表作为事务状态记录。

## 5.3 Outbox 模式实现 (兜底与回查支持)

- [ ] **5.3.1 创建 Outbox 实体**

  - 对应 `t_outbox` 表。
  - 字段: `id`, `aggregateType`, `aggregateId`, `eventType`, `payload`, `status` (NEW, SENT), `createTime`.

- [ ] **5.3.2 实现 OutboxRepository**

  - 提供 `save()` 和 `findPendingEvents()` 方法。

- [ ] **5.3.3 结合事务消息的 Outbox 策略**

  - 在 `executeLocalTransaction` 中:
    1. 执行业务 SQL。
    2. 写入 Outbox 记录 (状态为 SENT 或 NEW)。
  - 在 `checkLocalTransaction` 中:
    1. 检查 Outbox 表是否存在对应记录。
    2. 如果存在，返回 COMMIT；否则返回 ROLLBACK (或 UNKNOWN)。

- [ ] **5.3.4 兜底扫描任务 (Scheduler)**
  - 在 `module/scheduler` 中创建 `OutboxScanner`。
  - 定时 (如每分钟) 扫描 `t_outbox` 中状态为 NEW 且超过一定时间的记录。
  - 重新投递消息，并更新状态。
  - **注意**: 消费端必须幂等，因为兜底扫描可能会导致重复发送。

## 5.4 消费端封装

- [ ] **5.4.1 封装 EventListener**
  - 定义抽象消费者基类，处理通用的反序列化、日志记录、Trace Context 恢复。
  - 强制要求子类实现幂等逻辑 (如基于 Redis 的 `setIfAbsent` 检查 `eventId`)。
