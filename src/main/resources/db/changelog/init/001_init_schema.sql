-- liquibase formatted sql

-- changeset faustofan:003-schema-from-entities runOnChange:true
-- comment: 基于 Jimmer 实体定义生成的数据库 Schema

-- ============================================================================
-- 删除旧表（如果存在），按依赖顺序删除
-- ============================================================================
DROP TABLE IF EXISTS sys_user_role_mapping CASCADE;
DROP TABLE IF EXISTS sys_role_menu_mapping CASCADE;
DROP TABLE IF EXISTS sys_role_permission_mapping CASCADE;
DROP TABLE IF EXISTS sys_role_org_mapping CASCADE;
DROP TABLE IF EXISTS sys_user CASCADE;
DROP TABLE IF EXISTS sys_role CASCADE;
DROP TABLE IF EXISTS sys_menu CASCADE;
DROP TABLE IF EXISTS sys_permission CASCADE;
DROP TABLE IF EXISTS sys_org CASCADE;
DROP TABLE IF EXISTS sys_dict_item CASCADE;
DROP TABLE IF EXISTS sys_dict CASCADE;
DROP TABLE IF EXISTS sys_tenant CASCADE;

-- ============================================================================
-- 1. sys_tenant (租户表) - 继承 BaseEntity
-- ============================================================================
CREATE TABLE sys_tenant (
    -- BaseEntity 字段
    id              BIGINT          NOT NULL PRIMARY KEY,
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,

    -- SysTenant 特有字段
    name            VARCHAR(64)     NOT NULL,
    code            VARCHAR(32)     NOT NULL,
    contact_name    VARCHAR(64),
    status          VARCHAR(20)     NOT NULL DEFAULT 'ENABLE',
    expire_time     TIMESTAMP
);

CREATE UNIQUE INDEX uk_sys_tenant_name ON sys_tenant(name) WHERE deleted = FALSE;
CREATE UNIQUE INDEX uk_sys_tenant_code ON sys_tenant(code) WHERE deleted = FALSE;

-- ============================================================================
-- 2. sys_org (组织/部门表) - 继承 TenantAware
-- ============================================================================
CREATE TABLE sys_org (
    -- BaseEntity 字段
    id              BIGINT          NOT NULL PRIMARY KEY,
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,

    -- TenantAware 字段
    tenant_id       BIGINT          NOT NULL,

    -- SysOrg 特有字段
    name            VARCHAR(64)     NOT NULL,
    parent_id       BIGINT          -- 自关联：父组织
);

CREATE INDEX idx_sys_org_tenant ON sys_org(tenant_id);
CREATE INDEX idx_sys_org_parent ON sys_org(parent_id);

-- ============================================================================
-- 3. sys_menu (菜单表) - 继承 TenantAware
-- ============================================================================
CREATE TABLE sys_menu (
    -- BaseEntity 字段
    id              BIGINT          NOT NULL PRIMARY KEY,
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,

    -- TenantAware 字段
    tenant_id       BIGINT          NOT NULL,

    -- SysMenu 特有字段
    title           VARCHAR(64)     NOT NULL,
    parent_id       BIGINT,         -- 自关联：父菜单
    type            VARCHAR(20)     NOT NULL, -- DIRECTORY, MENU, BUTTON
    path            VARCHAR(255),
    component       VARCHAR(255),
    permission      VARCHAR(128),
    icon            VARCHAR(64),
    sort_order      INT             NOT NULL DEFAULT 0
);

CREATE INDEX idx_sys_menu_tenant ON sys_menu(tenant_id);
CREATE INDEX idx_sys_menu_parent ON sys_menu(parent_id);
-- 同一层级下 title 唯一 (组合键)
CREATE UNIQUE INDEX uk_sys_menu_title_parent ON sys_menu(tenant_id, parent_id, title) WHERE deleted = FALSE;

-- ============================================================================
-- 4. sys_permission (权限表) - 继承 TenantAware
-- ============================================================================
CREATE TABLE sys_permission (
    -- BaseEntity 字段
    id              BIGINT          NOT NULL PRIMARY KEY,
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,

    -- TenantAware 字段
    tenant_id       BIGINT          NOT NULL,

    -- SysPermission 特有字段
    code            VARCHAR(128)    NOT NULL,
    name            VARCHAR(64)     NOT NULL,
    group_name      VARCHAR(64),
    description     VARCHAR(255)
);

CREATE INDEX idx_sys_permission_tenant ON sys_permission(tenant_id);
-- 租户内权限码唯一
CREATE UNIQUE INDEX uk_sys_permission_code ON sys_permission(tenant_id, code) WHERE deleted = FALSE;

-- ============================================================================
-- 5. sys_role (角色表) - 继承 TenantAware
-- ============================================================================
CREATE TABLE sys_role (
    -- BaseEntity 字段
    id              BIGINT          NOT NULL PRIMARY KEY,
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,

    -- TenantAware 字段
    tenant_id       BIGINT          NOT NULL,

    -- SysRole 特有字段
    code            VARCHAR(64)     NOT NULL,
    name            VARCHAR(64)     NOT NULL,
    description     VARCHAR(255),
    data_scope      VARCHAR(20)     NOT NULL DEFAULT 'SELF' -- ALL, SAME_DEPT, SAME_DEPT_TREE, CUSTOM, SELF
);

CREATE INDEX idx_sys_role_tenant ON sys_role(tenant_id);
-- 租户内角色码唯一
CREATE UNIQUE INDEX uk_sys_role_code ON sys_role(tenant_id, code) WHERE deleted = FALSE;

-- ============================================================================
-- 6. sys_user (用户表) - 继承 TenantAware
-- ============================================================================
CREATE TABLE sys_user (
    -- BaseEntity 字段
    id              BIGINT          NOT NULL PRIMARY KEY,
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,

    -- TenantAware 字段
    tenant_id       BIGINT          NOT NULL,

    -- SysUser 特有字段
    username        VARCHAR(64)     NOT NULL,
    nickname        VARCHAR(64),
    password_hash   VARCHAR(255)    NOT NULL,
    email           VARCHAR(128),
    phone           VARCHAR(20),
    avatar          VARCHAR(512),
    status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, LOCKED, DISABLED

    -- 关联：所属组织
    org_id          BIGINT
);

CREATE INDEX idx_sys_user_tenant ON sys_user(tenant_id);
CREATE INDEX idx_sys_user_org ON sys_user(org_id);
-- 租户内用户名唯一
CREATE UNIQUE INDEX uk_sys_user_username ON sys_user(tenant_id, username) WHERE deleted = FALSE;

-- ============================================================================
-- 7. sys_dict (字典表) - 继承 TenantAware
-- ============================================================================
CREATE TABLE sys_dict (
    -- BaseEntity 字段
    id              BIGINT          NOT NULL PRIMARY KEY,
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,

    -- TenantAware 字段 (0 表示系统级字典)
    tenant_id       BIGINT          NOT NULL DEFAULT 0,

    -- SysDict 特有字段
    code            VARCHAR(64)     NOT NULL,
    name            VARCHAR(64)     NOT NULL,
    description     VARCHAR(255),
    status          VARCHAR(20)     NOT NULL DEFAULT 'ENABLE'
);

CREATE INDEX idx_sys_dict_tenant ON sys_dict(tenant_id);
CREATE UNIQUE INDEX uk_sys_dict_code ON sys_dict(tenant_id, code) WHERE deleted = FALSE;

-- ============================================================================
-- 8. sys_dict_item (字典项表) - 继承 BaseEntity
-- ============================================================================
CREATE TABLE sys_dict_item (
    -- BaseEntity 字段
    id              BIGINT          NOT NULL PRIMARY KEY,
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,

    -- SysDictItem 特有字段
    dict_id         BIGINT          NOT NULL,
    label           VARCHAR(64)     NOT NULL,
    value           VARCHAR(128)    NOT NULL,
    sort_order      INT             NOT NULL DEFAULT 0,
    status          VARCHAR(20)     NOT NULL DEFAULT 'ENABLE',
    color           VARCHAR(32),
    extra           VARCHAR(512)    -- 扩展数据 (JSON)
);

CREATE INDEX idx_sys_dict_item_dict ON sys_dict_item(dict_id);

-- ============================================================================
-- 9. 关联表 (中间表)
-- ============================================================================

-- 用户-角色关联
CREATE TABLE sys_user_role_mapping (
    user_id         BIGINT          NOT NULL,
    role_id         BIGINT          NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- 角色-菜单关联
CREATE TABLE sys_role_menu_mapping (
    role_id         BIGINT          NOT NULL,
    menu_id         BIGINT          NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

-- 角色-权限关联
CREATE TABLE sys_role_permission_mapping (
    role_id         BIGINT          NOT NULL,
    permission_id   BIGINT          NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

-- 角色-组织关联 (用于自定义数据权限)
CREATE TABLE sys_role_org_mapping (
    role_id         BIGINT          NOT NULL,
    org_id          BIGINT          NOT NULL,
    PRIMARY KEY (role_id, org_id)
);

-- ============================================================================
-- 10. 外键约束
-- ============================================================================

-- 组织自关联
ALTER TABLE sys_org ADD CONSTRAINT fk_sys_org_parent
    FOREIGN KEY (parent_id) REFERENCES sys_org(id) ON DELETE SET NULL;

-- 菜单自关联
ALTER TABLE sys_menu ADD CONSTRAINT fk_sys_menu_parent
    FOREIGN KEY (parent_id) REFERENCES sys_menu(id) ON DELETE SET NULL;

-- 用户 -> 组织
ALTER TABLE sys_user ADD CONSTRAINT fk_sys_user_org
    FOREIGN KEY (org_id) REFERENCES sys_org(id) ON DELETE SET NULL;

-- 字典项 -> 字典
ALTER TABLE sys_dict_item ADD CONSTRAINT fk_sys_dict_item_dict
    FOREIGN KEY (dict_id) REFERENCES sys_dict(id) ON DELETE CASCADE;

-- 用户-角色关联
ALTER TABLE sys_user_role_mapping ADD CONSTRAINT fk_ur_user
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE;
ALTER TABLE sys_user_role_mapping ADD CONSTRAINT fk_ur_role
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE;

-- 角色-菜单关联
ALTER TABLE sys_role_menu_mapping ADD CONSTRAINT fk_rm_role
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE;
ALTER TABLE sys_role_menu_mapping ADD CONSTRAINT fk_rm_menu
    FOREIGN KEY (menu_id) REFERENCES sys_menu(id) ON DELETE CASCADE;

-- 角色-权限关联
ALTER TABLE sys_role_permission_mapping ADD CONSTRAINT fk_rp_role
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE;
ALTER TABLE sys_role_permission_mapping ADD CONSTRAINT fk_rp_perm
    FOREIGN KEY (permission_id) REFERENCES sys_permission(id) ON DELETE CASCADE;

-- 角色-组织关联
ALTER TABLE sys_role_org_mapping ADD CONSTRAINT fk_ro_role
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE;
ALTER TABLE sys_role_org_mapping ADD CONSTRAINT fk_ro_org
    FOREIGN KEY (org_id) REFERENCES sys_org(id) ON DELETE CASCADE;

