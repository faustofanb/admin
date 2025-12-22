package io.github.faustofan.admin.shared.distributed.constants;

import java.time.Duration;

/**
 * Key 定义规范接口
 */
public interface KeyDefinition {
    String getPrefix();

    Duration getTtl();

    String getDescription();

    /**
     * 构建完整Key
     * 
     * @param suffix 业务后缀
     * @return 完整Key字符串
     */
    default String buildKey(String suffix) {
        return getPrefix() + suffix;
    }
}
