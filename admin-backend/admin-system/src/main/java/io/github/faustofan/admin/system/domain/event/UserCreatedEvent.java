package io.github.faustofan.admin.system.domain.event;

import java.io.Serializable;
import java.time.Instant;

/**
 * 用户已创建事件
 */
public record UserCreatedEvent(
        Long userId,
        Long tenantId,
        String username,
        String nickname,
        Long orgId,
        Instant createTime
) implements Serializable {}
