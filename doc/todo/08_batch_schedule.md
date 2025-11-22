# 阶段八：批处理与调度 (Batch & Schedule)

本阶段目标是集成 EasyExcel 和 PowerJob。

## 8.1 批处理 (backend-batch)

### 8.1.1 Excel 工具

- **封装**: `ExcelUtil`，支持同步/异步导入导出。
- **监听器**: `DataListener`，处理读取到的数据。

---

## 8.2 任务调度 (backend-schedule)

### 8.2.1 PowerJob 集成

- **配置**: `PowerJobConfig`。
- **处理器**: `SysJobService` (如果使用本地 DB 调度) 或 PowerJob Processor。
- **示例任务**: 定时清理日志。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第八阶段：批处理与调度。
请在 `backend-batch` 和 `backend-schedule` 模块中执行以下操作：

1. **Excel 批处理**:
   - 封装 EasyExcel 工具类。
   - 实现一个通用的导入导出接口示例。

2. **任务调度**:
   - 集成 PowerJob Worker。
   - 编写一个 `LogCleanupProcessor`，用于定期清理 `SysOperLog` 表中超过 30 天的数据。

请确保调度任务能够正确注入 Spring Bean 并执行业务逻辑。
```
