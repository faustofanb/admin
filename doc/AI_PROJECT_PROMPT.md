# 全局 AI 项目提示词 (Master Prompt)

## 项目背景
本项目是一个基于 Java 21 和 Spring Boot 3.2 的模块化单体（Modular Monolith）后台管理系统。项目采用 DDD（领域驱动设计）思想，使用 Jimmer 作为 ORM 框架。

## 模块结构
- `backend-common`: 通用工具、异常、结果封装。
- `backend-core`: DDD 基础类、CQRS 接口。
- `backend-infra`: 基础设施配置（Redis, Jackson, GlobalException）。
- `backend-rpc-api`: 模块间通信接口定义。
- `backend-rpc-impl`: 模块间通信接口实现。
- `backend-boot`: 启动入口与集成配置。
- `module/backend-system`: **核心系统与认证模块**（用户、部门、菜单、角色、字典、配置、登录认证）。
- `module/backend-file`: 文件服务。
- `module/backend-audit`: 审计日志。
- `module/backend-batch`: 批处理。
- `module/backend-schedule`: 任务调度。

## 编码规范
1. **包命名**: `io.github.faustofanb.admin.<module>.<layer>`
   - e.g. `io.github.faustofanb.admin.module.system.domain.entity`
2. **实体定义**: 使用 Jimmer 的 `@Entity`。
   - 所有实体继承 `BaseEntity`。
   - 树形实体继承 `TreeEntity`。
3. **异常处理**: 统一抛出 `BizException`。
4. **注释**: 关键业务逻辑必须包含 Javadoc。

## 任务执行指南
请参考 `doc/todo/` 目录下的分阶段待办清单进行开发。
顺序如下：
1. `01_foundation.md`: 基础架构。
2. `02_system_module_setup.md`: 系统模块初始化。
3. `03_system_rbac.md`: 系统核心 RBAC。
4. `04_system_auth.md`: 身份认证。
5. `05_gateway_rpc.md`: RPC。
6. `06_file.md`: 文件。
7. `07_audit.md`: 审计。
8. `08_batch_schedule.md`: 批处理与调度。
9. `09_boot.md`: 启动。

请严格按照上述顺序执行，确保依赖关系正确。
