package io.github.faustofan.admin.system.adapter.rest

import io.github.faustofan.admin.common.api.ApiResponse
import io.github.faustofan.admin.common.api.PageRequestDTO
import io.github.faustofan.admin.common.api.PageResponse
import io.github.faustofan.admin.system.app.command.SysUserCommandService
import io.github.faustofan.admin.system.app.query.SysUserQueryService
import io.github.faustofan.admin.system.domain.entity.UserStatus
import io.github.faustofan.admin.system.dto.SysUserCreateCommand
import io.github.faustofan.admin.system.dto.SysUserSearchQuery
import io.github.faustofan.admin.system.dto.SysUserUpdateCommand
import io.github.faustofan.admin.system.dto.SysUserView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * 用户管理接口
 */
@Tag(name = "用户管理", description = "用户的增删改查、状态变更等")
@RestController
@RequestMapping("/api/v1/sys/users")
class SysUserController(
    private val commandService: SysUserCommandService,
    private val queryService: SysUserQueryService
) {

    // ==========================================
    // 查询接口 (Query)
    // ==========================================

    /**
     * 分页查询用户列表
     */
    @Operation(summary = "分页查询用户列表")
    @GetMapping
    @PreAuthorize("hasAuthority('sys:user:list')")
    fun search(
        query: SysUserSearchQuery,
        page: PageRequestDTO
    ): PageResponse<SysUserView> {
        return PageResponse.from(queryService.search(query, page))
    }

    /**
     * 获取用户详情
     */
    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:query')")
    fun findById(@PathVariable id: Long): ApiResponse<SysUserView> {
        val user = queryService.findById(id)
        return ApiResponse.success(user)
    }

    // ==========================================
    // 命令接口 (Command)
    // ==========================================

    /**
     * 创建用户
     */
    @Operation(summary = "创建用户")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    fun create(@RequestBody cmd: SysUserCreateCommand): ApiResponse<Long> {
        val id = commandService.create(cmd)
        return ApiResponse.success(id)
    }

    /**
     * 更新用户
     */
    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    fun update(
        @PathVariable id: Long,
        @RequestBody cmd: SysUserUpdateCommand
    ): ApiResponse<Long> {
        val userId = commandService.update(id, cmd)
        return ApiResponse.success(userId)
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    fun delete(@PathVariable id: Long): ApiResponse<String> {
        commandService.delete(id)
        return ApiResponse.success("success")
    }

    /**
     * 批量删除用户
     */
    @Operation(summary = "批量删除用户")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:user:delete')")
    fun batchDelete(@RequestBody ids: List<Long>): ApiResponse<String> {
        commandService.batchDelete(ids)
        return ApiResponse.success("success")
    }

    // ==========================================
    // 业务动作接口
    // ==========================================

    /**
     * 变更用户状态
     */
    @Operation(summary = "变更用户状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    fun changeStatus(
        @PathVariable id: Long,
        @RequestParam status: UserStatus
    ): ApiResponse<String> {
        commandService.changeStatus(id, status)
        return ApiResponse.success("success")
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置用户密码")
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('sys:user:resetPwd')")
    fun resetPassword(
        @PathVariable id: Long,
        @RequestBody request: ResetPasswordRequest
    ): ApiResponse<String> {
        commandService.resetPassword(id, request.newPassword)
        return ApiResponse.success("success")
    }
}

/**
 * 重置密码请求
 */
data class ResetPasswordRequest(
    val newPassword: String
)

