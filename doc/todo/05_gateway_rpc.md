# 阶段五：RPC 服务与网关 (RPC & Gateway)

本阶段目标是定义模块间的远程调用接口（为微服务拆分做准备），并配置网关（当前为单体内的逻辑网关）。

## 5.1 RPC 接口 (backend-rpc-api)

### 5.1.1 用户服务接口 `RemoteUserService`

- **路径**: `io.github.faustofanb.admin.rpc.api.system.RemoteUserService`
- **方法**:
  - `LoginUser getUserInfo(String username)`: 获取用户详细信息（含角色权限）。
  - `boolean registerUser(SysUserDTO user)`: 注册用户。

### 5.1.2 日志服务接口 `RemoteLogService`

- **路径**: `io.github.faustofanb.admin.rpc.api.system.RemoteLogService`
- **方法**:
  - `boolean saveLog(SysOperLogDTO log)`: 保存操作日志。
  - `boolean saveLogininfor(SysLogininforDTO info)`: 保存登录日志。

---

## 5.2 RPC 实现 (backend-rpc-impl)

### 5.2.1 实现类

- **路径**: `io.github.faustofanb.admin.rpc.impl.system.*`
- **逻辑**:
  - 注入 `SysUserService`, `SysLoginService` 等本地服务。
  - 实现 RPC 接口，进行 DTO 转换。
  - 使用 `@DubboService` (如果引入 Dubbo) 或 Spring `@Service`。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第五阶段：RPC 服务封装。
虽然目前是单体运行，但为了解耦和未来拆分，我们需要通过 RPC 接口层隔离模块。

1. **backend-rpc-api**:
   - 定义 `RemoteUserService` 和 `RemoteLogService` 接口。
   - 定义相关的 DTO 对象（POJO）。

2. **backend-rpc-impl**:
   - 引入 `backend-system` 和 `backend-audit` (如果日志在 audit 模块) 依赖。
   - 实现上述接口，内部调用本地 Service 完成业务逻辑。
   - 确保实现类可以被 Spring 容器扫描到。

3. **验证**:
   - 在 `backend-system` 中，尝试将直接调用 `SysUserService` 改为调用 `RemoteUserService` (模拟远程调用)，验证解耦效果。
```
