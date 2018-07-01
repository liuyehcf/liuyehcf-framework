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
public class TestBooleanExpression {
    @Test
    public void testConditionExpression1() {
        String text = "void func(boolean a, int i, int j) {\n" +
                "\tint k= (a||i<j&&j>=3) ? i++:--j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":11,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":11,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"increment\":-1,\"name\":\"_iinc\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testConditionExpression2() {
        String text = "void func1(long a,long b,boolean c){\n" +
                "\tc=a==b;\n" +
                "\tc=a!=b;\n" +
                "\tc=a<b;\n" +
                "\tc=a>b;\n" +
                "\tc=a<=b;\n" +
                "\tc=a>=b;\n" +
                "}\n" +
                "\n" +
                "void func2(long a,long b){\n" +
                "\tboolean c=a==b;\n" +
                "\tboolean d=a!=b;\n" +
                "\tboolean e=a<b;\n" +
                "\tboolean f=a>b;\n" +
                "\tboolean g=a<=b;\n" +
                "\tboolean h=a>=b;\n" +
                "}\n" +
                "\n" +
                "void func3(long a,long b,int i){\n" +
                "\tif(a<b){\n" +
                "\t\tif(a>b)\n" +
                "\t\t\tif(a<=b){\n" +
                "\t\t\t\ti=3;\n" +
                "\t\t\t}else{\n" +
                "\t\t\t\tif(a<=b){\n" +
                "\t\t\t\t\tif(a==b){\n" +
                "\t\t\t\t\t\tif(a!=b){\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\ti=1;\n" +
                "\t}\n" +
                "\ti=2;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(long,long,boolean)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":22,\"name\":\"_ifge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":30,\"name\":\"_ifle\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":31,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":38,\"name\":\"_ifgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":39,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":46,\"name\":\"_iflt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":47,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}],\"func2(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":22,\"name\":\"_ifge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":30,\"name\":\"_ifle\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":31,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":38,\"name\":\"_ifgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":39,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":6},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":46,\"name\":\"_iflt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":47,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":7},{\"name\":\"_return\"}],\"func3(long,long,int)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":29,\"name\":\"_ifge\"},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":27,\"name\":\"_ifle\"},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":15,\"name\":\"_ifgt\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":27,\"name\":\"_goto\"},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":27,\"name\":\"_ifgt\"},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":27,\"name\":\"_ifne\"},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":27,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexCompareOperator1() {
        String text = "void func1(boolean a, boolean b, int i, int j) {\n" +
                "\tif(i>=j&&i<j||i>3&&j>=5||a&&b)\n" +
                "\t\ti=1;\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, int i, int j) {\n" +
                "\tif((i>=j&&(i<j||i>3))&&((j>=5)||a&&b))\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":16,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":12,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}],\"func2(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":18,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":9,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":18,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexCompareOperator2() {
        String text = "void func1(boolean a, boolean b, int i, int j) {\n" +
                "\tboolean c=i>=j&&i<j||i>3&&j>=5||a&&b;\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, int i, int j) {\n" +
                "\tboolean c;\n" +
                "\tc=((i>=j&&(i<j||i>3))&&((j>=5)||a&&b));\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":16,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":12,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func2(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":18,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":9,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":18,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionAssign1() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e= a||b&&c||d;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionAssign2() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e= a&&b||c&&d;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionAssign3() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e= a&&b&&c&&d ,f ;\n" +
                "\tf = a||b||c||d;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":20,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":20,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":20,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":22,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionAssign4() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e;\n" +
                "\te = (a||b)&&(c||d);\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionAssign5() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e;\n" +
                "\te = (a && b)||((c && d) ||(c)) && (d);\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":12,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testMixBinaryOperator() {
        String text = "void func(int a, int b, int c, int d, int e, int f, int g, int h, int i) {\n" +
                "\tint j = a + b - c * d / e % f & g ^ h | i;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int,int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_imul\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_idiv\"},{\"name\":\"_iload\",\"order\":5},{\"name\":\"_irem\"},{\"name\":\"_isub\"},{\"name\":\"_iload\",\"order\":6},{\"name\":\"_iand\"},{\"name\":\"_iload\",\"order\":7},{\"name\":\"_ixor\"},{\"name\":\"_iload\",\"order\":8},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":9},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testMixBinaryOperatorWithSmallParenthesis() {
        String text = "void func(int a, int b, int c, int d, int e, int f, int g, int h, int i) {\n" +
                "\tint j = ((a + b) - ((c)) * d) / e % ((f) & g) ^ h | i;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int,int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_imul\"},{\"name\":\"_isub\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_idiv\"},{\"name\":\"_iload\",\"order\":5},{\"name\":\"_iload\",\"order\":6},{\"name\":\"_iand\"},{\"name\":\"_irem\"},{\"name\":\"_iload\",\"order\":7},{\"name\":\"_ixor\"},{\"name\":\"_iload\",\"order\":8},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":9},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
