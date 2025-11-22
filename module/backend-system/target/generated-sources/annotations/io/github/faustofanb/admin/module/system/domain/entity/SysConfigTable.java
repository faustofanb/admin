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
        type = SysConfig.class
)
public class SysConfigTable extends AbstractTypedTable<SysConfig> implements SysConfigProps {
    public static final SysConfigTable $ = new SysConfigTable();

    public SysConfigTable() {
        super(SysConfig.class);
    }

    public SysConfigTable(AbstractTypedTable.DelayedOperation<SysConfig> delayedOperation) {
        super(SysConfig.class, delayedOperation);
    }

    public SysConfigTable(TableImplementor<SysConfig> table) {
        super(table);
    }

    protected SysConfigTable(SysConfigTable base, String joinDisabledReason) {
        super(base, joinDisabledReason);
    }

    @Override
    public PropExpression.Num<Long> id() {
        return __get(SysConfigProps.ID.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> createdTime() {
        return __get(SysConfigProps.CREATED_TIME.unwrap());
    }

    @Override
    public PropExpression.Tp<LocalDateTime> updatedTime() {
        return __get(SysConfigProps.UPDATED_TIME.unwrap());
    }

    @Override
    public PropExpression.Num<Long> createdBy() {
        return __get(SysConfigProps.CREATED_BY.unwrap());
    }

    @Override
    public PropExpression.Num<Long> updatedBy() {
        return __get(SysConfigProps.UPDATED_BY.unwrap());
    }

    @Override
    public PropExpression.Str configName() {
        return __get(SysConfigProps.CONFIG_NAME.unwrap());
    }

    @Override
    public PropExpression.Str configKey() {
        return __get(SysConfigProps.CONFIG_KEY.unwrap());
    }

    @Override
    public PropExpression.Str configValue() {
        return __get(SysConfigProps.CONFIG_VALUE.unwrap());
    }

    @Override
    public PropExpression.Num<Integer> configType() {
        return __get(SysConfigProps.CONFIG_TYPE.unwrap());
    }

    @Override
    public PropExpression.Str remark() {
        return __get(SysConfigProps.REMARK.unwrap());
    }

    @Override
    public SysConfigTableEx asTableEx() {
        return new SysConfigTableEx(this, null);
    }

    @Override
    public SysConfigTable __disableJoin(String reason) {
        return new SysConfigTable(this, reason);
    }

    @GeneratedBy(
            type = SysConfig.class
    )
    public static class Remote extends AbstractTypedTable<SysConfig> {
        public Remote(AbstractTypedTable.DelayedOperation delayedOperation) {
            super(SysConfig.class, delayedOperation);
        }

        public Remote(TableImplementor<SysConfig> table) {
            super(table);
        }

        public PropExpression.Num<Long> id() {
            return __get(SysConfigProps.ID.unwrap());
        }

        @Override
        @Deprecated
        public TableEx<SysConfig> asTableEx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Remote __disableJoin(String reason) {
            return this;
        }
    }
}
