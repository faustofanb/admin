package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Override;
import java.util.function.Consumer;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.lang.NewChain;
import org.babyfish.jimmer.meta.ImmutableProp;
import org.babyfish.jimmer.sql.ast.table.Table;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.babyfish.jimmer.sql.fetcher.FieldConfig;
import org.babyfish.jimmer.sql.fetcher.IdOnlyFetchType;
import org.babyfish.jimmer.sql.fetcher.ReferenceFetchType;
import org.babyfish.jimmer.sql.fetcher.ReferenceFieldConfig;
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl;
import org.babyfish.jimmer.sql.fetcher.spi.AbstractTypedFetcher;

@GeneratedBy(
        type = SysDictData.class
)
public class SysDictDataFetcher extends AbstractTypedFetcher<SysDictData, SysDictDataFetcher> {
    public static final SysDictDataFetcher $ = new SysDictDataFetcher(null);

    private SysDictDataFetcher(FetcherImpl<SysDictData> base) {
        super(SysDictData.class, base);
    }

    private SysDictDataFetcher(SysDictDataFetcher prev, ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        super(prev, prop, negative, idOnlyFetchType);
    }

    private SysDictDataFetcher(SysDictDataFetcher prev, ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        super(prev, prop, fieldConfig);
    }

    public static SysDictDataFetcher $from(Fetcher<SysDictData> base) {
        return base instanceof SysDictDataFetcher ? 
        	(SysDictDataFetcher)base : 
        	new SysDictDataFetcher((FetcherImpl<SysDictData>)base);
    }

    @NewChain
    public SysDictDataFetcher createdTime() {
        return add("createdTime");
    }

    @NewChain
    public SysDictDataFetcher createdTime(boolean enabled) {
        return enabled ? add("createdTime") : remove("createdTime");
    }

    @NewChain
    public SysDictDataFetcher updatedTime() {
        return add("updatedTime");
    }

    @NewChain
    public SysDictDataFetcher updatedTime(boolean enabled) {
        return enabled ? add("updatedTime") : remove("updatedTime");
    }

    @NewChain
    public SysDictDataFetcher createdBy() {
        return add("createdBy");
    }

    @NewChain
    public SysDictDataFetcher createdBy(boolean enabled) {
        return enabled ? add("createdBy") : remove("createdBy");
    }

    @NewChain
    public SysDictDataFetcher updatedBy() {
        return add("updatedBy");
    }

    @NewChain
    public SysDictDataFetcher updatedBy(boolean enabled) {
        return enabled ? add("updatedBy") : remove("updatedBy");
    }

    @NewChain
    public SysDictDataFetcher sort() {
        return add("sort");
    }

    @NewChain
    public SysDictDataFetcher sort(boolean enabled) {
        return enabled ? add("sort") : remove("sort");
    }

    @NewChain
    public SysDictDataFetcher label() {
        return add("label");
    }

    @NewChain
    public SysDictDataFetcher label(boolean enabled) {
        return enabled ? add("label") : remove("label");
    }

    @NewChain
    public SysDictDataFetcher value() {
        return add("value");
    }

    @NewChain
    public SysDictDataFetcher value(boolean enabled) {
        return enabled ? add("value") : remove("value");
    }

    @NewChain
    public SysDictDataFetcher dictType() {
        return add("dictType");
    }

    @NewChain
    public SysDictDataFetcher dictType(boolean enabled) {
        return enabled ? add("dictType") : remove("dictType");
    }

    @NewChain
    public SysDictDataFetcher dictType(Fetcher<SysDictType> childFetcher) {
        return add("dictType", childFetcher);
    }

    @NewChain
    public SysDictDataFetcher dictType(IdOnlyFetchType idOnlyFetchType) {
        return add("dictType", idOnlyFetchType);
    }

    @NewChain
    public SysDictDataFetcher dictType(Fetcher<SysDictType> childFetcher,
            Consumer<ReferenceFieldConfig<SysDictType, SysDictTypeTable>> fieldConfig) {
        return add("dictType", childFetcher, fieldConfig);
    }

    @NewChain
    public SysDictDataFetcher dictType(ReferenceFetchType fetchType,
            Fetcher<SysDictType> childFetcher) {
        return dictType(childFetcher, cfg -> cfg.fetchType(fetchType));
    }

    @NewChain
    public SysDictDataFetcher cssClass() {
        return add("cssClass");
    }

    @NewChain
    public SysDictDataFetcher cssClass(boolean enabled) {
        return enabled ? add("cssClass") : remove("cssClass");
    }

    @NewChain
    public SysDictDataFetcher listClass() {
        return add("listClass");
    }

    @NewChain
    public SysDictDataFetcher listClass(boolean enabled) {
        return enabled ? add("listClass") : remove("listClass");
    }

    @NewChain
    public SysDictDataFetcher status() {
        return add("status");
    }

    @NewChain
    public SysDictDataFetcher status(boolean enabled) {
        return enabled ? add("status") : remove("status");
    }

    @NewChain
    public SysDictDataFetcher remark() {
        return add("remark");
    }

    @NewChain
    public SysDictDataFetcher remark(boolean enabled) {
        return enabled ? add("remark") : remove("remark");
    }

    @Override
    protected SysDictDataFetcher createFetcher(ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        return new SysDictDataFetcher(this, prop, negative, idOnlyFetchType);
    }

    @Override
    protected SysDictDataFetcher createFetcher(ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        return new SysDictDataFetcher(this, prop, fieldConfig);
    }
}
