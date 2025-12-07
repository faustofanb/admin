package io.github.faustofan.admin.cache

import org.redisson.api.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * 通用缓存服务
 *
 * 提供业务层使用的缓存 API，封装 Redisson 操作
 * 当 Redis 不可用时，提供空实现
 */
@Service
class CacheService(
    @field:Autowired(required = false)
    private val redissonClient: RedissonClient?
) {
    private val log = LoggerFactory.getLogger(CacheService::class.java)

    private val isRedisAvailable: Boolean = redissonClient != null

    init {
        if (!isRedisAvailable) {
            log.warn("⚠️  RedissonClient is not available. Cache operations will be no-op (always miss).")
        }
    }

    companion object {
        // 缓存 Key 前缀
        private const val KEY_PREFIX = "admin:"

        // 默认过期时间
        private val DEFAULT_TTL = Duration.ofHours(1)
    }

    // ==========================================
    // 基础操作
    // ==========================================

    /**
     * 设置缓存
     */
    fun <T> set(key: String, value: T, ttl: Duration = DEFAULT_TTL) {
        if (!isRedisAvailable) return

        val bucket: RBucket<T> = redissonClient!!.getBucket(buildKey(key))
        bucket.set(value, ttl)
        log.debug("Cache SET: key={}, ttl={}s", key, ttl.seconds)
    }

    /**
     * 获取缓存
     */
    fun <T> get(key: String): T? {
        if (!isRedisAvailable) return null

        val bucket: RBucket<T> = redissonClient!!.getBucket(buildKey(key))
        return bucket.get()
    }

    /**
     * 获取缓存，不存在则加载
     */
    fun <T> getOrLoad(key: String, ttl: Duration = DEFAULT_TTL, loader: () -> T?): T? {
        if (!isRedisAvailable) {
            // Redis 不可用时，直接调用 loader
            return loader()
        }

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

    /**
     * 删除缓存
     */
    fun delete(key: String): Boolean {
        if (!isRedisAvailable) return false

        val bucket: RBucket<Any> = redissonClient!!.getBucket(buildKey(key))
        val deleted = bucket.delete()
        log.debug("Cache DELETE: key={}, success={}", key, deleted)
        return deleted
    }

    /**
     * 批量删除缓存（按模式匹配）
     */
    fun deleteByPattern(pattern: String): Long {
        if (!isRedisAvailable) return 0

        val keys = redissonClient!!.keys
        val fullPattern = buildKey(pattern)
        val count = keys.deleteByPattern(fullPattern)
        log.debug("Cache DELETE BY PATTERN: pattern={}, count={}", pattern, count)
        return count
    }

    /**
     * 检查缓存是否存在
     */
    fun exists(key: String): Boolean {
        if (!isRedisAvailable) return false

        val bucket: RBucket<Any> = redissonClient!!.getBucket(buildKey(key))
        return bucket.isExists
    }

    /**
     * 设置过期时间
     */
    fun expire(key: String, ttl: Duration): Boolean {
        if (!isRedisAvailable) return false

        val bucket: RBucket<Any> = redissonClient!!.getBucket(buildKey(key))
        return bucket.expire(ttl)
    }

    /**
     * 获取剩余过期时间（毫秒）
     */
    fun ttl(key: String): Long {
        if (!isRedisAvailable) return -1

        val bucket: RBucket<Any> = redissonClient!!.getBucket(buildKey(key))
        return bucket.remainTimeToLive()
    }

    // ==========================================
    // Hash 操作
    // ==========================================

    /**
     * Hash 设置
     */
    fun <K, V> hSet(key: String, field: K, value: V) {
        if (!isRedisAvailable) return

        val map: RMap<K, V> = redissonClient!!.getMap(buildKey(key))
        map[field] = value
    }

    /**
     * Hash 获取
     */
    fun <K, V> hGet(key: String, field: K): V? {
        if (!isRedisAvailable) return null

        val map: RMap<K, V> = redissonClient!!.getMap(buildKey(key))
        return map[field]
    }

    /**
     * Hash 获取全部
     */
    fun <K, V> hGetAll(key: String): Map<K, V> {
        if (!isRedisAvailable) return emptyMap()

        val map: RMap<K, V> = redissonClient!!.getMap(buildKey(key))
        return map.readAllMap()
    }

    /**
     * Hash 删除字段
     */
    fun <K> hDel(key: String, vararg fields: K): Long {
        if (!isRedisAvailable) return 0

        val map: RMap<K, Any> = redissonClient!!.getMap(buildKey(key))
        return map.fastRemove(*fields)
    }

    // ==========================================
    // List 操作
    // ==========================================

    /**
     * List 右侧添加
     */
    fun <T> rPush(key: String, vararg values: T): Boolean {
        if (!isRedisAvailable) return false

        val list: RList<T> = redissonClient!!.getList(buildKey(key))
        return list.addAll(values.toList())
    }

    /**
     * List 获取范围
     */
    fun <T> lRange(key: String, start: Int, end: Int): List<T> {
        if (!isRedisAvailable) return emptyList()

        val list: RList<T> = redissonClient!!.getList(buildKey(key))
        return list.subList(start, end)
    }

    // ==========================================
    // Set 操作
    // ==========================================

    /**
     * Set 添加
     */
    fun <T> sAdd(key: String, vararg values: T): Boolean {
        if (!isRedisAvailable) return false

        val set: RSet<T> = redissonClient!!.getSet(buildKey(key))
        return set.addAll(values.toList())
    }

    /**
     * Set 获取全部成员
     */
    fun <T> sMembers(key: String): Set<T> {
        if (!isRedisAvailable) return emptySet()

        val set: RSet<T> = redissonClient!!.getSet(buildKey(key))
        return set.readAll()
    }

    /**
     * Set 是否包含
     */
    fun <T> sIsMember(key: String, value: T): Boolean {
        if (!isRedisAvailable) return false

        val set: RSet<T> = redissonClient!!.getSet(buildKey(key))
        return set.contains(value)
    }

    // ==========================================
    // 分布式锁
    // ==========================================

    /**
     * 尝试获取锁
     */
    fun tryLock(lockKey: String, waitTime: Duration, leaseTime: Duration): Boolean {
        if (!isRedisAvailable) {
            log.warn("Redis not available, cannot acquire lock: {}", lockKey)
            return false
        }

        val lock = redissonClient!!.getLock(buildKey("lock:$lockKey"))
        return lock.tryLock(waitTime.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 释放锁
     */
    fun unlock(lockKey: String) {
        if (!isRedisAvailable) return

        val lock = redissonClient!!.getLock(buildKey("lock:$lockKey"))
        if (lock.isHeldByCurrentThread) {
            lock.unlock()
        }
    }

    /**
     * 使用锁执行操作
     */
    fun <T> withLock(
        lockKey: String,
        waitTime: Duration = Duration.ofSeconds(10),
        leaseTime: Duration = Duration.ofSeconds(30),
        action: () -> T
    ): T? {
        if (!isRedisAvailable) {
            log.warn("Redis not available, executing action without lock: {}", lockKey)
            return action()
        }

        val acquired = tryLock(lockKey, waitTime, leaseTime)
        if (!acquired) {
            log.warn("Failed to acquire lock: {}", lockKey)
            return null
        }
        return try {
            action()
        } finally {
            unlock(lockKey)
        }
    }

    // ==========================================
    // 布隆过滤器操作
    // ==========================================

    /**
     * 初始化布隆过滤器
     * @param name 过滤器名称
     * @param expectedInsertions 预计插入数量
     * @param falseProbability 误判率 (0.01 = 1%)
     */
    fun bloomFilterInit(name: String, expectedInsertions: Long, falseProbability: Double = 0.01): Boolean {
        if (!isRedisAvailable) return false

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.tryInit(expectedInsertions, falseProbability)
    }

    /**
     * 向布隆过滤器添加元素
     */
    fun bloomFilterAdd(name: String, value: String): Boolean {
        if (!isRedisAvailable) return false

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.add(value)
    }

    /**
     * 批量向布隆过滤器添加元素
     */
    fun bloomFilterAddAll(name: String, values: Collection<String>): Long {
        if (!isRedisAvailable) return 0

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        var count = 0L
        values.forEach {
            if (bloomFilter.add(it)) count++
        }
        return count
    }

    /**
     * 检查布隆过滤器中是否可能存在
     * 返回 false 表示一定不存在，返回 true 表示可能存在
     */
    fun bloomFilterContains(name: String, value: String): Boolean {
        if (!isRedisAvailable) return false

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.contains(value)
    }

    /**
     * 获取布隆过滤器已添加元素数量
     */
    fun bloomFilterCount(name: String): Long {
        if (!isRedisAvailable) return 0

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.count()
    }

    /**
     * 删除布隆过滤器
     */
    fun bloomFilterDelete(name: String): Boolean {
        if (!isRedisAvailable) return false

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.delete()
    }

    // ==========================================
    // 工具方法
    // ==========================================

    private fun buildKey(key: String): String {
        return "$KEY_PREFIX$key"
    }
}

