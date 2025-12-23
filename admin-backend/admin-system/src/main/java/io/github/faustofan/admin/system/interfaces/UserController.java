package io.github.faustofan.admin.system.interfaces;

import io.github.faustofan.admin.shared.common.dto.ApiResponse;
import io.github.faustofan.admin.shared.common.dto.PageRequestDto;
import io.github.faustofan.admin.shared.common.dto.PageResponse;
import io.github.faustofan.admin.shared.web.config.OpenApiConfig;
import io.github.faustofan.admin.system.application.command.UserCommandService;
import io.github.faustofan.admin.system.application.query.UserQueryService;
import io.github.faustofan.admin.system.dto.SysUserSearchQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.babyfish.jimmer.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import io.github.faustofan.admin.system.dto.SysUserCreateCommand;
import io.github.faustofan.admin.system.dto.SysUserView;

/**
 * 用户管理 API 控制器
 * <p>
 * 提供系统用户相关的接口，如创建用户等。
 */
@Tag(name = "02. 系统用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    /** 用户命令服务，用于处理用户相关的业务逻辑 */
    private final UserCommandService userCommandService;

    /** 用户查询服务，用于处理用户相关的查询操作*/
    private final UserQueryService userQueryService;
    /**
     * 构造方法，注入 UserCommandService
     *
     * @param userCommandService 用户命令服务
     */
    public UserController(final UserCommandService userCommandService, final UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    /**
     * 创建用户接口
     *
     * @param cmd 用户创建命令参数
     * @return 创建成功后返回用户ID
     */
    @Operation(
            operationId = "创建用户",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @PostMapping("/create")
    public ApiResponse<Long> create(@RequestBody @Valid SysUserCreateCommand cmd) {
        return ApiResponse.success(userCommandService.create(cmd));
    }

    /**
     * 列出用户接口
     *
     * @param page 分页请求参数
     * @param query 用户搜索查询参数
     * @return 分页的用户视图列表
     */
    @Operation(
            operationId = "分页查找用户",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @PostMapping("/list")
    public ApiResponse<Page<SysUserView>> listUsers(
            @PageableDefault @ParameterObject Pageable page,
            @RequestBody SysUserSearchQuery query
    ) {
        // 示例实现，实际逻辑应调用服务层获取用户列表
        return ApiResponse.success(userQueryService.listUsers(page, query));
    }

    // ==========================================
    // V2 版本：特定版本 (Specific Match)
    // 只有 Header 明确等于 2 时才进这里
    // ==========================================
//    @Operation(summary = "创建用户 (V2)")
//    @PostMapping(value = "/create", headers = "X-API-VERSION=2")
//    public ApiResponse<Long> createV2(@RequestBody @Validated SysUserCreateCommandV2 cmd) {
//        // ... V2 逻辑
//        return ApiResponse.success(userCommandService.createV2(cmd));
//    }
}