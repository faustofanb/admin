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
        type = SysMenu.class
)
public class SysMenuTableEx extends SysMenuTable implements TableEx<SysMenu> {
    public static final SysMenuTableEx $ = new SysMenuTableEx(SysMenuTable.$, null);

    public SysMenuTableEx() {
        super();
    }

    public SysMenuTableEx(AbstractTypedTable.DelayedOperation<SysMenu> delayedOperation) {
        super(delayedOperation);
    }

    public SysMenuTableEx(TableImplementor<SysMenu> table) {
        super(table);
    }

    protected SysMenuTableEx(SysMenuTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    public SysMenuTableEx parent() {
        __beforeJoin();
        if (raw != null) {
            return new SysMenuTableEx(raw.joinImplementor(SysMenuProps.PARENT.unwrap()));
        }
        return new SysMenuTableEx(joinOperation(SysMenuProps.PARENT.unwrap()));
    }

    public SysMenuTableEx parent(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysMenuTableEx(raw.joinImplementor(SysMenuProps.PARENT.unwrap(), joinType));
        }
        return new SysMenuTableEx(joinOperation(SysMenuProps.PARENT.unwrap(), joinType));
    }

    public SysMenuTableEx children() {
        __beforeJoin();
        if (raw != null) {
            return new SysMenuTableEx(raw.joinImplementor(SysMenuProps.CHILDREN.unwrap()));
        }
        return new SysMenuTableEx(joinOperation(SysMenuProps.CHILDREN.unwrap()));
    }

    public SysMenuTableEx children(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysMenuTableEx(raw.joinImplementor(SysMenuProps.CHILDREN.unwrap(), joinType));
        }
        return new SysMenuTableEx(joinOperation(SysMenuProps.CHILDREN.unwrap(), joinType));
    }

    @Override
    public Predicate children(Function<SysMenuTableEx, Predicate> block) {
        return exists(SysMenuProps.CHILDREN.unwrap(), block);
    }

    public SysRoleTableEx roles() {
        __beforeJoin();
        if (raw != null) {
            return new SysRoleTableEx(raw.joinImplementor(SysMenuProps.ROLES.unwrap()));
        }
        return new SysRoleTableEx(joinOperation(SysMenuProps.ROLES.unwrap()));
    }

    public SysRoleTableEx roles(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysRoleTableEx(raw.joinImplementor(SysMenuProps.ROLES.unwrap(), joinType));
        }
        return new SysRoleTableEx(joinOperation(SysMenuProps.ROLES.unwrap(), joinType));
    }

    @Override
    public Predicate roles(Function<SysRoleTableEx, Predicate> block) {
        return exists(SysMenuProps.ROLES.unwrap(), block);
    }

    @Override
    public SysMenuTableEx asTableEx() {
        return this;
    }

    @Override
    public SysMenuTableEx __disableJoin(String reason) {
        return new SysMenuTableEx(this, reason);
    }

    public <TT extends Table<?>, WJ extends WeakJoin<SysMenuTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType) {
        return weakJoin(weakJoinType, JoinType.INNER);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>, WJ extends WeakJoin<SysMenuTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType, JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(weakJoinType, joinType));
        }
        return (TT)TableProxies.fluent(joinOperation(weakJoinType, joinType));
    }

    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType,
            WeakJoin<SysMenuTable, TT> weakJoinLambda) {
        return weakJoin(targetTableType, JoinType.INNER, weakJoinLambda);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType, JoinType joinType,
            WeakJoin<SysMenuTable, TT> weakJoinLambda) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(targetTableType, joinType, weakJoinLambda));
        }
        return (TT)TableProxies.fluent(joinOperation(targetTableType, joinType, weakJoinLambda));
    }
}
