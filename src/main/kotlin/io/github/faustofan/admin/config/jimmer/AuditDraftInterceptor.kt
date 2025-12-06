package io.github.faustofan.admin.config.jimmer

import io.github.faustofan.admin.common.context.AppContextHolder
import io.github.faustofan.admin.common.model.BaseEntity
import io.github.faustofan.admin.common.model.BaseEntityDraft
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * 审计字段拦截器
 *
 * 在实体保存前自动填充审计字段：
 * - createdTime: 创建时间
 * - updatedTime: 更新时间
 * - createdBy: 创建人ID
 * - updatedBy: 更新人ID
 */
@Component
class AuditDraftInterceptor : DraftInterceptor<BaseEntity, BaseEntityDraft> {

    override fun beforeSave(draft: BaseEntityDraft, original: BaseEntity?) {
        val now = Instant.now()
        val currentUserId = AppContextHolder.get().userId

        // 新增时填充创建时间和创建人
        if (original === null) {
            if (!isLoaded(draft, BaseEntity::createdTime)) {
                draft.createdTime = now
            }
            if (!isLoaded(draft, BaseEntity::createdBy)) {
                draft.createdBy = currentUserId
            }
            // 新增时逻辑删除字段默认为 false
            if (!isLoaded(draft, BaseEntity::deleted)) {
                draft.deleted = false
            }
        }

        // 新增和更新都填充更新时间和更新人
        draft.updatedTime = now
        draft.updatedBy = currentUserId
    }
}

