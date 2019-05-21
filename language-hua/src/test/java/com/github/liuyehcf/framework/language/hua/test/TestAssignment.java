package com.github.liuyehcf.framework.language.hua.test;

import org.junit.Test;

import static com.github.liuyehcf.framework.language.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestAssignment {

    @Test
    public void testBooleanNormalAssign() {
        String text = "void func(boolean a, boolean b) {\n" +
                "\ta=b;\n" +
                "}";

        test(text, "{\"func(boolean,boolean)\":[{\"name\":\"_iload\",\"order\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharNormalAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta=b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntNormalAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta=b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongNormalAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta=b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatNormalAssign() {
        String text = "void func(float a, float b) {\n" +
                "\ta=b;\n" +
                "}";

        test(text, "{\"func(float,float)\":[{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleNormalAssign() {
        String text = "void func(double a, double b) {\n" +
                "\ta=b;\n" +
                "}";

        test(text, "{\"func(double,double)\":[{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharMulAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta *= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntMulAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta *= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongMulAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta *= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lmul\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatMulAssign() {
        String text = "void func(float a, float b) {\n" +
                "\ta *= b;\n" +
                "}";

        test(text, "{\"func(float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fmul\"},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleMulAssign() {
        String text = "void func(double a, double b) {\n" +
                "\ta *= b;\n" +
                "}";

        test(text, "{\"func(double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dmul\"},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharDivAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta /= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntDivAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta /= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongDivAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta /= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_ldiv\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatDivAssign() {
        String text = "void func(float a, float b) {\n" +
                "\ta /= b;\n" +
                "}";

        test(text, "{\"func(float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fdiv\"},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleDivAssign() {
        String text = "void func(double a, double b) {\n" +
                "\ta /= b;\n" +
                "}";

        test(text, "{\"func(double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_ddiv\"},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharRemAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta %= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntRemAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta %= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongRemAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta %= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lrem\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatRemAssign() {
        String text = "void func(float a, float b) {\n" +
                "\ta %= b;\n" +
                "}";

        test(text, "{\"func(float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_frem\"},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleRemAssign() {
        String text = "void func(double a, double b) {\n" +
                "\ta %= b;\n" +
                "}";

        test(text, "{\"func(double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_drem\"},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharAddAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta += b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntAddAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta += b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongAddAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta += b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatAddAssign() {
        String text = "void func(float a, float b) {\n" +
                "\ta += b;\n" +
                "}";

        test(text, "{\"func(float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fadd\"},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleAddAssign() {
        String text = "void func(double a, double b) {\n" +
                "\ta += b;\n" +
                "}";

        test(text, "{\"func(double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dadd\"},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharSubAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta -= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntSubAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta -= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongSubAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta -= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lsub\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatSubAssign() {
        String text = "void func(float a, float b) {\n" +
                "\ta -= b;\n" +
                "}";

        test(text, "{\"func(float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fsub\"},{\"name\":\"_fstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleSubAssign() {
        String text = "void func(double a, double b) {\n" +
                "\ta -= b;\n" +
                "}";

        test(text, "{\"func(double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dsub\"},{\"name\":\"_dstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharShlAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta <<= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntShlAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta <<= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongShlAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta <<= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lshl\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharShrAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta >>= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntShrAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta >>= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongShrAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta >>= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lshr\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharUshrAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta >>>= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntUshrAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta >>>= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongUshrAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta >>>= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lushr\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharAndAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta &= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntAndAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta &= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongAndAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta &= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_land\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharXorAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta ^= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntXorAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta ^= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongXorAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta ^= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lxor\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharOrAssign() {
        String text = "void func(char a, char b) {\n" +
                "\ta |= b;\n" +
                "}";

        test(text, "{\"func(char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntOrAssign() {
        String text = "void func(int a, int b) {\n" +
                "\ta |= b;\n" +
                "}";

        test(text, "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongOrAssign() {
        String text = "void func(long a, long b) {\n" +
                "\ta |= b;\n" +
                "}";

        test(text, "{\"func(long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lor\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleBooleanArrayNormalAssign() {
        String text = "void func(boolean[] a, boolean b) {\n" +
                "\ta[1]=b;\n" +
                "}";

        test(text, "{\"func(boolean[],boolean)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_bastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayNormalAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayNormalAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayNormalAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleFloatArrayNormalAssign() {
        String text = "void func(float[] a, float b) {\n" +
                "\ta[1]=b;\n" +
                "}";

        test(text, "{\"func(float[],float)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleDoubleArrayNormalAssign() {
        String text = "void func(double[] a, double b) {\n" +
                "\ta[1]=b;\n" +
                "}";

        test(text, "{\"func(double[],double)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayMulAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]*=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_imul\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayMulAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]*=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_imul\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayMulAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]*=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lmul\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleFloatArrayMulAssign() {
        String text = "void func(float[] a, float b) {\n" +
                "\ta[1]*=b;\n" +
                "}";

        test(text, "{\"func(float[],float)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fmul\"},{\"name\":\"_fastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleDoubleArrayMulAssign() {
        String text = "void func(double[] a, double b) {\n" +
                "\ta[1]*=b;\n" +
                "}";

        test(text, "{\"func(double[],double)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dmul\"},{\"name\":\"_dastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayDivAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]/=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_idiv\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayDivAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]/=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_idiv\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayDivAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]/=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_ldiv\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleFloatArrayDivAssign() {
        String text = "void func(float[] a, float b) {\n" +
                "\ta[1]/=b;\n" +
                "}";

        test(text, "{\"func(float[],float)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fdiv\"},{\"name\":\"_fastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleDoubleArrayDivAssign() {
        String text = "void func(double[] a, double b) {\n" +
                "\ta[1]/=b;\n" +
                "}";

        test(text, "{\"func(double[],double)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_ddiv\"},{\"name\":\"_dastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayRemAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]%=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_irem\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayRemAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]%=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_irem\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayRemAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]%=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lrem\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleFloatArrayRemAssign() {
        String text = "void func(float[] a, float b) {\n" +
                "\ta[1]%=b;\n" +
                "}";

        test(text, "{\"func(float[],float)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_frem\"},{\"name\":\"_fastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleDoubleArrayRemAssign() {
        String text = "void func(double[] a, double b) {\n" +
                "\ta[1]%=b;\n" +
                "}";

        test(text, "{\"func(double[],double)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_drem\"},{\"name\":\"_dastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayAddAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]+=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayAddAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]+=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayAddAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]+=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_ladd\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleFloatArrayAddAssign() {
        String text = "void func(float[] a, float b) {\n" +
                "\ta[1]+=b;\n" +
                "}";

        test(text, "{\"func(float[],float)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fadd\"},{\"name\":\"_fastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleDoubleArrayAddAssign() {
        String text = "void func(double[] a, double b) {\n" +
                "\ta[1]+=b;\n" +
                "}";

        test(text, "{\"func(double[],double)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dadd\"},{\"name\":\"_dastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArraySubAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]-=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_isub\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArraySubAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]-=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_isub\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArraySubAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]-=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lsub\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleFloatArraySubAssign() {
        String text = "void func(float[] a, float b) {\n" +
                "\ta[1]-=b;\n" +
                "}";

        test(text, "{\"func(float[],float)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fsub\"},{\"name\":\"_fastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleDoubleArraySubAssign() {
        String text = "void func(double[] a, double b) {\n" +
                "\ta[1]-=b;\n" +
                "}";

        test(text, "{\"func(double[],double)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dsub\"},{\"name\":\"_dastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayShlAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]<<=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishl\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayShlAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]<<=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishl\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayShlAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]<<=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lshl\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayShrAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]>>=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishr\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayShrAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]>>=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishr\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayShrAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]>>=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lshr\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayUshrAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]>>>=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iushr\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayUshrAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]>>>=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iushr\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayUshrAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]>>>=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lushr\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayAndAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]&=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iand\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayAndAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]&=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iand\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayAndAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]&=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_land\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayXorAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]^=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ixor\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayXorAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]^=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ixor\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayXorAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]^=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lxor\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleCharArrayOrAssign() {
        String text = "void func(char[] a, char b) {\n" +
                "\ta[1]|=b;\n" +
                "}";

        test(text, "{\"func(char[],char)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ior\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleIntArrayOrAssign() {
        String text = "void func(int[] a, int b) {\n" +
                "\ta[1]|=b;\n" +
                "}";

        test(text, "{\"func(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testSingleLongArrayOrAssign() {
        String text = "void func(long[] a, long b) {\n" +
                "\ta[1]|=b;\n" +
                "}";

        test(text, "{\"func(long[],long)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lor\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testBooleanArrayReferenceAssign() {
        String text = "void func(boolean[] a, boolean[][][] b) {\n" +
                "\ta = new boolean[1];\n" +
                "\tb = new boolean[2][2][2];\n" +
                "\tb[1] = new boolean[2][2];\n" +
                "\tb[1][1] = new boolean[2];\n" +
                "\tb[1][1][1] = true;\n" +
                "}";

        test(text, "{\"func(boolean[],boolean[][][])\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"boolean\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"boolean[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"boolean[][]\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_newarray\",\"type\":\"boolean\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_bastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharArrayReferenceAssign() {
        String text = "void func(char[] a, char[][][] b) {\n" +
                "\ta = new char[1];\n" +
                "\tb = new char[2][2][2];\n" +
                "\tb[1] = new char[2][2];\n" +
                "\tb[1][1] = new char[2];\n" +
                "\tb[1][1][1] = '1';\n" +
                "\tb[1][1][1] *= '1';\n" +
                "\tb[1][1][1] /= '1';\n" +
                "\tb[1][1][1] %= '1';\n" +
                "\tb[1][1][1] += '1';\n" +
                "\tb[1][1][1] -= '1';\n" +
                "\tb[1][1][1] <<= '1';\n" +
                "\tb[1][1][1] >>= '1';\n" +
                "\tb[1][1][1] >>>= '1';\n" +
                "\tb[1][1][1] &= '1';\n" +
                "\tb[1][1][1] ^= '1';\n" +
                "\tb[1][1][1] |= '1';\n" +
                "}";

        test(text, "{\"func(char[],char[][][])\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"char\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"char[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"char[][]\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_newarray\",\"type\":\"char\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_imul\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_idiv\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_irem\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_iadd\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_isub\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_ishl\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_ishr\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_iushr\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_iand\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_ixor\"},{\"name\":\"_castore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_caload\"},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_ior\"},{\"name\":\"_castore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntArrayReferenceAssign() {
        String text = "void func(int[] a, int[][][] b) {\n" +
                "\ta = new int[1];\n" +
                "\tb = new int[2][2][2];\n" +
                "\tb[1] = new int[2][2];\n" +
                "\tb[1][1] = new int[2];\n" +
                "\tb[1][1][1] = 1;\n" +
                "\tb[1][1][1] *= 1;\n" +
                "\tb[1][1][1] /= 1;\n" +
                "\tb[1][1][1] %= 1;\n" +
                "\tb[1][1][1] += 1;\n" +
                "\tb[1][1][1] -= 1;\n" +
                "\tb[1][1][1] <<= 1;\n" +
                "\tb[1][1][1] >>= 1;\n" +
                "\tb[1][1][1] >>>= 1;\n" +
                "\tb[1][1][1] &= 1;\n" +
                "\tb[1][1][1] ^= 1;\n" +
                "\tb[1][1][1] |= 1;\n" +
                "}";

        test(text, "{\"func(int[],int[][][])\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"int[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"int[][]\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_imul\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_idiv\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_irem\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_ishl\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_ishr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iushr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iand\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_ixor\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongArrayReferenceAssign() {
        String text = "void func(long[] a, long[][][] b) {\n" +
                "\ta = new long[1];\n" +
                "\tb = new long[2][2][2];\n" +
                "\tb[1] = new long[2][2];\n" +
                "\tb[1][1] = new long[2];\n" +
                "\tb[1][1][1] = 1L;\n" +
                "\tb[1][1][1] *= 1L;\n" +
                "\tb[1][1][1] /= 1L;\n" +
                "\tb[1][1][1] %= 1L;\n" +
                "\tb[1][1][1] += 1L;\n" +
                "\tb[1][1][1] -= 1L;\n" +
                "\tb[1][1][1] <<= 1L;\n" +
                "\tb[1][1][1] >>= 1L;\n" +
                "\tb[1][1][1] >>>= 1L;\n" +
                "\tb[1][1][1] &= 1L;\n" +
                "\tb[1][1][1] ^= 1L;\n" +
                "\tb[1][1][1] |= 1L;\n" +
                "}";

        test(text, "{\"func(long[],long[][][])\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"long\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"long[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"long[][]\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_newarray\",\"type\":\"long\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lmul\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_ldiv\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lrem\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_ladd\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lsub\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lshl\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lshr\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lushr\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_land\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lxor\"},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_laload\"},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lor\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatArrayReferenceAssign() {
        String text = "void func(float[] a, float[][][] b) {\n" +
                "\ta = new float[1];\n" +
                "\tb = new float[2][2][2];\n" +
                "\tb[1] = new float[2][2];\n" +
                "\tb[1][1] = new float[2];\n" +
                "\tb[1][1][1] = 1.1f;\n" +
                "\tb[1][1][1] *= 1.1f;\n" +
                "\tb[1][1][1] /= 1.1f;\n" +
                "\tb[1][1][1] %= 1.1f;\n" +
                "\tb[1][1][1] += 1.1f;\n" +
                "\tb[1][1][1] -= 1.1f;\n" +
                "}";

        test(text, "{\"func(float[],float[][][])\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"float\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"float[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"float[][]\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_newarray\",\"type\":\"float\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_fconst\",\"value\":1.1},{\"name\":\"_fastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fconst\",\"value\":1.1},{\"name\":\"_fmul\"},{\"name\":\"_fastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fconst\",\"value\":1.1},{\"name\":\"_fdiv\"},{\"name\":\"_fastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fconst\",\"value\":1.1},{\"name\":\"_frem\"},{\"name\":\"_fastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fconst\",\"value\":1.1},{\"name\":\"_fadd\"},{\"name\":\"_fastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_faload\"},{\"name\":\"_fconst\",\"value\":1.1},{\"name\":\"_fsub\"},{\"name\":\"_fastore\"},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleArrayReferenceAssign() {
        String text = "void func(double[] a, double[][][] b) {\n" +
                "\ta = new double[1];\n" +
                "\tb = new double[2][2][2];\n" +
                "\tb[1] = new double[2][2];\n" +
                "\tb[1][1] = new double[2];\n" +
                "\tb[1][1][1] = 1.1d;\n" +
                "\tb[1][1][1] *= 1.1d;\n" +
                "\tb[1][1][1] /= 1.1d;\n" +
                "\tb[1][1][1] %= 1.1d;\n" +
                "\tb[1][1][1] += 1.1d;\n" +
                "\tb[1][1][1] -= 1.1d;\n" +
                "}";

        test(text, "{\"func(double[],double[][][])\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"double\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"double[][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"double[][]\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_newarray\",\"type\":\"double\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dconst\",\"value\":1.1},{\"name\":\"_dastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dconst\",\"value\":1.1},{\"name\":\"_dmul\"},{\"name\":\"_dastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dconst\",\"value\":1.1},{\"name\":\"_ddiv\"},{\"name\":\"_dastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dconst\",\"value\":1.1},{\"name\":\"_drem\"},{\"name\":\"_dastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dconst\",\"value\":1.1},{\"name\":\"_dadd\"},{\"name\":\"_dastore\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_daload\"},{\"name\":\"_dconst\",\"value\":1.1},{\"name\":\"_dsub\"},{\"name\":\"_dastore\"},{\"name\":\"_return\"}]}");
    }
}
