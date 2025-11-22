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
        type = SysPost.class
)
public class SysPostTable extends AbstractTypedTable<SysPost> implements SysPostProps {
    public static final SysPostTable $ = new SysPostTable();

    public SysPostTable() {
        super(SysPost.class);
    }

    public SysPostTable(AbstractTypedTable.DelayedOperation<SysPost> delayedOperation) {
        super(SysPost.class, delayedOperation);
    }

    public SysPostTable(TableImplementor<SysPost> table) {
        super(table);
    }

    protected SysPostTable(SysPostTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public PropExpression.Num<Long> id() {
        return __get(SysPostProps.ID.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> createdTime() {
        return __get(SysPostProps.CREATED_TIME.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> updatedTime() {
        return __get(SysPostProps.UPDATED_TIME.unwrap());
    }

    @Override
    public PropExpression.Num<Long> createdBy() {
        return __get(SysPostProps.CREATED_BY.unwrap());
    }

    @Override
    public PropExpression.Num<Long> updatedBy() {
        return __get(SysPostProps.UPDATED_BY.unwrap());
    }

    @Override
    public PropExpression.Str postCode() {
        return __get(SysPostProps.POST_CODE.unwrap());
    }

    @Override
    public PropExpression.Str postName() {
        return __get(SysPostProps.POST_NAME.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> sort() {
        return __get(SysPostProps.SORT.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> status() {
        return __get(SysPostProps.STATUS.unwrap());
    }

    @Override
    public PropExpression.Str remark() {
        return __get(SysPostProps.REMARK.unwrap());
    }

    @Override
    public SysPostTableEx asTableEx() {
        return new SysPostTableEx(this, null);
    }

    @Override
    public SysPostTable __disableJoin(String reason) {
        return new SysPostTable(this, reason);
    }

    @GeneratedBy(
            type = SysPost.class
    )
    public static class Remote extends AbstractTypedTable<SysPost> {
        public Remote(AbstractTypedTable.DelayedOperation delayedOperation) {
            super(SysPost.class, delayedOperation);
        }

        public Remote(TableImplementor<SysPost> table) {
            super(table);
        }

        public PropExpression.Num<Long> id() {
            return __get(SysPostProps.ID.unwrap());
        }

        @Override
        @Deprecated
        public TableEx<SysPost> asTableEx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Remote __disableJoin(String reason) {
            return this;
        }
    }
}
