package io.github.faustofan.admin.boot.common.api;

import java.util.List;

/**
 * 分页响应对象。
 * <p>
 * 封装分页查询的结果，包括数据记录、总条数、当前页码和每页大小。
 *
 * @param <T> 数据记录类型
 */
public record PageResponse<T>(
        List<T> records, // 当前页的数据记录列表
        long total,      // 总记录数
        int page,        // 当前页码
        int size         // 每页数据条数
) {
    /**
     * 创建分页响应对象。
     *
     * @param records 当前页的数据记录列表
     * @param total   总记录数
     * @param page    当前页码
     * @param size    每页数据条数
     * @param <T>     数据记录类型
     * @return PageResponse实例
     */
    public static <T> PageResponse<T> of(
            final List<T> records, final long total,
            final int page, final int size
    ) {
        return new PageResponse<T>(records, total, page, size);
    }

    /**
     * 计算总页数。
     *
     * @return 总页数
     */
    public int totalPages() {
        return (int) Math.ceil((double) total / size);
    }

}