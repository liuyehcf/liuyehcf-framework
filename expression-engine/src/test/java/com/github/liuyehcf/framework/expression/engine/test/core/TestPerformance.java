package com.github.liuyehcf.framework.expression.engine.test.core;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class TestPerformance extends TestBase {
    @Test
    public void case1() {
        Map<String, Object> env = Maps.newHashMap();
        env.put("a", 1);
        env.put("c", 2);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            ExpressionEngine.execute("(a < c && (3<4 || math.abs(5) !=7) || a < c && (3<4 || math.abs(5) !=7)) && a < c && (3<4 || math.abs(5) !=7)", env);
        }

        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
