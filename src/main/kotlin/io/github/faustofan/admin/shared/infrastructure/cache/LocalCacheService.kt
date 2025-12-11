package io.github.faustofan.admin.shared.infrastructure.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

/**
 * Caffeine 本地缓存实现
 *
 * 提供本地内存缓存能力，适合单机部署场景或作为 Redis 的降级方案
 * 不支持分布式锁和布隆过滤器等分布式特性
 *
 * 注：始终创建此 Bean 作为默认实现，当 Redis 可用时，CacheService 会优先使用 Redis
 */
@Service("localCacheService")
class LocalCacheService : CacheOperations {
    private val log = LoggerFactory.getLogger(LocalCacheService::class.java)

    companion object {
        // 缓存 Key 前缀
        private const val KEY_PREFIX = "admin:"

        // 默认过期时间
        private val DEFAULT_TTL = Duration.ofHours(1)

        // 默认最大缓存条目数
        private const val DEFAULT_MAX_SIZE = 10000L
    }

    // 主缓存容器
    private val cache: Cache<String, CacheEntry> = Caffeine.newBuilder()
        .maximumSize(DEFAULT_MAX_SIZE)
        .expireAfterWrite(DEFAULT_TTL)
        .build()

    // Hash、List、Set 的独立存储
    private val hashStore = ConcurrentHashMap<String, MutableMap<Any, Any>>()
    private val listStore = ConcurrentHashMap<String, MutableList<Any>>()
    private val setStore = ConcurrentHashMap<String, MutableSet<Any>>()

    init {
        log.info("✅ LocalCacheService (Caffeine) initialized with max size: {}", DEFAULT_MAX_SIZE)
    }

    // ==========================================
    // 基础操作
    // ==========================================

    override fun <T> set(key: String, value: T, ttl: Duration) {
        val fullKey = buildKey(key)
        val expiryTime = System.currentTimeMillis() + ttl.toMillis()
        cache.put(fullKey, CacheEntry(value, expiryTime))
        log.debug("Cache SET: key={}, ttl={}s", key, ttl.seconds)
    }

    override fun <T> get(key: String): T? {
        val fullKey = buildKey(key)
        val entry = cache.getIfPresent(fullKey) ?: return null

        // 检查是否过期
        if (System.currentTimeMillis() > entry.expiryTime) {
            cache.invalidate(fullKey)
            return null
        }

        @Suppress("UNCHECKED_CAST")
        return entry.value as? T
    }

    override fun <T> getOrLoad(key: String, ttl: Duration, loader: () -> T?): T? {
        val cached = get<T>(key)
        if (cached != null) {
            log.debug("Cache HIT: key={}", key)
            return cached
        }

        log.debug("Cache MISS: key={}", key)
        val value = loader()
        if (value != null) {
            set(key, value, ttl)
        }
        return value
    }

    override fun delete(key: String): Boolean {
        val fullKey = buildKey(key)
        cache.invalidate(fullKey)
        log.debug("Cache DELETE: key={}", key)
        return true
    }

    override fun deleteByPattern(pattern: String): Long {
        val fullPattern = buildKey(pattern)
        val regex = fullPattern.replace("*", ".*").toRegex()

        val keysToDelete = cache.asMap().keys.filter { it.matches(regex) }
        keysToDelete.forEach { cache.invalidate(it) }

        val count = keysToDelete.size.toLong()
        log.debug("Cache DELETE BY PATTERN: pattern={}, count={}", pattern, count)
        return count
    }

    override fun exists(key: String): Boolean {
        val fullKey = buildKey(key)
        val entry = cache.getIfPresent(fullKey) ?: return false
        return System.currentTimeMillis() <= entry.expiryTime
    }

    override fun expire(key: String, ttl: Duration): Boolean {
        val fullKey = buildKey(key)
        val entry = cache.getIfPresent(fullKey) ?: return false

        // 更新过期时间
        val newExpiryTime = System.currentTimeMillis() + ttl.toMillis()
        cache.put(fullKey, CacheEntry(entry.value, newExpiryTime))
        return true
    }

    override fun ttl(key: String): Long {
        val fullKey = buildKey(key)
        val entry = cache.getIfPresent(fullKey) ?: return -2L

        val remaining = entry.expiryTime - System.currentTimeMillis()
        return if (remaining > 0) remaining else -1L
    }

    // ==========================================
    // Hash 操作
    // ==========================================

    override fun <K, V> hSet(key: String, field: K, value: V) {
        val fullKey = buildKey(key)
        val map = hashStore.computeIfAbsent(fullKey) { ConcurrentHashMap<Any, Any>() }
        @Suppress("UNCHECKED_CAST")
        (map as MutableMap<K, V>)[field] = value
    }

    override fun <K, V> hGet(key: String, field: K): V? {
        val fullKey = buildKey(key)
        val map = hashStore[fullKey] ?: return null
        @Suppress("UNCHECKED_CAST")
        return (map as Map<K, V>)[field]
    }

    override fun <K, V> hGetAll(key: String): Map<K, V> {
        val fullKey = buildKey(key)
        val map = hashStore[fullKey] ?: return emptyMap()
        @Suppress("UNCHECKED_CAST")
        return map.toMap() as Map<K, V>
    }

    override fun <K> hDel(key: String, vararg fields: K): Long {
        val fullKey = buildKey(key)
        val map = hashStore[fullKey] ?: return 0L

        @Suppress("UNCHECKED_CAST")
        val typedMap = map as MutableMap<K, Any>
        var count = 0L
        fields.forEach {
            if (typedMap.remove(it) != null) count++
        }
        return count
    }

    // ==========================================
    // List 操作
    // ==========================================

    override fun <T> rPush(key: String, vararg values: T): Boolean {
        val fullKey = buildKey(key)
        val list = listStore.computeIfAbsent(fullKey) { mutableListOf<Any>() }
        @Suppress("UNCHECKED_CAST")
        return (list as MutableList<T>).addAll(values)
    }

    override fun <T> lRange(key: String, start: Int, end: Int): List<T> {
        val fullKey = buildKey(key)
        val list = listStore[fullKey] ?: return emptyList()

        val actualEnd = if (end >= list.size) list.size else end + 1
        @Suppress("UNCHECKED_CAST")
        return list.subList(start, actualEnd) as List<T>
    }

    // ==========================================
    // Set 操作
    // ==========================================

    override fun <T> sAdd(key: String, vararg values: T): Boolean {
        val fullKey = buildKey(key)
        val set = setStore.computeIfAbsent(fullKey) { mutableSetOf<Any>() }
        @Suppress("UNCHECKED_CAST")
        return (set as MutableSet<T>).addAll(values)
    }

    override fun <T> sMembers(key: String): Set<T> {
        val fullKey = buildKey(key)
        val set = setStore[fullKey] ?: return emptySet()
        @Suppress("UNCHECKED_CAST")
        return set.toSet() as Set<T>
    }

    override fun <T> sIsMember(key: String, value: T): Boolean {
        val fullKey = buildKey(key)
        val set = setStore[fullKey] ?: return false
        @Suppress("UNCHECKED_CAST")
        return (set as Set<T>).contains(value)
    }

    // ==========================================
    // 分布式锁（本地缓存不支持）
    // ==========================================

    override fun tryLock(lockKey: String, waitTime: Duration, leaseTime: Duration): Boolean {
        log.warn("Local cache does not support distributed locks: {}", lockKey)
        return false
    }

    override fun unlock(lockKey: String) {
        log.warn("Local cache does not support distributed locks: {}", lockKey)
    }

    override fun <T> withLock(
        lockKey: String,
        waitTime: Duration,
        leaseTime: Duration,
        action: () -> T
    ): T? {
        log.warn("Local cache does not support distributed locks, executing action without lock: {}", lockKey)
        return action()
    }

    // ==========================================
    // 布隆过滤器操作（本地缓存不支持）
    // ==========================================

    override fun bloomFilterInit(name: String, expectedInsertions: Long, falseProbability: Double): Boolean {
        log.warn("Local cache does not support bloom filters: {}", name)
        return false
    }

    override fun bloomFilterAdd(name: String, value: String): Boolean {
        log.warn("Local cache does not support bloom filters: {}", name)
        return false
    }

    override fun bloomFilterAddAll(name: String, values: Collection<String>): Long {
        log.warn("Local cache does not support bloom filters: {}", name)
        return 0
    }

    override fun bloomFilterContains(name: String, value: String): Boolean {
        log.warn("Local cache does not support bloom filters: {}", name)
        return false
    }

    override fun bloomFilterCount(name: String): Long {
        log.warn("Local cache does not support bloom filters: {}", name)
        return 0
    }

    override fun bloomFilterDelete(name: String): Boolean {
        log.warn("Local cache does not support bloom filters: {}", name)
        return false
    }

    // ==========================================
    // 工具方法
    // ==========================================

    private fun buildKey(key: String): String {
        return "$KEY_PREFIX$key"
    }

    /**
     * 缓存条目，包含值和过期时间
     */
    private data class CacheEntry(
        val value: Any?,
        val expiryTime: Long
    )
}