package com.github.liuyehcf.framework.rule.engine.util;

/**
 * @author hechenfeng
 * @date 2019/9/12
 */
public abstract class StatisticsUtils {

    public static boolean isAccept(int acceptNum, int totalNum) {
        if (totalNum <= 0
                || acceptNum >= totalNum) {
            return true;
        } else {
            return acceptNum >= ((totalNum >> 1) + 1);
        }
    }

    public static boolean isReject(int rejectNum, int totalNum) {
        if (totalNum <= 0) {
            return false;
        } else if (rejectNum >= totalNum) {
            return true;
        } else {
            return rejectNum >= ((totalNum + 1) >> 1);
        }
    }
}
