package org.liuyehcf.compile.engine.hua.test;

import org.junit.Test;

import static org.liuyehcf.compile.engine.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestDoWhile {

    @Test
    public void testDoWhile() {
        String text = "void func(int i) {\n" +
                "\tdo{\n" +
                "\t\ti=i+1;\n" +
                "\t}while(i<10);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmplt\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoWhileNestedIfThen() {
        String text = "void func(int i) {\n" +
                "\tdo{\n" +
                "\t\tif(i<2){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti=i+1;\n" +
                "\t}while(i<10);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"codeOffset\":4,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmplt\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoWhileNestedIfThenElse() {
        String text = "void func(int i) {\n" +
                "\tdo{\n" +
                "\t\tif(i<2){\n" +
                "\t\t\ti++;\n" +
                "\t\t}else{\n" +
                "\t\t\ti+=2;\n" +
                "\t\t}\n" +
                "\t\ti=i+1;\n" +
                "\t}while(i<10);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmplt\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoWhileNestedWhile() {
        String text = "void func(int i) {\n" +
                "\tdo{\n" +
                "\t\twhile(i>3){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti=i+1;\n" +
                "\t}while(i<10);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":5,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmplt\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoWhileNestedDoWhile() {
        String text = "void func(int i) {\n" +
                "\tdo{\n" +
                "\t\tdo{\n" +
                "\t\t\ti++;\n" +
                "\t\t}while(i>3);\n" +
                "\t\ti=i+1;\n" +
                "\t}while(i<10);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":0,\"name\":\"_if_icmpgt\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmplt\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoWhileNestedFor() {
        String text = "void func(int i) {\n" +
                "\tdo{\n" +
                "\t\tfor(;i<3;){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti=i+1;\n" +
                "\t}while(i<10);\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmplt\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testNestedDoWhile2() {
        String text = "void func1(int i, int j) {\n" +
                "\tif(i<j){\n" +
                "\t\twhile(i>=1){\n" +
                "\t\t\tdo{\n" +
                "\t\t\t\ti=i+1;\n" +
                "\t\t\t}while(i<=10||j>10&&j<20);\n" +
                "\t\t}\n" +
                "\t}else{\n" +
                "\t\tif(i>100){\n" +
                "\t\t\tdo{\n" +
                "\t\t\t\ti=i+1;\n" +
                "\t\t\t}while(i<=1000&&(j<=3||(j>1)));\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func2(int i, int j) {\n" +
                "\t\n" +
                "\twhile(i>=1){\n" +
                "\t\tif(j<1){\n" +
                "\t\t\tdo{\n" +
                "\t\t\t\ti=i+1;\n" +
                "\t\t\t}while(i<=10||j>10&&j<20);\n" +
                "\t\t}else{\n" +
                "\t\t\tj=3;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        test(text, "{\"func1(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":36,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":20},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"codeOffset\":3,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":36,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1000},{\"codeOffset\":36,\"name\":\"_if_icmpgt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":23,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_if_icmpgt\"},{\"name\":\"_return\"}],\"func2(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":20},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}");
    }
}
