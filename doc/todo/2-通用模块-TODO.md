# 第 2 章 通用模块 TODO 清单（AI Agent 实施指引）

> 本章是 AI 代码生成 Agent 针对 **`backend-common` 通用基础模块** 的详细实施指南。
> 该模块是整个系统的“内核工具箱”，为所有业务模块、基础设施模块提供统一的：
>
> - API 协议与错误码模型；
> - 统一异常体系；
> - 通用 ID 与时间工具；
> - AppContext 与多租户上下文基础；
> - 虚拟线程与上下文传播抽象；
> - 通用函数式工具与集合工具；
> - 基于 TDD 的高质量单元测试示例与基线。
>
> 要求：
>
> - 细粒度到类级/方法级；
> - 所有公共 API 必须有清晰 Javadoc（中文为主，适量英文术语）；
> - 所有非 trivial 方法必须有单元测试，关键方法应兼顾边界条件与异常场景；
> - 尽量采用 Java 21 现代风格（record、sealed、pattern matching 等）与函数式风格；
> - 禁止在本模块引入任何与具体业务强耦合的概念（保持通用）。

---

## 2.0 信息索引（包 / 类 / 小节导航）

> 本小节作为 `backend-common` 的信息索引，帮助 AI Agent 和人类快速定位某个类/方法在文档中的说明位置。版本与依赖矩阵仍以 `1-基础骨架搭建-TODO.md` 为准，这里只索引包与类。

- `io.github.faustofanb.admin.common.api`

  - `ApiResponse<T>`：统一响应模型（见 2.2.1）。
  - `PageRequest` / `PageResponse<T>` / `SortOrder`：分页与排序模型（见 2.2.2）。

- `io.github.faustofanb.admin.common.error`

  - `ErrorCategory`：错误大类枚举（见 2.3.1）。
  - `ErrorCode`：错误码枚举与查找工具（见 2.3.1）。

- `io.github.faustofanb.admin.common.exception`

  - `BusinessException`：通用业务异常（见 2.3.2）。
  - `ValidationException`：领域/应用校验异常（见 2.3.2）。
  - `SystemException`：系统级异常（见 2.3.2）。
  - `ExceptionTranslator`：异常到 `ApiResponse` 的转换工具（见 2.3.2）。

- `io.github.faustofanb.admin.common.context`

  - `AppContext`：请求/租户上下文（见 2.4.1）。
  - `AppContextHolder`：基于 ThreadLocal 的上下文持有器（见 2.4.2）。
  - `AppContexts`：从 JWT 等来源构造 `AppContext` 的辅助类（见 2.4.1）。

- `io.github.faustofanb.admin.common.id`

  - `RequestIdGenerator`：请求 ID 生成工具（见 2.5.1）。
  - `SnowflakeIdGenerator`：长整型雪花 ID 生成器（见 2.5.1）。

- `io.github.faustofanb.admin.common.time`

  - `DateTimeUtils`：时间与时区工具（见 2.5.2）。

- `io.github.faustofanb.admin.common.concurrent`

  - `VirtualThreadExecutorFactory`：虚拟线程执行器工厂（规划于第 1 章，引用于本章）。
  - `ContextAwareExecutor`：上下文传播感知执行器（规划于第 1 章，引用于本章）。

- `io.github.faustofanb.admin.common.util`

  - `CollectionUtils`：集合与流操作工具（见 2.6.1）。
  - `Preconditions`：参数与状态校验工具（见 2.6.2）。

- `io.github.faustofanb.admin.common.keys`

  - `RedisKeyPrefixes`：Redis Key 前缀与拼接工具（见 2.8.1）。
  - `MqTopics`：消息 Topic/Tag 常量与组合工具（见 2.8.2）。

- `io.github.faustofanb.admin.common.test`（test 源集）
  - `TestDataFactory`：测试随机数据工厂（见 2.7.1）。
  - `VirtualThreadTestHelper`：虚拟线程测试辅助工具（见 2.7.2）。

---

## 2.1 模块总体结构与依赖约束

### 2.1.1 包结构约定

**目标**：统一 `backend-common` 内部包结构，便于 AI Agent 在正确位置创建类与方法；依赖版本与模块间依赖关系一律以 `doc/todo/1-基础骨架搭建-TODO.md` 为唯一真相源，本章只约束类/方法与实现风格。

**TODO**：

1. 包结构（仅列出本章涉及的部分）：

   - [ ] `io.github.faustofanb.admin.common.api` —— 对外 API 协议模型（响应包装、分页、排序等）；
   - [ ] `io.github.faustofanb.admin.common.error` —— 错误码、错误分类；
   - [ ] `io.github.faustofanb.admin.common.exception` —— 统一异常体系；
   - [ ] `io.github.faustofanb.admin.common.context` —— AppContext、多租户上下文持有器；
   - [ ] `io.github.faustofanb.admin.common.id` —— ID / RequestId 生成工具；
   - [ ] `io.github.faustofanb.admin.common.concurrent` —— 虚拟线程与上下文传播工具；
   - [ ] `io.github.faustofanb.admin.common.time` —— 时间/时区/时刻工具；

- [ ] `io.github.faustofanb.admin.common.util` —— 通用函数式 & 集合 & 校验工具；
- [ ] `io.github.faustofanb.admin.common.keys` —— Redis Key 与消息 Topic 前缀常量；
- [ ] `io.github.faustofanb.admin.common.test` —— 测试辅助工具（仅 test scope 使用）。

2. 依赖约束（本模块内必须遵守）：
   - [ ] 严禁在 `backend-common` 中依赖具体中间件（如 Redis/JPA/RocketMQ 等）；
   - [ ] 允许且**鼓励**依赖：
     - [ ] Java 标准库；
     - [ ] Guava（`com.google.guava:guava`，版本由父 POM 统一管理），用于集合工具、不可变集合、缓存少量通用工具方法；
     - [ ] Apache Commons Lang（`org.apache.commons:commons-lang3`），仅用于字符串与基础工具补充；
     - [ ] JUnit 5 / Mockito（test scope）。
   - [ ] 严禁依赖或使用：
     - [ ] Hutool 及其任何子模块（`cn.hutool:*`），包括在测试代码中也不可使用。

---

## 2.2 API 协议与分页模型（`common.api`）

### 2.2.1 响应包装扩展与工厂方法完善

> 第 1 章已定义基础 `ApiResponse<T>` 构想，这里在此基础上补充更多实用方法与 TDD 要求。

**类：** `io.github.faustofanb.admin.common.api.ApiResponse<T>`

**TODO（方法级）**：

1. 已在第 1 章规划的方法需要在实现前补充完整 Javadoc：

   - [ ] `static <T> ApiResponse<T> success(T data)`
   - [ ] `static <T> ApiResponse<T> success()`
   - [ ] `static <T> ApiResponse<T> failure(String code, String message)`
   - [ ] `ApiResponse<T> withTraceId(String traceId)`
   - 每个方法需要说明：
     - 入参与出参含义；
     - 线程安全性（record + 无状态工厂方法，本质上是线程安全的）；
     - 典型使用示例（可在 Javadoc 中给出简短示例代码片段）。

2. 新增便捷失败工厂方法：

   - [ ] `static <T> ApiResponse<T> failure(ErrorCode errorCode)`
     - 使用 `errorCode.code()` 与 `errorCode.messageTemplate()` 作为默认 message；
     - 若 `messageTemplate` 为 null，则可以使用一个通用提示（如 "error"）。
     - **TDD**：
       - [ ] `ApiResponseTest.shouldBuildFailureResponseFromErrorCode()`：
         - 构造一个 `ErrorCode` 测试枚举值；
         - 调用该方法，断言 code 与 message 与枚举匹配。
   - [ ] `static <T> ApiResponse<T> failure(ErrorCode errorCode, String overrideMessage)`
     - 允许覆盖默认 message；
     - TDD：
       - [ ] `ApiResponseTest.shouldOverrideMessageWhenProvided()`。

3. 新增 `boolean isSuccess()` 与 `boolean isFailure()`：

   - [ ] 成功条件可定义为：`"OK".equals(code)` 或采用统一常量；
   - [ ] 失败则为非成功；
   - **TDD**：
     - [ ] `ApiResponseTest.shouldIdentifySuccessAndFailure()`：创建 success/failure 响应，断言布尔标记正确。

4. 考虑与前端约定的字段命名兼容性：
   - 若前端期望 `msg` 而非 `message`，可以在 record 字段使用 `message`，但通过 `@JsonProperty("msg")` 等注解映射；
   - TODO：
     - [ ] 在类上标记合适的 JSON 序列化注解（基于实际选型 Jackson/Fastjson2 等）；
     - [ ] `ApiResponseJsonTest.shouldSerializeToExpectedJsonFields()`（组件测试，可使用 Jackson ObjectMapper）。

---

### 2.2.2 分页与排序模型

**目标**：为列表查询统一提供分页与排序模型，避免各模块自行发明一套。

**类：** `PageRequest` / `PageResponse<T>` / `SortOrder`

**TODO（类与方法）**：

1. `record PageRequest(int page, int size, List<SortOrder> sort)`：

   - [ ] 包路径：`io.github.faustofanb.admin.common.api`。
   - 字段约定：
     - `page`：从 1 开始的页码；
     - `size`：每页大小；
     - `sort`：排序条件列表，可为空或空列表。
   - 方法：
     - [ ] 校验工厂：`public static PageRequest of(int page, int size)`
       - 对 `page <= 0` / `size <= 0` 给出合理默认或抛出异常（与整体 API 风格保持一致，建议抛 `BusinessException` / `IllegalArgumentException`）；
       - TDD：
         - [ ] `PageRequestTest.shouldCreateValidRequest()`
         - [ ] `PageRequestTest.shouldRejectNonPositivePageOrSize()`。
     - [ ] `public int offset()`
       - 返回 `(page - 1) * size`，供底层分页计算使用；
       - 注意整型溢出，可使用 long 计算再裁剪；
       - TDD：
         - [ ] `PageRequestTest.shouldCalculateOffsetCorrectly()`。

2. `record SortOrder(String property, Direction direction)`：

   - [ ] 嵌套枚举 `Direction { ASC, DESC }`；
   - [ ] 提供静态工厂方法：
     - [ ] `static SortOrder asc(String property)`
     - [ ] `static SortOrder desc(String property)`
   - TDD：
     - [ ] `SortOrderTest.shouldBuildAscAndDescCorrectly()`。

3. `record PageResponse<T>(List<T> records, long total, int page, int size)`：

   - [ ] 提供静态工厂：
     - [ ] `static <T> PageResponse<T> of(List<T> records, long total, PageRequest pageRequest)`
   - [ ] 方法：
     - [ ] `int totalPages()`：计算总页数（注意除法与进位）；
   - **TDD**：
     - [ ] `PageResponseTest.shouldCalculateTotalPages()`；
     - [ ] `PageResponseTest.shouldHoldPagedData()`。

4. TDD 与使用约定：
   - [ ] 在 `backend-common` 的测试中编写一个简单的“分页转换示例”：
     - 模拟从 DAO 层拿到 `List<Domain>` + `total`，使用 `PageResponse` 转换为返回给 Controller 的 DTO 分页；
     - 该示例测试命名如 `PageModelUsageExampleTest`，用于后续模块参考。

---

## 2.3 错误码与异常体系（`common.error` / `common.exception`）

> 在第 1 章中已经给出 `ErrorCode` 与 `BusinessException` 的 TODO，这里进一步细化错误分类、分层异常与 TDD 要求。

### 2.3.1 错误码分层设计

**类：** `ErrorCategory`、`ErrorCode`（扩展）

**TODO**：

1. 定义错误类别枚举 `ErrorCategory`：

   - [ ] 枚举值建议：
     - `AUTH`（认证）
     - `PERMISSION`（授权/访问控制）
     - `VALIDATION`（参数校验）
     - `BUSINESS`（业务冲突/规则）
     - `RATE_LIMIT`（限流与熔断）
     - `SYSTEM`（系统内部错误）
     - `INTEGRATION`（调用外部系统/MQ/DB 出错）
   - [ ] 字段：
     - [ ] `String prefix()`，如 `"AUTH"`、`"PERM"` 等；
   - [ ] TDD：
     - [ ] `ErrorCategoryTest.shouldExposeCodePrefix()`。

2. 扩展 `ErrorCode` 枚举：

   - [ ] 与 `ErrorCategory` 关联：
     - 字段增加：`ErrorCategory category`；
     - `String code` 可以由 `category.prefix() + "_..."` 组成，或在枚举中直接给出最终 code；
   - [ ] 提供静态查找方法：
     - [ ] `static Optional<ErrorCode> fromCode(String code)`
       - 将字符串 code 映射回枚举实例（用于反序列化或外部系统）；
       - TDD：
         - [ ] `ErrorCodeTest.shouldFindByCode()`；
         - [ ] `ErrorCodeTest.shouldReturnEmptyForUnknownCode()`。

3. 对常用错误码给出建议枚举值清单（非代码实现，但要在文档中列出，便于统一）：
   - [ ] 认证类：`AUTH_INVALID_TOKEN`、`AUTH_TOKEN_EXPIRED`、`AUTH_BAD_CREDENTIALS`、`AUTH_ACCOUNT_LOCKED`；
   - [ ] 授权类：`PERM_FORBIDDEN`；
   - [ ] 校验类：`VALIDATION_FAILED`；
   - [ ] 业务类：`BIZ_CONFLICT`、`BIZ_ILLEGAL_STATE`；
   - [ ] 限流类：`RATE_LIMITED`；
   - [ ] 系统类：`SYS_INTERNAL_ERROR`、`SYS_DEPENDENCY_UNAVAILABLE`；
   - [ ] 集成类：`INTEGRATION_TIMEOUT`、`INTEGRATION_ERROR` 等。

---

### 2.3.2 统一异常抽象与子类

**类：** `BusinessException`、`ValidationException`、`SystemException` 等

**TODO（方法级）**：

1. `BusinessException` 已在第 1 章规划，这里补充：

   - [ ] 增加 `Map<String, Object> attributes` 字段（可选），用于存放关键上下文信息（仅用于日志/内部诊断，不自动暴露给前端）；
   - [ ] 方法：
     - [ ] `BusinessException withAttribute(String key, Object value)` —— 返回带附加属性的新异常或在当前实例上追加（需权衡不可变性）；
     - [ ] `Map<String, Object> getAttributes()`；
   - **TDD**：
     - [ ] `BusinessExceptionTest.shouldStoreAttributes()`。

2. 定义 `ValidationException`：

   - [ ] 用于在领域/应用层主动抛出业务校验失败（区别于 Spring 的 `MethodArgumentNotValidException`）；
   - [ ] 字段：
     - [ ] `List<String> fieldErrors`（或结构化 `List<FieldError>`）；
   - [ ] 方法：
     - [ ] 静态工厂：`static ValidationException of(ErrorCode code, List<String> fieldErrors)`；
   - **TDD**：
     - [ ] `ValidationExceptionTest.shouldHoldFieldErrors()`。

3. 定义 `SystemException`：

   - [ ] 表达非预期、不可恢复的内部错误（外部依赖故障、编程错误等）；
   - [ ] 构造方法：
     - [ ] `SystemException(String message, Throwable cause)`；
   - [ ] 默认使用 `ErrorCode.SYS_INTERNAL_ERROR`；
   - **TDD**：
     - [ ] `SystemExceptionTest.shouldWrapUnexpectedErrors()`。

4. 为异常层提供一个简单的“映射到 ApiResponse”工具（可选，最终由 `GlobalExceptionHandler` 使用）：
   - [ ] 类：`ExceptionTranslator`；
   - [ ] 方法：
     - [ ] `public static ApiResponse<?> toResponse(Throwable ex, String traceId)`；
   - **TDD**：
     - [ ] `ExceptionTranslatorTest.shouldTranslateBusinessException()`；
     - [ ] `ExceptionTranslatorTest.shouldTranslateSystemException()`；
     - [ ] `ExceptionTranslatorTest.shouldTranslateUnknownThrowableAsSysError()`。

---

## 2.4 多租户上下文与安全基础（`common.context`）

> 在第 1 章已规划 `AppContext` 与 `AppContextHolder`，本节补充：与安全信息的扩展、构造辅助、TDD 细节。

### 2.4.1 AppContext 扩展

**类：** `AppContext`

**TODO**：

1. 评估是否需要额外字段：

   - [ ] `String clientId`（前端/调用方标识，可选）；
   - [ ] `String locale`（语言偏好，可选）；
   - 若添加字段，应：
     - [ ] 更新构造测试 `AppContextTest`；
     - [ ] 更新所有使用 AppContext 的工具类与测试（如 `LoggingContextTest`）。

2. 提供构造辅助工具 `AppContexts`：
   - [ ] 包路径：`io.github.faustofanb.admin.common.context`；
   - [ ] 方法（行为必须确定，不允许实现时自行选择策略）：
     - [ ] `public static AppContext fromJwtClaims(Map<String, Object> claims, String requestId, String traceId)`
       - 约定从 `claims` 中读取：`"tid"` → tenantId，`"sub"` → userId；
       - 若 `claims` 为空、缺失 `tid` 或 `sub`，一律抛出 `ValidationException`（使用错误码 `ErrorCode.AUTH_INVALID_TOKEN` 或等价码）；
       - 严禁返回“匿名/游客上下文”规避错误；
       - **TDD**：
         - [ ] `AppContextsTest.shouldBuildContextFromValidClaims()`；
         - [ ] `AppContextsTest.shouldRejectMissingTenantOrUser()`（断言抛出 `ValidationException` 且携带合适错误码）。

---

### 2.4.2 上下文传播策略占位

> 具体传播在 `ContextAwareExecutor`、Web Filter、Dubbo Filter 等处完成，本节只补充必要的策略标记与工具约定。

**TODO**：

1. 文档层面约定：

   - [ ] `AppContextHolder` **只在入口处设置一次**，并在出口处清理；
   - [ ] 异步/线程切换场景必须使用 `ContextAwareExecutor` 或等价封装；
   - [ ] 禁止在业务代码中随意修改 `AppContext`，统一通过认证/网关逻辑设置。

2. 在测试中验证“跨线程不共享上下文”：
   - [ ] `AppContextHolderIsolationTest.shouldNotLeakContextAcrossThreads()`：
     - 在线程 A 设置上下文；
     - 在线程 B 直接读取，应为 null 或 empty；
     - 以确保 ThreadLocal 行为符合预期。

---

## 2.5 通用 ID 与时间工具（`common.id` / `common.time`）

### 2.5.1 ID 生成工具

**类：** `RequestIdGenerator`（已在第 1 章规划）、`SnowflakeIdGenerator`（如需要长整型 ID）

**TODO**：

1. 完善 `RequestIdGenerator`：

   - [ ] 确定是否需要前缀（如 `REQ-`）；
   - [ ] 确保线程安全（使用无状态方法即可）。

2. 新增 `SnowflakeIdGenerator`（**必须实现**）：
   - [ ] 支持按 workerId / datacenterId 生成 long 型 ID；
   - [ ] 方法：
     - [ ] `public synchronized long nextId()`（默认实现使用同步保证线程安全，后续如需更高并发可在不改变签名的前提下优化）；
   - **TDD**：
     - [ ] `SnowflakeIdGeneratorTest.shouldGenerateIncreasingIds()`；
     - [ ] `SnowflakeIdGeneratorTest.shouldBeThreadSafe()`（多线程并发生成，检查重复）。

---

### 2.5.2 时间与时区工具

**类：** `DateTimeUtils`

**TODO**：

1. 包路径：`io.github.faustofanb.admin.common.time`。
2. 方法建议：
   - [ ] `public static Instant nowUtc()` —— 返回 `Instant.now()`，但语义明确为 UTC；
   - [ ] `public static ZonedDateTime nowAtZone(ZoneId zone)`；
   - [ ] `public static String formatIso(Instant instant)` —— 统一 ISO8601 格式字符串；
   - [ ] `public static Instant parseIso(String text)`；

- [ ] `public static ZonedDateTime toUserZone(Instant instant, ZoneId userZone)`（必须实现，作为 API 返回前的时区转换基础工具）；
- **TDD**：
  - [ ] `DateTimeUtilsTest.shouldFormatAndParseIso()`；
  - [ ] `DateTimeUtilsTest.shouldConvertToUserZone()`。

> 要与设计文档中“统一使用 UTC 存储，API 返回 ISO8601 字符串”保持一致。

---

## 2.6 函数式与集合工具（`common.util`）

**目标**：为各模块提供一套轻量、类型安全的函数式与集合工具，避免重复造轮子，并鼓励使用现代 Java 函数式风格。

### 2.6.1 集合工具

**类：** `CollectionUtils`

**TODO（示例方法，可按需扩展）**：

1. 空安全判断：

   - [ ] `public static <T> boolean isNullOrEmpty(Collection<T> collection)`；
   - [ ] `public static <T> boolean isNotEmpty(Collection<T> collection)`；
   - **TDD**：
     - [ ] `CollectionUtilsTest.shouldJudgeEmptyAndNonEmptyCollections()`。

2. 不可变集合构造：

   - [ ] `public static <T> List<T> immutableListOf(T... elements)`；
   - [ ] `public static <K,V> Map<K,V> immutableMapOf(K k1, V v1, K k2, V v2, ...)`（可使用 `Map.of` 代理）；
   - **TDD**：
     - [ ] `CollectionUtilsTest.shouldCreateImmutableCollections()`（尝试修改应抛异常）。

3. 分组与索引构建（基于 Stream）：
   - [ ] `public static <T, K> Map<K, List<T>> groupBy(Collection<T> items, Function<T, K> keyExtractor)`；
   - [ ] `public static <T, K> Map<K, T> indexBy(Collection<T> items, Function<T, K> keyExtractor)`；
   - **TDD**：
     - [ ] `CollectionUtilsTest.shouldGroupByKeyExtractor()`；
     - [ ] `CollectionUtilsTest.shouldIndexByKeyExtractor()`（注意重复 key 的处理策略，要在 Javadoc 中说明）。

---

### 2.6.2 校验与断言工具

**类：** `Preconditions`

**TODO**：

1. 方法（异常类型与行为统一约定）：

   - [ ] `public static <T> T notNull(T value, String message)`；
     - 当 `value == null` 时，必须抛出 `IllegalArgumentException`，异常消息为传入的 `message`；
   - [ ] `public static void isTrue(boolean expression, String message)`；
     - 当 `expression == false` 时，必须抛出 `IllegalArgumentException`，异常消息为传入的 `message`；
   - [ ] `public static void state(boolean expression, String message)` —— 表达状态不合法；
     - 当 `expression == false` 时，必须抛出 `IllegalStateException`；
   - **TDD**：
     - [ ] `PreconditionsTest.shouldThrowIllegalArgumentOnNull()`；
     - [ ] `PreconditionsTest.shouldThrowIllegalArgumentOnFalseExpression()`；
     - [ ] `PreconditionsTest.shouldThrowIllegalStateOnInvalidState()`；
     - [ ] `PreconditionsTest.shouldReturnValueOnValidInput()`。

2. 注意：
   - 抛出的异常类型应与整体风格统一，可使用 `IllegalArgumentException` / `IllegalStateException`，或自定义异常；
   - 这些方法在领域层与应用层被广泛使用，是 TDD 时编写前置条件检查的基础。

---

## 2.7 测试辅助工具（`common.test`）

**目标**：在 test scope 下为各模块提供公共测试工具，降低重复测试代码；同时为 AI Agent 提供“如何写高质量单测”的参考实现。

### 2.7.1 随机数据与断言辅助

**类：** `TestDataFactory`

**TODO**：

1. 包路径：`io.github.faustofanb.admin.common.test`（仅 test 源集）；
2. 示例方法：
   - [ ] `public static String randomUsername()`；
   - [ ] `public static String randomEmail()`；
   - [ ] `public static String randomTenantId()`；
   - [ ] `public static AppContext randomAppContext()`；
   - **TDD**：
     - [ ] `TestDataFactoryTest.shouldGenerateNonEmptyValues()`。

---

### 2.7.2 虚拟线程测试工具

**类：** `VirtualThreadTestHelper`

**TODO**：

1. 提供在测试中便捷运行虚拟线程任务的方法：
   - [ ] `public static void runInVirtualThread(Runnable task)`；
   - [ ] `public static <T> T callInVirtualThread(Callable<T> task)`；
   - **TDD**：
     - [ ] `VirtualThreadTestHelperTest.shouldRunTaskInVirtualThread()` —— 验证线程名与结果。

> 这些工具将帮助后续模块在测试中更自然地覆盖虚拟线程与上下文传播逻辑。

---

## 2.8 Redis Key 与消息 Topic 前缀常量（`common.keys`）

> 统一规范 Redis Key 与 MQ Topic/Tag 命名，避免硬编码字符串散落在各个模块中，同时确保多租户维度在缓存与消息系统中得以体现。

### 2.8.1 Redis Key 前缀约定

**类：** `RedisKeyPrefixes`

**TODO**：

1. 包路径：`io.github.faustofanb.admin.common.keys`。
2. 设计原则：

- [ ] 所有 Redis Key 必须以租户维度开头，例如：`"t:" + tenantId + ":..."`；
- [ ] 尽量采用「领域:实体:标识」结构，例如 `t:{tenantId}:user:{userId}`、`t:{tenantId}:perm:cache:{userId}`；
- [ ] 前缀常量只定义模板，不直接拼接具体 ID，避免在 common 模块中引入具体业务 ID 概念。

3. 字段示例（仅常量，不含业务 ID）：

- [ ] `public static final String TENANT_PREFIX = "t:";` —— 所有 Key 以该前缀开始；
- [ ] `public static final String USER_CACHE_PREFIX = "user:";` —— 用户相关缓存二级前缀；
- [ ] `public static final String PERMISSION_CACHE_PREFIX = "perm:";`；
- [ ] `public static final String MENU_CACHE_PREFIX = "menu:";`；
- [ ] `public static final String RESOURCE_CACHE_PREFIX = "res:";`；
- [ ] `public static final String EXCEL_IMPORT_PREFIX = "excel:import:";`；
- [ ] `public static final String RATE_LIMIT_PREFIX = "rate:";`；
- [ ] 以上命名需与 `doc/design/5.缓存与限流策略.md` 中的 Key 规范保持一致，如有差异需先更新设计文档。

4. 提供便捷拼接方法（不直接依赖具体业务实体，只按通用维度）：

- [ ] `public static String tenantScoped(String tenantId)` → 返回 `"t:" + tenantId + ":"`；
- [ ] `public static String tenantUser(String tenantId, String userId)` → 返回 `"t:" + tenantId + ":user:" + userId`；
- [ ] `public static String tenantPermissionCache(String tenantId, String userId)`；
- [ ] `public static String tenantRateLimitKey(String tenantId, String resourceCode)` → 如 `"t:{tenantId}:rate:{resourceCode}"`；
- **TDD**：
  - [ ] `RedisKeyPrefixesTest.shouldBuildTenantScopedPrefix()`；
  - [ ] `RedisKeyPrefixesTest.shouldBuildUserCacheKey()`；
  - [ ] `RedisKeyPrefixesTest.shouldBuildPermissionCacheKey()`；
  - [ ] `RedisKeyPrefixesTest.shouldBuildRateLimitKey()`。

5. 注意：

- [ ] 方法必须对 `tenantId` / `userId` 为空的情况进行前置条件校验（使用 `Preconditions.notNull`），防止生成非法 Key；若收到空值，必须抛出 `IllegalArgumentException`；
- [ ] Javadoc 中应明确：这些方法仅做字符串拼接，不做 Redis 访问逻辑。

---

### 2.8.2 消息 Topic 与 Tag 前缀约定

**类：** `MqTopics`

**TODO**：

1. 包路径：`io.github.faustofanb.admin.common.keys`。
2. 设计原则：

- [ ] Topic 命名遵循设计文档（事件总线章节）中的命名，如 `user.events`、`tenant.events`、`resource.events` 等；
- [ ] Tag 或子主题可以采用 `租户无关的业务子类目`，租户信息通常放在消息体/AppContext 中，而非 Topic 名中；
- [ ] common 模块内只定义常量，不依赖具体 MQ 客户端类型。

3. 字段示例：

- [ ] `public static final String USER_EVENTS = "user.events";`
- [ ] `public static final String TENANT_EVENTS = "tenant.events";`
- [ ] `public static final String RESOURCE_EVENTS = "resource.events";`
- [ ] `public static final String PERMISSION_EVENTS = "permission.events";`
- [ ] `public static final String EXCEL_EVENTS = "excel.events";`

4. 可根据需要提供 Tag 常量：

- [ ] `public static final String TAG_CREATED = "created";`
- [ ] `public static final String TAG_UPDATED = "updated";`
- [ ] `public static final String TAG_DELETED = "deleted";`

5. 可选工具方法：

- [ ] `public static String withTag(String topic, String tag)` → 拼接成 MQ 客户端需要的格式（如 RocketMQ 的 `topic:tag`），但要避免与具体实现过度耦合；
- **TDD**：
  - [ ] `MqTopicsTest.shouldExposeAllTopics()`；
  - [ ] `MqTopicsTest.shouldBuildTopicWithTag()`。

---

## 2.9 TDD 与质量要求（针对 `backend-common` 模块）

**总体要求**：

1. 覆盖率：

   - [ ] 语句/指令覆盖率 ≥ 80%；
   - [ ] 分支覆盖率 ≥ 70%；
   - [ ] 对关键核心类（`ApiResponse`、`AppContextHolder`、`ContextAwareExecutor`、`Preconditions` 等）应尽量达到 90%+。

2. 测试风格：

   - [ ] 使用 JUnit 5 + AssertJ（或 Hamcrest）提高断言可读性；
   - [ ] 测试方法命名应清晰表达行为：`shouldXxxWhenYyy`；
   - [ ] 每个类至少有一个对应的 `*Test` 类，放在同包路径下。

3. TDD 流程：

   - [ ] 新增公共 API 时，先在测试中写出期望行为；
   - [ ] 通过最小实现让测试通过；
   - [ ] 重构时保持测试绿灯，必要时补充回归测试。

4. 文档同步：
   - [ ] 对于 `backend-common` 中的重要工具类和模式，应在 `doc/design` 或 `doc/todo` 后续章节中引用/链接，保持“文档 ←→ 代码”一致。

---

> 本章是整个项目的通用基石。AI Agent 在实现 `backend-common` 时，务必逐条对照本 TODO 清单执行，并保持：
>
> - 先写测试，再写实现；
> - 方法级注释清晰；
> - 不与具体业务耦合，只提供通用、可复用的能力。
