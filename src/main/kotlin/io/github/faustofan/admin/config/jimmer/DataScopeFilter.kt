package io.github.faustofan.admin.config.jimmer

import io.github.faustofan.admin.common.*
import io.github.faustofan.admin.common.context.AppContextHolder
import io.github.faustofan.admin.common.context.DataScope
import io.github.faustofan.admin.common.context.RoleDataScopeInfo
import io.github.faustofan.admin.common.model.DataScoped
import io.github.faustofan.admin.common.model.createdBy
import io.github.faustofan.admin.common.model.ownerOrgId
import io.github.faustofan.admin.system.infra.repository.SysOrgRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.babyfish.jimmer.sql.kt.filter.KFilter
import org.babyfish.jimmer.sql.kt.filter.KFilterArgs
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

/**
 * 数据权限过滤器
 *
 * 根据用户角色的 DataScope 配置，自动添加数据权限过滤条件
 * 适用于所有实现 DataScoped 接口的业务实体
 *
 * DataScope 规则：
 * - ALL: 查看所有数据（无过滤）
 * - SAME_DEPT: 只能查看本部门数据
 * - SAME_DEPT_TREE: 查看本部门及所有子部门数据
 * - CUSTOM: 查看指定部门列表的数据
 * - SELF: 只能查看自己创建的数据
 */
@Component
class DataScopeFilter(
    @Lazy private val orgRepository: SysOrgRepository
) : KFilter<DataScoped> {

    override fun filter(args: KFilterArgs<DataScoped>) {
        val context = AppContextHolder.get()

        // 超级管理员跳过数据权限过滤
        if (context.isSuperAdmin) {
            return
        }

        val roles = context.currentUserRoles ?: return
        if (roles.isEmpty()) return

        // 获取用户的最高数据权限
        val effectiveScope = getEffectiveDataScope(roles)

        args.apply {
            when (effectiveScope) {
                DataScope.ALL -> {
                    // 不添加额外过滤条件
                }

                DataScope.SAME_DEPT -> {
                    // 只能查看本部门数据
                    val orgId = context.orgId
                    if (orgId != null) {
                        where(table.ownerOrgId eq orgId)
                    } else {
                        // 没有部门则只能看自己的数据
                        where(table.createdBy eq context.userId)
                    }
                }

                DataScope.SAME_DEPT_TREE -> {
                    // 查看本部门及子部门数据
                    val orgId = context.orgId
                    if (orgId != null) {
                        val childOrgIds = getChildOrgIds(orgId, context.tenantId)
                        val allOrgIds = childOrgIds + orgId
                        where(table.ownerOrgId valueIn allOrgIds)
                    } else {
                        where(table.createdBy eq context.userId)
                    }
                }

                DataScope.CUSTOM -> {
                    // 查看指定部门数据
                    val customOrgIds = roles
                        .filter { it.dataScope == DataScope.CUSTOM }
                        .flatMap { it.specificOrgIds }
                        .toSet()

                    if (customOrgIds.isNotEmpty()) {
                        where(table.ownerOrgId valueIn customOrgIds)
                    } else {
                        where(table.createdBy eq context.userId)
                    }
                }

                DataScope.SELF -> {
                    // 只能查看自己创建的数据
                    where(table.createdBy eq context.userId)
                }
            }
        }
    }

    /**
     * 获取用户的有效数据权限（取最大权限）
     */
    private fun getEffectiveDataScope(roles: List<RoleDataScopeInfo>): DataScope {
        val priority = mapOf(
            DataScope.ALL to 5,
            DataScope.SAME_DEPT_TREE to 4,
            DataScope.SAME_DEPT to 3,
            DataScope.CUSTOM to 2,
            DataScope.SELF to 1
        )

        return roles
            .maxByOrNull { priority[it.dataScope] ?: 0 }
            ?.dataScope ?: DataScope.SELF
    }

    /**
     * 获取指定组织的所有子组织ID
     * 使用递归查询获取完整的子树
     */
    private fun getChildOrgIds(parentOrgId: Long, tenantId: Long?): Set<Long> {
        // TODO: 实现递归查询子部门
        // 可以使用 CTE 递归查询或在代码中递归
        // 这里先返回空集合，后续完善
        return emptySet()
    }
}

