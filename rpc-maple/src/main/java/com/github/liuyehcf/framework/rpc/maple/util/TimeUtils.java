package com.github.liuyehcf.framework.rpc.maple.util;

import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/3/23
 */
public class TimeUtils {
    public static void sleepMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
