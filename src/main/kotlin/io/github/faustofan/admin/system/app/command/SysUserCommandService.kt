package io.github.faustofan.admin.system.app.command

import io.github.faustofan.admin.common.exception.BizException
import io.github.faustofan.admin.system.domain.entity.*
import io.github.faustofan.admin.system.domain.events.*
import io.github.faustofan.admin.system.dto.SysUserCreateCommand
import io.github.faustofan.admin.system.dto.SysUserUpdateCommand
import io.github.faustofan.admin.system.infra.repository.SysUserRepository
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 用户命令服务
 *
 * 职责：处理用户的创建、更新、状态变更等写操作
 * 原则：只关注业务逻辑，通过发布领域事件解耦缓存、通知等副作用
 */
@Service
@Transactional
class SysUserCommandService(
    private val userRepository: SysUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: ApplicationEventPublisher
) {

    /**
     * 创建用户
     */
    fun create(cmd: SysUserCreateCommand): Long {
        val encodedPassword = passwordEncoder.encode(cmd.password)

        val entity = new(SysUser::class).by {
            username = cmd.username
            nickname = cmd.nickname
            encodedPassword?.let { passwordHash = it }
            email = cmd.email
            phone = cmd.phone
            avatar = cmd.avatar
            status = UserStatus.ACTIVE

            cmd.orgId?.let { org = new(SysOrg::class).by { id = it } }
            cmd.roleIds.let { ids ->
                roles = ids.map { roleId -> new(SysRole::class).by { id = roleId } }
            }
        }

        val savedUser = userRepository.save(entity)

        eventPublisher.publishEvent(
            UserCreatedEvent(
                userId = savedUser.id,
                username = savedUser.username,
                tenantId = savedUser.tenantId,
                nickname = savedUser.nickname,
                email = savedUser.email
            )
        )

        return savedUser.id
    }

    /**
     * 更新用户
     */
    fun update(id: Long, cmd: SysUserUpdateCommand): Long {
        val existing = userRepository.findById(id)
            .orElseThrow { BizException(message = "用户不存在") }

        val entity = new(SysUser::class).by {
            this.id = id
            nickname = cmd.nickname
            email = cmd.email
            phone = cmd.phone
            avatar = cmd.avatar
            status = cmd.status

            cmd.orgId?.let { org = new(SysOrg::class).by { this.id = it } }
            cmd.roleIds.let { ids ->
                roles = ids.map { roleId -> new(SysRole::class).by { this.id = roleId } }
            }
        }

        val savedUser = userRepository.save(entity, SaveMode.UPDATE_ONLY)

        eventPublisher.publishEvent(
            UserUpdatedEvent(
                userId = savedUser.id,
                username = existing.username,
                tenantId = existing.tenantId
            )
        )

        return savedUser.id
    }

    /**
     * 重置密码
     */
    fun resetPassword(userId: Long, newPassword: String) {
        val user = userRepository.findById(userId)
            .orElseThrow { BizException(message = "用户不存在") }

        val encodedPassword = passwordEncoder.encode(newPassword)

        val updated = new(SysUser::class).by {
            id = userId
            encodedPassword?.let { passwordHash = it }
        }

        userRepository.save(updated, SaveMode.UPDATE_ONLY)

        eventPublisher.publishEvent(
            UserPasswordChangedEvent(
                userId = userId,
                username = user.username,
                tenantId = user.tenantId
            )
        )
    }

    /**
     * 修改密码
     */
    fun changePassword(userId: Long, oldPassword: String, newPassword: String) {
        val user = userRepository.findById(userId)
            .orElseThrow { BizException(message = "用户不存在") }

        if (!passwordEncoder.matches(oldPassword, user.passwordHash)) {
            throw BizException(message = "原密码错误")
        }

        val encodedPassword = passwordEncoder.encode(newPassword)

        val updated = new(SysUser::class).by {
            id = userId
            encodedPassword?.let { passwordHash = it }
        }

        userRepository.save(updated, SaveMode.UPDATE_ONLY)

        eventPublisher.publishEvent(
            UserPasswordChangedEvent(
                userId = userId,
                username = user.username,
                tenantId = user.tenantId
            )
        )
    }

    /**
     * 变更用户状态
     */
    fun changeStatus(userId: Long, status: UserStatus) {
        val user = userRepository.findById(userId)
            .orElseThrow { BizException(message = "用户不存在") }

        if (user.status == status) return

        val oldStatus = user.status

        val updated = new(SysUser::class).by {
            id = userId
            this.status = status
        }

        userRepository.save(updated, SaveMode.UPDATE_ONLY)

        eventPublisher.publishEvent(
            UserStatusChangedEvent(
                userId = userId,
                username = user.username,
                oldStatus = oldStatus,
                newStatus = status
            )
        )
    }

    /**
     * 删除用户（逻辑删除）
     */
    fun delete(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { BizException(message = "用户不存在") }

        userRepository.deleteById(userId)

        eventPublisher.publishEvent(
            UserDeletedEvent(
                userId = userId,
                username = user.username,
                tenantId = user.tenantId
            )
        )
    }

    /**
     * 批量删除用户
     */
    fun batchDelete(userIds: List<Long>) {
        if (userIds.isEmpty()) return

        val firstUser = userRepository.findById(userIds.first())
            .orElseThrow { BizException(message = "用户不存在") }

        userRepository.deleteAllById(userIds)

        eventPublisher.publishEvent(
            UserBatchDeletedEvent(
                userIds = userIds,
                tenantId = firstUser.tenantId
            )
        )
    }
}

