/*
  补全关联表结构
  注意：关联表通常是物理删除，不需要 deleted 字段
*/

-- 1. 套餐-菜单关联表
CREATE TABLE IF NOT EXISTS sys_package_menu (
                                                package_id      BIGINT NOT NULL,
                                                menu_id         BIGINT NOT NULL,
                                                PRIMARY KEY (package_id, menu_id)
);

-- 2. 角色-菜单关联表 (RBAC核心)
CREATE TABLE IF NOT EXISTS sys_role_menu (
                                             role_id         BIGINT NOT NULL,
                                             menu_id         BIGINT NOT NULL,
                                             PRIMARY KEY (role_id, menu_id)
);

-- 3. 用户-角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
                                             user_id         BIGINT NOT NULL,
                                             role_id         BIGINT NOT NULL,
                                             PRIMARY KEY (user_id, role_id)
);

-- 4. 角色-ABAC策略关联表
CREATE TABLE IF NOT EXISTS sys_role_policy (
                                               role_id         BIGINT NOT NULL,
                                               policy_id       BIGINT NOT NULL,
                                               PRIMARY KEY (role_id, policy_id)
);