package org.liuyehcf.compile.engine.hua.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.GrammarDefinition;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.test.TestCases.*;


/**
 * @author hechenfeng
 * @date 2018/5/31
 */
public class TestGrammar {

    private static final List<String> RIGHT_CASES = new ArrayList<>();
    private static LRCompiler compiler;

    static {
        RIGHT_CASES.addAll(Arrays.asList(WHILE_CASES));
        RIGHT_CASES.addAll(Arrays.asList(FOR_CASES));
        RIGHT_CASES.addAll(Arrays.asList(DO_CASES));
        RIGHT_CASES.addAll(Arrays.asList(IF_CASES));
        RIGHT_CASES.addAll(Arrays.asList(VARIABLE_DECLARATION_CASES));
        RIGHT_CASES.addAll(Arrays.asList(OPERATOR_CASES));
        RIGHT_CASES.addAll(Arrays.asList(CLASSIC_CASES));
    }

    @BeforeClass
    public static void init() {
        long start, end;
        start = System.currentTimeMillis();
        compiler = new HuaCompiler(GrammarDefinition.GRAMMAR, GrammarDefinition.LEXICAL_ANALYZER);
        end = System.currentTimeMillis();
        System.out.println("build HuaCompiler consume " + (end - start) / 1000 + "s");

        assertTrue(compiler.isLegal());
    }

    public void testRightCases() {
        for (String rightCase : RIGHT_CASES) {
            System.out.println(rightCase);
            assertTrue(compiler.compile(rightCase).isSuccess());
        }
    }

    public void printMarkdown() {
        System.out.println(compiler.getAnalysisTableMarkdownString());
    }


    @Test
    public void testNameWithKeyWord() {
        assertTrue(compiler.compile("void doSomething(){\n" +
                "  doSomething();\n" +
                "  int intI;\n" +
                "}").isSuccess());
    }

    @Test
    public void testPostIncrement() {
        assertTrue(compiler.compile("void testPostIncrement(){\n" +
                "  int a;\n" +
                "  a++;\n" +
                "}").isSuccess());
    }

    @Test
    public void testParamSize() {
        assertTrue(compiler.compile("void add(int i, int j, int k) {\n" +
                "        int temp = i;\n" +
                "        j = k;\n" +
                "        k = temp;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testAdd() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a+b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testSub() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a-b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testMul() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a*b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testDiv() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a/b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testRem() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a%b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testShl() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a<<b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testShr() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a>>b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testUshr() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a>>>b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testBitAnd() {
        assertTrue(compiler.compile("int func() {\n" +
                "        int a,b,c;\n" +
                "        c=a&b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testBitOr() {
        assertTrue(compiler.compile("boolean func() {\n" +
                "        int a,b,c;\n" +
                "        c=a|b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testBitXor() {
        assertTrue(compiler.compile("void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a^b;\n" +
                "    }").isSuccess());
    }

    @Test
    public void testNormalAssign() {
        assertTrue(compiler.compile("void func() {\n" +
                "  int a,b,c;\n" +
                "  c=a+b-a;\n" +
                "}").isSuccess());
    }

    @Test
    public void testMethodInvocation() {
        assertTrue(compiler.compile("void testMethodInvocation() {\n" +
                "    int a,b,c;\n" +
                "    func1();\n" +
                "    func2(a+b,c);\n" +
                "}").isSuccess());
    }

    @Test
    public void testVariableInitialize() {
        assertTrue(compiler.compile("void testVariableInitialize() {\n" +
                "    int a,b,c;\n" +
                "    int d=a+b-c;\n" +
                "}").isSuccess());
    }

    @Test
    public void testMixBinaryOperator() {
        assertTrue(compiler.compile("void testMixBinaryOperator() {\n" +
                "    int a, b, c, d, e, f, g, h, i;\n" +
                "    int j = a + b - c * d / e % f & g ^ h | i;\n" +
                "}").isSuccess());
    }

    @Test
    public void testDecimalLiteral() {
        assertTrue(compiler.compile("void testDecimalLiteral() {\n" +
                "    int a=5,b=100000;\n" +
                "}").isSuccess());
    }
}