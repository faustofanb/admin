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
        type = SysDictData.class
)
public class SysDictDataTableEx extends SysDictDataTable implements TableEx<SysDictData> {
    public static final SysDictDataTableEx $ = new SysDictDataTableEx(SysDictDataTable.$, null);

    public SysDictDataTableEx() {
        super();
    }

    public SysDictDataTableEx(AbstractTypedTable.DelayedOperation<SysDictData> delayedOperation) {
        super(delayedOperation);
    }

    public SysDictDataTableEx(TableImplementor<SysDictData> table) {
        super(table);
    }

    protected SysDictDataTableEx(SysDictDataTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    public SysDictTypeTableEx dictType() {
        __beforeJoin();
        if (raw != null) {
            return new SysDictTypeTableEx(raw.joinImplementor(SysDictDataProps.DICT_TYPE.unwrap()));
        }
        return new SysDictTypeTableEx(joinOperation(SysDictDataProps.DICT_TYPE.unwrap()));
    }

    public SysDictTypeTableEx dictType(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysDictTypeTableEx(raw.joinImplementor(SysDictDataProps.DICT_TYPE.unwrap(), joinType));
        }
        return new SysDictTypeTableEx(joinOperation(SysDictDataProps.DICT_TYPE.unwrap(), joinType));
    }

    @Override
    public SysDictDataTableEx asTableEx() {
        return this;
    }

    @Override
    public SysDictDataTableEx __disableJoin(String reason) {
        return new SysDictDataTableEx(this, reason);
    }

    public <TT extends Table<?>, WJ extends WeakJoin<SysDictDataTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType) {
        return weakJoin(weakJoinType, JoinType.INNER);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>, WJ extends WeakJoin<SysDictDataTable, TT>> TT weakJoin(
            Class<WJ> weakJoinType, JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(weakJoinType, joinType));
        }
        return (TT)TableProxies.fluent(joinOperation(weakJoinType, joinType));
    }

    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType,
            WeakJoin<SysDictDataTable, TT> weakJoinLambda) {
        return weakJoin(targetTableType, JoinType.INNER, weakJoinLambda);
    }

    @SuppressWarnings("all")
    public <TT extends Table<?>> TT weakJoin(Class<TT> targetTableType, JoinType joinType,
            WeakJoin<SysDictDataTable, TT> weakJoinLambda) {
        __beforeJoin();
        if (raw != null) {
            return (TT)TableProxies.wrap(raw.weakJoinImplementor(targetTableType, joinType, weakJoinLambda));
        }
        return (TT)TableProxies.fluent(joinOperation(targetTableType, joinType, weakJoinLambda));
    }
}
