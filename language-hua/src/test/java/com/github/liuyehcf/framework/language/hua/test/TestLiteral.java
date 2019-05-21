package com.github.liuyehcf.framework.language.hua.test;

import org.junit.Test;

import static com.github.liuyehcf.framework.language.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestLiteral {

    @Test
    public void testBooleanLiteral() {
        String text = "void func() {\n" +
                "\tboolean a=true;\n" +
                "\tboolean b=false;\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharLiteral() {
        String text = "void func() {\n" +
                "\tchar a='a';\n" +
                "\ta='1';\n" +
                "\ta='\0';\n" +
                "\ta='\n';\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntLiteral() {
        String text = "void func() {\n" +
                "\tint a=0;\n" +
                "\ta=100;\n" +
                "\ta=-100;\n" +
                "\ta=0xffff;\n" +
                "\ta=0767;\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":100},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":100},{\"name\":\"_ineg\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":65535},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":503},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongLiteral() {
        String text = "void func() {\n" +
                "\tlong a=0L;\n" +
                "\ta=100L;\n" +
                "\ta=-100L;\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_lconst\",\"value\":0},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_lconst\",\"value\":100},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_lconst\",\"value\":100},{\"name\":\"_lneg\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatLiteral() {
        String text = "void func() {\n" +
                "\tfloat a=0f;\n" +
                "\ta=1f;\n" +
                "\ta=1.f;\n" +
                "\ta=.0f;\n" +
                "\ta=.123f;\n" +
                "\ta=1e9f;\n" +
                "\ta=0e-3f;\n" +
                "\ta=3.3e+3f;\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_fconst\",\"value\":0.0},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_fconst\",\"value\":1.0},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_fconst\",\"value\":1.0},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_fconst\",\"value\":0.0},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_fconst\",\"value\":0.123},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_fconst\",\"value\":1.0E9},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_fconst\",\"value\":0.0},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_fconst\",\"value\":3300.0},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleLiteral() {
        String text = "void func() {\n" +
                "\tdouble a=0d;\n" +
                "\ta=1d;\n" +
                "\ta=1.d;\n" +
                "\ta=.0d;\n" +
                "\ta=.123d;\n" +
                "\ta=1e9d;\n" +
                "\ta=0e-3d;\n" +
                "\ta=3.3e+3d;\n" +
                "\ta=1;\n" +
                "\ta=1.;\n" +
                "\ta=.0;\n" +
                "\ta=.123;\n" +
                "\ta=1e9;\n" +
                "\ta=0e-3;\n" +
                "\ta=3.3e+3;\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_dconst\",\"value\":0.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":1.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":1.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":0.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":0.123},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":1.0E9},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":0.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":3300.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":1.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":0.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":0.123},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":1.0E9},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":0.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_dconst\",\"value\":3300.0},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testStringLiteral() {
        String text = "void func() {\n" +
                "\tchar[] c=\"abcdefg\";\n" +
                "}";

        test(text, "{\"func()\":[{\"constantPoolOffset\":30,\"name\":\"_ldc\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_return\"}]}");
    }
}
