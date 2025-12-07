package io.github.faustofan.admin.shared.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import tools.jackson.databind.json.JsonMapper
import tools.jackson.datatype.jsr310.JavaTimeModule
import tools.jackson.module.kotlin.KotlinModule

@Configuration
class JacksonConfig {

    @Bean
    @Primary
    fun jsonMapper(): JsonMapper {
        return JsonMapper.builder()
            // 【关键】注册时间模块，让 LocalDateTime 变成字符串
            .addModule(JavaTimeModule())
            // 【关键】注册 Kotlin 模块，让 data class 能正常工作
            .addModule(KotlinModule.Builder().build())

            .build()
    }
}