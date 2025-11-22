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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@GeneratedBy(
        type = SysConfig.class
)
public interface SysConfigDraft extends SysConfig, BaseEntityDraft {
    SysConfigDraft.Producer $ = Producer.INSTANCE;

    @OldChain
    SysConfigDraft setId(long id);

    @OldChain
    SysConfigDraft setCreatedTime(LocalDateTime createdTime);

    @OldChain
    SysConfigDraft setUpdatedTime(LocalDateTime updatedTime);

    @OldChain
    SysConfigDraft setCreatedBy(Long createdBy);

    @OldChain
    SysConfigDraft setUpdatedBy(Long updatedBy);

    @OldChain
    SysConfigDraft setConfigName(String configName);

    @OldChain
    SysConfigDraft setConfigKey(String configKey);

    @OldChain
    SysConfigDraft setConfigValue(String configValue);

    @OldChain
    SysConfigDraft setConfigType(int configType);

    @OldChain
    SysConfigDraft setRemark(String remark);

    @GeneratedBy(
            type = SysConfig.class
    )
    class Producer {
        static final Producer INSTANCE = new Producer();

        public static final int SLOT_ID = 0;

        public static final int SLOT_CREATED_TIME = 1;

        public static final int SLOT_UPDATED_TIME = 2;

        public static final int SLOT_CREATED_BY = 3;

        public static final int SLOT_UPDATED_BY = 4;

        public static final int SLOT_CONFIG_NAME = 5;

        public static final int SLOT_CONFIG_KEY = 6;

        public static final int SLOT_CONFIG_VALUE = 7;

        public static final int SLOT_CONFIG_TYPE = 8;

        public static final int SLOT_REMARK = 9;

        public static final ImmutableType TYPE = ImmutableType
            .newBuilder(
                "0.9.96",
                SysConfig.class,
                Collections.singleton(BaseEntityDraft.Producer.TYPE),
                (ctx, base) -> new DraftImpl(ctx, (SysConfig)base)
            )
            .redefine("id", SLOT_ID)
            .redefine("createdTime", SLOT_CREATED_TIME)
            .redefine("updatedTime", SLOT_UPDATED_TIME)
            .redefine("createdBy", SLOT_CREATED_BY)
            .redefine("updatedBy", SLOT_UPDATED_BY)
            .add(SLOT_CONFIG_NAME, "configName", ImmutablePropCategory.SCALAR, String.class, false)
            .key(SLOT_CONFIG_KEY, "configKey", String.class, false)
            .add(SLOT_CONFIG_VALUE, "configValue", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_CONFIG_TYPE, "configType", ImmutablePropCategory.SCALAR, int.class, false)
            .add(SLOT_REMARK, "remark", ImmutablePropCategory.SCALAR, String.class, true)
            .build();

        private Producer() {
        }

        public SysConfig produce(DraftConsumer<SysConfigDraft> block) {
            return (SysConfig)Internal.produce(TYPE, null, block);
        }

        public SysConfig produce(SysConfig base, DraftConsumer<SysConfigDraft> block) {
            return (SysConfig)Internal.produce(TYPE, base, block);
        }

        public SysConfig produce(boolean resolveImmediately, DraftConsumer<SysConfigDraft> block) {
            return (SysConfig)Internal.produce(TYPE, null, resolveImmediately, block);
        }

        public SysConfig produce(SysConfig base, boolean resolveImmediately,
                DraftConsumer<SysConfigDraft> block) {
            return (SysConfig)Internal.produce(TYPE, base, resolveImmediately, block);
        }

        /**
         * Class, not interface, for free-marker
         */
        @GeneratedBy(
                type = SysConfig.class
        )
        @JsonPropertyOrder({"dummyPropForJacksonError__", "id", "createdTime", "updatedTime", "createdBy", "updatedBy", "configName", "configKey", "configValue", "configType", "remark"})
        public abstract static class Implementor implements SysConfig, ImmutableSpi {
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
                    case SLOT_CONFIG_NAME:
                    		return configName();
                    case SLOT_CONFIG_KEY:
                    		return configKey();
                    case SLOT_CONFIG_VALUE:
                    		return configValue();
                    case SLOT_CONFIG_TYPE:
                    		return (Integer)configType();
                    case SLOT_REMARK:
                    		return remark();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + prop + "\"");
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
                    case "configName":
                    		return configName();
                    case "configKey":
                    		return configKey();
                    case "configValue":
                    		return configValue();
                    case "configType":
                    		return (Integer)configType();
                    case "remark":
                    		return remark();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + prop + "\"");
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

            public final String getConfigName() {
                return configName();
            }

            public final String getConfigKey() {
                return configKey();
            }

            public final String getConfigValue() {
                return configValue();
            }

            public final int getConfigType() {
                return configType();
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
                type = SysConfig.class
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

            String __configNameValue;

            String __configKeyValue;

            String __configValueValue;

            int __configTypeValue;

            boolean __configTypeLoaded = false;

            String __remarkValue;

            boolean __remarkLoaded = false;

            @Override
            @JsonIgnore
            public long id() {
                if (!__idLoaded) {
                    throw new UnloadedException(SysConfig.class, "id");
                }
                return __idValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime createdTime() {
                if (__createdTimeValue == null) {
                    throw new UnloadedException(SysConfig.class, "createdTime");
                }
                return __createdTimeValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime updatedTime() {
                if (__updatedTimeValue == null) {
                    throw new UnloadedException(SysConfig.class, "updatedTime");
                }
                return __updatedTimeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long createdBy() {
                if (!__createdByLoaded) {
                    throw new UnloadedException(SysConfig.class, "createdBy");
                }
                return __createdByValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long updatedBy() {
                if (!__updatedByLoaded) {
                    throw new UnloadedException(SysConfig.class, "updatedBy");
                }
                return __updatedByValue;
            }

            @Override
            @JsonIgnore
            @Description("参数名称")
            public String configName() {
                if (__configNameValue == null) {
                    throw new UnloadedException(SysConfig.class, "configName");
                }
                return __configNameValue;
            }

            @Override
            @JsonIgnore
            @Description("参数键名")
            public String configKey() {
                if (__configKeyValue == null) {
                    throw new UnloadedException(SysConfig.class, "configKey");
                }
                return __configKeyValue;
            }

            @Override
            @JsonIgnore
            @Description("参数键值")
            public String configValue() {
                if (__configValueValue == null) {
                    throw new UnloadedException(SysConfig.class, "configValue");
                }
                return __configValueValue;
            }

            @Override
            @JsonIgnore
            @Description("系统内置（Y是 N否）")
            public int configType() {
                if (!__configTypeLoaded) {
                    throw new UnloadedException(SysConfig.class, "configType");
                }
                return __configTypeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("备注")
            public String remark() {
                if (!__remarkLoaded) {
                    throw new UnloadedException(SysConfig.class, "remark");
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
                    case SLOT_CONFIG_NAME:
                    		return __configNameValue != null;
                    case SLOT_CONFIG_KEY:
                    		return __configKeyValue != null;
                    case SLOT_CONFIG_VALUE:
                    		return __configValueValue != null;
                    case SLOT_CONFIG_TYPE:
                    		return __configTypeLoaded;
                    case SLOT_REMARK:
                    		return __remarkLoaded;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + prop + "\"");
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
                    case "configName":
                    		return __configNameValue != null;
                    case "configKey":
                    		return __configKeyValue != null;
                    case "configValue":
                    		return __configValueValue != null;
                    case "configType":
                    		return __configTypeLoaded;
                    case "remark":
                    		return __remarkLoaded;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + prop + "\"");
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
                    case SLOT_CONFIG_NAME:
                    		return __visibility.visible(SLOT_CONFIG_NAME);
                    case SLOT_CONFIG_KEY:
                    		return __visibility.visible(SLOT_CONFIG_KEY);
                    case SLOT_CONFIG_VALUE:
                    		return __visibility.visible(SLOT_CONFIG_VALUE);
                    case SLOT_CONFIG_TYPE:
                    		return __visibility.visible(SLOT_CONFIG_TYPE);
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
                    case "configName":
                    		return __visibility.visible(SLOT_CONFIG_NAME);
                    case "configKey":
                    		return __visibility.visible(SLOT_CONFIG_KEY);
                    case "configValue":
                    		return __visibility.visible(SLOT_CONFIG_VALUE);
                    case "configType":
                    		return __visibility.visible(SLOT_CONFIG_TYPE);
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
                if (__configNameValue != null) {
                    hash = 31 * hash + __configNameValue.hashCode();
                }
                if (__configKeyValue != null) {
                    hash = 31 * hash + __configKeyValue.hashCode();
                }
                if (__configValueValue != null) {
                    hash = 31 * hash + __configValueValue.hashCode();
                }
                if (__configTypeLoaded) {
                    hash = 31 * hash + Integer.hashCode(__configTypeValue);
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
                if (__configNameValue != null) {
                    hash = 31 * hash + System.identityHashCode(__configNameValue);
                }
                if (__configKeyValue != null) {
                    hash = 31 * hash + System.identityHashCode(__configKeyValue);
                }
                if (__configValueValue != null) {
                    hash = 31 * hash + System.identityHashCode(__configValueValue);
                }
                if (__configTypeLoaded) {
                    hash = 31 * hash + Integer.hashCode(__configTypeValue);
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
                if (__isVisible(PropId.byIndex(SLOT_CONFIG_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_CONFIG_NAME))) {
                    return false;
                }
                boolean __configNameLoaded = __configNameValue != null;
                if (__configNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CONFIG_NAME))) {
                    return false;
                }
                if (__configNameLoaded && !Objects.equals(__configNameValue, __other.configName())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CONFIG_KEY)) != __other.__isVisible(PropId.byIndex(SLOT_CONFIG_KEY))) {
                    return false;
                }
                boolean __configKeyLoaded = __configKeyValue != null;
                if (__configKeyLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CONFIG_KEY))) {
                    return false;
                }
                if (__configKeyLoaded && !Objects.equals(__configKeyValue, __other.configKey())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CONFIG_VALUE)) != __other.__isVisible(PropId.byIndex(SLOT_CONFIG_VALUE))) {
                    return false;
                }
                boolean __configValueLoaded = __configValueValue != null;
                if (__configValueLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CONFIG_VALUE))) {
                    return false;
                }
                if (__configValueLoaded && !Objects.equals(__configValueValue, __other.configValue())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CONFIG_TYPE)) != __other.__isVisible(PropId.byIndex(SLOT_CONFIG_TYPE))) {
                    return false;
                }
                boolean __configTypeLoaded = this.__configTypeLoaded;
                if (__configTypeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CONFIG_TYPE))) {
                    return false;
                }
                if (__configTypeLoaded && __configTypeValue != __other.configType()) {
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
                if (__isVisible(PropId.byIndex(SLOT_CONFIG_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_CONFIG_NAME))) {
                    return false;
                }
                boolean __configNameLoaded = __configNameValue != null;
                if (__configNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CONFIG_NAME))) {
                    return false;
                }
                if (__configNameLoaded && __configNameValue != __other.configName()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CONFIG_KEY)) != __other.__isVisible(PropId.byIndex(SLOT_CONFIG_KEY))) {
                    return false;
                }
                boolean __configKeyLoaded = __configKeyValue != null;
                if (__configKeyLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CONFIG_KEY))) {
                    return false;
                }
                if (__configKeyLoaded && __configKeyValue != __other.configKey()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CONFIG_VALUE)) != __other.__isVisible(PropId.byIndex(SLOT_CONFIG_VALUE))) {
                    return false;
                }
                boolean __configValueLoaded = __configValueValue != null;
                if (__configValueLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CONFIG_VALUE))) {
                    return false;
                }
                if (__configValueLoaded && __configValueValue != __other.configValue()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_CONFIG_TYPE)) != __other.__isVisible(PropId.byIndex(SLOT_CONFIG_TYPE))) {
                    return false;
                }
                boolean __configTypeLoaded = this.__configTypeLoaded;
                if (__configTypeLoaded != __other.__isLoaded(PropId.byIndex(SLOT_CONFIG_TYPE))) {
                    return false;
                }
                if (__configTypeLoaded && __configTypeValue != __other.configType()) {
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
                type = SysConfig.class
        )
        private static class DraftImpl extends Implementor implements DraftSpi, SysConfigDraft {
            private DraftContext __ctx;

            private Impl __base;

            private Impl __modified;

            private boolean __resolving;

            private SysConfig __resolved;

            DraftImpl(DraftContext ctx, SysConfig base) {
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
            public SysConfigDraft setId(long id) {
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
            public SysConfigDraft setCreatedTime(LocalDateTime createdTime) {
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
            public SysConfigDraft setUpdatedTime(LocalDateTime updatedTime) {
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
            public SysConfigDraft setCreatedBy(Long createdBy) {
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
            public SysConfigDraft setUpdatedBy(Long updatedBy) {
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
            public String configName() {
                return (__modified!= null ? __modified : __base).configName();
            }

            @Override
            public SysConfigDraft setConfigName(String configName) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (configName == null) {
                    throw new IllegalArgumentException(
                        "'configName' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__configNameValue = configName;
                return this;
            }

            @Override
            @JsonIgnore
            public String configKey() {
                return (__modified!= null ? __modified : __base).configKey();
            }

            @Override
            public SysConfigDraft setConfigKey(String configKey) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (configKey == null) {
                    throw new IllegalArgumentException(
                        "'configKey' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__configKeyValue = configKey;
                return this;
            }

            @Override
            @JsonIgnore
            public String configValue() {
                return (__modified!= null ? __modified : __base).configValue();
            }

            @Override
            public SysConfigDraft setConfigValue(String configValue) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (configValue == null) {
                    throw new IllegalArgumentException(
                        "'configValue' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__configValueValue = configValue;
                return this;
            }

            @Override
            @JsonIgnore
            public int configType() {
                return (__modified!= null ? __modified : __base).configType();
            }

            @Override
            public SysConfigDraft setConfigType(int configType) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__configTypeValue = configType;
                __tmpModified.__configTypeLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String remark() {
                return (__modified!= null ? __modified : __base).remark();
            }

            @Override
            public SysConfigDraft setRemark(String remark) {
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
                    case SLOT_CONFIG_NAME:
                    		setConfigName((String)value);break;
                    case SLOT_CONFIG_KEY:
                    		setConfigKey((String)value);break;
                    case SLOT_CONFIG_VALUE:
                    		setConfigValue((String)value);break;
                    case SLOT_CONFIG_TYPE:
                    		if (value == null) throw new IllegalArgumentException("'configType' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setConfigType((Integer)value);
                            break;
                    case SLOT_REMARK:
                    		setRemark((String)value);break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + prop + "\"");
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
                    case "configName":
                    		setConfigName((String)value);break;
                    case "configKey":
                    		setConfigKey((String)value);break;
                    case "configValue":
                    		setConfigValue((String)value);break;
                    case "configType":
                    		if (value == null) throw new IllegalArgumentException("'configType' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setConfigType((Integer)value);
                            break;
                    case "remark":
                    		setRemark((String)value);break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + prop + "\"");
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
                    __modified().__visibility = __visibility = Visibility.of(10);
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
                    case SLOT_CONFIG_NAME:
                    		__visibility.show(SLOT_CONFIG_NAME, visible);break;
                    case SLOT_CONFIG_KEY:
                    		__visibility.show(SLOT_CONFIG_KEY, visible);break;
                    case SLOT_CONFIG_VALUE:
                    		__visibility.show(SLOT_CONFIG_VALUE, visible);break;
                    case SLOT_CONFIG_TYPE:
                    		__visibility.show(SLOT_CONFIG_TYPE, visible);break;
                    case SLOT_REMARK:
                    		__visibility.show(SLOT_REMARK, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + 
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
                    __modified().__visibility = __visibility = Visibility.of(10);
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
                    case "configName":
                    		__visibility.show(SLOT_CONFIG_NAME, visible);break;
                    case "configKey":
                    		__visibility.show(SLOT_CONFIG_KEY, visible);break;
                    case "configValue":
                    		__visibility.show(SLOT_CONFIG_VALUE, visible);break;
                    case "configType":
                    		__visibility.show(SLOT_CONFIG_TYPE, visible);break;
                    case "remark":
                    		__visibility.show(SLOT_REMARK, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + 
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
                    case SLOT_CONFIG_NAME:
                    		__modified().__configNameValue = null;break;
                    case SLOT_CONFIG_KEY:
                    		__modified().__configKeyValue = null;break;
                    case SLOT_CONFIG_VALUE:
                    		__modified().__configValueValue = null;break;
                    case SLOT_CONFIG_TYPE:
                    		__modified().__configTypeValue = 0;
                    __modified().__configTypeLoaded = false;break;
                    case SLOT_REMARK:
                    		__modified().__remarkValue = null;
                    __modified().__remarkLoaded = false;break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
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
                    case "configName":
                    		__modified().__configNameValue = null;break;
                    case "configKey":
                    		__modified().__configKeyValue = null;break;
                    case "configValue":
                    		__modified().__configValueValue = null;break;
                    case "configType":
                    		__modified().__configTypeValue = 0;
                    __modified().__configTypeLoaded = false;break;
                    case "remark":
                    		__modified().__remarkValue = null;
                    __modified().__remarkLoaded = false;break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysConfig\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
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
            type = SysConfig.class
    )
    class Builder {
        private final Producer.DraftImpl __draft;

        public Builder() {
            this(null);
        }

        public Builder(@Nullable SysConfig base) {
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

        public Builder configName(@NotNull String configName) {
            if (configName != null) {
                __draft.setConfigName(configName);
            }
            return this;
        }

        public Builder configKey(@NotNull String configKey) {
            if (configKey != null) {
                __draft.setConfigKey(configKey);
            }
            return this;
        }

        public Builder configValue(@NotNull String configValue) {
            if (configValue != null) {
                __draft.setConfigValue(configValue);
            }
            return this;
        }

        public Builder configType(@NotNull Integer configType) {
            if (configType != null) {
                __draft.setConfigType(configType);
            }
            return this;
        }

        public Builder remark(@Nullable String remark) {
            __draft.setRemark(remark);
            return this;
        }

        public SysConfig build() {
            return (SysConfig)__draft.__modified();
        }
    }
}
