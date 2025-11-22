package io.github.faustofanb.admin.module.audit.service.impl;

import org.springframework.stereotype.Service;

import io.github.faustofanb.admin.module.audit.domain.entity.SysOperLog;
import io.github.faustofanb.admin.module.audit.repository.SysOperLogRepository;
import io.github.faustofanb.admin.module.audit.service.SysOperLogService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysOperLogServiceImpl implements SysOperLogService {

    private final SysOperLogRepository sysOperLogRepository;

    @Override
    public void saveLog(SysOperLog sysOperLog) {
        sysOperLogRepository.save(sysOperLog);
    }

    @Override
    public void cleanLog() {
        sysOperLogRepository.deleteAll();
    }
}
