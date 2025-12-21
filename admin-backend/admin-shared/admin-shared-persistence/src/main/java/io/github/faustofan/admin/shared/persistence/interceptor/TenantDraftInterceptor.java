package io.github.faustofan.admin.shared.persistence.interceptor;

import io.github.faustofan.admin.shared.common.constant.CustomConstants;
import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import io.github.faustofan.admin.shared.common.exception.CommonErrorCode;
import io.github.faustofan.admin.shared.common.exception.SystemException;
import io.github.faustofan.admin.shared.persistence.model.TenantAware;
import io.github.faustofan.admin.shared.persistence.model.TenantAwareDraft;
import io.github.faustofan.admin.shared.persistence.model.TenantAwareProps;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.sql.DraftInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class TenantDraftInterceptor implements DraftInterceptor<TenantAware, TenantAwareDraft> {
    @Override
    public void beforeSave(@NotNull TenantAwareDraft draft, @Nullable TenantAware original) {
        if(null == AppContextHolder.getContext())
            throw new SystemException(CommonErrorCode.CONTEXT_MISSING);

        Long tenantId = AppContextHolder.getContext().tenantId();
        long userId = AppContextHolder.getContext().userId() == null
                ? CustomConstants.SYSTEM_USER_ID
                : AppContextHolder.getContext().userId();

        if(null == tenantId)
            throw new SystemException(CommonErrorCode.TENANT_ID_MISSING);

        // 新建时，设置租户ID
        if(null == original) {
            if(!ImmutableObjects.isLoaded(draft, TenantAwareProps.TENANT_ID)) {
                draft.setTenantId(tenantId);
            }
            if(!ImmutableObjects.isLoaded(draft, TenantAwareProps.CREATED_BY)) {
                draft.setCreatedBy(userId);
            }
        }
        // 更新时，都设置更新人和更新时间
        draft.setUpdatedBy(userId);
    }
}
