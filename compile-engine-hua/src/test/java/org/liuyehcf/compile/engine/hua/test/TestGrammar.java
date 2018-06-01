package org.liuyehcf.compile.engine.hua.test;

import org.junit.Before;
import org.junit.Test;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.GrammarDefinition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * @author chenlu
 * @date 2018/5/31
 */
public class TestGrammar {

    private static final String[] RIGHT_CASES = {
            "void sort(int a){}",
            "int sort(int a){{{}}}",
            "float sort(int a,float c, boolean d){}",
            "void exchange(int a,int b){\n" +
                    "    int a, b[][];\n" +
                    "    a=b;\n" +
                    "    b=c;\n" +
                    "}\n",
            "void sort(int a){}\n" +
                    "\n" +
                    "int add(int a,int b){;;;}",
            "void exchange(int a,int b){\n" +
                    "    if(a){\n" +
                    "      b=c;\n" +
                    "    }\n" +
                    "}",
            "void exchange(int a,int b){\n" +
                    "    if(a){\n" +
                    "      b=c;\n" +
                    "      if(d){\n" +
                    "        ;\n" +
                    "      }\n" +
                    "    }else{\n" +
                    "      a=b;\n" +
                    "    }\n" +
                    "}",
            "void exchange(int a,int b){\n" +
                    "    if(a){\n" +
                    "      b=c;\n" +
                    "      if(d){\n" +
                    "        while(a){\n" +
                    "          if(c)\n" +
                    "            ;\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }else{\n" +
                    "      a=b;\n" +
                    "    }\n" +
                    "}",
            "void exchange(int a,int b){\n" +
                    "    if(a){\n" +
                    "      b=c;\n" +
                    "      if(d){\n" +
                    "        while(a){\n" +
                    "          if(c)\n" +
                    "            ;\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }else{\n" +
                    "      a=b;\n" +
                    "      for(;;){\n" +
                    "        \n" +
                    "      }\n" +
                    "    }\n" +
                    "}",
            "int testFor(){\n" +
                    "  for(int a,b,c,d,e,f,g;c;a=b,c=d,e=f){\n" +
                    "\n" +
                    "  }\n" +
                    "}",
            "void exchange(int a,int b){\n" +
                    "    if(a){\n" +
                    "      b=c;\n" +
                    "      if(d){\n" +
                    "        while(a){\n" +
                    "          if(c)\n" +
                    "            ;\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }else{\n" +
                    "      a=b;\n" +
                    "      for(;;){\n" +
                    "        for(int a,b;;a=b){\n" +
                    "          for(;b;a=e,b=c){\n" +
                    "            ;\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }\n" +
                    "}",
            "void func(){\n" +
                    "  int[] a=new int[5];\n" +
                    "}"
    };

    private static final String[] WRONG_CASES = {
            "sort(int a){}",
            "void sort(int a){}}",
            "void sort(int a,b){}",
            "void exchange(int a,int b){\n" +
                    "    int a, b[][]]];\n" +
                    "    a=b;\n" +
                    "    b=c;\n" +
                    "}"
    };

    private LRCompiler compilerLR1;

    private LRCompiler compilerLALR;

    @Before
    public void init() {
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
}
