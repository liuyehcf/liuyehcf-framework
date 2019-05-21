package com.github.liuyehcf.framework.expression.engine.test.function.collection;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestCollectionSizeFunction extends TestBase {
    @Test
    public void case1() {
        long result = ExpressionEngine.execute("collection.size([\"aaa\",\"bbb\",string.substring(\"ccc\",1)])");

        assertEquals(3L, result);
    }


    @Test
    public void case2() {
        long result = ExpressionEngine.execute("collection.size([])");

        assertEquals(0L, result);
    }

    @Test
    public void case3() {
        long result = ExpressionEngine.execute("collection.size(null)");

        assertEquals(0L, result);
    }

    @Test
    public void case5() {
        Map<String, Object> env = EnvBuilder.builder().put("a", new boolean[]{true}).build();
        long result = ExpressionEngine.execute("collection.size(a)", env);
        assertEquals(1L, result);

        env = EnvBuilder.builder().put("a", new boolean[0]).build();
        result = ExpressionEngine.execute("collection.size(a)", env);
        assertEquals(0L, result);
    }

    @Test
    public void case6() {
        Map<String, Object> env = EnvBuilder.builder().put("a", Lists.newArrayList(1L, 2L, 3L)).build();
        long result = ExpressionEngine.execute("collection.size(a)", env);
        assertEquals(3L, result);

        result = ExpressionEngine.execute("collection.size(a)");
        assertEquals(0L, result);
    }

    @Test
    public void case7() {
        Map<String, Object> env = EnvBuilder.builder().put("a", Sets.newHashSet(1L, 2L, 3L)).build();
        long result = ExpressionEngine.execute("collection.size(a)", env);
        assertEquals(3L, result);

        result = ExpressionEngine.execute("collection.size(a)");
        assertEquals(0L, result);
    }
}
