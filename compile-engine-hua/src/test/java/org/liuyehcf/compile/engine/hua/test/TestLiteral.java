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
public class TestLiteral {
    @Test
    public void testDecimalLiteral() {
        String text = "void func() {\n" +
                "\tint a=5;\n" +
                "\tlong b=+0L;\n" +
                "\ta+=-100;\n" +
                "\tb-=-0L;\n" +
                "\tb+=-10000L;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_lconst\",\"value\":0},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":100},{\"name\":\"_ineg\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lconst\",\"value\":0},{\"name\":\"_lneg\"},{\"name\":\"_lsub\"},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lconst\",\"value\":10000},{\"name\":\"_lneg\"},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testHexLiteral() {
        String text = "void func() {\n" +
                "\tint a=0X005,b=0xffff;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":65535},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testOctalLiteral() {
        String text = "void func() {\n" +
                "\tint a=0005,b=0765;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":501},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBooleanLiteral() {
        String text = "void func() {\n" +
                "\tboolean a=true,b=false;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharLiteral() {
        String text = "void func() {\n" +
                "\tchar c='1';\n" +
                "\tc='a';\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testStringLiteral() {
        String text = "void func() {\n" +
                "\tchar[] c=\"abcdefg\";\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"constantPoolOffset\":20,\"name\":\"_ldc\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
