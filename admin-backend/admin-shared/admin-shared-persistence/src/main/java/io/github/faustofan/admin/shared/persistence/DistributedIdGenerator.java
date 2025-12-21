package io.github.faustofan.admin.shared.persistence;

import io.github.faustofan.admin.shared.common.util.SnowflakeIdGenerator;
import org.babyfish.jimmer.sql.meta.UserIdGenerator;

/**
 * 分布式ID生成器，基于雪花算法
 */
public class DistributedIdGenerator implements UserIdGenerator<Long> {

    /**
     * 生成分布式唯一ID
     *
     * @param entityType 实体类型
     * @return 分布式唯一ID
     */
    @Override
    public Long generate(Class<?> entityType) {
        return SnowflakeIdGenerator.getInstance().nextId();
    }
}
