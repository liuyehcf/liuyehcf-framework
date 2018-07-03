package org.liuyehcf.compile.engine.hua.test;

import org.junit.Test;

import static org.liuyehcf.compile.engine.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestWhile {

    @Test
    public void testWhile() {
        String text = "void func1(int i){\n" +
                "\twhile(i<10){\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        test(text, "{\"func1(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testWhileNestedIfThen() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\tif(i>5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":9,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":7,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testWhileNestedIfThenElse() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\tif(i>5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}else{\n" +
                "\t\t\ti+=2;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":14,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":8,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testWhileNestedWhile() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\twhile(i>5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":10,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":8,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":3,\"name\":\"_goto\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testWhileNestedDoWhile() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\tdo{\n" +
                "\t\t\ti++;\n" +
                "\t\t}while(i>5);\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":9,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":3,\"name\":\"_if_icmpgt\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testWhileNestedFor() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\tfor(;i<5;){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":10,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":8,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":3,\"name\":\"_goto\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}");
    }

}
