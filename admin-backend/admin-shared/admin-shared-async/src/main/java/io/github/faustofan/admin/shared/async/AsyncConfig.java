package io.github.faustofan.admin.shared.async;

import io.github.faustofan.admin.shared.common.context.AppContext;
import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务配置类
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * 1. 默认异步执行器 (@Async 不指定名称时使用)
     * 场景：常规业务，日志记录、发邮件、简单的异步逻辑
     * 特性：使用虚拟线程，无界并发
     */
    @Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    @Primary
    @Override
    public AsyncTaskExecutor getAsyncExecutor() {
        log.info("Initializing Default Virtual Thread Executor");
        // Spring 3.2+ SimpleAsyncTaskExecutor 原生支持虚拟线程
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("vt-default-");
        executor.setVirtualThreads(true); // 开启虚拟线程
        executor.setTaskDecorator(new ContextTaskDecorator()); // 上下文透传
        executor.setTaskTerminationTimeout(60_000); // 优雅停机等待60s
        return executor;
    }

    /**
     * 2. IO密集型专用 (@Async("ioExecutor"))
     * 场景：HTTP请求、OSS上传下载、RPC调用
     * 特性：虚拟线程，高并发
     */
    @Bean("ioExecutor")
    public Executor ioExecutor() {
        log.info("Initializing IO-Bound Virtual Thread Executor");
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("vt-io-");
        executor.setVirtualThreads(true);
        executor.setTaskDecorator(new ContextTaskDecorator());
        return executor;
    }

    /**
     * 3. 批量/报表处理专用 (@Async("batchExecutor"))
     * 场景：Excel导入导出、批量数据同步
     * 特性：虚拟线程 + 强限流。防止瞬间生成1万个任务耗尽数据库连接池。
     */
    @Bean("batchExecutor")
    public Executor batchExecutor() {
        log.info("Initializing Batch Virtual Thread Executor (Throttled)");
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("vt-batch-");
        executor.setVirtualThreads(true);
        executor.setTaskDecorator(new ContextTaskDecorator());
        // 【关键】限制同时执行的任务数不超过 50，超出部分会阻塞调用方或排队
        // 注意：SimpleAsyncTaskExecutor 的限流机制会阻塞提交任务的线程，
        // 如果需要队列缓冲，此处需改用封装了 Semaphore 的自定义实现，但通常 50 并发对后台管理系统足够。
        executor.setConcurrencyLimit(50);
        return executor;
    }

    /**
     * 4. CPU密集型专用 (@Async("cpuExecutor"))
     * 场景：大图压缩、复杂加密解密、视频转码
     * 特性：传统平台线程池。计算密集型任务不适合虚拟线程（无法被OS抢占）。
     */
    @Bean("cpuExecutor")
    public Executor cpuExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        log.info("Initializing CPU-Bound Platform Thread Executor (Cores: {})", cores);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores + 1);
        executor.setMaxPoolSize(cores * 2);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("pt-cpu-");
        executor.setTaskDecorator(new ContextTaskDecorator());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 异常捕获器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

    /**
     * 内部类：MDC 上下文装饰器
     * 解决 TraceId 丢失问题
     */
    public static class ContextTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            // 1. 主线程：捕获上下文
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            AppContext appContext = AppContextHolder.getContext();
            return () -> {
                try {
                    // 2. 子线程：注入上下文
                    if (contextMap != null) {
                        MDC.setContextMap(contextMap);
                    }
                    if(appContext != null) {
                        AppContextHolder.setContext(appContext);
                    }
                    runnable.run();
                } finally {
                    // 3. 子线程：清理
                    MDC.clear();
                    AppContextHolder.clearContext();
                }
            };
        }
    }

    /**
     * 内部类：自定义异常处理
     */
    public static class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            log.error("Async execution error. Method: [{}], Msg: {}", method.getName(), ex.getMessage(), ex);
            // TODO: 发送监控告警 (钉钉/邮件)
        }
    }
}