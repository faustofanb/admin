package io.github.faustofan.admin.system.adapter.rest

import io.github.faustofan.admin.common.api.ApiResponse
import io.github.faustofan.admin.common.api.PageRequestDTO
import io.github.faustofan.admin.common.api.PageResponse
import io.github.faustofan.admin.config.OpenApiConfig
import io.github.faustofan.admin.system.app.command.SysUserCommandService
import io.github.faustofan.admin.system.app.query.SysUserQueryService
import io.github.faustofan.admin.system.domain.entity.UserStatus
import io.github.faustofan.admin.system.dto.SysUserCreateCommand
import io.github.faustofan.admin.system.dto.SysUserSearchQuery
import io.github.faustofan.admin.system.dto.SysUserUpdateCommand
import io.github.faustofan.admin.system.dto.SysUserView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * 用户管理接口
 */
@Tag(name = "03. 用户管理", description = "用户的增删改查、状态变更、密码重置等操作")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
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
    @Operation(
        summary = "分页查询用户列表",
        description = "支持按用户名、昵称、手机号、状态等条件进行动态查询，返回分页结果",
        operationId = "searchUsers"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "查询成功"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问")
        ]
    )
    @GetMapping
    @PreAuthorize("hasAuthority('sys:user:list')")
    fun search(
        @Parameter(description = "查询条件") query: SysUserSearchQuery,
        @Parameter(description = "分页参数") page: PageRequestDTO
    ): PageResponse<SysUserView> {
        return PageResponse.from(queryService.search(query, page))
    }

    /**
     * 获取用户详情
     */
    @Operation(
        summary = "获取用户详情",
        description = "根据用户 ID 获取用户的详细信息",
        operationId = "getUserById"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "查询成功",
                content = [Content(schema = Schema(implementation = SysUserView::class))]
            ),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "用户不存在")
        ]
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:query')")
    fun findById(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable id: Long
    ): ApiResponse<SysUserView> {
        val user = queryService.findById(id)
        return ApiResponse.success(user)
    }

    // ==========================================
    // 命令接口 (Command)
    // ==========================================

    /**
     * 创建用户
     */
    @Operation(
        summary = "创建用户",
        description = "创建新用户，返回新用户的 ID",
        operationId = "createUser"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "创建成功，返回用户ID"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "409", description = "用户名已存在")
        ]
    )
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "用户创建参数",
            required = true
        )
        @RequestBody cmd: SysUserCreateCommand
    ): ApiResponse<Long> {
        val id = commandService.create(cmd)
        return ApiResponse.success(id)
    }

    /**
     * 更新用户
     */
    @Operation(
        summary = "更新用户信息",
        description = "更新指定用户的信息",
        operationId = "updateUser"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "更新成功"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "用户不存在")
        ]
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    fun update(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable id: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "用户更新参数",
            required = true
        )
        @RequestBody cmd: SysUserUpdateCommand
    ): ApiResponse<Long> {
        val userId = commandService.update(id, cmd)
        return ApiResponse.success(userId)
    }

    /**
     * 删除用户
     */
    @Operation(
        summary = "删除用户",
        description = "逻辑删除指定的用户（软删除）",
        operationId = "deleteUser"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "删除成功"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "用户不存在")
        ]
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    fun delete(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable id: Long
    ): ApiResponse<String> {
        commandService.delete(id)
        return ApiResponse.success("删除成功")
    }

    /**
     * 批量删除用户
     */
    @Operation(
        summary = "批量删除用户",
        description = "批量逻辑删除多个用户（软删除）",
        operationId = "batchDeleteUsers"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "删除成功"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问")
        ]
    )
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:user:delete')")
    fun batchDelete(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "用户ID列表",
            required = true,
            content = [Content(array = ArraySchema(schema = Schema(type = "integer", format = "int64")))]
        )
        @RequestBody ids: List<Long>
    ): ApiResponse<String> {
        commandService.batchDelete(ids)
        return ApiResponse.success("删除成功")
    }

    // ==========================================
    // 业务动作接口
    // ==========================================

    /**
     * 变更用户状态
     */
    @Operation(
        summary = "变更用户状态",
        description = "修改用户状态：正常、锁定、禁用等",
        operationId = "changeUserStatus"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "状态变更成功"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "用户不存在")
        ]
    )
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    fun changeStatus(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable id: Long,
        @Parameter(description = "目标状态", required = true, schema = Schema(implementation = UserStatus::class))
        @RequestParam status: UserStatus
    ): ApiResponse<String> {
        commandService.changeStatus(id, status)
        return ApiResponse.success("状态变更成功")
    }

    /**
     * 重置密码
     */
    @Operation(
        summary = "重置用户密码",
        description = "管理员重置指定用户的密码",
        operationId = "resetUserPassword"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "密码重置成功"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "用户不存在")
        ]
    )
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('sys:user:resetPwd')")
    fun resetPassword(
        @Parameter(description = "用户ID", required = true, example = "1")
        @PathVariable id: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "新密码",
            required = true
        )
        @RequestBody request: ResetPasswordRequest
    ): ApiResponse<String> {
        commandService.resetPassword(id, request.newPassword)
        return ApiResponse.success("密码重置成功")
    }
}

/**
 * 重置密码请求
 */
@Schema(description = "重置密码请求")
data class ResetPasswordRequest(
    @param:Schema(description = "新密码", required = true, example = "newPassword123", minLength = 6, maxLength = 32)
    val newPassword: String
)

