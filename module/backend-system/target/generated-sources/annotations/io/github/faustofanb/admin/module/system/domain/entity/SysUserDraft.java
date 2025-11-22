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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import org.babyfish.jimmer.runtime.NonSharedList;
import org.babyfish.jimmer.runtime.Visibility;
import org.babyfish.jimmer.sql.ManyToMany;
import org.babyfish.jimmer.sql.ManyToOne;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@GeneratedBy(
        type = SysUser.class
)
public interface SysUserDraft extends SysUser, BaseEntityDraft {
    SysUserDraft.Producer $ = Producer.INSTANCE;

    @OldChain
    SysUserDraft setId(long id);

    @OldChain
    SysUserDraft setCreatedTime(LocalDateTime createdTime);

    @OldChain
    SysUserDraft setUpdatedTime(LocalDateTime updatedTime);

    @OldChain
    SysUserDraft setCreatedBy(Long createdBy);

    @OldChain
    SysUserDraft setUpdatedBy(Long updatedBy);

    @OldChain
    SysUserDraft setUserName(String userName);

    @OldChain
    SysUserDraft setNickName(String nickName);

    @OldChain
    SysUserDraft setEmail(String email);

    @OldChain
    SysUserDraft setPhonenumber(String phonenumber);

    @OldChain
    SysUserDraft setSex(String sex);

    @OldChain
    SysUserDraft setAvatar(String avatar);

    @OldChain
    SysUserDraft setPassword(String password);

    @OldChain
    SysUserDraft setStatus(int status);

    @OldChain
    SysUserDraft setLoginIp(String loginIp);

    @OldChain
    SysUserDraft setLoginDate(LocalDateTime loginDate);

    @Nullable
    SysDeptDraft dept();

    SysDeptDraft dept(boolean autoCreate);

    @OldChain
    SysUserDraft setDept(SysDept dept);

    @Nullable
    @JsonIgnore
    Long deptId();

    @OldChain
    SysUserDraft setDeptId(@Nullable Long deptId);

    @OldChain
    SysUserDraft applyDept(DraftConsumer<SysDeptDraft> block);

    @OldChain
    SysUserDraft applyDept(SysDept base, DraftConsumer<SysDeptDraft> block);

    List<SysRoleDraft> roles(boolean autoCreate);

    @OldChain
    SysUserDraft setRoles(List<SysRole> roles);

    @OldChain
    SysUserDraft addIntoRoles(DraftConsumer<SysRoleDraft> block);

    @OldChain
    SysUserDraft addIntoRoles(SysRole base, DraftConsumer<SysRoleDraft> block);

    List<SysPostDraft> posts(boolean autoCreate);

    @OldChain
    SysUserDraft setPosts(List<SysPost> posts);

    @OldChain
    SysUserDraft addIntoPosts(DraftConsumer<SysPostDraft> block);

    @OldChain
    SysUserDraft addIntoPosts(SysPost base, DraftConsumer<SysPostDraft> block);

    @OldChain
    SysUserDraft setRemark(String remark);

    @GeneratedBy(
            type = SysUser.class
    )
    class Producer {
        static final Producer INSTANCE = new Producer();

        public static final int SLOT_ID = 0;

        public static final int SLOT_CREATED_TIME = 1;

        public static final int SLOT_UPDATED_TIME = 2;

        public static final int SLOT_CREATED_BY = 3;

        public static final int SLOT_UPDATED_BY = 4;

        public static final int SLOT_USER_NAME = 5;

        public static final int SLOT_NICK_NAME = 6;

        public static final int SLOT_EMAIL = 7;

        public static final int SLOT_PHONENUMBER = 8;

        public static final int SLOT_SEX = 9;

        public static final int SLOT_AVATAR = 10;

        public static final int SLOT_PASSWORD = 11;

        public static final int SLOT_STATUS = 12;

        public static final int SLOT_LOGIN_IP = 13;

        public static final int SLOT_LOGIN_DATE = 14;

        public static final int SLOT_DEPT = 15;

        public static final int SLOT_ROLES = 16;

        public static final int SLOT_POSTS = 17;

        public static final int SLOT_REMARK = 18;

        public static final ImmutableType TYPE = ImmutableType
            .newBuilder(
                "0.9.96",
                SysUser.class,
                Collections.singleton(BaseEntityDraft.Producer.TYPE),
                (ctx, base) -> new DraftImpl(ctx, (SysUser)base)
            )
            .redefine("id", SLOT_ID)
            .redefine("createdTime", SLOT_CREATED_TIME)
            .redefine("updatedTime", SLOT_UPDATED_TIME)
            .redefine("createdBy", SLOT_CREATED_BY)
            .redefine("updatedBy", SLOT_UPDATED_BY)
            .add(SLOT_USER_NAME, "userName", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_NICK_NAME, "nickName", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_EMAIL, "email", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_PHONENUMBER, "phonenumber", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_SEX, "sex", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_AVATAR, "avatar", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_PASSWORD, "password", ImmutablePropCategory.SCALAR, String.class, false)
            .add(SLOT_STATUS, "status", ImmutablePropCategory.SCALAR, int.class, false)
            .add(SLOT_LOGIN_IP, "loginIp", ImmutablePropCategory.SCALAR, String.class, true)
            .add(SLOT_LOGIN_DATE, "loginDate", ImmutablePropCategory.SCALAR, LocalDateTime.class, true)
            .add(SLOT_DEPT, "dept", ManyToOne.class, SysDept.class, true)
            .add(SLOT_ROLES, "roles", ManyToMany.class, SysRole.class, false)
            .add(SLOT_POSTS, "posts", ManyToMany.class, SysPost.class, false)
            .add(SLOT_REMARK, "remark", ImmutablePropCategory.SCALAR, String.class, true)
            .build();

        private Producer() {
        }

        public SysUser produce(DraftConsumer<SysUserDraft> block) {
            return (SysUser)Internal.produce(TYPE, null, block);
        }

        public SysUser produce(SysUser base, DraftConsumer<SysUserDraft> block) {
            return (SysUser)Internal.produce(TYPE, base, block);
        }

        public SysUser produce(boolean resolveImmediately, DraftConsumer<SysUserDraft> block) {
            return (SysUser)Internal.produce(TYPE, null, resolveImmediately, block);
        }

        public SysUser produce(SysUser base, boolean resolveImmediately,
                DraftConsumer<SysUserDraft> block) {
            return (SysUser)Internal.produce(TYPE, base, resolveImmediately, block);
        }

        /**
         * Class, not interface, for free-marker
         */
        @GeneratedBy(
                type = SysUser.class
        )
        @JsonPropertyOrder({"dummyPropForJacksonError__", "id", "createdTime", "updatedTime", "createdBy", "updatedBy", "userName", "nickName", "email", "phonenumber", "sex", "avatar", "password", "status", "loginIp", "loginDate", "dept", "roles", "posts", "remark"})
        public abstract static class Implementor implements SysUser, ImmutableSpi {
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
                    case SLOT_USER_NAME:
                    		return userName();
                    case SLOT_NICK_NAME:
                    		return nickName();
                    case SLOT_EMAIL:
                    		return email();
                    case SLOT_PHONENUMBER:
                    		return phonenumber();
                    case SLOT_SEX:
                    		return sex();
                    case SLOT_AVATAR:
                    		return avatar();
                    case SLOT_PASSWORD:
                    		return password();
                    case SLOT_STATUS:
                    		return (Integer)status();
                    case SLOT_LOGIN_IP:
                    		return loginIp();
                    case SLOT_LOGIN_DATE:
                    		return loginDate();
                    case SLOT_DEPT:
                    		return dept();
                    case SLOT_ROLES:
                    		return roles();
                    case SLOT_POSTS:
                    		return posts();
                    case SLOT_REMARK:
                    		return remark();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + prop + "\"");
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
                    case "userName":
                    		return userName();
                    case "nickName":
                    		return nickName();
                    case "email":
                    		return email();
                    case "phonenumber":
                    		return phonenumber();
                    case "sex":
                    		return sex();
                    case "avatar":
                    		return avatar();
                    case "password":
                    		return password();
                    case "status":
                    		return (Integer)status();
                    case "loginIp":
                    		return loginIp();
                    case "loginDate":
                    		return loginDate();
                    case "dept":
                    		return dept();
                    case "roles":
                    		return roles();
                    case "posts":
                    		return posts();
                    case "remark":
                    		return remark();
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + prop + "\"");
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

            public final String getUserName() {
                return userName();
            }

            public final String getNickName() {
                return nickName();
            }

            @Nullable
            public final String getEmail() {
                return email();
            }

            @Nullable
            public final String getPhonenumber() {
                return phonenumber();
            }

            @Nullable
            public final String getSex() {
                return sex();
            }

            @Nullable
            public final String getAvatar() {
                return avatar();
            }

            public final String getPassword() {
                return password();
            }

            public final int getStatus() {
                return status();
            }

            @Nullable
            public final String getLoginIp() {
                return loginIp();
            }

            @Nullable
            public final LocalDateTime getLoginDate() {
                return loginDate();
            }

            @Nullable
            public final SysDept getDept() {
                return dept();
            }

            public final List<SysRole> getRoles() {
                return roles();
            }

            public final List<SysPost> getPosts() {
                return posts();
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
                type = SysUser.class
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

            String __userNameValue;

            String __nickNameValue;

            String __emailValue;

            boolean __emailLoaded = false;

            String __phonenumberValue;

            boolean __phonenumberLoaded = false;

            String __sexValue;

            boolean __sexLoaded = false;

            String __avatarValue;

            boolean __avatarLoaded = false;

            String __passwordValue;

            int __statusValue;

            boolean __statusLoaded = false;

            String __loginIpValue;

            boolean __loginIpLoaded = false;

            LocalDateTime __loginDateValue;

            boolean __loginDateLoaded = false;

            SysDept __deptValue;

            boolean __deptLoaded = false;

            NonSharedList<SysRole> __rolesValue;

            NonSharedList<SysPost> __postsValue;

            String __remarkValue;

            boolean __remarkLoaded = false;

            @Override
            @JsonIgnore
            public long id() {
                if (!__idLoaded) {
                    throw new UnloadedException(SysUser.class, "id");
                }
                return __idValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime createdTime() {
                if (__createdTimeValue == null) {
                    throw new UnloadedException(SysUser.class, "createdTime");
                }
                return __createdTimeValue;
            }

            @Override
            @JsonIgnore
            public LocalDateTime updatedTime() {
                if (__updatedTimeValue == null) {
                    throw new UnloadedException(SysUser.class, "updatedTime");
                }
                return __updatedTimeValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long createdBy() {
                if (!__createdByLoaded) {
                    throw new UnloadedException(SysUser.class, "createdBy");
                }
                return __createdByValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            public Long updatedBy() {
                if (!__updatedByLoaded) {
                    throw new UnloadedException(SysUser.class, "updatedBy");
                }
                return __updatedByValue;
            }

            @Override
            @JsonIgnore
            @Description("用户账号")
            public String userName() {
                if (__userNameValue == null) {
                    throw new UnloadedException(SysUser.class, "userName");
                }
                return __userNameValue;
            }

            @Override
            @JsonIgnore
            @Description("用户昵称")
            public String nickName() {
                if (__nickNameValue == null) {
                    throw new UnloadedException(SysUser.class, "nickName");
                }
                return __nickNameValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("用户邮箱")
            public String email() {
                if (!__emailLoaded) {
                    throw new UnloadedException(SysUser.class, "email");
                }
                return __emailValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("手机号码")
            public String phonenumber() {
                if (!__phonenumberLoaded) {
                    throw new UnloadedException(SysUser.class, "phonenumber");
                }
                return __phonenumberValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("用户性别（0男 1女 2未知）")
            public String sex() {
                if (!__sexLoaded) {
                    throw new UnloadedException(SysUser.class, "sex");
                }
                return __sexValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("头像地址")
            public String avatar() {
                if (!__avatarLoaded) {
                    throw new UnloadedException(SysUser.class, "avatar");
                }
                return __avatarValue;
            }

            @Override
            @JsonIgnore
            @Description("密码")
            public String password() {
                if (__passwordValue == null) {
                    throw new UnloadedException(SysUser.class, "password");
                }
                return __passwordValue;
            }

            @Override
            @JsonIgnore
            @Description("帐号状态（0正常 1停用）")
            public int status() {
                if (!__statusLoaded) {
                    throw new UnloadedException(SysUser.class, "status");
                }
                return __statusValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("最后登录IP")
            public String loginIp() {
                if (!__loginIpLoaded) {
                    throw new UnloadedException(SysUser.class, "loginIp");
                }
                return __loginIpValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("最后登录时间")
            public LocalDateTime loginDate() {
                if (!__loginDateLoaded) {
                    throw new UnloadedException(SysUser.class, "loginDate");
                }
                return __loginDateValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("部门")
            public SysDept dept() {
                if (!__deptLoaded) {
                    throw new UnloadedException(SysUser.class, "dept");
                }
                return __deptValue;
            }

            @Override
            @JsonIgnore
            @Description("角色")
            public List<SysRole> roles() {
                if (__rolesValue == null) {
                    throw new UnloadedException(SysUser.class, "roles");
                }
                return __rolesValue;
            }

            @Override
            @JsonIgnore
            @Description("岗位")
            public List<SysPost> posts() {
                if (__postsValue == null) {
                    throw new UnloadedException(SysUser.class, "posts");
                }
                return __postsValue;
            }

            @Override
            @JsonIgnore
            @Nullable
            @Description("备注")
            public String remark() {
                if (!__remarkLoaded) {
                    throw new UnloadedException(SysUser.class, "remark");
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
                    case SLOT_USER_NAME:
                    		return __userNameValue != null;
                    case SLOT_NICK_NAME:
                    		return __nickNameValue != null;
                    case SLOT_EMAIL:
                    		return __emailLoaded;
                    case SLOT_PHONENUMBER:
                    		return __phonenumberLoaded;
                    case SLOT_SEX:
                    		return __sexLoaded;
                    case SLOT_AVATAR:
                    		return __avatarLoaded;
                    case SLOT_PASSWORD:
                    		return __passwordValue != null;
                    case SLOT_STATUS:
                    		return __statusLoaded;
                    case SLOT_LOGIN_IP:
                    		return __loginIpLoaded;
                    case SLOT_LOGIN_DATE:
                    		return __loginDateLoaded;
                    case SLOT_DEPT:
                    		return __deptLoaded;
                    case SLOT_ROLES:
                    		return __rolesValue != null;
                    case SLOT_POSTS:
                    		return __postsValue != null;
                    case SLOT_REMARK:
                    		return __remarkLoaded;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + prop + "\"");
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
                    case "userName":
                    		return __userNameValue != null;
                    case "nickName":
                    		return __nickNameValue != null;
                    case "email":
                    		return __emailLoaded;
                    case "phonenumber":
                    		return __phonenumberLoaded;
                    case "sex":
                    		return __sexLoaded;
                    case "avatar":
                    		return __avatarLoaded;
                    case "password":
                    		return __passwordValue != null;
                    case "status":
                    		return __statusLoaded;
                    case "loginIp":
                    		return __loginIpLoaded;
                    case "loginDate":
                    		return __loginDateLoaded;
                    case "dept":
                    		return __deptLoaded;
                    case "roles":
                    		return __rolesValue != null;
                    case "posts":
                    		return __postsValue != null;
                    case "remark":
                    		return __remarkLoaded;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + prop + "\"");
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
                    case SLOT_USER_NAME:
                    		return __visibility.visible(SLOT_USER_NAME);
                    case SLOT_NICK_NAME:
                    		return __visibility.visible(SLOT_NICK_NAME);
                    case SLOT_EMAIL:
                    		return __visibility.visible(SLOT_EMAIL);
                    case SLOT_PHONENUMBER:
                    		return __visibility.visible(SLOT_PHONENUMBER);
                    case SLOT_SEX:
                    		return __visibility.visible(SLOT_SEX);
                    case SLOT_AVATAR:
                    		return __visibility.visible(SLOT_AVATAR);
                    case SLOT_PASSWORD:
                    		return __visibility.visible(SLOT_PASSWORD);
                    case SLOT_STATUS:
                    		return __visibility.visible(SLOT_STATUS);
                    case SLOT_LOGIN_IP:
                    		return __visibility.visible(SLOT_LOGIN_IP);
                    case SLOT_LOGIN_DATE:
                    		return __visibility.visible(SLOT_LOGIN_DATE);
                    case SLOT_DEPT:
                    		return __visibility.visible(SLOT_DEPT);
                    case SLOT_ROLES:
                    		return __visibility.visible(SLOT_ROLES);
                    case SLOT_POSTS:
                    		return __visibility.visible(SLOT_POSTS);
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
                    case "userName":
                    		return __visibility.visible(SLOT_USER_NAME);
                    case "nickName":
                    		return __visibility.visible(SLOT_NICK_NAME);
                    case "email":
                    		return __visibility.visible(SLOT_EMAIL);
                    case "phonenumber":
                    		return __visibility.visible(SLOT_PHONENUMBER);
                    case "sex":
                    		return __visibility.visible(SLOT_SEX);
                    case "avatar":
                    		return __visibility.visible(SLOT_AVATAR);
                    case "password":
                    		return __visibility.visible(SLOT_PASSWORD);
                    case "status":
                    		return __visibility.visible(SLOT_STATUS);
                    case "loginIp":
                    		return __visibility.visible(SLOT_LOGIN_IP);
                    case "loginDate":
                    		return __visibility.visible(SLOT_LOGIN_DATE);
                    case "dept":
                    		return __visibility.visible(SLOT_DEPT);
                    case "roles":
                    		return __visibility.visible(SLOT_ROLES);
                    case "posts":
                    		return __visibility.visible(SLOT_POSTS);
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
                if (__userNameValue != null) {
                    hash = 31 * hash + __userNameValue.hashCode();
                }
                if (__nickNameValue != null) {
                    hash = 31 * hash + __nickNameValue.hashCode();
                }
                if (__emailLoaded && __emailValue != null) {
                    hash = 31 * hash + __emailValue.hashCode();
                }
                if (__phonenumberLoaded && __phonenumberValue != null) {
                    hash = 31 * hash + __phonenumberValue.hashCode();
                }
                if (__sexLoaded && __sexValue != null) {
                    hash = 31 * hash + __sexValue.hashCode();
                }
                if (__avatarLoaded && __avatarValue != null) {
                    hash = 31 * hash + __avatarValue.hashCode();
                }
                if (__passwordValue != null) {
                    hash = 31 * hash + __passwordValue.hashCode();
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__loginIpLoaded && __loginIpValue != null) {
                    hash = 31 * hash + __loginIpValue.hashCode();
                }
                if (__loginDateLoaded && __loginDateValue != null) {
                    hash = 31 * hash + __loginDateValue.hashCode();
                }
                if (__deptLoaded && __deptValue != null) {
                    hash = 31 * hash + __deptValue.hashCode();
                }
                if (__rolesValue != null) {
                    hash = 31 * hash + __rolesValue.hashCode();
                }
                if (__postsValue != null) {
                    hash = 31 * hash + __postsValue.hashCode();
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
                if (__userNameValue != null) {
                    hash = 31 * hash + System.identityHashCode(__userNameValue);
                }
                if (__nickNameValue != null) {
                    hash = 31 * hash + System.identityHashCode(__nickNameValue);
                }
                if (__emailLoaded) {
                    hash = 31 * hash + System.identityHashCode(__emailValue);
                }
                if (__phonenumberLoaded) {
                    hash = 31 * hash + System.identityHashCode(__phonenumberValue);
                }
                if (__sexLoaded) {
                    hash = 31 * hash + System.identityHashCode(__sexValue);
                }
                if (__avatarLoaded) {
                    hash = 31 * hash + System.identityHashCode(__avatarValue);
                }
                if (__passwordValue != null) {
                    hash = 31 * hash + System.identityHashCode(__passwordValue);
                }
                if (__statusLoaded) {
                    hash = 31 * hash + Integer.hashCode(__statusValue);
                }
                if (__loginIpLoaded) {
                    hash = 31 * hash + System.identityHashCode(__loginIpValue);
                }
                if (__loginDateLoaded) {
                    hash = 31 * hash + System.identityHashCode(__loginDateValue);
                }
                if (__deptLoaded) {
                    hash = 31 * hash + System.identityHashCode(__deptValue);
                }
                if (__rolesValue != null) {
                    hash = 31 * hash + System.identityHashCode(__rolesValue);
                }
                if (__postsValue != null) {
                    hash = 31 * hash + System.identityHashCode(__postsValue);
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
                if (__isVisible(PropId.byIndex(SLOT_USER_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_USER_NAME))) {
                    return false;
                }
                boolean __userNameLoaded = __userNameValue != null;
                if (__userNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_USER_NAME))) {
                    return false;
                }
                if (__userNameLoaded && !Objects.equals(__userNameValue, __other.userName())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_NICK_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_NICK_NAME))) {
                    return false;
                }
                boolean __nickNameLoaded = __nickNameValue != null;
                if (__nickNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_NICK_NAME))) {
                    return false;
                }
                if (__nickNameLoaded && !Objects.equals(__nickNameValue, __other.nickName())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_EMAIL)) != __other.__isVisible(PropId.byIndex(SLOT_EMAIL))) {
                    return false;
                }
                boolean __emailLoaded = this.__emailLoaded;
                if (__emailLoaded != __other.__isLoaded(PropId.byIndex(SLOT_EMAIL))) {
                    return false;
                }
                if (__emailLoaded && !Objects.equals(__emailValue, __other.email())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PHONENUMBER)) != __other.__isVisible(PropId.byIndex(SLOT_PHONENUMBER))) {
                    return false;
                }
                boolean __phonenumberLoaded = this.__phonenumberLoaded;
                if (__phonenumberLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PHONENUMBER))) {
                    return false;
                }
                if (__phonenumberLoaded && !Objects.equals(__phonenumberValue, __other.phonenumber())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_SEX)) != __other.__isVisible(PropId.byIndex(SLOT_SEX))) {
                    return false;
                }
                boolean __sexLoaded = this.__sexLoaded;
                if (__sexLoaded != __other.__isLoaded(PropId.byIndex(SLOT_SEX))) {
                    return false;
                }
                if (__sexLoaded && !Objects.equals(__sexValue, __other.sex())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_AVATAR)) != __other.__isVisible(PropId.byIndex(SLOT_AVATAR))) {
                    return false;
                }
                boolean __avatarLoaded = this.__avatarLoaded;
                if (__avatarLoaded != __other.__isLoaded(PropId.byIndex(SLOT_AVATAR))) {
                    return false;
                }
                if (__avatarLoaded && !Objects.equals(__avatarValue, __other.avatar())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PASSWORD)) != __other.__isVisible(PropId.byIndex(SLOT_PASSWORD))) {
                    return false;
                }
                boolean __passwordLoaded = __passwordValue != null;
                if (__passwordLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PASSWORD))) {
                    return false;
                }
                if (__passwordLoaded && !Objects.equals(__passwordValue, __other.password())) {
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
                if (__isVisible(PropId.byIndex(SLOT_LOGIN_IP)) != __other.__isVisible(PropId.byIndex(SLOT_LOGIN_IP))) {
                    return false;
                }
                boolean __loginIpLoaded = this.__loginIpLoaded;
                if (__loginIpLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LOGIN_IP))) {
                    return false;
                }
                if (__loginIpLoaded && !Objects.equals(__loginIpValue, __other.loginIp())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_LOGIN_DATE)) != __other.__isVisible(PropId.byIndex(SLOT_LOGIN_DATE))) {
                    return false;
                }
                boolean __loginDateLoaded = this.__loginDateLoaded;
                if (__loginDateLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LOGIN_DATE))) {
                    return false;
                }
                if (__loginDateLoaded && !Objects.equals(__loginDateValue, __other.loginDate())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DEPT)) != __other.__isVisible(PropId.byIndex(SLOT_DEPT))) {
                    return false;
                }
                boolean __deptLoaded = this.__deptLoaded;
                if (__deptLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DEPT))) {
                    return false;
                }
                if (__deptLoaded && !Objects.equals(__deptValue, __other.dept())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ROLES)) != __other.__isVisible(PropId.byIndex(SLOT_ROLES))) {
                    return false;
                }
                boolean __rolesLoaded = __rolesValue != null;
                if (__rolesLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLES))) {
                    return false;
                }
                if (__rolesLoaded && !Objects.equals(__rolesValue, __other.roles())) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_POSTS)) != __other.__isVisible(PropId.byIndex(SLOT_POSTS))) {
                    return false;
                }
                boolean __postsLoaded = __postsValue != null;
                if (__postsLoaded != __other.__isLoaded(PropId.byIndex(SLOT_POSTS))) {
                    return false;
                }
                if (__postsLoaded && !Objects.equals(__postsValue, __other.posts())) {
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
                if (__isVisible(PropId.byIndex(SLOT_USER_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_USER_NAME))) {
                    return false;
                }
                boolean __userNameLoaded = __userNameValue != null;
                if (__userNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_USER_NAME))) {
                    return false;
                }
                if (__userNameLoaded && __userNameValue != __other.userName()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_NICK_NAME)) != __other.__isVisible(PropId.byIndex(SLOT_NICK_NAME))) {
                    return false;
                }
                boolean __nickNameLoaded = __nickNameValue != null;
                if (__nickNameLoaded != __other.__isLoaded(PropId.byIndex(SLOT_NICK_NAME))) {
                    return false;
                }
                if (__nickNameLoaded && __nickNameValue != __other.nickName()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_EMAIL)) != __other.__isVisible(PropId.byIndex(SLOT_EMAIL))) {
                    return false;
                }
                boolean __emailLoaded = this.__emailLoaded;
                if (__emailLoaded != __other.__isLoaded(PropId.byIndex(SLOT_EMAIL))) {
                    return false;
                }
                if (__emailLoaded && __emailValue != __other.email()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PHONENUMBER)) != __other.__isVisible(PropId.byIndex(SLOT_PHONENUMBER))) {
                    return false;
                }
                boolean __phonenumberLoaded = this.__phonenumberLoaded;
                if (__phonenumberLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PHONENUMBER))) {
                    return false;
                }
                if (__phonenumberLoaded && __phonenumberValue != __other.phonenumber()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_SEX)) != __other.__isVisible(PropId.byIndex(SLOT_SEX))) {
                    return false;
                }
                boolean __sexLoaded = this.__sexLoaded;
                if (__sexLoaded != __other.__isLoaded(PropId.byIndex(SLOT_SEX))) {
                    return false;
                }
                if (__sexLoaded && __sexValue != __other.sex()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_AVATAR)) != __other.__isVisible(PropId.byIndex(SLOT_AVATAR))) {
                    return false;
                }
                boolean __avatarLoaded = this.__avatarLoaded;
                if (__avatarLoaded != __other.__isLoaded(PropId.byIndex(SLOT_AVATAR))) {
                    return false;
                }
                if (__avatarLoaded && __avatarValue != __other.avatar()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_PASSWORD)) != __other.__isVisible(PropId.byIndex(SLOT_PASSWORD))) {
                    return false;
                }
                boolean __passwordLoaded = __passwordValue != null;
                if (__passwordLoaded != __other.__isLoaded(PropId.byIndex(SLOT_PASSWORD))) {
                    return false;
                }
                if (__passwordLoaded && __passwordValue != __other.password()) {
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
                if (__isVisible(PropId.byIndex(SLOT_LOGIN_IP)) != __other.__isVisible(PropId.byIndex(SLOT_LOGIN_IP))) {
                    return false;
                }
                boolean __loginIpLoaded = this.__loginIpLoaded;
                if (__loginIpLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LOGIN_IP))) {
                    return false;
                }
                if (__loginIpLoaded && __loginIpValue != __other.loginIp()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_LOGIN_DATE)) != __other.__isVisible(PropId.byIndex(SLOT_LOGIN_DATE))) {
                    return false;
                }
                boolean __loginDateLoaded = this.__loginDateLoaded;
                if (__loginDateLoaded != __other.__isLoaded(PropId.byIndex(SLOT_LOGIN_DATE))) {
                    return false;
                }
                if (__loginDateLoaded && __loginDateValue != __other.loginDate()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_DEPT)) != __other.__isVisible(PropId.byIndex(SLOT_DEPT))) {
                    return false;
                }
                boolean __deptLoaded = this.__deptLoaded;
                if (__deptLoaded != __other.__isLoaded(PropId.byIndex(SLOT_DEPT))) {
                    return false;
                }
                if (__deptLoaded && __deptValue != __other.dept()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_ROLES)) != __other.__isVisible(PropId.byIndex(SLOT_ROLES))) {
                    return false;
                }
                boolean __rolesLoaded = __rolesValue != null;
                if (__rolesLoaded != __other.__isLoaded(PropId.byIndex(SLOT_ROLES))) {
                    return false;
                }
                if (__rolesLoaded && __rolesValue != __other.roles()) {
                    return false;
                }
                if (__isVisible(PropId.byIndex(SLOT_POSTS)) != __other.__isVisible(PropId.byIndex(SLOT_POSTS))) {
                    return false;
                }
                boolean __postsLoaded = __postsValue != null;
                if (__postsLoaded != __other.__isLoaded(PropId.byIndex(SLOT_POSTS))) {
                    return false;
                }
                if (__postsLoaded && __postsValue != __other.posts()) {
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
                type = SysUser.class
        )
        private static class DraftImpl extends Implementor implements DraftSpi, SysUserDraft {
            private DraftContext __ctx;

            private Impl __base;

            private Impl __modified;

            private boolean __resolving;

            private SysUser __resolved;

            DraftImpl(DraftContext ctx, SysUser base) {
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
            public SysUserDraft setId(long id) {
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
            public SysUserDraft setCreatedTime(LocalDateTime createdTime) {
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
            public SysUserDraft setUpdatedTime(LocalDateTime updatedTime) {
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
            public SysUserDraft setCreatedBy(Long createdBy) {
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
            public SysUserDraft setUpdatedBy(Long updatedBy) {
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
            public String userName() {
                return (__modified!= null ? __modified : __base).userName();
            }

            @Override
            public SysUserDraft setUserName(String userName) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (userName == null) {
                    throw new IllegalArgumentException(
                        "'userName' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__userNameValue = userName;
                return this;
            }

            @Override
            @JsonIgnore
            public String nickName() {
                return (__modified!= null ? __modified : __base).nickName();
            }

            @Override
            public SysUserDraft setNickName(String nickName) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (nickName == null) {
                    throw new IllegalArgumentException(
                        "'nickName' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__nickNameValue = nickName;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String email() {
                return (__modified!= null ? __modified : __base).email();
            }

            @Override
            public SysUserDraft setEmail(String email) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__emailValue = email;
                __tmpModified.__emailLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String phonenumber() {
                return (__modified!= null ? __modified : __base).phonenumber();
            }

            @Override
            public SysUserDraft setPhonenumber(String phonenumber) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__phonenumberValue = phonenumber;
                __tmpModified.__phonenumberLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String sex() {
                return (__modified!= null ? __modified : __base).sex();
            }

            @Override
            public SysUserDraft setSex(String sex) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__sexValue = sex;
                __tmpModified.__sexLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String avatar() {
                return (__modified!= null ? __modified : __base).avatar();
            }

            @Override
            public SysUserDraft setAvatar(String avatar) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__avatarValue = avatar;
                __tmpModified.__avatarLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            public String password() {
                return (__modified!= null ? __modified : __base).password();
            }

            @Override
            public SysUserDraft setPassword(String password) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (password == null) {
                    throw new IllegalArgumentException(
                        "'password' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__passwordValue = password;
                return this;
            }

            @Override
            @JsonIgnore
            public int status() {
                return (__modified!= null ? __modified : __base).status();
            }

            @Override
            public SysUserDraft setStatus(int status) {
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
            public String loginIp() {
                return (__modified!= null ? __modified : __base).loginIp();
            }

            @Override
            public SysUserDraft setLoginIp(String loginIp) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__loginIpValue = loginIp;
                __tmpModified.__loginIpLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public LocalDateTime loginDate() {
                return (__modified!= null ? __modified : __base).loginDate();
            }

            @Override
            public SysUserDraft setLoginDate(LocalDateTime loginDate) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__loginDateValue = loginDate;
                __tmpModified.__loginDateLoaded = true;
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public SysDeptDraft dept() {
                return __ctx.toDraftObject((__modified!= null ? __modified : __base).dept());
            }

            @Override
            public SysDeptDraft dept(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_DEPT)) || dept() == null)) {
                    setDept(SysDeptDraft.$.produce(null, null));
                }
                return __ctx.toDraftObject((__modified!= null ? __modified : __base).dept());
            }

            @Override
            public SysUserDraft setDept(SysDept dept) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                Impl __tmpModified = __modified();
                __tmpModified.__deptValue = dept;
                __tmpModified.__deptLoaded = true;
                return this;
            }

            @Nullable
            @JsonIgnore
            @Override
            public Long deptId() {
                SysDept dept = dept();
                if (dept == null) {
                    return null;
                }
                return dept.id();
            }

            @OldChain
            @Override
            public SysUserDraft setDeptId(@Nullable Long deptId) {
                if (deptId == null) {
                    setDept(null);
                    return this;
                }
                dept(true).setId(deptId);
                return this;
            }

            @Override
            public SysUserDraft applyDept(DraftConsumer<SysDeptDraft> block) {
                applyDept(null, block);
                return this;
            }

            @Override
            public SysUserDraft applyDept(SysDept base, DraftConsumer<SysDeptDraft> block) {
                setDept(SysDeptDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            public List<SysRole> roles() {
                return __ctx.toDraftList((__modified!= null ? __modified : __base).roles(), SysRole.class, true);
            }

            @Override
            public List<SysRoleDraft> roles(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_ROLES)))) {
                    setRoles(new ArrayList<>());
                }
                return __ctx.toDraftList((__modified!= null ? __modified : __base).roles(), SysRole.class, true);
            }

            @Override
            public SysUserDraft setRoles(List<SysRole> roles) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (roles == null) {
                    throw new IllegalArgumentException(
                        "'roles' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__rolesValue = NonSharedList.of(__tmpModified.__rolesValue, roles);
                return this;
            }

            @Override
            public SysUserDraft addIntoRoles(DraftConsumer<SysRoleDraft> block) {
                addIntoRoles(null, block);
                return this;
            }

            @Override
            public SysUserDraft addIntoRoles(SysRole base, DraftConsumer<SysRoleDraft> block) {
                roles(true).add((SysRoleDraft)SysRoleDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            public List<SysPost> posts() {
                return __ctx.toDraftList((__modified!= null ? __modified : __base).posts(), SysPost.class, true);
            }

            @Override
            public List<SysPostDraft> posts(boolean autoCreate) {
                if (autoCreate && (!__isLoaded(PropId.byIndex(SLOT_POSTS)))) {
                    setPosts(new ArrayList<>());
                }
                return __ctx.toDraftList((__modified!= null ? __modified : __base).posts(), SysPost.class, true);
            }

            @Override
            public SysUserDraft setPosts(List<SysPost> posts) {
                if (__resolved != null) {
                    throw new IllegalStateException("The current draft has been resolved so it cannot be modified");
                }
                if (posts == null) {
                    throw new IllegalArgumentException(
                        "'posts' cannot be null, please specify non-null value or use nullable annotation to decorate this property"
                    );
                }
                Impl __tmpModified = __modified();
                __tmpModified.__postsValue = NonSharedList.of(__tmpModified.__postsValue, posts);
                return this;
            }

            @Override
            public SysUserDraft addIntoPosts(DraftConsumer<SysPostDraft> block) {
                addIntoPosts(null, block);
                return this;
            }

            @Override
            public SysUserDraft addIntoPosts(SysPost base, DraftConsumer<SysPostDraft> block) {
                posts(true).add((SysPostDraft)SysPostDraft.$.produce(base, block));
                return this;
            }

            @Override
            @JsonIgnore
            @Nullable
            public String remark() {
                return (__modified!= null ? __modified : __base).remark();
            }

            @Override
            public SysUserDraft setRemark(String remark) {
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
                    case SLOT_USER_NAME:
                    		setUserName((String)value);break;
                    case SLOT_NICK_NAME:
                    		setNickName((String)value);break;
                    case SLOT_EMAIL:
                    		setEmail((String)value);break;
                    case SLOT_PHONENUMBER:
                    		setPhonenumber((String)value);break;
                    case SLOT_SEX:
                    		setSex((String)value);break;
                    case SLOT_AVATAR:
                    		setAvatar((String)value);break;
                    case SLOT_PASSWORD:
                    		setPassword((String)value);break;
                    case SLOT_STATUS:
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case SLOT_LOGIN_IP:
                    		setLoginIp((String)value);break;
                    case SLOT_LOGIN_DATE:
                    		setLoginDate((LocalDateTime)value);break;
                    case SLOT_DEPT:
                    		setDept((SysDept)value);break;
                    case SLOT_ROLES:
                    		setRoles((List<SysRole>)value);break;
                    case SLOT_POSTS:
                    		setPosts((List<SysPost>)value);break;
                    case SLOT_REMARK:
                    		setRemark((String)value);break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + prop + "\"");
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
                    case "userName":
                    		setUserName((String)value);break;
                    case "nickName":
                    		setNickName((String)value);break;
                    case "email":
                    		setEmail((String)value);break;
                    case "phonenumber":
                    		setPhonenumber((String)value);break;
                    case "sex":
                    		setSex((String)value);break;
                    case "avatar":
                    		setAvatar((String)value);break;
                    case "password":
                    		setPassword((String)value);break;
                    case "status":
                    		if (value == null) throw new IllegalArgumentException("'status' cannot be null, if you want to set null, please use any annotation whose simple name is \"Nullable\" to decorate the property");
                            setStatus((Integer)value);
                            break;
                    case "loginIp":
                    		setLoginIp((String)value);break;
                    case "loginDate":
                    		setLoginDate((LocalDateTime)value);break;
                    case "dept":
                    		setDept((SysDept)value);break;
                    case "roles":
                    		setRoles((List<SysRole>)value);break;
                    case "posts":
                    		setPosts((List<SysPost>)value);break;
                    case "remark":
                    		setRemark((String)value);break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + prop + "\"");
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
                    __modified().__visibility = __visibility = Visibility.of(19);
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
                    case SLOT_USER_NAME:
                    		__visibility.show(SLOT_USER_NAME, visible);break;
                    case SLOT_NICK_NAME:
                    		__visibility.show(SLOT_NICK_NAME, visible);break;
                    case SLOT_EMAIL:
                    		__visibility.show(SLOT_EMAIL, visible);break;
                    case SLOT_PHONENUMBER:
                    		__visibility.show(SLOT_PHONENUMBER, visible);break;
                    case SLOT_SEX:
                    		__visibility.show(SLOT_SEX, visible);break;
                    case SLOT_AVATAR:
                    		__visibility.show(SLOT_AVATAR, visible);break;
                    case SLOT_PASSWORD:
                    		__visibility.show(SLOT_PASSWORD, visible);break;
                    case SLOT_STATUS:
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case SLOT_LOGIN_IP:
                    		__visibility.show(SLOT_LOGIN_IP, visible);break;
                    case SLOT_LOGIN_DATE:
                    		__visibility.show(SLOT_LOGIN_DATE, visible);break;
                    case SLOT_DEPT:
                    		__visibility.show(SLOT_DEPT, visible);break;
                    case SLOT_ROLES:
                    		__visibility.show(SLOT_ROLES, visible);break;
                    case SLOT_POSTS:
                    		__visibility.show(SLOT_POSTS, visible);break;
                    case SLOT_REMARK:
                    		__visibility.show(SLOT_REMARK, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + 
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
                    __modified().__visibility = __visibility = Visibility.of(19);
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
                    case "userName":
                    		__visibility.show(SLOT_USER_NAME, visible);break;
                    case "nickName":
                    		__visibility.show(SLOT_NICK_NAME, visible);break;
                    case "email":
                    		__visibility.show(SLOT_EMAIL, visible);break;
                    case "phonenumber":
                    		__visibility.show(SLOT_PHONENUMBER, visible);break;
                    case "sex":
                    		__visibility.show(SLOT_SEX, visible);break;
                    case "avatar":
                    		__visibility.show(SLOT_AVATAR, visible);break;
                    case "password":
                    		__visibility.show(SLOT_PASSWORD, visible);break;
                    case "status":
                    		__visibility.show(SLOT_STATUS, visible);break;
                    case "loginIp":
                    		__visibility.show(SLOT_LOGIN_IP, visible);break;
                    case "loginDate":
                    		__visibility.show(SLOT_LOGIN_DATE, visible);break;
                    case "dept":
                    		__visibility.show(SLOT_DEPT, visible);break;
                    case "roles":
                    		__visibility.show(SLOT_ROLES, visible);break;
                    case "posts":
                    		__visibility.show(SLOT_POSTS, visible);break;
                    case "remark":
                    		__visibility.show(SLOT_REMARK, visible);break;
                    default: throw new IllegalArgumentException(
                                "Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + 
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
                    case SLOT_USER_NAME:
                    		__modified().__userNameValue = null;break;
                    case SLOT_NICK_NAME:
                    		__modified().__nickNameValue = null;break;
                    case SLOT_EMAIL:
                    		__modified().__emailValue = null;
                    __modified().__emailLoaded = false;break;
                    case SLOT_PHONENUMBER:
                    		__modified().__phonenumberValue = null;
                    __modified().__phonenumberLoaded = false;break;
                    case SLOT_SEX:
                    		__modified().__sexValue = null;
                    __modified().__sexLoaded = false;break;
                    case SLOT_AVATAR:
                    		__modified().__avatarValue = null;
                    __modified().__avatarLoaded = false;break;
                    case SLOT_PASSWORD:
                    		__modified().__passwordValue = null;break;
                    case SLOT_STATUS:
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case SLOT_LOGIN_IP:
                    		__modified().__loginIpValue = null;
                    __modified().__loginIpLoaded = false;break;
                    case SLOT_LOGIN_DATE:
                    		__modified().__loginDateValue = null;
                    __modified().__loginDateLoaded = false;break;
                    case SLOT_DEPT:
                    		__modified().__deptValue = null;
                    __modified().__deptLoaded = false;break;
                    case SLOT_ROLES:
                    		__modified().__rolesValue = null;break;
                    case SLOT_POSTS:
                    		__modified().__postsValue = null;break;
                    case SLOT_REMARK:
                    		__modified().__remarkValue = null;
                    __modified().__remarkLoaded = false;break;
                    default: throw new IllegalArgumentException("Illegal property id for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
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
                    case "userName":
                    		__modified().__userNameValue = null;break;
                    case "nickName":
                    		__modified().__nickNameValue = null;break;
                    case "email":
                    		__modified().__emailValue = null;
                    __modified().__emailLoaded = false;break;
                    case "phonenumber":
                    		__modified().__phonenumberValue = null;
                    __modified().__phonenumberLoaded = false;break;
                    case "sex":
                    		__modified().__sexValue = null;
                    __modified().__sexLoaded = false;break;
                    case "avatar":
                    		__modified().__avatarValue = null;
                    __modified().__avatarLoaded = false;break;
                    case "password":
                    		__modified().__passwordValue = null;break;
                    case "status":
                    		__modified().__statusValue = 0;
                    __modified().__statusLoaded = false;break;
                    case "loginIp":
                    		__modified().__loginIpValue = null;
                    __modified().__loginIpLoaded = false;break;
                    case "loginDate":
                    		__modified().__loginDateValue = null;
                    __modified().__loginDateLoaded = false;break;
                    case "dept":
                    		__modified().__deptValue = null;
                    __modified().__deptLoaded = false;break;
                    case "roles":
                    		__modified().__rolesValue = null;break;
                    case "posts":
                    		__modified().__postsValue = null;break;
                    case "remark":
                    		__modified().__remarkValue = null;
                    __modified().__remarkLoaded = false;break;
                    default: throw new IllegalArgumentException("Illegal property name for \"io.github.faustofanb.admin.module.system.domain.entity.SysUser\": \"" + prop + "\", it does not exist or its loaded state is not controllable");
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
                        if (base.__isLoaded(PropId.byIndex(SLOT_DEPT))) {
                            SysDept oldValue = base.dept();
                            SysDept newValue = __ctx.resolveObject(oldValue);
                            if (oldValue != newValue) {
                                setDept(newValue);
                            }
                        }
                        if (base.__isLoaded(PropId.byIndex(SLOT_ROLES))) {
                            List<SysRole> oldValue = base.roles();
                            List<SysRole> newValue = __ctx.resolveList(oldValue);
                            if (oldValue != newValue) {
                                setRoles(newValue);
                            }
                        }
                        if (base.__isLoaded(PropId.byIndex(SLOT_POSTS))) {
                            List<SysPost> oldValue = base.posts();
                            List<SysPost> newValue = __ctx.resolveList(oldValue);
                            if (oldValue != newValue) {
                                setPosts(newValue);
                            }
                        }
                        __tmpModified = __modified;
                    }
                    else {
                        __tmpModified.__deptValue = __ctx.resolveObject(__tmpModified.__deptValue);
                        __tmpModified.__rolesValue = NonSharedList.of(__tmpModified.__rolesValue, __ctx.resolveList(__tmpModified.__rolesValue));
                        __tmpModified.__postsValue = NonSharedList.of(__tmpModified.__postsValue, __ctx.resolveList(__tmpModified.__postsValue));
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
            type = SysUser.class
    )
    class Builder {
        private final Producer.DraftImpl __draft;

        public Builder() {
            this(null);
        }

        public Builder(@Nullable SysUser base) {
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

        public Builder userName(@NotNull String userName) {
            if (userName != null) {
                __draft.setUserName(userName);
            }
            return this;
        }

        public Builder nickName(@NotNull String nickName) {
            if (nickName != null) {
                __draft.setNickName(nickName);
            }
            return this;
        }

        public Builder email(@Nullable String email) {
            __draft.setEmail(email);
            return this;
        }

        public Builder phonenumber(@Nullable String phonenumber) {
            __draft.setPhonenumber(phonenumber);
            return this;
        }

        public Builder sex(@Nullable String sex) {
            __draft.setSex(sex);
            return this;
        }

        public Builder avatar(@Nullable String avatar) {
            __draft.setAvatar(avatar);
            return this;
        }

        public Builder password(@NotNull String password) {
            if (password != null) {
                __draft.setPassword(password);
            }
            return this;
        }

        public Builder status(@NotNull Integer status) {
            if (status != null) {
                __draft.setStatus(status);
            }
            return this;
        }

        public Builder loginIp(@Nullable String loginIp) {
            __draft.setLoginIp(loginIp);
            return this;
        }

        public Builder loginDate(@Nullable LocalDateTime loginDate) {
            __draft.setLoginDate(loginDate);
            return this;
        }

        public Builder dept(@Nullable SysDept dept) {
            __draft.setDept(dept);
            return this;
        }

        public Builder roles(@NotNull List<SysRole> roles) {
            if (roles != null) {
                __draft.setRoles(roles);
            }
            return this;
        }

        public Builder posts(@NotNull List<SysPost> posts) {
            if (posts != null) {
                __draft.setPosts(posts);
            }
            return this;
        }

        public Builder remark(@Nullable String remark) {
            __draft.setRemark(remark);
            return this;
        }

        public SysUser build() {
            return (SysUser)__draft.__modified();
        }
    }
}
