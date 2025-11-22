# 阶段一：基础架构建设 (Foundation)

本阶段目标是建立项目的核心骨架，包括通用工具、上下文管理、异常体系、DDD 基础类以及基础设施配置。

## 1.1 通用模块 (backend-common)

### 1.1.1 统一响应结构 `Result<T>`
- **目标**: 定义 API 统一返回格式。
- **路径**: `io.github.faustofanb.admin.common.model.Result`
- **字段**:
  - `code` (int): 状态码 (200 成功, 其他失败)
  - `message` (String): 提示信息
  - `data` (T): 泛型数据
  - `traceId` (String): 链路追踪 ID
- **方法**: `success(T data)`, `fail(ErrorCode errorCode)`, `fail(String msg)` 等静态工厂方法。

### 1.1.2 错误码接口与枚举 `ErrorCode`
- **目标**: 统一管理系统错误码。
- **路径**: `io.github.faustofanb.admin.common.exception.ErrorCode` (Interface), `BizErrorCode` (Enum)
- **内容**: 定义如 `SUCCESS(200)`, `BAD_REQUEST(400)`, `UNAUTHORIZED(401)`, `FORBIDDEN(403)`, `INTERNAL_ERROR(500)` 等标准错误。

### 1.1.3 业务异常 `BizException`
- **目标**: 统一业务运行时异常。
- **路径**: `io.github.faustofanb.admin.common.exception.BizException`
- **继承**: `RuntimeException`
- **属性**: `ErrorCode errorCode`

### 1.1.4 应用上下文 `AppContext`
- **目标**: 在线程间传递请求上下文（租户、用户、链路信息）。
- **路径**: `io.github.faustofanb.admin.common.context.AppContext`
- **技术**: 使用 Alibaba `TransmittableThreadLocal` 以支持异步线程池传递。
- **字段**: `tenantId`, `userId`, `username`, `roles`, `traceId`。
- **方法**: `setContext(...)`, `getContext()`, `clear()`。

### 1.1.5 JSON 工具类 `JsonUtils`
- **目标**: 封装 Jackson，统一序列化配置。
- **路径**: `io.github.faustofanb.admin.common.util.JsonUtils`
- **配置**: 支持 Java 8 Time (JSR310), 忽略 Null 值, 忽略未知属性。

---

## 1.2 核心领域层 (backend-core)

### 1.2.1 Jimmer 实体基类 `BaseEntity`
- **目标**: 为所有领域实体提供审计字段。
- **路径**: `io.github.faustofanb.admin.core.domain.BaseEntity`
- **类型**: `@MappedSuperclass` (Jimmer 概念)
- **字段**:
  - `createdTime` (LocalDateTime)
  - `updatedTime` (LocalDateTime)
  - `createdBy` (Long)
  - `updatedBy` (Long)

### 1.2.2 树形实体基类 `TreeEntity`
- **目标**: 为树形结构（菜单、部门）提供统一定义。
- **路径**: `io.github.faustofanb.admin.core.domain.TreeEntity`
- **继承**: `BaseEntity`
- **字段**:
  - `parentId` (Long)
  - `sort` (Integer)
  - `children` (List<T>) - `@OneToMany`

### 1.2.3 领域事件基类 `DomainEvent`
- **目标**: 定义事件总线的基础消息格式。
- **路径**: `io.github.faustofanb.admin.core.event.DomainEvent`
- **字段**: `eventId` (UUID), `occurredOn` (Timestamp), `tenantId`, `source` (来源模块)。

### 1.2.4 CQRS 标记接口
- **目标**: 规范应用层入参。
- **路径**: `io.github.faustofanb.admin.core.cqrs.Command`, `io.github.faustofanb.admin.core.cqrs.Query`。

---

## 1.3 基础设施层 (backend-infra)

### 1.3.1 Redisson 配置
- **目标**: 初始化 Redis 客户端。
- **路径**: `io.github.faustofanb.admin.infra.config.RedissonConfig`
- **逻辑**: 读取 `spring.data.redis` 配置，实例化 `RedissonClient` Bean。

### 1.3.2 全局异常处理器
- **目标**: 拦截 Controller 抛出的异常，转换为标准 `Result`。
- **路径**: `io.github.faustofanb.admin.infra.web.GlobalExceptionHandler`
- **注解**: `@RestControllerAdvice`
- **处理**:
  - `BizException` -> 返回对应错误码。
  - `MethodArgumentNotValidException` -> 返回 400 及参数校验错误详情。
  - `Exception` -> 返回 500 系统错误。

### 1.3.3 Jackson 配置
- **目标**: Spring Boot 全局 JSON 配置。
- **路径**: `io.github.faustofanb.admin.infra.config.JacksonConfig`
- **逻辑**: 配置 `ObjectMapper` Bean，注册 `JavaTimeModule`，设置时区为 GMT+8。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第一阶段：基础架构建设。
请按照以下步骤在对应的模块中编写代码：

1. **backend-common**:
   - 创建 `Result<T>` 类，包含 code, message, data, traceId 字段及静态构建方法。
   - 创建 `ErrorCode` 接口和 `BizErrorCode` 枚举（包含常见 HTTP 状态映射）。
   - 创建 `BizException` 运行时异常。
   - 创建 `AppContext` 类，使用 `TransmittableThreadLocal` 存储 `UserContext` (tenantId, userId, etc.)。
   - 创建 `JsonUtils` 工具类，基于 Jackson 实现对象与 JSON 字符串的互转。

2. **backend-core**:
   - 创建 Jimmer 的 `BaseEntity` 接口，定义 createdTime, updatedTime 等审计字段。
   - 创建 `TreeEntity` 接口，继承 BaseEntity，增加 parentId, sort, children 字段，用于树形结构。
   - 创建 `DomainEvent` 抽象基类，包含 eventId, tenantId, occurredOn 字段。
   - 创建 `Command` 和 `Query` 标记接口。

3. **backend-infra**:
   - 创建 `RedissonConfig` 配置类，注入 `RedissonClient`。
   - 创建 `GlobalExceptionHandler`，使用 `@RestControllerAdvice` 处理 `BizException` 和系统异常，返回 `Result` 对象。
   - 创建 `JacksonConfig`，配置全局的时间序列化格式（yyyy-MM-dd HH:mm:ss）。

请确保代码符合 Java 21 规范，并正确处理模块间的依赖关系。
```
