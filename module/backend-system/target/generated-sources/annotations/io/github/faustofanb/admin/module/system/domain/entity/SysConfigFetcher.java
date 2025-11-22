package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Override;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.lang.NewChain;
import org.babyfish.jimmer.meta.ImmutableProp;
import org.babyfish.jimmer.sql.ast.table.Table;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.babyfish.jimmer.sql.fetcher.FieldConfig;
import org.babyfish.jimmer.sql.fetcher.IdOnlyFetchType;
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl;
import org.babyfish.jimmer.sql.fetcher.spi.AbstractTypedFetcher;

@GeneratedBy(
        type = SysConfig.class
)
public class SysConfigFetcher extends AbstractTypedFetcher<SysConfig, SysConfigFetcher> {
    public static final SysConfigFetcher $ = new SysConfigFetcher(null);

    private SysConfigFetcher(FetcherImpl<SysConfig> base) {
        super(SysConfig.class, base);
    }

    private SysConfigFetcher(SysConfigFetcher prev, ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        super(prev, prop, negative, idOnlyFetchType);
    }

    private SysConfigFetcher(SysConfigFetcher prev, ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        super(prev, prop, fieldConfig);
    }

    public static SysConfigFetcher $from(Fetcher<SysConfig> base) {
        return base instanceof SysConfigFetcher ? 
        	(SysConfigFetcher)base : 
        	new SysConfigFetcher((FetcherImpl<SysConfig>)base);
    }

    @NewChain
    public SysConfigFetcher createdTime() {
        return add("createdTime");
    }

    @NewChain
    public SysConfigFetcher createdTime(boolean enabled) {
        return enabled ? add("createdTime") : remove("createdTime");
    }

    @NewChain
    public SysConfigFetcher updatedTime() {
        return add("updatedTime");
    }

    @NewChain
    public SysConfigFetcher updatedTime(boolean enabled) {
        return enabled ? add("updatedTime") : remove("updatedTime");
    }

    @NewChain
    public SysConfigFetcher createdBy() {
        return add("createdBy");
    }

    @NewChain
    public SysConfigFetcher createdBy(boolean enabled) {
        return enabled ? add("createdBy") : remove("createdBy");
    }

    @NewChain
    public SysConfigFetcher updatedBy() {
        return add("updatedBy");
    }

    @NewChain
    public SysConfigFetcher updatedBy(boolean enabled) {
        return enabled ? add("updatedBy") : remove("updatedBy");
    }

    @NewChain
    public SysConfigFetcher configName() {
        return add("configName");
    }

    @NewChain
    public SysConfigFetcher configName(boolean enabled) {
        return enabled ? add("configName") : remove("configName");
    }

    @NewChain
    public SysConfigFetcher configKey() {
        return add("configKey");
    }

    @NewChain
    public SysConfigFetcher configKey(boolean enabled) {
        return enabled ? add("configKey") : remove("configKey");
    }

    @NewChain
    public SysConfigFetcher configValue() {
        return add("configValue");
    }

    @NewChain
    public SysConfigFetcher configValue(boolean enabled) {
        return enabled ? add("configValue") : remove("configValue");
    }

    @NewChain
    public SysConfigFetcher configType() {
        return add("configType");
    }

    @NewChain
    public SysConfigFetcher configType(boolean enabled) {
        return enabled ? add("configType") : remove("configType");
    }

    @NewChain
    public SysConfigFetcher remark() {
        return add("remark");
    }

    @NewChain
    public SysConfigFetcher remark(boolean enabled) {
        return enabled ? add("remark") : remove("remark");
    }

    @Override
    protected SysConfigFetcher createFetcher(ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        return new SysConfigFetcher(this, prop, negative, idOnlyFetchType);
    }

    @Override
    protected SysConfigFetcher createFetcher(ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        return new SysConfigFetcher(this, prop, fieldConfig);
    }
}
