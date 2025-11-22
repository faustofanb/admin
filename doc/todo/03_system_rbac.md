# 阶段三：系统核心 RBAC (System Core RBAC)

本阶段目标是实现后台管理系统的核心权限模型，包括用户、部门、角色和菜单。这是“后台管理系统”区别于普通 system 的关键部分。

## 3.1 组织架构 (backend-system)

### 3.1.1 部门实体 `SysDept`

- **路径**: `io.github.faustofanb.admin.module.system.domain.entity.SysDept`
- **继承**: `TreeEntity` (获得 parentId, sort, children)
- **字段**:
  - `deptName` (String)
  - `leader` (String): 负责人
  - `phone` (String)
  - `email` (String)
  - `status` (Integer): 0 正常 1 停用
  - `ancestors` (String): 祖级列表 (e.g. "0,100,101")，用于快速查询子孙。

### 3.1.2 岗位实体 `SysPost` (可选)

- **字段**: `postCode`, `postName`, `sort`, `status`.

---

## 3.2 权限模型 (backend-system)

### 3.2.1 菜单实体 `SysMenu`

- **路径**: `io.github.faustofanb.admin.module.system.domain.entity.SysMenu`
- **继承**: `TreeEntity`
- **字段**:
  - `menuName` (String): 菜单名称
  - `menuType` (String): M 目录 C 菜单 F 按钮
  - `path` (String): 路由地址
  - `component` (String): 组件路径
  - `perms` (String): 权限标识 (e.g. "system:user:list")
  - `icon` (String): 图标
  - `visible` (Boolean): 是否显示
  - `status` (Integer)

### 3.2.2 角色实体 `SysRole`

- **路径**: `io.github.faustofanb.admin.module.system.domain.entity.SysRole`
- **字段**:
  - `roleName` (String)
  - `roleKey` (String): 角色权限字符串 (e.g. "admin")
  - `roleSort` (Integer)
  - `dataScope` (String): 数据范围 (1 全部 2 自定义 3 本部门 4 本部门及以下 5 仅本人)
  - `status` (Integer)
- **关联**:
  - `@ManyToMany` `SysMenu` (角色-菜单关联)
  - `@ManyToMany` `SysDept` (角色-部门关联，用于数据权限)

### 3.2.3 用户实体 `SysUser`

- **路径**: `io.github.faustofanb.admin.module.system.domain.entity.SysUser`
- **字段**:
  - `userName` (String): 账号
  - `nickName` (String): 昵称
  - `email`, `phonenumber`
  - `sex` (String)
  - `avatar` (String)
  - `password` (String): 加密后的密码
  - `status` (Integer)
  - `loginIp`, `loginDate`
  - `deptId` (Long): 归属部门
- **关联**:
  - `@ManyToOne` `SysDept`
  - `@ManyToMany` `SysRole`
  - `@ManyToMany` `SysPost`

---

## 3.3 业务逻辑

### 3.3.1 用户服务 `SysUserService`

- **功能**:
  - 用户 CRUD。
  - `checkUserNameUnique`: 校验账号唯一。
  - `resetPassword`: 重置密码。
  - `updateUserStatus`: 修改状态。

### 3.3.2 菜单服务 `SysMenuService`

- **功能**:
  - `selectMenuTreeByUserId`: 根据用户角色构建前端路由树。
  - `selectPermsByUserId`: 获取用户权限字符串集合。

### 3.3.3 部门服务 `SysDeptService`

- **功能**:
  - 构建部门树。
  - 校验部门是否有下级，是否有用户。

---

## AI 提示词 (Prompt)

```text
你现在的任务是完成项目的第三阶段：系统核心 RBAC 模型。
请在 `backend-system` 模块中执行以下操作：

1. **领域实体 (Jimmer)**:
   - 定义 `SysDept` 和 `SysMenu`，继承 `TreeEntity`。
   - 定义 `SysRole` 和 `SysUser`，包含上述详细字段。
   - 配置实体间的关联关系（User-Dept, User-Role, Role-Menu）。
   - 确保所有实体继承 `BaseEntity`。

2. **仓储层**:
   - 创建对应的 `JRepository`。

3. **应用服务**:
   - 实现 `SysUserService`：处理用户增删改查，密码加密（使用 BCrypt）。
   - 实现 `SysMenuService`：提供根据用户 ID 查询菜单树（构建路由）和权限标识集合的方法。
   - 实现 `SysDeptService`：提供部门树查询。

请注意：菜单树的构建需要考虑递归或一次性查询后内存组装，推荐使用内存组装以减少数据库压力。
```
