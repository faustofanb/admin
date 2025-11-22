package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntity;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.ManyToMany;
import org.babyfish.jimmer.sql.ManyToOne;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息表
 */
@Entity
public interface SysUser extends BaseEntity {

    /**
     * 用户账号
     */
    String userName();

    /**
     * 用户昵称
     */
    String nickName();

    /**
     * 用户邮箱
     */
    @Nullable
    String email();

    /**
     * 手机号码
     */
    @Nullable
    String phonenumber();

    /**
     * 用户性别（0男 1女 2未知）
     */
    @Nullable
    String sex();

    /**
     * 头像地址
     */
    @Nullable
    String avatar();

    /**
     * 密码
     */
    String password();

    /**
     * 帐号状态（0正常 1停用）
     */
    int status();

    /**
     * 最后登录IP
     */
    @Nullable
    String loginIp();

    /**
     * 最后登录时间
     */
    @Nullable
    LocalDateTime loginDate();

    /**
     * 部门
     */
    @Nullable
    @ManyToOne
    SysDept dept();

    /**
     * 角色
     */
    @ManyToMany
    List<SysRole> roles();

    /**
     * 岗位
     */
    @ManyToMany
    List<SysPost> posts();
    
    /**
     * 备注
     */
    @Nullable
    String remark();
}
