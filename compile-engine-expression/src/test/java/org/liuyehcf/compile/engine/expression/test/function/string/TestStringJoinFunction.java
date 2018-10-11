package org.liuyehcf.compile.engine.expression.test.function.string;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class TestStringJoinFunction extends TestBase {
    @Test
    public void case1() {
        Map<String, Object> env = Maps.newHashMap();
        env.put("a", new String[]{"a", "b", "c"});
        String result = ExpressionEngine.execute("string.join(a,\"#\")", env);

        assertEquals("a#b#c", result);
    }

    @Test
    public void case2() {
        Map<String, Object> env = Maps.newHashMap();
        env.put("a", new String[0]);
        String result = ExpressionEngine.execute("string.join(a,\"#\")", env);

        assertEquals("", result);

        env.put("a", Lists.<String>newArrayList());
        result = ExpressionEngine.execute("string.join(a,\"#\")", env);

        assertEquals("", result);
    }

    @Test
    public void case3() {
        Map<String, Object> env = Maps.newHashMap();
        env.put("a", new String[]{"a",});
        String result = ExpressionEngine.execute("string.join(a,\"#\")", env);

        assertEquals("a", result);
    }

    @Test
    public void case4() {
        String result = ExpressionEngine.execute("string.join(a,\"#\")");

        assertEquals("", result);
    }

    @Test
    public void case5() {
        String result = ExpressionEngine.execute("string.join(null,\"#\")");

        assertEquals("", result);
    }

    @Test
    public void case6() {
        String result = ExpressionEngine.execute("string.join([\"a\",\"b\",\"c\"],\"#\")");

        assertEquals("a#b#c", result);
    }

    @Test
    public void case7() {
        String result = ExpressionEngine.execute("string.join([true,false,1,2,1.3d],0)");

        assertEquals("true0false010201.3", result);
    }

    @Test
    public void case8() {
        String result = ExpressionEngine.execute("string.join([true,false,1,2,1.3d],null)");

        assertEquals("truefalse121.3", result);
    }
}