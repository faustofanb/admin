# 第 13 章 资源与文件管理 TODO 清单（AI Agent 实施指引）

> 本章对应 `doc/design/6.资源与文件管理.md`，聚焦：
>
> - 文件上传/下载接口统一规范（HTTP、权限、多租户）；
> - 文件元数据与物理存储解耦（本地 / 对象存储，如 MinIO / OSS / COS 等）；
> - 大文件分片上传、断点续传；
> - 访问控制（ACL）、防直链、防盗链；
> - 与审计日志、任务调度、Excel 批处理等模块的协同。

---

## 13.1 整体架构与分层

**TODO（文档约定）**：

1. 包结构建议：

   - `io.github.faustofanb.admin.resource.api`：对外暴露的 DTO 与 Facade；
   - `io.github.faustofanb.admin.resource.app`：应用服务、用例编排（非流程编排层）；
   - `io.github.faustofanb.admin.resource.domain`：文件/资源领域模型与领域服务；
   - `io.github.faustofanb.admin.resource.infrastructure`：存储实现（本地文件系统、对象存储客户端等）。

2. 能力边界：
   - 把“资源管理”抽象为统一的文件/对象存储服务，与具体存储介质解耦；
   - 所有读写操作必须经过多租户与权限校验；
   - 尽量不在 Controller 里直接操作 `MultipartFile`，而是通过应用服务完成逻辑。

---

## 13.2 领域模型与仓储

### 13.2.1 `ResourceFile` 聚合根

**类：** `io.github.faustofanb.admin.resource.domain.model.ResourceFile`

**TODO**：

1. 字段示例：

   - [ ] `Long id`；
   - [ ] `Long tenantId`；
   - [ ] `String bucket`；
   - [ ] `String path`（在存储中的 Key/路径）；
   - [ ] `String originalFilename`；
   - [ ] `String contentType`；
   - [ ] `Long size`；
   - [ ] `String bizType`（业务分类，如 avatar、excel、report 等）；
   - [ ] `Long ownerId`（上传人/归属用户）；
   - [ ] `Boolean deleted`；
   - [ ] 审计字段：`createdAt`、`createdBy`、`updatedAt`、`updatedBy`。

2. 领域行为：

   - [ ] `markDeleted()`：逻辑删除；
   - [ ] 生成下载 URL 所需的上下文信息（不直接生成 URL，由应用服务或 infrastructure 完成）；

3. **TDD 占位**：

   - [ ] `ResourceFileTest.shouldMarkDeleted()`。

---

### 13.2.2 仓储接口 `ResourceFileRepository`

**接口：** `io.github.faustofanb.admin.resource.domain.repository.ResourceFileRepository`

**TODO**：

1. 方法：

   - [ ] `ResourceFile save(ResourceFile file)`；
   - [ ] `Optional<ResourceFile> findById(Long tenantId, Long id)`；
   - [ ] `List<ResourceFile> findByOwnerAndBizType(Long tenantId, Long ownerId, String bizType)`；

2. **TDD 占位**：

   - [ ] `ResourceFileRepositoryTest.basicCrud()`（可用内存实现或 JPA Mock）。

---

## 13.3 存储抽象与实现

### 13.3.1 存储客户端接口 `StorageClient`

**接口：** `io.github.faustofanb.admin.resource.infrastructure.storage.StorageClient`

**TODO**：

1. 方法：

   - [ ] `String upload(String bucket, String path, InputStream input, long size, String contentType)`；
   - [ ] `InputStream download(String bucket, String path)`；
   - [ ] `void delete(String bucket, String path)`；
   - [ ] `String generatePresignedUrl(String bucket, String path, Duration ttl)`（可选）；

2. 语义：

   - `bucket + path` 唯一标识一个存储对象；
   - 返回值一般为对象存储的 Key 或最终 URL，视实现而定；

3. **TDD 占位**：

   - [ ] `StorageClientTest.shouldUploadAndDownload()`（可基于内存实现）。

---

### 13.3.2 本地文件实现 `LocalStorageClient`

**类：** `io.github.faustofanb.admin.resource.infrastructure.storage.LocalStorageClient`

**TODO**：

1. 配置：

   - [ ] 属性类 `StorageProperties`（根前缀目录、是否启用本地存储、各 bucket 子目录等）；

2. 行为：

   - [ ] 将对象写入本地文件系统（`baseDir/bucket/path`）；
   - [ ] 支持创建目录；

3. **TDD 占位**：

   - [ ] `LocalStorageClientTest.shouldWriteFileToDisk()`。

---

### 13.3.3 对象存储实现（如 MinIO）

**类：** `MinioStorageClient`（位于 `io.github.faustofanb.admin.resource.infrastructure.storage`）

**TODO（只做骨架，具体 SDK 调用在实现阶段补全）**：

1. 依赖 MinIO SDK 客户端（或其他对象存储 SDK）；
2. 实现 `StorageClient` 接口所有方法；
3. 与多租户策略：

   - 可按租户划分 bucket，或按租户前缀划分 path；

4. **TDD 占位**：

   - [ ] `MinioStorageClientTest.shouldUploadAndGenerateUrl()`（可使用 MinIO 容器或 Mock 客户端）。

---

## 13.4 上传/下载应用服务与 API

### 13.4.1 上传应用服务 `FileUploadAppService`

**类：** `io.github.faustofanb.admin.resource.app.FileUploadAppService`

**TODO（方法级）**：

1. 依赖：

   - `StorageClient`；
   - `ResourceFileRepository`；
   - 多租户/安全上下文（获取 `tenantId`、`operatorId`）。

2. 方法：

   - [ ] `ResourceFileDto upload(FileUploadCommand command)`：
     - 校验 bizType、大小限制、白名单后缀等；
     - 调用 `StorageClient.upload` 写入对象存储；
     - 持久化 `ResourceFile` 元数据；

3. **TDD 占位**：

   - [ ] `FileUploadAppServiceTest.shouldUploadAndPersistMetadata()`。

---

### 13.4.2 下载应用服务 `FileDownloadAppService`

**类：** `io.github.faustofanb.admin.resource.app.FileDownloadAppService`

**TODO**：

1. 方法：

   - [ ] `FileDownloadDescriptor getDownloadDescriptor(Long fileId)`：
     - 加载 `ResourceFile`，校验租户与访问权限；
     - 返回包含 `bucket`、`path`、`contentType`、`filename` 等信息的描述对象；

2. 可扩展：

   - 支持生成临时签名 URL（对象存储场景）；

3. **TDD 占位**：

   - [ ] `FileDownloadAppServiceTest.shouldCheckPermissionBeforeDownload()`。

---

### 13.4.3 Web 层接口约定

**TODO（文档约定）**：

1. 上传接口：

   - `POST /api/resources/upload`：
     - 请求：`multipart/form-data`，包含文件 + `bizType` 等参数；
     - 响应：`ResourceFileDto`；

2. 下载接口：

   - `GET /api/resources/{id}/download`：
     - 从 `FileDownloadAppService` 获取文件描述并输出为响应流；

3. 权限：

   - 所有接口必须检查当前用户是否有对应 bizType/资源的访问权限；
   - 需结合第 3 章安全与多租户模块的鉴权能力。

---

## 13.5 大文件与分片上传

**TODO（骨架）**：

1. 分片上传模型：

   - `UploadSession` 聚合（sessionId、tenantId、bizType、totalSize、chunkSize、status 等）；
   - `UploadChunk` 记录每个分片的信息（序号、大小、校验码等）。

2. 应用服务：

   - [ ] `UploadSessionAppService`：创建/查询上传会话、合并分片；

3. 任务调度协同：

   - 通过任务调度模块清理过期的 `UploadSession` 与临时分片文件；

4. **TDD 占位**：

   - [ ] `UploadSessionAppServiceTest.shouldCreateAndCompleteSession()`。

---

## 13.6 安全、ACL 与防直链

**TODO（文档约定）**：

1. 访问控制：

   - 所有文件都必须绑定到某个租户与业务实体上（如用户、报表、任务等）；
   - 下载前校验调用者对业务实体的访问权限，而不是仅凭 fileId；

2. 防直链/防盗链：

   - 若使用对象存储公共域名，下载 URL 应为短时效签名链接；
   - 或在网关层做 Referer 校验与 Token 校验；

3. 审计日志：

   - 上传/下载/删除操作应写入审计日志模块，并记录 operatorId、tenantId、ip 等信息。

---

## 13.7 与 Excel、任务调度、流程编排的协同

**TODO（文档约定）**：

1. Excel 模块：

   - Excel 导入模板、导出结果文件统一通过资源模块管理；
   - Excel 批处理完成后，将生成的文件元数据写入 `ResourceFile` 并返回给前端下载链接；

2. 任务调度模块：

   - 大文件导出可由任务调度触发异步任务，任务完成后将结果文件持久化并推送进度；

3. 流程编排模块：

   - 在复杂流程（如批量导入 + 校验 + 持久化）中，资源模块仅负责文件读写，实际业务逻辑由编排层与应用服务完成。

---

> 本章完成后，系统将具备：
>
> - 统一的资源与文件管理能力；
> - 与多租户、安全、审计、Excel、任务调度等模块协同的文件读写闭环；
> - 支持大文件与分片上传的扩展骨架。
