package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.sql.JoinType;
import org.babyfish.jimmer.sql.ast.impl.table.TableImplementor;
import org.babyfish.jimmer.sql.ast.impl.table.TableProxies;
import org.babyfish.jimmer.sql.ast.table.Table;
import org.babyfish.jimmer.sql.ast.table.TableEx;
import org.babyfish.jimmer.sql.ast.table.WeakJoin;
import org.babyfish.jimmer.sql.ast.table.spi.AbstractTypedTable;

@GeneratedBy(
        type = SysDictType.class
)
public class SysDictTypeTableEx extends SysDictTypeTable implements TableEx<SysDictType> {
    public static final SysDictTypeTableEx $ = new SysDictTypeTableEx(SysDictTypeTable.$, null);

    public SysDictTypeTableEx() {
        super();
    }

    public SysDictTypeTableEx(AbstractTypedTable.DelayedOperation<SysDictType> delayedOperation) {
        super(delayedOperation);
    }

    public SysDictTypeTableEx(TableImplementor<SysDictType> table) {
        super(table);
    }

    protected SysDictTypeTableEx(SysDictTypeTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public SysDictTypeTableEx asTableEx() {
        return this;
    }

    @Override
    public SysDictTypeTableEx __disableJoin(String reason) {
        return new SysDictTypeTableEx(this, reason);
    }

    public <TT extends Table<?>, WJ extends WeakJoin<SysDictTypeTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType) {
        return weakJoin(weakJoinType, JoinType.INNER);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>, WJ extends WeakJoin<SysDictTypeTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType, JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(weakJoinType, joinType));
        }
        return (TT)TableProxies.fluent(joinOperation(weakJoinType, joinType));
    }

    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType,
            WeakJoin<SysDictTypeTable, TT> weakJoinLambda) {
        return weakJoin(targetTableType, JoinType.INNER, weakJoinLambda);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType, JoinType joinType,
            WeakJoin<SysDictTypeTable, TT> weakJoinLambda) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(targetTableType, joinType, weakJoinLambda));
        }
        return (TT)TableProxies.fluent(joinOperation(targetTableType, joinType, weakJoinLambda));
    }
}
