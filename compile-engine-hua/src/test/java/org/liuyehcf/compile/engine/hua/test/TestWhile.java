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
public class TestWhile {
    @Test
    public void testSimpleWhile1() {
        String text = "void func1(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\twhile(a){\n" +
                "\t\ti=i-1;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIfWhile1() {
        String text = "void func1(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a){\n" +
                "\t\twhile(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tj=1;\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a)\n" +
                "\t\twhile(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\telse\n" +
                "\t\ti=2;\n" +
                "\tj=1;\n" +
                "}\n" +
                "\n" +
                "void func3(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a){\n" +
                "\t\twhile(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=2;\n" +
                "}\n" +
                "\n" +
                "void func4(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a)\n" +
                "\t\tif(c){\n" +
                "\t\t\twhile(b){\n" +
                "\t\t\t\ti=1;\n" +
                "\t\t\t}\n" +
                "\t\t\tj=1;\n" +
                "\t\t}\n" +
                "\telse {\n" +
                "\t\ti=2;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func5(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\twhile(a){\n" +
                "\t\twhile(b){\n" +
                "\t\t\twhile(c)\n" +
                "\t\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\tj=2;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":4,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func5(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":2,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":4,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
