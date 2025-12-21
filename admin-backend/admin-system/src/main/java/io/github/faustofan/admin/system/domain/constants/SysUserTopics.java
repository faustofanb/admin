package io.github.faustofan.admin.system.domain.constants;

public final class SysUserTopics {
    private SysUserTopics() {}

    // 格式：业务域.聚合.动作
    public static final String USER_CREATED = "sys.user.created";
    public static final String USER_STATUS_CHANGED = "sys.user.status.changed";
    public static final String USER_PASSWORD_CHANGED = "sys.user.password.changed";
}
