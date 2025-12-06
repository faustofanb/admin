package io.github.faustofan.admin.system.adapter.rest

import io.github.faustofan.admin.common.api.PageRequestDTO
import io.github.faustofan.admin.common.api.PageResponse
import io.github.faustofan.admin.system.app.command.SysTenantCommandService
import io.github.faustofan.admin.system.app.query.SysTenantQueryService
import io.github.faustofan.admin.system.domain.entity.TenantStatus
import io.github.faustofan.admin.system.dto.SysTenantCreateCommand
import io.github.faustofan.admin.system.dto.SysTenantSearchQuery
import io.github.faustofan.admin.system.dto.SysTenantUpdateCommand
import io.github.faustofan.admin.system.dto.SysTenantView
import org.springframework.web.bind.annotation.*

/**
 * 租户管理接口
 * 通常只有超级管理员(Super Admin)有权限访问此控制器
 */
@RestController
@RequestMapping("/sys/tenants")
class SysTenantController(
    private val commandService: SysTenantCommandService,
    private val queryService: SysTenantQueryService
) {

    // ==========================================
    // 查询接口 (Query)
    // ==========================================

    /**
     * 分页搜索租户
     * 支持动态条件：name, code, contactName 等 (由 SysTenantSearchQuery 定义)
     */
    @GetMapping
    fun search(
        query: SysTenantSearchQuery,
        page: PageRequestDTO
    ): PageResponse<SysTenantView> {
        return PageResponse.from(queryService.search(query, page))
    }

    /**
     * 获取租户详情
     */
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): SysTenantView? {
        return queryService.findById(id)
    }

    // ==========================================
    // 命令接口 (Command)
    // ==========================================

    /**
     * 创建租户
     */
    @PostMapping
    fun create(@RequestBody cmd: SysTenantCreateCommand): Long {
        return commandService.create(cmd)
    }

    /**
     * 修改租户
     * 注意：使用 URL 中的 ID 覆盖 DTO 中的 ID，确保安全性
     */
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody cmd: SysTenantUpdateCommand
    ): Long {
        return commandService.update(cmd)
    }

    // ==========================================
    // 业务动作接口 (Business Actions)
    // ==========================================

    /**
     * 租户续费
     * POST /sys/tenants/{id}/renewal?days=30
     */
    @PostMapping("/{id}/renewal")
    fun renew(
        @PathVariable id: Long,
        @RequestParam days: Long
    ) {
        commandService.renew(id, days)
    }

    /**
     * 更改租户状态 (启用/禁用)
     * PUT /sys/tenants/{id}/status?status=DISABLE
     */
    @PutMapping("/{id}/status")
    fun changeStatus(
        @PathVariable id: Long,
        @RequestParam status: TenantStatus
    ) {
        commandService.changeStatus(id, status)
    }
}