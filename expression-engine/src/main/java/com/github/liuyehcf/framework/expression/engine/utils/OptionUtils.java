package com.github.liuyehcf.framework.expression.engine.utils;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.Option;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public class OptionUtils {
    public static boolean isCache() {
        return ExpressionEngine.getOptions().get(Option.USING_CACHE);
    }

    public static boolean isOptimize() {
        return ExpressionEngine.getOptions().get(Option.OPTIMIZE_CODE);
    }
}
