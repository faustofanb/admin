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
        type = SysRole.class
)
public class SysRoleTableEx extends SysRoleTable implements TableEx<SysRole> {
    public static final SysRoleTableEx $ = new SysRoleTableEx(SysRoleTable.$, null);

    public SysRoleTableEx() {
        super();
    }

    public SysRoleTableEx(AbstractTypedTable.DelayedOperation<SysRole> delayedOperation) {
        super(delayedOperation);
    }

    public SysRoleTableEx(TableImplementor<SysRole> table) {
        super(table);
    }

    protected SysRoleTableEx(SysRoleTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    public SysMenuTableEx menus() {
        __beforeJoin();
        if (raw != null) {
            return new SysMenuTableEx(raw.joinImplementor(SysRoleProps.MENUS.unwrap()));
        }
        return new SysMenuTableEx(joinOperation(SysRoleProps.MENUS.unwrap()));
    }

    public SysMenuTableEx menus(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysMenuTableEx(raw.joinImplementor(SysRoleProps.MENUS.unwrap(), joinType));
        }
        return new SysMenuTableEx(joinOperation(SysRoleProps.MENUS.unwrap(), joinType));
    }

    @Override
    public Predicate menus(Function<SysMenuTableEx, Predicate> block) {
        return exists(SysRoleProps.MENUS.unwrap(), block);
    }

    public SysDeptTableEx depts() {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTableEx(raw.joinImplementor(SysRoleProps.DEPTS.unwrap()));
        }
        return new SysDeptTableEx(joinOperation(SysRoleProps.DEPTS.unwrap()));
    }

    public SysDeptTableEx depts(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTableEx(raw.joinImplementor(SysRoleProps.DEPTS.unwrap(), joinType));
        }
        return new SysDeptTableEx(joinOperation(SysRoleProps.DEPTS.unwrap(), joinType));
    }

    @Override
    public Predicate depts(Function<SysDeptTableEx, Predicate> block) {
        return exists(SysRoleProps.DEPTS.unwrap(), block);
    }

    public SysUserTableEx users() {
        __beforeJoin();
        if (raw != null) {
            return new SysUserTableEx(raw.joinImplementor(SysRoleProps.USERS.unwrap()));
        }
        return new SysUserTableEx(joinOperation(SysRoleProps.USERS.unwrap()));
    }

    public SysUserTableEx users(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysUserTableEx(raw.joinImplementor(SysRoleProps.USERS.unwrap(), joinType));
        }
        return new SysUserTableEx(joinOperation(SysRoleProps.USERS.unwrap(), joinType));
    }

    @Override
    public Predicate users(Function<SysUserTableEx, Predicate> block) {
        return exists(SysRoleProps.USERS.unwrap(), block);
    }

    @Override
    public SysRoleTableEx asTableEx() {
        return this;
    }

    @Override
    public SysRoleTableEx __disableJoin(String reason) {
        return new SysRoleTableEx(this, reason);
    }

    public <TT extends Table<?>, WJ extends WeakJoin<SysRoleTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType) {
        return weakJoin(weakJoinType, JoinType.INNER);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>, WJ extends WeakJoin<SysRoleTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType, JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(weakJoinType, joinType));
        }
        return (TT)TableProxies.fluent(joinOperation(weakJoinType, joinType));
    }

    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType,
            WeakJoin<SysRoleTable, TT> weakJoinLambda) {
        return weakJoin(targetTableType, JoinType.INNER, weakJoinLambda);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType, JoinType joinType,
            WeakJoin<SysRoleTable, TT> weakJoinLambda) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(targetTableType, joinType, weakJoinLambda));
        }
        return (TT)TableProxies.fluent(joinOperation(targetTableType, joinType, weakJoinLambda));
    }
}
