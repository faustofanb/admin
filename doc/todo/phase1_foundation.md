# 第一阶段：基础项目骨架搭建 (Phase 1: Foundation)

本阶段目标是建立稳固的 Maven 多模块工程结构，配置好基础依赖、数据库连接和迁移工具，为后续业务开发打下基础。

## 1.1 创建 Maven 多模块工程

- [ ] **1.1.1 初始化父工程**

  - 创建根目录 `pom.xml`。
  - 设置 `groupId`: `com.example.admin` (根据实际情况调整)。
  - 设置 `artifactId`: `admin-backend`。
  - 设置 `version`: `1.0.0-SNAPSHOT`。
  - 设置 `packaging`: `pom`。
  - **Properties 配置**:
    - `java.version`: 21
    - `spring-boot.version`: 3.2.x (或最新稳定版)
    - `spring-cloud.version`: 2023.0.x (与 Boot 版本对应)
    - `spring-cloud-alibaba.version`: 2023.x (如果需要)
    - `dubbo.version`: 3.3.x
    - `jimmer.version`: 最新版
    - `mapstruct.version`: 1.5.x
    - `lombok.version`: 1.18.30+
  - **DependencyManagement**:
    - 引入 `spring-boot-dependencies`
    - 引入 `spring-cloud-dependencies`
    - 引入 `dubbo-bom`
    - 定义常用第三方库版本 (Guava, Hutool, EasyExcel, Redisson 等)。

- [ ] **1.1.2 创建通用模块 (Common & Core)**

  - **`common`**:
    - 依赖: `lombok`, `jackson`, `guava`, `jakarta.servlet-api`。
    - 内容: 全局异常类, `Result<T>` 包装类, `AppContext` 定义, 工具类。
  - **`core`**:
    - 依赖: `common`, `jimmer-core`。
    - 内容: 基础实体定义 (`BaseEntity`), 领域模型基类。

- [ ] **1.1.3 创建基础设施模块 (Infra)**

  - **`infra-integration`**:
    - 依赖: `common`, `spring-boot-starter-data-redis`, `redisson`, `rocketmq-spring-boot-starter`, `micrometer`.
    - 内容: 第三方中间件的配置封装。
  - **`transaction-support`**:
    - 依赖: `common`.
    - 内容: 分布式事务相关注解和切面 (Seata, Outbox 支持)。

- [ ] **1.1.4 创建 RPC 模块**

  - **`rpc-api`**:
    - 依赖: `common`.
    - 内容: Dubbo 接口定义, DTO 对象。
  - **`rpc-impl`**:
    - 依赖: `rpc-api`, 各业务模块 (scope=provided 或 runtime).
    - 内容: Dubbo 接口实现类 (Facade 模式)。

- [ ] **1.1.5 创建业务模块 (Modules)**

  - 创建以下子模块 (packaging: jar):
    - `module/user-tenant-permission`
    - `module/resource`
    - `module/excel`
    - `module/scheduler`
    - `module/logging`
  - 每个模块依赖: `core`。

- [ ] **1.1.6 创建网关模块 (Gateway)**

  - **`gateway`**:
    - 依赖: `spring-cloud-starter-gateway`, `spring-boot-starter-actuator`.
    - 注意: Gateway 基于 WebFlux，**不要**引入 `spring-boot-starter-web`。

- [ ] **1.1.7 创建启动模块 (Boot)**
  - **`app-boot`** (或直接在根目录/独立模块):
    - 依赖: 所有 `module/*`, `rpc-impl`, `infra-integration`, `transaction-support`, `spring-boot-starter-web`, `spring-boot-starter-actuator`.
    - 插件: `spring-boot-maven-plugin`。

## 1.2 基础环境与数据库配置

- [ ] **1.2.1 配置 application.yml**

  - 在 `app-boot` 中创建 `src/main/resources/application.yml`。
  - 配置 `server.port` (默认 8080)。
  - 配置 `spring.application.name`。
  - 配置 `spring.datasource`:
    - Driver: PostgreSQL
    - URL, Username, Password (使用环境变量占位符)。
  - 配置 `spring.redis`: Host, Port, Password。

- [ ] **1.2.2 集成 Flyway**

  - 在 `app-boot` 引入 `flyway-core` 和 `flyway-database-postgresql`。
  - 创建目录 `src/main/resources/db/migration`。
  - 迁移现有 SQL 脚本:
    - `V1__init_core_schema.sql` (租户, 用户, 权限)
    - `V2__init_resource_schema.sql` (资源)
    - `V3__init_task_schema.sql` (任务, Excel)
    - `V4__init_powerjob_tables.sql` (PowerJob)
  - 验证启动时自动建表。

- [ ] **1.2.3 集成 Jimmer**

  - 在 `app-boot` 引入 `jimmer-spring-boot-starter`。
  - 配置 Jimmer 生成器 (apt) 在 `pom.xml` 中。
  - 编写一个简单的 `HelloController` 和 `HelloRepository` 验证数据库连通性。

- [ ] **1.2.4 配置 Gateway 基础路由**
  - 在 `gateway` 模块的 `application.yml` 中配置:
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: auth-service
              uri: ${APP_BACKEND_URI:http://localhost:8081} # 转发到后端服务，默认本地直连，生产环境可通过环境变量覆盖
              predicates:
                - Path=/auth/**
            - id: api-service
              uri: ${APP_BACKEND_URI:http://localhost:8081}
              predicates:
                - Path=/api/**
          # discovery:
          # locator:
          # enabled: true # 不引入 Nacos 时无需开启
    ```
  - 配置全局 CORS (跨域) 策略，允许前端 Vben Admin 访问。

## 1.3 验证与测试

- [ ] **1.3.1 编写冒烟测试**
  - 创建 `src/test/java` 下的 `ApplicationContextTest`。
  - 确保 `mvn test` 能成功启动 Spring Context。
- [ ] **1.3.2 验证多模块依赖**
  - 执行 `mvn clean install`，确保所有模块编译通过，依赖关系正确。
