package org.liuyehcf.compile.engine.expression.test.core;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

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
