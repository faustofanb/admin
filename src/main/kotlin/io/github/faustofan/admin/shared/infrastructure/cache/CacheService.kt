package io.github.faustofan.admin.shared.infrastructure.cache

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.time.Duration

/**
 * 通用缓存服务（外观模式）
 *
 * 委托给具体的缓存实现（LocalCacheService 或 RedisCacheService）
 * 优先使用 Redis 分布式缓存，不可用时降级为本地缓存
 */
@Service
@Primary
@Lazy
class CacheService(
    @field:Autowired(required = false)
    @field:Qualifier("redisCacheService")
    @field:Lazy
    private val redisCacheService: RedisCacheService?,
    @field:Autowired(required = true)
    @field:Qualifier("localCacheService")
    private val localCacheService: LocalCacheService
) : CacheOperations {
    private val log = LoggerFactory.getLogger(CacheService::class.java)

    companion object {
        // 默认过期时间
        private val DEFAULT_TTL = Duration.ofHours(1)
    }

    // 选择实际的缓存实现
    private val delegate: CacheOperations by lazy {
        (redisCacheService ?: localCacheService).also {
            val cacheType = when (it) {
                is RedisCacheService -> "RedisCacheService (distributed)"
                is LocalCacheService -> "LocalCacheService (local)"
                else -> "Unknown"
            }
            log.info("========================================")
            log.info("CacheService 初始化完成")
            log.info("使用实现: {}", cacheType)
            log.info("========================================")
        }
    }

    // ==========================================
    // 基础操作（委托给具体实现）
    // ==========================================

    override fun <T> set(key: String, value: T, ttl: Duration) {
        delegate.set(key, value, ttl)
    }

    override fun <T> get(key: String): T? {
        return delegate.get(key)
    }

    override fun <T> getOrLoad(key: String, ttl: Duration, loader: () -> T?): T? {
        return delegate.getOrLoad(key, ttl, loader)
    }

    override fun delete(key: String): Boolean {
        return delegate.delete(key)
    }

    override fun deleteByPattern(pattern: String): Long {
        return delegate.deleteByPattern(pattern)
    }

    override fun exists(key: String): Boolean {
        return delegate.exists(key)
    }

    override fun expire(key: String, ttl: Duration): Boolean {
        return delegate.expire(key, ttl)
    }

    override fun ttl(key: String): Long {
        return delegate.ttl(key)
    }

    // ==========================================
    // Hash 操作
    // ==========================================

    override fun <K, V> hSet(key: String, field: K, value: V) {
        delegate.hSet(key, field, value)
    }

    override fun <K, V> hGet(key: String, field: K): V? {
        return delegate.hGet(key, field)
    }

    override fun <K, V> hGetAll(key: String): Map<K, V> {
        return delegate.hGetAll(key)
    }

    override fun <K> hDel(key: String, vararg fields: K): Long {
        return delegate.hDel(key, *fields)
    }

    // ==========================================
    // List 操作
    // ==========================================

    override fun <T> rPush(key: String, vararg values: T): Boolean {
        return delegate.rPush(key, *values)
    }

    override fun <T> lRange(key: String, start: Int, end: Int): List<T> {
        return delegate.lRange(key, start, end)
    }

    // ==========================================
    // Set 操作
    // ==========================================

    override fun <T> sAdd(key: String, vararg values: T): Boolean {
        return delegate.sAdd(key, *values)
    }

    override fun <T> sMembers(key: String): Set<T> {
        return delegate.sMembers(key)
    }

    override fun <T> sIsMember(key: String, value: T): Boolean {
        return delegate.sIsMember(key, value)
    }

    // ==========================================
    // 分布式锁
    // ==========================================

    override fun tryLock(lockKey: String, waitTime: Duration, leaseTime: Duration): Boolean {
        return delegate.tryLock(lockKey, waitTime, leaseTime)
    }

    override fun unlock(lockKey: String) {
        delegate.unlock(lockKey)
    }

    override fun <T> withLock(
        lockKey: String,
        waitTime: Duration,
        leaseTime: Duration,
        action: () -> T
    ): T? {
        return delegate.withLock(lockKey, waitTime, leaseTime, action)
    }

    // ==========================================
    // 布隆过滤器操作
    // ==========================================

    override fun bloomFilterInit(name: String, expectedInsertions: Long, falseProbability: Double): Boolean {
        return delegate.bloomFilterInit(name, expectedInsertions, falseProbability)
    }

    override fun bloomFilterAdd(name: String, value: String): Boolean {
        return delegate.bloomFilterAdd(name, value)
    }

    override fun bloomFilterAddAll(name: String, values: Collection<String>): Long {
        return delegate.bloomFilterAddAll(name, values)
    }

    override fun bloomFilterContains(name: String, value: String): Boolean {
        return delegate.bloomFilterContains(name, value)
    }

    override fun bloomFilterCount(name: String): Long {
        return delegate.bloomFilterCount(name)
    }

    override fun bloomFilterDelete(name: String): Boolean {
        return delegate.bloomFilterDelete(name)
    }

    // ==========================================
    // 兼容旧 API（保持默认参数）
    // ==========================================

    /**
     * 设置缓存（带默认 TTL）
     */
    fun <T> set(key: String, value: T) {
        set(key, value, DEFAULT_TTL)
    }

    /**
     * 获取缓存，不存在则加载（带默认 TTL）
     */
    fun <T> getOrLoad(key: String, loader: () -> T?): T? {
        return getOrLoad(key, DEFAULT_TTL, loader)
    }

    /**
     * 使用锁执行操作（带默认超时）
     */
    fun <T> withLock(lockKey: String, action: () -> T): T? {
        return withLock(lockKey, Duration.ofSeconds(10), Duration.ofSeconds(30), action)
    }

    /**
     * 初始化布隆过滤器（带默认误判率）
     */
    fun bloomFilterInit(name: String, expectedInsertions: Long): Boolean {
        return bloomFilterInit(name, expectedInsertions, 0.01)
    }
}

