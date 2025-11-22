package io.github.faustofanb.admin.module.audit.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.faustofanb.admin.common.model.Result;
import io.github.faustofanb.admin.module.audit.annotation.Log;
import io.github.faustofanb.admin.module.audit.enums.BusinessType;
import io.github.faustofanb.admin.module.audit.service.SysOperLogService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/monitor/operlog")
@RequiredArgsConstructor
public class SysOperLogController {

    private final SysOperLogService sysOperLogService;

    @Log(title = "Operation Log", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public Result<Void> clean() {
        sysOperLogService.cleanLog();
        return Result.success();
    }
}
