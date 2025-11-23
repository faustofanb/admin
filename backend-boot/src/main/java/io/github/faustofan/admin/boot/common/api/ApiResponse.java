package io.github.faustofan.admin.boot.common.api;

import org.springframework.http.HttpStatus;

/**
 * 标准 API 响应包装类
 *
 * @param <T> 响应数据的类型
 */
public record ApiResponse<T>(
        /*响应码，通常与 HTTP 状态码对应*/
        String code,
        /*响应消息，描述本次请求的结果*/
        String message,
        /*链路追踪 ID，用于定位请求链路*/
        String traceId,
        /*响应数据主体*/
        T data
) {
    /**
     * 创建一个成功的 ApiResponse，包含指定数据
     *
     * @param data 响应数据
     * @param <T>  响应数据类型
     * @return 成功的 ApiResponse
     */
    public static <T> ApiResponse<T> success(final T data) {
        return new ApiResponse<T>(
                String.valueOf(HttpStatus.OK.value()),
                "Success",
                "", //TODO: 添加 traceId
                data
        );
    }

    /**
     * 创建一个无数据的成功 ApiResponse
     *
     * @param <T> 响应数据类型
     * @return 无数据的成功 ApiResponse
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 创建一个失败的 ApiResponse，包含错误码和错误信息
     *
     * @param code    错误码
     * @param message 错误信息
     * @param <T>     响应数据类型
     * @return 失败的 ApiResponse
     */
    public static <T> ApiResponse<T> failure(final String code, final String message) {
        return new ApiResponse<>(
                code,
                message,
                "",
                null
        );
    }

    /**
     * 返回带有指定 traceId 的 ApiResponse
     *
     * @param traceId 链路追踪 ID
     * @return 新的 ApiResponse，包含 traceId
     */
    public ApiResponse<T> withTraceId(final String traceId) {
        return new ApiResponse<>(this.code, this.message, traceId, this.data);
    }
}
