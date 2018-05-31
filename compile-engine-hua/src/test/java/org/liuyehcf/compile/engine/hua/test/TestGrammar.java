package org.liuyehcf.compile.engine.hua.test;

import org.junit.Test;
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
    @Test
    public void testCase1() {
        LRCompiler compiler = LR1.create(GrammarDefinition.LEXICAL_ANALYZER, GrammarDefinition.GRAMMAR);
        assertTrue(compiler.isLegal());
        assertTrue(compiler.compile("\n" +
                "void sort(int a){}\n" +
                "\n" +
                "int add(int a,int b){}\n" +
                "\n" +
                "void exchange(int a,int b){\n" +
                "    int a, b[][];\n" +
                "    a=b;\n" +
                "    b=c;\n" +
                "}\n" +
                "\n" +
                "\n").isSuccess());

        assertFalse(compiler.compile("void exchange(int a,int b){\n" +
                "    int a, b[][]]];\n" +
                "    a=b;\n" +
                "    b=c;\n" +
                "}").isSuccess());
    }
}
