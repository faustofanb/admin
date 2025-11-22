# 阶段九：启动与集成 (Boot & Integration)

本阶段目标是整合所有模块，配置启动类，并进行最终测试。

## 9.1 启动模块 (backend-boot)

### 9.1.1 依赖聚合

- 引入 `backend-system`, `backend-iam`, `backend-file`, `backend-audit`, `backend-batch`, `backend-schedule`。

### 9.1.2 配置文件

- `application.yml`: 核心配置。
- `application-druid.yml`: 数据库连接池。
- `logback-spring.xml`: 日志配置。

### 9.1.3 启动类

- `AdminApplication`: 扫描所有包。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第九阶段：启动与集成。
请在 `backend-boot` 模块中执行以下操作：

1. **依赖管理**:
   - 在 `pom.xml` 中引入所有业务模块。

2. **配置整合**:
   - 完善 `application.yml`，配置 DataSource, Redis, MyBatis/Jimmer, Security 等。
   - 确保各模块的配置（如 `file.type`, `powerjob`）都有默认值。

3. **启动类**:
   - 编写 `AdminApplication`，确保 `@ComponentScan` 覆盖 `io.github.faustofanb.admin`。

4. **验证**:
   - 编写集成测试，模拟启动过程，确保无 Bean 冲突。
   - 验证 `/auth/login` 接口可用。
```
