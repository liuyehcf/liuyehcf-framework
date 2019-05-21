package com.github.liuyehcf.framework.expression.engine.test.function.collection;

import com.github.liuyehcf.framework.expression.engine.ExpressionEngine;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestCollectionIncludeFunction extends TestBase {
    @Test
    public void case1() {
        boolean result = ExpressionEngine.execute("collection.include([\"aaa\",\"bbb\",\"ccc\"],\"aaa\")");

        assertTrue(result);
    }

    @Test
    public void case2() {
        boolean result = ExpressionEngine.execute("collection.include([\"aaa\",\"bbb\",\"ccc\"],\"zzz\")");

        assertFalse(result);
    }

    @Test
    public void case3() {
        boolean result = ExpressionEngine.execute("collection.include([],\"zzz\")");

        assertFalse(result);
    }

    @Test
    public void case4() {
        boolean result = ExpressionEngine.execute("collection.include(null,\"zzz\")");

        assertFalse(result);
    }

    @Test
    public void case5() {
        Map<String, Object> env = EnvBuilder.builder().put("a", new boolean[]{true}).build();
        boolean result = ExpressionEngine.execute("collection.include(a,true)", env);
        assertTrue(result);

        env = EnvBuilder.builder().put("a", new boolean[0]).build();
        result = ExpressionEngine.execute("collection.include(a,true)", env);
        assertFalse(result);
    }

    @Test
    public void case6() {
        Map<String, Object> env = EnvBuilder.builder().put("a", Lists.newArrayList(1L, 2L, 3L)).build();
        boolean result = ExpressionEngine.execute("collection.include(a,1)", env);
        assertTrue(result);

        result = ExpressionEngine.execute("collection.include(a,4)", env);
        assertFalse(result);

        result = ExpressionEngine.execute("collection.include(a,1)");
        assertFalse(result);
    }

    @Test
    public void case7() {
        Map<String, Object> env = EnvBuilder.builder().put("a", Sets.newHashSet(1L, 2L, 3L)).build();
        boolean result = ExpressionEngine.execute("collection.include(a,1)", env);
        assertTrue(result);

        result = ExpressionEngine.execute("collection.include(a,4)", env);
        assertFalse(result);

        result = ExpressionEngine.execute("collection.include(a,1)");
        assertFalse(result);
    }
}
