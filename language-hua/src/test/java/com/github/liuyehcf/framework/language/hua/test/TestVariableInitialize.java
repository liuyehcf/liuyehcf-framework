package com.github.liuyehcf.framework.language.hua.test;

import org.junit.Test;

import static com.github.liuyehcf.framework.language.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestVariableInitialize {

    @Test
    public void testBooleanInitialize() {
        String text = "void func(int i) {\n" +
                "\tboolean a=true;\n" +
                "\tboolean b=i<=0||i<=100&&i>=3;\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":11,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":13,\"name\":\"_if_icmpgt\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":13,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharInitialize() {
        String text = "void func(char c) {\n" +
                "\tchar a='a';\n" +
                "\tchar b=c+'1';\n" +
                "}";

        test(text, "{\"func(char)\":[{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntInitialize() {
        String text = "void func(int c) {\n" +
                "\tint a=100;\n" +
                "\tint b=c+0777;\n" +
                "}";

        test(text, "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":100},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":511},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongInitialize() {
        String text = "void func(long c) {\n" +
                "\tlong a=100L;\n" +
                "\tlong b=c+1000000l;\n" +
                "}";

        test(text, "{\"func(long)\":[{\"name\":\"_lconst\",\"value\":100},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lconst\",\"value\":1000000},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatInitialize() {
        String text = "void func(float c) {\n" +
                "\tfloat a=100.0f;\n" +
                "\tfloat b=c+1e-10f;\n" +
                "}";

        test(text, "{\"func(float)\":[{\"name\":\"_fconst\",\"value\":100.0},{\"name\":\"_fstore\",\"order\":1},{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fconst\",\"value\":1.0E-10},{\"name\":\"_fadd\"},{\"name\":\"_fstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleInitialize() {
        String text = "void func(double c) {\n" +
                "\tdouble a=100.0d;\n" +
                "\tdouble b=c+9.9e+10d;\n" +
                "}";

        test(text, "{\"func(double)\":[{\"name\":\"_dconst\",\"value\":100.0},{\"name\":\"_dstore\",\"order\":1},{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dconst\",\"value\":9.9E10},{\"name\":\"_dadd\"},{\"name\":\"_dstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleBooleanArray() {
        String text = "void func(boolean[] c) {\n" +
                "\tboolean[] a=new boolean[5];\n" +
                "\tboolean[] b=c;\n" +
                "}";

        test(text, "{\"func(boolean[])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"boolean\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArray() {
        String text = "void func(char[] c) {\n" +
                "\tchar[] a=new char[5];\n" +
                "\tchar[] b=c;\n" +
                "}";

        test(text, "{\"func(char[])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"char\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArray() {
        String text = "void func(int[] c) {\n" +
                "\tint[] a=new int[5];\n" +
                "\tint[] b=c;\n" +
                "}";

        test(text, "{\"func(int[])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArray() {
        String text = "void func(long[] c) {\n" +
                "\tlong[] a=new long[5];\n" +
                "\tlong[] b=c;\n" +
                "}";

        test(text, "{\"func(long[])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"long\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleFloatArray() {
        String text = "void func(float[] c) {\n" +
                "\tfloat[] a=new float[5];\n" +
                "\tfloat[] b=c;\n" +
                "}";

        test(text, "{\"func(float[])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"float\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleDoubleArray() {
        String text = "void func(double[] c) {\n" +
                "\tdouble[] a=new double[5];\n" +
                "\tdouble[] b=c;\n" +
                "}";

        test(text, "{\"func(double[])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"double\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testMultiBooleanArray() {
        String text = "void func(boolean[][][] e) {\n" +
                "\tboolean[][][] a=new boolean[5][4][3];\n" +
                "\tboolean[][][] b=new boolean[5][4][];\n" +
                "\tboolean[][][] c=new boolean[5][][];\n" +
                "\tboolean[][][] d=e;\n" +
                "}";

        test(text, "{\"func(boolean[][][])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":3},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"boolean[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"boolean[][][]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_anewarray\",\"type\":\"boolean[][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testMultiCharArray() {
        String text = "void func(char[][][] e) {\n" +
                "\tchar[][][] a=new char[5][4][3];\n" +
                "\tchar[][][] b=new char[5][4][];\n" +
                "\tchar[][][] c=new char[5][][];\n" +
                "\tchar[][][] d=e;\n" +
                "}";

        test(text, "{\"func(char[][][])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":3},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"char[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"char[][][]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_anewarray\",\"type\":\"char[][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testMultiIntArray() {
        String text = "void func(int[][][] e) {\n" +
                "\tint[][][] a=new int[5][4][3];\n" +
                "\tint[][][] b=new int[5][4][];\n" +
                "\tint[][][] c=new int[5][][];\n" +
                "\tint[][][] d=e;\n" +
                "}";

        test(text, "{\"func(int[][][])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":3},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"int[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"int[][][]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_anewarray\",\"type\":\"int[][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testMultiLongArray() {
        String text = "void func(long[][][] e) {\n" +
                "\tlong[][][] a=new long[5][4][3];\n" +
                "\tlong[][][] b=new long[5][4][];\n" +
                "\tlong[][][] c=new long[5][][];\n" +
                "\tlong[][][] d=e;\n" +
                "}";

        test(text, "{\"func(long[][][])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":3},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"long[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"long[][][]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_anewarray\",\"type\":\"long[][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testMultiFloatArray() {
        String text = "void func(float[][][] e) {\n" +
                "\tfloat[][][] a=new float[5][4][3];\n" +
                "\tfloat[][][] b=new float[5][4][];\n" +
                "\tfloat[][][] c=new float[5][][];\n" +
                "\tfloat[][][] d=e;\n" +
                "}";

        test(text, "{\"func(float[][][])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":3},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"float[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"float[][][]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_anewarray\",\"type\":\"float[][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testMultiDoubleArray() {
        String text = "void func(double[][][] e) {\n" +
                "\tdouble[][][] a=new double[5][4][3];\n" +
                "\tdouble[][][] b=new double[5][4][];\n" +
                "\tdouble[][][] c=new double[5][][];\n" +
                "\tdouble[][][] d=e;\n" +
                "}";

        test(text, "{\"func(double[][][])\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":3},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"double[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"double[][][]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_anewarray\",\"type\":\"double[][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_return\"}]}");
    }
}
