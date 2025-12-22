package io.github.faustofan.admin.shared.distributed.core;

import java.time.Duration;
import java.util.Optional;

import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import io.github.faustofan.admin.shared.distributed.constants.KeyDefinition;

/** Redis 工具类，封装常用的 Redis 操作 */
@Component
public class RedisUtil {

    /**
     * Redis 基础设施通用操作类
     * <p>
     * 提供针对 Bucket, Set, Map 等数据结构的各种原子操作。
     * </p>
     */

    private final RedissonClient redissonClient;

    public RedisUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // ---------------- Bucket (String/Object) ----------------

    /**
     * 设置对象值
     */
    public <T> void set(KeyDefinition keyDef, String suffix, T value) {
        RBucket<T> bucket = redissonClient.getBucket(keyDef.buildKey(suffix));
        if (keyDef.getTtl().isZero()) {
            bucket.set(value);
        } else {
            bucket.set(value, keyDef.getTtl());
        }
    }

    /**
     * 获取对象值
     */
    public <T> Optional<T> get(KeyDefinition keyDef, String suffix) {
        RBucket<T> bucket = redissonClient.getBucket(keyDef.buildKey(suffix));
        return Optional.ofNullable(bucket.get());
    }

    /**
     * 原子操作：仅当不存在时设置 (SETNX)
     */
    public <T> boolean setIfAbsent(KeyDefinition keyDef, String suffix, T value) {
        RBucket<T> bucket = redissonClient.getBucket(keyDef.buildKey(suffix));
        Duration ttl = keyDef.getTtl().isZero() ? Duration.ofHours(1) : keyDef.getTtl();
        return bucket.setIfAbsent(value, ttl);
    }

    /**
     * 删除 Key
     */
    public void delete(KeyDefinition keyDef, String suffix) {
        redissonClient.getBucket(keyDef.buildKey(suffix)).delete();
    }

    // ---------------- Set (集合 - 用于名单/标签) ----------------

    public <T> void addToSet(KeyDefinition keyDef, String suffix, T value) {
        RSet<T> set = redissonClient.getSet(keyDef.buildKey(suffix));
        set.add(value);
        if (!keyDef.getTtl().isZero()) {
            set.expire(keyDef.getTtl());
        }
    }

    public <T> boolean isMemberOfSet(KeyDefinition keyDef, String suffix, T value) {
        return redissonClient.getSet(keyDef.buildKey(suffix)).contains(value);
    }

    public <T> void removeFromSet(KeyDefinition keyDef, String suffix, T value) {
        redissonClient.getSet(keyDef.buildKey(suffix)).remove(value);
    }

    // ---------------- Map (哈希 - 用于配置项) ----------------

    public <K, V> void putInMap(KeyDefinition keyDef, String suffix, K mapKey, V mapValue) {
        RMap<K, V> map = redissonClient.getMap(keyDef.buildKey(suffix));
        map.put(mapKey, mapValue);
        if (!keyDef.getTtl().isZero()) {
            map.expire(keyDef.getTtl());
        }
    }

    public <K, V> V getFromMap(KeyDefinition keyDef, String suffix, K mapKey) {
        RMap<K, V> map = redissonClient.getMap(keyDef.buildKey(suffix));
        return map.get(mapKey);
    }
}
