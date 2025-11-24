package io.github.faustofan.admin.boot.common.api;

/**
 * 排序规则对象。
 * <p>
 * 包含排序属性和排序方向。
 */
public record SortOrder(
        String property,      // 排序字段名
        Direction direction   // 排序方向（升序或降序）
) {
    /**
     * 创建一个升序排序规则。
     *
     * @param property 排序字段名
     * @return 升序的SortOrder实例
     */
    public static SortOrder asc(String property) {
        return new SortOrder(property, Direction.ASC);
    }

    /**
     * 创建一个降序排序规则。
     *
     * @param property 排序字段名
     * @return 降序的SortOrder实例
     */
    public static SortOrder desc(String property) {
        return new SortOrder(property, Direction.DESC);
    }

    /**
     * 排序方向枚举。
     * <ul>
     *   <li>ASC：升序</li>
     *   <li>DESC：降序</li>
     * </ul>
     */
    public enum Direction {
        ASC,
        DESC;
    }
}