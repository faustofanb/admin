package io.github.faustofanb.admin.module.system.domain.entity;

import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.time.LocalDateTime;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.sql.ast.PropExpression;
import org.babyfish.jimmer.sql.ast.impl.table.TableImplementor;
import org.babyfish.jimmer.sql.ast.table.TableEx;
import org.babyfish.jimmer.sql.ast.table.spi.AbstractTypedTable;

@GeneratedBy(
        type = SysDictType.class
)
public class SysDictTypeTable extends AbstractTypedTable<SysDictType> implements SysDictTypeProps {
    public static final SysDictTypeTable $ = new SysDictTypeTable();

    public SysDictTypeTable() {
        super(SysDictType.class);
    }

    public SysDictTypeTable(AbstractTypedTable.DelayedOperation<SysDictType> delayedOperation) {
        super(SysDictType.class, delayedOperation);
    }

    public SysDictTypeTable(TableImplementor<SysDictType> table) {
        super(table);
    }

    protected SysDictTypeTable(SysDictTypeTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public PropExpression.Num<Long> id() {
        return __get(SysDictTypeProps.ID.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> createdTime() {
        return __get(SysDictTypeProps.CREATED_TIME.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> updatedTime() {
        return __get(SysDictTypeProps.UPDATED_TIME.unwrap());
    }

    @Override
    public PropExpression.Num<Long> createdBy() {
        return __get(SysDictTypeProps.CREATED_BY.unwrap());
    }

    @Override
    public PropExpression.Num<Long> updatedBy() {
        return __get(SysDictTypeProps.UPDATED_BY.unwrap());
    }

    @Override
    public PropExpression.Str name() {
        return __get(SysDictTypeProps.NAME.unwrap());
    }

    @Override
    public PropExpression.Str type() {
        return __get(SysDictTypeProps.TYPE.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> status() {
        return __get(SysDictTypeProps.STATUS.unwrap());
    }

    @Override
    public PropExpression.Str remark() {
        return __get(SysDictTypeProps.REMARK.unwrap());
    }

    @Override
    public SysDictTypeTableEx asTableEx() {
        return new SysDictTypeTableEx(this, null);
    }

    @Override
    public SysDictTypeTable __disableJoin(String reason) {
        return new SysDictTypeTable(this, reason);
    }

    @GeneratedBy(
            type = SysDictType.class
    )
    public static class Remote extends AbstractTypedTable<SysDictType> {
        public Remote(AbstractTypedTable.DelayedOperation delayedOperation) {
            super(SysDictType.class, delayedOperation);
        }

        public Remote(TableImplementor<SysDictType> table) {
            super(table);
        }

        public PropExpression.Num<Long> id() {
            return __get(SysDictTypeProps.ID.unwrap());
        }

        @Override
        @Deprecated
        public TableEx<SysDictType> asTableEx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Remote __disableJoin(String reason) {
            return this;
        }
    }
}
