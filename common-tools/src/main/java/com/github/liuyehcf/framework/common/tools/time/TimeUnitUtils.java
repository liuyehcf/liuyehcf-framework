package com.github.liuyehcf.framework.common.tools.time;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;

import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2020/2/16
 */
public abstract class TimeUnitUtils {

    public static void sleep(long timeout, TimeUnit timeUnit) {
        Assert.assertNotNull(timeUnit, "timeUnit");

        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
