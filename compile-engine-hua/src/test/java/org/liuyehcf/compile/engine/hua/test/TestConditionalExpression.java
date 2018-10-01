package org.liuyehcf.compile.engine.hua.test;

import org.junit.Test;

import static org.liuyehcf.compile.engine.hua.test.TestUtil.test;

/**
 * @author chenlu
 * @date 2018/10/1
 */
public class TestConditionalExpression {
    @Test
    public void test1() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = ((a < b) ? !(c < d) : !(e >= f));\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":8,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":15,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }

    @Test
    public void test2() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = (((a < b) ? !(c < d) : !(e >= f)) || e==f);\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":8,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":15,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":20,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":22,\"name\":\"_if_icmpne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }

    @Test
    public void test3() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = (e==f || ((a < b) ? !(c < d) : !(e >= f))) ;\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":20,\"name\":\"_if_icmpeq\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":13,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":11,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":18,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":22,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }

    @Test
    public void test4() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = (true || a!=b) ;\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":5,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_if_icmpeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":8,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }

    @Test
    public void test5() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = ( a<=b||false) ;\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":8,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }

    @Test
    public void test6() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = (((a < b) ? !(c < d) : !(e >= f)) && e != f);\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":8,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":15,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":22,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":22,\"name\":\"_if_icmpeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }

    @Test
    public void test7() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = (e >= f && ((a < b) ? !(c < d) : !(e >= f))) ;\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":22,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":13,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":11,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iload\",\"order\":5},{\"codeOffset\":18,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":22,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }

    @Test
    public void test8() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = (false && a==b) ;\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_if_icmpne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":8,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }

    @Test
    public void test9() {
        String text = "void func(int a, int b, int c, int d, int e, int f) {\n" +
                "\tboolean g = ( a>b&&true) ;\n" +
                "}";

        test(text, "{\"func(int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":8,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_return\"}]}");
    }
}
