package io.github.faustofan.admin.system.domain.events

import io.github.faustofan.admin.common.AppContextHolder
import java.time.LocalDateTime

/**
 * 领域事件基类
 */
abstract class DomainEvent {
    val occurredOn: LocalDateTime = LocalDateTime.now()
    val operatorId: Long? = AppContextHolder.get().userId
}

