package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.support;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hechenfeng
 * @date 2019/9/12
 */
public class LeaderStatusSupport {

    /**
     * previous request id
     */
    private final AtomicLong previousRequestId = new AtomicLong();

    /**
     * request id
     */
    private final AtomicLong requestId = new AtomicLong();

    /**
     * different from status 'inactive'
     * not active num means all status exception active
     */
    private final AtomicInteger notActiveNum = new AtomicInteger();

    public long getPreviousRequestId() {
        return previousRequestId.get();
    }

    public long getRequestId() {
        return requestId.get();
    }

    public long start() {
        notActiveNum.set(0);
        long newRequestId;

        while (!requestId.compareAndSet(requestId.get(), newRequestId = System.nanoTime())) ;

        return newRequestId;
    }

    public void finish() {
        notActiveNum.set(0);

        previousRequestId.set(requestId.get());

        requestId.set(0);
    }

    public int increase(long requestId) {
        if (Objects.equals(this.requestId.get(), requestId)) {
            notActiveNum.incrementAndGet();
        }
        return notActiveNum.get();
    }
}
