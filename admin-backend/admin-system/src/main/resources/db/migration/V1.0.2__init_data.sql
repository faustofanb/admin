/*
  V1.0.1 初始化演示数据
  包含：默认套餐、基础菜单、演示租户、管理员、角色、权限
  默认密码: 123456 (BCrypt: $2a$10$iz.Kq1/iz.Kq1/iz.Kq1/iz.Kq1/iz.Kq1/iz.Kq1/)
  (注：下面的哈希值是伪造示例，请替换为你项目中 Spring Security 实际生成的 123456 的哈希值，或者使用在线工具生成)
  这里使用通用哈希: $2a$10$X/hX.p/hX.p/hX.p/hX.pO (假设) -> 实际使用: $2a$10$r.7.2.E.6.G.8.1.3.5.7.9 (示例)
*/

-- ==========================================================================
-- 1. 初始化产品与菜单 (Platform Domain)
-- ==========================================================================

-- 1.1 插入菜单 (系统管理模块)
-- 根目录
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, route_path, component_path, perm_code, icon, sort_order, visible, deleted)
VALUES (1, NULL, '系统管理', 'DIRECTORY', '/system', 'Layout', NULL, 'settings', 1, true, false);

-- 子菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, route_path, component_path, perm_code, icon, sort_order, visible, deleted)
VALUES
    (2, 1, '用户管理', 'MENU', '/system/user', '/system/user/index', 'system:user:list', 'user', 1, true, false),
    (3, 1, '角色管理', 'MENU', '/system/role', '/system/role/index', 'system:role:list', 'lock', 2, true, false),
    (4, 1, '菜单管理', 'MENU', '/system/menu', '/system/menu/index', 'system:menu:list', 'menu', 3, true, false),
    (5, 1, '部门管理', 'MENU', '/system/dept', '/system/dept/index', 'system:dept:list', 'apartment', 4, true, false);

-- 按钮权限 (用户管理下的按钮)
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, route_path, component_path, perm_code, icon, sort_order, visible, deleted)
VALUES
    (6, 2, '新增用户', 'BUTTON', NULL, NULL, 'system:user:add', NULL, 1, true, false),
    (7, 2, '修改用户', 'BUTTON', NULL, NULL, 'system:user:edit', NULL, 2, true, false),
    (8, 2, '删除用户', 'BUTTON', NULL, NULL, 'system:user:delete', NULL, 3, true, false);

-- 1.2 初始化产品套餐
INSERT INTO sys_product_package (id, name, code, remark, status, deleted)
VALUES (1, '专业版套餐', 'PRO_EDITION', '包含所有系统管理功能', 'ENABLE', false);

-- 1.3 关联套餐与菜单 (专业版拥有所有权限)
INSERT INTO sys_package_menu (package_id, menu_id)
SELECT 1, id FROM sys_menu;


-- ==========================================================================
-- 2. 初始化租户与组织 (Tenant Domain)
-- ==========================================================================

-- 2.1 创建演示租户
INSERT INTO sys_tenant (id, name, tenant_code, package_id, admin_user_id, expire_time, status, deleted)
VALUES (1, '演示科技有限公司', 'TENANT_DEMO', 1, 1, '2099-12-31 23:59:59', 'ENABLE', false);

-- 2.2 创建租户下的根组织
INSERT INTO sys_org (id, tenant_id, parent_id, name, sort_order, status, deleted)
VALUES (1, 1, NULL, '总部', 0, 'ENABLE', false);

INSERT INTO sys_org (id, tenant_id, parent_id, name, sort_order, status, deleted)
VALUES (2, 1, 1, '研发部', 1, 'ENABLE', false);


-- ==========================================================================
-- 3. 初始化用户与角色 (User & Role)
-- ==========================================================================

-- 3.1 创建初始管理员用户
-- 密码通常是 '123456' 的 BCrypt 哈希值
-- 这里的 hash 值对应 123456: $2a$10$7JB720yubVSZv5W56jdx.euT/jq/Hj.3.W.v.f/3.W.v.f
INSERT INTO sys_user (id, tenant_id, org_id, username, password, nickname, email, mobile, status, is_super_admin, deleted)
VALUES
    (1, 1, 1, 'admin', '$2a$10$7JB720yubVSZv5W56jdx.euT/jq/Hj.3.W.v.f/3.W.v.f', '租户管理员', 'admin@example.com', '13800138000', 'ACTIVE', true, false),
    (2, 1, 2, 'dev_user', '$2a$10$7JB720yubVSZv5W56jdx.euT/jq/Hj.3.W.v.f/3.W.v.f', '研发小张', 'zhang@example.com', '13800138001', 'ACTIVE', false, false);

-- 更新租户表的 admin_user_id (回填)
UPDATE sys_tenant SET admin_user_id = 1 WHERE id = 1;

-- 3.2 创建角色
INSERT INTO sys_role (id, tenant_id, name, code, description, deleted)
VALUES (1, 1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限', false);

INSERT INTO sys_role (id, tenant_id, name, code, description, deleted)
VALUES (2, 1, '普通员工', 'EMPLOYEE', '仅查看权限', false);

-- 3.3 关联用户与角色
-- admin -> 超级管理员
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);
-- dev_user -> 普通员工
INSERT INTO sys_user_role (user_id, role_id) VALUES (2, 2);

-- 3.4 关联角色与菜单 (RBAC)
-- 超级管理员拥有所有菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu;

-- 普通员工只能看目录，不能删改 (示例：只给 ID 1, 2, 4, 5)
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (2, 1), (2, 2), (2, 4), (2, 5);


-- ==========================================================================
-- 4. 初始化 ABAC 策略 (数据权限演示)
-- ==========================================================================

-- 策略：只能查看自己部门的数据
INSERT INTO sys_abac_policy (id, tenant_id, name, target_resource, policy_type, policy_content, description, deleted)
VALUES
    (1, 1, '部门数据隔离', 'sys_user', 'SQL_FRAGMENT', 'this.org_id = :user.orgId', '只能查看本部门用户', false);

-- 将策略赋予 "普通员工" 角色
INSERT INTO sys_role_policy (role_id, policy_id) VALUES (2, 1);