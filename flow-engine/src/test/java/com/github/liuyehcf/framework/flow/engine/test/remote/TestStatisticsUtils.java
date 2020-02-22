package com.github.liuyehcf.framework.flow.engine.test.remote;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.util.StatisticsUtils;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/9/12
 */
public class TestStatisticsUtils extends TestRemoteBase {

    @Test
    public void testZero() {
        Assert.assertTrue(StatisticsUtils.isAccept(0, 0));
        Assert.assertFalse(StatisticsUtils.isReject(0, 0));
    }

    @Test
    public void testOne() {
        Assert.assertFalse(StatisticsUtils.isAccept(0, 1));
        Assert.assertTrue(StatisticsUtils.isAccept(1, 1));

        Assert.assertFalse(StatisticsUtils.isReject(0, 1));
        Assert.assertTrue(StatisticsUtils.isReject(1, 1));
    }

    @Test
    public void testGreaterThan2() {
        for (int totalNum = 2; totalNum < 1000; totalNum++) {
            for (int acceptNum = 0; acceptNum <= totalNum; acceptNum++) {
                int rejectNum = totalNum - acceptNum;
                if (acceptNum > rejectNum) {
                    Assert.assertTrue(StatisticsUtils.isAccept(acceptNum, totalNum));
                    Assert.assertFalse(StatisticsUtils.isReject(rejectNum, totalNum));
                } else {
                    Assert.assertFalse(StatisticsUtils.isAccept(acceptNum, totalNum));
                    Assert.assertTrue(StatisticsUtils.isReject(rejectNum, totalNum));
                }
            }
        }
    }

    @Test
    public void testBoundary() {
        int num = 10000;

        for (int i = 0; i < num * 2; i++) {
            Assert.assertTrue(StatisticsUtils.isAccept(RANDOM.nextInt(num * 2) - num, RANDOM.nextInt(num * 2) - num * 2));
            Assert.assertFalse(StatisticsUtils.isReject(RANDOM.nextInt(num * 2) - num, RANDOM.nextInt(num * 2) - num * 2));
        }
    }
}
