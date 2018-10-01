package org.liuyehcf.compile.engine.hua.test;

import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.compile.HuaCompiler;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;

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