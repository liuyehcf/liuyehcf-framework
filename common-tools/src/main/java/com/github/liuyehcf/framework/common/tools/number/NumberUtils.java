package com.github.liuyehcf.framework.common.tools.number;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;

/**
 * @author hechenfeng
 * @date 2020/2/14
 */
public abstract class NumberUtils {

    public static final int _1K = 1000;
    public static final int _1M = _1K * _1K;
    public static final int _1G = _1K * _1M;
    public static final int _1T = _1K * _1G;

    /**
     * majority of totalNum
     * if totalNum = 3, then majority is 2
     * if totalNum = 4, then majority is 3
     * if totalNum = 5, then majority is 3
     * if totalNum = 6, then majority is 4
     */
    public static int majorityOf(int totalNum) {
        Assert.assertTrue(totalNum > 0, "totalNum");

        return (totalNum >> 1) + 1;
    }

    /**
     * minority of totalNum
     * if totalNum = 3, then minority is 1
     * if totalNum = 4, then minority is 2
     * if totalNum = 5, then minority is 2
     * if totalNum = 6, then minority is 3
     */
    public static int minorityOf(int totalNum) {
        return totalNum - majorityOf(totalNum);
    }

    /**
     * sub-majority of totalNum
     * if totalNum = 3, then sub-majority is 2
     * if totalNum = 4, then sub-majority is 2
     * if totalNum = 5, then sub-majority is 3
     * if totalNum = 6, then sub-majority is 3
     */
    public static int subMajorityOf(int totalNum) {
        Assert.assertTrue(totalNum > 0, "totalNum");

        return (totalNum + 1) >> 1;
    }
}
