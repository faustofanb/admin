package io.github.faustofan.admin.shared.infrastructure.exception

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 全局错误码定义
 * 参考阿里集团内部风格：
 *  1xxx 系统级异常
 *  2xxx 业务异常
 *  3xxx 参数校验异常
 *  4xxx 第三方调用异常
 */
@Schema(description = "统一错误码")
enum class ErrorCode(
    @field:Schema(description = "错误码", example = "2000")
    val code: Int,

    @field:Schema(description = "错误描述", example = "业务处理异常")
    val message: String
) {
    // 系统级异常
    SYSTEM_ERROR(1000, "系统异常，请稍后重试"),
    DATABASE_ERROR(1001, "数据库操作异常"),
    NETWORK_ERROR(1002, "网络异常"),
    TIMEOUT_ERROR(1003, "请求超时"),

    // 业务异常
    BUSINESS_ERROR(2000, "业务处理异常"),
    USER_NOT_FOUND(2001, "用户不存在"),
    PERMISSION_DENIED(2002, "权限不足"),
    OPERATION_FORBIDDEN(2003, "操作禁止"),

    // 参数校验异常
    PARAM_INVALID(3000, "请求参数校验失败"),
    PARAM_MISSING(3001, "缺少必要参数"),
    PARAM_TYPE_MISMATCH(3002, "参数类型错误"),

    // 第三方调用异常
    THIRD_PARTY_ERROR(4000, "第三方系统调用失败"),
    THIRD_PARTY_TIMEOUT(4001, "第三方系统请求超时")
}