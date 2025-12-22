INSERT INTO
    public.sys_abac_policy (
        id,
        tenant_id,
        name,
        target_resource,
        policy_type,
        policy_content,
        description,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        1,
        1,
        '部门数据隔离',
        'sys_user',
        'SQL_FRAGMENT',
        'this.org_id = :user.orgId',
        '只能查看本部门用户',
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_abac_policy (
        id,
        tenant_id,
        name,
        target_resource,
        policy_type,
        policy_content,
        description,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        999999999,
        999999999,
        '开发数据策略',
        'sys_user',
        'SQL_FRAGMENT',
        '1=1',
        '查看所有',
        false,
        '2025-12-21 20:18:51.521539',
        '2025-12-21 20:18:51.521539',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_menu (
        id,
        parent_id,
        menu_name,
        menu_type,
        route_path,
        component_path,
        perm_code,
        icon,
        sort_order,
        visible,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        1,
        null,
        '系统管理',
        'DIRECTORY',
        '/system',
        'Layout',
        null,
        'settings',
        1,
        true,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_menu (
        id,
        parent_id,
        menu_name,
        menu_type,
        route_path,
        component_path,
        perm_code,
        icon,
        sort_order,
        visible,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        2,
        1,
        '用户管理',
        'MENU',
        '/system/user',
        '/system/user/index',
        'system:user:list',
        'user',
        1,
        true,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_menu (
        id,
        parent_id,
        menu_name,
        menu_type,
        route_path,
        component_path,
        perm_code,
        icon,
        sort_order,
        visible,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        3,
        1,
        '角色管理',
        'MENU',
        '/system/role',
        '/system/role/index',
        'system:role:list',
        'lock',
        2,
        true,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_menu (
        id,
        parent_id,
        menu_name,
        menu_type,
        route_path,
        component_path,
        perm_code,
        icon,
        sort_order,
        visible,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        4,
        1,
        '菜单管理',
        'MENU',
        '/system/menu',
        '/system/menu/index',
        'system:menu:list',
        'menu',
        3,
        true,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_menu (
        id,
        parent_id,
        menu_name,
        menu_type,
        route_path,
        component_path,
        perm_code,
        icon,
        sort_order,
        visible,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        5,
        1,
        '部门管理',
        'MENU',
        '/system/dept',
        '/system/dept/index',
        'system:dept:list',
        'apartment',
        4,
        true,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_menu (
        id,
        parent_id,
        menu_name,
        menu_type,
        route_path,
        component_path,
        perm_code,
        icon,
        sort_order,
        visible,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        6,
        2,
        '新增用户',
        'BUTTON',
        null,
        null,
        'system:user:add',
        null,
        1,
        true,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_menu (
        id,
        parent_id,
        menu_name,
        menu_type,
        route_path,
        component_path,
        perm_code,
        icon,
        sort_order,
        visible,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        7,
        2,
        '修改用户',
        'BUTTON',
        null,
        null,
        'system:user:edit',
        null,
        2,
        true,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_menu (
        id,
        parent_id,
        menu_name,
        menu_type,
        route_path,
        component_path,
        perm_code,
        icon,
        sort_order,
        visible,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        8,
        2,
        '删除用户',
        'BUTTON',
        null,
        null,
        'system:user:delete',
        null,
        3,
        true,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_org (
        id,
        tenant_id,
        parent_id,
        name,
        sort_order,
        status,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        999999999,
        999999999,
        null,
        '基础设施开发部门',
        0,
        'ENABLE',
        false,
        '2025-12-21 20:08:16.885335',
        '2025-12-21 20:08:16.885335',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_org (
        id,
        tenant_id,
        parent_id,
        name,
        sort_order,
        status,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        1,
        1,
        null,
        '总部',
        0,
        'ENABLE',
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_org (
        id,
        tenant_id,
        parent_id,
        name,
        sort_order,
        status,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        2,
        1,
        1,
        '研发部',
        1,
        'ENABLE',
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (1, 1);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (1, 2);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (1, 3);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (1, 4);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (1, 5);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (1, 6);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (1, 7);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (1, 8);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (999999999, 1);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (999999999, 2);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (999999999, 3);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (999999999, 4);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (999999999, 5);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (999999999, 6);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (999999999, 7);

INSERT INTO
    public.sys_package_menu (package_id, menu_id)
VALUES
    (999999999, 8);

INSERT INTO
    public.sys_product_package (
        id,
        name,
        code,
        remark,
        status,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        1,
        '专业版套餐',
        'PRO_EDITION',
        '包含所有系统管理功能',
        'ENABLE',
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_product_package (
        id,
        name,
        code,
        remark,
        status,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        999999999,
        '开发包',
        'DEV_EDITION',
        '所有功能',
        'ENABLE',
        false,
        '2025-12-21 20:17:23.288473',
        '2025-12-21 20:17:23.288473'
    );

INSERT INTO
    public.sys_role (
        id,
        tenant_id,
        name,
        code,
        description,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        1,
        1,
        '超级管理员',
        'SUPER_ADMIN',
        '拥有所有权限',
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_role (
        id,
        tenant_id,
        name,
        code,
        description,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        2,
        1,
        '普通员工',
        'EMPLOYEE',
        '仅查看权限',
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_role (
        id,
        tenant_id,
        name,
        code,
        description,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        999999999,
        999999999,
        '开发人员',
        'DEV_ADMIN',
        '全部权限',
        false,
        '2025-12-21 20:10:38.058314',
        '2025-12-21 20:10:38.058314',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (1, 1);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (1, 2);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (1, 3);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (1, 4);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (1, 5);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (1, 6);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (1, 7);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (1, 8);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (2, 1);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (2, 2);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (2, 4);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (2, 5);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (999999999, 1);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (999999999, 2);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (999999999, 3);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (999999999, 4);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (999999999, 5);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (999999999, 6);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (999999999, 7);

INSERT INTO
    public.sys_role_menu (role_id, menu_id)
VALUES
    (999999999, 8);

INSERT INTO
    public.sys_role_policy (role_id, policy_id)
VALUES
    (2, 1);

INSERT INTO
    public.sys_role_policy (role_id, policy_id)
VALUES
    (999999999, 999999999);

INSERT INTO
    public.sys_tenant (
        id,
        name,
        tenant_code,
        package_id,
        admin_user_id,
        expire_time,
        status,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        1,
        '演示科技有限公司',
        'TENANT_DEMO',
        1,
        1,
        '2099-12-31 23:59:59.000000',
        'ENABLE',
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842'
    );

INSERT INTO
    public.sys_tenant (
        id,
        name,
        tenant_code,
        package_id,
        admin_user_id,
        expire_time,
        status,
        deleted,
        created_time,
        updated_time
    )
VALUES
    (
        999999999,
        '开发公司',
        'DEV_TENANT',
        999999999,
        999999999,
        '2099-12-31 23:59:59.000000',
        'ENABLE',
        false,
        '2025-12-21 20:11:54.641554',
        '2025-12-21 20:11:54.641554'
    );

INSERT INTO
    public.sys_user (
        id,
        tenant_id,
        org_id,
        username,
        password,
        nickname,
        email,
        mobile,
        status,
        is_super_admin,
        attributes,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        1,
        1,
        1,
        'admin',
        '$2a$10$7JB720yubVSZv5W56jdx.euT/jq/Hj.3.W.v.f/3.W.v.f',
        '租户管理员',
        'admin@example.com',
        '13800138000',
        'ACTIVE',
        true,
        null,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_user (
        id,
        tenant_id,
        org_id,
        username,
        password,
        nickname,
        email,
        mobile,
        status,
        is_super_admin,
        attributes,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        2,
        1,
        2,
        'dev_user',
        '$2a$10$7JB720yubVSZv5W56jdx.euT/jq/Hj.3.W.v.f/3.W.v.f',
        '研发小张',
        'zhang@example.com',
        '13800138001',
        'ACTIVE',
        false,
        null,
        false,
        '2025-12-21 19:51:55.782842',
        '2025-12-21 19:51:55.782842',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_user (
        id,
        tenant_id,
        org_id,
        username,
        password,
        nickname,
        email,
        mobile,
        status,
        is_super_admin,
        attributes,
        deleted,
        created_time,
        updated_time,
        created_by,
        updated_by
    )
VALUES
    (
        999999999,
        999999999,
        999999999,
        'faustofan',
        '$2a$10$fnxPEHOjzK3CirTj4vWfV.lBqAHhf7X9Hv73eZv6EgHjlV1YxDCPu',
        '开发管理员',
        'faustofanb@gmail.com',
        '15087191698',
        'ACTIVE',
        true,
        null,
        false,
        '2025-12-21 20:15:33.674313',
        '2025-12-21 20:15:33.674313',
        999999999,
        999999999
    );

INSERT INTO
    public.sys_user_role (user_id, role_id)
VALUES
    (1, 1);

INSERT INTO
    public.sys_user_role (user_id, role_id)
VALUES
    (2, 2);

INSERT INTO
    public.sys_user_role (user_id, role_id)
VALUES
    (999999999, 999999999);