package io.github.faustofan.admin.system.adapter.rest

import io.github.faustofan.admin.common.api.ApiResponse
import io.github.faustofan.admin.common.api.PageRequestDTO
import io.github.faustofan.admin.common.api.PageResponse
import io.github.faustofan.admin.config.OpenApiConfig
import io.github.faustofan.admin.system.app.command.SysTenantCommandService
import io.github.faustofan.admin.system.app.query.SysTenantQueryService
import io.github.faustofan.admin.system.domain.entity.TenantStatus
import io.github.faustofan.admin.system.dto.SysTenantCreateCommand
import io.github.faustofan.admin.system.dto.SysTenantSearchQuery
import io.github.faustofan.admin.system.dto.SysTenantUpdateCommand
import io.github.faustofan.admin.system.dto.SysTenantView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * 租户管理接口
 * 通常只有超级管理员(Super Admin)有权限访问此控制器
 */
@Tag(name = "02. 租户管理", description = "租户的增删改查、状态变更、续费等操作（仅超级管理员可访问）")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RestController
@RequestMapping("/api/v1/sys/tenants")
class SysTenantController(
    private val commandService: SysTenantCommandService,
    private val queryService: SysTenantQueryService
) {

    // ==========================================
    // 查询接口 (Query)
    // ==========================================

    /**
     * 分页搜索租户
     */
    @Operation(
        summary = "分页查询租户列表",
        description = "支持按名称、编码、联系人等条件进行动态查询，返回分页结果",
        operationId = "searchTenants"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "查询成功"
            ),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问")
        ]
    )
    @GetMapping
    @PreAuthorize("hasAuthority('sys:tenant:list')")
    fun search(
        @Parameter(description = "查询条件") query: SysTenantSearchQuery,
        @Parameter(description = "分页参数") page: PageRequestDTO
    ): PageResponse<SysTenantView> {
        return PageResponse.from(queryService.search(query, page))
    }

    /**
     * 获取租户详情
     */
    @Operation(
        summary = "获取租户详情",
        description = "根据租户 ID 获取租户的详细信息",
        operationId = "getTenantById"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "查询成功",
                content = [Content(schema = Schema(implementation = SysTenantView::class))]
            ),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "租户不存在")
        ]
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:tenant:query')")
    fun findById(
        @Parameter(description = "租户ID", required = true, example = "1")
        @PathVariable id: Long
    ): ApiResponse<SysTenantView> {
        return ApiResponse.success(queryService.findById(id))
    }

    // ==========================================
    // 命令接口 (Command)
    // ==========================================

    /**
     * 创建租户
     */
    @Operation(
        summary = "创建租户",
        description = "创建新的租户，返回新租户的 ID",
        operationId = "createTenant"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "创建成功，返回租户ID"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "409", description = "租户编码已存在")
        ]
    )
    @PostMapping
    @PreAuthorize("hasAuthority('sys:tenant:add')")
    fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "租户创建参数",
            required = true
        )
        @RequestBody cmd: SysTenantCreateCommand
    ): ApiResponse<Long> {
        return ApiResponse.success(commandService.create(cmd))
    }

    /**
     * 修改租户
     */
    @Operation(
        summary = "更新租户信息",
        description = "更新指定租户的信息",
        operationId = "updateTenant"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "更新成功"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "租户不存在")
        ]
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:tenant:edit')")
    fun update(
        @Parameter(description = "租户ID", required = true, example = "1")
        @PathVariable id: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "租户更新参数",
            required = true
        )
        @RequestBody cmd: SysTenantUpdateCommand
    ): ApiResponse<Long> {
        return ApiResponse.success(commandService.update(cmd))
    }

    /**
     * 删除租户
     */
    @Operation(
        summary = "删除租户",
        description = "逻辑删除指定的租户（软删除）",
        operationId = "deleteTenant"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "删除成功"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "租户不存在")
        ]
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:tenant:delete')")
    fun delete(
        @Parameter(description = "租户ID", required = true, example = "1")
        @PathVariable id: Long
    ): ApiResponse<String> {
        commandService.delete(id)
        return ApiResponse.success("删除成功")
    }

    // ==========================================
    // 业务动作接口 (Business Actions)
    // ==========================================

    /**
     * 租户续费
     */
    @Operation(
        summary = "租户续费",
        description = "延长租户的有效期，以天为单位",
        operationId = "renewTenant"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "续费成功"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "租户不存在")
        ]
    )
    @PostMapping("/{id}/renewal")
    @PreAuthorize("hasAuthority('sys:tenant:edit')")
    fun renew(
        @Parameter(description = "租户ID", required = true, example = "1")
        @PathVariable id: Long,
        @Parameter(description = "续费天数", required = true, example = "30")
        @RequestParam days: Long
    ): ApiResponse<String> {
        commandService.renew(id, days)
        return ApiResponse.success("续费成功")
    }

    /**
     * 更改租户状态
     */
    @Operation(
        summary = "变更租户状态",
        description = "启用或禁用租户，禁用后该租户下的所有用户将无法登录",
        operationId = "changeTenantStatus"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "状态变更成功"),
            SwaggerApiResponse(responseCode = "400", description = "请求参数错误"),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期"),
            SwaggerApiResponse(responseCode = "403", description = "无权限访问"),
            SwaggerApiResponse(responseCode = "404", description = "租户不存在")
        ]
    )
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('sys:tenant:edit')")
    fun changeStatus(
        @Parameter(description = "租户ID", required = true, example = "1")
        @PathVariable id: Long,
        @Parameter(description = "目标状态", required = true, schema = Schema(implementation = TenantStatus::class))
        @RequestParam status: TenantStatus
    ): ApiResponse<String> {
        commandService.changeStatus(id, status)
        return ApiResponse.success("状态变更成功")
    }
}