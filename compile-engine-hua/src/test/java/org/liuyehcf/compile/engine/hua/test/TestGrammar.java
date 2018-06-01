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

import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.test.TestCases.*;


/**
 * @author chenlu
 * @date 2018/5/31
 */
public class TestGrammar {

    private static final List<String> RIGHT_CASES = new ArrayList<>();

    static {
        RIGHT_CASES.addAll(Arrays.asList(WHILE_CASES));
        RIGHT_CASES.addAll(Arrays.asList(FOR_CASES));
        RIGHT_CASES.addAll(Arrays.asList(DO_CASES));
        RIGHT_CASES.addAll(Arrays.asList(IF_CASES));
        RIGHT_CASES.addAll(Arrays.asList(VARIABLE_DECLARATION_CASES));
        RIGHT_CASES.addAll(Arrays.asList(OPERATOR_CASES));
        RIGHT_CASES.addAll(Arrays.asList(CLASSIC_CASES));
    }

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
            assertTrue(compilerLR1.compile(rightCase).isSuccess());
            assertTrue(compilerLALR.compile(rightCase).isSuccess());
        }
    }

    @Test
    public void testCase2() {
        System.out.println(compilerLR1.getAnalysisTableMarkdownString());
        System.out.println(compilerLALR.getAnalysisTableMarkdownString());
    }
}