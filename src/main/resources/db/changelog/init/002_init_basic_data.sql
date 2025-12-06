-- liquibase formatted sql

-- changeset faustofan:004-init-data runOnChange:true
-- comment: 初始化基础数据：默认租户、管理员、角色、菜单、权限等

-- ============================================================================
-- 1. 默认租户
-- ============================================================================
INSERT INTO sys_tenant (id, name, code, contact_name, status, created_time, updated_time, deleted)
VALUES (1, '默认租户', 'DEFAULT', '系统管理员', 'ENABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

-- ============================================================================
-- 2. 默认组织
-- ============================================================================
INSERT INTO sys_org (id, tenant_id, name, parent_id, created_time, updated_time, deleted)
VALUES
    (1, 1, '总公司', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (2, 1, '技术部', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (3, 1, '运营部', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

-- ============================================================================
-- 3. 默认角色
-- ============================================================================
INSERT INTO sys_role (id, tenant_id, code, name, description, data_scope, created_time, updated_time, deleted)
VALUES
    (1, 1, 'SUPER_ADMIN', '超级管理员', '拥有系统全部权限', 'ALL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (2, 1, 'ADMIN', '管理员', '租户管理员', 'ALL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (3, 1, 'USER', '普通用户', '普通用户角色', 'SELF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (4, 1, 'DEPT_MANAGER', '部门经理', '可管理本部门数据', 'SAME_DEPT_TREE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

-- ============================================================================
-- 4. 默认用户 (密码: admin123 的 BCrypt 哈希)
-- ============================================================================
INSERT INTO sys_user (id, tenant_id, username, nickname, password_hash, email, phone, status, org_id, created_time, updated_time, deleted)
VALUES
    (1, 1, 'admin', '超级管理员', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@example.com', '13800138000', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (2, 1, 'user', '测试用户', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'user@example.com', '13800138001', 'ACTIVE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

-- ============================================================================
-- 5. 用户-角色关联
-- ============================================================================
INSERT INTO sys_user_role_mapping (user_id, role_id)
VALUES
    (1, 1),  -- admin -> 超级管理员
    (2, 3);  -- user -> 普通用户

-- ============================================================================
-- 6. 默认菜单
-- ============================================================================
INSERT INTO sys_menu (id, tenant_id, title, parent_id, type, path, component, permission, icon, sort_order, created_time, updated_time, deleted)
VALUES
    -- 系统管理
    (1, 1, '系统管理', NULL, 'DIRECTORY', '/system', NULL, NULL, 'setting', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (2, 1, '用户管理', 1, 'MENU', '/system/user', 'system/user/index', 'system:user:list', 'user', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (3, 1, '角色管理', 1, 'MENU', '/system/role', 'system/role/index', 'system:role:list', 'team', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (4, 1, '菜单管理', 1, 'MENU', '/system/menu', 'system/menu/index', 'system:menu:list', 'menu', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (5, 1, '组织管理', 1, 'MENU', '/system/org', 'system/org/index', 'system:org:list', 'apartment', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (6, 1, '字典管理', 1, 'MENU', '/system/dict', 'system/dict/index', 'system:dict:list', 'book', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),

    -- 用户管理按钮
    (7, 1, '新增用户', 2, 'BUTTON', NULL, NULL, 'system:user:add', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (8, 1, '编辑用户', 2, 'BUTTON', NULL, NULL, 'system:user:edit', NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (9, 1, '删除用户', 2, 'BUTTON', NULL, NULL, 'system:user:delete', NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

-- ============================================================================
-- 7. 默认权限
-- ============================================================================
INSERT INTO sys_permission (id, tenant_id, code, name, group_name, description, created_time, updated_time, deleted)
VALUES
    -- 用户权限
    (1, 1, 'system:user:list', '用户列表', '用户管理', '查看用户列表', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (2, 1, 'system:user:add', '新增用户', '用户管理', '新增用户', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (3, 1, 'system:user:edit', '编辑用户', '用户管理', '编辑用户信息', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (4, 1, 'system:user:delete', '删除用户', '用户管理', '删除用户', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    -- 角色权限
    (5, 1, 'system:role:list', '角色列表', '角色管理', '查看角色列表', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (6, 1, 'system:role:add', '新增角色', '角色管理', '新增角色', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (7, 1, 'system:role:edit', '编辑角色', '角色管理', '编辑角色', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (8, 1, 'system:role:delete', '删除角色', '角色管理', '删除角色', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    -- 菜单权限
    (9, 1, 'system:menu:list', '菜单列表', '菜单管理', '查看菜单列表', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (10, 1, 'system:menu:add', '新增菜单', '菜单管理', '新增菜单', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

-- ============================================================================
-- 8. 角色-菜单关联 (超级管理员拥有所有菜单)
-- ============================================================================
INSERT INTO sys_role_menu_mapping (role_id, menu_id)
SELECT 1, id FROM sys_menu WHERE deleted = FALSE;

-- ============================================================================
-- 9. 角色-权限关联 (超级管理员拥有所有权限)
-- ============================================================================
INSERT INTO sys_role_permission_mapping (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE deleted = FALSE;

-- ============================================================================
-- 10. 默认字典
-- ============================================================================
INSERT INTO sys_dict (id, tenant_id, code, name, description, status, created_time, updated_time, deleted)
VALUES
    (1, 0, 'user_status', '用户状态', '用户账号状态', 'ENABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (2, 0, 'common_status', '通用状态', '启用/禁用状态', 'ENABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (3, 0, 'menu_type', '菜单类型', '菜单类型枚举', 'ENABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (4, 0, 'data_scope', '数据权限', '数据权限范围', 'ENABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

-- ============================================================================
-- 11. 字典项
-- ============================================================================
INSERT INTO sys_dict_item (id, dict_id, label, value, sort_order, status, color, created_time, updated_time, deleted)
VALUES
    -- 用户状态
    (1, 1, '正常', 'ACTIVE', 1, 'ENABLE', 'green', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (2, 1, '锁定', 'LOCKED', 2, 'ENABLE', 'orange', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (3, 1, '禁用', 'DISABLED', 3, 'ENABLE', 'red', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    -- 通用状态
    (4, 2, '启用', 'ENABLE', 1, 'ENABLE', 'green', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (5, 2, '禁用', 'DISABLE', 2, 'ENABLE', 'red', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    -- 菜单类型
    (6, 3, '目录', 'DIRECTORY', 1, 'ENABLE', 'blue', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (7, 3, '菜单', 'MENU', 2, 'ENABLE', 'cyan', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (8, 3, '按钮', 'BUTTON', 3, 'ENABLE', 'purple', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    -- 数据权限
    (9, 4, '全部数据', 'ALL', 1, 'ENABLE', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (10, 4, '本部门', 'SAME_DEPT', 2, 'ENABLE', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (11, 4, '本部门及下级', 'SAME_DEPT_TREE', 3, 'ENABLE', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (12, 4, '自定义', 'CUSTOM', 4, 'ENABLE', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (13, 4, '仅本人', 'SELF', 5, 'ENABLE', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);
