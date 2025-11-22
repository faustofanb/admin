# 阶段七：审计与监控 (Audit & Monitor)

本阶段目标是实现操作日志记录和系统监控。

## 7.1 审计日志 (backend-audit)

### 7.1.1 实体定义

- **路径**: `io.github.faustofanb.admin.module.audit.domain.entity.SysOperLog`
- **字段**:
  - `title` (模块标题)
  - `businessType` (业务类型: 0 其它 1 新增 2 修改 3 删除...)
  - `method` (方法名称)
  - `requestMethod` (GET/POST)
  - `operName` (操作人员)
  - `operUrl` (请求 URL)
  - `operIp` (主机地址)
  - `operParam` (请求参数)
  - `jsonResult` (返回参数)
  - `status` (操作状态)
  - `errorMsg` (错误消息)
  - `operTime` (操作时间)
  - `costTime` (消耗时间)

### 7.1.2 登录日志 `SysLogininfor`

- **字段**: `userName`, `ipaddr`, `status`, `msg`, `accessTime`.

### 7.1.3 AOP 切面

- **注解**: `@Log(title="...", businessType=...)`
- **切面**: `LogAspect`
- **逻辑**: 拦截注解，异步记录日志。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第七阶段：审计日志。
请在 `backend-audit` 模块中执行以下操作：

1. **实体定义**:
   - 创建 `SysOperLog` 和 `SysLogininfor` 实体。
   - 创建对应的 Repository。

2. **日志切面**:
   - 定义 `@Log` 注解。
   - 实现 `LogAspect` 切面：
     - 解析请求参数、IP、UserAgent。
     - 捕获执行结果或异常。
     - 异步调用 `RemoteLogService` (或直接调用本地 Service) 保存日志。

3. **业务服务**:
   - 实现 `SysOperLogService` 和 `SysLogininforService`，提供查询和清空日志的功能。
   - 实现对应的 Controller。
```
