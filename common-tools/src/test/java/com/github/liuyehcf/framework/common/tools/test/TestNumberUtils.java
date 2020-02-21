package com.github.liuyehcf.framework.common.tools.test;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2020/2/15
 */
public class TestNumberUtils {

    @Test
    public void test() {
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
}
