# 阶段六：文件与资源服务 (File & Resource)

本阶段目标是实现文件上传、下载及元数据管理，支持本地存储和 MinIO 对象存储。

## 6.1 领域模型 (backend-file)

### 6.1.1 实体定义

- **路径**: `io.github.faustofanb.admin.module.file.domain.entity.SysFile`
- **字段**:
  - `id`, `tenantId`
  - `fileName` (原始文件名)
  - `fileType` (扩展名)
  - `fileSize` (字节大小)
  - `url` (访问地址)
  - `storageType` (LOCAL, MINIO)
  - `bucket` (存储桶)
  - `filePath` (存储路径/Key)
  - `md5` (用于秒传)

---

## 6.2 存储策略

### 6.2.1 抽象接口 `FileStorage`

- **方法**: `upload`, `download`, `delete`.

### 6.2.2 实现类

- `LocalFileStorage`: 本地磁盘存储。
- `MinioFileStorage`: MinIO 对象存储。

---

## 6.3 业务接口

### 6.3.1 控制器 `SysFileController`

- **API**:
  - `POST /common/upload`: 通用上传接口。
  - `GET /common/download`: 通用下载接口。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第六阶段：文件存储模块。
请在 `backend-file` 模块中执行以下操作：

1. **领域实体**:
   - 创建 `SysFile` 实体，记录文件元数据。
   - 创建 `SysFileRepository`。

2. **存储策略**:
   - 定义 `FileStorage` 接口。
   - 实现 `LocalFileStorage` 和 `MinioFileStorage`。
   - 编写配置类，根据 `file.type` 属性动态选择实现 Bean。

3. **服务与接口**:
   - 实现 `SysFileService`：处理上传逻辑（计算 MD5 秒传、保存文件、保存记录）。
   - 实现 `SysFileController`：提供上传下载接口。

请确保上传接口支持 MultipartFile，并限制文件大小和类型。
```
