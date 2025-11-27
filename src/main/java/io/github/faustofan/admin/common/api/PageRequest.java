package io.github.faustofan.admin.common.api;

import java.util.List;

/**
 * 分页请求参数对象。
 * <p>
 * 包含页码、每页大小和排序规则列表。
 */
public record PageRequest(
        int page,           // 当前页码，从1开始
        int size,           // 每页数据条数
        List<SortOrder> sort // 排序规则列表
) {
    /**
     * 创建一个不带排序规则的分页请求。
     *
     * @param page 页码，必须大于0
     * @param size 每页大小，必须大于0
     * @return PageRequest实例
     * @throws IllegalArgumentException 如果page或size小于等于0
     */
    public static PageRequest of(int page, int size) {
        if (page <= 0 || size <= 0)
            throw new IllegalArgumentException("Page and Size must be positive");

        return new PageRequest(page, size, List.of());
    }

    /**
     * 计算当前页的偏移量（用于数据库查询）。
     *
     * @return 偏移量
     */
    public int offset() {
        return (page - 1) * size;
    }
}
