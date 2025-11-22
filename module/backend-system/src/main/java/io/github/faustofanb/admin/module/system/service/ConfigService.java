package io.github.faustofanb.admin.module.system.service;

public interface ConfigService {

    String selectConfigByKey(String configKey);

    void deleteConfigByIds(Iterable<Long> ids);
}
