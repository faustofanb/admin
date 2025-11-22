package io.github.faustofanb.admin.module.audit.service;

import io.github.faustofanb.admin.module.audit.domain.entity.SysOperLog;

public interface SysOperLogService {
    void saveLog(SysOperLog sysOperLog);

    void cleanLog();
}
