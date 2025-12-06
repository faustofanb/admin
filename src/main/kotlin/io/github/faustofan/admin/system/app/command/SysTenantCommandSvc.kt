package io.github.faustofan.admin.system.app.command

import io.github.faustofan.admin.common.exception.BizException
import io.github.faustofan.admin.system.domain.service.*
import io.github.faustofan.admin.system.domain.entity.TenantStatus
import io.github.faustofan.admin.system.domain.events.TenantCreatedEvent
import io.github.faustofan.admin.system.domain.events.TenantRenewedEvent
import io.github.faustofan.admin.system.domain.events.TenantStatusChangedEvent
import io.github.faustofan.admin.system.dto.SysTenantCreateCommand
import io.github.faustofan.admin.system.dto.SysTenantUpdateCommand
import io.github.faustofan.admin.system.infra.repository.SysTenantRepository
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 租户命令服务，处理租户的创建、更新、续费和状态变更等业务操作。
 *
 * 该服务负责与租户仓库交互，并在关键操作后发布领域事件。
 *
 * @property tenantRepository 租户仓库，用于持久化租户实体。
 * @property eventPublisher 事件发布器，用于发布领域事件。
 */
@Service
@Transactional
class SysTenantCommandService(
    private val tenantRepository: SysTenantRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    /**
     * 创建租户。
     *
     * @param cmd 租户创建命令对象，包含租户相关信息。
     * @return 新创建租户的ID。
     */
    fun create(cmd: SysTenantCreateCommand): Long {
        val entity = tenantRepository.save(cmd.toEntity())

        eventPublisher.publishEvent(
            TenantCreatedEvent(
                tenantId = entity.id,
                name = entity.name,
                code = entity.code,
                contactName = entity.contactName,
                email = null // DTO 如果没传 email 暂时为空
            )
        )
        return entity.id
    }

    /**
     * 更新租户信息。
     *
     * @param cmd 租户更新命令对象，包含需更新的信息。
     * @return 更新后的租户ID。
     */
    fun update(cmd: SysTenantUpdateCommand): Long {
        val entity = tenantRepository.save(cmd.toEntity())
        // 普通更新通常不需要发核心事件，除非改了关键信息
        return entity.id
    }

    /**
     * 租户续费业务逻辑。
     *
     * @param tenantId 租户ID。
     * @param days 续费天数。
     * @throws BizException 如果租户不存在则抛出异常。
     */
    fun renew(tenantId: Long, days: Long) {
        val tenant = tenantRepository.findById(tenantId)
            .orElseThrow { BizException(message = "tenant not found") }

        // 领域行为：处理续费逻辑（计算时间、激活状态）
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
     * 更改租户状态（禁用/启用）。
     *
     * @param tenantId 租户ID。
     * @param status 目标状态。
     * @throws BizException 如果租户不存在则抛出异常。
     */
    fun changeStatus(tenantId: Long, status: TenantStatus) {
        val tenant = tenantRepository.findById(tenantId)
            .orElseThrow { BizException(message = "tenant not found") }
        if (tenant.status == status) return

        val oldStatus = tenant.status

        // 领域行为
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
}