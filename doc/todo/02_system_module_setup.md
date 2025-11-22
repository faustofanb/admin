# 阶段二：系统模块初始化 (System Module Setup)

本阶段目标是创建核心业务模块 `backend-system`，并实现基础的系统配置与字典管理功能。这是后台管理系统的基石。

## 2.1 模块创建

### 2.1.1 创建 Maven 模块

- **模块名**: `backend-system`
- **父模块**: `module`
- **路径**: `module/backend-system`
- **依赖**:
  - `backend-core`
  - `backend-common`
  - `jimmer-spring-boot-starter`
  - `spring-boot-starter-web`

### 2.1.2 注册到父 POM

- 在 `module/pom.xml` 中添加 `<module>backend-system</module>`。

---

## 2.2 基础实体 (backend-system)

### 2.2.1 字典类型 `SysDictType`

- **路径**: `io.github.faustofanb.admin.module.system.domain.entity.SysDictType`
- **字段**:
  - `id` (Long)
  - `name` (String): 字典名称 (e.g. "用户性别")
  - `type` (String): 字典类型 (e.g. "sys_user_sex")
  - `status` (Integer): 状态 (0 正常 1 停用)
  - `remark` (String)

### 2.2.2 字典数据 `SysDictData`

- **路径**: `io.github.faustofanb.admin.module.system.domain.entity.SysDictData`
- **字段**:
  - `id` (Long)
  - `typeId` (Long): 关联 `SysDictType`
  - `label` (String): 字典标签 (e.g. "男")
  - `value` (String): 字典键值 (e.g. "1")
  - `sort` (Integer): 排序
  - `status` (Integer)
  - `cssClass` (String): 样式属性 (可选)
  - `listClass` (String): 表格回显样式 (e.g. "default", "primary")

### 2.2.3 参数配置 `SysConfig`

- **路径**: `io.github.faustofanb.admin.module.system.domain.entity.SysConfig`
- **字段**:
  - `id` (Long)
  - `configName` (String): 参数名称
  - `configKey` (String): 参数键名 (e.g. "sys.user.initPassword")
  - `configValue` (String): 参数键值
  - `configType` (Integer): 系统内置 (Y/N)

---

## 2.3 业务逻辑

### 2.3.1 字典服务 `DictService`

- **功能**:
  - 根据 `type` 查询字典数据列表（支持缓存）。
  - 增删改查字典类型与数据。
  - 刷新缓存。

### 2.3.2 配置服务 `ConfigService`

- **功能**:
  - 根据 `key` 获取配置值（支持缓存）。
  - 增删改查配置。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第二阶段：系统模块初始化。
请执行以下操作：

1. **创建模块**:
   - 在 `module/` 目录下创建新模块 `backend-system`。
   - 配置 `pom.xml`，继承父模块，引入 `backend-core` 和 `jimmer` 等依赖。
   - 在 `module/pom.xml` 中注册该子模块。

2. **领域实体 (Jimmer)**:
   - 在 `backend-system` 中定义 `SysDictType`, `SysDictData`, `SysConfig` 实体。
   - 继承 `BaseEntity`。
   - 使用 Jimmer 注解 (`@Entity`, `@Key`, `@ManyToOne` 等)。

3. **仓储与服务**:
   - 创建对应的 `JRepository`。
   - 实现 `DictService` 和 `ConfigService`，包含基本的 CRUD 逻辑。
   - *注意*: 暂时不需要实现 Controller，先完成领域层和应用层逻辑。

请确保新模块的包名为 `io.github.faustofanb.admin.module.system`。
```
