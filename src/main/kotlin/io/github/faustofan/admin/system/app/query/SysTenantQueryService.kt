package io.github.faustofan.admin.system.app.query

import io.github.faustofan.admin.system.domain.entity.SysTenant
import io.github.faustofan.admin.system.domain.entity.createdTime
import io.github.faustofan.admin.system.domain.entity.name
import io.github.faustofan.admin.system.dto.SysTenantSearchQuery
import io.github.faustofan.admin.system.dto.SysTenantView
import io.github.faustofan.admin.system.infra.repository.SysTenantRepository
import org.babyfish.jimmer.spring.SqlClients
import org.babyfish.jimmer.spring.repository.fetchSpringPage
import org.babyfish.jimmer.spring.repository.orderBy
import org.babyfish.jimmer.sql.JSqlClient
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.desc
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SysTenantQueryService(
    private val tenantRepository: SysTenantRepository,
    private val sql: KSqlClient
) {

    fun findById(id: Long): SysTenantView? {
        return tenantRepository.findNullable(id)?.let {
            SysTenantView(it)
        }
    }

//    fun search(query: SysTenantSearchQuery, pageable: Pageable): Page<SysTenantView> {
//        return sql.createQuery(SysTenantView::class) {
//            where()
//            select(table)
//        }.fetchSpringPage(pageable)
//    }
}