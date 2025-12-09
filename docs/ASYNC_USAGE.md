# 异步编程配置文档

本项目已完成企业级异步支持配置，包括 JDK 21 虚拟线程和 Kotlin 协程的完整集成。

## 📋 目录结构

```
shared/infrastructure/
├── config/async/
│   ├── VirtualThreadConfig.kt          # 虚拟线程配置
│   ├── CoroutineConfig.kt              # Kotlin 协程配置
│   ├── AsyncExecutorConfig.kt          # 异步执行器统一配置
│   └── AsyncExceptionHandler.kt        # 异步异常处理器
├── async/
│   ├── CoroutineContextElements.kt     # 上下文传播（Security、AppContext、MDC）
│   ├── CoroutineUtils.kt               # 协程工具类
│   └── metrics/
│       ├── AsyncMetricsService.kt      # 异步监控服务
│       └── AsyncMetricsCollector.kt    # 指标采集器
└── test/
    └── async/
        ├── VirtualThreadConfigTest.kt          # 虚拟线程测试
        ├── CoroutineConfigTest.kt              # 协程配置测试
        ├── CoroutineContextElementsTest.kt     # 上下文传播测试
        ├── CoroutineUtilsTest.kt               # 工具类测试
        ├── AsyncMetricsServiceTest.kt          # 监控服务测试
        └── AsyncIntegrationTest.kt             # 集成测试
```

## 🚀 核心特性

### 1. 虚拟线程支持（JDK 21+）

- ✅ 轻量级：单个 JVM 可创建百万级虚拟线程
- ✅ 高吞吐：非常适合 I/O 密集型任务
- ✅ 简化编程：使用同步风格编写异步代码
- ✅ 自动配置：Spring Boot 4 原生支持

**可用执行器：**
- `applicationTaskExecutor`：主虚拟线程执行器（`@Async` 默认）
- `ioVirtualExecutor`：I/O 密集型执行器
- `batchVirtualExecutor`：批量处理执行器
- `scheduledVirtualExecutor`：定时任务执行器
- `cpuBoundExecutor`：CPU 密集型执行器（传统线程池）

### 2. Kotlin 协程支持

- ✅ 结构化并发：SupervisorJob 保证子协程失败不影响父协程
- ✅ 上下文传播：自动传播 SecurityContext、AppContext、MDC
- ✅ 多种调度器：IO、CPU、虚拟线程调度器
- ✅ 协程作用域：应用级、I/O、CPU 三种作用域

**可用调度器：**
- `virtualThreadDispatcher`：基于虚拟线程的调度器（推荐用于 I/O）
- `ioDispatcher`：Kotlin 标准 IO 调度器
- `cpuDispatcher`：Kotlin 标准 CPU 调度器

**可用作用域：**
- `applicationCoroutineScope`：应用级作用域
- `ioCoroutineScope`：I/O 作用域
- `cpuCoroutineScope`：CPU 作用域

### 3. 上下文自动传播

支持以下上下文在异步任务间传播：
- ✅ Spring SecurityContext（认证信息）
- ✅ AppContext（租户、用户、组织、权限等完整业务上下文）
- ✅ MDC（日志上下文：traceId、requestId 等）

### 4. 可观测性

- ✅ Micrometer 指标采集
- ✅ 任务执行时间统计
- ✅ 成功/失败率监控
- ✅ 线程数量监控
- ✅ 定时采集系统资源

## 📝 使用示例

### 示例 1：使用 @Async 注解（虚拟线程）

```kotlin
@Service
class UserService {

    // 使用默认虚拟线程执行器
    @Async
    fun sendNotification(userId: String): CompletableFuture<Void> {
        // 发送通知
        return CompletableFuture.completedFuture(null)
    }

    // 指定 I/O 虚拟线程执行器
    @Async("ioVirtualExecutor")
    fun queryDatabase(sql: String): CompletableFuture<List<User>> {
        // 数据库查询
        return CompletableFuture.completedFuture(emptyList())
    }

    // CPU 密集型使用传统线程池
    @Async("cpuBoundExecutor")
    fun computeReport(data: List<Int>): CompletableFuture<Report> {
        // 计算密集型任务
        return CompletableFuture.completedFuture(Report())
    }
}
```

### 示例 2：使用 Kotlin 协程（推荐）

```kotlin
@Service
class UserService(
    private val ioDispatcher: CoroutineDispatcher,
    private val cpuDispatcher: CoroutineDispatcher
) {

    // I/O 密集型任务
    suspend fun fetchUser(userId: String): User = withContext(ioDispatcher) {
        // 数据库查询
        delay(100)
        User(userId, "User Name")
    }

    // CPU 密集型任务
    suspend fun computeStatistics(data: List<Int>): Long = withContext(cpuDispatcher) {
        // 计算
        data.map { it.toLong() * it }.sum()
    }

    // 并行查询多个用户
    suspend fun fetchMultipleUsers(userIds: List<String>): List<User> {
        return CoroutineUtils.parallelMap(userIds, ioDispatcher) { userId ->
            fetchUser(userId)
        }
    }
}
```

### 示例 3：使用协程工具类

```kotlin
@Service
class OrderService(
    private val applicationCoroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) {

    // 并行执行多个任务
    suspend fun processOrder(orderId: String) {
        val results = CoroutineUtils.parallelTasks(
            context = ioDispatcher,
            tasks = listOf(
                { fetchOrderInfo(orderId) },
                { fetchUserInfo(orderId) },
                { fetchProductInfo(orderId) }
            )
        )
        // 处理结果
    }

    // 批量处理（分批并行）
    suspend fun batchProcess(items: List<String>) {
        CoroutineUtils.chunkedParallelMap(
            items = items,
            batchSize = 100,
            context = ioDispatcher
        ) { item ->
            processItem(item)
        }
    }

    // Fire and Forget
    fun sendAsyncNotification(userId: String) {
        CoroutineUtils.fireAndForget(applicationCoroutineScope, ioDispatcher) {
            // 发送通知，不等待结果
            delay(100)
            println("Notification sent")
        }
    }

    // 带重试
    suspend fun fetchWithRetry(url: String): String {
        return CoroutineUtils.retry(times = 3, initialDelay = 100) {
            httpClient.get(url)
        }
    }
}
```

### 示例 4：Flow 流式处理

```kotlin
@Service
class DataStreamService {

    fun processStream(items: List<String>): Flow<String> = flow {
        items.forEach { item ->
            delay(10)
            emit("Processed: $item")
        }
    }.buffer(64) // 背压控制

    suspend fun collectStream() {
        processStream(listOf("a", "b", "c"))
            .collect { result ->
                println(result)
            }
    }
}
```

### 示例 5：带监控的异步任务

```kotlin
@Service
class MonitoredService(
    private val metricsService: AsyncMetricsService
) {

    // 监控协程执行
    suspend fun fetchUserWithMetrics(userId: String): User {
        return withMetrics(
            metricsService = metricsService,
            taskName = "fetchUser",
            tags = mapOf("userId" to userId)
        ) {
            // 业务逻辑
            User(userId, "Name")
        }
    }

    // 监控虚拟线程执行
    fun processWithMetrics(data: String): String {
        return metricsService.recordVirtualThreadExecution(
            name = "processData",
            tags = mapOf("type" to "io")
        ) {
            Thread.sleep(100)
            "Result"
        }
    }
}
```

### 示例 6：上下文传播

```kotlin
@Service
class ContextPropagationExample {

    // 自动传播 SecurityContext、AppContext、MDC
    suspend fun businessOperation() {
        CoroutineUtils.withBusinessContext(Dispatchers.IO) {
            // 这里可以获取当前用户、租户信息
            val auth = SecurityContextHolder.getContext().authentication
            val appContext = AppContextHolder.get()

            println("User: ${appContext.username}")
            println("Tenant: ${appContext.tenantId}")
            println("OrgId: ${appContext.orgId}")
            println("RequestId: ${appContext.requestId}")

            // 执行业务逻辑
        }
    }
}
```

## ⚙️ 配置说明

### application.yaml 配置

```yaml
spring:
  threads:
    virtual:
      enabled: true           # 启用虚拟线程
      name-prefix: virt-      # 线程名前缀

app:
  async:
    enabled: true
    # 虚拟线程配置
    virtual-thread:
      enabled: true
      name-prefix: virt-
      task-termination-timeout: 60000
    # 协程配置
    coroutine:
      enabled: true
      use-virtual-thread: true
    # 传统线程池配置（CPU 密集型）
    thread-pool:
      cpu-core-pool-size: 8
      cpu-max-pool-size: 16
      queue-capacity: 1000
      keep-alive-seconds: 60
```

## 📊 监控指标

异步任务会自动上报以下 Prometheus 指标：

- `app.async.coroutine.execution`：协程执行时间
- `app.async.virtual_thread.execution`：虚拟线程执行时间
- `app.async.task.success`：任务成功计数
- `app.async.task.failure`：任务失败计数
- `app.async.virtual_thread.count`：虚拟线程数量
- `app.async.platform_thread.count`：平台线程数量

访问 `/actuator/prometheus` 查看所有指标。

## 🎯 最佳实践

### 1. 选择合适的执行模型

| 任务类型 | 推荐方案 | 原因 |
|---------|---------|------|
| I/O 密集型（数据库、HTTP） | 虚拟线程 / 协程 + IO Dispatcher | 高并发、低资源占用 |
| CPU 密集型（计算、加密） | 传统线程池 / 协程 + CPU Dispatcher | 充分利用 CPU 核心 |
| 混合任务 | 协程（切换调度器） | 灵活控制 |
| 简单异步 | @Async 注解 | 使用方便 |
| 复杂编排 | 协程 + CoroutineUtils | 代码清晰、功能强大 |

### 2. 上下文传播

始终使用 `CoroutineUtils.withBusinessContext()` 或手动添加上下文元素：

```kotlin
withContext(ioDispatcher + SecurityContextElement() + TenantContextElement()) {
    // 业务逻辑
}
```

### 3. 异常处理

协程会自动捕获异常并记录日志，但建议在关键位置添加 try-catch：

```kotlin
suspend fun criticalOperation() {
    try {
        CoroutineUtils.withBusinessContext {
            // 关键业务逻辑
        }
    } catch (e: Exception) {
        logger.error("Critical operation failed", e)
        // 发送告警
    }
}
```

### 4. 避免阻塞

虚拟线程和协程都不应执行阻塞操作，使用 `withContext()` 切换调度器：

```kotlin
// ❌ 错误：在默认调度器执行阻塞操作
suspend fun bad() {
    Thread.sleep(1000) // 阻塞线程
}

// ✅ 正确：使用 IO 调度器
suspend fun good() {
    withContext(Dispatchers.IO) {
        Thread.sleep(1000) // OK
    }
}
```

### 5. 控制并发度

批量任务使用 `chunkedParallelMap` 控制并发：

```kotlin
// ❌ 错误：创建 10000 个协程
suspend fun bad(items: List<String>) {
    items.map { async { process(it) } }.awaitAll()
}

// ✅ 正确：分批处理
suspend fun good(items: List<String>) {
    CoroutineUtils.chunkedParallelMap(items, batchSize = 100) {
        process(it)
    }
}
```

## 🔍 故障排查

### 问题 1：上下文丢失

**症状**：协程中无法获取当前用户、租户信息

**解决**：使用 `withBusinessContext()` 或手动传播上下文元素

### 问题 2：虚拟线程未生效

**症状**：线程名不包含 "virt"

**检查**：
1. 确认 `spring.threads.virtual.enabled=true`
2. 确认 JDK 版本 >= 21
3. 查看启动日志确认虚拟线程已初始化

### 问题 3：任务执行缓慢

**排查**：
1. 查看 Prometheus 指标分析瓶颈
2. 检查是否在虚拟线程/协程中执行 CPU 密集型任务
3. 检查数据库连接池配置

## 📚 参考资料

- [Spring Boot Virtual Threads](https://spring.io/blog/2022/10/11/embracing-virtual-threads)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html)
- [Project Loom](https://openjdk.org/projects/loom/)
- [Structured Concurrency](https://kotlinlang.org/docs/coroutines-basics.html#structured-concurrency)

## 📝 完整示例

查看 `AsyncUsageExamples.kt` 获取更多使用示例。
