package io.github.faustofan.admin.shared.infrastructure.exception

/**
 * 业务异常基类
 * @param code 错误码
 * @param message 错误信息
 */
open class BizException(
    val code: Int = ErrorCode.BUSINESS_ERROR.code,
    override val message: String = ErrorCode.BUSINESS_ERROR.message
) : RuntimeException(message)

/**
 * 系统异常基类
 * @param code 错误码
 * @param message 错误信息
 */
open class SysException(
    val code: Int = ErrorCode.SYSTEM_ERROR.code,
    override val message: String = ErrorCode.SYSTEM_ERROR.message
) : RuntimeException(message)

/**
 * 参数校验异常
 */
class ParamException(
    val code: Int = ErrorCode.PARAM_INVALID.code,
    override val message: String = ErrorCode.PARAM_INVALID.message
) : RuntimeException(message)

/**
 * 第三方依赖异常
 */
class ThirdPartyException(
    val code: Int = ErrorCode.THIRD_PARTY_ERROR.code,
    override val message: String = ErrorCode.THIRD_PARTY_ERROR.message
) : RuntimeException(message)

