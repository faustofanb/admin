package io.github.faustofan.admin.shared.cache.core;

import java.io.Serializable;

// 简洁、不可变的消息体
public record CacheInvalidateMsg(
        String cacheName,
        Object key,
        String sourceInstanceId) implements Serializable {
}