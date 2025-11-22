package io.github.faustofanb.admin.module.system.service;

import io.github.faustofanb.admin.module.system.domain.entity.SysConfig;

public interface ConfigService {

    String selectConfigByKey(String configKey);

    void deleteConfigByIds(Iterable<Long> ids);
}
