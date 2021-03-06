package com.github.liuyehcf.framework.expression.engine.test.utils;

import com.github.liuyehcf.framework.common.tools.collection.EnvBuilder;
import com.github.liuyehcf.framework.expression.engine.test.TestBase;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class TestEnvBuilder extends TestBase {
    @Test
    public void case1() {
        Map<String, Object> map = EnvBuilder.builder().put("a", 1).build();
        assertEquals(1, map.get("a"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void case2() {
        Map<String, Object> map = EnvBuilder.builder().put("a.b.c", 1).build();
        assertEquals(1, ((Map<String, Object>) ((Map<String, Object>) map.get("a")).get("b")).get("c"));
    }

    @Test
    public void case3() {
        expectedException(
                () -> EnvBuilder.builder().put("a.b", 1).put("a.b.c", 3).build(),
                RuntimeException.class,
                "a.b='java.lang.Integer' is incompatible with 'java.util.Map<String, Object>'");

        expectedException(
                () -> EnvBuilder.builder().put("a", 1).put("a.b.c", 3).build(),
                RuntimeException.class,
                "a='java.lang.Integer' is incompatible with 'java.util.Map<String, Object>'");

        EnvBuilder.builder().put("a", null).put("a.b", 3).build();
        EnvBuilder.builder().put("a", Maps.newHashMap()).put("a", 3).build();
    }
}
