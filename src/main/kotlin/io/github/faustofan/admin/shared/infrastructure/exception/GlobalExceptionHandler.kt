package io.github.faustofan.admin.shared.infrastructure.exception

import io.github.faustofan.admin.shared.application.dto.ApiResponse
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 全局统一异常处理器
 * 拦截系统、业务、参数、第三方异常，保证返回统一响应格式
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BizException::class)
    fun handleBizException(e: BizException): ApiResponse<Nothing> {
        e.printStackTrace()
        return ApiResponse(
            code = e.code,
            message = e.message
        )
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SysException::class)
    fun handleSysException(e: SysException): ApiResponse<Nothing> {
        e.printStackTrace()
        return ApiResponse(
            code = e.code,
            message = e.message
        )
    }

    /**
     * 处理参数校验异常（Spring @Valid / @Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException::class, BindException::class)
    fun handleValidationException(e: Exception): ApiResponse<Nothing> {
        e.printStackTrace()
        val msg = when (e) {
            is MethodArgumentNotValidException -> e.bindingResult.allErrors.joinToString("; ") { it.defaultMessage ?: "" }
            is BindException -> e.bindingResult.allErrors.joinToString("; ") { it.defaultMessage ?: "" }
            else -> e.message ?: "参数校验失败"
        }
        return ApiResponse(
            code = ErrorCode.PARAM_INVALID.code,
            message = msg
        )
    }

    /**
     * 处理第三方调用异常
     */
    @ExceptionHandler(ThirdPartyException::class)
    fun handleThirdPartyException(e: ThirdPartyException): ApiResponse<Nothing> {
        e.printStackTrace()
        return ApiResponse(
            code = e.code,
            message = e.message
        )
    }

    /**
     * 处理所有未捕获异常
     */
    @ExceptionHandler(Exception::class)
    fun handleOtherException(e: Exception): ApiResponse<Nothing> {
        e.printStackTrace() // 可替换为日志框架
        return ApiResponse(
            code = ErrorCode.SYSTEM_ERROR.code,
            message = e.message ?: ErrorCode.SYSTEM_ERROR.message
        )
    }
}