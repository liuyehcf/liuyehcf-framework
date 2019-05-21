package com.github.liuyehcf.framework.language.hua.test;

import org.junit.Test;

import static com.github.liuyehcf.framework.language.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestComparableOperator {

    @Test
    public void testChar() {
        String text = "void func(char i, char j, boolean c) {\n" +
                "\tc=i==j;\n" +
                "\tc=i!=j;\n" +
                "\tc=i<j;\n" +
                "\tc=i<=j;\n" +
                "\tc=i>j;\n" +
                "\tc=i>=j;\n" +
                "}";

        test(text, "{\"func(char,char,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":12,\"name\":\"_if_icmpeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":19,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":20,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":26,\"name\":\"_if_icmpgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":27,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":33,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":34,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":40,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":41,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testInt() {
        String text = "void func(int i, int j, boolean c) {\n" +
                "\tc=i==j;\n" +
                "\tc=i!=j;\n" +
                "\tc=i<j;\n" +
                "\tc=i<=j;\n" +
                "\tc=i>j;\n" +
                "\tc=i>=j;\n" +
                "}";

        test(text, "{\"func(int,int,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":12,\"name\":\"_if_icmpeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":19,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":20,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":26,\"name\":\"_if_icmpgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":27,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":33,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":34,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":40,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":41,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLong() {
        String text = "void func(long i, long j, boolean c) {\n" +
                "\tc=i==j;\n" +
                "\tc=i!=j;\n" +
                "\tc=i<j;\n" +
                "\tc=i<=j;\n" +
                "\tc=i>j;\n" +
                "\tc=i>=j;\n" +
                "}";

        test(text, "{\"func(long,long,boolean)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":22,\"name\":\"_ifge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":30,\"name\":\"_ifgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":31,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":38,\"name\":\"_ifle\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":39,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":46,\"name\":\"_iflt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":47,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloat() {
        String text = "void func(float i, float j, boolean c) {\n" +
                "\tc=i==j;\n" +
                "\tc=i!=j;\n" +
                "\tc=i<j;\n" +
                "\tc=i<=j;\n" +
                "\tc=i>j;\n" +
                "\tc=i>=j;\n" +
                "}";

        test(text, "{\"func(float,float,boolean)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fcmp\"},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fcmp\"},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fcmp\"},{\"codeOffset\":22,\"name\":\"_ifge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fcmp\"},{\"codeOffset\":30,\"name\":\"_ifgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":31,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fcmp\"},{\"codeOffset\":38,\"name\":\"_ifle\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":39,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fcmp\"},{\"codeOffset\":46,\"name\":\"_iflt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":47,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDouble() {
        String text = "void func(double i, double j, boolean c) {\n" +
                "\tc=i==j;\n" +
                "\tc=i!=j;\n" +
                "\tc=i<j;\n" +
                "\tc=i<=j;\n" +
                "\tc=i>j;\n" +
                "\tc=i>=j;\n" +
                "}";

        test(text, "{\"func(double,double,boolean)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dcmp\"},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dcmp\"},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dcmp\"},{\"codeOffset\":22,\"name\":\"_ifge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dcmp\"},{\"codeOffset\":30,\"name\":\"_ifgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":31,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dcmp\"},{\"codeOffset\":38,\"name\":\"_ifle\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":39,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dcmp\"},{\"codeOffset\":46,\"name\":\"_iflt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":47,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }
}
