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
        type = SysMenu.class
)
public class SysMenuFetcher extends AbstractTypedFetcher<SysMenu, SysMenuFetcher> {
    public static final SysMenuFetcher $ = new SysMenuFetcher(null);

    private SysMenuFetcher(FetcherImpl<SysMenu> base) {
        super(SysMenu.class, base);
    }

    private SysMenuFetcher(SysMenuFetcher prev, ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        super(prev, prop, negative, idOnlyFetchType);
    }

    private SysMenuFetcher(SysMenuFetcher prev, ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        super(prev, prop, fieldConfig);
    }

    public static SysMenuFetcher $from(Fetcher<SysMenu> base) {
        return base instanceof SysMenuFetcher ? 
        	(SysMenuFetcher)base : 
        	new SysMenuFetcher((FetcherImpl<SysMenu>)base);
    }

    @NewChain
    public SysMenuFetcher createdTime() {
        return add("createdTime");
    }

    @NewChain
    public SysMenuFetcher createdTime(boolean enabled) {
        return enabled ? add("createdTime") : remove("createdTime");
    }

    @NewChain
    public SysMenuFetcher updatedTime() {
        return add("updatedTime");
    }

    @NewChain
    public SysMenuFetcher updatedTime(boolean enabled) {
        return enabled ? add("updatedTime") : remove("updatedTime");
    }

    @NewChain
    public SysMenuFetcher createdBy() {
        return add("createdBy");
    }

    @NewChain
    public SysMenuFetcher createdBy(boolean enabled) {
        return enabled ? add("createdBy") : remove("createdBy");
    }

    @NewChain
    public SysMenuFetcher updatedBy() {
        return add("updatedBy");
    }

    @NewChain
    public SysMenuFetcher updatedBy(boolean enabled) {
        return enabled ? add("updatedBy") : remove("updatedBy");
    }

    @NewChain
    public SysMenuFetcher parentId() {
        return add("parentId");
    }

    @NewChain
    public SysMenuFetcher parentId(boolean enabled) {
        return enabled ? add("parentId") : remove("parentId");
    }

    @NewChain
    public SysMenuFetcher sort() {
        return add("sort");
    }

    @NewChain
    public SysMenuFetcher sort(boolean enabled) {
        return enabled ? add("sort") : remove("sort");
    }

    @NewChain
    public SysMenuFetcher parent() {
        return add("parent");
    }

    @NewChain
    public SysMenuFetcher parent(boolean enabled) {
        return enabled ? add("parent") : remove("parent");
    }

    @NewChain
    public SysMenuFetcher parent(Fetcher<SysMenu> childFetcher) {
        return add("parent", childFetcher);
    }

    @NewChain
    public SysMenuFetcher parent(IdOnlyFetchType idOnlyFetchType) {
        return add("parent", idOnlyFetchType);
    }

    @NewChain
    public SysMenuFetcher parent(Fetcher<SysMenu> childFetcher,
            Consumer<ReferenceFieldConfig<SysMenu, SysMenuTable>> fieldConfig) {
        return add("parent", childFetcher, fieldConfig);
    }

    @NewChain
    public SysMenuFetcher parent(ReferenceFetchType fetchType, Fetcher<SysMenu> childFetcher) {
        return parent(childFetcher, cfg -> cfg.fetchType(fetchType));
    }

    @NewChain
    public SysMenuFetcher recursiveParent() {
        return addRecursion("parent", null);
    }

    @NewChain
    public SysMenuFetcher recursiveParent(
            Consumer<RecursiveReferenceFieldConfig<SysMenu, SysMenuTable>> fieldConfig) {
        return addRecursion("parent", fieldConfig);
    }

    @NewChain
    public SysMenuFetcher children() {
        return add("children");
    }

    @NewChain
    public SysMenuFetcher children(boolean enabled) {
        return enabled ? add("children") : remove("children");
    }

    @NewChain
    public SysMenuFetcher children(Fetcher<SysMenu> childFetcher) {
        return add("children", childFetcher);
    }

    @NewChain
    public SysMenuFetcher children(Fetcher<SysMenu> childFetcher,
            Consumer<ListFieldConfig<SysMenu, SysMenuTable>> fieldConfig) {
        return add("children", childFetcher, fieldConfig);
    }

    @NewChain
    public SysMenuFetcher recursiveChildren() {
        return addRecursion("children", null);
    }

    @NewChain
    public SysMenuFetcher recursiveChildren(
            Consumer<RecursiveListFieldConfig<SysMenu, SysMenuTable>> fieldConfig) {
        return addRecursion("children", fieldConfig);
    }

    @NewChain
    public SysMenuFetcher menuName() {
        return add("menuName");
    }

    @NewChain
    public SysMenuFetcher menuName(boolean enabled) {
        return enabled ? add("menuName") : remove("menuName");
    }

    @NewChain
    public SysMenuFetcher menuType() {
        return add("menuType");
    }

    @NewChain
    public SysMenuFetcher menuType(boolean enabled) {
        return enabled ? add("menuType") : remove("menuType");
    }

    @NewChain
    public SysMenuFetcher path() {
        return add("path");
    }

    @NewChain
    public SysMenuFetcher path(boolean enabled) {
        return enabled ? add("path") : remove("path");
    }

    @NewChain
    public SysMenuFetcher component() {
        return add("component");
    }

    @NewChain
    public SysMenuFetcher component(boolean enabled) {
        return enabled ? add("component") : remove("component");
    }

    @NewChain
    public SysMenuFetcher perms() {
        return add("perms");
    }

    @NewChain
    public SysMenuFetcher perms(boolean enabled) {
        return enabled ? add("perms") : remove("perms");
    }

    @NewChain
    public SysMenuFetcher icon() {
        return add("icon");
    }

    @NewChain
    public SysMenuFetcher icon(boolean enabled) {
        return enabled ? add("icon") : remove("icon");
    }

    @NewChain
    public SysMenuFetcher visible() {
        return add("visible");
    }

    @NewChain
    public SysMenuFetcher visible(boolean enabled) {
        return enabled ? add("visible") : remove("visible");
    }

    @NewChain
    public SysMenuFetcher status() {
        return add("status");
    }

    @NewChain
    public SysMenuFetcher status(boolean enabled) {
        return enabled ? add("status") : remove("status");
    }

    @NewChain
    public SysMenuFetcher roles() {
        return add("roles");
    }

    @NewChain
    public SysMenuFetcher roles(boolean enabled) {
        return enabled ? add("roles") : remove("roles");
    }

    @NewChain
    public SysMenuFetcher roles(Fetcher<SysRole> childFetcher) {
        return add("roles", childFetcher);
    }

    @NewChain
    public SysMenuFetcher roles(Fetcher<SysRole> childFetcher,
            Consumer<ListFieldConfig<SysRole, SysRoleTable>> fieldConfig) {
        return add("roles", childFetcher, fieldConfig);
    }

    @Override
    protected SysMenuFetcher createFetcher(ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        return new SysMenuFetcher(this, prop, negative, idOnlyFetchType);
    }

    @Override
    protected SysMenuFetcher createFetcher(ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        return new SysMenuFetcher(this, prop, fieldConfig);
    }
}
