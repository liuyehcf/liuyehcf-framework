package com.github.liuyehcf.framework.language.hua.test;

import org.junit.Test;

import static com.github.liuyehcf.framework.language.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestUnaryOperator {
    @Test
    public void testBitReverse() {
        String text = "void func(int a, long b) {\n" +
                "\ta=~a;\n" +
                "\tb=~b;\n" +
                "}";

        test(text, "{\"func(int,long)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":-1},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lconst\",\"value\":-1},{\"name\":\"_lxor\"},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testPreDecrement() {
        String text = "void func(int i) {\n" +
                "\tint j=--i;\n" +
                "\n" +
                "\tj=(1+--i)+(--j+3);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iadd\"},{\"increment\":-1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testPreIncrement() {
        String text = "void func(int i) {\n" +
                "\tint j=++i;\n" +
                "\n" +
                "\tj=(++i-1)+(++j+3);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_istore\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testPostDecrement() {
        String text = "void func(int i) {\n" +
                "\tint j=i--;\n" +
                "\n" +
                "\tj=(1+i--)+(j--+3);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iload\",\"order\":0},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":1},{\"increment\":-1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testPostIncrement() {
        String text = "void func(int i) {\n" +
                "\tint j=i++;\n" +
                "\n" +
                "\tj=((i++)+1)+(3+j++);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iload\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSimpleIncrementDecrement() {
        String text = "void func(int i) {\n" +
                "\t++i;\n" +
                "\ti++;\n" +
                "\t--i;\n" +
                "\ti--;\n" +
                "}";

        test(text, "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testReverseConditionExpression1() {
        String text = "void func(boolean a, int i, int j) {\n" +
                "\tboolean c=!(i>=j);\n" +
                "}";

        test(text, "{\"func(boolean,int,int)\":[{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testReverseConditionExpression2() {
        String text = "void func(boolean a, boolean b, boolean c) {\n" +
                "\tboolean d=!(a&&b||c);\n" +
                "}";

        test(text, "{\"func(boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testReverseConditionExpression3() {
        String text = "void func1(boolean a, boolean b, int i, int j) {\n" +
                "\tboolean c=!(a&&b);\n" +
                "}\n";

        test(text, "{\"func1(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testReverseConditionExpression4() {
        String text = "void func1(boolean a, boolean b, int i, int j) {\n" +
                "\tboolean c=i>=j&&!(i<j||i>3)&&j>=5||a&&b;\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, int i, int j) {\n" +
                "\tboolean c;\n" +
                "\tc=!((i>=j&&(i<j||i>3))&&(!(j>=5)||a&&!b));\n" +
                "}";

        test(text, "{\"func1(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":12,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":12,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":12,\"name\":\"_if_icmpgt\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func2(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":16,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":9,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":16,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":18,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testReverseConditionExpression5() {
        String text = "void func1(boolean a, boolean b, int i, int j) {\n" +
                "\tboolean c=!true;\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, int i, int j) {\n" +
                "\tboolean c;\n" +
                "\tc=!a;\n" +
                "}";

        test(text, "{\"func1(boolean,boolean,int,int)\":[{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":5,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func2(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":5,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}");
    }
}
