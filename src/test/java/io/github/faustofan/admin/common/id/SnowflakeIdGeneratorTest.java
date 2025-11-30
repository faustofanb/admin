package io.github.faustofan.admin.common.id;

import io.github.faustofan.admin.common.SnowflakeIdGenerator;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class SnowflakeIdGeneratorTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instance = SnowflakeIdGenerator.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @DisplayName("显式初始化")
    void explicitInitTest() {
        SnowflakeIdGenerator.init(3L, 17L);
        SnowflakeIdGenerator gen = SnowflakeIdGenerator.getInstance();
        assertTrue(gen.nextId() > 0);
    }

    @Test
    @DisplayName("系统参数初始化")
    void systemPropertyInitTest() {
        System.setProperty("workerId", "2");
        System.setProperty("datacenterId", "5");
        SnowflakeIdGenerator gen = SnowflakeIdGenerator.getInstance();
        assertTrue(gen.nextId() > 0);
        System.clearProperty("workerId");
        System.clearProperty("datacenterId");
    }

    @Test
    @DisplayName("默认值初始化")
    void defaultInitTest() {
        SnowflakeIdGenerator gen = SnowflakeIdGenerator.getInstance();
        assertTrue(gen.nextId() > 0);
    }

    @Test
    @DisplayName("单例保证")
    void singletonTest() {
        SnowflakeIdGenerator gen1 = SnowflakeIdGenerator.getInstance();
        SnowflakeIdGenerator gen2 = SnowflakeIdGenerator.getInstance();
        assertSame(gen1, gen2);
    }

    @Test
    @DisplayName("生成ID递增性")
    void increasingTest() {
        SnowflakeIdGenerator gen = SnowflakeIdGenerator.getInstance();
        long last = -1;
        for (int i = 0; i < 1000; i++) {
            long id = gen.nextId();
            assertTrue(id > last);
            last = id;
        }
    }

    @Test
    @DisplayName("高并发生成无重复")
    void concurrentTest() throws Exception {
        SnowflakeIdGenerator gen = SnowflakeIdGenerator.getInstance();
        // 增加并发量验证 synchronized 性能
        int threads = 50;
        int perThread = 5000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Set<Long> set = ConcurrentHashMap.newKeySet();
        CountDownLatch latch = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            executor.submit(() -> {
                try {
                    for (int i = 0; i < perThread; i++) {
                        set.add(gen.nextId());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS); // 防止死锁挂住测试
        executor.shutdown();

        assertEquals(threads * perThread, set.size(), "并发生成存在重复或丢失");
    }

    @Test
    @DisplayName("序列号溢出进入下一毫秒")
    void sequenceOverflowTest() throws Exception {
        SnowflakeIdGenerator gen = SnowflakeIdGenerator.getInstance();

        // 获取内部字段
        Field lastTsField = SnowflakeIdGenerator.class.getDeclaredField("lastTimestamp");
        lastTsField.setAccessible(true);
        Field seqField = SnowflakeIdGenerator.class.getDeclaredField("sequence");
        seqField.setAccessible(true);

        // 手动修改状态：设置 sequence 为最大值 4095
        long now = System.currentTimeMillis();
        lastTsField.setLong(gen, now);
        seqField.setLong(gen, 4095L);

        long id1 = gen.nextId();
        // 因为 seq 溢出，nextId 内部会等待直到下一毫秒
        // 所以新生成的 id1 的 timestamp 应该 > now
        long timestamp1 = (id1 >> 22) + 1609459200000L;

        assertTrue(timestamp1 > now, "序列号溢出后应进入下一毫秒");
    }

    @Test
    @DisplayName("时间回拨检测")
    void timeRollbackTest() throws Exception {
        SnowflakeIdGenerator gen = SnowflakeIdGenerator.getInstance();

        Field lastTsField = SnowflakeIdGenerator.class.getDeclaredField("lastTimestamp");
        lastTsField.setAccessible(true);

        // 模拟回拨：将 lastTimestamp 修改为未来时间
        lastTsField.setLong(gen, System.currentTimeMillis() + 5000);

        IllegalStateException ex = assertThrows(IllegalStateException.class, gen::nextId);
        assertTrue(ex.getMessage().contains("Clock moved backwards"));
    }
}