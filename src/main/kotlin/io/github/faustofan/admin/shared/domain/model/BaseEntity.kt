package io.github.faustofan.admin.shared.domain.model

import io.github.faustofan.admin.shared.infrastructure.idgen.SnowflakeIdGenerator
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.LogicalDeleted
import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.Instant

/**
 * 基础实体接口
 * 包含 ID 和审计字段
 *
 * 使用雪花算法生成分布式唯一 ID
 */
@MappedSuperclass
interface BaseEntity {

    @Id
    @GeneratedValue(generatorType = SnowflakeIdGenerator::class)
    val id: Long

    val createdTime: Instant
    val updatedTime: Instant

    // 审计字段：创建人和更新人
    val createdBy: Long?
    val updatedBy: Long?

    // 逻辑删除 (false: 未删除, true: 已删除)
    @LogicalDeleted("true")
    val deleted: Boolean
}



