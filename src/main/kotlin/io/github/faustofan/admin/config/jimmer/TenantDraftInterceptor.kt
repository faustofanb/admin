package io.github.faustofan.admin.config.jimmer

import io.github.faustofan.admin.common.context.AppContextHolder
import io.github.faustofan.admin.common.model.TenantAware
import io.github.faustofan.admin.common.model.TenantAwareDraft
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component

/**
 * 租户字段拦截器
 *
 * 在实体保存前自动填充租户ID
 * 从当前用户上下文中获取租户信息
 */
@Component
class TenantDraftInterceptor : DraftInterceptor<TenantAware, TenantAwareDraft> {

    override fun beforeSave(draft: TenantAwareDraft, original: TenantAware?) {
        // 只在新增时填充租户ID
        if (original === null) {
            if (!isLoaded(draft, TenantAware::tenantId)) {
                val tenantId = AppContextHolder.get().tenantId
                    ?: throw IllegalStateException("租户上下文未初始化，无法保存租户隔离数据")
                draft.tenantId = tenantId
            }
        }
        // 注意：更新时不允许修改租户ID，保证数据安全
    }
}

