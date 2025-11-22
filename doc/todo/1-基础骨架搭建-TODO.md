# 第 1 章 基础骨架搭建 TODO 清单（AI Agent 实施指引）

> 本文档定位：**AI 代码生成 Agent 的详细实施指南**。
> 要求：
>
> - 细粒度打到「类级 / 方法级」粒度，便于逐条实现与校验；
> - 全程遵循 TDD 思想，强调先设计测试再实现代码；
> - 所有关键方法都应具备高质量单元测试与必要的组件/集成测试；
> - 严格对齐 `README.md` 与 `doc/design` 下总体/各专题设计文档。
>
> 本章目标：搭建完整的 **Maven 多模块骨架 + 公共基础设施 + 虚拟线程执行环境 + 基础可观测性**，为后续各业务模块实现提供稳定、统一的技术底座。

---

## 1.1 顶层 Maven 工程与模块结构

### 1.1.1 创建父 POM 工程

**目标**：建立统一的父 `pom.xml`，管理所有子模块依赖版本、Java 编译选项与通用插件配置。

**TODO 列表（方法/配置级）**：

1. 创建根 `pom.xml`，定义以下核心内容（所有版本号必须**与本小节列出的值完全一致**，禁止使用“最新稳定版”等模糊描述）：

   - [ ] 在 `<properties>` 中声明统一版本（**版本矩阵单一真相源**）：

     - [ ] `java.version=21`
     - [ ] `spring.boot.version=4.0.0`
     - [ ] `spring.cloud.version=2024.0.0`
     - [ ] `dubbo.version=3.2.12`
     - [ ] `jimmer.version=0.9.110`
     - [ ] `redisson.version=3.37.0`
     - [ ] `rocketmq.version=5.3.3`
     - [ ] `seata.version=2.5.0`
     - [ ] `sentinel.version=1.8.9`
     - [ ] `powerjob.version=4.3.6`
     - [ ] `otel.version=1.41.0`（OpenTelemetry API/SDK 主版本）
     - [ ] `mysql.version=8.4.0`
     - [ ] `postgresql.version=42.7.4`
     - [ ] `arthas.version=4.1.1`
     - [ ] 测试相关版本（由父 POM 统一管理）：

       - [ ] `junit.jupiter.version=5.10.3`
       - [ ] `mockito.version=5.12.0`
       - [ ] `testcontainers.version=1.20.1`
       - [ ] `assertj.version=3.26.0`

   - [ ] 在 `<dependencyManagement>` 中：

     - [ ] 引入 Spring 体系 BOM：
       - [ ] `org.springframework.boot:spring-boot-dependencies:${spring.boot.version}`（import scope）
       - [ ] `org.springframework.cloud:spring-cloud-dependencies:${spring.cloud.version}`（import scope）
     - [ ] 如有 Dubbo 官方 BOM，可统一声明：
       - [ ] `org.apache.dubbo:dubbo-bom:${dubbo.version}`（import scope，如使用）
     - [ ] 明确声明以下核心依赖的统一版本（即使后续由各模块直接依赖，也需在此集中声明）：
       - [ ] ORM：`org.babyfish.jimmer:jimmer-core:${jimmer.version}` 及 `jimmer-spring-boot-starter`
       - [ ] Redis 客户端：`org.redisson:redisson-spring-boot-starter:${redisson.version}`
       - [ ] 消息队列：`org.apache.rocketmq:rocketmq-spring-boot-starter:${rocketmq.version}`
       - [ ] 分布式事务：`io.seata:seata-spring-boot-starter:${seata.version}`
       - [ ] 限流熔断：`com.alibaba.csp:sentinel-transport-simple-http:${sentinel.version}` 及 Spring Cloud Alibaba Sentinel 相关依赖（若需要可在此预留）
       - [ ] 调度平台：`tech.powerjob:powerjob-worker-spring-boot-starter:${powerjob.version}`
       - [ ] 可观测性：
         - [ ] `io.micrometer:micrometer-core`（版本由 Spring Boot BOM 决定，不再单独声明）
         - [ ] `io.micrometer:micrometer-registry-prometheus`
         - [ ] `io.opentelemetry:opentelemetry-api:${otel.version}`
         - [ ] `io.opentelemetry:opentelemetry-sdk:${otel.version}`
       - [ ] 数据库驱动：
         - [ ] `mysql:mysql-connector-j:${mysql.version}`
         - [ ] `org.postgresql:postgresql:${postgresql.version}`
       - [ ] 测试依赖（由 dependencyManagement 管理版本，在各模块按需引入）：
         - [ ] `org.junit.jupiter:junit-jupiter:${junit.jupiter.version}`
         - [ ] `org.mockito:mockito-core:${mockito.version}`
         - [ ] `org.testcontainers:testcontainers:${testcontainers.version}` 及 MySQL/PostgreSQL/Redis 模块
         - [ ] `org.assertj:assertj-core:${assertj.version}`
       - [ ] 调试诊断：`com.taobao.arthas:arthas-spring-boot-starter:${arthas.version}`（仅在非生产环境启用）

   - [ ] 在 `<build>` 中：
     - [ ] 配置 `maven-compiler-plugin`：
       - [ ] `source=21`，`target=21`
       - [ ] 启用参数名保留（`-parameters`）
     - [ ] 配置 `maven-surefire-plugin` 与 `maven-failsafe-plugin`：
       - [ ] 支持 JUnit 5
       - [ ] 能运行单元测试与集成测试
     - [ ] 配置 `jacoco-maven-plugin`：
       - [ ] 生成单测覆盖率报告
       - [ ] 预留覆盖率门槛设置（例如全局 70%+，关键模块更高）

2. 在父 POM 中声明各子模块：

   - [ ] `backend-common`
   - [ ] `backend-core`
   - [ ] `backend-infra`
   - [ ] `backend-transaction`
   - [ ] `backend-rpc-api`
   - [ ] `backend-rpc-impl`
   - [ ] `backend-gateway`
   - [ ] `backend-boot`
   - [ ] `module/backend-system`
   - [ ] `module/backend-file`
   - [ ] `module/backend-batch`
   - [ ] `module/backend-schedule`
   - [ ] `module/backend-audit`

3. 为父 POM 添加 **通用依赖**（scope=import 或 dependencyManagement 管理即可）：

   - [ ] 日志：`logback` / `log4j2`，并确保可输出 JSON 日志
   - [ ] Lombok（如需）——谨慎使用，优先 Java 21 record
   - [ ] Micrometer、Prometheus 依赖
   - [ ] OpenTelemetry instrumentation 基础依赖

4. TDD 要求：
   - [ ] 为 `pom.xml` 的关键配置编写 **最小验证测试步骤文档**（可以是 markdown），说明如何：
     - [ ] 执行 `mvn test` 验证 JUnit5 是否生效
     - [ ] 执行 `mvn verify` 验证 Jacoco 报告是否生成
   - [ ] 将上述验证步骤写入本 TODO 文档或单独 `doc/todo/环境验证.md`（后续章节补充）。

> 说明：本节主要是配置层工作，不直接产生 Java 方法，但后续所有模块的方法级实现都依赖于此处的正确配置，因此必须详细记录和验证。

---

### 1.1.2 依赖与模块映射表（用于生成各模块 pom.xml）

**目标**：将上一小节中定义的版本矩阵，具体映射到每个 Maven 子模块，指导 AI Agent 在生成各模块 `pom.xml` 时**精确引入所需依赖**，避免猜测。

**TODO 列表（模块级）**：

1. 在父 POM 的说明注释中，补充如下「模块–依赖」映射表（也可在本 TODO 文档中维护），要求：

   - [ ] `backend-common`（通用基础模块）：

     - [ ] 直接依赖：
       - [ ] `org.springframework.boot:spring-boot-starter`（基础 Spring 栈）
       - [ ] `org.springframework.boot:spring-boot-starter-validation`（Bean Validation）
       - [ ] `io.micrometer:micrometer-core`（由 BOM 管理版本）
       - [ ] `io.opentelemetry:opentelemetry-api`
       - [ ] `io.opentelemetry:opentelemetry-sdk`
       - [ ] 测试栈：`junit-jupiter`、`mockito-core`、`assertj-core`
     - [ ] 主要职责：放置 `ApiResponse`、`ErrorCode`、`BusinessException`、`GlobalExceptionHandler`、`AppContext` 等通用类。

   - [ ] `backend-core`（领域与应用核心）：

     - [ ] 直接依赖：
       - [ ] `org.springframework.boot:spring-boot-starter`（基础容器）
       - [ ] `org.springframework.boot:spring-boot-starter-validation`
       - [ ] `org.babyfish.jimmer:jimmer-spring-boot-starter`
       - [ ] 数据库驱动：`mysql-connector-j` 或 `postgresql`（按实际选型二选一）
       - [ ] `io.seata:seata-spring-boot-starter`
       - [ ] 测试栈：`junit-jupiter`、`testcontainers` 数据库模块
     - [ ] 主要职责：聚合系统内通用领域模型与应用服务（不含具体业务模块）。

   - [ ] `backend-infra`（基础设施适配）：

     - [ ] 直接依赖：
       - [ ] `org.springframework.boot:spring-boot-starter`
       - [ ] `org.redisson:redisson-spring-boot-starter`
       - [ ] `org.apache.rocketmq:rocketmq-spring-boot-starter`
       - [ ] `tech.powerjob:powerjob-worker-spring-boot-starter`
       - [ ] `com.alibaba.csp:sentinel-transport-simple-http`
       - [ ] `io.micrometer:micrometer-registry-prometheus`
       - [ ] `io.opentelemetry:opentelemetry-api` / `sdk`
       - [ ] 测试栈：`junit-jupiter`、`testcontainers`（Redis / RocketMQ）
     - [ ] 主要职责：统一封装 Redis、消息队列、调度、限流熔断等基础设施细节。

   - [ ] `backend-transaction`（分布式事务与一致性）：

     - [ ] 直接依赖：
       - [ ] `org.springframework.boot:spring-boot-starter`
       - [ ] `io.seata:seata-spring-boot-starter`
       - [ ] 事件/Outbox 需要使用的消息组件（通过 `backend-infra` 暴露接口，必要时只依赖 API）
       - [ ] 测试栈：`junit-jupiter`、`testcontainers` + 嵌入式 DB
     - [ ] 主要职责：统一封装 Seata、Outbox、补偿任务等事务相关能力。

   - [ ] `backend-rpc-api`（RPC 接口定义）：

     - [ ] 直接依赖：
       - [ ] `org.apache.dubbo:dubbo-spring-boot-starter`
       - [ ] `spring-boot-starter`（基础）
     - [ ] 主要职责：定义跨服务调用用到的 DTO / 接口，不依赖具体实现模块。

   - [ ] `backend-rpc-impl`（RPC 实现）：

     - [ ] 直接依赖：
       - [ ] `backend-rpc-api`
       - [ ] `backend-core` / 各业务模块（提供实际实现）
       - [ ] `org.apache.dubbo:dubbo-spring-boot-starter`
       - [ ] `spring-boot-starter` / `spring-boot-starter-web`
     - [ ] 主要职责：对外暴露 Dubbo 服务，实现接口、编排权限与租户上下文传递。

   - [ ] `backend-gateway`（HTTP/API 网关）：

     - [ ] 直接依赖：
       - [ ] `org.springframework.cloud:spring-cloud-starter-gateway`
       - [ ] `spring-boot-starter-actuator`
       - [ ] `com.alibaba.csp:sentinel-transport-simple-http`（如需网关侧限流）
       - [ ] 可观测性相关：`micrometer-registry-prometheus`、Otel HTTP 入口
     - [ ] 主要职责：对接前端 / 第三方入口，进行路由、鉴权、限流、观测埋点。

   - [ ] `backend-boot`（主应用聚合模块）：

     - [ ] 直接依赖：
       - [ ] 依赖所有需要作为同一进程运行的模块：`backend-common`、`backend-core`、`backend-infra`、`backend-transaction`、`module/*` 等
       - [ ] `spring-boot-starter-web` / `spring-boot-starter-actuator`
       - [ ] Otel、Micrometer、日志实现
     - [ ] 主要职责：提供主启动类、统一配置装配、健康检查与基础管理接口。

   - [ ] 业务模块（示例以 `module/backend-system` 为例）：
     - [ ] 直接依赖：
       - [ ] `backend-common`
       - [ ] `backend-core`
       - [ ] `backend-infra`（通过接口或 SPI 方式使用缓存 / MQ / 调度等能力）
       - [ ] `spring-boot-starter` / `spring-boot-starter-web`
       - [ ] 若有 RPC 能力：`backend-rpc-api`
       - [ ] 测试栈：`junit-jupiter`、`mockito-core`、`assertj-core`、`spring-boot-starter-test`
     - [ ] 主要职责：承载具体业务领域（系统管理、文件、批处理、调度、审计等）的应用与领域实现。

2. 为 Agent 写出一条明确规则：

   - [ ] 任何新建模块的 `pom.xml` 都必须：
     - [ ] 以父 POM 为 `<parent>`，仅在本模块声明**最小必要依赖**；
     - [ ] 所有版本号一律通过 `${...}` 属性引用，禁止在子模块中写死具体版本；
     - [ ] 首选依赖上文映射表中推荐的依赖组合，若需要新增依赖，必须先回到 1.1.1 更新版本矩阵与本映射表后再使用。

> 实施提示：后续章节在说明各模块内部类与方法时，可以直接引用本小节的模块–依赖映射，不再重复描述依赖细节。

---

## 1.2 模块目录结构与基础包命名

### 1.2.1 创建基础目录结构

**目标**：在文件系统与构建系统中，创建符合设计文档约定的模块/包结构，为后续代码实现提供清晰边界。

**TODO 列表**：

1. 为每个子模块创建基础目录：

   - [ ] `src/main/java`
   - [ ] `src/main/resources`
   - [ ] `src/test/java`
   - [ ] `src/test/resources`

2. 确定统一的包名前缀：

   - [ ] 使用 `io.github.faustofanb.admin` 作为根包名（与 README 保持一致）。
   - [ ] 在每个模块下根据职责建立一级子包，例如：
     - [ ] `backend-common`：`io.github.faustofanb.admin.common`
     - [ ] `backend-core`：`io.github.faustofanb.admin.core`
     - [ ] `backend-infra`：`io.github.faustofanb.admin.infra`
     - [ ] `backend-transaction`：`io.github.faustofanb.admin.transaction`
     - [ ] `backend-gateway`：`io.github.faustofanb.admin.gateway`
     - [ ] `backend-boot`：`io.github.faustofanb.admin.boot`
     - [ ] `module/backend-system`：`io.github.faustofanb.admin.system`
     - [ ] ……（其他模块类似）

3. 为业务模块预创建标准分层包结构（以 `backend-system` 为例）：

   - [ ] `io.github.faustofanb.admin.system.app`
   - [ ] `io.github.faustofanb.admin.system.domain`
   - [ ] `io.github.faustofanb.admin.system.infra`
   - [ ] `io.github.faustofanb.admin.system.adapter`
   - [ ] `io.github.faustofanb.admin.system.config`

4. TDD 相关准备：
   - [ ] 在每个模块的 `src/test/java` 下创建与主代码包结构对齐的空测试包结构，例如：
     - [ ] `io.github.faustofanb.admin.system.app`（测试包）
     - [ ] `io.github.faustofanb.admin.system.domain`
     - [ ] ……
   - [ ] 在每个模块中预置一个最简单的「样例测试类」：
     - [ ] 类名示例：`ModuleStructureSmokeTest`
     - [ ] 方法：`shouldLoadModuleContext()`，用于后续 Spring Boot 上下文加载测试（可暂时留空或简单断言 true）。

---

## 1.3 backend-common：通用基础模块

### 1.3.1 统一响应模型与错误码体系

**目标**：为整个系统提供统一的 API 返回结构、错误码约定和异常基类，方便前后端及各模块间协作。

#### 1.3.1.1 类与方法 TODO

1. 创建响应包装类 `ApiResponse<T>`（建议使用 `record`）：

   - [ ] 包路径：`io.github.faustofanb.admin.common.api`。
   - [ ] 字段：
     - [ ] `String code`：业务码（如 `AUTH_INVALID_TOKEN`、`SYS_INTERNAL_ERROR`）。
     - [ ] `String message`：人类可读描述。
     - [ ] `String traceId`：链路追踪 ID。
     - [ ] `T data`：业务数据。
   - [ ] 方法级 TODO：
     - [ ] `static <T> ApiResponse<T> success(T data)`
       - 返回成功响应，`code` 使用统一成功码（如 `OK`），`message` 可为 `"success"`。
       - TDD：
         - [ ] `ApiResponseTest.shouldBuildSuccessResponseWithData()`
           - 给定 data，不为空；断言 code=OK，message=success，data 为传入值。
     - [ ] `static <T> ApiResponse<T> success()`
       - 返回无 data 的成功响应（`data=null`）。
       - TDD：
         - [ ] `ApiResponseTest.shouldBuildSuccessResponseWithoutData()`
     - [ ] `static <T> ApiResponse<T> failure(String code, String message)`
       - 用于构建失败响应。
       - TDD：
         - [ ] `ApiResponseTest.shouldBuildFailureResponseWithCodeAndMessage()`
     - [ ] `ApiResponse<T> withTraceId(String traceId)`（如使用 record，可返回新实例）
       - 用于在已有响应上补充 traceId。
       - TDD：
         - [ ] `ApiResponseTest.shouldAttachTraceId()`

2. 定义错误码枚举 `ErrorCode`：

   - [ ] 包路径：`io.github.faustofanb.admin.common.error`。
   - [ ] 枚举值示例：
     - [ ] `AUTH_INVALID_TOKEN`
     - [ ] `AUTH_UNAUTHORIZED`
     - [ ] `PERM_FORBIDDEN`
     - [ ] `VALIDATION_FAILED`
     - [ ] `BIZ_CONFLICT`
     - [ ] `RATE_LIMITED`
     - [ ] `SYS_INTERNAL_ERROR`
   - [ ] 字段：
     - [ ] `String code`
     - [ ] `String messageTemplate`（可选，用于国际化或模板消息）
   - [ ] 方法级 TODO：
     - [ ] `String code()`（getter）
     - [ ] `String messageTemplate()`
     - [ ] `String format(Object... args)`：根据模板填充参数，返回格式化后的 message。
       - TDD：
         - [ ] `ErrorCodeTest.shouldFormatMessageWithArguments()`

3. 定义基础异常 `BusinessException`：

   - [ ] 包路径：`io.github.faustofanb.admin.common.exception`。
   - [ ] 字段：
     - [ ] `ErrorCode errorCode`
     - [ ] `String detail`（可选，存放业务详细信息，不直接暴露给前端）
   - [ ] 构造方法 TODO：
     - [ ] `BusinessException(ErrorCode errorCode)`
     - [ ] `BusinessException(ErrorCode errorCode, String detail)`
     - [ ] `BusinessException(ErrorCode errorCode, Throwable cause)`
   - [ ] 方法级 TODO：
     - [ ] `ErrorCode getErrorCode()`
     - [ ] `String getDetail()`
   - [ ] TDD：
     - [ ] `BusinessExceptionTest.shouldHoldErrorCodeAndDetail()`
     - [ ] `BusinessExceptionTest.shouldSupportWrappingCause()`

4. 定义全局异常处理器骨架 `GlobalExceptionHandler`：
   - [ ] 包路径：`io.github.faustofanb.admin.common.web`。
   - [ ] 使用 `@RestControllerAdvice` 注解。
   - [ ] 方法级 TODO：
     - [ ] `ApiResponse<?> handleBusinessException(BusinessException ex, WebRequest request)`
       - 映射为业务错误响应，使用 `ex.getErrorCode().code()` 作为 code。
       - 自动填充 traceId（从 MDC 或 Tracer 获取）。
       - TDD（组件测试）：
         - [ ] 使用 `MockMvc` 或 `WebTestClient` 编写 `GlobalExceptionHandlerTest.shouldHandleBusinessException()`，模拟抛出 `BusinessException` 的控制器，验证响应结构。
     - [ ] `ApiResponse<?> handleValidationException(MethodArgumentNotValidException ex, WebRequest request)`
       - 将参数校验错误映射为 `VALIDATION_FAILED`。
       - TDD：
         - [ ] `GlobalExceptionHandlerTest.shouldHandleValidationException()`
     - [ ] `ApiResponse<?> handleUnhandledException(Throwable ex, WebRequest request)`
       - 兜底处理，返回 `SYS_INTERNAL_ERROR`。
       - TDD：
         - [ ] `GlobalExceptionHandlerTest.shouldHandleUnhandledExceptionAsInternalError()`

> 要求：在实现上述类和方法前，**先编写相应的测试类与测试方法签名**，确保遵循 TDD；可以先让测试失败，然后逐步实现最小通过实现。

---

### 1.3.2 AppContext 与上下文持有器

**目标**：提供统一的请求/租户上下文对象，贯穿 HTTP / RPC / 任务等调用路径；为多租户隔离、安全审计与可观测性提供基础数据。

#### 1.3.2.1 类与方法 TODO

1. 定义 `AppContext` 记录类型：

   - [ ] 包路径：`io.github.faustofanb.admin.common.context`。
   - [ ] 使用 `record AppContext(String tenantId, String userId, String requestId, String traceId)`。
   - [ ] 方法级 TODO（record 可自动生成访问器）：
     - [ ] `String tenantId()`
     - [ ] `String userId()`
     - [ ] `String requestId()`
     - [ ] `String traceId()`
   - [ ] TDD：
     - [ ] `AppContextTest.shouldHoldAllFields()`：构造对象并断言各字段。

2. 定义 `AppContextHolder`：

   - [ ] 包路径：`io.github.faustofanb.admin.common.context`。
   - [ ] 内部使用 `ThreadLocal<AppContext>` 存储当前上下文。
   - [ ] 方法级 TODO：
     - [ ] `static void set(AppContext ctx)`
       - 设置当前线程上下文。
       - TDD：
         - [ ] `AppContextHolderTest.shouldSetAndGetContextInSameThread()`
     - [ ] `static AppContext get()`
       - 获取当前线程上下文，若无可返回 null 或抛出自定义异常（需在设计中明确，推荐返回 Optional）。
       - TDD：
         - [ ] `AppContextHolderTest.shouldReturnNullOrEmptyWhenNoContext()`
     - [ ] `static Optional<AppContext> getOptional()`（可选）
       - 以 Optional 形式返回，减少 NPE 风险。
     - [ ] `static void clear()`
       - 清除当前线程上下文，防止线程复用带来的污染。
       - TDD：
         - [ ] `AppContextHolderTest.shouldClearContext()`

3. 定义用于生成 `requestId` 的工具类 `RequestIdGenerator`：
   - [ ] 包路径：`io.github.faustofanb.admin.common.id`。
   - [ ] 方法级 TODO：
     - [ ] `static String next()`
       - 生成全局唯一请求 ID，可基于 UUID 或雪花算法；初期可采用简化实现（如 `UUID.randomUUID().toString()`）。
       - TDD：
         - [ ] `RequestIdGeneratorTest.shouldGenerateNonEmptyId()`
         - [ ] `RequestIdGeneratorTest.shouldGenerateDifferentIdsForMultipleCalls()`

> 注意：后续在虚拟线程与上下文传播工具中会大量使用 `AppContext` 与 `AppContextHolder`，必须先保证其正确性与线程安全行为。

---

### 1.3.3 虚拟线程执行器与上下文传播工具

**目标**：统一封装基于 Java 21 虚拟线程的执行器，并支持 `AppContext` 等 ThreadLocal 在任务提交与执行之间的安全传播，避免上下文丢失。

#### 1.3.3.1 类：`VirtualThreadExecutorFactory`

1. 创建工厂类 `VirtualThreadExecutorFactory`：
   - [ ] 包路径：`io.github.faustofanb.admin.common.concurrent`。
   - [ ] 方法级 TODO：
     - [ ] `public static ExecutorService newVirtualThreadPerTaskExecutor(String namePrefix)`
       - 使用 `Executors.newThreadPerTaskExecutor` + `Thread.ofVirtual().name(namePrefix, 0).factory()` 创建虚拟线程执行器。
       - 要求：线程名带有可读前缀（如 `biz-vt-`）。
       - TDD：
         - [ ] `VirtualThreadExecutorFactoryTest.shouldCreateExecutorAndRunTask()`
           - 提交一个简单任务（如设置 AtomicBoolean），断言任务执行成功；
           - 通过当前线程名断言其包含指定前缀（可以在任务内部记录 `Thread.currentThread().getName()`）。

#### 1.3.3.2 类：`ContextAwareExecutor`

2. 创建上下文感知执行器 `ContextAwareExecutor`：

   - [ ] 包路径：`io.github.faustofanb.admin.common.concurrent`。
   - [ ] 核心设计：对外暴露一个封装了 `ExecutorService` 的类，提交任务时自动捕获当前线程的 `AppContext`（以及后续扩展的 SecurityContext、Seata XID 等），在执行线程中恢复。
   - [ ] 字段：
     - [ ] `private final ExecutorService delegate;`
   - [ ] 构造方法：
     - [ ] `public ContextAwareExecutor(ExecutorService delegate)`
   - [ ] 方法级 TODO：
     - [ ] `public void execute(Runnable task)`
       - 行为：
         1. 捕获当前线程的 `AppContext`（从 `AppContextHolder.get()` 获取）。
         2. 将包装后的任务提交给 `delegate`：
            - 在执行前设置捕获的 `AppContext` 到 `AppContextHolder`；
            - 在执行后清理 `AppContextHolder`。
       - TDD：
         - [ ] `ContextAwareExecutorTest.shouldPropagateAppContextToTask()`
           - 在主线程设置一个特定的 `AppContext`；
           - 使用 `ContextAwareExecutor` 提交任务，在任务内部读取 `AppContextHolder.get()` 并断言与主线程相同；
           - 确保任务结束后，任务线程中的上下文被清理（可通过再次提交任务并断言不存在上下文）。
     - [ ] `public <T> Future<T> submit(Callable<T> task)`
       - 类似 `execute`，但支持返回值与异常。
       - TDD：
         - [ ] `ContextAwareExecutorTest.shouldReturnResultFromSubmittedTask()`
         - [ ] `ContextAwareExecutorTest.shouldPropagateExceptionFromTask()`

3. 为 `ContextAwareExecutor` 提供静态工厂方法（可选）：
   - [ ] 在 `VirtualThreadExecutorFactory` 中增加：
     - [ ] `public static ContextAwareExecutor newContextAwareVirtualExecutor(String namePrefix)`
       - 内部：创建虚拟线程执行器 → 包装为 `ContextAwareExecutor` 返回。
       - TDD：
         - [ ] `VirtualThreadExecutorFactoryTest.shouldCreateContextAwareVirtualExecutor()`

> 注意：后续在控制器、应用服务、任务调度、MQ 消费者中，统一依赖 `ContextAwareExecutor` 来确保 `AppContext` 传递正确，特别是在虚拟线程场景下。

---

## 1.4 backend-infra：可观测性与日志基础设施骨架

### 1.4.1 日志配置与 JSON 格式输出

**目标**：为后端服务提供统一的 JSON 日志输出格式，并预留与 Promtail/Loki 集成的字段结构。

**TODO 列表（配置与基础类）**：

1. 在 `backend-infra` 的 `src/main/resources` 中创建日志配置文件：

   - [ ] 若使用 Logback：`logback-spring.xml`；
   - [ ] 要求：
     - [ ] 输出 JSON 格式日志，包含字段（至少）：
       - 时间戳 `ts`
       - 日志级别 `level`
       - logger 名称 `logger`
       - 线程名 `thread`
       - 消息 `msg`
       - `traceId`
       - `tenantId`
       - `userId`
       - 请求路径（如可获取）
     - [ ] 与 MDC 集成：
       - 预留 `traceId`、`tenantId`、`userId`、`requestId` 等 Key。
   - [ ] TDD：
     - [ ] 虽然日志配置难以传统单元测试，但可编写一个简单的 `LogConfigurationSmokeTest`：
       - 创建 Spring Boot 测试上下文；
       - 打一条日志；
       - 验证日志框架启动无异常（必要时可通过自定义 Appender 捕获输出做断言）。

2. 在 `backend-infra` 中定义日志 MDC 工具类 `LoggingContext`：
   - [ ] 包路径：`io.github.faustofanb.admin.infra.logging`。
   - [ ] 方法级 TODO：
     - [ ] `public static void putContextFromAppContext(AppContext ctx)`
       - 将 `tenantId`、`userId`、`requestId`、`traceId` 放入 MDC。
       - TDD：
         - [ ] `LoggingContextTest.shouldPutMdcFromAppContext()`：调用该方法后，从 MDC 中读取并断言字段存在。
     - [ ] `public static void clear()`
       - 清理 MDC 中的相关 Key。
       - TDD：
         - [ ] `LoggingContextTest.shouldClearMdc()`

> 后续在 `AppContext` 过滤器、虚拟线程任务包装等位置统一调用 `LoggingContext`，以确保日志中携带必要的上下文信息。

---

### 1.4.2 Micrometer 与 Prometheus 基础集成

**目标**：为服务提供基础指标采集能力，如 HTTP 请求时延、系统资源使用情况等。

**TODO 列表**：

1. 在 `backend-infra` 中增加依赖：

   - [ ] `io.micrometer:micrometer-core`
   - [ ] `io.micrometer:micrometer-registry-prometheus`
   - [ ] Spring Boot Actuator 相关依赖。

2. 创建指标配置类 `MetricsConfig`：

   - [ ] 包路径：`io.github.faustofanb.admin.infra.metrics`。
   - [ ] 使用 `@Configuration`、`@AutoConfiguration` 或在 `backend-boot` 中导入。
   - [ ] 方法级 TODO：
     - [ ] `@Bean MeterRegistryCustomizer<MeterRegistry> commonTagsCustomizer()`
       - 为所有指标添加统一标签，例如：`app=admin-backend`、`env`（从配置读取）、`instanceId` 等。
       - TDD：
         - [ ] `MetricsConfigTest.shouldAddCommonTags()`：使用 `SimpleMeterRegistry` 作为测试替身，验证注册的 Tag。

3. 确认/配置 Actuator 暴露 `/actuator/prometheus` 端点：
   - [ ] 在公共配置中启用相应端点。
   - [ ] TDD：
     - [ ] 使用 `WebTestClient` 或 `MockMvc` 对 `/actuator/prometheus` 做一个简单的集成测试，确保返回 200 并包含基本指标字段。

---

### 1.4.3 OpenTelemetry 集成占位

**目标**：预留 OpenTelemetry Trace 集成基础结构，使后续 HTTP / RPC / MQ / 任务调度可以统一接入链路追踪。

**TODO 列表**：

1. 在 `backend-infra` 中新增 Trace 配置类 `TracingConfig`（骨架）:

   - [ ] 包路径：`io.github.faustofanb.admin.infra.tracing`。
   - [ ] 方法级 TODO（可暂时留空实现或返回 Noop）：
     - [ ] `@Bean Tracer tracer()`（如采用 OpenTelemetry API，可封装 `OpenTelemetry.getTracer("admin-backend")`）。
   - [ ] TDD：
     - [ ] `TracingConfigTest.shouldProvideTracerBean()`：加载配置并断言存在 `Tracer` Bean。

2. 定义简单的 Trace 工具类 `TraceHelper`：
   - [ ] 包路径：`io.github.faustofanb.admin.infra.tracing`。
   - [ ] 方法级 TODO：
     - [ ] `public static String currentTraceId()`
       - 从当前 Span 中获取 traceId；如暂时未集成真实 OTel，可返回一个占位或从 MDC 读取。
       - TDD：
         - [ ] `TraceHelperTest.shouldReturnNonEmptyTraceIdEvenWithoutRealTracer()`（初期保障不为 null）。

> 实际的 OTel 集成细节可在后续章节扩展，此处只需确保有统一的入口与抽象，方便后续在过滤器/拦截器中使用。

---

## 1.5 backend-boot：主应用启动与基础配置

### 1.5.1 主启动类与最小可运行应用

**目标**：提供一个最小可启动的 Spring Boot 应用，验证多模块结构与基础配置的正确性，为后续功能迭代提供运行环境。

**TODO 列表**：

1. 创建主应用类 `AdminApplication`：

   - [ ] 包路径：`io.github.faustofanb.admin.boot`。
   - [ ] 内容要求：
     - [ ] 使用 `@SpringBootApplication` 注解；
     - [ ] 启动时打印应用名称、环境信息等基础日志；
   - [ ] 方法级 TODO：
     - [ ] `public static void main(String[] args)`
       - 调用 `SpringApplication.run(AdminApplication.class, args);`
       - 可在启动前后增加简单的 log（使用 JSON 日志结构）。
       - TDD：
         - [ ] `AdminApplicationTest.shouldStartSpringContext()`：使用 Spring Boot 测试注解，验证上下文可正常启动且无关键 Bean 缺失。

2. 基础配置文件：
   - [ ] 在 `backend-boot/src/main/resources` 中创建 `application.yaml`：
     - [ ] 配置基础 server 端口、应用名、虚拟线程开启开关 `spring.threads.virtual.enabled=true`；
     - [ ] 配置基础的日志级别与 Actuator 端点。
   - [ ] TDD：
     - [ ] 通过 `AdminApplicationTest` 或独立的环境测试，验证虚拟线程配置不阻碍应用启动。

---

## 1.6 骨架搭建与系统演进路线（合并原第 14 章内容）

> 本小节将原 `14-演进路线与版本规划-TODO` 的内容前移，
> 与基础骨架搭建放在同一章节中，方便 AI Agent 在搭建初期就整体规划版本与演进路径。

### 1.6.1 版本与里程碑规划

**TODO（文档约定）**：

1. 版本编号约定：

- 使用 `MAJOR.MINOR.PATCH` 语义化版本；
- MAJOR 对应架构或不兼容变更；
- MINOR 对应功能增强；
- PATCH 对应缺陷修复。

2. 典型阶段（示例，可在设计文档中细化）：

- v0.x：内部验证版，功能不稳定；
- v1.0：单体+多租户版本，覆盖核心管理功能；
- v2.x：逐步服务化/模块化拆分；
- v3.x：支持多区域部署、更复杂的可观测性与弹性能力。

---

### 1.6.2 单体到微服务/模块化的演进 TODO

**TODO（骨架）**：

1. 模块边界识别：

- 根据领域边界与团队边界识别待拆分模块（如：用户与权限、租户与计费、资源管理、审计与报表等）；

2. 拆分顺序：

- 优先拆分外部依赖多、变更频率较高的模块；
- 核心领域在早期保持在同一进程内，等边界稳定后再拆分；

3. 技术手段：

- 在 v1.x 阶段通过模块化（多 Maven module）先完成逻辑拆分；
- 在 v2.x 阶段通过 Dubbo / HTTP RPC + 事件总线实现进程间通信；
- 引入 API 网关、统一鉴权、统一审计等基础设施。

---

### 1.6.3 数据库与多租户演进

**TODO**：

1. 多租户模型演进：

- 单库共表 → 单库分 schema → 多库多租户；

2. 迁移策略：

- 使用数据迁移脚本与工具（Flyway / Liquibase），在版本升级时迁移数据结构；
- 对长时间运行的迁移任务结合任务调度与进度上报机制；

3. 向后兼容：

- 对老版本客户端或接口保留兼容层，在若干版本后逐步废弃；

---

### 1.6.4 性能与可观测性演进

**TODO（文档约定）**：

1. 性能基线：

- 为关键接口设定 QPS / 延时 / 资源占用等基线指标；

2. 优化路径：

- v1.x：通过缓存、批处理、异步任务等方式优化单体性能；
- v2.x 之后：通过水平扩展、读写分离、分库分表、分布式缓存等方式提升整体容量；

3. 可观测性：

- 从基础日志 → 统一日志平台 → 指标与 Tracing → 分布式追踪与告警；

---

### 1.6.5 架构决策记录（ADR）与回溯

**TODO**：

1. ADR 存放位置：

- 建议在 `doc/other/15.系统模块架构决策.md` 中按章节记录；

2. 决策示例：

- 选择 Dubbo 而非直连 HTTP 的原因；
- 选择 RocketMQ + Outbox 设计事件总线的原因；
- 选择 PowerJob 作为任务调度的原因等。

3. 变更管理：

- 版本升级或架构重构时，在 ADR 中新增记录并关联版本号。

---

### 1.6.6 为未来模块预留 TODO 占位

**TODO（骨架）**：

1. 可能的新模块：

- 计费/账单模块；
- 报表与 BI 模块；
- 工作流/审批模块（可与 12 章流程编排协同）。

2. 对于每个潜在模块，提前约定：

- 领域边界与上下游依赖；
- 与多租户、安全、资源/Excel、任务调度、事件总线等模块的协同方式。

---

## 1.7 TDD 流程与质量门槛要求（本章通用）

**目标**：为「基础骨架搭建」阶段确立 TDD 与质量门槛标准，确保后续模块在此基础上执行同样标准。

**TODO 列表**：

1. TDD 流程在本章的落地要求：

   - [ ] 每新增一个核心类（例如 `ApiResponse`、`BusinessException`、`AppContextHolder`、`ContextAwareExecutor` 等），必须先在对应的 `*Test` 类中定义至少 1~3 个关键行为的测试方法，再开始编写实现。
   - [ ] 不允许为了赶进度而省略测试；若某方法暂无法测试，需要在 TODO 中明确标记原因和后续补测计划。

2. 覆盖率要求：

   - [ ] 在 `jacoco-maven-plugin` 中配置全局覆盖率阈值（例如：
     - [ ] 指令覆盖率（INSTRUCTION） ≥ 70%
     - [ ] 分支覆盖率（BRANCH） ≥ 60%
   - [ ] 对 `backend-common` 与 `backend-infra` 这类通用基础模块，建议覆盖率要求更高（如 80%+），因为其复用度高，风险大。

3. 静态检查与代码规范（可在本章先占位，后续细化）：

   - [ ] 预留集成 Checkstyle / SpotBugs / Sonar 等静态分析工具的插件位；
   - [ ] 在 TODO 文档中记录：
     - [ ] 后续需要为这些工具配置规则文件（可以在 `doc/todo/代码规范与静态分析.md` 中展开）。

4. 测试类别区分：
   - [ ] 单元测试（Unit Test）：
     - 针对单个类或少量类，保证逻辑正确性；
     - 不依赖外部系统（DB、Redis、MQ）；
   - [ ] 组件/集成测试（Component/Integration Test）：
     - 例如 `GlobalExceptionHandler` 与 Spring MVC 结合的测试；
     - 可以使用内存数据库或 Testcontainers；
   - [ ] 本章主要以单元测试为主，少量对 Spring 上下文进行的 Smoke/集成测试。

---

> 本章节仅是整个「AI Agent 代码实现指导 TODO 文档」的第 1 部分，后续章节将围绕安全与多租户基础、系统模块、资源模块、Excel 模块、调度与事务一致性等逐步展开，每一章都将保持同样的「类级/方法级 + TDD 要求」的详细粒度。
