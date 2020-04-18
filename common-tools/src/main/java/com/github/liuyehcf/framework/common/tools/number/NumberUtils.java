package com.github.liuyehcf.framework.common.tools.number;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;

import java.util.Collection;

/**
 * @author hechenfeng
 * @date 2020/2/14
 */
public abstract class NumberUtils {

    public static final long THOUSAND = 1000;
    public static final long MILLION = THOUSAND * THOUSAND;
    public static final long BILLION = MILLION * THOUSAND;
    public static final long TRILLION = BILLION * THOUSAND;

    public static final long K = 1024;
    public static final long M = K * K;
    public static final long G = K * M;
    public static final long T = K * G;

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

    public static double average(Collection<? extends Number> numbers) {
        Assert.assertNotNull(numbers, "numbers");

        double total = 0;

        for (Number number : numbers) {
            Assert.assertNotNull(number, "number");

            total += number.doubleValue();
        }

        return total / numbers.size();
    }

    public static double standardDeviation(Collection<? extends Number> numbers, double average) {
        Assert.assertNotNull(numbers, "numbers");

        double total = 0;

        for (Number number : numbers) {
            Assert.assertNotNull(number, "number");

            double diff = number.doubleValue() - average;
            total += diff * diff;
        }

        return Math.sqrt(total / numbers.size());
    }

    public static double standardDeviation(Collection<? extends Number> numbers) {
        return standardDeviation(numbers, average(numbers));
    }
}
