package io.github.faustofan.admin.cache

import org.redisson.api.RBucket
import org.redisson.api.RList
import org.redisson.api.RMap
import org.redisson.api.RSet
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * 通用缓存服务
 *
 * 提供业务层使用的缓存 API，封装 Redisson 操作
 */
@Service
@ConditionalOnBean(RedissonClient::class)
class CacheService(
    private val redissonClient: RedissonClient
) {
    private val log = LoggerFactory.getLogger(CacheService::class.java)

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
        val bucket: RBucket<T> = redissonClient.getBucket(buildKey(key))
        bucket.set(value, ttl)
        log.debug("Cache SET: key={}, ttl={}s", key, ttl.seconds)
    }

    /**
     * 获取缓存
     */
    fun <T> get(key: String): T? {
        val bucket: RBucket<T> = redissonClient.getBucket(buildKey(key))
        return bucket.get()
    }

    /**
     * 获取缓存，不存在则加载
     */
    fun <T> getOrLoad(key: String, ttl: Duration = DEFAULT_TTL, loader: () -> T?): T? {
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
        val bucket: RBucket<Any> = redissonClient.getBucket(buildKey(key))
        val deleted = bucket.delete()
        log.debug("Cache DELETE: key={}, success={}", key, deleted)
        return deleted
    }

    /**
     * 批量删除缓存（按模式匹配）
     */
    fun deleteByPattern(pattern: String): Long {
        val keys = redissonClient.keys
        val fullPattern = buildKey(pattern)
        val count = keys.deleteByPattern(fullPattern)
        log.debug("Cache DELETE BY PATTERN: pattern={}, count={}", pattern, count)
        return count
    }

    /**
     * 检查缓存是否存在
     */
    fun exists(key: String): Boolean {
        val bucket: RBucket<Any> = redissonClient.getBucket(buildKey(key))
        return bucket.isExists
    }

    /**
     * 设置过期时间
     */
    fun expire(key: String, ttl: Duration): Boolean {
        val bucket: RBucket<Any> = redissonClient.getBucket(buildKey(key))
        return bucket.expire(ttl)
    }

    /**
     * 获取剩余过期时间（毫秒）
     */
    fun ttl(key: String): Long {
        val bucket: RBucket<Any> = redissonClient.getBucket(buildKey(key))
        return bucket.remainTimeToLive()
    }

    // ==========================================
    // Hash 操作
    // ==========================================

    /**
     * Hash 设置
     */
    fun <K, V> hSet(key: String, field: K, value: V) {
        val map: RMap<K, V> = redissonClient.getMap(buildKey(key))
        map[field] = value
    }

    /**
     * Hash 获取
     */
    fun <K, V> hGet(key: String, field: K): V? {
        val map: RMap<K, V> = redissonClient.getMap(buildKey(key))
        return map[field]
    }

    /**
     * Hash 获取全部
     */
    fun <K, V> hGetAll(key: String): Map<K, V> {
        val map: RMap<K, V> = redissonClient.getMap(buildKey(key))
        return map.readAllMap()
    }

    /**
     * Hash 删除字段
     */
    fun <K> hDel(key: String, vararg fields: K): Long {
        val map: RMap<K, Any> = redissonClient.getMap(buildKey(key))
        return map.fastRemove(*fields)
    }

    // ==========================================
    // List 操作
    // ==========================================

    /**
     * List 右侧添加
     */
    fun <T> rPush(key: String, vararg values: T): Boolean {
        val list: RList<T> = redissonClient.getList(buildKey(key))
        return list.addAll(values.toList())
    }

    /**
     * List 获取范围
     */
    fun <T> lRange(key: String, start: Int, end: Int): List<T> {
        val list: RList<T> = redissonClient.getList(buildKey(key))
        return list.subList(start, end)
    }

    // ==========================================
    // Set 操作
    // ==========================================

    /**
     * Set 添加
     */
    fun <T> sAdd(key: String, vararg values: T): Boolean {
        val set: RSet<T> = redissonClient.getSet(buildKey(key))
        return set.addAll(values.toList())
    }

    /**
     * Set 获取全部成员
     */
    fun <T> sMembers(key: String): Set<T> {
        val set: RSet<T> = redissonClient.getSet(buildKey(key))
        return set.readAll()
    }

    /**
     * Set 是否包含
     */
    fun <T> sIsMember(key: String, value: T): Boolean {
        val set: RSet<T> = redissonClient.getSet(buildKey(key))
        return set.contains(value)
    }

    // ==========================================
    // 分布式锁
    // ==========================================

    /**
     * 尝试获取锁
     */
    fun tryLock(lockKey: String, waitTime: Duration, leaseTime: Duration): Boolean {
        val lock = redissonClient.getLock(buildKey("lock:$lockKey"))
        return lock.tryLock(waitTime.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS)
    }

    /**
     * 释放锁
     */
    fun unlock(lockKey: String) {
        val lock = redissonClient.getLock(buildKey("lock:$lockKey"))
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
        val bloomFilter = redissonClient.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.tryInit(expectedInsertions, falseProbability)
    }

    /**
     * 向布隆过滤器添加元素
     */
    fun bloomFilterAdd(name: String, value: String): Boolean {
        val bloomFilter = redissonClient.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.add(value)
    }

    /**
     * 批量向布隆过滤器添加元素
     */
    fun bloomFilterAddAll(name: String, values: Collection<String>): Long {
        val bloomFilter = redissonClient.getBloomFilter<String>(buildKey("bloom:$name"))
        var count = 0L
        values.forEach {
            if (bloomFilter.add(it)) count++
        }
        return count
    }

    /**
     * 检查元素是否可能存在于布隆过滤器中
     * 返回 false 表示一定不存在，返回 true 表示可能存在
     */
    fun bloomFilterContains(name: String, value: String): Boolean {
        val bloomFilter = redissonClient.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.contains(value)
    }

    /**
     * 获取布隆过滤器已添加元素数量
     */
    fun bloomFilterCount(name: String): Long {
        val bloomFilter = redissonClient.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.count()
    }

    /**
     * 删除布隆过滤器
     */
    fun bloomFilterDelete(name: String): Boolean {
        val bloomFilter = redissonClient.getBloomFilter<String>(buildKey("bloom:$name"))
        return bloomFilter.delete()
    }

    // ==========================================
    // 工具方法
    // ==========================================

    private fun buildKey(key: String): String {
        return "$KEY_PREFIX$key"
    }
}

