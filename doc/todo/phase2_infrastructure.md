# 第二阶段：基础设施与通用组件 (Phase 2: Infrastructure & Common Components)

本阶段目标是构建系统的“神经系统”，包括上下文传播、日志记录、监控指标以及实时通信的基础设施。

## 2.1 上下文传播 (Context Propagation)

- [ ] **2.1.1 定义 AppContext**

  - 在 `common` 模块中创建 `AppContext` record。
  - 字段包含:
    - `Long tenantId`
    - `Long userId`
    - `String username`
    - `String traceId`
    - `String requestId`
    - `Set<String> roles`
  - 实现 `Serializable` 接口。

- [ ] **2.1.2 实现 AppContextHolder**

  - 使用 `TransmittableThreadLocal` (TTL) 或 `ThreadLocal` 存储 `AppContext`。
  - 提供静态方法 `get()`, `set()`, `clear()`。
  - **注意**: 确保在 Java 21 虚拟线程环境下测试通过 (TTL 通常兼容性较好)。

- [ ] **2.1.3 创建 ContextFilter**

  - 实现 `jakarta.servlet.Filter`。
  - 逻辑:
    1. 生成或读取 `X-Request-ID`。
    2. 从 OpenTelemetry/MDC 获取 `traceId`。
    3. 解析 Header 中的租户/用户信息 (在 Security Filter 之后执行，或从 JWT 解析)。
    4. 构建 `AppContext` 并存入 Holder。
    5. `try-finally` 块中确保 `Holder.clear()`。

- [ ] **2.1.4 配置 Dubbo 上下文透传**
  - 实现 Dubbo 的 `Filter` (Provider & Consumer)。
  - Consumer 端: 从 `AppContextHolder` 读取并写入 `RpcContext` attachment。
  - Provider 端: 从 `RpcContext` 读取并写入 `AppContextHolder`。

## 2.2 日志与可观测性 (Logging & Observability)

- [ ] **2.2.1 配置 Logback**

  - 引入 `logstash-logback-encoder`。
  - 创建 `logback-spring.xml`。
  - 配置 Console Appender (开发环境高亮)。
  - 配置 File Appender (生产环境 JSON 格式)。
  - **关键**: JSON 字段必须包含 `trace_id`, `tenant_id`, `user_id` (从 MDC 获取)。

- [ ] **2.2.2 集成 Prometheus**

  - 确保 `micrometer-registry-prometheus` 已引入。
  - 配置 `application.yml`:
    ```yaml
    management:
      endpoints:
        web:
          exposure:
            include: "health,info,prometheus,metrics"
      metrics:
        tags:
          application: ${spring.application.name}
    ```

- [ ] **2.2.3 集成 OpenTelemetry**
  - 引入 `opentelemetry-bom` 和 `opentelemetry-api`。
  - (可选) 使用 Spring Cloud Sleuth 的 OTel 桥接或直接使用 OTel Java Agent。
  - 编写 `TraceFilter` (如果未使用 Agent 自动注入)，将 `traceId` 放入 MDC。

## 2.3 实时通信基础 (WebSocket & SSE)

- [ ] **2.3.1 网关层 WebSocket 配置**

  - 在 `gateway` 模块中配置 WebSocket 路由转发。
  - 确保网关支持 `Upgrade` 头。

- [ ] **2.3.2 SSE 工具类封装**

  - 在 `common` 或 `infra-integration` 中创建 `SseEmitterManager`。
  - 功能:
    - 存储 `Map<String, SseEmitter>` (Key 为 taskId 或 userId)。
    - 提供 `send(String key, Object data)` 方法。
    - 处理连接超时和完成回调 (自动移除)。
  - **注意**: 考虑分布式场景下的 SSE。如果部署多实例，SSE 连接只在本地。
    - _进阶方案_: 使用 Redis Pub/Sub 广播消息，触发持有连接的实例进行推送。本阶段可先实现单机版，或直接利用 Redis Pub/Sub 实现分布式推送基础。

- [ ] **2.3.3 WebSocket 基础处理器 (可选)**
  - 如果 SSE 无法满足双向通信需求，在 `app-boot` 中配置 `WebSocketHandler`。
  - 实现基于 Token 的握手拦截器 (`HandshakeInterceptor`) 进行鉴权。
