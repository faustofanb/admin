package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.sql.JoinType;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.impl.table.TableImplementor;
import org.babyfish.jimmer.sql.ast.table.TableEx;
import org.babyfish.jimmer.sql.ast.table.spi.AbstractTypedTable;

@GeneratedBy(
        type = SysDictData.class
)
public class SysDictDataTable extends AbstractTypedTable<SysDictData> implements SysDictDataProps {
    public static final SysDictDataTable $ = new SysDictDataTable();

    public SysDictDataTable() {
        super(SysDictData.class);
    }

    public SysDictDataTable(AbstractTypedTable.DelayedOperation<SysDictData> delayedOperation) {
        super(SysDictData.class, delayedOperation);
    }

    public SysDictDataTable(TableImplementor<SysDictData> table) {
        super(table);
    }

    protected SysDictDataTable(SysDictDataTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public PropExpression.Num<Long> id() {
        return __get(SysDictDataProps.ID.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> createdTime() {
        return __get(SysDictDataProps.CREATED_TIME.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> updatedTime() {
        return __get(SysDictDataProps.UPDATED_TIME.unwrap());
    }

    @Override
    public PropExpression.Num<Long> createdBy() {
        return __get(SysDictDataProps.CREATED_BY.unwrap());
    }

    @Override
    public PropExpression.Num<Long> updatedBy() {
        return __get(SysDictDataProps.UPDATED_BY.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> sort() {
        return __get(SysDictDataProps.SORT.unwrap());
    }

    @Override
    public PropExpression.Str label() {
        return __get(SysDictDataProps.LABEL.unwrap());
    }

    @Override
    public PropExpression.Str value() {
        return __get(SysDictDataProps.VALUE.unwrap());
    }

    @Override
    public SysDictTypeTable dictType() {
        __beforeJoin();
        if (raw != null) {
            return new SysDictTypeTable(raw.joinImplementor(SysDictDataProps.DICT_TYPE.unwrap()));
        }
        return new SysDictTypeTable(joinOperation(SysDictDataProps.DICT_TYPE.unwrap()));
    }

    @Override
    public SysDictTypeTable dictType(JoinType joinType) {
        __beforeJoin();
        if (raw != null) {
            return new SysDictTypeTable(raw.joinImplementor(SysDictDataProps.DICT_TYPE.unwrap(), joinType));
        }
        return new SysDictTypeTable(joinOperation(SysDictDataProps.DICT_TYPE.unwrap(), joinType));
    }

    @Override
    public PropExpression.Num<Long> dictTypeId() {
        return __getAssociatedId(SysDictDataProps.DICT_TYPE.unwrap());
    }

    @Override
    public PropExpression.Str cssClass() {
        return __get(SysDictDataProps.CSS_CLASS.unwrap());
    }

    @Override
    public PropExpression.Str listClass() {
        return __get(SysDictDataProps.LIST_CLASS.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> status() {
        return __get(SysDictDataProps.STATUS.unwrap());
    }

    @Override
    public PropExpression.Str remark() {
        return __get(SysDictDataProps.REMARK.unwrap());
    }

    @Override
    public SysDictDataTableEx asTableEx() {
        return new SysDictDataTableEx(this, null);
    }

    @Override
    public SysDictDataTable __disableJoin(String reason) {
        return new SysDictDataTable(this, reason);
    }

    @GeneratedBy(
            type = SysDictData.class
    )
    public static class Remote extends AbstractTypedTable<SysDictData> {
        public Remote(AbstractTypedTable.DelayedOperation delayedOperation) {
            super(SysDictData.class, delayedOperation);
        }

        public Remote(TableImplementor<SysDictData> table) {
            super(table);
        }

        public PropExpression.Num<Long> id() {
            return __get(SysDictDataProps.ID.unwrap());
        }

        @Override
        @Deprecated
        public TableEx<SysDictData> asTableEx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Remote __disableJoin(String reason) {
            return this;
        }
    }
}
