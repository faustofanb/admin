package io.github.faustofan.admin.system.domain.entity

import io.github.faustofan.admin.common.model.TenantAware
import org.babyfish.jimmer.sql.*

@Entity
@Table(name = "sys_user")
interface SysUser : TenantAware {

    @Key
    val username: String

    val nickname: String?

    // 密码 (加密存储)
    val passwordHash: String

    val email: String?

    val phone: String?

    val avatar: String?

    // 状态
    val status: UserStatus

    // 关联：所属部门 (多对一)
    @ManyToOne
    val org: SysOrg?

    // 关联：拥有的角色 (多对多)
    @ManyToMany
    @JoinTable(
        name = "sys_user_role_mapping",
        joinColumnName = "user_id",
        inverseJoinColumnName = "role_id"
    )
    val roles: List<SysRole>
}

/**
 * 用户状态枚举
 */
@EnumType(EnumType.Strategy.NAME)
enum class UserStatus {
    ACTIVE,
    LOCKED,
    DISABLED
}
