package io.github.faustofan.admin.system.infra.config

import io.github.faustofan.admin.common.AppContext
import io.github.faustofan.admin.common.AppContextHolder
import io.github.faustofan.admin.common.TenantAware
import io.github.faustofan.admin.common.tenantId
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.filter.KFilter
import org.babyfish.jimmer.sql.kt.filter.KFilterArgs
import org.springframework.stereotype.Component

/**
 * 租户过滤器，用于根据当前应用上下文中的租户ID过滤数据。
 *
 * 该过滤器实现了 KFilter 接口，适用于实现 TenantAware 接口的实体。
 * 当应用上下文中存在租户ID时，会自动为查询添加租户ID的过滤条件。
 *
 * @property appContext 应用上下文，默认从 AppContextHolder 获取。
 */
@Component
class TenantFilter(
    private val appContext: AppContext = AppContextHolder.get()
) : KFilter<TenantAware> {

    /**
     * 根据租户ID过滤查询结果。
     *
     * 如果 appContext 中存在租户ID，则为查询添加 where 条件，仅返回匹配租户ID的数据。
     *
     * @param args 查询参数，包含表信息和条件构建方法。
     */
    override fun filter(args: KFilterArgs<TenantAware>) {
        appContext.tenantId?.let {
            args.apply {
                where(table.tenantId.eq(it))
            }
        }
    }

}