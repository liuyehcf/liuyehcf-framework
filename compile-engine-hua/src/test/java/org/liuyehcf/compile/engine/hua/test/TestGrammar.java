package org.liuyehcf.compile.engine.hua.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.GrammarDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.test.TestCases.*;


/**
 * @author chenlu
 * @date 2018/5/31
 */
public class TestGrammar {

    private static final List<String> RIGHT_CASES = new ArrayList<>();
    private static final List<String> WRONG_CASES = new ArrayList<>();

    static {
        RIGHT_CASES.addAll(Arrays.asList(WHILE_CASES));
        RIGHT_CASES.addAll(Arrays.asList(FOR_CASES));
        RIGHT_CASES.addAll(Arrays.asList(DO_CASES));
        RIGHT_CASES.addAll(Arrays.asList(IF_CASES));
        RIGHT_CASES.addAll(Arrays.asList(VARIABLE_DECLARATION_CASES));
        RIGHT_CASES.addAll(Arrays.asList(OPERATOR_CASES));
        RIGHT_CASES.addAll(Arrays.asList(CLASSIC_CASES));
    }

//    private static final String[] RIGHT_CASES = {
//            "void sort(int a){}",
//            "int sort(int a){{{}}}",
//            "float sort(int a,float c, boolean d){}",
//            "void exchange(int a,int b){\n" +
//                    "    int a, b[][];\n" +
//                    "    a=b;\n" +
//                    "    b=c;\n" +
//                    "}\n",
//            "void sort(int a){}\n" +
//                    "\n" +
//                    "int add(int a,int b){;;;}",
//            "void exchange(int a,int b){\n" +
//                    "    if(a){\n" +
//                    "      b=c;\n" +
//                    "    }\n" +
//                    "}",
//            "void exchange(int a,int b){\n" +
//                    "    if(a){\n" +
//                    "      b=c;\n" +
//                    "      if(d){\n" +
//                    "        ;\n" +
//                    "      }\n" +
//                    "    }else{\n" +
//                    "      a=b;\n" +
//                    "    }\n" +
//                    "}",
//            "void exchange(int a,int b){\n" +
//                    "    if(a){\n" +
//                    "      b=c;\n" +
//                    "      if(d){\n" +
//                    "        while(a){\n" +
//                    "          if(c)\n" +
//                    "            ;\n" +
//                    "        }\n" +
//                    "      }\n" +
//                    "    }else{\n" +
//                    "      a=b;\n" +
//                    "    }\n" +
//                    "}",
//            "void exchange(int a,int b){\n" +
//                    "    if(a){\n" +
//                    "      b=c;\n" +
//                    "      if(d){\n" +
//                    "        while(a){\n" +
//                    "          if(c)\n" +
//                    "            ;\n" +
//                    "        }\n" +
//                    "      }\n" +
//                    "    }else{\n" +
//                    "      a=b;\n" +
//                    "      for(;;){\n" +
//                    "        \n" +
//                    "      }\n" +
//                    "    }\n" +
//                    "}",
//            "int testFor(){\n" +
//                    "  for(int a,b,c,d,e,f,g;c;a=b,c=d,e=f){\n" +
//                    "\n" +
//                    "  }\n" +
//                    "}",
//            "void exchange(int a,int b){\n" +
//                    "    if(a){\n" +
//                    "      b=c;\n" +
//                    "      if(d){\n" +
//                    "        while(a){\n" +
//                    "          if(c)\n" +
//                    "            ;\n" +
//                    "        }\n" +
//                    "      }\n" +
//                    "    }else{\n" +
//                    "      a=b;\n" +
//                    "      for(;;){\n" +
//                    "        for(int a,b;;a=b){\n" +
//                    "          for(;b;a=e,b=c){\n" +
//                    "            ;\n" +
//                    "          }\n" +
//                    "        }\n" +
//                    "      }\n" +
//                    "    }\n" +
//                    "}",
//            "void func(){\n" +
//                    "  int h = ++a * b++ / c-- % --d + e - f << g >> h >>> ~j;\n" +
//                    "}",
//            "void func(){\n" +
//                    "  int[] a = new int[c];\n" +
//                    "}",
//            "void exchange(int[] nums, int i, int j) {\n" +
//                    "        int temp = nums[i];\n" +
//                    "        nums[i] = nums[j];\n" +
//                    "        nums[j] = temp;\n" +
//                    "    }",
//            "int partition(int[] nums, int lo, int hi) {\n" +
//                    "        int i = lo - 1;\n" +
//                    "        int pivot = nums[hi];\n" +
//                    "\n" +
//                    "        for (int j = lo; j < hi; j++) {\n" +
//                    "            if (nums[j] < pivot) {\n" +
//                    "                exchange(nums, ++i, j);\n" +
//                    "            }\n" +
//                    "        }\n" +
//                    "\n" +
//                    "        exchange(nums, ++i, hi);\n" +
//                    "\n" +
//                    "        return i;\n" +
//                    "    }",
//            "void sort(int[] nums, int size) {\n" +
//                    "        sort(nums, 0, size-1);\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    void sort(int[] nums, int lo, int hi) {\n" +
//                    "        if (lo < hi) {\n" +
//                    "            int mid = partition(nums, lo, hi);\n" +
//                    "            sort(nums, lo, mid - 1);\n" +
//                    "            sort(nums, mid + 1, hi);\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    int partition(int[] nums, int lo, int hi) {\n" +
//                    "        int i = lo - 1;\n" +
//                    "        int pivot = nums[hi];\n" +
//                    "\n" +
//                    "        for (int j = lo; j < hi; j++) {\n" +
//                    "            if (nums[j] < pivot) {\n" +
//                    "                exchange(nums, ++i, j);\n" +
//                    "            }\n" +
//                    "        }\n" +
//                    "\n" +
//                    "        exchange(nums, ++i, hi);\n" +
//                    "\n" +
//                    "        return i;\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    void exchange(int[] nums, int i, int j) {\n" +
//                    "        int temp = nums[i];\n" +
//                    "        nums[i] = nums[j];\n" +
//                    "        nums[j] = temp;\n" +
//                    "    }"
//    };

//    private static final String[] WRONG_CASES = {
//            "sort(int a){}",
//            "void sort(int a){}}",
//            "void sort(int a,b){}",
//            "void exchange(int a,int b){\n" +
//                    "    int a, b[][]]];\n" +
//                    "    a=b;\n" +
//                    "    b=c;\n" +
//                    "}"
//    };

    private static LRCompiler compilerLR1;

    private static LRCompiler compilerLALR;

    @BeforeClass
    public static void init() {
        long start, end;
        start = System.currentTimeMillis();
        compilerLR1 = LR1.create(GrammarDefinition.LEXICAL_ANALYZER, GrammarDefinition.GRAMMAR);
        end = System.currentTimeMillis();
        System.out.println("build LR1 consume " + (end - start) / 1000 + "s");

        start = System.currentTimeMillis();
        compilerLALR = LALR1.create(GrammarDefinition.LEXICAL_ANALYZER, GrammarDefinition.GRAMMAR);
        end = System.currentTimeMillis();
        System.out.println("build LALR1 consume " + (end - start) / 1000 + "s");

        assertTrue(compilerLR1.isLegal());
        assertTrue(compilerLALR.isLegal());
    }

    @Test
    public void testCase1() {
        for (String rightCase : RIGHT_CASES) {
            System.out.println(rightCase);
            assertTrue(compilerLR1.compile(rightCase).isSuccess());
            assertTrue(compilerLALR.compile(rightCase).isSuccess());
        }

        for (String wrongCase : WRONG_CASES) {
            assertFalse(compilerLR1.compile(wrongCase).isSuccess());
            assertFalse(compilerLALR.compile(wrongCase).isSuccess());
        }
    }

    @Test
    public void testCase2() {
        System.out.println(compilerLR1.getAnalysisTableMarkdownString());
        System.out.println(compilerLALR.getAnalysisTableMarkdownString());
    }


    @Test
    public void test() {
        System.out.println(compilerLR1.compile("void testWhile(){\n" +
                "    doSomething(1);\n" +
                "}").isSuccess());
    }
}
