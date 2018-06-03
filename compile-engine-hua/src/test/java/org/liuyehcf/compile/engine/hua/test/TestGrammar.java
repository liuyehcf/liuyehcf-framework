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
        assertTrue(compiler.compile("void sort(int[] nums, int size) {\n" +
                "        sort(nums, 0, size-1);\n" +
                "    }\n" +
                "\n" +
                "    void sort(int[] nums, int lo, int hi) {\n" +
                "        if (lo < hi) {\n" +
                "            int mid = partition(nums, lo, hi);\n" +
                "            sort(nums, lo, mid - 1);\n" +
                "            sort(nums, mid + 1, hi);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    int partition(int[] nums, int lo, int hi) {\n" +
                "        int i = lo - 1;\n" +
                "        int pivot = nums[hi];\n" +
                "\n" +
                "        for (int j = lo; j < hi; j++) {\n" +
                "            if (nums[j] < pivot) {\n" +
                "                exchange(nums, ++i, j);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        exchange(nums, ++i, hi);\n" +
                "\n" +
                "        return i;\n" +
                "    }\n" +
                "\n" +
                "    void exchange(int[] nums, int i, int j) {\n" +
                "        int temp = nums[i];\n" +
                "        nums[i] = nums[j];\n" +
                "        nums[j] = temp;\n" +
                "    }\n").isSuccess());
    }
}