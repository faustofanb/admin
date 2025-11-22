package io.github.faustofanb.admin.module.system.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.faustofanb.admin.core.domain.BaseEntityDraft;
import java.io.Serializable;
import java.lang.CloneNotSupportedException;
import java.lang.Cloneable;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.System;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import org.babyfish.jimmer.CircularReferenceException;
import org.babyfish.jimmer.DraftConsumer;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.UnloadedException;
import org.babyfish.jimmer.client.Description;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.jackson.ImmutableModuleRequiredException;
import org.babyfish.jimmer.lang.OldChain;
import org.babyfish.jimmer.meta.ImmutablePropCategory;
import org.babyfish.jimmer.meta.ImmutableType;
import org.babyfish.jimmer.meta.PropId;
import org.babyfish.jimmer.runtime.DraftContext;
import org.babyfish.jimmer.runtime.DraftSpi;
import org.babyfish.jimmer.runtime.ImmutableSpi;
import org.babyfish.jimmer.runtime.Internal;
import org.babyfish.jimmer.runtime.Visibility;
import org.babyfish.jimmer.sql.ManyToOne;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@GeneratedBy(
        type = SysDictData.class
)
public interface SysDictDataDraft extends SysDictData, BaseEntityDraft {
    SysDictDataDraft.Producer $ = Producer.INSTANCE;

    @OldChain
    SysDictDataDraft setId(long id);

    @OldChain
    SysDictDataDraft setCreatedTime(LocalDateTime createdTime);

    @OldChain
    SysDictDataDraft setUpdatedTime(LocalDateTime updatedTime);

    @OldChain
    SysDictDataDraft setCreatedBy(Long createdBy);

    @OldChain
    SysDictDataDraft setUpdatedBy(Long updatedBy);

    @OldChain
    SysDictDataDraft setSort(int sort);

    @OldChain
    SysDictDataDraft setLabel(String label);

    @OldChain
    SysDictDataDraft setValue(String value);

    SysDictTypeDraft dictType();

    SysDictTypeDraft dictType(boolean autoCreate);

    @OldChain
    SysDictDataDraft setDictType(SysDictType dictType);

    @JsonIgnore
    long dictTypeId();

    @OldChain
    SysDictDataDraft setDictTypeId(long dictTypeId);

    @OldChain
    SysDictDataDraft applyDictType(DraftConsumer<SysDictTypeDraft> block);

    @OldChain
    SysDictDataDraft applyDictType(SysDictType base, DraftConsumer<SysDictTypeDraft> block);

    @OldChain
    SysDictDataDraft setCssClass(String cssClass);

    @OldChain
    SysDictDataDraft setListClass(String listClass);

    @OldChain
    SysDictDataDraft setStatus(int status);

    @OldChain
    SysDictDataDraft setRemark(String remark);

    @GeneratedBy(
            type = SysDictData.class
    )
    class Producer {
        static final Producer INSTANCE = new Producer();

        public static final int SLOT_ID = 0;

        public static final int SLOT_CREATED_TIME = 1;

        public static final int SLOT_UPDATED_TIME = 2;

        public static final int SLOT_CREATED_BY = 3;

        public static final int SLOT_UPDATED_BY = 4;

        public static final int SLOT_SORT = 5;

        public static final int SLOT_LABEL = 6;

        public static final int SLOT_VALUE = 7;

        public static final int SLOT_DICT_TYPE = 8;

        public static final int SLOT_CSS_CLASS = 9;

        public static final int SLOT_LIST_CLASS = 10;

        public static final int SLOT_STATUS = 11;

        public static final int SLOT_REMARK = 12;

        public static final ImmutableType TYPE = ImmutableType
            .newBuilder(
                "0.9.96",
                SysDictData.class,
                Collections.singleton(BaseEntityDraft.Producer.TYPE),
                (ctx, base) -> new DraftImpl(ctx, (SysDictData)base)
            )
            .redefine("id", SLOT_ID)
            .redefine("createdTime", SLOT_CREATED_TIME)
            .redefine("updatedTime", SLOT_UPDATED_TIME)
            .redefine("createdBy", SLOT_CREATED_BY)
            .redefine("updatedBy", SLOT_UPDATED_BY)
            .add(SLOT_SORT, "sort", ImmutablePropCategory.SCALAR, int.class, false)
            .add(SLOT_LABEL, "label", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_VALUE, "value", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_DICT_TYPE, "dictType", ManyToOne.class, SysDictType.class, false)
            .add(SLOT_CSS_CLASS, "cssClass", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_LIST_CLASS, "listClass", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_STATUS, "status", ImmutablePropCategory.SCALAR, int.class, false)
            .add(SLOT_REMARK, "remark", ImmutablePropCategory.SCALAR, String.class, true)
            .build();

        private Producer() {
        }

        public SysDictData produce(DraftConsumer<SysDictDataDraft> block) {
            return (SysDictData)Internal.produce(TYPE, null, block);
        }

        public SysDictData produce(SysDictData base, DraftConsumer<SysDictDataDraft> block) {
            return (SysDictData)Internal.produce(TYPE, base, block);
        }

        public SysDictData produce(boolean resolveImmediately,
                DraftConsumer<SysDictDataDraft> block) {
            return (SysDictData)Internal.produce(TYPE, null, resolveImmediately, block);
        }

        public SysDictData produce(SysDictData base, boolean resolveImmediately,
                DraftConsumer<SysDictDataDraft> block) {
            return (SysDictData)Internal.produce(TYPE, base, resolveImmediately, block);
        }

        /**
         * Class, not interface, for free-marker
         */
        @GeneratedBy(
                type = SysDictData.class
        )
        @JsonPropertyOrder({"dummyPropForJacksonError__", "id", "createdTime", "updatedTime", "createdBy", "updatedBy", "sort", "label", "value", "dictType", "cssClass", "listClass", "status", "remark"})
        public abstract static class Implementor implements SysDictData, ImmutableSpi {
            @Override
            public final Object __get(PropId prop) {
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		return __get(prop.asName());
                    case SLOT_ID:
                    		return (Long)id();
                    case SLOT_CREATED_TIME:
                    		return createdTime();
                    case SLOT_UPDATED_TIME:
                    		return updatedTime();
                    case SLOT_CREATED_BY:
                    		return createdBy();
                    case SLOT_UPDATED_BY:
                    		return updatedBy();
                    case SLOT_SORT:
                    		return (Integer)sort();
                    case SLOT_LABEL:
                    		return label();
                    case SLOT_VALUE:
                    		return value();
                    case SLOT_DICT_TYPE:
                    		return dictType();
                    case SLOT_CSS_CLASS:
                    		return cssClass();
                    case SLOT_LIST_CLASS:
                    		return listClass();
                    case SLOT_STATUS:
                    		return (Integer)status();
                    case SLOT_REMARK:
                    		return remark();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + prop + "\"");
                }
            }

            @Override
            public final Object __get(String prop) {
                switch (prop) {
                    case "id":
                    		return (Long)id();
                    case "createdTime":
                    		return createdTime();
                    case "updatedTime":
                    		return updatedTime();
                    case "createdBy":
                    		return createdBy();
                    case "updatedBy":
                    		return updatedBy();
                    case "sort":
                    		return (Integer)sort();
                    case "label":
                    		return label();
                    case "value":
                    		return value();
                    case "dictType":
                    		return dictType();
                    case "cssClass":
                    		return cssClass();
                    case "listClass":
                    		return listClass();
                    case "status":
                    		return (Integer)status();
                    case "remark":
                    		return remark();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + prop + "\"");
                }
            }

            public final long getId() {
                return id();
            }

            public final LocalDateTime getCreatedTime() {
                return createdTime();
            }

            public final LocalDateTime getUpdatedTime() {
                return updatedTime();
            }

            public final Long getCreatedBy() {
                return createdBy();
            }

            public final Long getUpdatedBy() {
                return updatedBy();
            }

            public final int getSort() {
                return sort();
            }

            public final String getLabel() {
                return label();
            }

            public final String getValue() {
                return value();
            }

            public final SysDictType getDictType() {
                return dictType();
            }

            @Nullable
            public final String getCssClass() {
                return cssClass();
            }

            @Nullable
            public final String getListClass() {
                return listClass();
            }

            public final int getStatus() {
                return status();
            }

            @Nullable
            public final String getRemark() {
                return remark();
            }

            @Override
            public final ImmutableType __type() {
                return TYPE;
            }

            public final int getDummyPropForJacksonError__() {
                throw new ImmutableModuleRequiredException();
            }
        }

        @GeneratedBy(
                type = SysDictData.class
        )
        private static class Impl extends Implementor implements Cloneable, Serializable {
            private Visibility __visibility;

            long __idValue;

            boolean __idLoaded = false;

            LocalDateTime __createdTimeValue;

            LocalDateTime __updatedTimeValue;

            Long __createdByValue;

            boolean __createdByLoaded = false;

            Long __updatedByValue;

            boolean __updatedByLoaded = false;

            int __sortValue;

            boolean __sortLoaded = false;

            String __labelValue;

            String __valueValue;

            SysDictType __dictTypeValue;

            String __cssClassValue;

            boolean __cssClassLoaded = false;

            String __listClassValue;

            boolean __listClassLoaded = false;

            int __statusValue;

            boolean __statusLoaded = false;

            String __remarkValue;

            boolean __remarkLoaded = false;

            @Override
            @JsonIgnore
            public long id() {
                if (!__idLoaded) {
                    throw new UnloadedException(SysDictData.class, "id");
                }
                return __idValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime createdTime() {
                if (__createdTimeValue == null) {
                    throw new UnloadedException(SysDictData.class, "createdTime");
                }
                return __createdTimeValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime updatedTime() {
                if (__updatedTimeValue == null) {
                    throw new UnloadedException(SysDictData.class, "updatedTime");
                }
                return __updatedTimeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long createdBy() {
                if (!__createdByLoaded) {
                    throw new UnloadedException(SysDictData.class, "createdBy");
                }
                return __createdByValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long updatedBy() {
                if (!__updatedByLoaded) {
                    throw new UnloadedException(SysDictData.class, "updatedBy");
                }
                return __updatedByValue;
            }

            @Override
            @JsonIgnore
            @Description("字典排序")
            public int sort() {
                if (!__sortLoaded) {
                    throw new UnloadedException(SysDictData.class, "sort");
                }
                return __sortValue;
            }

            @Override
            @JsonIgnore
            @Description("字典标签")
            public String label() {
                if (__labelValue == null) {
                    throw new UnloadedException(SysDictData.class, "label");
                }
                return __labelValue;
            }

            @Override
            @JsonIgnore
            @Description("字典键值")
            public String value() {
                if (__valueValue == null) {
                    throw new UnloadedException(SysDictData.class, "value");
                }
                return __valueValue;
            }

            @Override
            @JsonIgnore
            @Description("字典类型")
            public SysDictType dictType() {
                if (__dictTypeValue == null) {
                    throw new UnloadedException(SysDictData.class, "dictType");
                }
                return __dictTypeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("样式属性（其他样式扩展）")
            public String cssClass() {
                if (!__cssClassLoaded) {
                    throw new UnloadedException(SysDictData.class, "cssClass");
                }
                return __cssClassValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("表格回显样式")
            public String listClass() {
                if (!__listClassLoaded) {
                    throw new UnloadedException(SysDictData.class, "listClass");
                }
                return __listClassValue;
            }

            @Override
            @JsonIgnore
            @Description("状态（0正常 1停用）")
            public int status() {
                if (!__statusLoaded) {
                    throw new UnloadedException(SysDictData.class, "status");
                }
                return __statusValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("备注")
            public String remark() {
                if (!__remarkLoaded) {
                    throw new UnloadedException(SysDictData.class, "remark");
                }
                return __remarkValue;
            }

            @Override
            public Impl clone() {
                try {
                    return (Impl)super.clone();
                } catch(CloneNotSupportedException ex) {
                    throw new AssertionError(ex);
                }
            }

            @Override
            public boolean __isLoaded(PropId prop) {
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		return __isLoaded(prop.asName());
                    case SLOT_ID:
                    		return __idLoaded;
                    case SLOT_CREATED_TIME:
                    		return __createdTimeValue != null;
                    case SLOT_UPDATED_TIME:
                    		return __updatedTimeValue != null;
                    case SLOT_CREATED_BY:
                    		return __createdByLoaded;
                    case SLOT_UPDATED_BY:
                    		return __updatedByLoaded;
                    case SLOT_SORT:
                    		return __sortLoaded;
                    case SLOT_LABEL:
                    		return __labelValue != null;
                    case SLOT_VALUE:
                    		return __valueValue != null;
                    case SLOT_DICT_TYPE:
                    		return __dictTypeValue != null;
                    case SLOT_CSS_CLASS:
                    		return __cssClassLoaded;
                    case SLOT_LIST_CLASS:
                    		return __listClassLoaded;
                    case SLOT_STATUS:
                    		return __statusLoaded;
                    case SLOT_REMARK:
                    		return __remarkLoaded;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + prop + "\"");
                }
            }

            @Override
            public boolean __isLoaded(String prop) {
                switch (prop) {
                    case "id":
                    		return __idLoaded;
                    case "createdTime":
                    		return __createdTimeValue != null;
                    case "updatedTime":
                    		return __updatedTimeValue != null;
                    case "createdBy":
                    		return __createdByLoaded;
                    case "updatedBy":
                    		return __updatedByLoaded;
                    case "sort":
                    		return __sortLoaded;
                    case "label":
                    		return __labelValue != null;
                    case "value":
                    		return __valueValue != null;
                    case "dictType":
                    		return __dictTypeValue != null;
                    case "cssClass":
                    		return __cssClassLoaded;
                    case "listClass":
                    		return __listClassLoaded;
                    case "status":
                    		return __statusLoaded;
                    case "remark":
                    		return __remarkLoaded;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + prop + "\"");
                }
            }

            @Override
            public boolean __isVisible(PropId prop) {
                if (__visibility == null) {
                    return true;
                }
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		return __isVisible(prop.asName());
                    case SLOT_ID:
                    		return __visibility.visible(SLOT_ID);
                    case SLOT_CREATED_TIME:
                    		return __visibility.visible(SLOT_CREATED_TIME);
                    case SLOT_UPDATED_TIME:
                    		return __visibility.visible(SLOT_UPDATED_TIME);
                    case SLOT_CREATED_BY:
                    		return __visibility.visible(SLOT_CREATED_BY);
                    case SLOT_UPDATED_BY:
                    		return __visibility.visible(SLOT_UPDATED_BY);
                    case SLOT_SORT:
                    		return __visibility.visible(SLOT_SORT);
                    case SLOT_LABEL:
                    		return __visibility.visible(SLOT_LABEL);
                    case SLOT_VALUE:
                    		return __visibility.visible(SLOT_VALUE);
                    case SLOT_DICT_TYPE:
                    		return __visibility.visible(SLOT_DICT_TYPE);
                    case SLOT_CSS_CLASS:
                    		return __visibility.visible(SLOT_CSS_CLASS);
                    case SLOT_LIST_CLASS:
                    		return __visibility.visible(SLOT_LIST_CLASS);
                    case SLOT_STATUS:
                    		return __visibility.visible(SLOT_STATUS);
                    case SLOT_REMARK:
                    		return __visibility.visible(SLOT_REMARK);
                    default: return true;
                }
            }

            @Override
            public boolean __isVisible(String prop) {
                if (__visibility == null) {
                    return true;
                }
                switch (prop) {
                    case "id":
                    		return __visibility.visible(SLOT_ID);
                    case "createdTime":
                    		return __visibility.visible(SLOT_CREATED_TIME);
                    case "updatedTime":
                    		return __visibility.visible(SLOT_UPDATED_TIME);
                    case "createdBy":
                    		return __visibility.visible(SLOT_CREATED_BY);
                    case "updatedBy":
                    		return __visibility.visible(SLOT_UPDATED_BY);
                    case "sort":
                    		return __visibility.visible(SLOT_SORT);
                    case "label":
                    		return __visibility.visible(SLOT_LABEL);
                    case "value":
                    		return __visibility.visible(SLOT_VALUE);
                    case "dictType":
                    		return __visibility.visible(SLOT_DICT_TYPE);
                    case "cssClass":
                    		return __visibility.visible(SLOT_CSS_CLASS);
                    case "listClass":
                    		return __visibility.visible(SLOT_LIST_CLASS);
                    case "status":
                    		return __visibility.visible(SLOT_STATUS);
                    case "remark":
                    		return __visibility.visible(SLOT_REMARK);
                    default: return true;
                }
            }

            @Override
            public int hashCode() {
                int hash = __visibility != null ? __visibility.hashCode() : 0;
                if (__idLoaded) {
                    hash = 31 * hash + Long.hashCode(__idValue);
                    // If entity-id is loaded, return directly
                    return hash;
                }
                if (__createdTimeValue != null) {
                    hash = 31 * hash + __createdTimeValue.hashCode();
                }
                if (__updatedTimeValue != null) {
                    hash = 31 * hash + __updatedTimeValue.hashCode();
                }
                if (__createdByLoaded && __createdByValue != null) {
                    hash = 31 * hash + __createdByValue.hashCode();
                }
                if (__updatedByLoaded && __updatedByValue != null) {
                    hash = 31 * hash + __updatedByValue.hashCode();
                }
                if (__sortLoaded) {
                    hash = 31 * hash + Integer.hashCode(__sortValue);
                }
                if (__labelValue != null) {
                    hash = 31 * hash + __labelValue.hashCode();
                }
                if (__valueValue != null) {
                    hash = 31 * hash + __valueValue.hashCode();
                }
                if (__dictTypeValue != null) {
                    hash = 31 * hash + __dictTypeValue.hashCode();
                }
                if (__cssClassLoaded && __cssClassValue != null) {
                    hash = 31 * hash + __cssClassValue.hashCode();
                }
                if (__listClassLoaded && __listClassValue != null) {
                    hash = 31 * hash + __listClassValue.hashCode();
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__remarkLoaded && __remarkValue != null) {
                    hash = 31 * hash + __remarkValue.hashCode();
                }
                return hash;
            }

            private int __shallowHashCode() {
                int hash = __visibility != null ? __visibility.hashCode() : 0;
                if (__idLoaded) {
                    hash = 31 * hash + Long.hashCode(__idValue);
                }
                if (__createdTimeValue != null) {
                    hash = 31 * hash + System.identityHashCode(__createdTimeValue);
                }
                if (__updatedTimeValue != null) {
                    hash = 31 * hash + System.identityHashCode(__updatedTimeValue);
                }
                if (__createdByLoaded) {
                    hash = 31 * hash + System.identityHashCode(__createdByValue);
                }
                if (__updatedByLoaded) {
                    hash = 31 * hash + System.identityHashCode(__updatedByValue);
                }
                if (__sortLoaded) {
                    hash = 31 * hash + Integer.hashCode(__sortValue);
                }
                if (__labelValue != null) {
                    hash = 31 * hash + System.identityHashCode(__labelValue);
                }
                if (__valueValue != null) {
                    hash = 31 * hash + System.identityHashCode(__valueValue);
                }
                if (__dictTypeValue != null) {
                    hash = 31 * hash + System.identityHashCode(__dictTypeValue);
                }
                if (__cssClassLoaded) {
                    hash = 31 * hash + System.identityHashCode(__cssClassValue);
                }
                if (__listClassLoaded) {
                    hash = 31 * hash + System.identityHashCode(__listClassValue);
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__remarkLoaded) {
                    hash = 31 * hash + System.identityHashCode(__remarkValue);
                }
                return hash;
            }

            @Override
            public int __hashCode(boolean shallow) {
                return shallow ? __shallowHashCode() : hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null || !(obj instanceof Implementor)) {
                    return false;
                }
                Implementor __other = (Implementor)obj;
                if (__isVisible(PropId.byIndex(SLOT_ID)) != __other.__isVisible(PropId.byIndex(SLOT_ID))) {
                    return false;
                }
                boolean __idLoaded = this.__idLoaded;
                if (__idLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ID))) {
                    return false;
                }
                if (__idLoaded) {
                    // If entity-id is loaded, return directly
                    return __idValue == __other.id();
                }
                if (__isVisible(PropId.byIndex(SLOT_CREATED_TIME)) != __other.__isVisible(PropId.byIndex(SLOT_CREATED_TIME))) {
                    return false;
                }
                boolean __createdTimeLoaded = __createdTimeValue != null;
                if (__createdTimeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CREATED_TIME))) {
                    return false;
                }
                if (__createdTimeLoaded && !Objects.equals(__createdTimeValue, __other.createdTime())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_UPDATED_TIME)) != __other.__isVisible(PropId.byIndex(SLOT_UPDATED_TIME))) {
                    return false;
                }
                boolean __updatedTimeLoaded = __updatedTimeValue != null;
                if (__updatedTimeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_UPDATED_TIME))) {
                    return false;
                }
                if (__updatedTimeLoaded && !Objects.equals(__updatedTimeValue, __other.updatedTime())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CREATED_BY)) != __other.__isVisible(PropId.byIndex(SLOT_CREATED_BY))) {
                    return false;
                }
                boolean __createdByLoaded = this.__createdByLoaded;
                if (__createdByLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CREATED_BY))) {
                    return false;
                }
                if (__createdByLoaded && !Objects.equals(__createdByValue, __other.createdBy())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_UPDATED_BY)) != __other.__isVisible(PropId.byIndex(SLOT_UPDATED_BY))) {
                    return false;
                }
                boolean __updatedByLoaded = this.__updatedByLoaded;
                if (__updatedByLoaded != __other.__isLoaded(PropId.byIndex(SLOT_UPDATED_BY))) {
                    return false;
                }
                if (__updatedByLoaded && !Objects.equals(__updatedByValue, __other.updatedBy())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_SORT)) != __other.__isVisible(PropId.byIndex(SLOT_SORT))) {
                    return false;
                }
                boolean __sortLoaded = this.__sortLoaded;
                if (__sortLoaded != __other.__isLoaded(PropId.byIndex(SLOT_SORT))) {
                    return false;
                }
                if (__sortLoaded && __sortValue != __other.sort()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_LABEL)) != __other.__isVisible(PropId.byIndex(SLOT_LABEL))) {
                    return false;
                }
                boolean __labelLoaded = __labelValue != null;
                if (__labelLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LABEL))) {
                    return false;
                }
                if (__labelLoaded && !Objects.equals(__labelValue, __other.label())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_VALUE)) != __other.__isVisible(PropId.byIndex(SLOT_VALUE))) {
                    return false;
                }
                boolean __valueLoaded = __valueValue != null;
                if (__valueLoaded != __other.__isLoaded(PropId.byIndex(SLOT_VALUE))) {
                    return false;
                }
                if (__valueLoaded && !Objects.equals(__valueValue, __other.value())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DICT_TYPE)) != __other.__isVisible(PropId.byIndex(SLOT_DICT_TYPE))) {
                    return false;
                }
                boolean __dictTypeLoaded = __dictTypeValue != null;
                if (__dictTypeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DICT_TYPE))) {
                    return false;
                }
                if (__dictTypeLoaded && !Objects.equals(__dictTypeValue, __other.dictType())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CSS_CLASS)) != __other.__isVisible(PropId.byIndex(SLOT_CSS_CLASS))) {
                    return false;
                }
                boolean __cssClassLoaded = this.__cssClassLoaded;
                if (__cssClassLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CSS_CLASS))) {
                    return false;
                }
                if (__cssClassLoaded && !Objects.equals(__cssClassValue, __other.cssClass())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_LIST_CLASS)) != __other.__isVisible(PropId.byIndex(SLOT_LIST_CLASS))) {
                    return false;
                }
                boolean __listClassLoaded = this.__listClassLoaded;
                if (__listClassLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LIST_CLASS))) {
                    return false;
                }
                if (__listClassLoaded && !Objects.equals(__listClassValue, __other.listClass())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_STATUS)) != __other.__isVisible(PropId.byIndex(SLOT_STATUS))) {
                    return false;
                }
                boolean __statusLoaded = this.__statusLoaded;
                if (__statusLoaded != __other.__isLoaded(PropId.byIndex(SLOT_STATUS))) {
                    return false;
                }
                if (__statusLoaded && __statusValue != __other.status()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_REMARK)) != __other.__isVisible(PropId.byIndex(SLOT_REMARK))) {
                    return false;
                }
                boolean __remarkLoaded = this.__remarkLoaded;
                if (__remarkLoaded != __other.__isLoaded(PropId.byIndex(SLOT_REMARK))) {
                    return false;
                }
                if (__remarkLoaded && !Objects.equals(__remarkValue, __other.remark())) {
                    return false;
                }
                return true;
            }

            private boolean __shallowEquals(Object obj) {
                if (obj == null || !(obj instanceof Implementor)) {
                    return false;
                }
                Implementor __other = (Implementor)obj;
                if (__isVisible(PropId.byIndex(SLOT_ID)) != __other.__isVisible(PropId.byIndex(SLOT_ID))) {
                    return false;
                }
                boolean __idLoaded = this.__idLoaded;
                if (__idLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ID))) {
                    return false;
                }
                if (__idLoaded && __idValue != __other.id()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CREATED_TIME)) != __other.__isVisible(PropId.byIndex(SLOT_CREATED_TIME))) {
                    return false;
                }
                boolean __createdTimeLoaded = __createdTimeValue != null;
                if (__createdTimeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CREATED_TIME))) {
                    return false;
                }
                if (__createdTimeLoaded && __createdTimeValue != __other.createdTime()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_UPDATED_TIME)) != __other.__isVisible(PropId.byIndex(SLOT_UPDATED_TIME))) {
                    return false;
                }
                boolean __updatedTimeLoaded = __updatedTimeValue != null;
                if (__updatedTimeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_UPDATED_TIME))) {
                    return false;
                }
                if (__updatedTimeLoaded && __updatedTimeValue != __other.updatedTime()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CREATED_BY)) != __other.__isVisible(PropId.byIndex(SLOT_CREATED_BY))) {
                    return false;
                }
                boolean __createdByLoaded = this.__createdByLoaded;
                if (__createdByLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CREATED_BY))) {
                    return false;
                }
                if (__createdByLoaded && __createdByValue != __other.createdBy()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_UPDATED_BY)) != __other.__isVisible(PropId.byIndex(SLOT_UPDATED_BY))) {
                    return false;
                }
                boolean __updatedByLoaded = this.__updatedByLoaded;
                if (__updatedByLoaded != __other.__isLoaded(PropId.byIndex(SLOT_UPDATED_BY))) {
                    return false;
                }
                if (__updatedByLoaded && __updatedByValue != __other.updatedBy()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_SORT)) != __other.__isVisible(PropId.byIndex(SLOT_SORT))) {
                    return false;
                }
                boolean __sortLoaded = this.__sortLoaded;
                if (__sortLoaded != __other.__isLoaded(PropId.byIndex(SLOT_SORT))) {
                    return false;
                }
                if (__sortLoaded && __sortValue != __other.sort()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_LABEL)) != __other.__isVisible(PropId.byIndex(SLOT_LABEL))) {
                    return false;
                }
                boolean __labelLoaded = __labelValue != null;
                if (__labelLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LABEL))) {
                    return false;
                }
                if (__labelLoaded && __labelValue != __other.label()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_VALUE)) != __other.__isVisible(PropId.byIndex(SLOT_VALUE))) {
                    return false;
                }
                boolean __valueLoaded = __valueValue != null;
                if (__valueLoaded != __other.__isLoaded(PropId.byIndex(SLOT_VALUE))) {
                    return false;
                }
                if (__valueLoaded && __valueValue != __other.value()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DICT_TYPE)) != __other.__isVisible(PropId.byIndex(SLOT_DICT_TYPE))) {
                    return false;
                }
                boolean __dictTypeLoaded = __dictTypeValue != null;
                if (__dictTypeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DICT_TYPE))) {
                    return false;
                }
                if (__dictTypeLoaded && __dictTypeValue != __other.dictType()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CSS_CLASS)) != __other.__isVisible(PropId.byIndex(SLOT_CSS_CLASS))) {
                    return false;
                }
                boolean __cssClassLoaded = this.__cssClassLoaded;
                if (__cssClassLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CSS_CLASS))) {
                    return false;
                }
                if (__cssClassLoaded && __cssClassValue != __other.cssClass()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_LIST_CLASS)) != __other.__isVisible(PropId.byIndex(SLOT_LIST_CLASS))) {
                    return false;
                }
                boolean __listClassLoaded = this.__listClassLoaded;
                if (__listClassLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LIST_CLASS))) {
                    return false;
                }
                if (__listClassLoaded && __listClassValue != __other.listClass()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_STATUS)) != __other.__isVisible(PropId.byIndex(SLOT_STATUS))) {
                    return false;
                }
                boolean __statusLoaded = this.__statusLoaded;
                if (__statusLoaded != __other.__isLoaded(PropId.byIndex(SLOT_STATUS))) {
                    return false;
                }
                if (__statusLoaded && __statusValue != __other.status()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_REMARK)) != __other.__isVisible(PropId.byIndex(SLOT_REMARK))) {
                    return false;
                }
                boolean __remarkLoaded = this.__remarkLoaded;
                if (__remarkLoaded != __other.__isLoaded(PropId.byIndex(SLOT_REMARK))) {
                    return false;
                }
                if (__remarkLoaded && __remarkValue != __other.remark()) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean __equals(Object obj, boolean shallow) {
                return shallow ? __shallowEquals(obj) : equals(obj);
            }

            @Override
            public String toString() {
                return ImmutableObjects.toString(this);
            }
        }

        @GeneratedBy(
                type = SysDictData.class
        )
        private static class DraftImpl extends Implementor implements DraftSpi, SysDictDataDraft {
            private DraftContext __ctx;

            private Impl __base;

            private Impl __modified;

            private boolean __resolving;

            private SysDictData __resolved;

            DraftImpl(DraftContext ctx, SysDictData base) {
                __ctx = ctx;
                if (base != null) {
                    __base = (Impl)base;
                }
                else {
                    __modified = new Impl();
                }
            }

            @Override
            public boolean __isLoaded(PropId prop) {
                return (__modified!= null ? __modified : __base).__isLoaded(prop);
            }

            @Override
            public boolean __isLoaded(String prop) {
                return (__modified!= null ? __modified : __base).__isLoaded(prop);
            }

            @Override
            public boolean __isVisible(PropId prop) {
                return (__modified!= null ? __modified : __base).__isVisible(prop);
            }

            @Override
            public boolean __isVisible(String prop) {
                return (__modified!= null ? __modified : __base).__isVisible(prop);
            }

            @Override
            public int hashCode() {
                return (__modified!= null ? __modified : __base).hashCode();
            }

            @Override
            public int __hashCode(boolean shallow) {
                return (__modified!= null ? __modified : __base).__hashCode(shallow);
            }

            @Override
            public boolean equals(Object obj) {
                return (__modified!= null ? __modified : __base).equals(obj);
            }

            @Override
            public boolean __equals(Object obj, boolean shallow) {
                return (__modified!= null ? __modified : __base).__equals(obj, shallow);
            }

            @Override
            public String toString() {
                return ImmutableObjects.toString(this);
            }

            @Override
            @JsonIgnore
            public long id() {
                return (__modified!= null ? __modified : __base).id();
            }

            @Override
            public SysDictDataDraft setId(long id) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__idValue = id;
                __tmpModified.__idLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public LocalDateTime createdTime() {
                return (__modified!= null ? __modified : __base).createdTime();
            }

            @Override
            public SysDictDataDraft setCreatedTime(LocalDateTime createdTime) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (createdTime == null) {
                    throw new IllegalArgumentException(
                        "'createdTime' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__createdTimeValue = createdTime;
                return this;
            }

            @Override
            @JsonIgnore
            public LocalDateTime updatedTime() {
                return (__modified!= null ? __modified : __base).updatedTime();
            }

            @Override
            public SysDictDataDraft setUpdatedTime(LocalDateTime updatedTime) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (updatedTime == null) {
                    throw new IllegalArgumentException(
                        "'updatedTime' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__updatedTimeValue = updatedTime;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long createdBy() {
                return (__modified!= null ? __modified : __base).createdBy();
            }

            @Override
            public SysDictDataDraft setCreatedBy(Long createdBy) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__createdByValue = createdBy;
                __tmpModified.__createdByLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long updatedBy() {
                return (__modified!= null ? __modified : __base).updatedBy();
            }

            @Override
            public SysDictDataDraft setUpdatedBy(Long updatedBy) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__updatedByValue = updatedBy;
                __tmpModified.__updatedByLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public int sort() {
                return (__modified!= null ? __modified : __base).sort();
            }

            @Override
            public SysDictDataDraft setSort(int sort) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__sortValue = sort;
                __tmpModified.__sortLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public String label() {
                return (__modified!= null ? __modified : __base).label();
            }

            @Override
            public SysDictDataDraft setLabel(String label) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (label == null) {
                    throw new IllegalArgumentException(
                        "'label' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__labelValue = label;
                return this;
            }

            @Override
            @JsonIgnore
            public String value() {
                return (__modified!= null ? __modified : __base).value();
            }

            @Override
            public SysDictDataDraft setValue(String value) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (value == null) {
                    throw new IllegalArgumentException(
                        "'value' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__valueValue = value;
                return this;
            }

            @Override
            @JsonIgnore
            public SysDictTypeDraft dictType() {
                return __ctx.toDraftObject((__modified!= null ? __modified : __base).dictType());
            }

            @Override
            public SysDictTypeDraft dictType(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_DICT_TYPE)))) {
                    setDictType(SysDictTypeDraft.$.produce(null, null));
                }
                return __ctx.toDraftObject((__modified!= null ? __modified : __base).dictType());
            }

            @Override
            public SysDictDataDraft setDictType(SysDictType dictType) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (dictType == null) {
                    throw new IllegalArgumentException(
                        "'dictType' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__dictTypeValue = dictType;
                return this;
            }

            @JsonIgnore
            @Override
            public long dictTypeId() {
                return dictType().id();
            }

            @OldChain
            @Override
            public SysDictDataDraft setDictTypeId(long dictTypeId) {
                dictType(true).setId(Objects.requireNonNull(dictTypeId, "\"dictType\" cannot be null"));
                return this;
            }

            @Override
            public SysDictDataDraft applyDictType(DraftConsumer<SysDictTypeDraft> block) {
                applyDictType(null, block);
                return this;
            }

            @Override
            public SysDictDataDraft applyDictType(SysDictType base,
                    DraftConsumer<SysDictTypeDraft> block) {
                setDictType(SysDictTypeDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String cssClass() {
                return (__modified!= null ? __modified : __base).cssClass();
            }

            @Override
            public SysDictDataDraft setCssClass(String cssClass) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__cssClassValue = cssClass;
                __tmpModified.__cssClassLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String listClass() {
                return (__modified!= null ? __modified : __base).listClass();
            }

            @Override
            public SysDictDataDraft setListClass(String listClass) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__listClassValue = listClass;
                __tmpModified.__listClassLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public int status() {
                return (__modified!= null ? __modified : __base).status();
            }

            @Override
            public SysDictDataDraft setStatus(int status) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__statusValue = status;
                __tmpModified.__statusLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String remark() {
                return (__modified!= null ? __modified : __base).remark();
            }

            @Override
            public SysDictDataDraft setRemark(String remark) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__remarkValue = remark;
                __tmpModified.__remarkLoaded = true;
                return this;
            }

            @SuppressWarnings("all")
            @Override
            public void __set(PropId prop, Object value) {
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		__set(prop.asName(), value);
                    return;
                    case SLOT_ID:
                    		if (value == null) throw new IllegalArgumentException("'id' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setId((Long)value);
                            break;
                    case SLOT_CREATED_TIME:
                    		setCreatedTime((LocalDateTime)value);break;
                    case SLOT_UPDATED_TIME:
                    		setUpdatedTime((LocalDateTime)value);break;
                    case SLOT_CREATED_BY:
                    		setCreatedBy((Long)value);break;
                    case SLOT_UPDATED_BY:
                    		setUpdatedBy((Long)value);break;
                    case SLOT_SORT:
                    		if (value == null) throw new IllegalArgumentException("'sort' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setSort((Integer)value);
                            break;
                    case SLOT_LABEL:
                    		setLabel((String)value);break;
                    case SLOT_VALUE:
                    		setValue((String)value);break;
                    case SLOT_DICT_TYPE:
                    		setDictType((SysDictType)value);break;
                    case SLOT_CSS_CLASS:
                    		setCssClass((String)value);break;
                    case SLOT_LIST_CLASS:
                    		setListClass((String)value);break;
                    case SLOT_STATUS:
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case SLOT_REMARK:
                    		setRemark((String)value);break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + prop + "\"");
                }
            }

            @SuppressWarnings("all")
            @Override
            public void __set(String prop, Object value) {
                switch (prop) {
                    case "id":
                    		if (value == null) throw new IllegalArgumentException("'id' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setId((Long)value);
                            break;
                    case "createdTime":
                    		setCreatedTime((LocalDateTime)value);break;
                    case "updatedTime":
                    		setUpdatedTime((LocalDateTime)value);break;
                    case "createdBy":
                    		setCreatedBy((Long)value);break;
                    case "updatedBy":
                    		setUpdatedBy((Long)value);break;
                    case "sort":
                    		if (value == null) throw new IllegalArgumentException("'sort' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setSort((Integer)value);
                            break;
                    case "label":
                    		setLabel((String)value);break;
                    case "value":
                    		setValue((String)value);break;
                    case "dictType":
                    		setDictType((SysDictType)value);break;
                    case "cssClass":
                    		setCssClass((String)value);break;
                    case "listClass":
                    		setListClass((String)value);break;
                    case "status":
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case "remark":
                    		setRemark((String)value);break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + prop + "\"");
                }
            }

            @Override
            public void __show(PropId prop, boolean visible) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Visibility __visibility = (__modified!= null ? __modified : __base).__visibility;
                if (__visibility == null) {
                    if (visible) {
                        return;
                    }
                    __modified().__visibility = __visibility = Visibility.of(13);
                }
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		__show(prop.asName(), visible);
                    return;
                    case SLOT_ID:
                    		__visibility.show(SLOT_ID, visible);break;
                    case SLOT_CREATED_TIME:
                    		__visibility.show(SLOT_CREATED_TIME, visible);break;
                    case SLOT_UPDATED_TIME:
                    		__visibility.show(SLOT_UPDATED_TIME, visible);break;
                    case SLOT_CREATED_BY:
                    		__visibility.show(SLOT_CREATED_BY, visible);break;
                    case SLOT_UPDATED_BY:
                    		__visibility.show(SLOT_UPDATED_BY, visible);break;
                    case SLOT_SORT:
                    		__visibility.show(SLOT_SORT, visible);break;
                    case SLOT_LABEL:
                    		__visibility.show(SLOT_LABEL, visible);break;
                    case SLOT_VALUE:
                    		__visibility.show(SLOT_VALUE, visible);break;
                    case SLOT_DICT_TYPE:
                    		__visibility.show(SLOT_DICT_TYPE, visible);break;
                    case SLOT_CSS_CLASS:
                    		__visibility.show(SLOT_CSS_CLASS, visible);break;
                    case SLOT_LIST_CLASS:
                    		__visibility.show(SLOT_LIST_CLASS, visible);break;
                    case SLOT_STATUS:
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case SLOT_REMARK:
                    		__visibility.show(SLOT_REMARK, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + 
                                prop + 
                                "\",it does not exists"
                            );
                }
            }

            @Override
            public void __show(String prop, boolean visible) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Visibility __visibility = (__modified!= null ? __modified : __base).__visibility;
                if (__visibility == null) {
                    if (visible) {
                        return;
                    }
                    __modified().__visibility = __visibility = Visibility.of(13);
                }
                switch (prop) {
                    case "id":
                    		__visibility.show(SLOT_ID, visible);break;
                    case "createdTime":
                    		__visibility.show(SLOT_CREATED_TIME, visible);break;
                    case "updatedTime":
                    		__visibility.show(SLOT_UPDATED_TIME, visible);break;
                    case "createdBy":
                    		__visibility.show(SLOT_CREATED_BY, visible);break;
                    case "updatedBy":
                    		__visibility.show(SLOT_UPDATED_BY, visible);break;
                    case "sort":
                    		__visibility.show(SLOT_SORT, visible);break;
                    case "label":
                    		__visibility.show(SLOT_LABEL, visible);break;
                    case "value":
                    		__visibility.show(SLOT_VALUE, visible);break;
                    case "dictType":
                    		__visibility.show(SLOT_DICT_TYPE, visible);break;
                    case "cssClass":
                    		__visibility.show(SLOT_CSS_CLASS, visible);break;
                    case "listClass":
                    		__visibility.show(SLOT_LIST_CLASS, visible);break;
                    case "status":
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case "remark":
                    		__visibility.show(SLOT_REMARK, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + 
                                prop + 
                                "\",it does not exists"
                            );
                }
            }

            @Override
            public void __unload(PropId prop) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                int __propIndex = prop.asIndex();
                switch (__propIndex) {
                    case -1:
                    		__unload(prop.asName());
                    return;
                    case SLOT_ID:
                    		__modified().__idValue = 0;
                    __modified().__idLoaded = false;break;
                    case SLOT_CREATED_TIME:
                    		__modified().__createdTimeValue = null;break;
                    case SLOT_UPDATED_TIME:
                    		__modified().__updatedTimeValue = null;break;
                    case SLOT_CREATED_BY:
                    		__modified().__createdByValue = null;
                    __modified().__createdByLoaded = false;break;
                    case SLOT_UPDATED_BY:
                    		__modified().__updatedByValue = null;
                    __modified().__updatedByLoaded = false;break;
                    case SLOT_SORT:
                    		__modified().__sortValue = 0;
                    __modified().__sortLoaded = false;break;
                    case SLOT_LABEL:
                    		__modified().__labelValue = null;break;
                    case SLOT_VALUE:
                    		__modified().__valueValue = null;break;
                    case SLOT_DICT_TYPE:
                    		__modified().__dictTypeValue = null;break;
                    case SLOT_CSS_CLASS:
                    		__modified().__cssClassValue = null;
                    __modified().__cssClassLoaded = false;break;
                    case SLOT_LIST_CLASS:
                    		__modified().__listClassValue = null;
                    __modified().__listClassLoaded = false;break;
                    case SLOT_STATUS:
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case SLOT_REMARK:
                    		__modified().__remarkValue = null;
                    __modified().__remarkLoaded = false;break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
                }
            }

            @Override
            public void __unload(String prop) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                switch (prop) {
                    case "id":
                    		__modified().__idValue = 0;
                    __modified().__idLoaded = false;break;
                    case "createdTime":
                    		__modified().__createdTimeValue = null;break;
                    case "updatedTime":
                    		__modified().__updatedTimeValue = null;break;
                    case "createdBy":
                    		__modified().__createdByValue = null;
                    __modified().__createdByLoaded = false;break;
                    case "updatedBy":
                    		__modified().__updatedByValue = null;
                    __modified().__updatedByLoaded = false;break;
                    case "sort":
                    		__modified().__sortValue = 0;
                    __modified().__sortLoaded = false;break;
                    case "label":
                    		__modified().__labelValue = null;break;
                    case "value":
                    		__modified().__valueValue = null;break;
                    case "dictType":
                    		__modified().__dictTypeValue = null;break;
                    case "cssClass":
                    		__modified().__cssClassValue = null;
                    __modified().__cssClassLoaded = false;break;
                    case "listClass":
                    		__modified().__listClassValue = null;
                    __modified().__listClassLoaded = false;break;
                    case "status":
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case "remark":
                    		__modified().__remarkValue = null;
                    __modified().__remarkLoaded = false;break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysDictData\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
                }
            }

            @Override
            public DraftContext __draftContext() {
                return __ctx;
            }

            @Override
            public Object __resolve() {
                if (__resolved != null) {
                    return __resolved;
                }
                if (__resolving) {
                    throw new CircularReferenceException();
                }
                __resolving = true;
                try {
                    Implementor base = __base;
                    Impl __tmpModified = __modified;
                    if (__tmpModified == null) {
                        if (base.__isLoaded(PropId.byIndex(SLOT_DICT_TYPE))) {
                            SysDictType oldValue = base.dictType();
                            SysDictType newValue = __ctx.resolveObject(oldValue);
                            if (oldValue != newValue) {
                                setDictType(newValue);
                            }
                        }
                        __tmpModified = __modified;
                    }
                    else {
                        __tmpModified.__dictTypeValue = __ctx.resolveObject(__tmpModified.__dictTypeValue);
                    }
                    if (__base != null && __tmpModified == null) {
                        this.__resolved = base;
                        return base;
                    }
                    this.__resolved = __tmpModified;
                    return __tmpModified;
                }
                finally {
                    __resolving = false;
                }
            }

            @Override
            public boolean __isResolved() {
                return __resolved != null;
            }

            Impl __modified() {
                Impl __tmpModified = __modified;
                if (__tmpModified == null) {
                    __tmpModified = __base.clone();
                    __modified = __tmpModified;
                }
                return __tmpModified;
            }
        }
    }

    @GeneratedBy(
            type = SysDictData.class
    )
    class Builder {
        private final Producer.DraftImpl __draft;

        public Builder() {
            this(null);
        }

        public Builder(@Nullable SysDictData base) {
            __draft = new Producer.DraftImpl(null, base);
        }

        public Builder id(@NotNull Long id) {
            if (id != null) {
                __draft.setId(id);
            }
            return this;
        }

        public Builder createdTime(@NotNull LocalDateTime createdTime) {
            if (createdTime != null) {
                __draft.setCreatedTime(createdTime);
            }
            return this;
        }

        public Builder updatedTime(@NotNull LocalDateTime updatedTime) {
            if (updatedTime != null) {
                __draft.setUpdatedTime(updatedTime);
            }
            return this;
        }

        public Builder createdBy(@Nullable Long createdBy) {
            __draft.setCreatedBy(createdBy);
            return this;
        }

        public Builder updatedBy(@Nullable Long updatedBy) {
            __draft.setUpdatedBy(updatedBy);
            return this;
        }

        public Builder sort(@NotNull Integer sort) {
            if (sort != null) {
                __draft.setSort(sort);
            }
            return this;
        }

        public Builder label(@NotNull String label) {
            if (label != null) {
                __draft.setLabel(label);
            }
            return this;
        }

        public Builder value(@NotNull String value) {
            if (value != null) {
                __draft.setValue(value);
            }
            return this;
        }

        public Builder dictType(@NotNull SysDictType dictType) {
            if (dictType != null) {
                __draft.setDictType(dictType);
            }
            return this;
        }

        public Builder cssClass(@Nullable String cssClass) {
            __draft.setCssClass(cssClass);
            return this;
        }

        public Builder listClass(@Nullable String listClass) {
            __draft.setListClass(listClass);
            return this;
        }

        public Builder status(@NotNull Integer status) {
            if (status != null) {
                __draft.setStatus(status);
            }
            return this;
        }

        public Builder remark(@Nullable String remark) {
            __draft.setRemark(remark);
            return this;
        }

        public SysDictData build() {
            return (SysDictData)__draft.__modified();
        }
    }
}
