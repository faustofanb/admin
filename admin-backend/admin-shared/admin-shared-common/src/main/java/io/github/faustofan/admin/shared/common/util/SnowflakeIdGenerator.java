package io.github.faustofan.admin.shared.common.util;

/**
 * 标准的 Snowflake ID 生成器（同步实现）。
 * 实现 Jimmer 的 UserIdGenerator 接口，可作为实体 ID 生成器使用。
 * <p>性能说明：</p>
 *      对于 Snowflake 算法，synchronized 比 CAS 更优，因为临界区非常短（仅位运算）。
 *      CAS 在高并发下会导致活锁和高 GC 压力，原因是对象分配和重试循环。
 * <p>单例模式: </p>
 *      本类实现了单例模式，支持自定义 workerId 和 datacenterId，
 *      并保证线程安全的 ID 生成。
 * <p>ID 结构说明：</p>
 *      Snowflake 算法生成的 ID 结构如下：
 * <pre>
 *      0 - 时间戳 - 数据中心ID - 机器ID - 序列号
 * </pre>
 *
 */
public class SnowflakeIdGenerator {

    /** 当前实例的工作机器ID */
    private final long workerId;

    /** 当前实例的数据中心ID */
    private final long datacenterId;

    /** 上一次生成ID的时间戳 */
    private long lastTimestamp = -1L;

    /** 当前毫秒内的序列号 */
    private long sequence = 0L;

    private SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(
                String.format("Worker Id can't be greater than %d or less than 0", MAX_WORKER_ID)
            );
        }
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
            throw new IllegalArgumentException(
                String.format("Datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID)
            );
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 线程安全地生成下一个唯一ID。
     *
     * @return 下一个唯一ID（long 类型）
     * @throws IllegalStateException 时钟回拨时抛出异常
     */
    public synchronized Long nextId() {
        long timestamp = System.currentTimeMillis();

        // 检查时钟回拨
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException(
                String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp)
            );
        }

        // 同一毫秒内，序列号递增
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 序列号溢出，等待下一毫秒
            if (sequence == 0L) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 新的毫秒，序列号重置
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 组装ID
        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 等待直到进入下一个毫秒。
     *
     * @param lastTimestamp 上一次的时间戳
     * @return 新的时间戳
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    // ---------------- 单例实现 ----------------

    private static volatile SnowflakeIdGenerator INSTANCE;

    /**
     * 初始化单例实例。
     *
     * @param workerId 工作机器ID（可为 null，默认从系统属性获取或为 0）
     * @param datacenterId 数据中心ID（可为 null，默认从系统属性获取或为 0）
     */
    public static synchronized void init(Long workerId, Long datacenterId) {
        if (INSTANCE != null) return;
        long w = workerId != null ? workerId : getWorkerIdFromSystem();
        long d = datacenterId != null ? datacenterId : getDatacenterIdFromSystem();
        INSTANCE = new SnowflakeIdGenerator(w, d);
    }

    /**
     * 获取单例实例。
     * 若未初始化，则自动使用默认参数初始化。
     *
     * @return SnowflakeIdGenerator 单例
     */
    public static SnowflakeIdGenerator getInstance() {
        if (INSTANCE == null) {
            synchronized (SnowflakeIdGenerator.class) {
                if (INSTANCE == null) {
                    init(null, null);
                }
            }
        }
        return INSTANCE;
    }

    private static long getWorkerIdFromSystem() {
        String s = System.getProperty("workerId");
        return s != null ? Long.parseLong(s) : 0L;
    }

    private static long getDatacenterIdFromSystem() {
        String s = System.getProperty("datacenterId");
        return s != null ? Long.parseLong(s) : 0L;
    }

    // ---------------- 配置参数 ----------------

    /** 起始时间戳（2021-01-01 UTC） */
    private static final long EPOCH = 1609459200000L;

    /** 工作机器ID占用的位数 */
    private static final long WORKER_ID_BITS = 5L;

    /** 数据中心ID占用的位数 */
    private static final long DATACENTER_ID_BITS = 5L;

    /** 序列号占用的位数 */
    private static final long SEQUENCE_BITS = 12L;

    /** 最大工作机器ID */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /** 最大数据中心ID */
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    /** 序列号掩码 */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /** 工作机器ID左移位数 */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /** 数据中心ID左移位数 */
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /** 时间戳左移位数 */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
}
