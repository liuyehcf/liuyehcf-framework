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
    public void testRightCases() {
        for (String rightCase : RIGHT_CASES) {
            System.out.println(rightCase);
            assertTrue(compiler.compile(rightCase).isSuccess());
        }
    }

    @Test
    public void printMarkdown() {
        System.out.println(compiler.getAnalysisTableMarkdownString());
    }

    @Test
    public void testPostIncrement() {
        assertTrue(compiler.compile("void testPostIncrement(){\n" +
                "  int a=1;\n" +
                "  a++;\n" +
                "}").isSuccess());
    }

    @Test
    public void testParamSize(){
        assertTrue(compiler.compile("void exchange(int[] nums, int i, int j) {\n" +
                "        int temp = nums[i];\n" +
                "        nums[i] = nums[j];\n" +
                "        nums[j] = temp;\n" +
                "    }").isSuccess());
    }
}