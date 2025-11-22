# 第 4 章 性能与高并发与数据访问基础 TODO 清单（AI Agent 实施指引）

> 本章聚焦：**虚拟线程在 IO 路径的落地、PostgreSQL 数据源与读写分离、Jimmer 基础配置、分页与限流基础抽象**。
> 该章节主要涉及 `backend-infra`（数据库、Redis、线程池等基础设施封装）以及与 `backend-common`/`backend-core` 的协作。
> 要求：
>
> - 细粒度到类/方法级，便于 AI Agent 逐条实现；
> - 严格遵循 `doc/design/3.性能与高并发.md` 中虚拟线程、读写分离、缓存等设计；
> - 所有与性能/资源控制相关的代码必须有明确注释，说明潜在风险与调优点；
> - 采用 TDD：先写测试（尤其是分页、键集分页、读写路由逻辑），再写实现。

---

## 4.1 虚拟线程在 Spring Boot 中的落地

### 4.1.1 虚拟线程开关与配置属性

**目标**：在 `backend-boot` 与 `backend-infra` 中建立统一的虚拟线程配置开关，便于在不同环境中启用/禁用和调试。

**TODO**：

1. 在 `application.yaml`（`backend-boot`）中增加配置占位：

   - [ ] `spring.threads.virtual.enabled: true`；
   - [ ] 自定义前缀（如 `app.threads.virtual`）下的参数：
     - [ ] `enabled`：是否启用虚拟线程池；
     - [ ] `name-prefix`：虚拟线程名称前缀，默认 `biz-vt-`；

2. 在 `backend-infra` 中定义配置属性类：
   - [ ] 类：`io.github.faustofanb.admin.infra.config.VirtualThreadProperties`；
   - [ ] 使用 `@ConfigurationProperties("app.threads.virtual")`；
   - [ ] 字段：
     - [ ] `boolean enabled = true;`
     - [ ] `String namePrefix = "biz-vt-";`；
   - **TDD**：
     - [ ] `VirtualThreadPropertiesTest.shouldBindFromConfiguration()`：
       - 使用 Spring Boot 测试环境加载配置并断言属性绑定正确。

---

### 4.1.2 虚拟线程执行器 Bean 定义

**类：** `io.github.faustofanb.admin.infra.config.ExecutorConfig`

**TODO（方法级）**：

1. 定义虚拟线程执行器 Bean：

   - [ ] `@Bean("virtualThreadExecutor") public ExecutorService virtualThreadExecutor(VirtualThreadProperties props)`：
     - 若 `props.enabled` 为 true，则使用 `VirtualThreadExecutorFactory.newVirtualThreadPerTaskExecutor(props.namePrefix())`；
     - 否则可回退到一个有限线程池（如 `Executors.newFixedThreadPool`），便于在不支持虚拟线程的环境中运行；
   - **TDD**：
     - [ ] `ExecutorConfigTest.shouldCreateVirtualThreadExecutorWhenEnabled()`；
     - [ ] `ExecutorConfigTest.shouldFallbackToPlatformPoolWhenDisabled()`（可通过配置覆盖）。

2. 定义 `ContextAwareExecutor` Bean：
   - [ ] `@Bean("contextAwareVirtualExecutor") public ContextAwareExecutor contextAwareVirtualExecutor(@Qualifier("virtualThreadExecutor") ExecutorService delegate)`；
   - **TDD**：
     - [ ] `ExecutorConfigTest.shouldCreateContextAwareExecutorBean()`：加载 Spring 上下文，断言 Bean 存在且可提交任务（参考 `ContextAwareExecutorTest`）。

> 后续所有 IO 密集型组件（数据库访问、Redis 操作、大量 HTTP 调用、Excel 处理）都应优先依赖 `contextAwareVirtualExecutor` 或更高层的封装。

---

## 4.2 PostgreSQL 数据源与读写分离

### 4.2.1 多数据源配置属性

**目标**：为主库（写）与从库（读）提供统一的配置入口。

**类：** `io.github.faustofanb.admin.infra.config.DataSourceProperties`

**TODO**：

1. 使用 `@ConfigurationProperties("app.datasource")`：
   - [ ] 字段：
     - [ ] `DataSourceConfig master;`
     - [ ] `List<DataSourceConfig> slaves;`
   - [ ] `record DataSourceConfig(String url, String username, String password, String driverClassName)`；
2. **TDD**：
   - [ ] `DataSourcePropertiesTest.shouldBindMasterAndSlavesFromYaml()`：
     - 在测试配置中定义多个数据源，验证绑定正确。

---

### 4.2.2 读写路由数据源 `ReadWriteRoutingDataSource`

**类：** `io.github.faustofanb.admin.infra.db.ReadWriteRoutingDataSource`

**TODO（方法级）**：

1. 继承 `AbstractRoutingDataSource`：

   - [ ] 静态 ThreadLocal 标记：`private static final ThreadLocal<Boolean> writeFlag = new ThreadLocal<>();`；
   - [ ] 辅助方法：
     - [ ] `public static void markWrite()`：`writeFlag.set(true);`；
     - [ ] `public static void clear()`：`writeFlag.remove();`；
     - [ ] `public static boolean isWrite()`：返回当前是否标记为写；
   - [ ] 覆写 `protected Object determineCurrentLookupKey()`：
     - 若 `Boolean.TRUE.equals(writeFlag.get())` 返回 `"master"`；否则返回 `"slave"`；
   - **TDD**（不依赖真实 DB）：
     - [ ] `ReadWriteRoutingDataSourceTest.shouldRouteToMasterWhenWriteFlagSet()`；
     - [ ] `ReadWriteRoutingDataSourceTest.shouldRouteToSlaveByDefault()`。

2. 在配置类中组装多数据源：

   - [ ] 类：`io.github.faustofanb.admin.infra.config.DataSourceConfig`（命名与属性类可协调）；
   - [ ] 方法：
     - [ ] `@Bean public DataSource routingDataSource(DataSourceProperties props)`：
       - 创建 master 与多个 slave DataSource；
       - 将它们放入 `Map<Object, Object> targetDataSources`，key 为 `"master"` 与 `"slave"`（可为 `slave0/slave1` 等，路由逻辑后续扩展为简单轮询或随机）；
   - **TDD**：
     - [ ] `DataSourceConfigTest.shouldCreateRoutingDataSourceWithMasterAndSlaves()`（可通过查看内部 targetDataSources 大小与 key）。

3. 与事务集成约定：
   - [ ] 在 Command Handler 或带有 `@Transactional` 的写操作入口处，使用 AOP 或拦截器在方法执行前调用 `ReadWriteRoutingDataSource.markWrite()`，执行后 `clear()`；
   - [ ] 在 TODO 文档中记录这一约定，具体实现可在事务支持章节细化。

---

### 4.2.3 Jimmer 配置与 TenantFilter 占位

**类：** `io.github.faustofanb.admin.infra.db.JimmerConfig`

**TODO**：

1. 提供 Jimmer 的基础配置 Bean：
   - [ ] `@Bean public SqlClient sqlClient(DataSource dataSource)`（伪代码，占位）；
   - [ ] 在配置中注册多租户过滤：
     - 使用 `AppContextHolder.get().tenantId()` 作为当前租户；
     - 在所有查询/更新自动追加 `tenant_id = :tenantId` 条件；
2. **TDD 思路**（实际代码在后续数据访问章节实现）：
   - [ ] `JimmerTenantFilterTest.shouldAppendTenantConditionForAllQueries()`：
     - 使用内存 DB 或 Mock 验证生成 SQL 中包含 tenant 条件。

> 本节先占位设计与 Bean 签名，详细 SQLClient 配置可以在真正引入 Jimmer 时展开，这里主要强调 TenantFilter 的存在与多租户安全要求。

---

## 4.3 分页与键集分页工具

> 对应设计文档中避免深度 offset、优先键集分页的要求。

### 4.3.1 Offset 分页辅助 `OffsetPagingUtils`

**类：** `io.github.faustofanb.admin.infra.db.OffsetPagingUtils`

**TODO（方法级）**：

1. `public static int calcOffset(int page, int size)`：

   - 若 `page <= 0` 或 `size <= 0`，抛出 `IllegalArgumentException` 或业务异常；
   - 返回 `(page - 1) * size`；
   - **TDD**：
     - [ ] `OffsetPagingUtilsTest.shouldCalcOffsetCorrectly()`；
     - [ ] `OffsetPagingUtilsTest.shouldRejectInvalidPageOrSize()`。

2. `public static int limit(int size, int maxSize)`：
   - 将请求的 pageSize 限制在 `1..maxSize`；
   - **TDD**：
     - [ ] `OffsetPagingUtilsTest.shouldClampPageSizeToMax()`。

---

### 4.3.2 键集分页辅助 `KeysetPagingUtils`

**类：** `io.github.faustofanb.admin.infra.db.KeysetPagingUtils`

**TODO**：

1. 定义通用键集分页参数：
   - [ ] `record KeysetPage<T extends Comparable<T>>(T lastKey, int size)`；
2. 方法：
   - [ ] `public static <T extends Comparable<T>> KeysetPage<T> of(T lastKey, int size)`：
     - 校验 size > 0；
   - [ ] 提供用于构造 where 条件的帮助方法（仅辅助，具体集成在 Repository 层）：
     - 例如返回 SQL 片段或参数包装对象，此处在 TODO 中说明，无需立即实现具体 Jimmer DSL。
3. **TDD**：
   - [ ] `KeysetPagingUtilsTest.shouldCreateValidKeysetPage()`。

---

## 4.4 Redis 基础访问抽象（轻量）

> 不在本章实现完整缓存策略（有专门章节），这里只定义最小抽象，方便后续模块使用，并强调多租户与 Key 规范。

### 4.4.1 Redis 客户端抽象接口 `RedisClient`

**类：** `io.github.faustofanb.admin.infra.redis.RedisClient`

**TODO（方法级）**：

1. 定义基础操作（不依赖具体实现）：
   - [ ] `CompletableFuture<Optional<String>> get(String key)`；
   - [ ] `CompletableFuture<Void> set(String key, String value, Duration ttl)`；
   - [ ] `CompletableFuture<Boolean> delete(String key)`；
   - [ ] `CompletableFuture<Long> incrBy(String key, long delta)`；
2. 要求：
   - [ ] 所有方法应假定在虚拟线程友好的 Redis 客户端基础上实现（如 Redisson 异步 API）；
   - [ ] 调用方应优先使用 `contextAwareVirtualExecutor` 提交阻塞操作，或直接使用客户端的异步接口。
3. **TDD**：
   - 接口本身无逻辑，可在后续具体实现（如 `RedissonRedisClient`）时编写测试；
   - 在本章 TODO 中标注测试占位：`RedissonRedisClientTest.*`。

---

### 4.4.2 Redis 限流与计数器抽象（占位）

**类：** `RateLimitCounter`

**TODO**：

1. 包路径：`io.github.faustofanb.admin.infra.redis`；
2. 方法示例：
   - [ ] `public boolean tryAcquire(String key, long maxCount, Duration window)`；
   - **说明**：
     - 最终可基于 Redisson 的 `RateLimiter` 或 Lua 脚本实现，这里只列方法签名与行为预期；
   - **TDD**：
     - [ ] `RateLimitCounterTest.shouldAllowWithinLimit()`；
     - [ ] `RateLimitCounterTest.shouldRejectWhenExceeded()`（在实现章节细化）。

---

## 4.5 Back-pressure 与 Sentinel/Spring 集成占位

> 详细 Sentinel 策略在缓存与限流章节，这里只为高并发基础提供一些通用抽象与 TODO。

### 4.5.1 通用后压工具接口 `BackPressureGuard`

**类：** `io.github.faustofanb.admin.infra.concurrency.BackPressureGuard`

**TODO**：

1. 方法：
   - [ ] `public <T> T execute(String resourceName, Callable<T> callable)`；
   - [ ] `public void execute(String resourceName, Runnable runnable)`；
2. 说明：
   - 实现可以基于 Sentinel 的 `SphU.entry(resourceName)` 等，在资源繁忙时抛出限流异常；
   - common 层只定义接口，本章在 infra 中提供基于 Sentinel 的实现时再补充。
3. **TDD 占位**：
   - [ ] `BackPressureGuardTest.shouldExecuteWhenPermitted()`；
   - [ ] `BackPressureGuardTest.shouldThrowWhenBlocked()`。

---

## 4.6 TDD 与性能相关质量要求

1. 所有与路由/分页/线程池相关的工具类必须有单元测试覆盖：

   - 读写路由的分支逻辑；
   - 分页 offset、键集分页参数校验；
   - 虚拟线程执行器在 enabled/disabled 两种情况下的行为。

2. 建议在 `backend-infra` 添加简单的 "性能防回归" 测试（可选）：

   - 对某些工具方法（如分页计算）使用 JMH 或简单循环，确保没有明显性能陷阱（可后置，不必立即实现）。

3. 文档同步：
   - 若在实现中对读写分离策略、分页策略等有调整，需同步更新 `doc/design/3.性能与高并发.md` 与本 TODO 文档。

---

> 本章完成后，系统将拥有：
>
> - 可配置的虚拟线程执行器与上下文感知封装；
> - 基础的 PostgreSQL 多数据源与读写分离路由能力；
> - 分页与键集分页工具方法；
> - Redis 访问与限流/计数器抽象的占位；
> - Back-pressure 抽象接口，为 Sentinel/熔断集成铺路。
>
> 这为后续资源模块、Excel 批处理、任务调度等高并发场景提供了坚实的技术底座。
