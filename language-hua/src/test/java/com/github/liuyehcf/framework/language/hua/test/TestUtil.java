package com.github.liuyehcf.framework.language.hua.test;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.LRCompiler;
import com.github.liuyehcf.framework.language.hua.compile.HuaCompiler;
import com.github.liuyehcf.framework.language.hua.core.IntermediateInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * 测试
 *
 * @author hechenfeng
 * @date 2018/5/31
 */
public class TestUtil {

    private static LRCompiler<IntermediateInfo> compiler;

    static LRCompiler<IntermediateInfo> getCompiler() {
        if (compiler == null) {
            compiler = HuaCompiler.getHuaCompiler();
        }
        return compiler;
    }

    static void test(String text, String expected) {
        test(text, expected, false);
    }

    static void testAndPrintCode(String text, String expected) {
        test(text, expected, true);
    }

    private static void test(String text, String expected, boolean print) {
        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        if (result.getError() != null) {
            result.getError().printStackTrace();
        }
        assertTrue(result.isSuccess());
        if (print) {
            System.out.println(result.getResult().toReadableString());
        }
        assertEquals(expected, result.getResult().getMethodInfoTable().toSimpleJSONString());
    }
}