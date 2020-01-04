package com.github.liuyehcf.framework.flow.engine.model;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class IDGenerator {

    private final AtomicLong count = new AtomicLong(1);

    private IDGenerator() {

    }

    public static IDGenerator create() {
        return new IDGenerator();
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public String generateNumberId() {
        return Long.toString(count.getAndIncrement());
    }
}
