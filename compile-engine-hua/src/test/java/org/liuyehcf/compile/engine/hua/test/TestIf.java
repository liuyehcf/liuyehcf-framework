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
public class TestIf {

    @Test
    public void testIfThen() {
        String text = "void func(boolean a, int i) {\n" +
                "\tif(a){\n" +
                "\t\ti=1;\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElse() {
        String text = "void func(boolean a, int i) {\n" +
                "\tif(a){\n" +
                "\t\ti=1;\n" +
                "\t}else{\n" +
                "\t\ti=2;\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenNestedIfThen() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenNestedIfThenElse() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=2;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElseTrueNestedIfThen() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}else{\n" +
                "\t\ti=2;\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElseTrueNestedIfThenElse() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=2;\n" +
                "\t\t}\n" +
                "\t}else{\n" +
                "\t\ti=3;\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElseFalseNestedIfThen() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\ti=1;\n" +
                "\t}else{\n" +
                "\t\tif(a){\n" +
                "\t\t\ti=2;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=3;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElseFalseNestedIfThenElse() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=2;\n" +
                "\t\t}\n" +
                "\t}else{\n" +
                "\t\ti=3;\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElseTrueNestedIfThenFalseNestedIfThen() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}else{\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=2;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElseTrueNestedIfThenFalseNestedIfThenElse() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}else{\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=2;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=3;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElseTrueNestedIfThenElseFalseNestedIfThen() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=2;\n" +
                "\t\t}\n" +
                "\t}else{\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=3;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIfThenElseTrueNestedIfThenElseFalseNestedIfThenElse() {
        String text = "void func(boolean a, boolean b, int i) {\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=2;\n" +
                "\t\t}\n" +
                "\t}else{\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=3;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=4;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\ti+=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":15,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIf1() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d,int i){\n" +
                "\tif(a){\n" +
                "\t\tif(b){\n" +
                "\t\t\tif(c){\n" +
                "\t\t\t\tif(d)\n" +
                "\t\t\t\t\ti=3;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIf2() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d,int i){\n" +
                "\tif(a)\n" +
                "\t\tif(b)\n" +
                "\t\t\ti=1;\n" +
                "\t\telse\n" +
                "\t\t\ti=2;\n" +
                "}\n";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIf3() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d,int i){\n" +
                "\tif(a) {\n" +
                "\t\tif(b)\n" +
                "\t\t\ti=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=2;\t\n" +
                "\t\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIf4() {
        String text = "void func1(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=3;\t\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=3;\n" +
                "\tj=1;\t\n" +
                "}\n" +
                "\n" +
                "void func3(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=3;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "void func4(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else\n" +
                "\t\t\ti=2;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=3;\n" +
                "}\n" +
                "\n" +
                "void func5(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else\n" +
                "\t\t\ti=2;\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=3;\n" +
                "}\n" +
                "\n" +
                "void func6(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else\n" +
                "\t\t\ti=2;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=3;\n" +
                "\tj=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func5(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func6(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIf5() {
        String text = "void func1(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t\tj=5;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func3(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else\n" +
                "\t\t\ti=2;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func4(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}else\n" +
                "\t\t\ti=2;\n" +
                "\t\tj=5;\n" +
                "\t}\n" +
                "}\n";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIf6() {
        String text = "void func1(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\ti=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\tif(b)\n" +
                "\t\t\ti=2;\n" +
                "\t\telse {\n" +
                "\t\t\ti=3;\n" +
                "\t\t}\t\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\ti=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\tif(b)\n" +
                "\t\t\ti=2;\n" +
                "\t\telse {\n" +
                "\t\t\ti=3;\n" +
                "\t\t}\n" +
                "\tj=1;\t\t\n" +
                "}\n" +
                "\n" +
                "void func3(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\ti=1;\n" +
                "\t}\n" +
                "\telse {\n" +
                "\t\tif(b)\n" +
                "\t\t\ti=2;\n" +
                "\t\telse {\n" +
                "\t\t\ti=3;\n" +
                "\t\t}\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func4(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(c){\n" +
                "\t\t\ti=5;\n" +
                "\t\t}else{\n" +
                "\t\t\ti=6;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\telse {\n" +
                "\t\tif(b)\n" +
                "\t\t\ti=2;\n" +
                "\t\telse {\n" +
                "\t\t\ti=3;\n" +
                "\t\t}\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\tj=2;\n" +
                "}\n" +
                "\n" +
                "void func5(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(c){\n" +
                "\t\t\tif(d)\n" +
                "\t\t\t\ti=7;\n" +
                "\t\t}else{\n" +
                "\t\t\tif(d)\n" +
                "\t\t\t\ti=8;\n" +
                "\t\t\telse\n" +
                "\t\t\t\ti=9;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\telse {\n" +
                "\t\ti=3;\n" +
                "\t}\n" +
                "\tj=2;\n" +
                "}\n" +
                "\n" +
                "void func6(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(c){\n" +
                "\t\t\tif(d)\n" +
                "\t\t\t\ti=7;\n" +
                "\t\t}else{\n" +
                "\t\t\tif(d)\n" +
                "\t\t\t\ti=8;\n" +
                "\t\t\telse\n" +
                "\t\t\t\ti=9;\n" +
                "\t\t}\n" +
                "\t\tj=2;\n" +
                "\t}\n" +
                "\telse {\n" +
                "\t\ti=3;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func7(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(c){\n" +
                "\t\t\tif(d)\n" +
                "\t\t\t\tif(b){\n" +
                "\t\t\t\t\ti=1;\n" +
                "\t\t\t\t}\n" +
                "\t\t\telse{\n" +
                "\t\t\t\ti=2;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\telse {\n" +
                "\t\ti=3;\n" +
                "\t}\n" +
                "\tj=1;\n" +
                "}\n" +
                "\n" +
                "void func8(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tif(c){\n" +
                "\t\t\tif(d)\n" +
                "\t\t\t\tif(b){\n" +
                "\t\t\t\t\ti=1;\n" +
                "\t\t\t\t}\n" +
                "\t\t\telse{\n" +
                "\t\t\t\ti=2;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\telse {\n" +
                "\t\ti=3;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":15,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func5(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":17,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":19,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":7},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":8},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":9},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func6(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":19,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":7},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":8},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":9},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":21,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func7(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func8(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":13,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":13,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":18,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIf7() {
        String text = "void func1(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tb=a||b&&c||d;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=2;\n" +
                "\tj=1;\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a) {\n" +
                "\t\tb=a||b&&c||d;\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=2;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":15,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":17,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSingleConditionIf() {
        String text = "void func1(boolean a,int b){\n" +
                "\tif(a){\n" +
                "\t\tb=3;\n" +
                "\t}\n" +
                "}" +
                "void func2(boolean a,int b){\n" +
                "\tif (a) {\n" +
                "\t\tb = 3;\n" +
                "\t} else {\n" +
                "\t\tb = 4;\n" +
                "\t}\n" +
                "}\n";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}],\"func2(boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionIf1() {
        String text = "void func1(boolean a, boolean b, int c){\n" +
                "\tif(a||b){\n" +
                "\t\tc=3;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, int c){\n" +
                "\tif(a&&b){\n" +
                "\t\tc=3;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func3(boolean a, boolean b, int c){\n" +
                "\tif(a||b){\n" +
                "\t\tc=3;\n" +
                "\t}else{\n" +
                "\t\tc=4;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func4(boolean a, boolean b, int c){\n" +
                "\tif(a&&b){\n" +
                "\t\tc=3;\n" +
                "\t}else{\n" +
                "\t\tc=4;\n" +
                "\t}\n" +
                "}\n" +
                "\n";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}],\"func2(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}],\"func3(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}],\"func4(boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":2},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionIf2() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d, int e){\n" +
                "\tif(a||b&&c){\n" +
                "\t\te=3;\n" +
                "\t}else{\n" +
                "\t\te=4;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionIf3() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d, int e){\n" +
                "\tif((a||b)&&c||(d)){\n" +
                "\t\te=3;\n" +
                "\t}else{\n" +
                "\t\te=4;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionIf4() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tif((a||b)&&(c||d)){int e=3;}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
