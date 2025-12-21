package io.github.faustofan.admin.shared.persistence.filter;

import io.github.faustofan.admin.shared.common.constant.CustomConstants;
import io.github.faustofan.admin.shared.common.context.AppContext;
import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import io.github.faustofan.admin.shared.common.exception.CommonErrorCode;
import io.github.faustofan.admin.shared.common.exception.SystemException;
import io.github.faustofan.admin.shared.persistence.model.TenantAwareProps;
import org.babyfish.jimmer.sql.filter.Filter;
import org.babyfish.jimmer.sql.filter.FilterArgs;
import org.springframework.stereotype.Component;

/**
 * 租户过滤器
 */
@Component
public class TenantFilter implements Filter<TenantAwareProps> {
    /**
     * 添加租户过滤条件
     */
    @Override
    public void filter(FilterArgs<TenantAwareProps> args) {
        if(AppContextHolder.getContext() == null)
            throw new SystemException(CommonErrorCode.CONTEXT_MISSING);

        AppContext appContext = AppContextHolder.getContext();

        if(appContext.tenantId() == null)
            throw new SystemException(CommonErrorCode.TENANT_ID_MISSING);

        Long tenantId = appContext.tenantId();

        //如果是系统操作， 比如用户第一次登录时， 就放行
        if(tenantId.equals(CustomConstants.SYSTEM_TENANT_ID))
            return;
        //如果是超级管理员， 放行
        if(appContext.isSuperAdmin())
            return;
        //其他情况， 都要加租户过滤
        args.where(args.getTable().tenantId().eq(tenantId));
    }
}
