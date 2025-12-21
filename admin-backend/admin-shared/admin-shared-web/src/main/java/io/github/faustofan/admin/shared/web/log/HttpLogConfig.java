package io.github.faustofan.admin.shared.web.log;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HttpLogProperties.class)
class HttpLogConfig {

    @Bean
    public HttpLoggingFilter httpLoggingFilter(HttpLogProperties properties) {
        return new HttpLoggingFilter(properties);
    }
}
