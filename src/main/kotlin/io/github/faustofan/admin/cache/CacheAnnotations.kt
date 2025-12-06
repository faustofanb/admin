package io.github.faustofan.admin.cache

/**
 * 缓存配置注解
 *
 * 用于标记需要缓存的方法，配合 AOP 实现声明式缓存
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Cached(
    /**
     * 缓存 Key，支持 SpEL 表达式
     * 例如: "user:#{#userId}" 或 "menu:tree:#{#tenantId}"
     */
    val key: String,

    /**
     * 过期时间（秒），默认 1 小时
     */
    val ttlSeconds: Long = 3600,

    /**
     * 缓存条件，SpEL 表达式，返回 true 才缓存
     */
    val condition: String = "",

    /**
     * 是否缓存 null 值，防止缓存穿透
     */
    val cacheNull: Boolean = false
)

/**
 * 缓存失效注解
 *
 * 用于标记会使缓存失效的方法
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheEvict(
    /**
     * 要失效的缓存 Key，支持 SpEL 表达式
     */
    val key: String = "",

    /**
     * 要失效的缓存 Key 模式（支持通配符）
     */
    val pattern: String = "",

    /**
     * 是否在方法执行前失效缓存
     */
    val beforeInvocation: Boolean = false
)

/**
 * 批量缓存失效注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheEvicts(
    vararg val value: CacheEvict
)

