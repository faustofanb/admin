# 第六阶段：业务模块实现 (Phase 6: Business Modules)

本阶段将实现核心业务功能，包括用户体系、资源管理、Excel 批处理和任务调度。

## 6.1 用户与租户模块 (User & Tenant)

- [ ] **6.1.1 实现 TenantService**

  - 创建/管理租户。
  - 初始化租户基础数据 (默认角色、管理员)。

- [ ] **6.1.2 实现 UserService**

  - 用户 CRUD。
  - 密码重置/修改。
  - 角色分配。

- [ ] **6.1.3 实现 PermissionService**

  - 菜单/权限树查询。
  - 角色与权限关联管理。

- [ ] **6.1.4 暴露 REST 接口**
  - 完善 `User/Tenant/Role/Permission` 的 Controller。
  - 补充 Swagger/OpenAPI 注解。

## 6.2 资源模块 (Resource) - BlobStorage

- [ ] **6.2.1 定义 BlobStorage 抽象**

  - 接口 `BlobStorage`:
    - `upload(String key, InputStream data, long size, String contentType)`
    - `download(String key)`
    - `delete(String key)`
    - `generatePresignedUrl(String key, int expireSeconds)`

- [ ] **6.2.2 实现存储适配器**

  - **MinIO/S3 实现**: 使用 AWS SDK for Java v2。
  - **RustFS 实现** (可选): 通过 HTTP Client 调用 RustFS 服务。
  - **Local 实现** (开发环境): 存储在本地磁盘。
  - 通过 Spring Profile (`dev`, `prod`) 切换实现 Bean。

- [ ] **6.2.3 实现 ResourceService**
  - **上传流程**:
    1. 计算 SHA256 (前端传或后端算)。
    2. 检查秒传 (查询 `t_resource_meta`)。
    3. 调用 `BlobStorage.upload`。
    4. 保存元数据。
    5. 发送 `RES_UPLOADED` 事件。
  - **下载流程**:
    1. 检查权限 (ACL)。
    2. 调用 `BlobStorage.download` 或生成预签名 URL (推荐)。

## 6.3 Excel 批处理模块 (Excel) - SSE 增强

- [ ] **6.3.1 集成 EasyExcel**

  - 封装通用的 `BatchImportListener`，支持分批处理和错误收集。

- [ ] **6.3.2 实现导入流程**

  - 接口 `POST /import`: 接收文件，保存到 BlobStorage，创建 `ImportTask` (PENDING)，发送 MQ。
  - 消费者:
    - 读取文件流。
    - 解析并分批写入 DB。
    - **关键**: 每处理一批 (如 100 条)，调用 `SseEmitterManager.send(taskId, progress)` 推送进度。
    - 完成后更新 Task 状态，生成错误报告 (如有)。

- [ ] **6.3.3 实现导出流程**

  - 接口 `POST /export`: 创建 `ExportTask`，发送 MQ。
  - 消费者:
    - 分页查询数据。
    - 写入 Excel 流 (上传到 BlobStorage)。
    - 定时推送进度。
    - 完成后更新 Task 状态 (包含下载链接)。

- [ ] **6.3.4 实现 SSE 接口**
  - `GET /api/v1/excel/progress/{taskId}`:
    - 建立 SSE 连接。
    - 注册到 `SseEmitterManager`。

## 6.4 任务调度模块 (Scheduler)

- [ ] **6.4.1 集成 PowerJob Worker**

  - 引入 `powerjob-worker-spring-boot-starter`。
  - 配置 `application.yml` 连接 PowerJob Server。

- [ ] **6.4.2 实现基础 Processor**

  - `OutboxScannerProcessor`: 扫描 Outbox 表。
  - `ResourceCleanupProcessor`: 清理逻辑删除的资源文件。

- [ ] **6.4.3 任务管理接口**
  - 提供 API 查询任务执行日志 (代理 PowerJob Server 接口或查本地库)。
