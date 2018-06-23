package org.liuyehcf.compile.engine.hua.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.HuaResult;
import org.liuyehcf.compile.engine.hua.definition.GrammarDefinition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author hechenfeng
 * @date 2018/5/31
 */
public class TestGrammar {

    private static LRCompiler<HuaResult> compiler;

    @BeforeClass
    public static void init() {
        long start, end;
        start = System.currentTimeMillis();
        compiler = new HuaCompiler(GrammarDefinition.GRAMMAR, GrammarDefinition.LEXICAL_ANALYZER);
        end = System.currentTimeMillis();
        System.out.println("build HuaCompiler consume " + (end - start) / 1000 + "s");

        assertTrue(compiler.isLegal());
    }

    @Test
    public void printMarkdown() {
        System.out.println(compiler.getFirstJSONString());
        System.out.println(compiler.getFollowJSONString());
        System.out.println(compiler.getClosureJSONString());
        System.out.println(compiler.getClosureTransferTableJSONString());
        System.out.println(compiler.getAnalysisTableMarkdownString());
    }

    @Test
    public void testNameWithKeyWord() {
        String text = "void doSomething() {\n" +
                "\n" +
                "}" +
                "void func() {\n" +
                "\tdoSomething();\n" +
                "}\n" +
                "\n";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"doSomething()\":[{\"name\":\"_return\"}],\"func()\":[{\"methodDescription\":\"doSomething()\",\"name\":\"_invokestatic\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testParamSize() {
        String text = "void func(int i, int j, int k) {\n" +
                "\tint temp = i;\n" +
                "\tj = k;\n" +
                "\tk = temp;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testAdd() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a+b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testSub() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a-b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testMul() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a*b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testDiv() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a/b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testRem() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a%b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testShl() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a<<b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testShr() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a>>b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testUshr() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a>>>b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testBitAnd() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a&b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testBitOr() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a|b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testBitXor() {
        String text = "void func(int a, int b) {\n" +
                "\tint c;\n" +
                "\tc=a^b;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testEqual() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i==j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":5,\"name\":\"_if_icmpne\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testNotEqual() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i!=j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":5,\"name\":\"_if_icmpeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testLess() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i<j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testLarge() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i>j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":5,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testLessEqual() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i<=j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":5,\"name\":\"_if_icmpgt\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testLargeEqual() {
        String text = "void func(int i, int j) {\n" +
                "\tif(i>=j)\n" +
                "\t\ti=1;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":5,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testConditionExpression() {
        String text = "void func(boolean a, int i, int j) {\n" +
                "\tint k= (a||i<j&&j>=3) ? i++:--j;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":11,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"offset\":12},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":11,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":4},{\"increment\":1,\"name\":\"_iinc\",\"offset\":4},{\"codeOffset\":13,\"name\":\"_goto\"},{\"increment\":-1,\"name\":\"_iinc\",\"offset\":12},{\"name\":\"_iload\",\"offset\":12},{\"name\":\"_istore\",\"offset\":20},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":16,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":12,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}],\"func2(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":18,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":18,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":16,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":12,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func2(boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":18,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":18,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":18,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_istore\",\"offset\":28},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":28},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_imul\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_imul\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_imul\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_idiv\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_idiv\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_idiv\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_irem\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_irem\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_irem\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iadd\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iadd\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_iadd\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_isub\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_isub\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_isub\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ishl\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_ishl\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ishl\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ishr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_ishr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ishr\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iushr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iushr\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_iushr\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iand\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iand\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_iand\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ixor\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_ixor\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ixor\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int[],int[][][])\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_dup2\"},{\"name\":\"_iaload\"},{\"name\":\"_aload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iaload\"},{\"name\":\"_ior\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testMethodInvocation() {
        String text = "void func1(){}\n" +
                "void func2(int a, int b){}\n" +
                "void func(int a, int b, int c) {\n" +
                "\tfunc1();\n" +
                "\tfunc2(a+b,c);\n" +
                "}\n";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1()\":[{\"name\":\"_return\"}],\"func2(int,int)\":[{\"name\":\"_return\"}],\"func(int,int,int)\":[{\"methodDescription\":\"func1()\",\"name\":\"_invokestatic\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":16},{\"methodDescription\":\"func2(int,int)\",\"name\":\"_invokestatic\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testVariableInitialize() {
        String text = "void func(int a,int b,int c) {\n" +
                "\tint d=a+b-c;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testMixBinaryOperator() {
        String text = "void func(int a, int b, int c, int d, int e, int f, int g, int h, int i) {\n" +
                "\tint j = a + b - c * d / e % f & g ^ h | i;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int,int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_imul\"},{\"name\":\"_iload\",\"offset\":32},{\"name\":\"_idiv\"},{\"name\":\"_iload\",\"offset\":40},{\"name\":\"_irem\"},{\"name\":\"_isub\"},{\"name\":\"_iload\",\"offset\":48},{\"name\":\"_iand\"},{\"name\":\"_iload\",\"offset\":56},{\"name\":\"_ixor\"},{\"name\":\"_iload\",\"offset\":64},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"offset\":72},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testMixBinaryOperatorWithSmallParenthesis() {
        String text = "void func(int a, int b, int c, int d, int e, int f, int g, int h, int i) {\n" +
                "\tint j = ((a + b) - ((c)) * d) / e % ((f) & g) ^ h | i;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int,int,int,int,int,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_imul\"},{\"name\":\"_isub\"},{\"name\":\"_iload\",\"offset\":32},{\"name\":\"_idiv\"},{\"name\":\"_iload\",\"offset\":40},{\"name\":\"_iload\",\"offset\":48},{\"name\":\"_iand\"},{\"name\":\"_irem\"},{\"name\":\"_iload\",\"offset\":56},{\"name\":\"_ixor\"},{\"name\":\"_iload\",\"offset\":64},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"offset\":72},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"increment\":-1,\"name\":\"_iinc\",\"offset\":0},{\"increment\":-1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":-1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"increment\":-1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iadd\"},{\"increment\":-1,\"name\":\"_iinc\",\"offset\":8},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_istore\",\"offset\":8},{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"increment\":1,\"name\":\"_iinc\",\"offset\":8},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"offset\":0},{\"increment\":-1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iload\",\"offset\":0},{\"increment\":-1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":8},{\"increment\":-1,\"name\":\"_iinc\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"offset\":0},{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_iload\",\"offset\":0},{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iload\",\"offset\":8},{\"increment\":1,\"name\":\"_iinc\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testDecimalLiteral() {
        String text = "void func() {\n" +
                "\tint a=5,b=100000;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iconst\",\"value\":100000},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testBooleanLiteral() {
        String text = "void func() {\n" +
                "\tboolean a=true,b=false;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testArrayLoad1() {
        String text = "void func(int[] a) {\n" +
                "\tint b=a[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int[])\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testArrayLoad2() {
        String text = "void func(int[][] a,int j) {\n" +
                "\tj=a[5][6];\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int[][],int)\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int[])\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":10000},{\"name\":\"_iastore\"},{\"name\":\"_return\"}],\"func2(int[])\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":100},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_isub\"},{\"name\":\"_imul\"},{\"name\":\"_iaload\"},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testArrayStore2() {
        String text = "void func(int[][] a,int j) {\n" +
                "\ta[4][5]=5;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int[][],int)\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int[][][][])\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_aaload\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":3},{\"expressionDimSize\":2,\"name\":\"_multianewarray\",\"type\":\"int[][]\"},{\"name\":\"_aastore\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"expressionDimSize\":3,\"name\":\"_multianewarray\",\"type\":\"int[][][]\"},{\"name\":\"_aastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":4},{\"name\":\"_return\"}],\"func2(boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"offset\":4},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}],\"func2(boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}],\"func3(boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":8},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}],\"func4(boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":8},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":6,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testComplexConditionIf4() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tif((a||b)&&(c||d)){int e=3;}\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func5(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func6(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":9,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":5,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":15,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func5(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":17,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":19,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":7},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":8},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":9},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func6(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":19,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":7},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":8},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":9},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":24},{\"codeOffset\":21,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func7(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":16,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func8(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":16,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":13,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":13,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":11,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"codeOffset\":18,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":15,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":4},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":17,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":4},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"codeOffset\":19,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testComplexConditionAssign1() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e= a||b&&c||d;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":6,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testComplexConditionAssign2() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e= a&&b||c&&d;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testComplexConditionAssign3() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e= a&&b&&c&&d ,f ;\n" +
                "\tf = a||b||c||d;\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":20,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":20,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":20,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":22,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":20},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testComplexConditionAssign4() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e;\n" +
                "\te = (a||b)&&(c||d);\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":4,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":8,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testComplexConditionAssign5() {
        String text = "void func(boolean a, boolean b, boolean c, boolean d){\n" +
                "\tboolean e;\n" +
                "\te = (a && b)||((c && d) ||(c)) && (d);\n" +
                "}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(boolean,boolean,boolean,boolean)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":4,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":12,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":8,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":10,\"name\":\"_ifne\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":12},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func2(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}],\"func3(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":10,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":7,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func4(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":14,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":4,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_return\"}],\"func5(boolean,boolean,boolean,boolean,int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"codeOffset\":12,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":4},{\"codeOffset\":9,\"name\":\"_ifeq\"},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":2,\"name\":\"_ifeq\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":16},{\"codeOffset\":4,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":24},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmplt\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}],\"func2(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":15,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}],\"func3(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}],\"func4(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":10,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}],\"func5(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":14,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}],\"func6(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":11,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"codeOffset\":13,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_return\"}],\"func7(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":13,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmplt\"},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"offset\":8},{\"codeOffset\":15,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":36,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":3,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":20},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"codeOffset\":3,\"name\":\"_goto\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":36,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1000},{\"codeOffset\":36,\"name\":\"_if_icmpgt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"codeOffset\":23,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_if_icmpgt\"},{\"name\":\"_return\"}],\"func2(int,int)\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":23,\"name\":\"_if_icmplt\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":0,\"name\":\"_if_icmple\"},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":20},{\"codeOffset\":6,\"name\":\"_if_icmplt\"},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_istore\",\"offset\":8},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"codeOffset\":0,\"name\":\"_goto\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":8,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"increment\":1,\"name\":\"_iinc\",\"offset\":8},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_astore\",\"offset\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_anewarray\",\"type\":\"int[][][][]\"},{\"name\":\"_astore\",\"offset\":8},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_iconst\",\"value\":4},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_iconst\",\"value\":6},{\"name\":\"_iconst\",\"value\":7},{\"expressionDimSize\":5,\"name\":\"_multianewarray\",\"type\":\"int[][][][][]\"},{\"name\":\"_astore\",\"offset\":16},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
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

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":13,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"offset\":0},{\"increment\":1,\"name\":\"_iinc\",\"offset\":8},{\"increment\":1,\"name\":\"_iinc\",\"offset\":8},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":8},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }

    @Test
    public void testQuickSort() {
        String text = "\tvoid exchange(int[] nums, int i, int j) {\n" +
                "\t\tint temp = nums[i];\n" +
                "\t\tnums[i] = nums[j];\n" +
                "\t\tnums[j] = temp;\n" +
                "\t}\n" +
                "\tint partition(int[] nums, int lo, int hi) {\n" +
                "\t\tint i = lo - 1;\n" +
                "\t\tint pivot = nums[hi];\n" +
                "\t\t\n" +
                "\t\tfor (int j = lo; j < hi; j++) {\n" +
                "\t\t\tif (nums[j] < pivot) {\n" +
                "\t\t\t\texchange(nums, ++i, j);\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\t\n" +
                "\t\texchange(nums, ++i, hi);\n" +
                "\t\t\n" +
                "\t\treturn i;\n" +
                "\t}\n" +
                "\t\n" +
                "\tvoid sort(int[] nums, int lo, int hi) {\n" +
                "\t\tif (lo < hi) {\n" +
                "\t\t\tint mid = partition(nums, lo, hi);\n" +
                "\t\t\tsort(nums, lo, mid - 1);\n" +
                "\t\t\tsort(nums, mid + 1, hi);\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\t\n" +
                "\tvoid sort(int[] nums, int size) {\n" +
                "\t\tsort(nums, 0, size-1);\n" +
                "\t}";

        System.out.println(text);

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"exchange(int[],int,int)\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iaload\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_iastore\"},{\"name\":\"_return\"}],\"partition(int[],int,int)\":[{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"offset\":32},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_istore\",\"offset\":40},{\"name\":\"_iload\",\"offset\":40},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":25,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":40},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"offset\":32},{\"codeOffset\":23,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"offset\":0},{\"increment\":1,\"name\":\"_iinc\",\"offset\":24},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_iload\",\"offset\":40},{\"methodDescription\":\"exchange(int[],int,int)\",\"name\":\"_invokestatic\"},{\"increment\":1,\"name\":\"_iinc\",\"offset\":40},{\"codeOffset\":10,\"name\":\"_goto\"},{\"name\":\"_aload\",\"offset\":0},{\"increment\":1,\"name\":\"_iinc\",\"offset\":24},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_iload\",\"offset\":16},{\"methodDescription\":\"exchange(int[],int,int)\",\"name\":\"_invokestatic\"},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_ireturn\"}],\"sort(int[],int,int)\":[{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":16},{\"methodDescription\":\"partition(int[],int,int)\",\"name\":\"_invokestatic\"},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"methodDescription\":\"sort(int[],int,int)\",\"name\":\"_invokestatic\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":16},{\"methodDescription\":\"sort(int[],int,int)\",\"name\":\"_invokestatic\"},{\"name\":\"_return\"}],\"sort(int[],int)\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"methodDescription\":\"sort(int[],int,int)\",\"name\":\"_invokestatic\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleString()
        );
    }
}