package io.github.faustofan.admin.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * OpenAPI 全局配置
 *
 * 定义全局安全方案、API 基本信息等
 */
@Configuration
@SecurityScheme(
    name = OpenApiConfig.SECURITY_SCHEME_NAME,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT 认证，请在登录后获取 Token，格式：Bearer {token}",
    `in` = SecuritySchemeIn.HEADER,
    paramName = "Authorization"
)
class OpenApiConfig {

    companion object {
        const val SECURITY_SCHEME_NAME = "bearerAuth"
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Admin Backend API")
                    .description("""
                        后台管理系统 API 文档
                        
                        ## 认证说明
                        - 除登录接口外，其他接口均需要携带 JWT Token
                        - Token 通过 `/api/v1/auth/login` 接口获取
                        - 请求时在 Header 中添加：`Authorization: Bearer {token}`
                        
                        ## 错误码说明
                        - `200`: 成功
                        - `400`: 请求参数错误
                        - `401`: 未认证或 Token 过期
                        - `403`: 无权限访问
                        - `404`: 资源不存在
                        - `500`: 服务器内部错误
                    """.trimIndent())
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("FaustoFan")
                            .email("faustofan@example.com")
                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0")
                    )
            )
            .addServersItem(
                Server()
                    .url("/")
                    .description("当前环境")
            )
    }
}

