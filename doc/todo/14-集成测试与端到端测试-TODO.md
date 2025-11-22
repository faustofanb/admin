# 第 14 章 集成测试与端到端测试 TODO 清单（AI Agent 实施指引）

> 本章原基于 `doc/design/13.单元测试与TDD.md` 的测试分层思路，
> 补充聚焦在：**集成测试（Integration Test）与端到端测试（E2E Test）** 的落地：
>
> - Spring Boot 集成测试的通用约定与基建；
> - 核心链路（登录鉴权、多租户隔离、Excel 批处理、任务调度、事件总线、流程编排、RPC）的集成验证；
> - 必要时的端到端测试（通过 HTTP / RPC 调用完整链路）；
> - 与 CI/CD 的集成。

---

## 14.1 集成测试整体约定

**TODO（文档约定）**：

1. 测试分层（在本章视角再强调一次）：

   - 单元测试（unit）：隔离依赖，Mock 外部组件（详见原第 13 章）；
   - 集成测试（integration）：启动部分或完整 Spring 上下文，验证 Bean 协作与基础设施集成；
   - 端到端测试（e2e，可选）：通过 HTTP / RPC / 消息驱动完整打通关键业务流程。

2. 命名规范：

   - 集成测试类命名：`XxxIntegrationTest`、`XxxIT`；
   - E2E 测试类命名：`XxxE2eTest` 或 `XxxEndToEndTest`；
   - 方法命名：`shouldBehavior_whenCondition`。

3. 目录结构（示例）：

   - `src/test/java/.../integration`：放置集成测试；
   - `src/test/java/.../e2e`：放置端到端测试；
   - 测试包结构尽量与生产代码保持一致，方便导航。

4. 测试工具：

   - Spring Boot Test：`@SpringBootTest`、`@DataJpaTest`、`@AutoConfigureMockMvc`；
   - MockMvc / WebTestClient 用于 HTTP 调用；
   - Testcontainers / 嵌入式组件（如内存数据库、嵌入式 MQ）用于依赖设施。

---

## 14.2 Spring Boot 集成测试基建 TODO

**TODO（方法级骨架）**：

1. 基础抽象：

   - [ ] `AbstractIntegrationTest`（`io.github.faustofanb.admin.test.integration`）：
     - 使用 `@SpringBootTest`、`@ActiveProfiles("test")`；
     - 统一初始化 Testcontainers / 内存数据库 / Redis 等依赖；
     - 统一多租户与安全上下文的构造工具方法。

2. Web 层集成测试基类：

   - [ ] `AbstractWebMvcIntegrationTest`：
     - 注入 `MockMvc` 或 `WebTestClient`；
     - 提供快捷方法封装常见鉴权头（token、tenantId 等）。

3. 数据隔离策略：

   - 使用内存数据库（H2）或每个测试类独立 schema；
   - 或者使用 Testcontainers 启动真实数据库容器，在 `@BeforeEach` 清理数据。

4. **TDD 占位**：

   - [ ] `AbstractIntegrationTestTest.shouldLoadSpringContext()`；
   - [ ] `AbstractWebMvcIntegrationTestTest.shouldPerformAuthenticatedRequest()`。

---

## 14.3 核心业务链路集成测试 TODO

### 14.3.1 登录与鉴权链路

**TODO（测试点）**：

1. 登录接口：

   - 正确的用户名/密码能够登录并返回 token；
   - 错误凭证返回合理错误码与信息；

2. 授权与多租户：

   - 携带 token 与 tenantId 访问受保护接口时的行为；
   - 不同角色（管理员/普通用户）访问相同接口的权限差异。

---

### 14.3.2 多租户数据隔离链路

**TODO**：

1. 准备多个租户的数据样本；
2. 使用不同租户上下文调用相同查询接口，验证只返回本租户数据；
3. 验证写入操作不会越权写入其他租户的数据。

---

### 14.3.3 分布式事务与 Outbox 链路

**TODO**：

1. 模拟一个典型业务用例（如下单或关键领域操作），在本地事务中写入 Outbox；
2. 启动 Outbox 分发任务（可通过任务调度或手动触发），验证 MQ 收到消息；
3. 验证消费端在收到消息后正确更新下游状态。

---

### 14.3.4 缓存与限流链路

**TODO**：

1. 在高并发下调用某个缓存重度依赖的接口：

   - 首次调用命中数据库并写入缓存；
   - 后续调用命中缓存；

2. 对限流保护的接口：

   - 在频繁调用下触发限流策略，验证返回码与日志；
   - 验证不会对其他接口造成连带影响。

---

### 14.3.5 Excel 批处理与任务调度链路

**TODO**：

1. Excel 导入集成测试：

   - 通过 HTTP 上传 Excel 文件；
   - 验证创建 Excel 任务记录；
   - 触发后台任务执行（模拟或真实 PowerJob 调度）；
   - 验证导入结果与错误行收集。

2. Excel 导出集成测试：

   - 发起导出请求；
   - 等待任务完成；
   - 通过资源模块下载导出文件并校验内容格式。

3. 进度推送：

   - 测试 WebSocket/SSE 端点在任务状态变化时能收到进度更新。

---

### 14.3.6 事件总线与流程编排链路

**TODO**：

1. 领域事件到集成事件的完整链路：

   - 触发一个领域事件（如租户开通）；
   - 验证 Outbox 中写入集成事件；
   - 验证 MQ 消费者收到消息并触发后续流程（如初始化权限）。

2. 流程编排链路：

   - 使用真实应用服务 + RPC 客户端 + 事件发布，执行一个编排流程（如租户开通）；
   - 验证流程各步骤的副作用（租户记录、用户记录、权限记录、通知事件等）。

---

### 14.3.7 RPC 与跨服务调用链路

**TODO**：

1. 在本地或多模块环境下启动提供者与消费者：

   - 验证 Dubbo 接口的调用成功/失败场景；
   - 验证上下文透传（tenantId、traceId 等）。

2. 与事件总线/流程编排的集成：

   - 在流程编排中通过 RPC 调用远程服务，并根据返回结果发布事件或更新本地状态。

---

## 14.4 端到端测试（E2E）约定 TODO

**TODO（文档约定）**：

1. 范围控制：

   - 仅对最关键的少数用户路径编写 E2E 测试（例如：租户初始化 → 用户登录 → 授权 → Excel 导入/导出 等）；
   - 避免全覆盖，以免维护成本过高。

2. 技术选型：

   - 可基于 REST 客户端（如 RestAssured）直接调用 HTTP 接口；
   - 或使用浏览器自动化工具（如 Playwright / Selenium）做 UI 层验证（视项目需要）；

3. 环境依赖：

   - 通常在 CI 的特定阶段或预发布环境执行；
   - 需要有较完整的依赖设施（数据库、消息队列、缓存等）。

---

## 14.5 与 CI/CD 的集成

**TODO（骨架）**：

1. 在 CI 流水线中：

   - 区分单元测试与集成/E2E 测试阶段；
   - 对集成/E2E 测试可在特定分支或 Tag 上执行，以控制执行时间；

2. 报告与可视化：

   - 为集成/E2E 测试配置独立报告，便于追踪失败原因；

3. 失败策略：

   - 单元测试失败必须阻断合并/发布；
   - 集成/E2E 测试失败的阻断策略可按环境不同配置（如生产前必须全部通过）。

---

> 本章完成后，项目将具备：
>
> - 结构化的集成测试与端到端测试实践指引；
> - 覆盖核心链路的集成验证方案；
> - 与 CI/CD 流水线协同的测试执行策略。
