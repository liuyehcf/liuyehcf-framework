package org.liuyehcf.compile.engine.hua.test;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.test.TestGrammar.getCompiler;

/**
 * @author chenlu
 * @date 2018/7/1
 */
public class TestReturn {
    @Test
    public void testBooleanReturn() {
        String text = "boolean func() {\n" +
                "\treturn true;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_ireturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharReturn() {
        String text = "char func() {\n" +
                "\treturn 'a';\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_ireturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIntReturn() {
        String text = "int func() {\n" +
                "\treturn 1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_ireturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLongReturn() {
        String text = "long func() {\n" +
                "\treturn 1L;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lreturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testFloatReturn() {
        String text = "float func() {\n" +
                "\treturn 1.0f;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_fconst\",\"value\":1.0},{\"name\":\"_freturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testDoubleReturn() {
        String text = "double func() {\n" +
                "\treturn 1.0d;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_dconst\",\"value\":1.0},{\"name\":\"_dreturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBooleanSingleArrayReturn() {
        String text = "boolean[] func() {\n" +
                "\treturn new boolean[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"boolean\"},{\"name\":\"_areturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharSingleArrayReturn() {
        String text = "char[] func() {\n" +
                "\treturn new char[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"char\"},{\"name\":\"_areturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIntSingleArrayReturn() {
        String text = "int[] func() {\n" +
                "\treturn new int[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_areturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLongSingleArrayReturn() {
        String text = "long[] func() {\n" +
                "\treturn new long[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"long\"},{\"name\":\"_areturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testFloatSingleArrayReturn() {
        String text = "float[] func() {\n" +
                "\treturn new float[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"float\"},{\"name\":\"_areturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testDoubleSingleArrayReturn() {
        String text = "double[] func() {\n" +
                "\treturn new double[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"double\"},{\"name\":\"_areturn\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
