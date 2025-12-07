package io.github.faustofan.admin.shared.domain.model

import org.babyfish.jimmer.sql.MappedSuperclass

/**
 * 数据权限隔离接口
 * 所有需要被"部门/个人"权限控制的业务表（如订单、日志）都必须继承此接口
 *
 * 继承 TenantAware，同时具备租户隔离和数据权限隔离能力
 */
@MappedSuperclass
interface DataScoped : TenantAware {

    // 数据归属于哪个部门/组织
    // 这里的 id 对应 SysOrg.id
    // 用于数据权限过滤（本部门、本部门及子部门等）
    val ownerOrgId: Long?
}