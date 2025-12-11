package io.github.faustofan.admin.shared.infrastructure.cache

import org.redisson.api.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Redisson 分布式缓存实现
 *
 * 提供 Redis 分布式缓存能力，支持集群部署场景
 * 支持分布式锁、布隆过滤器等分布式特性
 */
@Service("redisCacheService")
@ConditionalOnProperty(prefix = "app.cache.redis", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class RedisCacheService(
    @field:Autowired(required = false)
    private val redissonClient: RedissonClient?
) : CacheOperations {
    private val log = LoggerFactory.getLogger(RedisCacheService::class.java)

    private val isRedisAvailable: Boolean = redissonClient != null

    companion object {
        // 缓存 Key 前缀
        private const val KEY_PREFIX = "admin:"

        // 默认过期时间
        private val DEFAULT_TTL = Duration.ofHours(1)
    }

    init {
        if (!isRedisAvailable) {
            log.warn("⚠️  RedissonClient is not available. Cache operations will be no-op (always miss).")
        } else {
            log.info("✅ RedisCacheService (Redisson) initialized")
        }
    }

    // ==========================================
    // 基础操作
    // ==========================================

    override fun <T> set(key: String, value: T, ttl: Duration) {
        if (!isRedisAvailable) return

        val bucket: RBucket<T> = redissonClient!!.getBucket(buildKey(key))
        bucket.set(value, ttl)
        log.debug("Cache SET: key={}, ttl={}s", key, ttl.seconds)
    }

    override fun <T> get(key: String): T? {
        if (!isRedisAvailable) return null

        val bucket: RBucket<T> = redissonClient!!.getBucket(buildKey(key))
        return bucket.get()
    }

    override fun <T> getOrLoad(key: String, ttl: Duration, loader: () -> T?): T? {
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

    override fun delete(key: String): Boolean {
        if (!isRedisAvailable) return false

        val bucket: RBucket<Any> = redissonClient!!.getBucket(buildKey(key))
        val deleted = bucket.delete()
        log.debug("Cache DELETE: key={}, success={}", key, deleted)
        return deleted
    }

    override fun deleteByPattern(pattern: String): Long {
        if (!isRedisAvailable) return 0

        val keys = redissonClient!!.keys
        val fullPattern = buildKey(pattern)
        val count = keys.deleteByPattern(fullPattern)
        log.debug("Cache DELETE BY PATTERN: pattern={}, count={}", pattern, count)
        return count
    }

    override fun exists(key: String): Boolean {
        if (!isRedisAvailable) return false

        val bucket: RBucket<Any> = redissonClient!!.getBucket(buildKey(key))
        return bucket.isExists
    }

    override fun expire(key: String, ttl: Duration): Boolean {
        if (!isRedisAvailable) return false

        val bucket: RBucket<Any> = redissonClient!!.getBucket(buildKey(key))
        return bucket.expire(ttl)
    }

    override fun ttl(key: String): Long {
        if (!isRedisAvailable) return -1

        val bucket: RBucket<Any> = redissonClient!!.getBucket(buildKey(key))
        return bucket.remainTimeToLive()
    }

    // ==========================================
    // Hash 操作
    // ==========================================

    override fun <K, V> hSet(key: String, field: K, value: V) {
        if (!isRedisAvailable) return

        val map: RMap<K, V> = redissonClient!!.getMap(buildKey(key))
        map[field] = value
    }

    override fun <K, V> hGet(key: String, field: K): V? {
        if (!isRedisAvailable) return null

        val map: RMap<K, V> = redissonClient!!.getMap(buildKey(key))
        return map[field]
    }

    override fun <K, V> hGetAll(key: String): Map<K, V> {
        if (!isRedisAvailable) return emptyMap()

        val map: RMap<K, V> = redissonClient!!.getMap(buildKey(key))
        return map.readAllMap()
    }

    override fun <K> hDel(key: String, vararg fields: K): Long {
        if (!isRedisAvailable) return 0

        val map: RMap<K, Any> = redissonClient!!.getMap(buildKey(key))
        return map.fastRemove(*fields)
    }

    // ==========================================
    // List 操作
    // ==========================================

    override fun <T> rPush(key: String, vararg values: T): Boolean {
        if (!isRedisAvailable) return false

        val list: RList<T> = redissonClient!!.getList(buildKey(key))
        return list.addAll(values.toList())
    }

    override fun <T> lRange(key: String, start: Int, end: Int): List<T> {
        if (!isRedisAvailable) return emptyList()

        val list: RList<T> = redissonClient!!.getList(buildKey(key))
        return list.subList(start, end)
    }

    // ==========================================
    // Set 操作
    // ==========================================

    override fun <T> sAdd(key: String, vararg values: T): Boolean {
        if (!isRedisAvailable) return false

        val set: RSet<T> = redissonClient!!.getSet(buildKey(key))
        return set.addAll(values.toList())
    }

    override fun <T> sMembers(key: String): Set<T> {
        if (!isRedisAvailable) return emptySet()

        val set: RSet<T> = redissonClient!!.getSet(buildKey(key))
        return set.readAll()
    }

    override fun <T> sIsMember(key: String, value: T): Boolean {
        if (!isRedisAvailable) return false

        val set: RSet<T> = redissonClient!!.getSet(buildKey(key))
        return set.contains(value)
    }

    // ==========================================
    // 分布式锁
    // ==========================================

    override fun tryLock(lockKey: String, waitTime: Duration, leaseTime: Duration): Boolean {
        if (!isRedisAvailable) {
            log.warn("Redis not available, cannot acquire lock: {}", lockKey)
            return false
        }

        val lock = redissonClient!!.getLock(buildKey("lock:$lockKey"))
        return lock.tryLock(waitTime.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS)
    }

    override fun unlock(lockKey: String) {
        if (!isRedisAvailable) return

        val lock = redissonClient!!.getLock(buildKey("lock:$lockKey"))
        if (lock.isHeldByCurrentThread) {
            lock.unlock()
        }
    }

    override fun <T> withLock(
        lockKey: String,
        waitTime: Duration,
        leaseTime: Duration,
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

    override fun bloomFilterInit(name: String, expectedInsertions: Long, falseProbability: Double): Boolean {
        if (!isRedisAvailable) return false

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.tryInit(expectedInsertions, falseProbability)
    }

    override fun bloomFilterAdd(name: String, value: String): Boolean {
        if (!isRedisAvailable) return false

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.add(value)
    }

    override fun bloomFilterAddAll(name: String, values: Collection<String>): Long {
        if (!isRedisAvailable) return 0

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        var count = 0L
        values.forEach {
            if (bloomFilter.add(it)) count++
        }
        return count
    }

    override fun bloomFilterContains(name: String, value: String): Boolean {
        if (!isRedisAvailable) return false

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.contains(value)
    }

    override fun bloomFilterCount(name: String): Long {
        if (!isRedisAvailable) return 0

        val bloomFilter = redissonClient!!.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.count()
    }

    override fun bloomFilterDelete(name: String): Boolean {
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