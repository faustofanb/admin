package io.github.faustofan.admin.system.domain.event;

import java.io.Serializable;
import java.time.Instant;

/**
 * 用户密码变更事件
 */
public record UserPasswordChangedEvent(
        Long userId,
        Long tenantId,
        String username,
        Instant timestamp
) implements Serializable {}
