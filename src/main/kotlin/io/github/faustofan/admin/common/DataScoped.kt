package io.github.faustofan.admin.common

import org.babyfish.jimmer.sql.MappedSuperclass

/**
 * 数据权限隔离接口
 * 所有需要被"部门/个人"权限控制的业务表（如订单、日志）都必须继承此接口
 */
@MappedSuperclass
interface DataScoped : TenantAware {

    // 数据归属于哪个用户 (创建人)
    val createdBy: String

    // 数据归属于哪个部门/组织
    // 这里的 id 对应 SysOrg.id
    val orgId: Long?
}