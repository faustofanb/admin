package io.github.faustofan.admin.shared.infrastructure.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.Kryo5Codec
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(prefix = "spring.redisson", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(RedissonCustomProperties::class, DataRedisProperties::class)
class RedissonConfig(
    private val redisProperties: DataRedisProperties,
    private val redissonCustomProperties: RedissonCustomProperties
) {

    @Bean(destroyMethod = "shutdown")
    fun redissonClient(): RedissonClient {
        val config = Config()

        // 使用 Kryo5Codec - 高性能二进制序列化，自动处理类型
        // 比 JsonJacksonCodec 更快，且不需要 @class 属性
        config.codec = Kryo5Codec()

        // 2. 确定协议前缀 (SSL 支持)
        val protocolPrefix = if (redisProperties.ssl.isEnabled) "rediss://" else "redis://"

        // 3. 根据模式应用不同配置
        when (redissonCustomProperties.mode) {
            RedissonMode.SINGLE -> configureSingle(config, protocolPrefix)
            RedissonMode.CLUSTER -> configureCluster(config, protocolPrefix)
        }

        return Redisson.create(config)
    }

    /**
     * 配置单机模式
     */
    private fun configureSingle(config: Config, prefix: String) {
        val address = "$prefix${redisProperties.host}:${redisProperties.port}"

        val singleConfig = config.useSingleServer()
            .setAddress(address)
            .setDatabase(redisProperties.database)
            .setConnectTimeout(redissonCustomProperties.connectTimeout)
            .setTimeout(redissonCustomProperties.timeout)
            .setRetryAttempts(redissonCustomProperties.retryAttempts)
            .setRetryInterval(redissonCustomProperties.retryInterval)
            .setConnectionPoolSize(redissonCustomProperties.poolSize)
            .setConnectionMinimumIdleSize(redissonCustomProperties.minIdleSize)

        // 设置密码
        redisProperties.password?.takeIf { it.isNotEmpty() }?.let {
            singleConfig.password = it
        }

        singleConfig.clientName = "admin-backend-single"
    }

    /**
     * 配置集群模式
     */
    private fun configureCluster(config: Config, prefix: String) {
        // 校验 Spring 配置
        val nodes = redisProperties.cluster?.nodes
        if (nodes.isNullOrEmpty()) {
            throw IllegalStateException("Redisson is configured in CLUSTER mode, but 'spring.data.redis.cluster.nodes' is empty!")
        }

        val clusterConfig = config.useClusterServers()
            .setScanInterval(redissonCustomProperties.clusterScanInterval) // 集群拓扑刷新间隔
            .setConnectTimeout(redissonCustomProperties.connectTimeout)
            .setTimeout(redissonCustomProperties.timeout)
            .setRetryAttempts(redissonCustomProperties.retryAttempts)
            .setRetryInterval(redissonCustomProperties.retryInterval)
            // 集群模式下，连接池大小通常分为 Master 和 Slave
            .setMasterConnectionPoolSize(redissonCustomProperties.poolSize)
            .setSlaveConnectionPoolSize(redissonCustomProperties.poolSize)
            .setMasterConnectionMinimumIdleSize(redissonCustomProperties.minIdleSize)
            .setSlaveConnectionMinimumIdleSize(redissonCustomProperties.minIdleSize)

        // 添加节点地址
        nodes.forEach { node ->
            // node 格式通常为 "127.0.0.1:6379"
            clusterConfig.addNodeAddress("$prefix$node")
        }

        // 设置密码
        redisProperties.password?.takeIf { it.isNotEmpty() }?.let {
            clusterConfig.password = it
        }

        clusterConfig.clientName = "admin-backend-cluster"
    }
}

/**
 * Redisson 高级配置
 */
@ConfigurationProperties(prefix = "spring.redisson")
data class RedissonCustomProperties(
    var enabled: Boolean = true,
    /**
     * 运行模式: SINGLE (单机), CLUSTER (集群)
     */
    var mode: RedissonMode = RedissonMode.SINGLE,

    /**
     * 连接超时时间 (毫秒)
     */
    var connectTimeout: Int = 10000,

    /**
     * 命令等待超时时间 (毫秒)
     */
    var timeout: Int = 3000,

    /**
     * 命令失败重试次数
     */
    var retryAttempts: Int = 3,

    /**
     * 命令重试发送时间间隔 (毫秒)
     */
    var retryInterval: Int = 1500,

    /**
     * 连接池大小 (最大连接数)
     * 在集群模式下，这会被应用到 Master 和 Slave 的连接池设置中
     */
    var poolSize: Int = 64,

    /**
     * 最小空闲连接数
     */
    var minIdleSize: Int = 10,

    /**
     * 集群模式专属：集群扫描间隔 (毫秒)
     * 用于监测集群拓扑变化
     */
    var clusterScanInterval: Int = 2000
)

/**
 * 运行模式枚举
 */
enum class RedissonMode {
    SINGLE,
    CLUSTER
}