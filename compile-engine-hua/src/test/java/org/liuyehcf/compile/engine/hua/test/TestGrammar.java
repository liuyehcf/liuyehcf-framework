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
        compiler = HuaCompiler.create(GrammarDefinition.LEXICAL_ANALYZER, GrammarDefinition.GRAMMAR);
        end = System.currentTimeMillis();
        System.out.println("build HuaCompiler consume " + (end - start) / 1000 + "s");

        assertTrue(compiler.isLegal());
    }

    @Test
    public void testCase1() {
        for (String rightCase : RIGHT_CASES) {
            System.out.println(rightCase);
            assertTrue(compiler.compile(rightCase).isSuccess());
        }
    }

    @Test
    public void testCase2() {
        System.out.println(compiler.getAnalysisTableMarkdownString());
    }

    @Test
    public void test() {
        assertTrue(compiler.compile("void testFor(){\n" +
                "  for(;;)\n" +
                "    something();\n" +
                "\n" +
                "  for(;;){\n" +
                "    something();\n" +
                "  }\n" +
                "\n" +
                "  for(int a,b;;)\n" +
                "    something();\n" +
                "\n" +
                "  for(int b=3;;){\n" +
                "    something();\n" +
                "  }\n" +
                "\n" +
                "  for(;true;)\n" +
                "    something();\n" +
                "\n" +
                "  for(;;i++,j++)\n" +
                "    something();\n" +
                "\n" +
                "  for(int a=3;b<6;i++,j++){\n" +
                "    something();\n" +
                "  }\n" +
                "}").isSuccess());
    }
}