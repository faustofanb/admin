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
import org.babyfish.jimmer.sql.fetcher.ListFieldConfig;
import org.babyfish.jimmer.sql.fetcher.RecursiveListFieldConfig;
import org.babyfish.jimmer.sql.fetcher.RecursiveReferenceFieldConfig;
import org.babyfish.jimmer.sql.fetcher.ReferenceFetchType;
import org.babyfish.jimmer.sql.fetcher.ReferenceFieldConfig;
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl;
import org.babyfish.jimmer.sql.fetcher.spi.AbstractTypedFetcher;

@GeneratedBy(
        type = SysDept.class
)
public class SysDeptFetcher extends AbstractTypedFetcher<SysDept, SysDeptFetcher> {
    public static final SysDeptFetcher $ = new SysDeptFetcher(null);

    private SysDeptFetcher(FetcherImpl<SysDept> base) {
        super(SysDept.class, base);
    }

    private SysDeptFetcher(SysDeptFetcher prev, ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        super(prev, prop, negative, idOnlyFetchType);
    }

    private SysDeptFetcher(SysDeptFetcher prev, ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        super(prev, prop, fieldConfig);
    }

    public static SysDeptFetcher $from(Fetcher<SysDept> base) {
        return base instanceof SysDeptFetcher ? 
        	(SysDeptFetcher)base : 
        	new SysDeptFetcher((FetcherImpl<SysDept>)base);
    }

    @NewChain
    public SysDeptFetcher createdTime() {
        return add("createdTime");
    }

    @NewChain
    public SysDeptFetcher createdTime(boolean enabled) {
        return enabled ? add("createdTime") : remove("createdTime");
    }

    @NewChain
    public SysDeptFetcher updatedTime() {
        return add("updatedTime");
    }

    @NewChain
    public SysDeptFetcher updatedTime(boolean enabled) {
        return enabled ? add("updatedTime") : remove("updatedTime");
    }

    @NewChain
    public SysDeptFetcher createdBy() {
        return add("createdBy");
    }

    @NewChain
    public SysDeptFetcher createdBy(boolean enabled) {
        return enabled ? add("createdBy") : remove("createdBy");
    }

    @NewChain
    public SysDeptFetcher updatedBy() {
        return add("updatedBy");
    }

    @NewChain
    public SysDeptFetcher updatedBy(boolean enabled) {
        return enabled ? add("updatedBy") : remove("updatedBy");
    }

    @NewChain
    public SysDeptFetcher parentId() {
        return add("parentId");
    }

    @NewChain
    public SysDeptFetcher parentId(boolean enabled) {
        return enabled ? add("parentId") : remove("parentId");
    }

    @NewChain
    public SysDeptFetcher sort() {
        return add("sort");
    }

    @NewChain
    public SysDeptFetcher sort(boolean enabled) {
        return enabled ? add("sort") : remove("sort");
    }

    @NewChain
    public SysDeptFetcher parent() {
        return add("parent");
    }

    @NewChain
    public SysDeptFetcher parent(boolean enabled) {
        return enabled ? add("parent") : remove("parent");
    }

    @NewChain
    public SysDeptFetcher parent(Fetcher<SysDept> childFetcher) {
        return add("parent", childFetcher);
    }

    @NewChain
    public SysDeptFetcher parent(IdOnlyFetchType idOnlyFetchType) {
        return add("parent", idOnlyFetchType);
    }

    @NewChain
    public SysDeptFetcher parent(Fetcher<SysDept> childFetcher,
            Consumer<ReferenceFieldConfig<SysDept, SysDeptTable>> fieldConfig) {
        return add("parent", childFetcher, fieldConfig);
    }

    @NewChain
    public SysDeptFetcher parent(ReferenceFetchType fetchType, Fetcher<SysDept> childFetcher) {
        return parent(childFetcher, cfg -> cfg.fetchType(fetchType));
    }

    @NewChain
    public SysDeptFetcher recursiveParent() {
        return addRecursion("parent", null);
    }

    @NewChain
    public SysDeptFetcher recursiveParent(
            Consumer<RecursiveReferenceFieldConfig<SysDept, SysDeptTable>> fieldConfig) {
        return addRecursion("parent", fieldConfig);
    }

    @NewChain
    public SysDeptFetcher children() {
        return add("children");
    }

    @NewChain
    public SysDeptFetcher children(boolean enabled) {
        return enabled ? add("children") : remove("children");
    }

    @NewChain
    public SysDeptFetcher children(Fetcher<SysDept> childFetcher) {
        return add("children", childFetcher);
    }

    @NewChain
    public SysDeptFetcher children(Fetcher<SysDept> childFetcher,
            Consumer<ListFieldConfig<SysDept, SysDeptTable>> fieldConfig) {
        return add("children", childFetcher, fieldConfig);
    }

    @NewChain
    public SysDeptFetcher recursiveChildren() {
        return addRecursion("children", null);
    }

    @NewChain
    public SysDeptFetcher recursiveChildren(
            Consumer<RecursiveListFieldConfig<SysDept, SysDeptTable>> fieldConfig) {
        return addRecursion("children", fieldConfig);
    }

    @NewChain
    public SysDeptFetcher deptName() {
        return add("deptName");
    }

    @NewChain
    public SysDeptFetcher deptName(boolean enabled) {
        return enabled ? add("deptName") : remove("deptName");
    }

    @NewChain
    public SysDeptFetcher leader() {
        return add("leader");
    }

    @NewChain
    public SysDeptFetcher leader(boolean enabled) {
        return enabled ? add("leader") : remove("leader");
    }

    @NewChain
    public SysDeptFetcher phone() {
        return add("phone");
    }

    @NewChain
    public SysDeptFetcher phone(boolean enabled) {
        return enabled ? add("phone") : remove("phone");
    }

    @NewChain
    public SysDeptFetcher email() {
        return add("email");
    }

    @NewChain
    public SysDeptFetcher email(boolean enabled) {
        return enabled ? add("email") : remove("email");
    }

    @NewChain
    public SysDeptFetcher status() {
        return add("status");
    }

    @NewChain
    public SysDeptFetcher status(boolean enabled) {
        return enabled ? add("status") : remove("status");
    }

    @NewChain
    public SysDeptFetcher ancestors() {
        return add("ancestors");
    }

    @NewChain
    public SysDeptFetcher ancestors(boolean enabled) {
        return enabled ? add("ancestors") : remove("ancestors");
    }

    @Override
    protected SysDeptFetcher createFetcher(ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        return new SysDeptFetcher(this, prop, negative, idOnlyFetchType);
    }

    @Override
    protected SysDeptFetcher createFetcher(ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        return new SysDeptFetcher(this, prop, fieldConfig);
    }
}
