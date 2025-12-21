package io.github.faustofan.admin.system.domain.enums;

import org.babyfish.jimmer.sql.EnumType;

/**
 * 用户状态
 */
@EnumType(EnumType.Strategy.NAME)
public enum UserStatus {
    ACTIVE,
    LOCKED,
    DISABLED
}
