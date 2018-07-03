package org.liuyehcf.compile.engine.hua.test;

import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.compile.HuaCompiler;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.compile.HuaCompiler.HUA_PATH_PROPERTY;


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
            System.setProperty(HUA_PATH_PROPERTY, "./src/main/resources/");

            long start, end;
            start = System.currentTimeMillis();

            compiler = HuaCompiler.getHuaCompiler();

            end = System.currentTimeMillis();
            System.out.println("build HuaCompiler consume " + (end - start) / 1000 + "s");

            assertTrue(compiler.isLegal());
        }
        return compiler;
    }

    static void test(String text, String expected) {
        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        if (result.getError() != null) {
            result.getError().printStackTrace();
        }
        assertTrue(result.isSuccess());
        assertEquals(expected, result.getResult().getMethodInfoTable().toSimpleJSONString());
    }

}