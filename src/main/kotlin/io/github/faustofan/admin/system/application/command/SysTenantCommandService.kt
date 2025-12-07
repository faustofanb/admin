package io.github.faustofan.admin.system.application.command

import io.github.faustofan.admin.shared.infrastructure.exception.BizException
import io.github.faustofan.admin.system.domain.event.*
import io.github.faustofan.admin.system.domain.model.TenantStatus
import io.github.faustofan.admin.system.domain.service.disable
import io.github.faustofan.admin.system.domain.service.enable
import io.github.faustofan.admin.system.domain.service.renew
import io.github.faustofan.admin.system.dto.SysTenantCreateCommand
import io.github.faustofan.admin.system.dto.SysTenantUpdateCommand
import io.github.faustofan.admin.system.infrastructure.persistence.SysTenantRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 租户命令服务
 *
 * 职责：处理租户的创建、更新、续费和状态变更等业务操作
 * 原则：只关注业务逻辑，通过发布领域事件解耦缓存、通知等副作用
 */
@Service
@Transactional
class SysTenantCommandService(
    private val tenantRepository: SysTenantRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    /**
     * 创建租户
     */
    fun create(cmd: SysTenantCreateCommand): Long {
        val entity = tenantRepository.save(cmd.toEntity())

        eventPublisher.publishEvent(
            TenantCreatedEvent(
                tenantId = entity.id,
                name = entity.name,
                code = entity.code,
                contactName = entity.contactName,
                email = null
            )
        )

        return entity.id
    }

    /**
     * 更新租户信息
     */
    fun update(cmd: SysTenantUpdateCommand): Long {
        val entity = tenantRepository.save(cmd.toEntity())

        eventPublisher.publishEvent(
            TenantUpdatedEvent(
                tenantId = entity.id,
                name = entity.name,
                code = entity.code
            )
        )

        return entity.id
    }

    /**
     * 租户续费
     */
    fun renew(tenantId: Long, days: Long) {
        val tenant = tenantRepository.findById(tenantId)
            .orElseThrow { BizException(message = "租户不存在") }

        val renewedTenant = tenant.renew(days)
        tenantRepository.save(renewedTenant, SaveMode.UPDATE_ONLY)

        eventPublisher.publishEvent(
            TenantRenewedEvent(
                tenantId = tenant.id,
                name = tenant.name,
                daysAdded = days,
                newExpireTime = renewedTenant.expireTime
            )
        )
    }

    /**
     * 更改租户状态
     */
    fun changeStatus(tenantId: Long, status: TenantStatus) {
        val tenant = tenantRepository.findById(tenantId)
            .orElseThrow { BizException(message = "租户不存在") }

        if (tenant.status == status) return

        val oldStatus = tenant.status
        val updatedTenant = if (status == TenantStatus.ENABLE) tenant.enable() else tenant.disable()
        tenantRepository.save(updatedTenant, SaveMode.UPDATE_ONLY)

        eventPublisher.publishEvent(
            TenantStatusChangedEvent(
                tenantId = tenant.id,
                name = tenant.name,
                oldStatus = oldStatus,
                newStatus = status
            )
        )
    }

    /**
     * 删除租户
     */
    fun delete(tenantId: Long) {
        val tenant = tenantRepository.findById(tenantId)
            .orElseThrow { BizException(message = "租户不存在") }

        tenantRepository.deleteById(tenantId)

        eventPublisher.publishEvent(
            TenantDeletedEvent(
                tenantId = tenantId,
                code = tenant.code
            )
        )
    }
}

