package io.github.faustofan.admin.infra

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Configuration
@Profile("dev")
class LiquibaseLockFixConfig {

    companion object {
        private val log = LoggerFactory.getLogger(LiquibaseLockFixConfig::class.java)

        @Bean
        @JvmStatic
        // 【关键修改】这里不再注入 DataSource，改为注入 ApplicationContext
        // 这样就不会强制 DataSource 提前初始化了
        fun liquibaseUnlocker(applicationContext: ApplicationContext): BeanPostProcessor {
            return object : BeanPostProcessor {
                override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
                    // 只有当轮到 liquibase 初始化时，我们才去获取 DataSource
                    if (beanName == "liquibase") {
                        try {
                            // 【懒加载】此时 DataSource 肯定已经准备好了（因为 Liquibase 依赖它）
                            val dataSource = applicationContext.getBean(DataSource::class.java)
                            forceUnlock(dataSource)
                        } catch (e: Exception) {
                            log.warn("⚠️ [H2解锁] 尝试重置锁失败: {}", e.message)
                        }
                    }
                    return bean
                }
            }
        }

        private fun forceUnlock(dataSource: DataSource) {
            dataSource.connection.use { conn ->
                val stmt = conn.createStatement()
                log.info("🔨 [H2解锁] 检测到开发环境，正在强制重置 Liquibase 数据库锁...")
                // 执行解锁
                stmt.executeUpdate("UPDATE public.DATABASECHANGELOGLOCK SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1")
                log.info("✅ [H2解锁] 数据库锁已重置，Liquibase 即将瞬间启动")
            }
        }
    }
}