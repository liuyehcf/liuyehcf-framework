package org.liuyehcf.compile.engine.core.test.cfg.lr;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.test.GrammarCase;

import static org.junit.Assert.*;

public class TestLALR1 {
    @Test
    public void testLR1Status1() {

        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_7.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_7.GRAMMAR);

        assertTrue(compiler.isLegal());

        assertEquals(
                "{\"nonTerminator\":{\"L\":\"__$__,=\",\"R\":\"__$__,=\",\"S\":\"__$__\",\"__S__\":\"__$__\"}}",
                compiler.getFollowJSONString()
        );

        assertEquals(
                "{\"1\":\"[0, *] → 1\",\"2\":\"[0, id] → 2\",\"3\":\"[0, L] → 3\",\"4\":\"[0, R] → 4\",\"5\":\"[0, S] → "
                        + "5\",\"6\":\"[1, *] → 1\",\"7\":\"[1, id] → 2\",\"8\":\"[1, L] → 7\",\"9\":\"[1, R] → 6\","
                        + "\"10\":\"[3, =] → 8\",\"11\":\"[8, *] → 1\",\"12\":\"[8, id] → 2\",\"13\":\"[8, L] → 7\","
                        + "\"14\":\"[8, R] → 12\"}",
                compiler.getClosureTransferTableJSONString()
        );

        assertEquals(
                "{\"closures:\":[{\"id\":\"0\",\"coreItems\":{\"1\":\"__S__ → · S, [__$__]\"},\"equalItems\":{\"1\":\"L →"
                        + " · * R, [=, __$__]\",\"2\":\"L → · id, [=, __$__]\",\"3\":\"R → · L, [__$__]\",\"4\":\"S → · L = "
                        + "R, [__$__]\",\"5\":\"S → · R, [__$__]\"}}, {\"id\":\"1\",\"coreItems\":{\"1\":\"L → * · R, [=, "
                        + "__$__]\"},\"equalItems\":{\"1\":\"L → · * R, [=, __$__]\",\"2\":\"L → · id, [=, __$__]\",\"3\":\"R"
                        + " → · L, [=, __$__]\"}}, {\"id\":\"2\",\"coreItems\":{\"1\":\"L → id ·, [=, __$__]\"},"
                        + "\"equalItems\":{}}, {\"id\":\"3\",\"coreItems\":{\"1\":\"R → L ·, [__$__]\",\"2\":\"S → L · = R, "
                        + "[__$__]\"},\"equalItems\":{}}, {\"id\":\"4\",\"coreItems\":{\"1\":\"S → R ·, [__$__]\"},"
                        + "\"equalItems\":{}}, {\"id\":\"5\",\"coreItems\":{\"1\":\"__S__ → S ·, [__$__]\"},"
                        + "\"equalItems\":{}}, {\"id\":\"6\",\"coreItems\":{\"1\":\"L → * R ·, [=, __$__]\"},"
                        + "\"equalItems\":{}}, {\"id\":\"7\",\"coreItems\":{\"1\":\"R → L ·, [=, __$__]\"},"
                        + "\"equalItems\":{}}, {\"id\":\"8\",\"coreItems\":{\"1\":\"S → L = · R, [__$__]\"},"
                        + "\"equalItems\":{\"1\":\"L → · * R, [__$__]\",\"2\":\"L → · id, [__$__]\",\"3\":\"R → · L, "
                        + "[__$__]\"}}, {\"id\":\"12\",\"coreItems\":{\"1\":\"S → L = R ·, [__$__]\"},\"equalItems\":{}}]}",
                compiler.getClosureJSONString()
        );

        assertEquals(
                "| 状态\\文法符号 | * | = | __$__ | id | L | R | S |\n" +
                        "|:--|:--|:--|:--|:--|:--|:--|:--|\n" +
                        "| 0 | MOVE_IN \"1\" | \\ | \\ | MOVE_IN \"2\" | JUMP \"3\" | JUMP \"4\" | JUMP \"5\" |\n" +
                        "| 1 | MOVE_IN \"1\" | \\ | \\ | MOVE_IN \"2\" | JUMP \"7\" | JUMP \"6\" | \\ |\n" +
                        "| 2 | \\ | REDUCTION \"L → id\" | REDUCTION \"L → id\" | \\ | \\ | \\ | \\ |\n" +
                        "| 3 | \\ | MOVE_IN \"8\" | REDUCTION \"R → L\" | \\ | \\ | \\ | \\ |\n" +
                        "| 4 | \\ | \\ | REDUCTION \"S → R\" | \\ | \\ | \\ | \\ |\n" +
                        "| 5 | \\ | \\ | ACCEPT \"__S__ → S\" | \\ | \\ | \\ | \\ |\n" +
                        "| 6 | \\ | REDUCTION \"L → * R\" | REDUCTION \"L → * R\" | \\ | \\ | \\ | \\ |\n" +
                        "| 7 | \\ | REDUCTION \"R → L\" | REDUCTION \"R → L\" | \\ | \\ | \\ | \\ |\n" +
                        "| 8 | MOVE_IN \"1\" | \\ | \\ | MOVE_IN \"2\" | JUMP \"7\" | JUMP \"12\" | \\ |\n" +
                        "| 12 | \\ | \\ | REDUCTION \"S → L = R\" | \\ | \\ | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );

    }

    @Test
    public void testLR1Case1() {
        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_7.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_7.GRAMMAR);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_7.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_7.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLL1Case1() {
        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_1.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_1.GRAMMAR);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_1.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_1.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLL1Case2() {
        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_2.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_2.GRAMMAR);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_2.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_2.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLL1Case3() {
        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_3.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_3.GRAMMAR);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_3.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_3.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLR0Case1() {
        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_4.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_4.GRAMMAR);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_4.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_4.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testSLRCase1() {
        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_5.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_5.GRAMMAR);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_5.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_5.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testSLRCase2() {
        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_6.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_6.GRAMMAR);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_6.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_6.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }


    @Test
    public void testSLRCase3() {
        LRCompiler compiler = LALR1.create(GrammarCase.GRAMMAR_9.LEXICAL_ANALYZER, GrammarCase.GRAMMAR_9.GRAMMAR);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_9.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_9.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

}
