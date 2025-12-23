package io.github.faustofan.admin.shared.common.dto;


import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import io.github.faustofan.admin.shared.common.exception.ErrorCode;

import java.util.UUID;

/**
 * Standard API response wrapper.
 *
 * @param traceId Unique identifier for the request trace
 * @param code    Response status code
 * @param message Response message
 * @param data    Response data of generic type T
 * @param <T>     Type of the response data
 */
public record ApiResponse<T>(
        UUID traceId,
        String code,
        String message,
        T data
) {
    /**
     * Creates a successful ApiResponse with the given data.
     *
     * @param data the response data
     * @param <T>  the type of the response data
     * @return an ApiResponse representing a successful response
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                AppContextHolder.getContext() == null ? null : AppContextHolder.getContext().traceId(),
                "200",
                "Success",
                data
        );
    }

    /**
     * Creates a failed ApiResponse based on the provided ErrorCode.
     *
     * @param errorCode the error code representing the failure
     * @param <T>       the type of the response data
     * @return an ApiResponse representing a failed response
     */
    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(
                AppContextHolder.getContext() == null ? null : AppContextHolder.getContext().traceId(),
                errorCode.getCode(),
                errorCode.getMessage(),
                null
        );
    }

    /**
     * Creates a failed ApiResponse based on the provided ErrorCode and custom message.
     *
     * @param errorCode the error code representing the failure
     * @param message   the custom error message
     * @param <T>       the type of the response data
     * @return an ApiResponse representing a failed response
     */
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
        return new ApiResponse<>(
                AppContextHolder.getContext().traceId(),
                errorCode.getCode(),
                errorCode.getMessage() + ": " + message,
                null
        );
    }
}
