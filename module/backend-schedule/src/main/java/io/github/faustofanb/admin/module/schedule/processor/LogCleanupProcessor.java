package io.github.faustofanb.admin.module.schedule.processor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import io.github.faustofanb.admin.module.audit.service.SysOperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

@Component
@Slf4j
@RequiredArgsConstructor
public class LogCleanupProcessor implements BasicProcessor {

    private final SysOperLogService sysOperLogService;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        log.info("Start cleaning up operation logs...");

        // Default to 30 days ago, or parse from job params
        int days = 30;
        try {
            String jobParams = context.getJobParams();
            if (jobParams != null && !jobParams.isEmpty()) {
                days = Integer.parseInt(jobParams);
            }
        } catch (Exception e) {
            log.warn("Failed to parse job params, using default 30 days");
        }

        LocalDateTime time = LocalDateTime.now().minusDays(days);
        int count = sysOperLogService.cleanLogBefore(time);

        log.info("Cleaned up {} operation logs before {}", count, time);
        return new ProcessResult(true, "Cleaned up " + count + " logs");
    }
}
