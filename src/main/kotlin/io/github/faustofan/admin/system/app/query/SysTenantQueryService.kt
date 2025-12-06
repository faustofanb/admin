package io.github.faustofan.admin.system.app.query

import io.github.faustofan.admin.common.api.PageRequestDTO
import io.github.faustofan.admin.system.dto.SysTenantSearchQuery
import io.github.faustofan.admin.system.dto.SysTenantView
import io.github.faustofan.admin.system.infra.repository.SysTenantRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 服务类，用于处理与租户查询相关的操作。
 *
 * @property tenantRepository 租户数据仓库，用于访问租户数据。
 */
@Service
@Transactional(readOnly = true)
class SysTenantQueryService(
    private val tenantRepository: SysTenantRepository,
) {

    /**
     * 根据租户ID查询租户信息。
     *
     * @param id 租户的唯一标识符。
     * @return 如果找到租户，则返回对应的`SysTenantView`对象；否则返回`null`。
     */
    fun findById(id: Long): SysTenantView? {
        return tenantRepository.findNullable(id)?.let {
            SysTenantView(it)
        }
    }

    /**
     * 根据查询条件和分页信息搜索租户。
     *
     * @param query 包含搜索条件的`SysTenantSearchQuery`对象。
     * @param pageable 分页信息。
     * @return 包含`SysTenantView`对象的分页结果。
     */
    fun search(query: SysTenantSearchQuery, page: PageRequestDTO): Page<SysTenantView> {
        return tenantRepository.findPage(query, page)
    }
}