package io.github.faustofan.admin.shared.web.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * OpenAPI 全局配置 (Knife4j)
 */
@Configuration
@SecurityScheme(
        name = OpenApiConfig.SECURITY_SCHEME_NAME,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT 认证，请在登录后获取 Token，格式：Bearer {token}",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"
)
public class OpenApiConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);
    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    private final Environment environment;

    public OpenApiConfig(final Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Admin Backend API")
                        .description(
                                """
                                        后台管理系统 API 文档

                                        ## 认证说明
                                        - 除登录接口外，其他接口均需要携带 JWT Token
                                        - Token 通过 `/api/v1/auth/login` 接口获取
                                        - 请求时在 Header 中添加：`Authorization: Bearer {token}`
                                        """
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FaustoFan")
                                .email("faustofan@gmail.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                )
                .addServersItem(new Server()
                        .url("/")
                        .description("当前环境")
                );
    }

    @Override
    public void run(@Nullable String... args) throws Exception {
        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        // Knife4j 默认路径为 doc.html
        String knife4jUrl = "http://localhost:" + port + contextPath + "doc.html";

        logger.info("""
                
                ----------------------------------------------------------
                \tKnife4j API 文档: \t{}
                ----------------------------------------------------------""",
                knife4jUrl
        );
    }
}