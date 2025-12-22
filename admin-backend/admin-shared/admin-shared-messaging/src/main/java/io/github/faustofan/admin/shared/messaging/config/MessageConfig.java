package io.github.faustofan.admin.shared.messaging.config;

import java.util.concurrent.Executors;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.pulsar.shade.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import io.github.faustofan.admin.shared.messaging.core.NoOpMessageProvider;
import io.github.faustofan.admin.shared.messaging.core.PulsarMessageProvider;
import io.github.faustofan.admin.shared.messaging.core.RemoteMessageProvider;

@Configuration
public class MessageConfig {

    /**
     * JDK 21+ 核心特性：虚拟线程执行器
     * 用于托管所有消息发送、IO密集型任务，替代传统线程池
     */
    @Bean("msgVirtualExecutor")
    public AsyncTaskExecutor msgVirtualExecutor() {
        // 使用每个任务一个虚拟线程的策略，无界且极轻量
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * 生产级 Jackson 配置
     * 必须支持 Java 8/JDK 25 的时间日期类型 (JSR310)
     */
    @Bean
    public ObjectMapper msgObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    @ConditionalOnBean(PulsarClient.class)
    public RemoteMessageProvider pulsarMessageProvider(PulsarClient pulsarClient, ObjectMapper objectMapper) {
        return new PulsarMessageProvider(pulsarClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(RemoteMessageProvider.class) // 这里使用 MissingBean 是安全的，因为它是配置类
    public RemoteMessageProvider noOpMessageProvider() {
        return new NoOpMessageProvider();
    }
}