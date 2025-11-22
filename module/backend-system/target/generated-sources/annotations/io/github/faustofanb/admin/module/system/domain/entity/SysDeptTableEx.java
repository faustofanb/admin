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
        type = SysDept.class
)
public class SysDeptTableEx extends SysDeptTable implements TableEx<SysDept> {
    public static final SysDeptTableEx $ = new SysDeptTableEx(SysDeptTable.$, null);

    public SysDeptTableEx() {
        super();
    }

    public SysDeptTableEx(AbstractTypedTable.DelayedOperation<SysDept> delayedOperation) {
        super(delayedOperation);
    }

    public SysDeptTableEx(TableImplementor<SysDept> table) {
        super(table);
    }

    protected SysDeptTableEx(SysDeptTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    public SysDeptTableEx parent() {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTableEx(raw.joinImplementor(SysDeptProps.PARENT.unwrap()));
        }
        return new SysDeptTableEx(joinOperation(SysDeptProps.PARENT.unwrap()));
    }

    public SysDeptTableEx parent(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTableEx(raw.joinImplementor(SysDeptProps.PARENT.unwrap(), joinType));
        }
        return new SysDeptTableEx(joinOperation(SysDeptProps.PARENT.unwrap(), joinType));
    }

    public SysDeptTableEx children() {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTableEx(raw.joinImplementor(SysDeptProps.CHILDREN.unwrap()));
        }
        return new SysDeptTableEx(joinOperation(SysDeptProps.CHILDREN.unwrap()));
    }

    public SysDeptTableEx children(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysDeptTableEx(raw.joinImplementor(SysDeptProps.CHILDREN.unwrap(), joinType));
        }
        return new SysDeptTableEx(joinOperation(SysDeptProps.CHILDREN.unwrap(), joinType));
    }

    @Override
    public Predicate children(Function<SysDeptTableEx, Predicate> block) {
        return exists(SysDeptProps.CHILDREN.unwrap(), block);
    }

    @Override
    public SysDeptTableEx asTableEx() {
        return this;
    }

    @Override
    public SysDeptTableEx __disableJoin(String reason) {
        return new SysDeptTableEx(this, reason);
    }

    public <TT extends Table<?>, WJ extends WeakJoin<SysDeptTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType) {
        return weakJoin(weakJoinType, JoinType.INNER);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>, WJ extends WeakJoin<SysDeptTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType, JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(weakJoinType, joinType));
        }
        return (TT)TableProxies.fluent(joinOperation(weakJoinType, joinType));
    }

    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType,
            WeakJoin<SysDeptTable, TT> weakJoinLambda) {
        return weakJoin(targetTableType, JoinType.INNER, weakJoinLambda);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType, JoinType joinType,
            WeakJoin<SysDeptTable, TT> weakJoinLambda) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(targetTableType, joinType, weakJoinLambda));
        }
        return (TT)TableProxies.fluent(joinOperation(targetTableType, joinType, weakJoinLambda));
    }
}
