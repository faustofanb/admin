# 第 6 章 缓存与限流策略 TODO 清单（AI Agent 实施指引）

> 本章聚焦：**Redis/Redisson 缓存、热点数据防击穿、限流与保护**，
> 以及与 Sentinel、虚拟线程、高并发读写分离的协同。
>
> 目标：
>
> - 制定通用缓存与限流抽象，避免业务代码直接散落使用 Redis 命令；
> - 通过规范的 Key 设计、过期策略、回源与预热机制，控制缓存击穿、穿透、雪崩；
> - 提供统一的限流与保护能力（配合 Sentinel/BackPressureGuard）。
>
> 要求：
>
> - 明确 key 前缀、TTL 策略，统一使用 `backend-common` 的 `RedisKeyPrefixes`；
> - 所有核心组件需有单元测试，关键路径建议有简要文档注释；
> - 与第 4 章中 `RedisClient`/`BackPressureGuard` 抽象协同，不重复造轮子。

---

## 6.1 缓存分层与职责边界

**设计约定（文档说明）**：

1. 缓存类型：

   - 读多写少的配置类数据（权限、菜单、租户配置等）；
   - 中高频查询结果（列表分页快照、统计聚合缓存等）；
   - 防重复提交/防刷相关的短期令牌；
   - 任务进度、Excel 批处理状态等中间结果。

2. 职责划分：

   - `backend-common`：Key 常量、简单 TTL 约定；
   - `backend-infra`：Redis 客户端实现、底层连接池/序列化配置；
   - 各业务模块：基于缓存抽象进行缓存读写，不直接使用 Redis API 字符串；
   - 本章主要在 `backend-transaction` 或 `backend-infra` 中定义“可复用的缓存模式工具”。

3. 与虚拟线程的协同：
   - 所有缓存与限流调用默认运行在虚拟线程执行器上；
   - Redis 客户端 API 以同步调用为主（虚拟线程隐藏阻塞成本）。

---

## 6.2 通用缓存抽象 `CacheClient`

> 基于第 4 章中的 `RedisClient`，在上层引入“缓存语义”，屏蔽具体中间件细节。

### 6.2.1 接口定义

**接口：** `io.github.faustofanb.admin.cache.CacheClient`

**TODO（方法级）**：

1. 基础 Key-Value 操作：

   - [ ] `Optional<String> get(String key)`；
   - [ ] `void set(String key, String value, Duration ttl)`；
   - [ ] `void delete(String key)`；

2. 对象序列化支持（基于 Jackson 或其他工具，由 infra 实现）：

   - [ ] `<T> Optional<T> get(String key, Class<T> type)`；
   - [ ] `<T> void set(String key, T value, Duration ttl)`；

3. 批量支持（可选，按需要补充）：

   - [ ] `<T> Map<String, T> multiGet(Collection<String> keys, Class<T> type)`；
   - [ ] `<T> void multiSet(Map<String, T> values, Duration ttl)`。

4. **TDD 占位**：
   - [ ] `CacheClientContractTest`：
     - 约定：`set` 后 `get` 能取回同值；
     - 过期 TTL 行为在具体实现中用集成测试验证。

> 注：`CacheClient` 只表达同步语义；异步或流式场景可另行扩展。

---

### 6.2.2 Redis 实现占位 `RedisCacheClient`

**类：** `io.github.faustofanb.admin.cache.RedisCacheClient`

**TODO（实现说明）**：

1. 依赖：

   - [ ] 第 4 章中定义的 `RedisClient`；
   - [ ] 对象序列化工具（如 `ObjectMapper`）。

2. 行为：

   - 底层通过 `RedisClient` 的 `get/set/delete` 完成所有操作；
   - 决定 Value 序列化格式（JSON）与错误处理策略（记录错误日志、抛业务异常或忽略）。

3. **TDD 占位**：
   - [ ] `RedisCacheClientTest.shouldDelegateToRedisClient()`。

---

## 6.3 缓存模式工具 `CacheTemplate`

> 提供常见缓存模式封装：Cache-Aside（旁路缓存）、防击穿/穿透、批量预热等。

**类：** `io.github.faustofanb.admin.cache.CacheTemplate`

### 6.3.1 通用 Cache-Aside 模式

**TODO（方法级）**：

1. 依赖：

   - [ ] `CacheClient`；

2. 方法：

   - [ ] `<T> T getOrLoad(String key, Class<T> type, Duration ttl, Supplier<T> loader)`：
     - 步骤：
       1. 调用 `cacheClient.get(key, type)`；若存在则直接返回；
       2. 若不存在，则调用 `loader.get()` 从数据库或下游加载；
       3. 若 `loader` 返回非空，写入缓存并返回；
       4. 若为 `null` 或空，则可选择写入一个短 TTL 的“空值标记”（防穿透）。
   - [ ] 重载：`<T> Optional<T> findOrLoad(String key, Class<T> type, Duration ttl, Supplier<Optional<T>> loader)`；

3. **TDD**：
   - [ ] `CacheTemplateTest.shouldReturnCachedValueWhenPresent()`；
   - [ ] `CacheTemplateTest.shouldLoadAndCacheWhenMiss()`；
   - [ ] `CacheTemplateTest.shouldCacheNullMarkerForNotFound()`（若采用空值标记策略）。

---

### 6.3.2 热点 Key 防击穿与空值标记

**设计与 TODO**：

1. 在 `CacheTemplate` 中约定：

   - 对于某些高频关键 Key（如权限列表、菜单树），开启“空值标记”策略：
     - 当 `loader` 返回空时，向缓存写入特定标记（例如特殊 JSON），TTL 较短；
     - 下次命中该标记时，直接返回 empty，避免打到 DB。

2. 需要定义一个空值标记辅助类：

   - **类：** `io.github.faustofanb.admin.cache.NullValueMarker`
   - [ ] 方法：
     - [ ] `public static boolean isNullValueMarker(String value)`；
     - [ ] `public static String asNullValueMarker()`；
   - **TDD**：
     - [ ] `NullValueMarkerTest.shouldRecognizeMarker()`。

3. 在 `CacheTemplate` 的实现中：
   - 对取出的字符串先判断是否空值标记；是则直接返回 empty。

---

### 6.3.3 批量查询缓存模式（可选）

> 针对一次查询需要多个 Key 的场景，减少 N+1 次网络调用。

**TODO（占位）**：

- 预留方法签名，例如：
  - [ ] `<K, V> Map<K, V> batchGetOrLoad(Map<K, String> keyMapping, Class<V> type, Duration ttl, Function<Set<K>, Map<K, V>> batchLoader)`；
- 详细实现及测试可在出现具体需求时再补充。

---

## 6.4 业务缓存约定与 Key 设计

> 依赖 `backend-common` 中的 `RedisKeyPrefixes`，避免 Key 混乱。

**TODO（约定与示例）**：

1. 常见缓存类别与对应前缀：

   - 用户相关：`RedisKeyPrefixes.tenantUser(tenantId)` + `":detail:" + userId`；
   - 权限、角色、菜单：`RedisKeyPrefixes.tenantPermissionCache(tenantId)` + `":tree"`；
   - Excel 导入导出：`RedisKeyPrefixes.tenantExcelImport(tenantId)` + `":task:" + taskId`；
   - 限流相关：`RedisKeyPrefixes.tenantRateLimitKey(tenantId, routeId)`；

2. 业务模块缓存服务建议统一封装：

   - 例如：`UserCacheService`、`PermissionCacheService` 等，内部依赖 `CacheTemplate` 与 `RedisKeyPrefixes`。

3. **TDD 建议**：
   - 每个业务缓存服务都有对应的键构造测试，确保前缀和 key 结构符合规范。

---

## 6.5 限流与保护抽象

> 与第 4 章中的 `BackPressureGuard`、Sentinel 规则结合，形成统一的限流/熔断封装。

### 6.5.1 限流配置模型 `RateLimitRule`

**类：** `io.github.faustofanb.admin.rateLimit.RateLimitRule`

**TODO**：

1. 字段：

   - [ ] `String key` —— 限流维度标识，例如 API 路径、资源 ID；
   - [ ] `int permitsPerSecond` 或 `int maxRequestsPerWindow`；
   - [ ] `Duration window`；
   - [ ] 可选：`int burstCapacity`；

2. 方法：

   - [ ] `public String getKey()` 等基础 getter；
   - [ ] 可选：`public boolean isDisabled()`（当配置为 0 或负数时可视为关闭）；

3. **TDD**：
   - [ ] `RateLimitRuleTest.shouldRecognizeDisabledRule()`。

---

### 6.5.2 限流存储与计数 `RateLimiter`

> 基于第 4 章的 `RateLimitCounter` 占位，进行具象化封装。

**接口：** `io.github.faustofanb.admin.rateLimit.RateLimiter`

**TODO（方法级）**：

1. 方法：

   - [ ] `boolean tryAcquire(String key, RateLimitRule rule)`：
     - 返回 true 表示可以通过，false 表示被限流；

2. 实现说明：

   - 实现类可基于 Redis 的滑动窗口、固定窗口或令牌桶算法；
   - 底层使用 `RedisClient.incrBy` + 过期时间实现计数；
   - 结合 `RedisKeyPrefixes.tenantRateLimitKey` 生成 key。

3. **TDD 占位**：
   - [ ] `RateLimiterTest.shouldAllowWithinLimit()`；
   - [ ] `RateLimiterTest.shouldRejectWhenExceedLimit()`。

---

### 6.5.3 与 `BackPressureGuard`/Sentinel 的集成

**设计与 TODO**：

1. 在 `backend-transaction` 或 `backend-infra` 中提供一个适配器：

   - **类：** `RateLimitBackPressureGuard`
   - 包：`io.github.faustofanb.admin.rateLimit`；
   - 实现 `BackPressureGuard` 接口；
   - 内部依赖 `RateLimiter` 和必要的规则加载服务；
   - 在 `guard` 方法中先做限流判断，不通过则抛出业务异常或返回失败标记。

2. 与 Sentinel：
   - 可以在后续 Sentinel 集成章节中定义 `SentinelRateLimiter` 或规则同步逻辑；
   - 本章只预留接口和类名，不强制实现细节。

---

## 6.6 典型使用示例（文档级场景）

> 非代码实现，仅在 TODO 文档中描述业务如何使用缓存与限流。

1. 用户权限缓存：

   - 登录成功后，将用户权限树写入缓存，TTL 为 5~10 分钟；
   - 权限变更（授权/回收角色）时，发布事件并清理相关 Key；
   - 读取权限时，通过 `UserPermissionCacheService` 调用 `CacheTemplate.getOrLoad` 拿到数据。

2. Excel 导入进度缓存：

   - 提交导入任务时生成 taskId；
   - 任务执行过程中按阶段更新 `RedisKeyPrefixes.tenantExcelImport(tenantId) + ":task:" + taskId`；
   - 前端轮询查询进度 API，直接从缓存返回状态。

3. 接口限流：
   - 为敏感接口（登录、验证码发送、导出大批量数据）配置 `RateLimitRule`；
   - 控制器层或 Filter 中通过 `RateLimiter.tryAcquire` 做前置判断；
   - 若返回 false，则直接返回统一的“被限流”错误码（参考 `ErrorCode.RATE_LIMITED`）。

---

## 6.7 测试与可观测性要求

1. 覆盖率：

   - `cache` 与 `rateLimit` 相关的公共组件，单元测试覆盖率不低于 80%；
   - 特别是缓存击穿/空值标记/限流超限分支必须有测试用例覆盖。

2. 日志与指标：

   - 关键缓存 miss/回源行为可打 debug 日志；
   - 限流拒绝次数暴露为 Prometheus 指标（例如 `admin_rate_limit_rejected_total`）。

3. 压测建议（文档级）：
   - 对高频接口进行压测，观察缓存命中率、Redis QPS、限流拦截情况；
   - 根据结果调整 TTL、限流阈值与策略实现。

---

> 本章完成后，系统将具备：
>
> - 统一的缓存客户端与缓存模式封装；
> - 规范的业务缓存 Key 设计与使用示例；
> - 可复用的限流与保护抽象，为后续 Sentinel 集成和高并发优化打好基础。
