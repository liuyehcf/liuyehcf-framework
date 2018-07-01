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
public class TestAssignment {
    @Test
    public void testNormalAssign() {
        String text = "void func(int a,int b) {\n" +
                "\tint c;\n" +
                "\tc=a+b-a;\n" +
                "\tboolean d=true,f;\n" +
                "\tf=d;\n" +
                "\tf=false;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testMulAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti *= 3;\n" +
                "\ta[1] *= 5;\n" +
                "\tb[1][2][3] *= i;\n" +
                "\tb[1][2][3] *= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_imul\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_imul\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_imul\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testDivAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti /= 3;\n" +
                "\ta[1] /= 5;\n" +
                "\tb[1][2][3] /= i;\n" +
                "\tb[1][2][3] /= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_idiv\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_idiv\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_idiv\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testRemAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti %= 3;\n" +
                "\ta[1] %= 5;\n" +
                "\tb[1][2][3] %= i;\n" +
                "\tb[1][2][3] %= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_irem\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_irem\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_irem\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testAddAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti += 3;\n" +
                "\ta[1] += 5;\n" +
                "\tb[1][2][3] += i;\n" +
                "\tb[1][2][3] += a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iadd\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iadd\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_iadd\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSubAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti -= 3;\n" +
                "\ta[1] -= 5;\n" +
                "\tb[1][2][3] -= i;\n" +
                "\tb[1][2][3] -= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_isub\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_isub\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_isub\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testShlAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti <<= 3;\n" +
                "\ta[1] <<= 5;\n" +
                "\tb[1][2][3] <<= i;\n" +
                "\tb[1][2][3] <<= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ishl\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_ishl\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ishl\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testShrAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti >>= 3;\n" +
                "\ta[1] >>= 5;\n" +
                "\tb[1][2][3] >>= i;\n" +
                "\tb[1][2][3] >>= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ishr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_ishr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ishr\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testUshrAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti >>>= 3;\n" +
                "\ta[1] >>>= 5;\n" +
                "\tb[1][2][3] >>>= i;\n" +
                "\tb[1][2][3] >>>= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iushr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iushr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_iushr\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBitAndAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti &= 3;\n" +
                "\ta[1] &= 5;\n" +
                "\tb[1][2][3] &= i;\n" +
                "\tb[1][2][3] &= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iand\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iand\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_iand\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBitXorAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti ^= 3;\n" +
                "\ta[1] ^= 5;\n" +
                "\tb[1][2][3] ^= i;\n" +
                "\tb[1][2][3] ^= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ixor\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_ixor\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ixor\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBitOrAssign() {
        String text = "void func(int i, int[] a, int[][][] b) {\n" +
                "\ti |= 3;\n" +
                "\ta[1] |= 5;\n" +
                "\tb[1][2][3] |= i;\n" +
                "\tb[1][2][3] |= a[5];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
