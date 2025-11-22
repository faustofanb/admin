package io.github.faustofanb.admin.module.system.service.impl;

import io.github.faustofanb.admin.module.system.domain.entity.SysConfig;
import io.github.faustofanb.admin.module.system.repository.SysConfigRepository;
import io.github.faustofanb.admin.module.system.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private final SysConfigRepository configRepository;

    @Override
    public String selectConfigByKey(String configKey) {
        return configRepository.findByConfigKey(configKey)
                .map(SysConfig::configValue)
                .orElse("");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfigByIds(Iterable<Long> ids) {
        configRepository.deleteAllById(ids);
    }
}
