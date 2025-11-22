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
        type = SysDictType.class
)
public class SysDictTypeFetcher extends AbstractTypedFetcher<SysDictType, SysDictTypeFetcher> {
    public static final SysDictTypeFetcher $ = new SysDictTypeFetcher(null);

    private SysDictTypeFetcher(FetcherImpl<SysDictType> base) {
        super(SysDictType.class, base);
    }

    private SysDictTypeFetcher(SysDictTypeFetcher prev, ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        super(prev, prop, negative, idOnlyFetchType);
    }

    private SysDictTypeFetcher(SysDictTypeFetcher prev, ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        super(prev, prop, fieldConfig);
    }

    public static SysDictTypeFetcher $from(Fetcher<SysDictType> base) {
        return base instanceof SysDictTypeFetcher ? 
        	(SysDictTypeFetcher)base : 
        	new SysDictTypeFetcher((FetcherImpl<SysDictType>)base);
    }

    @NewChain
    public SysDictTypeFetcher createdTime() {
        return add("createdTime");
    }

    @NewChain
    public SysDictTypeFetcher createdTime(boolean enabled) {
        return enabled ? add("createdTime") : remove("createdTime");
    }

    @NewChain
    public SysDictTypeFetcher updatedTime() {
        return add("updatedTime");
    }

    @NewChain
    public SysDictTypeFetcher updatedTime(boolean enabled) {
        return enabled ? add("updatedTime") : remove("updatedTime");
    }

    @NewChain
    public SysDictTypeFetcher createdBy() {
        return add("createdBy");
    }

    @NewChain
    public SysDictTypeFetcher createdBy(boolean enabled) {
        return enabled ? add("createdBy") : remove("createdBy");
    }

    @NewChain
    public SysDictTypeFetcher updatedBy() {
        return add("updatedBy");
    }

    @NewChain
    public SysDictTypeFetcher updatedBy(boolean enabled) {
        return enabled ? add("updatedBy") : remove("updatedBy");
    }

    @NewChain
    public SysDictTypeFetcher name() {
        return add("name");
    }

    @NewChain
    public SysDictTypeFetcher name(boolean enabled) {
        return enabled ? add("name") : remove("name");
    }

    @NewChain
    public SysDictTypeFetcher type() {
        return add("type");
    }

    @NewChain
    public SysDictTypeFetcher type(boolean enabled) {
        return enabled ? add("type") : remove("type");
    }

    @NewChain
    public SysDictTypeFetcher status() {
        return add("status");
    }

    @NewChain
    public SysDictTypeFetcher status(boolean enabled) {
        return enabled ? add("status") : remove("status");
    }

    @NewChain
    public SysDictTypeFetcher remark() {
        return add("remark");
    }

    @NewChain
    public SysDictTypeFetcher remark(boolean enabled) {
        return enabled ? add("remark") : remove("remark");
    }

    @Override
    protected SysDictTypeFetcher createFetcher(ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        return new SysDictTypeFetcher(this, prop, negative, idOnlyFetchType);
    }

    @Override
    protected SysDictTypeFetcher createFetcher(ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        return new SysDictTypeFetcher(this, prop, fieldConfig);
    }
}
