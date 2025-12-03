-- liquibase formatted sql
-- changeset faustofan:002-initial-data-pg

INSERT INTO sys_tenant (name, code, status, deleted)
VALUES ('系统默认租户', 'master', 'ENABLE', FALSE);

INSERT INTO sys_org (name, parent_id, tenant_id, created_by, deleted)
VALUES ('总公司', NULL, 'master', 'system', FALSE);

INSERT INTO sys_user (username, password_hash, status, tenant_id, org_id, created_by, deleted)
VALUES ('admin', '$2a$10$YourBcryptHashHere...', 'ACTIVE', 'master', 1, 'system', FALSE);

INSERT INTO sys_role (code, name, data_scope, tenant_id, deleted)
VALUES ('super_admin', '超级管理员', 'ALL', 'master', FALSE);

INSERT INTO sys_user_role_mapping (user_id, role_id) VALUES (1, 1);