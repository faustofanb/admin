package io.github.faustofan.admin.system.app.query

import io.github.faustofan.admin.common.api.PageRequestDTO
import io.github.faustofan.admin.system.dto.SysUserSearchQuery
import io.github.faustofan.admin.system.dto.SysUserView
import io.github.faustofan.admin.system.infra.repository.SysUserRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 用户查询服务
 * 处理用户的查询操作
 */
@Service
@Transactional(readOnly = true)
class SysUserQueryService(
    private val userRepository: SysUserRepository
) {

    /**
     * 根据 ID 查询用户详情
     */
    fun findById(id: Long): SysUserView? {
        return userRepository.findNullable(id)?.let {
            SysUserView(it)
        }
    }

    /**
     * 分页查询用户列表
     */
    fun search(query: SysUserSearchQuery, page: PageRequestDTO): Page<SysUserView> {
        return userRepository.findPage(query, page)
    }
}

