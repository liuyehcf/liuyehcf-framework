package org.liuyehcf.compile.engine.expression.utils;

import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.Option;

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
