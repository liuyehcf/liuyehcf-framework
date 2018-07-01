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
public class TestComparableOperator {

    @Test
    public void testCharEqual() {
        String text = "void func(char i, char j, boolean c) {\n" +
                "\tc=i==j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(char,char,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIntEqual() {
        String text = "void func(int i, int j, boolean c) {\n" +
                "\tc=i==j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLongEqual() {
        String text = "void func(long i, long j, boolean c) {\n" +
                "\tc=i==j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(long,long,boolean)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharNotEqual() {
        String text = "void func(char i, char j, boolean c) {\n" +
                "\tc=i!=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(char,char,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIntNotEqual() {
        String text = "void func(int i, int j, boolean c) {\n" +
                "\tc=i!=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLongNotEqual() {
        String text = "void func(long i, long j, boolean c) {\n" +
                "\tc=i!=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(long,long,boolean)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharLess() {
        String text = "void func(char i, char j, boolean c) {\n" +
                "\tc=i<j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(char,char,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIntLess() {
        String text = "void func(int i, int j, boolean c) {\n" +
                "\tc=i<j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLongLess() {
        String text = "void func(long i, long j, boolean c) {\n" +
                "\tc=i<j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(long,long,boolean)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifge\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharLarge() {
        String text = "void func(char i, char j, boolean c) {\n" +
                "\tc=i>j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(char,char,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIntLarge() {
        String text = "void func(int i, int j, boolean c) {\n" +
                "\tc=i>j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLongLarge() {
        String text = "void func(long i, long j, boolean c) {\n" +
                "\tc=i>j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(long,long,boolean)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifle\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharLessEqual() {
        String text = "void func(char i, char j, boolean c) {\n" +
                "\tc=i<=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(char,char,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIntLessEqual() {
        String text = "void func(int i, int j, boolean c) {\n" +
                "\tc=i<=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLongLessEqual() {
        String text = "void func(long i, long j, boolean c) {\n" +
                "\tc=i<=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(long,long,boolean)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifgt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharLargeEqual() {
        String text = "void func(char i, char j, boolean c) {\n" +
                "\tc=i>=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(char,char,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testIntLargeEqual() {
        String text = "void func(int i, int j, boolean c) {\n" +
                "\tc=i>=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLongLargeEqual() {
        String text = "void func(long i, long j, boolean c) {\n" +
                "\tc=i>=j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(long,long,boolean)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_iflt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

}
