package com.github.liuyehcf.framework.common.tools.number;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;

/**
 * 1bit(not use) / 41bits(timestamp) / 10bits(workerId) / 12bits(sequence)
 *
 * @author hechenfeng
 * @date 2020/2/17
 */
public class SnowFlakeIDGenerator implements IDGenerator {

    private static final long TWEPOCH = 1288834974657L;

    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static final long WORKER_ID_BITS = 10L;
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 10 bits
     */
    private final int workerId;

    /**
     * 12 bits
     */
    private int sequence;

    private long lastTimestamp = -1L;

    public SnowFlakeIDGenerator(int workerId) {
        Assert.assertTrue(workerId >= 0 && workerId <= MAX_WORKER_ID,
                () -> String.format("workerId must between 1 and %d", MAX_WORKER_ID));

        this.workerId = workerId;
    }

    @Override
    public synchronized long nextId() {
        long timestamp = timeGen();

        Assert.assertTrue(timestamp >= lastTimestamp, "clock is moving backwards");

        if (timestamp == lastTimestamp) {
            sequence++;
            if (sequence == MAX_SEQUENCE + 1) {
                sequence = 0;
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT) |
                (workerId << WORKER_ID_SHIFT) |
                sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
