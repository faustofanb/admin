package io.github.faustofan.admin.shared.domain.event

import io.github.faustofan.admin.shared.application.context.AppContextHolder
import java.time.LocalDateTime

/**
 * 领域事件基类
 */
abstract class DomainEvent {
    val occurredOn: LocalDateTime = LocalDateTime.now()
    val operatorId: Long? = AppContextHolder.get().userId
}

