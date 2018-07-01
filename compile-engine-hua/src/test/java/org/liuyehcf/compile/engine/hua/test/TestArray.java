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
public class TestArray {
    @Test
    public void testArrayLoad1() {
        String text = "void func(int[] a) {\n" +
                "\tint b=a[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int[])\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayLoad2() {
        String text = "void func(int[][] a,int j) {\n" +
                "\tj=a[5][6];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int[][],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayLoad3() {
        String text = "void func(char[][] a,char j) {\n" +
                "\tj=a[5][6];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(char[][],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_caload\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayLoad4() {
        String text = "void func(boolean[][] a,boolean j) {\n" +
                "\tj=a[5][6];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean[][],boolean)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_baload\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayStore1() {
        String text = "void func1(int[] c) {\n" +
                "\tc[5] = 10000;\n" +
                "}\n" +
                "void func2(int[] a) {\n" +
                "\tint b = 1, c = 1, d = 1;\n" +
                "\ta[5] = 100;\n" +
                "\ta[5] = a[b * (c - d)];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int[])\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":10000},{\"name\":\"_iastore\"},{\"name\":\"_return\"}],\"func2(int[])\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":100},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_isub\"},{\"name\":\"_imul\"},{\"name\":\"_iaload\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayStore2() {
        String text = "void func(int[][] a,int j) {\n" +
                "\ta[4][5]=5;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int[][],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayStore3() {
        String text = "void func(int[][][][] a) {\n" +
                "\ta[1][2][3][4] = 5;\n" +
                "\ta[1][2][3] = new int[2];\n" +
                "\ta[1][2] = new int[2][3];\n" +
                "\ta[1] = new int[2][3][4];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int[][][][])\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":3},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"int[][]\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"int[][][]\"},{\"name\":\"_aastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayStore4() {
        String text = "void func(char[][] a) {\n" +
                "\ta[1][2] = 'b';\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(char[][])\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":98},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayStore5() {
        String text = "void func(boolean[][] a) {\n" +
                "\ta[1][2] = true;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean[][])\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_bastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNewArray1() {
        String text = "\tvoid func() {\n" +
                "\t\tint[] a1=new int[1];\n" +
                "\t\tint[][][][][] a2=new int[2][][][][];\n" +
                "\t\tint[][][][][] a3=new int[3][4][5][6][7];\n" +
                "\t}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_anewarray\",\"type\":\"int[][][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_iconst\",\"value\":7},{\"expressionDimSize\":5,\"name\":\"_multianewarray\",\"type\":\"int[][][][][]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
