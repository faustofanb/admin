package io.github.faustofan.admin.shared.infrastructure.idgen

import org.babyfish.jimmer.sql.meta.UserIdGenerator
import kotlin.concurrent.Volatile

/**
 * 标准的 Snowflake ID 生成器（同步实现）。
 * 实现 Jimmer 的 UserIdGenerator 接口，可作为实体 ID 生成器使用。
 *
 * 性能说明：
 * 对于 Snowflake 算法，synchronized 比 CAS 更优，因为临界区非常短（仅位运算）。
 * CAS 在高并发下会导致活锁和高 GC 压力，原因是对象分配和重试循环。
 *
 * 本类实现了单例模式，支持自定义 workerId 和 datacenterId，
 * 并保证线程安全的 ID 生成。
 *
 * Snowflake 算法生成的 ID 结构如下：
 * <pre>
 * 0 - 时间戳 - 数据中心ID - 机器ID - 序列号
 * </pre>
 *
 * @author faustofan
 * @since 2021-01-01
 */
class SnowflakeIdGenerator private constructor(workerId: Long, datacenterId: Long) : UserIdGenerator<Long> {

    /**
     * 实现 Jimmer UserIdGenerator 接口
     * 生成下一个唯一 ID
     */
    override fun generate(entityType: Class<*>): Long {
        return nextId()
    }
    /** 当前实例的工作机器ID  */
    private val workerId: Long

    /** 当前实例的数据中心ID  */
    private val datacenterId: Long

    // ---------------- 可变状态 ----------------
    /** 上一次生成ID的时间戳  */
    private var lastTimestamp = -1L

    /** 当前毫秒内的序列号  */
    private var sequence = 0L

    /**
     * 构造方法，校验 workerId 和 datacenterId 合法性。
     *
     * @param workerId 工作机器ID
     * @param datacenterId 数据中心ID
     * @throws IllegalArgumentException 参数超出范围
     */
    init {
        require(workerId in 0..MAX_WORKER_ID) {
            String.format(
                "Worker Id can't be greater than %d or less than 0",
                MAX_WORKER_ID
            )
        }
        require(datacenterId in 0..MAX_DATACENTER_ID) {
            String.format(
                "Datacenter Id can't be greater than %d or less than 0",
                MAX_DATACENTER_ID
            )
        }

        this.workerId = workerId
        this.datacenterId = datacenterId
    }

    /**
     * 线程安全地生成下一个唯一ID。
     *
     * @return 下一个唯一ID（long 类型）
     * @throws IllegalStateException 时钟回拨时抛出异常
     */
    @Synchronized
    fun nextId(): Long {
        var timestamp = System.currentTimeMillis()

        // 检查时钟回拨
        check(timestamp >= lastTimestamp) {
            String.format(
                "Clock moved backwards. Refusing to generate id for %d milliseconds",
                lastTimestamp - timestamp
            )
        }

        // 同一毫秒内，序列号递增
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) and SEQUENCE_MASK
            // 序列号溢出，等待下一毫秒
            if (sequence == 0L) {
                timestamp = waitNextMillis(lastTimestamp)
            }
        } else {
            // 新的毫秒，序列号重置
            sequence = 0L
        }

        lastTimestamp = timestamp

        // 组装ID
        return (((timestamp - EPOCH) shl TIMESTAMP_SHIFT.toInt())
                or (datacenterId shl DATACENTER_ID_SHIFT.toInt())
                or (workerId shl WORKER_ID_SHIFT.toInt())
                or sequence)
    }

    /**
     * 等待直到进入下一个毫秒。
     *
     * @param lastTimestamp 上一次的时间戳
     * @return 新的时间戳
     */
    private fun waitNextMillis(lastTimestamp: Long): Long {
        var timestamp = System.currentTimeMillis()
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis()
        }
        return timestamp
    }

    companion object {
        /** 单例实例  */
        @Volatile
        private var INSTANCE: SnowflakeIdGenerator? = null

        /**
         * 初始化单例实例。
         *
         * @param workerId 工作机器ID（可为 null，默认从系统属性获取或为 0）
         * @param datacenterId 数据中心ID（可为 null，默认从系统属性获取或为 0）
         */
        @Synchronized
        fun init(workerId: Long?, datacenterId: Long?) {
            if (INSTANCE != null) return
            val w = workerId ?: workerIdFromSystem
            val d = datacenterId ?: datacenterIdFromSystem
            INSTANCE = SnowflakeIdGenerator(w, d)
        }

        val instance: SnowflakeIdGenerator?
            /**
             * 获取单例实例。
             * 若未初始化，则自动使用默认参数初始化。
             *
             * @return SnowflakeIdGenerator 单例
             */
            get() {
                if (INSTANCE == null) {
                    synchronized(SnowflakeIdGenerator::class.java) {
                        if (INSTANCE == null) {
                            init(null, null)
                        }
                    }
                }
                return INSTANCE
            }

        private val workerIdFromSystem: Long
            /**
             * 从系统属性获取 workerId。
             *
             * @return workerId，默认 0
             */
            get() {
                val s = System.getProperty("workerId")
                return s?.toLong() ?: 0L
            }

        private val datacenterIdFromSystem: Long
            /**
             * 从系统属性获取 datacenterId。
             *
             * @return datacenterId，默认 0
             */
            get() {
                val s = System.getProperty("datacenterId")
                return s?.toLong() ?: 0L
            }

        // ---------------- 配置参数 ----------------
        /** 起始时间戳（2021-01-01 UTC）  */
        private const val EPOCH = 1609459200000L

        /** 工作机器ID占用的位数  */
        private const val WORKER_ID_BITS = 5L

        /** 数据中心ID占用的位数  */
        private const val DATACENTER_ID_BITS = 5L

        /** 序列号占用的位数  */
        private const val SEQUENCE_BITS = 12L

        /** 最大工作机器ID  */
        private const val MAX_WORKER_ID = (-1L shl WORKER_ID_BITS.toInt()).inv()

        /** 最大数据中心ID  */
        private const val MAX_DATACENTER_ID = (-1L shl DATACENTER_ID_BITS.toInt()).inv()

        /** 序列号掩码  */
        private const val SEQUENCE_MASK = (-1L shl SEQUENCE_BITS.toInt()).inv()

        /** 工作机器ID左移位数  */
        private const val WORKER_ID_SHIFT: Long = SEQUENCE_BITS

        /** 数据中心ID左移位数  */
        private const val DATACENTER_ID_SHIFT: Long = SEQUENCE_BITS + WORKER_ID_BITS

        /** 时间戳左移位数  */
        private const val TIMESTAMP_SHIFT: Long = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS
    }
}