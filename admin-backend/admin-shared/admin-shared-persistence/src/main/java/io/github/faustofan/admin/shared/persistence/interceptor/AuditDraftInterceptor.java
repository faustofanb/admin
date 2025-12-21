package io.github.faustofan.admin.shared.persistence.interceptor;

import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import io.github.faustofan.admin.shared.common.exception.CommonErrorCode;
import io.github.faustofan.admin.shared.common.exception.SystemException;
import io.github.faustofan.admin.shared.persistence.model.BaseEntity;
import io.github.faustofan.admin.shared.persistence.model.BaseEntityDraft;
import io.github.faustofan.admin.shared.persistence.model.BaseEntityProps;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.sql.DraftInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * 审计字段拦截器
 * 在保存草稿前，自动设置创建人、创建时间、更新人、更新时间等审计字段
 */
@Component
public class AuditDraftInterceptor implements DraftInterceptor<BaseEntity, BaseEntityDraft> {

    /**
     * 在保存草稿前调用
     * @param draft 草稿对象
     * @param original 原始实体对象，新增时为 null
     */
    @Override
    public void beforeSave(@NotNull BaseEntityDraft draft, @Nullable BaseEntity original) {
        if(null == AppContextHolder.getContext())
            throw new SystemException(CommonErrorCode.CONTEXT_MISSING);

        Instant now = Instant.now();

        // 新增时，设置创建人和创建时间
        if (original == null) {
            if(!ImmutableObjects.isLoaded(draft, BaseEntityProps.CREATED_TIME)) {
                draft.setCreatedTime(now);
            }
            if(!ImmutableObjects.isLoaded(draft, BaseEntityProps.DELETED)) {
                draft.setDeleted(false); // 默认未删除
            }
        }

        draft.setUpdatedTime(now);
    }
}
