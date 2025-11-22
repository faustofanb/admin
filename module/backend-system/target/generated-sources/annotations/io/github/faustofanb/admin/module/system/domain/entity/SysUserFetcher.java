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
import org.babyfish.jimmer.sql.fetcher.ReferenceFetchType;
import org.babyfish.jimmer.sql.fetcher.ReferenceFieldConfig;
import org.babyfish.jimmer.sql.fetcher.impl.FetcherImpl;
import org.babyfish.jimmer.sql.fetcher.spi.AbstractTypedFetcher;

@GeneratedBy(
        type = SysUser.class
)
public class SysUserFetcher extends AbstractTypedFetcher<SysUser, SysUserFetcher> {
    public static final SysUserFetcher $ = new SysUserFetcher(null);

    private SysUserFetcher(FetcherImpl<SysUser> base) {
        super(SysUser.class, base);
    }

    private SysUserFetcher(SysUserFetcher prev, ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        super(prev, prop, negative, idOnlyFetchType);
    }

    private SysUserFetcher(SysUserFetcher prev, ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        super(prev, prop, fieldConfig);
    }

    public static SysUserFetcher $from(Fetcher<SysUser> base) {
        return base instanceof SysUserFetcher ? 
        	(SysUserFetcher)base : 
        	new SysUserFetcher((FetcherImpl<SysUser>)base);
    }

    @NewChain
    public SysUserFetcher createdTime() {
        return add("createdTime");
    }

    @NewChain
    public SysUserFetcher createdTime(boolean enabled) {
        return enabled ? add("createdTime") : remove("createdTime");
    }

    @NewChain
    public SysUserFetcher updatedTime() {
        return add("updatedTime");
    }

    @NewChain
    public SysUserFetcher updatedTime(boolean enabled) {
        return enabled ? add("updatedTime") : remove("updatedTime");
    }

    @NewChain
    public SysUserFetcher createdBy() {
        return add("createdBy");
    }

    @NewChain
    public SysUserFetcher createdBy(boolean enabled) {
        return enabled ? add("createdBy") : remove("createdBy");
    }

    @NewChain
    public SysUserFetcher updatedBy() {
        return add("updatedBy");
    }

    @NewChain
    public SysUserFetcher updatedBy(boolean enabled) {
        return enabled ? add("updatedBy") : remove("updatedBy");
    }

    @NewChain
    public SysUserFetcher userName() {
        return add("userName");
    }

    @NewChain
    public SysUserFetcher userName(boolean enabled) {
        return enabled ? add("userName") : remove("userName");
    }

    @NewChain
    public SysUserFetcher nickName() {
        return add("nickName");
    }

    @NewChain
    public SysUserFetcher nickName(boolean enabled) {
        return enabled ? add("nickName") : remove("nickName");
    }

    @NewChain
    public SysUserFetcher email() {
        return add("email");
    }

    @NewChain
    public SysUserFetcher email(boolean enabled) {
        return enabled ? add("email") : remove("email");
    }

    @NewChain
    public SysUserFetcher phonenumber() {
        return add("phonenumber");
    }

    @NewChain
    public SysUserFetcher phonenumber(boolean enabled) {
        return enabled ? add("phonenumber") : remove("phonenumber");
    }

    @NewChain
    public SysUserFetcher sex() {
        return add("sex");
    }

    @NewChain
    public SysUserFetcher sex(boolean enabled) {
        return enabled ? add("sex") : remove("sex");
    }

    @NewChain
    public SysUserFetcher avatar() {
        return add("avatar");
    }

    @NewChain
    public SysUserFetcher avatar(boolean enabled) {
        return enabled ? add("avatar") : remove("avatar");
    }

    @NewChain
    public SysUserFetcher password() {
        return add("password");
    }

    @NewChain
    public SysUserFetcher password(boolean enabled) {
        return enabled ? add("password") : remove("password");
    }

    @NewChain
    public SysUserFetcher status() {
        return add("status");
    }

    @NewChain
    public SysUserFetcher status(boolean enabled) {
        return enabled ? add("status") : remove("status");
    }

    @NewChain
    public SysUserFetcher loginIp() {
        return add("loginIp");
    }

    @NewChain
    public SysUserFetcher loginIp(boolean enabled) {
        return enabled ? add("loginIp") : remove("loginIp");
    }

    @NewChain
    public SysUserFetcher loginDate() {
        return add("loginDate");
    }

    @NewChain
    public SysUserFetcher loginDate(boolean enabled) {
        return enabled ? add("loginDate") : remove("loginDate");
    }

    @NewChain
    public SysUserFetcher dept() {
        return add("dept");
    }

    @NewChain
    public SysUserFetcher dept(boolean enabled) {
        return enabled ? add("dept") : remove("dept");
    }

    @NewChain
    public SysUserFetcher dept(Fetcher<SysDept> childFetcher) {
        return add("dept", childFetcher);
    }

    @NewChain
    public SysUserFetcher dept(IdOnlyFetchType idOnlyFetchType) {
        return add("dept", idOnlyFetchType);
    }

    @NewChain
    public SysUserFetcher dept(Fetcher<SysDept> childFetcher,
            Consumer<ReferenceFieldConfig<SysDept, SysDeptTable>> fieldConfig) {
        return add("dept", childFetcher, fieldConfig);
    }

    @NewChain
    public SysUserFetcher dept(ReferenceFetchType fetchType, Fetcher<SysDept> childFetcher) {
        return dept(childFetcher, cfg -> cfg.fetchType(fetchType));
    }

    @NewChain
    public SysUserFetcher roles() {
        return add("roles");
    }

    @NewChain
    public SysUserFetcher roles(boolean enabled) {
        return enabled ? add("roles") : remove("roles");
    }

    @NewChain
    public SysUserFetcher roles(Fetcher<SysRole> childFetcher) {
        return add("roles", childFetcher);
    }

    @NewChain
    public SysUserFetcher roles(Fetcher<SysRole> childFetcher,
            Consumer<ListFieldConfig<SysRole, SysRoleTable>> fieldConfig) {
        return add("roles", childFetcher, fieldConfig);
    }

    @NewChain
    public SysUserFetcher posts() {
        return add("posts");
    }

    @NewChain
    public SysUserFetcher posts(boolean enabled) {
        return enabled ? add("posts") : remove("posts");
    }

    @NewChain
    public SysUserFetcher posts(Fetcher<SysPost> childFetcher) {
        return add("posts", childFetcher);
    }

    @NewChain
    public SysUserFetcher posts(Fetcher<SysPost> childFetcher,
            Consumer<ListFieldConfig<SysPost, SysPostTable>> fieldConfig) {
        return add("posts", childFetcher, fieldConfig);
    }

    @NewChain
    public SysUserFetcher remark() {
        return add("remark");
    }

    @NewChain
    public SysUserFetcher remark(boolean enabled) {
        return enabled ? add("remark") : remove("remark");
    }

    @Override
    protected SysUserFetcher createFetcher(ImmutableProp prop, boolean negative,
            IdOnlyFetchType idOnlyFetchType) {
        return new SysUserFetcher(this, prop, negative, idOnlyFetchType);
    }

    @Override
    protected SysUserFetcher createFetcher(ImmutableProp prop,
            FieldConfig<?, ? extends Table<?>> fieldConfig) {
        return new SysUserFetcher(this, prop, fieldConfig);
    }
}
