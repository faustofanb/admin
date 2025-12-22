package io.github.faustofan.admin.shared.distributed.constants;

import io.github.faustofan.admin.shared.common.constant.GlobalConstants;
import java.time.Duration;

/**
 * Redis Key 定义接口
 */
public interface KeyDefinition {
    
    /** 模块名称 (如 AUTH, SYS, BIZ) */
    String getModule();

    /** 业务标识 (如 USER:INFO) */
    String getFunction();

    Duration getTtl();

    String getDescription();

    /**
     * 构建完整Key: ADMIN:MODULE:FUNCTION:SUFFIX
     */
    default String buildKey(Object... suffixes) {
        StringBuilder sb = new StringBuilder()
            .append(GlobalConstants.ROOT_PREFIX).append(GlobalConstants.DELIMITER)
            .append(getModule()).append(GlobalConstants.DELIMITER)
            .append(getFunction()).append(GlobalConstants.DELIMITER);
            
        if (suffixes != null && suffixes.length > 0) {
            for (Object suffix : suffixes) {
                sb.append(suffix).append(GlobalConstants.DELIMITER);
            }
            // 移除最后一个多余的分隔符
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}