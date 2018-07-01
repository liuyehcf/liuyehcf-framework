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
public class TestDoWhile {

    @Test
    public void testSimpleDoWhile1() {
        String text = "void func1(int i) {\n" +
                "\tdo{\n" +
                "\t\ti=i+1;\n" +
                "\t}while(i<10);\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmplt\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedDoWhile1() {
        String text = "void func1(int i) {\n" +
                "\twhile(i>1){\n" +
                "\t\tdo{\n" +
                "\t\t\ti=i+1;\n" +
                "\t\t}while(i<10);\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func2(int i, int j) {\n" +
                "\twhile(i>1){\n" +
                "\t\tdo{\n" +
                "\t\t\ti=i+1;\n" +
                "\t\t}while(i<10);\n" +
                "\t\tj=j+2;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func3(int i, int j) {\n" +
                "\twhile(i>1){\n" +
                "\t\tdo{\n" +
                "\t\t\ti=i+1;\n" +
                "\t\t}while(i<10);\n" +
                "\t}\n" +
                "\tj=j+2;\n" +
                "}\n" +
                "\n" +
                "void func4(int i, int j) {\n" +
                "\tif(i>1){\n" +
                "\t\tdo{\n" +
                "\t\t\ti=i+1;\n" +
                "\t\t}while(i<10);\n" +
                "\t}\n" +
                "\tj=j+2;\n" +
                "}\n" +
                "\n" +
                "void func5(int i, int j) {\n" +
                "\tif(i>1){\n" +
                "\t\tdo{\n" +
                "\t\t\ti=i+1;\n" +
                "\t\t}while(i<10);\n" +
                "\t\tj=j+2;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func6(int i, int j) {\n" +
                "\tif(i>1){\n" +
                "\t\tdo{\n" +
                "\t\t\ti=i+1;\n" +
                "\t\t}while(i<10);\n" +
                "\t}else{\n" +
                "\t\ti=5;\n" +
                "\t}\n" +
                "\tj=1;\n" +
                "}\n" +
                "\n" +
                "void func7(int i, int j) {\n" +
                "\tif(i>1){\n" +
                "\t\tdo{\n" +
                "\t\t\ti=i+1;\n" +
                "\t\t}while(i<10);\n" +
                "\t\tj=1;\n" +
                "\t}else{\n" +
                "\t\ti=5;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}],\"func2(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}],\"func3(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}],\"func4(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":10,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}],\"func5(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":14,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}],\"func6(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}],\"func7(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
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

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":36,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":20},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"codeOffset\":3,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":36,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1000},{\"codeOffset\":36,\"name\":\"_if_icmpgt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":23,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_if_icmpgt\"},{\"name\":\"_return\"}],\"func2(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":20},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
