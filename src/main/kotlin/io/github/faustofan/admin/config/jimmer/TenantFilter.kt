package io.github.faustofan.admin.config.jimmer

import io.github.faustofan.admin.common.context.AppContextHolder
import io.github.faustofan.admin.common.model.TenantAware
import io.github.faustofan.admin.common.model.tenantId
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.filter.KFilter
import org.babyfish.jimmer.sql.kt.filter.KFilterArgs
import org.springframework.stereotype.Component

/**
 * 租户全局过滤器
 *
 * 自动为所有实现 TenantAware 接口的实体添加租户条件过滤
 * 确保数据隔离，用户只能访问本租户的数据
 *
 * 注意：超级管理员可以绕过此过滤器访问所有租户数据
 */
@Component
class TenantFilter : KFilter<TenantAware> {

    override fun filter(args: KFilterArgs<TenantAware>) {
        val context = AppContextHolder.get()

        // 超级管理员跳过租户过滤
        if (context.isSuperAdmin) {
            return
        }

        val tenantId = context.tenantId
        if (tenantId != null) {
            args.apply {
                where(table.tenantId eq tenantId)
            }
        }
    }
}

