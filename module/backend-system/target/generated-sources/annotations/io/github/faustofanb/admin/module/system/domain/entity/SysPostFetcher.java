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
        type = SysPost.class
)
public class SysPostFetcher extends AbstractTypedFetcher<SysPost, SysPostFetcher> {
    public static final SysPostFetcher $ = new SysPostFetcher(null);

    private SysPostFetcher(FetcherImpl<SysPost> base) {
        super(SysPost.class, base);
    }

    private SysPostFetcher(SysPostFetcher prev, ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        super(prev, prop, negative, idOnlyFetchType);
    }

    private SysPostFetcher(SysPostFetcher prev, ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        super(prev, prop, fieldConfig);
    }

    public static SysPostFetcher $from(Fetcher<SysPost> base) {
        return base instanceof SysPostFetcher ? 
        	(SysPostFetcher)base : 
        	new SysPostFetcher((FetcherImpl<SysPost>)base);
    }

    @NewChain
    public SysPostFetcher createdTime() {
        return add("createdTime");
    }

    @NewChain
    public SysPostFetcher createdTime(boolean enabled) {
        return enabled ? add("createdTime") : remove("createdTime");
    }

    @NewChain
    public SysPostFetcher updatedTime() {
        return add("updatedTime");
    }

    @NewChain
    public SysPostFetcher updatedTime(boolean enabled) {
        return enabled ? add("updatedTime") : remove("updatedTime");
    }

    @NewChain
    public SysPostFetcher createdBy() {
        return add("createdBy");
    }

    @NewChain
    public SysPostFetcher createdBy(boolean enabled) {
        return enabled ? add("createdBy") : remove("createdBy");
    }

    @NewChain
    public SysPostFetcher updatedBy() {
        return add("updatedBy");
    }

    @NewChain
    public SysPostFetcher updatedBy(boolean enabled) {
        return enabled ? add("updatedBy") : remove("updatedBy");
    }

    @NewChain
    public SysPostFetcher postCode() {
        return add("postCode");
    }

    @NewChain
    public SysPostFetcher postCode(boolean enabled) {
        return enabled ? add("postCode") : remove("postCode");
    }

    @NewChain
    public SysPostFetcher postName() {
        return add("postName");
    }

    @NewChain
    public SysPostFetcher postName(boolean enabled) {
        return enabled ? add("postName") : remove("postName");
    }

    @NewChain
    public SysPostFetcher sort() {
        return add("sort");
    }

    @NewChain
    public SysPostFetcher sort(boolean enabled) {
        return enabled ? add("sort") : remove("sort");
    }

    @NewChain
    public SysPostFetcher status() {
        return add("status");
    }

    @NewChain
    public SysPostFetcher status(boolean enabled) {
        return enabled ? add("status") : remove("status");
    }

    @NewChain
    public SysPostFetcher remark() {
        return add("remark");
    }

    @NewChain
    public SysPostFetcher remark(boolean enabled) {
        return enabled ? add("remark") : remove("remark");
    }

    @Override
    protected SysPostFetcher createFetcher(ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        return new SysPostFetcher(this, prop, negative, idOnlyFetchType);
    }

    @Override
    protected SysPostFetcher createFetcher(ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        return new SysPostFetcher(this, prop, fieldConfig);
    }
}
