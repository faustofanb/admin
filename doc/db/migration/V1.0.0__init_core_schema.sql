-- ----------------------------------------------------------------------------------------------------------------
-- 1. 租户表 (sys_tenant)
-- ----------------------------------------------------------------------------------------------------------------
CREATE TABLE sys_tenant (
    id              BIGINT          NOT NULL,
    name            VARCHAR(64)     NOT NULL,
    code            VARCHAR(32)     NOT NULL,
    contact_name    VARCHAR(32),
    contact_phone   VARCHAR(20),
    status          INT             NOT NULL DEFAULT 1, -- 1: 正常, 0: 禁用
    start_time      TIMESTAMP,
    end_time        TIMESTAMP,
    domain          VARCHAR(128),   -- 租户独立域名(可选)
    
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    
    CONSTRAINT pk_sys_tenant PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uk_sys_tenant_code ON sys_tenant(code);


-- ----------------------------------------------------------------------------------------------------------------
-- 2. 部门表 (sys_dept)
-- ----------------------------------------------------------------------------------------------------------------
CREATE TABLE sys_dept (
    id              BIGINT          NOT NULL,
    tenant_id       BIGINT          NOT NULL,
    parent_id       BIGINT          NOT NULL DEFAULT 0,
    name            VARCHAR(64)     NOT NULL,
    sort            INT             NOT NULL DEFAULT 0,
    leader          VARCHAR(32),
    phone           VARCHAR(20),
    email           VARCHAR(64),
    status          INT             NOT NULL DEFAULT 1,
    
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    
    CONSTRAINT pk_sys_dept PRIMARY KEY (id)
);

CREATE INDEX idx_sys_dept_tenant ON sys_dept(tenant_id);
CREATE INDEX idx_sys_dept_parent ON sys_dept(parent_id);


-- ----------------------------------------------------------------------------------------------------------------
-- 3. 用户表 (sys_user)
-- ----------------------------------------------------------------------------------------------------------------
CREATE TABLE sys_user (
    id              BIGINT          NOT NULL,
    tenant_id       BIGINT          NOT NULL,
    dept_id         BIGINT,
    username        VARCHAR(64)     NOT NULL,
    password        VARCHAR(128)    NOT NULL, -- BCrypt
    nickname        VARCHAR(64),
    email           VARCHAR(64),
    phone           VARCHAR(20),
    avatar          VARCHAR(255),
    gender          INT             DEFAULT 0, -- 0: 未知, 1: 男, 2: 女
    status          INT             NOT NULL DEFAULT 1,
    login_ip        VARCHAR(50),
    login_date      TIMESTAMP,
    
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    
    CONSTRAINT pk_sys_user PRIMARY KEY (id)
);

CREATE INDEX idx_sys_user_tenant ON sys_user(tenant_id);
CREATE UNIQUE INDEX uk_sys_user_username ON sys_user(tenant_id, username); -- 租户内用户名唯一


-- ----------------------------------------------------------------------------------------------------------------
-- 4. 角色表 (sys_role)
-- ----------------------------------------------------------------------------------------------------------------
CREATE TABLE sys_role (
    id              BIGINT          NOT NULL,
    tenant_id       BIGINT          NOT NULL,
    name            VARCHAR(64)     NOT NULL,
    code            VARCHAR(32)     NOT NULL,
    sort            INT             NOT NULL DEFAULT 0,
    status          INT             NOT NULL DEFAULT 1,
    data_scope      INT             DEFAULT 1, -- 1: 全部, 2: 本部门, 3: 本人 ...
    remark          VARCHAR(255),
    
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    
    CONSTRAINT pk_sys_role PRIMARY KEY (id)
);

CREATE INDEX idx_sys_role_tenant ON sys_role(tenant_id);


-- ----------------------------------------------------------------------------------------------------------------
-- 5. 菜单/权限表 (sys_menu)
-- ----------------------------------------------------------------------------------------------------------------
CREATE TABLE sys_menu (
    id              BIGINT          NOT NULL,
    parent_id       BIGINT          NOT NULL DEFAULT 0,
    title           VARCHAR(64)     NOT NULL,
    name            VARCHAR(64),    -- 路由名称
    path            VARCHAR(128),   -- 路由路径
    component       VARCHAR(128),   -- 组件路径
    icon            VARCHAR(64),
    sort            INT             NOT NULL DEFAULT 0,
    type            INT             NOT NULL, -- 0: 目录, 1: 菜单, 2: 按钮
    permission      VARCHAR(64),    -- 权限标识 (sys:user:add)
    status          INT             NOT NULL DEFAULT 1,
    visible         BOOLEAN         DEFAULT TRUE,
    keep_alive      BOOLEAN         DEFAULT TRUE,
    
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    
    CONSTRAINT pk_sys_menu PRIMARY KEY (id)
);

CREATE INDEX idx_sys_menu_parent ON sys_menu(parent_id);


-- ----------------------------------------------------------------------------------------------------------------
-- 6. 关联表 (sys_user_role, sys_role_menu)
-- ----------------------------------------------------------------------------------------------------------------
CREATE TABLE sys_user_role (
    user_id         BIGINT          NOT NULL,
    role_id         BIGINT          NOT NULL,
    CONSTRAINT pk_sys_user_role PRIMARY KEY (user_id, role_id)
);

CREATE TABLE sys_role_menu (
    role_id         BIGINT          NOT NULL,
    menu_id         BIGINT          NOT NULL,
    CONSTRAINT pk_sys_role_menu PRIMARY KEY (role_id, menu_id)
);


-- ----------------------------------------------------------------------------------------------------------------
-- 7. 字典表 (sys_dict) - 可选，但通常必须
-- ----------------------------------------------------------------------------------------------------------------
CREATE TABLE sys_dict (
    id              BIGINT          NOT NULL,
    tenant_id       BIGINT          NOT NULL DEFAULT 0, -- 0 表示系统级字典
    code            VARCHAR(64)     NOT NULL,
    name            VARCHAR(64)     NOT NULL,
    remark          VARCHAR(255),
    status          INT             NOT NULL DEFAULT 1,
    
    created_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    
    CONSTRAINT pk_sys_dict PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_sys_dict_code ON sys_dict(tenant_id, code);

CREATE TABLE sys_dict_item (
    id              BIGINT          NOT NULL,
    dict_id         BIGINT          NOT NULL,
    label           VARCHAR(64)     NOT NULL,
    value           VARCHAR(64)     NOT NULL,
    sort            INT             DEFAULT 0,
    status          INT             DEFAULT 1,
    color           VARCHAR(32),    -- 标签颜色
    
    CONSTRAINT pk_sys_dict_item PRIMARY KEY (id)
);
CREATE INDEX idx_sys_dict_item_dict ON sys_dict_item(dict_id);
