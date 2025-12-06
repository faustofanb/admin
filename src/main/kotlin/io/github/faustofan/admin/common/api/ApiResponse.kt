package io.github.faustofan.admin.common.api

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

/**
 * 统一响应对象，用于封装通用的 API 响应结构。
 *
 * @param T 泛型参数，表示返回的数据类型。
 * @property code 业务状态码，例如：200 表示成功。
 * @property message 提示信息。
 * @property traceId 请求追踪ID，用于链路追踪。
 * @property data 返回的数据内容。
 */
@Schema(description = "统一响应对象")
data class ApiResponse<T : Any>(
    @field:Schema(description = "业务状态码，例如：200 表示成功", example = "200")
    val code: Int = 200,

    @field:Schema(description = "提示信息", example = "Success")
    val message: String = "Success",

    @field:Schema(description = "请求追踪ID，用于链路追踪")
    val traceId: String? = null,

    @field:Schema(description = "返回的数据内容")
    val data: T? = null
) {
    companion object {
        /**
         * 创建一个成功的响应对象。
         *
         * @param T 泛型参数，表示返回的数据类型。
         * @param data 返回的数据内容，默认为 null。
         * @return ApiResponse 成功的响应对象。
         */
        fun <T : Any> success(data: T? = null): ApiResponse<T> =
            //TODO：返回空时， 无法确认泛型类型导致编译报错
            ApiResponse(code = 200, message = "Success", data = data)

        /**
         * 创建一个失败的响应对象。
         *
         * @param T 泛型参数，表示返回的数据类型。
         * @param code 业务状态码，默认为 500。
         * @param message 错误提示信息。
         * @return ApiResponse 失败的响应对象。
         */
        fun <T : Any> failure(code: Int = 500, message: String): ApiResponse<T> =
            ApiResponse(code = code, message = message)
    }
}

/**
 * 分页响应对象，用于封装分页数据的 API 响应结构。
 *
 * @param T 泛型参数，表示分页数据的类型。
 * @property code 业务状态码，例如：200 表示成功。
 * @property message 提示信息。
 * @property traceId 请求追踪ID，用于链路追踪。
 * @property data 分页数据。
 */
@Schema(description = "分页响应对象，data 字段复用分页数据")
data class PageResponse<T : Any>(
    @field:Schema(description = "业务状态码，例如：200 表示成功", example = "200")
    val code: Int = 200,

    @field:Schema(description = "提示信息", example = "Success")
    val message: String = "Success",

    @field:Schema(description = "请求追踪ID，用于链路追踪")
    val traceId: String? = null,

    @field:Schema(description = "分页数据")
    val data: PageData<T>
) {
    companion object {
        /**
         * 从分页对象创建一个分页响应对象。
         *
         * @param T 泛型参数，表示分页数据的类型。
         * @param page Spring Data 分页对象。
         * @param code 业务状态码，默认为 200。
         * @param message 提示信息，默认为 "Success"。
         * @param traceId 请求追踪ID，默认为 null。
         * @return PageResponse 分页响应对象。
         */
        fun <T : Any> from(page: Page<T>, code: Int = 200, message: String = "Success", traceId: String? = null): PageResponse<T> =
            PageResponse(
                code = code,
                message = message,
                traceId = traceId,
                data = PageData.from(page)
            )
    }
}

/**
 * 分页数据对象，用于封装分页的具体数据内容。
 *
 * @param T 泛型参数，表示分页数据的类型。
 * @property items 当前页数据列表。
 * @property page 当前页码（从 1 开始）。
 * @property size 每页大小。
 * @property total 总条数。
 * @property totalPages 总页数。
 */
@Schema(description = "分页数据")
data class PageData<T : Any>(
    @field:Schema(description = "当前页数据列表")
    val items: List<T>,

    @field:Schema(description = "当前页码（从 1 开始）", example = "1")
    val page: Int,

    @field:Schema(description = "每页大小", example = "10")
    val size: Int,

    @field:Schema(description = "总条数", example = "100")
    val total: Long,

    @field:Schema(description = "总页数", example = "10")
    val totalPages: Int
) {
    companion object {
        /**
         * 从分页对象创建分页数据对象。
         *
         * @param T 泛型参数，表示分页数据的类型。
         * @param page Spring Data 分页对象。
         * @return PageData 分页数据对象。
         */
        fun <T : Any> from(page: Page<T>): PageData<T> = PageData(
            items = page.content,
            page = page.number + 1,
            size = page.size,
            total = page.totalElements,
            totalPages = page.totalPages
        )
    }
}