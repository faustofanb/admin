package io.github.faustofan.admin.system.domain.model

import io.github.faustofan.admin.shared.domain.model.BaseEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.EnumType
import org.babyfish.jimmer.sql.Key
import java.time.LocalDateTime

@Entity
interface SysTenant : BaseEntity {

    // 租户名称，唯一键
    @Key
    val name: String

    // 租户编码 (用于二级域名或系统标识)
    @Key
    val code: String

    // 联系人
    val contactName: String?

    // 状态: ENABLE, DISABLE
    val status: TenantStatus

    // 过期时间
    val expireTime: LocalDateTime?
}

@EnumType
enum class TenantStatus {
    ENABLE, DISABLE
}