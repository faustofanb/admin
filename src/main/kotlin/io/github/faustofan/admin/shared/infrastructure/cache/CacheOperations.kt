package io.github.faustofan.admin.shared.infrastructure.cache

import java.time.Duration

/**
 * 缓存操作接口
 *
 * 定义所有缓存相关操作，支持本地缓存（Caffeine）和分布式缓存（Redisson）两种实现
 */
interface CacheOperations {

    // ==========================================
    // 基础操作
    // ==========================================

    /**
     * 设置缓存
     */
    fun <T> set(key: String, value: T, ttl: Duration)

    /**
     * 获取缓存
     */
    fun <T> get(key: String): T?

    /**
     * 获取缓存，不存在则加载
     */
    fun <T> getOrLoad(key: String, ttl: Duration, loader: () -> T?): T?

    /**
     * 删除缓存
     */
    fun delete(key: String): Boolean

    /**
     * 批量删除缓存（按模式匹配）
     */
    fun deleteByPattern(pattern: String): Long

    /**
     * 检查缓存是否存在
     */
    fun exists(key: String): Boolean

    /**
     * 设置过期时间
     */
    fun expire(key: String, ttl: Duration): Boolean

    /**
     * 获取剩余过期时间（毫秒）
     */
    fun ttl(key: String): Long

    // ==========================================
    // Hash 操作
    // ==========================================

    /**
     * Hash 设置
     */
    fun <K, V> hSet(key: String, field: K, value: V)

    /**
     * Hash 获取
     */
    fun <K, V> hGet(key: String, field: K): V?

    /**
     * Hash 获取全部
     */
    fun <K, V> hGetAll(key: String): Map<K, V>

    /**
     * Hash 删除字段
     */
    fun <K> hDel(key: String, vararg fields: K): Long

    // ==========================================
    // List 操作
    // ==========================================

    /**
     * List 右侧添加
     */
    fun <T> rPush(key: String, vararg values: T): Boolean

    /**
     * List 获取范围
     */
    fun <T> lRange(key: String, start: Int, end: Int): List<T>

    // ==========================================
    // Set 操作
    // ==========================================

    /**
     * Set 添加
     */
    fun <T> sAdd(key: String, vararg values: T): Boolean

    /**
     * Set 获取全部成员
     */
    fun <T> sMembers(key: String): Set<T>

    /**
     * Set 是否包含
     */
    fun <T> sIsMember(key: String, value: T): Boolean

    // ==========================================
    // 分布式锁（本地缓存不支持）
    // ==========================================

    /**
     * 尝试获取锁
     */
    fun tryLock(lockKey: String, waitTime: Duration, leaseTime: Duration): Boolean

    /**
     * 释放锁
     */
    fun unlock(lockKey: String)

    /**
     * 使用锁执行操作
     */
    fun <T> withLock(
        lockKey: String,
        waitTime: Duration,
        leaseTime: Duration,
        action: () -> T
    ): T?

    // ==========================================
    // 布隆过滤器操作（本地缓存不支持）
    // ==========================================

    /**
     * 初始化布隆过滤器
     */
    fun bloomFilterInit(name: String, expectedInsertions: Long, falseProbability: Double): Boolean

    /**
     * 向布隆过滤器添加元素
     */
    fun bloomFilterAdd(name: String, value: String): Boolean

    /**
     * 批量向布隆过滤器添加元素
     */
    fun bloomFilterAddAll(name: String, values: Collection<String>): Long

    /**
     * 检查布隆过滤器中是否可能存在
     */
    fun bloomFilterContains(name: String, value: String): Boolean

    /**
     * 获取布隆过滤器已添加元素数量
     */
    fun bloomFilterCount(name: String): Long

    /**
     * 删除布隆过滤器
     */
    fun bloomFilterDelete(name: String): Boolean
}