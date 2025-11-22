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
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl;
import org.babyfish.jimmer.sql.fetcher.spi.AbstractTypedFetcher;

@GeneratedBy(
        type = SysRole.class
)
public class SysRoleFetcher extends AbstractTypedFetcher<SysRole, SysRoleFetcher> {
    public static final SysRoleFetcher $ = new SysRoleFetcher(null);

    private SysRoleFetcher(FetcherImpl<SysRole> base) {
        super(SysRole.class, base);
    }

    private SysRoleFetcher(SysRoleFetcher prev, ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        super(prev, prop, negative, idOnlyFetchType);
    }

    private SysRoleFetcher(SysRoleFetcher prev, ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        super(prev, prop, fieldConfig);
    }

    public static SysRoleFetcher $from(Fetcher<SysRole> base) {
        return base instanceof SysRoleFetcher ? 
        	(SysRoleFetcher)base : 
        	new SysRoleFetcher((FetcherImpl<SysRole>)base);
    }

    @NewChain
    public SysRoleFetcher createdTime() {
        return add("createdTime");
    }

    @NewChain
    public SysRoleFetcher createdTime(boolean enabled) {
        return enabled ? add("createdTime") : remove("createdTime");
    }

    @NewChain
    public SysRoleFetcher updatedTime() {
        return add("updatedTime");
    }

    @NewChain
    public SysRoleFetcher updatedTime(boolean enabled) {
        return enabled ? add("updatedTime") : remove("updatedTime");
    }

    @NewChain
    public SysRoleFetcher createdBy() {
        return add("createdBy");
    }

    @NewChain
    public SysRoleFetcher createdBy(boolean enabled) {
        return enabled ? add("createdBy") : remove("createdBy");
    }

    @NewChain
    public SysRoleFetcher updatedBy() {
        return add("updatedBy");
    }

    @NewChain
    public SysRoleFetcher updatedBy(boolean enabled) {
        return enabled ? add("updatedBy") : remove("updatedBy");
    }

    @NewChain
    public SysRoleFetcher roleName() {
        return add("roleName");
    }

    @NewChain
    public SysRoleFetcher roleName(boolean enabled) {
        return enabled ? add("roleName") : remove("roleName");
    }

    @NewChain
    public SysRoleFetcher roleKey() {
        return add("roleKey");
    }

    @NewChain
    public SysRoleFetcher roleKey(boolean enabled) {
        return enabled ? add("roleKey") : remove("roleKey");
    }

    @NewChain
    public SysRoleFetcher roleSort() {
        return add("roleSort");
    }

    @NewChain
    public SysRoleFetcher roleSort(boolean enabled) {
        return enabled ? add("roleSort") : remove("roleSort");
    }

    @NewChain
    public SysRoleFetcher dataScope() {
        return add("dataScope");
    }

    @NewChain
    public SysRoleFetcher dataScope(boolean enabled) {
        return enabled ? add("dataScope") : remove("dataScope");
    }

    @NewChain
    public SysRoleFetcher status() {
        return add("status");
    }

    @NewChain
    public SysRoleFetcher status(boolean enabled) {
        return enabled ? add("status") : remove("status");
    }

    @NewChain
    public SysRoleFetcher remark() {
        return add("remark");
    }

    @NewChain
    public SysRoleFetcher remark(boolean enabled) {
        return enabled ? add("remark") : remove("remark");
    }

    @NewChain
    public SysRoleFetcher menus() {
        return add("menus");
    }

    @NewChain
    public SysRoleFetcher menus(boolean enabled) {
        return enabled ? add("menus") : remove("menus");
    }

    @NewChain
    public SysRoleFetcher menus(Fetcher<SysMenu> childFetcher) {
        return add("menus", childFetcher);
    }

    @NewChain
    public SysRoleFetcher menus(Fetcher<SysMenu> childFetcher,
            Consumer<ListFieldConfig<SysMenu, SysMenuTable>> fieldConfig) {
        return add("menus", childFetcher, fieldConfig);
    }

    @NewChain
    public SysRoleFetcher depts() {
        return add("depts");
    }

    @NewChain
    public SysRoleFetcher depts(boolean enabled) {
        return enabled ? add("depts") : remove("depts");
    }

    @NewChain
    public SysRoleFetcher depts(Fetcher<SysDept> childFetcher) {
        return add("depts", childFetcher);
    }

    @NewChain
    public SysRoleFetcher depts(Fetcher<SysDept> childFetcher,
            Consumer<ListFieldConfig<SysDept, SysDeptTable>> fieldConfig) {
        return add("depts", childFetcher, fieldConfig);
    }

    @NewChain
    public SysRoleFetcher users() {
        return add("users");
    }

    @NewChain
    public SysRoleFetcher users(boolean enabled) {
        return enabled ? add("users") : remove("users");
    }

    @NewChain
    public SysRoleFetcher users(Fetcher<SysUser> childFetcher) {
        return add("users", childFetcher);
    }

    @NewChain
    public SysRoleFetcher users(Fetcher<SysUser> childFetcher,
            Consumer<ListFieldConfig<SysUser, SysUserTable>> fieldConfig) {
        return add("users", childFetcher, fieldConfig);
    }

    @Override
    protected SysRoleFetcher createFetcher(ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        return new SysRoleFetcher(this, prop, negative, idOnlyFetchType);
    }

    @Override
    protected SysRoleFetcher createFetcher(ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        return new SysRoleFetcher(this, prop, fieldConfig);
    }
}
