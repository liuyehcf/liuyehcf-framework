package com.github.liuyehcf.framework.common.tools.test.number;

import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * @author hechenfeng
 * @date 2020/2/15
 */
public class TestNumberUtils {

    @Test
    public void testValue() {
        Assert.assertEquals(1000, NumberUtils._1K);
        Assert.assertEquals(1000 * 1000, NumberUtils._1M);
        Assert.assertEquals(1000 * 1000 * 1000, NumberUtils._1G);
        Assert.assertEquals(1000 * 1000 * 1000 * 1000L, NumberUtils._1T);

        Assert.assertEquals(1024, NumberUtils._1K_BINARY);
        Assert.assertEquals(1024 * 1024, NumberUtils._1M_BINARY);
        Assert.assertEquals(1024 * 1024 * 1024, NumberUtils._1G_BINARY);
        Assert.assertEquals(1024 * 1024 * 1024 * 1024L, NumberUtils._1T_BINARY);
    }

    @Test
    public void testMajorityMinority() {
        Assert.assertEquals(1, NumberUtils.majorityOf(1));
        Assert.assertEquals(2, NumberUtils.majorityOf(2));
        Assert.assertEquals(2, NumberUtils.majorityOf(3));
        Assert.assertEquals(3, NumberUtils.majorityOf(4));
        Assert.assertEquals(3, NumberUtils.majorityOf(5));
        Assert.assertEquals(4, NumberUtils.majorityOf(6));

        Assert.assertEquals(0, NumberUtils.minorityOf(1));
        Assert.assertEquals(0, NumberUtils.minorityOf(2));
        Assert.assertEquals(1, NumberUtils.minorityOf(3));
        Assert.assertEquals(1, NumberUtils.minorityOf(4));
        Assert.assertEquals(2, NumberUtils.minorityOf(5));
        Assert.assertEquals(2, NumberUtils.minorityOf(6));

        Assert.assertEquals(1, NumberUtils.subMajorityOf(1));
        Assert.assertEquals(1, NumberUtils.subMajorityOf(2));
        Assert.assertEquals(2, NumberUtils.subMajorityOf(3));
        Assert.assertEquals(2, NumberUtils.subMajorityOf(4));
        Assert.assertEquals(3, NumberUtils.subMajorityOf(5));
        Assert.assertEquals(3, NumberUtils.subMajorityOf(6));
    }

    @Test
    public void testAverage() {
        double average = NumberUtils.average(Collections.singletonList(NumberUtils._1T));
        Assert.assertEquals(NumberUtils._1T, average, 1e-10);

        average = NumberUtils.average(Sets.newHashSet(NumberUtils._1T, 0d));
        Assert.assertEquals(NumberUtils._1T / 2.0, average, 1e-10);

        average = NumberUtils.average(Lists.newArrayList(0, 1L, 2D));
        Assert.assertEquals(1, average, 1e-10);
    }

    @Test
    public void testStandardDeviation() {
        double standardDeviation = NumberUtils.standardDeviation(Collections.singletonList(NumberUtils._1T));
        Assert.assertEquals(0, standardDeviation, 1e-10);

        standardDeviation = NumberUtils.standardDeviation(Lists.newArrayList(0, 1L, 2D));
        Assert.assertEquals(Math.sqrt(2 / 3d), standardDeviation, 1e-10);
    }
}
