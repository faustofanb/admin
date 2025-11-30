package io.github.faustofan.admin.common

import org.babyfish.jimmer.sql.*
import java.time.Instant
import java.time.LocalDateTime

/**
 * 基础实体接口
 * 包含 ID 和审计字段
 */
@MappedSuperclass
interface BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    val createdTime: Instant
    val updatedTime: Instant

    // 逻辑删除 (0: 未删除, >0: 已删除的时间戳或ID，Jimmer支持多种策略，这里用布尔示例，或者用 long)
    // 推荐使用 Long 类型存储时间戳作为逻辑删除，以保证唯一索引在删除后可用
    @LogicalDeleted("true")
    val deleted: Boolean
}



