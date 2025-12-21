/* ==========================================================================
   基类字段说明 (所有业务表均包含)
   - created_time, updated_time
   - deleted: 逻辑删除标识 (f=正常, t=已删除)
   ========================================================================== */

/* --------------------------------------------------------------------------
   1. 平台运营域
   -------------------------------------------------------------------------- */

-- 1.1 系统菜单
CREATE TABLE sys_menu (
                          id              BIGINT NOT NULL,
                          parent_id       BIGINT,
                          menu_name       VARCHAR(50) NOT NULL,
                          menu_type       VARCHAR(10) NOT NULL,
                          route_path      VARCHAR(255),
                          component_path  VARCHAR(255),
                          perm_code       VARCHAR(100),
                          icon            VARCHAR(50),
                          sort_order      INT DEFAULT 0,
                          visible         BOOLEAN DEFAULT TRUE,
                          deleted         BOOLEAN NOT NULL DEFAULT FALSE,
                          created_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (id)
);
CREATE INDEX idx_menu_parent ON sys_menu(parent_id);
-- 唯一索引优化：同级目录下菜单名唯一，且只约束未删除的记录
-- CREATE UNIQUE INDEX uidx_menu_name ON sys_menu(parent_id, menu_name) WHERE deleted = false;

-- 1.2 产品套餐包
CREATE TABLE sys_product_package (
                                     id              BIGINT NOT NULL,
                                     name            VARCHAR(50) NOT NULL,
                                     code            VARCHAR(50) NOT NULL,
                                     remark          VARCHAR(500),
                                     status          VARCHAR(20) DEFAULT 'ENABLE',
                                     deleted         BOOLEAN NOT NULL DEFAULT FALSE, -- 新增
                                     created_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     PRIMARY KEY (id)
);
-- 关键：使用部分索引，允许存在多个已删除的同code套餐，但活动的只能有一个
CREATE UNIQUE INDEX uidx_package_code ON sys_product_package(code) WHERE deleted = false;

-- 1.3 套餐-菜单关联 (中间表通常物理删除，无需逻辑删除)
CREATE TABLE sys_package_menu (
                                  package_id      BIGINT NOT NULL,
                                  menu_id         BIGINT NOT NULL,
                                  PRIMARY KEY (package_id, menu_id)
);

/* --------------------------------------------------------------------------
   2. 租户域
   -------------------------------------------------------------------------- */
-- 2.1 租户表
CREATE TABLE sys_tenant (
                            id              BIGINT NOT NULL,
                            name            VARCHAR(100) NOT NULL,
                            tenant_code     VARCHAR(50) NOT NULL,
                            package_id      BIGINT NOT NULL,
                            admin_user_id   BIGINT,
                            expire_time     TIMESTAMP,
                            status          VARCHAR(20) NOT NULL DEFAULT 'ENABLE',
                            deleted         BOOLEAN NOT NULL DEFAULT FALSE,
                            created_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (id),
                            CONSTRAINT ck_tenant_status CHECK (status IN ('ENABLE', 'DISABLE'))
);
CREATE UNIQUE INDEX uidx_tenant_code ON sys_tenant(tenant_code) WHERE deleted = false;
/* --------------------------------------------------------------------------
   3. 租户内部权限域
   -------------------------------------------------------------------------- */

-- 3.1 组织机构
CREATE TABLE sys_org (
                         id              BIGINT NOT NULL,
                         tenant_id       BIGINT NOT NULL,
                         parent_id       BIGINT,
                         name            VARCHAR(100) NOT NULL,
                         sort_order      INT DEFAULT 0,
                         status          VARCHAR(20) DEFAULT 'ENABLE',
                         deleted         BOOLEAN NOT NULL DEFAULT FALSE, -- 新增
                         created_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         created_by      BIGINT,
                         updated_by      BIGINT,
                         PRIMARY KEY (id)
);
CREATE INDEX idx_org_tenant ON sys_org(tenant_id);

-- 3.2 用户表
CREATE TABLE sys_user (
                          id              BIGINT NOT NULL,
                          tenant_id       BIGINT NOT NULL,
                          org_id          BIGINT,
                          username        VARCHAR(50) NOT NULL,
                          password        VARCHAR(255) NOT NULL,
                          nickname        VARCHAR(50),
                          email           VARCHAR(100),
                          mobile          VARCHAR(20),
                          status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                          is_super_admin  BOOLEAN DEFAULT FALSE,
                          attributes      JSONB,
                          deleted         BOOLEAN NOT NULL DEFAULT FALSE,
                          created_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          created_by      BIGINT,
                          updated_by      BIGINT,
                          PRIMARY KEY (id),
                          CONSTRAINT ck_user_status CHECK (status IN ('ACTIVE', 'LOCKED', 'DISABLED'))
);
CREATE UNIQUE INDEX uidx_user_username ON sys_user(tenant_id, username) WHERE deleted = false;

-- 3.3 角色表
CREATE TABLE sys_role (
                          id              BIGINT NOT NULL,
                          tenant_id       BIGINT NOT NULL,
                          name            VARCHAR(50) NOT NULL,
                          code            VARCHAR(50) NOT NULL,
                          description     VARCHAR(200),
                          deleted         BOOLEAN NOT NULL DEFAULT FALSE, -- 新增
                          created_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          created_by      BIGINT,
                          updated_by      BIGINT,
                          PRIMARY KEY (id)
);
CREATE INDEX idx_role_tenant ON sys_role(tenant_id);
-- 同租户角色编码唯一
CREATE UNIQUE INDEX uidx_role_code ON sys_role(tenant_id, code) WHERE deleted = false;

-- 3.4 ABAC 策略表
CREATE TABLE sys_abac_policy (
                                 id              BIGINT NOT NULL,
                                 tenant_id       BIGINT NOT NULL,
                                 name            VARCHAR(100) NOT NULL,
                                 target_resource VARCHAR(100) NOT NULL,
                                 policy_type     VARCHAR(20) NOT NULL,
                                 policy_content  TEXT NOT NULL,
                                 description     VARCHAR(255),
                                 deleted         BOOLEAN NOT NULL DEFAULT FALSE, -- 新增
                                 created_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 created_by      BIGINT,
                                 updated_by      BIGINT,
                                 PRIMARY KEY (id)
);
CREATE INDEX idx_policy_tenant ON sys_abac_policy(tenant_id);

-- 关联表（sys_role_menu, sys_user_role, sys_role_policy, sys_package_menu）保持物理删除，不添加 deleted 字段。