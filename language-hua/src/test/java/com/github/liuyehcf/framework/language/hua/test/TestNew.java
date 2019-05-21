package com.github.liuyehcf.framework.language.hua.test;

import org.junit.Test;

import static com.github.liuyehcf.framework.language.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestNew {

    @Test
    public void testBooleanNewArray() {
        String text = "void func() {\n" +
                "\tboolean[] a1=new boolean[1];\n" +
                "\tboolean[][] a2=new boolean[1][2];\n" +
                "\tboolean[][] a3=new boolean[1][];\n" +
                "\tboolean[][][] a4=new boolean[3][4][5];\n" +
                "\tboolean[][][] a5=new boolean[3][4][];\n" +
                "\tboolean[][][] a6=new boolean[3][][];\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"boolean\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"boolean[][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_anewarray\",\"type\":\"boolean[]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"boolean[][][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"boolean[][][]\"},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_anewarray\",\"type\":\"boolean[][]\"},{\"name\":\"_astore\",\"order\":5},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharNewArray() {
        String text = "void func() {\n" +
                "\tchar[] a1=new char[1];\n" +
                "\tchar[][] a2=new char[1][2];\n" +
                "\tchar[][] a3=new char[1][];\n" +
                "\tchar[][][] a4=new char[3][4][5];\n" +
                "\tchar[][][] a5=new char[3][4][];\n" +
                "\tchar[][][] a6=new char[3][][];\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"char\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"char[][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_anewarray\",\"type\":\"char[]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"char[][][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"char[][][]\"},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_anewarray\",\"type\":\"char[][]\"},{\"name\":\"_astore\",\"order\":5},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntNewArray() {
        String text = "void func() {\n" +
                "\tint[] a1=new int[1];\n" +
                "\tint[][] a2=new int[1][2];\n" +
                "\tint[][] a3=new int[1][];\n" +
                "\tint[][][] a4=new int[3][4][5];\n" +
                "\tint[][][] a5=new int[3][4][];\n" +
                "\tint[][][] a6=new int[3][][];\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"int[][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_anewarray\",\"type\":\"int[]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"int[][][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"int[][][]\"},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_anewarray\",\"type\":\"int[][]\"},{\"name\":\"_astore\",\"order\":5},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongNewArray() {
        String text = "void func() {\n" +
                "\tlong[] a1=new long[1];\n" +
                "\tlong[][] a2=new long[1][2];\n" +
                "\tlong[][] a3=new long[1][];\n" +
                "\tlong[][][] a4=new long[3][4][5];\n" +
                "\tlong[][][] a5=new long[3][4][];\n" +
                "\tlong[][][] a6=new long[3][][];\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"long\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"long[][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_anewarray\",\"type\":\"long[]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"long[][][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"long[][][]\"},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_anewarray\",\"type\":\"long[][]\"},{\"name\":\"_astore\",\"order\":5},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatNewArray() {
        String text = "void func() {\n" +
                "\tfloat[] a1=new float[1];\n" +
                "\tfloat[][] a2=new float[1][2];\n" +
                "\tfloat[][] a3=new float[1][];\n" +
                "\tfloat[][][] a4=new float[3][4][5];\n" +
                "\tfloat[][][] a5=new float[3][4][];\n" +
                "\tfloat[][][] a6=new float[3][][];\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"float\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"float[][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_anewarray\",\"type\":\"float[]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"float[][][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"float[][][]\"},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_anewarray\",\"type\":\"float[][]\"},{\"name\":\"_astore\",\"order\":5},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleNewArray() {
        String text = "void func() {\n" +
                "\tdouble[] a1=new double[1];\n" +
                "\tdouble[][] a2=new double[1][2];\n" +
                "\tdouble[][] a3=new double[1][];\n" +
                "\tdouble[][][] a4=new double[3][4][5];\n" +
                "\tdouble[][][] a5=new double[3][4][];\n" +
                "\tdouble[][][] a6=new double[3][][];\n" +
                "}";

        test(text, "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"double\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"double[][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_anewarray\",\"type\":\"double[]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"double[][][]\"},{\"name\":\"_astore\",\"order\":3},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"double[][][]\"},{\"name\":\"_astore\",\"order\":4},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_anewarray\",\"type\":\"double[][]\"},{\"name\":\"_astore\",\"order\":5},{\"name\":\"_return\"}]}");
    }
}
