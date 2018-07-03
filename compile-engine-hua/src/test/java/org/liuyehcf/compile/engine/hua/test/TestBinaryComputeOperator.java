package org.liuyehcf.compile.engine.hua.test;

import org.junit.Test;

import static org.liuyehcf.compile.engine.hua.test.TestUtil.test;

/**
 * @author hechenfeng
 * @date 2018/7/1
 */
public class TestBinaryComputeOperator {

    @Test
    public void testCharAdd() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a+b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntAdd() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a+b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongAdd() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a+b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatAdd() {
        String text = "void func(float a, float b, float c) {\n" +
                "\tc=a+b;\n" +
                "}";

        test(text, "{\"func(float,float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fadd\"},{\"name\":\"_fstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleAdd() {
        String text = "void func(double a, double b, double c) {\n" +
                "\tc=a+b;\n" +
                "}";

        test(text, "{\"func(double,double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dadd\"},{\"name\":\"_dstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharSub() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a-b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntSub() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a-b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongSub() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a-b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lsub\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatSub() {
        String text = "void func(float a, float b, float c) {\n" +
                "\tc=a-b;\n" +
                "}";

        test(text, "{\"func(float,float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fsub\"},{\"name\":\"_fstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleSub() {
        String text = "void func(double a, double b, double c) {\n" +
                "\tc=a-b;\n" +
                "}";

        test(text, "{\"func(double,double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dsub\"},{\"name\":\"_dstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharMul() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a*b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntMul() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a*b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongMul() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a*b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lmul\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatMul() {
        String text = "void func(float a, float b, float c) {\n" +
                "\tc=a*b;\n" +
                "}";

        test(text, "{\"func(float,float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fmul\"},{\"name\":\"_fstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleMul() {
        String text = "void func(double a, double b, double c) {\n" +
                "\tc=a*b;\n" +
                "}";

        test(text, "{\"func(double,double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_dmul\"},{\"name\":\"_dstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharDiv() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a/b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntDiv() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a/b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongDiv() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a/b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_ldiv\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatDiv() {
        String text = "void func(float a, float b, float c) {\n" +
                "\tc=a/b;\n" +
                "}";

        test(text, "{\"func(float,float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fdiv\"},{\"name\":\"_fstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleDiv() {
        String text = "void func(double a, double b, double c) {\n" +
                "\tc=a/b;\n" +
                "}";

        test(text, "{\"func(double,double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_ddiv\"},{\"name\":\"_dstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharRem() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a%b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntRem() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a%b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongRem() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a%b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lrem\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testFloatRem() {
        String text = "void func(float a, float b, float c) {\n" +
                "\tc=a%b;\n" +
                "}";

        test(text, "{\"func(float,float,float)\":[{\"name\":\"_fload\",\"order\":0},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_frem\"},{\"name\":\"_fstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testDoubleRem() {
        String text = "void func(double a, double b, double c) {\n" +
                "\tc=a%b;\n" +
                "}";

        test(text, "{\"func(double,double,double)\":[{\"name\":\"_dload\",\"order\":0},{\"name\":\"_dload\",\"order\":1},{\"name\":\"_drem\"},{\"name\":\"_dstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharShl() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a<<b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntShl() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a<<b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongShl() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a<<b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lshl\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharShr() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a>>b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntShr() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a>>b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongShr() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a>>b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lshr\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharUshr() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a>>>b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntUshr() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a>>>b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongUshr() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a>>>b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lushr\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharBitAnd() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a&b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntBitAnd() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a&b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongBitAnd() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a&b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_land\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharBitOr() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a|b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntBitOr() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a|b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongBitOr() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a|b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lor\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testCharBitXor() {
        String text = "void func(char a, char b, char c) {\n" +
                "\tc=a^b;\n" +
                "}";

        test(text, "{\"func(char,char,char)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testIntBitXor() {
        String text = "void func(int a, int b, int c) {\n" +
                "\tc=a^b;\n" +
                "}";

        test(text, "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}");
    }

    @Test
    public void testLongBitXor() {
        String text = "void func(long a, long b, long c) {\n" +
                "\tc=a^b;\n" +
                "}";

        test(text, "{\"func(long,long,long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_lxor\"},{\"name\":\"_lstore\",\"order\":2},{\"name\":\"_return\"}]}");
    }
}
