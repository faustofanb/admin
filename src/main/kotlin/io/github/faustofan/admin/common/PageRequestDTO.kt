package io.github.faustofan.admin.common

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * 分页请求参数的数据传输对象（DTO）。
 *
 * @property page 页码（从 1 开始），默认值为 1。
 * @property size 每页大小，默认值为 10。
 * @property sort 排序规则，格式为 field,asc 或多个使用 ; 分隔，例如：id,desc;name,asc，可为空。
 */
@Schema(description = "分页请求参数")
data class PageRequestDTO(

    @field:Schema(description = "页码（从 1 开始）", example = "1")
    val page: Int = 1,

    @field:Schema(description = "每页大小", example = "10")
    val size: Int = 10,

    @field:Schema(
        description = "排序规则，格式：field,asc 或多个使用 ; 分隔，如：id,desc;name,asc",
        example = "id,desc"
    )
    val sort: String? = null
) {

    /**
     * 将当前分页请求参数转换为 Spring Data 的 Pageable 对象。
     *
     * @return Pageable 分页请求对象。
     */
    fun toPageable(): Pageable {
        val zeroPage = if (page > 0) page - 1 else 0
        val sortObj = parseSort(sort)
        return PageRequest.of(zeroPage, size, sortObj)
    }

    /**
     * 解析排序规则字符串并转换为 Spring Data 的 Sort 对象。
     *
     * @param sort 排序规则字符串，格式为 field,asc 或多个使用 ; 分隔。
     * @return Sort 排序对象，如果未指定排序规则则返回未排序对象。
     */
    private fun parseSort(sort: String?): Sort {
        if (sort.isNullOrBlank()) return Sort.unsorted()

        val orders = sort.split(';').mapNotNull { rule ->
            val parts = rule.split(',')
            if (parts.size != 2) return@mapNotNull null

            val property = parts[0].trim()
            val direction = parts[1].trim().lowercase()

            val sortDir = when (direction) {
                "asc" -> Sort.Direction.ASC
                "desc" -> Sort.Direction.DESC
                else -> return@mapNotNull null
            }
            Sort.Order(sortDir, property)
        }

        return if (orders.isEmpty()) Sort.unsorted() else Sort.by(orders)
    }
}