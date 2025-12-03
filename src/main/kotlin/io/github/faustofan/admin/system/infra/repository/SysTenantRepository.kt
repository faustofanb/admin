package io.github.faustofan.admin.system.infra.repository

import io.github.faustofan.admin.common.PageRequestDTO
import io.github.faustofan.admin.system.domain.entity.SysTenant
import io.github.faustofan.admin.system.domain.entity.createdTime
import io.github.faustofan.admin.system.domain.entity.name
import io.github.faustofan.admin.system.domain.entity.status
import io.github.faustofan.admin.system.dto.SysTenantSearchQuery
import io.github.faustofan.admin.system.dto.SysTenantView
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.spring.repository.fetchSpringPage
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.desc
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.`ilike?`
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

/**
 * 仓库接口，用于管理与`SysTenant`实体相关的数据库操作。
 */
@Repository
interface SysTenantRepository : KRepository<SysTenant, Long> {

    /**
     * 根据租户代码查询租户信息。
     *
     * @param code 租户的唯一代码。
     * @return 如果找到租户，则返回对应的`SysTenant`对象；否则返回`null`。
     */
//    fun findByCode(code: String): SysTenant?
    fun findByCodeIsLikeIgnoreCase(code: String): List<SysTenant>
    /**
     * 根据查询条件和分页信息获取租户分页数据。
     *
     * @param query 包含搜索条件的`SysTenantSearchQuery`对象。
     * @param pageable 分页信息。
     * @return 包含`SysTenantView`对象的分页结果。
     */
    fun findPage(query: SysTenantSearchQuery, page: PageRequestDTO): Page<SysTenantView> {
        return sql.createQuery(SysTenant::class) {
            where {
                query.status?.let {
                    table.status eq it
                }
                query.name?.let {
                    table.name `ilike?` it
                }
            }
            orderBy(table.createdTime.desc())
            orderBy(table.name.asc())
            select(table.fetch(SysTenantView::class))
        }.fetchSpringPage(page.toPageable())
    }
}