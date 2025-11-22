package io.github.faustofanb.admin.module.audit.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.faustofanb.admin.common.model.Result;
import io.github.faustofanb.admin.module.audit.annotation.Log;
import io.github.faustofanb.admin.module.audit.enums.BusinessType;
import io.github.faustofanb.admin.module.audit.service.SysLogininforService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/monitor/logininfor")
@RequiredArgsConstructor
public class SysLogininforController {

    private final SysLogininforService sysLogininforService;

    @Log(title = "Login Log", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public Result<Void> clean() {
        sysLogininforService.cleanLogininfor();
        return Result.success();
    }
}
