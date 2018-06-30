package org.liuyehcf.compile.engine.hua.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.compile.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.compile.HuaCompiler.HUA_PATH_PROPERTY;


/**
 * 测试
 *
 * @author hechenfeng
 * @date 2018/5/31
 */
public class TestGrammar {

    private static LRCompiler<IntermediateInfo> compiler;

    @BeforeClass
    public static void init() {
        System.setProperty(HUA_PATH_PROPERTY, "./src/main/resources/");

        long start, end;
        start = System.currentTimeMillis();

        compiler = HuaCompiler.getHuaCompiler();

        end = System.currentTimeMillis();
        System.out.println("build HuaCompiler consume " + (end - start) / 1000 + "s");

        assertTrue(compiler.isLegal());
    }

    @Test
    public void testProductions() {
        System.out.println(GrammarDefinition.GRAMMAR);
    }

    @Test
    public void testFirstJSONString() {
        System.out.println(compiler.getFirstJSONString());
    }

    @Test
    public void testFollowJSONString() {
        System.out.println(compiler.getFollowJSONString());
    }

    @Test
    public void testClosureJSONString() {
        System.out.println(compiler.getClosureJSONString());
    }

    @Test
    public void testClosureTransferTableJSONString() {
        System.out.println(compiler.getClosureTransferTableJSONString());
    }

    @Test
    public void testMarkDownString() {
        System.out.println(compiler.getAnalysisTableMarkdownString());
    }

    @Test
    public void testParamSize() {
        String text = "void func(int i, int j, int k) {\n" +
                "\tint temp = i;\n" +
                "\tj = k;\n" +
                "\tk = temp;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testTypeLong() {
        String text = "void func() {\n" +
                "\tlong[] a=new long[5];\n" +
                "\ta[1]=1L;\n" +
                "\tlong b=a[2];\n" +
                "\tb=1000000L;\n" +
                "\tb+=a[3];\n" +
                "\ta[3]=a[2]<<a[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"long\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_laload\"},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_lconst\",\"value\":1000000},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_laload\"},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_laload\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_laload\"},{\"name\":\"_lshl\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testAdd() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a+b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSub() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a-b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testMul() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a*b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testDiv() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a/b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testRem() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a%b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testShl() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a<<b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testShr() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a>>b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testUshr() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a>>>b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBitAnd() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a&b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBitOr() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a|b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBitXor() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a^b;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testEqual() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i==j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpne\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNotEqual() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i!=j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLess() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i<j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLarge() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i>j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLessEqual() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i<=j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmpgt\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testLargeEqual() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i>=j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":5,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testConditionExpression() {
        String text = "void func(boolean a, int i, int j) {\n" +
                "\tint k= (a||i<j&&j>=3) ? i++:--j;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":11,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":11,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"increment\":-1,\"name\":\"_iinc\",\"order\":2},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_return\"}]}",
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":16,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":12,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func2(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":18,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":9,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":18,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testVariableInitialize() {
        String text = "void func(int a,int b,int c) {\n" +
                "\tint d=a+b-c;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testMixBinaryOperator() {
        String text = "void func(int a, int b, int c, int d, int e, int f, int g, int h, int i) {\n" +
                "\tint j = a + b - c * d / e % f & g ^ h | i;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int,int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_imul\"},{\"name\":\"_isub\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_idiv\"},{\"name\":\"_iload\",\"order\":5},{\"name\":\"_iload\",\"order\":6},{\"name\":\"_iand\"},{\"name\":\"_irem\"},{\"name\":\"_iload\",\"order\":7},{\"name\":\"_ixor\"},{\"name\":\"_iload\",\"order\":8},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"order\":9},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSimpleIncrementDecrement() {
        String text = "void func(int i) {\n" +
                "\t++i;\n" +
                "\ti++;\n" +
                "\t--i;\n" +
                "\ti--;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testPreDecrement() {
        String text = "void func(int i) {\n" +
                "\tint j=--i;\n" +
                "\n" +
                "\tj=(1+--i)+(--j+3);\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iadd\"},{\"increment\":-1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testPreIncrement() {
        String text = "void func(int i) {\n" +
                "\tint j=++i;\n" +
                "\n" +
                "\tj=(++i-1)+(++j+3);\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_istore\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testPostDecrement() {
        String text = "void func(int i) {\n" +
                "\tint j=i--;\n" +
                "\n" +
                "\tj=(1+i--)+(j--+3);\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iload\",\"order\":0},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":1},{\"increment\":-1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testPostIncrement() {
        String text = "void func(int i) {\n" +
                "\tint j=i++;\n" +
                "\n" +
                "\tj=((i++)+1)+(3+j++);\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iload\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testDecimalLiteral() {
        String text = "void func() {\n" +
                "\tint a=5,b=100000;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":100000},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testHexLiteral() {
        String text = "void func() {\n" +
                "\tint a=0X005,b=0xffff;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":65535},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testOctalLiteral() {
        String text = "void func() {\n" +
                "\tint a=0005,b=0765;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":501},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testBooleanLiteral() {
        String text = "void func() {\n" +
                "\tboolean a=true,b=false;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCharLiteral() {
        String text = "void func() {\n" +
                "\tchar c='1';\n" +
                "\tc='a';\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testStringLiteral() {
        String text = "void func() {\n" +
                "\tchar[] c=\"abcdefg\";\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"constantPoolOffset\":20,\"name\":\"_ldc\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testArrayLoad1() {
        String text = "void func(int[] a) {\n" +
                "\tint b=a[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean[][])\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_bastore\"},{\"name\":\"_return\"}]}",
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":15,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":17,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testComplexConditionAssign1() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e= a||b&&c||d;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":12,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":3},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSimpleWhile1() {
        String text = "void func1(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\twhile(a){\n" +
                "\t\ti=i-1;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testNestedIfWhile1() {
        String text = "void func1(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a){\n" +
                "\t\twhile(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tj=1;\n" +
                "}\n" +
                "\n" +
                "void func2(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a)\n" +
                "\t\twhile(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\telse\n" +
                "\t\ti=2;\n" +
                "\tj=1;\n" +
                "}\n" +
                "\n" +
                "void func3(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a){\n" +
                "\t\twhile(b){\n" +
                "\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\telse\n" +
                "\t\ti=2;\n" +
                "}\n" +
                "\n" +
                "void func4(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\tif(a)\n" +
                "\t\tif(c){\n" +
                "\t\t\twhile(b){\n" +
                "\t\t\t\ti=1;\n" +
                "\t\t\t}\n" +
                "\t\t\tj=1;\n" +
                "\t\t}\n" +
                "\telse {\n" +
                "\t\ti=2;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void func5(boolean a, boolean b, boolean c, boolean d, int i, int j){\n" +
                "\twhile(a){\n" +
                "\t\twhile(b){\n" +
                "\t\t\twhile(c)\n" +
                "\t\t\t\ti=1;\n" +
                "\t\t}\n" +
                "\t\tj=1;\n" +
                "\t}\n" +
                "\tj=2;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":4,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_return\"}],\"func5(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":2,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":4},{\"codeOffset\":4,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":5},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSimpleDoWhile1() {
        String text = "void func1(int i) {\n" +
                "\tdo{\n" +
                "\t\ti=i+1;\n" +
                "\t}while(i<10);\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":36,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":20},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"codeOffset\":3,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":36,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1000},{\"codeOffset\":36,\"name\":\"_if_icmpgt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":23,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_if_icmpgt\"},{\"name\":\"_return\"}],\"func2(int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":20},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSimpleFor1() {
        String text = "void func(int i) {\n" +
                "\tfor(;;){\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSimpleFor2() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<100;i++){\n" +
                "\t\tj++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":8,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
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

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_anewarray\",\"type\":\"int[][][][]\"},{\"name\":\"_astore\",\"order\":1},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_iconst\",\"value\":7},{\"expressionDimSize\":5,\"name\":\"_multianewarray\",\"type\":\"int[][][][][]\"},{\"name\":\"_astore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSimpleFor3() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<100;i++,i++,i=i+1){\n" +
                "\t\tj++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":13,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testQuickSort() {
        String text = "void exchange(int[] nums, int i, int j) {\n" +
                "\tint temp = nums[i];\n" +
                "\tnums[i] = nums[j];\n" +
                "\tnums[j] = temp;\n" +
                "}\n" +
                "\n" +
                "int partition(int[] nums, int lo, int hi) {\n" +
                "\tint i = lo - 1;\n" +
                "\tint pivot = nums[hi];\n" +
                "\t\n" +
                "\tfor (int j = lo; j < hi; j++) {\n" +
                "\t\tif (nums[j] < pivot) {\n" +
                "\t\t\texchange(nums, ++i, j);\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\n" +
                "\texchange(nums, ++i, hi);\n" +
                "\t\n" +
                "\treturn i;\n" +
                "}\n" +
                "\n" +
                "void sort(int[] nums, int lo, int hi) {\n" +
                "\tif (lo < hi) {\n" +
                "\t\tint mid = partition(nums, lo, hi);\n" +
                "\t\tsort(nums, lo, mid - 1);\n" +
                "\t\tsort(nums, mid + 1, hi);\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void sort(int[] nums, int size) {\n" +
                "\tsort(nums, 0, size-1);\n" +
                "}\n" +
                "\n" +
                "void main(){\n" +
                "\tint[] nums=new int[200];\n" +
                "\n" +
                "\tfor(int i=0;i<200;i++){\n" +
                "\t\tnums[i]=nextInt(0,25);\n" +
                "\t}\n" +
                "\n" +
                "\tfor(int i=0;i<200;i++){\n" +
                "\t\tprint(nums[i]);\n" +
                "\t}\n" +
                "\n" +
                "\tsort(nums,200);\n" +
                "\n" +
                "\tfor(int i=0;i<200;i++){\n" +
                "\t\tprint(nums[i]);\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"exchange(int[],int,int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iaload\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iastore\"},{\"name\":\"_return\"}],\"partition(int[],int,int)\":[{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_iload\",\"order\":5},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":25,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":5},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":4},{\"codeOffset\":23,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":3},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iload\",\"order\":5},{\"constantPoolOffset\":19,\"name\":\"_invokestatic\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":5},{\"codeOffset\":10,\"name\":\"_goto\"},{\"name\":\"_aload\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":3},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iload\",\"order\":2},{\"constantPoolOffset\":19,\"name\":\"_invokestatic\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_ireturn\"}],\"sort(int[],int,int)\":[{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":2},{\"constantPoolOffset\":20,\"name\":\"_invokestatic\"},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"constantPoolOffset\":21,\"name\":\"_invokestatic\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":2},{\"constantPoolOffset\":21,\"name\":\"_invokestatic\"},{\"name\":\"_return\"}],\"sort(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"constantPoolOffset\":21,\"name\":\"_invokestatic\"},{\"name\":\"_return\"}],\"main()\":[{\"name\":\"_iconst\",\"value\":200},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":200},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_iconst\",\"value\":25},{\"constantPoolOffset\":16,\"name\":\"_invokestatic\"},{\"name\":\"_iastore\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":5,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":200},{\"codeOffset\":27,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iaload\"},{\"constantPoolOffset\":2,\"name\":\"_invokestatic\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":18,\"name\":\"_goto\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":200},{\"constantPoolOffset\":22,\"name\":\"_invokestatic\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":200},{\"codeOffset\":41,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iaload\"},{\"constantPoolOffset\":2,\"name\":\"_invokestatic\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":32,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}