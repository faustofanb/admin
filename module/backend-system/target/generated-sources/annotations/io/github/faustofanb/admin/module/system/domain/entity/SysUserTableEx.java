package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.function.Function;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.sql.JoinType;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.impl.table.TableImplementor;
import org.babyfish.jimmer.sql.ast.impl.table.TableProxies;
import org.babyfish.jimmer.sql.ast.table.Table;
import org.babyfish.jimmer.sql.ast.table.TableEx;
import org.babyfish.jimmer.sql.ast.table.WeakJoin;
import org.babyfish.jimmer.sql.ast.table.spi.AbstractTypedTable;

@GeneratedBy(
        type = SysUser.class
)
public class SysUserTableEx extends SysUserTable implements TableEx<SysUser> {
    public static final SysUserTableEx $ = new SysUserTableEx(SysUserTable.$, null);

    public SysUserTableEx() {
        super();
    }

    public SysUserTableEx(AbstractTypedTable.DelayedOperation<SysUser> delayedOperation) {
        super(delayedOperation);
    }

    public SysUserTableEx(TableImplementor<SysUser> table) {
        super(table);
    }

    protected SysUserTableEx(SysUserTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    public SysDeptTableEx dept() {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTableEx(raw.joinImplementor(SysUserProps.DEPT.unwrap()));
        }
        return new SysDeptTableEx(joinOperation(SysUserProps.DEPT.unwrap()));
    }

    public SysDeptTableEx dept(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTableEx(raw.joinImplementor(SysUserProps.DEPT.unwrap(), joinType));
        }
        return new SysDeptTableEx(joinOperation(SysUserProps.DEPT.unwrap(), joinType));
    }

    public SysRoleTableEx roles() {
        __beforeJoin();
        if (raw != null) {
            return new SysRoleTableEx(raw.joinImplementor(SysUserProps.ROLES.unwrap()));
        }
        return new SysRoleTableEx(joinOperation(SysUserProps.ROLES.unwrap()));
    }

    public SysRoleTableEx roles(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysRoleTableEx(raw.joinImplementor(SysUserProps.ROLES.unwrap(), joinType));
        }
        return new SysRoleTableEx(joinOperation(SysUserProps.ROLES.unwrap(), joinType));
    }

    @Override
    public Predicate roles(Function<SysRoleTableEx, Predicate> block) {
        return exists(SysUserProps.ROLES.unwrap(), block);
    }

    public SysPostTableEx posts() {
        __beforeJoin();
        if (raw != null) {
            return new SysPostTableEx(raw.joinImplementor(SysUserProps.POSTS.unwrap()));
        }
        return new SysPostTableEx(joinOperation(SysUserProps.POSTS.unwrap()));
    }

    public SysPostTableEx posts(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysPostTableEx(raw.joinImplementor(SysUserProps.POSTS.unwrap(), joinType));
        }
        return new SysPostTableEx(joinOperation(SysUserProps.POSTS.unwrap(), joinType));
    }

    @Override
    public Predicate posts(Function<SysPostTableEx, Predicate> block) {
        return exists(SysUserProps.POSTS.unwrap(), block);
    }

    @Override
    public SysUserTableEx asTableEx() {
        return this;
    }

    @Override
    public SysUserTableEx __disableJoin(String reason) {
        return new SysUserTableEx(this, reason);
    }

    public <TT extends Table<?>, WJ extends WeakJoin<SysUserTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType) {
        return weakJoin(weakJoinType, JoinType.INNER);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>, WJ extends WeakJoin<SysUserTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType, JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(weakJoinType, joinType));
        }
        return (TT)TableProxies.fluent(joinOperation(weakJoinType, joinType));
    }

    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType,
            WeakJoin<SysUserTable, TT> weakJoinLambda) {
        return weakJoin(targetTableType, JoinType.INNER, weakJoinLambda);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType, JoinType joinType,
            WeakJoin<SysUserTable, TT> weakJoinLambda) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(targetTableType, joinType, weakJoinLambda));
        }
        return (TT)TableProxies.fluent(joinOperation(targetTableType, joinType, weakJoinLambda));
    }
}
