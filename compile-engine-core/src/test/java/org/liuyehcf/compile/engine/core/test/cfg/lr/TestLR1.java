package org.liuyehcf.compile.engine.core.test.cfg.lr;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.cfg.lr.LR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.test.GrammarCase;

import static org.junit.Assert.*;

public class TestLR1 {
    @Test
    public void testAmbiguityStatus1() {
        LRCompiler compiler = new LR1(GrammarCase.Ambiguity_1.GRAMMAR, GrammarCase.Ambiguity_1.LEXICAL_ANALYZER);

        assertFalse(compiler.isLegal());

        assertEquals(
                "| 状态\\文法符号 | ( | ) | * | + | __$__ | id | E |\n" +
                        "|:--|:--|:--|:--|:--|:--|:--|:--|\n" +
                        "| 0 | MOVE_IN \"1\" | \\ | \\ | \\ | \\ | MOVE_IN \"2\" | JUMP \"3\" |\n" +
                        "| 1 | MOVE_IN \"4\" | \\ | \\ | \\ | \\ | MOVE_IN \"6\" | JUMP \"5\" |\n" +
                        "| 2 | \\ | \\ | REDUCTION \"E → id\" | REDUCTION \"E → id\" | REDUCTION \"E → id\" | \\ | \\ |\n" +
                        "| 3 | \\ | \\ | MOVE_IN \"7\" | MOVE_IN \"8\" | ACCEPT \"__S__ → E\" | \\ | \\ |\n" +
                        "| 4 | MOVE_IN \"4\" | \\ | \\ | \\ | \\ | MOVE_IN \"6\" | JUMP \"9\" |\n" +
                        "| 5 | \\ | MOVE_IN \"10\" | MOVE_IN \"11\" | MOVE_IN \"12\" | \\ | \\ | \\ |\n" +
                        "| 6 | \\ | REDUCTION \"E → id\" | REDUCTION \"E → id\" | REDUCTION \"E → id\" | \\ | \\ | \\ |\n" +
                        "| 7 | MOVE_IN \"1\" | \\ | \\ | \\ | \\ | MOVE_IN \"2\" | JUMP \"13\" |\n" +
                        "| 8 | MOVE_IN \"1\" | \\ | \\ | \\ | \\ | MOVE_IN \"2\" | JUMP \"14\" |\n" +
                        "| 9 | \\ | MOVE_IN \"15\" | MOVE_IN \"11\" | MOVE_IN \"12\" | \\ | \\ | \\ |\n" +
                        "| 10 | \\ | \\ | REDUCTION \"E → ( E )\" | REDUCTION \"E → ( E )\" | REDUCTION \"E → ( E )\" | \\ | "
                        + "\\ |\n"
                        +
                        "| 11 | MOVE_IN \"4\" | \\ | \\ | \\ | \\ | MOVE_IN \"6\" | JUMP \"16\" |\n" +
                        "| 12 | MOVE_IN \"4\" | \\ | \\ | \\ | \\ | MOVE_IN \"6\" | JUMP \"17\" |\n" +
                        "| 13 | \\ | \\ | MOVE_IN \"7\" / REDUCTION \"E → E * E\" | REDUCTION \"E → E * E\" / MOVE_IN \"8\" |"
                        + " REDUCTION \"E → E * E\" | \\ | \\ |\n"
                        +
                        "| 14 | \\ | \\ | MOVE_IN \"7\" / REDUCTION \"E → E + E\" | MOVE_IN \"8\" / REDUCTION \"E → E + E\" |"
                        + " REDUCTION \"E → E + E\" | \\ | \\ |\n"
                        +
                        "| 15 | \\ | REDUCTION \"E → ( E )\" | REDUCTION \"E → ( E )\" | REDUCTION \"E → ( E )\" | \\ | \\ | "
                        + "\\ |\n"
                        +
                        "| 16 | \\ | REDUCTION \"E → E * E\" | MOVE_IN \"11\" / REDUCTION \"E → E * E\" | REDUCTION \"E → E *"
                        + " E\" / MOVE_IN \"12\" | \\ | \\ | \\ |\n"
                        +
                        "| 17 | \\ | REDUCTION \"E → E + E\" | MOVE_IN \"11\" / REDUCTION \"E → E + E\" | MOVE_IN \"12\" / "
                        + "REDUCTION \"E → E + E\" | \\ | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );
    }

    @Test
    public void testLR1Status1() {

        LRCompiler compiler = new LR1(GrammarCase.LR1_1.GRAMMAR, GrammarCase.LR1_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        assertEquals(
                "{\"nonTerminator\":{\"L\":\"__$__,=\",\"R\":\"__$__,=\",\"S\":\"__$__\",\"__S__\":\"__$__\"}}",
                compiler.getFollowJSONString()
        );

        assertEquals(
                "{\"1\":\"[0, *] → 1\",\"2\":\"[0, id] → 2\",\"3\":\"[0, L] → 3\",\"4\":\"[0, R] → 4\",\"5\":\"[0, S] → "
                        + "5\",\"6\":\"[1, *] → 1\",\"7\":\"[1, id] → 2\",\"8\":\"[1, L] → 7\",\"9\":\"[1, R] → 6\","
                        + "\"10\":\"[3, =] → 8\",\"11\":\"[8, *] → 9\",\"12\":\"[8, id] → 10\",\"13\":\"[8, L] → 11\","
                        + "\"14\":\"[8, R] → 12\",\"15\":\"[9, *] → 9\",\"16\":\"[9, id] → 10\",\"17\":\"[9, L] → 11\","
                        + "\"18\":\"[9, R] → 13\"}",
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
                        + "[__$__]\"}}, {\"id\":\"9\",\"coreItems\":{\"1\":\"L → * · R, [__$__]\"},\"equalItems\":{\"1\":\"L "
                        + "→ · * R, [__$__]\",\"2\":\"L → · id, [__$__]\",\"3\":\"R → · L, [__$__]\"}}, {\"id\":\"10\","
                        + "\"coreItems\":{\"1\":\"L → id ·, [__$__]\"},\"equalItems\":{}}, {\"id\":\"11\","
                        + "\"coreItems\":{\"1\":\"R → L ·, [__$__]\"},\"equalItems\":{}}, {\"id\":\"12\","
                        + "\"coreItems\":{\"1\":\"S → L = R ·, [__$__]\"},\"equalItems\":{}}, {\"id\":\"13\","
                        + "\"coreItems\":{\"1\":\"L → * R ·, [__$__]\"},\"equalItems\":{}}]}",
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
                        "| 8 | MOVE_IN \"9\" | \\ | \\ | MOVE_IN \"10\" | JUMP \"11\" | JUMP \"12\" | \\ |\n" +
                        "| 9 | MOVE_IN \"9\" | \\ | \\ | MOVE_IN \"10\" | JUMP \"11\" | JUMP \"13\" | \\ |\n" +
                        "| 10 | \\ | \\ | REDUCTION \"L → id\" | \\ | \\ | \\ | \\ |\n" +
                        "| 11 | \\ | \\ | REDUCTION \"R → L\" | \\ | \\ | \\ | \\ |\n" +
                        "| 12 | \\ | \\ | REDUCTION \"S → L = R\" | \\ | \\ | \\ | \\ |\n" +
                        "| 13 | \\ | \\ | REDUCTION \"L → * R\" | \\ | \\ | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );

    }

    @Test
    public void testLL1Case1() {
        LRCompiler compiler = new LR1(GrammarCase.LL1_1.GRAMMAR, GrammarCase.LL1_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.LL1_1.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.LL1_1.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLL1Case2() {
        LRCompiler compiler = new LR1(GrammarCase.LL1_2.GRAMMAR, GrammarCase.LL1_2.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.LL1_2.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.LL1_2.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLL1Case3() {
        LRCompiler compiler = new LR1(GrammarCase.LL1_3.GRAMMAR, GrammarCase.LL1_3.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.LL1_3.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.LL1_3.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLR0Case1() {
        LRCompiler compiler = new LR1(GrammarCase.LR0_1.GRAMMAR, GrammarCase.LR0_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.LR0_1.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.LR0_1.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testSLRCase1() {
        LRCompiler compiler = new LR1(GrammarCase.SLR_1.GRAMMAR, GrammarCase.SLR_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.SLR_1.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.SLR_1.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testSLRCase2() {
        LRCompiler compiler = new LR1(GrammarCase.SLR_2.GRAMMAR, GrammarCase.SLR_2.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.SLR_2.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.SLR_2.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLR1Case1() {
        LRCompiler compiler = new LR1(GrammarCase.LR1_1.GRAMMAR, GrammarCase.LR1_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.LR1_1.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.LR1_1.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLR1Case2() {
        LRCompiler compiler = new LR1(GrammarCase.LR1_2.GRAMMAR, GrammarCase.LR1_2.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.LR1_2.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.LR1_2.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }

    @Test
    public void testLR1Case3() {
        LRCompiler compiler = new LR1(GrammarCase.LR1_3.GRAMMAR, GrammarCase.LR1_3.LEXICAL_ANALYZER);

        System.out.println(compiler.getFirstJSONString());

        System.out.println(compiler.getFollowJSONString());

        System.out.println(compiler.getClosureJSONString());

        System.out.println(compiler.getClosureTransferTableJSONString());

        System.out.println(compiler.getAnalysisTableMarkdownString());

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.LR1_3.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.LR1_3.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }
}
