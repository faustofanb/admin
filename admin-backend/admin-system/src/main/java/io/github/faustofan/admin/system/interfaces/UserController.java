package io.github.faustofan.admin.system.interfaces;

import io.github.faustofan.admin.shared.common.dto.ApiResponse;
import io.github.faustofan.admin.shared.web.config.OpenApiConfig;
import io.github.faustofan.admin.system.application.command.UserCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.faustofan.admin.system.dto.SysUserCreateCommand;

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

    /**
     * 构造方法，注入 UserCommandService
     *
     * @param userCommandService 用户命令服务
     */
    public UserController(final UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * 创建用户接口
     *
     * @param cmd 用户创建命令参数
     * @return 创建成功后返回用户ID
     */
    @Operation(
            operationId = "createUser",
            security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    )
    @PostMapping("/create")
    public ApiResponse<Long> create(@RequestBody @Valid SysUserCreateCommand cmd) {
        return ApiResponse.success(userCommandService.create(cmd));
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