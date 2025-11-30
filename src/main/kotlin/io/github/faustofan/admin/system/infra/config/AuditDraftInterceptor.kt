package io.github.faustofan.admin.system.infra.config

import io.github.faustofan.admin.common.AppContextHolder
import io.github.faustofan.admin.common.BaseEntity
import io.github.faustofan.admin.common.BaseEntityDraft
import io.github.faustofan.admin.common.DataScoped
import io.github.faustofan.admin.common.DataScopedDraft
import io.github.faustofan.admin.common.TenantAware
import io.github.faustofan.admin.common.TenantAwareDraft
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * 审计字段和租户信息自动填充拦截器
 *
 * 该拦截器在实体保存之前自动填充以下字段：
 * - createdTime: 实体创建时间（仅在插入时设置）
 * - updatedTime: 实体更新时间（每次保存时更新）
 * - tenantId: 租户 ID（仅在插入时设置，适用于实现 TenantAware 接口的实体）
 * - orgId: 组织机构 ID（仅在插入时设置，适用于实现 DataScoped 接口的实体）
 * - createdBy: 创建人（仅在插入时设置，适用于实现 DataScoped 接口的实体）
 *
 * 通过使用该拦截器，可以确保所有实体在保存时都具有正确的审计信息和租户隔离信息，
 * 从而简化了代码并提高了数据一致性。
 */
@Component
class AuditDraftInterceptor: DraftInterceptor<BaseEntity, BaseEntityDraft> {

    /**
     * 在保存实体之前调用，用于自动填充审计字段和租户信息。
     *
     * @param draft 当前的实体草稿对象
     * @param original 原始的实体对象，如果是插入操作则为 null
     */
    override fun beforeSave(draft: BaseEntityDraft, original: BaseEntity?) {
        val appContext = AppContextHolder.get()
        // 1. 处理通用时间字段
        if (!isLoaded(draft, BaseEntity::updatedTime)) {
            draft.updatedTime = Instant.now()
        }

        // 2. 如果是 INSERT 操作 (original == null)
        if (original == null) {
            if (!isLoaded(draft, BaseEntity::createdTime)) {
                draft.createdTime = Instant.now()
            }

            // 3. 处理租户 ID
            if (draft is TenantAwareDraft) {
                if (!isLoaded(draft, TenantAware::tenantId)) {
                    appContext.tenantId?.let { draft.tenantId = it }
                }
            }

            // 4. 处理数据权限字段 (OrgId, CreatedBy)
            if (draft is DataScopedDraft) {
                if (!isLoaded(draft, DataScoped::orgId)) {
                    draft.orgId = appContext.orgId
                }
                if (!isLoaded(draft, DataScoped::createdBy)) {
                    // 假设 createdBy 存的是 username
                    appContext.username?.let { draft.createdBy = it }
                }
            }
        }
    }
}