package com.github.liuyehcf.framework.compile.engine.test.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.cfg.lr.LRCompiler;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.SLR;
import com.github.liuyehcf.framework.compile.engine.test.GrammarCase;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSLR {

    @Test
    public void testSLRStatus1() {
        LRCompiler compiler = new SLR(GrammarCase.SLR_1.GRAMMAR, GrammarCase.SLR_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        assertEquals(
                "{\"nonTerminator\":{\"E\":\"__$__,),+\",\"F\":\"__$__,),*,+\",\"T\":\"__$__,),*,+\",\"__S__\":\"__$__\"}}",
                compiler.getFollowJSONString()
        );

        assertEquals(
                "{\"1\":\"[0, (] → 3\",\"2\":\"[0, id] → 4\",\"3\":\"[0, E] → 1\",\"4\":\"[0, F] → 5\",\"5\":\"[0, T] → "
                        + "2\",\"6\":\"[1, +] → 6\",\"7\":\"[2, *] → 7\",\"8\":\"[3, (] → 3\",\"9\":\"[3, id] → 4\","
                        + "\"10\":\"[3, E] → 8\",\"11\":\"[3, F] → 5\",\"12\":\"[3, T] → 2\",\"13\":\"[6, (] → 3\","
                        + "\"14\":\"[6, id] → 4\",\"15\":\"[6, F] → 5\",\"16\":\"[6, T] → 9\",\"17\":\"[7, (] → 3\","
                        + "\"18\":\"[7, id] → 4\",\"19\":\"[7, F] → 10\",\"20\":\"[8, )] → 11\",\"21\":\"[8, +] → 6\","
                        + "\"22\":\"[9, *] → 7\"}",
                compiler.getClosureTransferTableJSONString()
        );

        assertEquals(
                "{\"closures:\":[{\"id\":\"0\",\"coreItems\":{\"1\":\"__S__ → · E\"},\"equalItems\":{\"1\":\"E → · E + "
                        + "T\",\"2\":\"E → · T\",\"3\":\"F → · ( E )\",\"4\":\"F → · id\",\"5\":\"T → · F\",\"6\":\"T → · T *"
                        + " F\"}}, {\"id\":\"1\",\"coreItems\":{\"1\":\"E → E · + T\",\"2\":\"__S__ → E ·\"},"
                        + "\"equalItems\":{}}, {\"id\":\"2\",\"coreItems\":{\"1\":\"E → T ·\",\"2\":\"T → T · * F\"},"
                        + "\"equalItems\":{}}, {\"id\":\"3\",\"coreItems\":{\"1\":\"F → ( · E )\"},\"equalItems\":{\"1\":\"E "
                        + "→ · E + T\",\"2\":\"E → · T\",\"3\":\"F → · ( E )\",\"4\":\"F → · id\",\"5\":\"T → · F\",\"6\":\"T"
                        + " → · T * F\"}}, {\"id\":\"4\",\"coreItems\":{\"1\":\"F → id ·\"},\"equalItems\":{}}, "
                        + "{\"id\":\"5\",\"coreItems\":{\"1\":\"T → F ·\"},\"equalItems\":{}}, {\"id\":\"6\","
                        + "\"coreItems\":{\"1\":\"E → E + · T\"},\"equalItems\":{\"1\":\"F → · ( E )\",\"2\":\"F → · id\","
                        + "\"3\":\"T → · F\",\"4\":\"T → · T * F\"}}, {\"id\":\"7\",\"coreItems\":{\"1\":\"T → T * · F\"},"
                        + "\"equalItems\":{\"1\":\"F → · ( E )\",\"2\":\"F → · id\"}}, {\"id\":\"8\",\"coreItems\":{\"1\":\"E"
                        + " → E · + T\",\"2\":\"F → ( E · )\"},\"equalItems\":{}}, {\"id\":\"9\",\"coreItems\":{\"1\":\"E → E"
                        + " + T ·\",\"2\":\"T → T · * F\"},\"equalItems\":{}}, {\"id\":\"10\",\"coreItems\":{\"1\":\"T → T * "
                        + "F ·\"},\"equalItems\":{}}, {\"id\":\"11\",\"coreItems\":{\"1\":\"F → ( E ) ·\"},"
                        + "\"equalItems\":{}}]}",
                compiler.getClosureJSONString()
        );

        assertEquals(
                "| 状态\\文法符号 | ( | ) | * | + | __$__ | id | E | F | T |\n" +
                        "|:--|:--|:--|:--|:--|:--|:--|:--|:--|:--|\n" +
                        "| 0 | MOVE_IN \"3\" | \\ | \\ | \\ | \\ | MOVE_IN \"4\" | JUMP \"1\" | JUMP \"5\" | JUMP \"2\" |\n" +
                        "| 1 | \\ | \\ | \\ | MOVE_IN \"6\" | ACCEPT \"__S__ → E\" | \\ | \\ | \\ | \\ |\n" +
                        "| 2 | \\ | REDUCTION \"E → T\" | MOVE_IN \"7\" | REDUCTION \"E → T\" | REDUCTION \"E → T\" | \\ | \\"
                        + " | \\ | \\ |\n"
                        +
                        "| 3 | MOVE_IN \"3\" | \\ | \\ | \\ | \\ | MOVE_IN \"4\" | JUMP \"8\" | JUMP \"5\" | JUMP \"2\" |\n" +
                        "| 4 | \\ | REDUCTION \"F → id\" | REDUCTION \"F → id\" | REDUCTION \"F → id\" | REDUCTION \"F → id\""
                        + " | \\ | \\ | \\ | \\ |\n"
                        +
                        "| 5 | \\ | REDUCTION \"T → F\" | REDUCTION \"T → F\" | REDUCTION \"T → F\" | REDUCTION \"T → F\" | "
                        + "\\ | \\ | \\ | \\ |\n"
                        +
                        "| 6 | MOVE_IN \"3\" | \\ | \\ | \\ | \\ | MOVE_IN \"4\" | \\ | JUMP \"5\" | JUMP \"9\" |\n" +
                        "| 7 | MOVE_IN \"3\" | \\ | \\ | \\ | \\ | MOVE_IN \"4\" | \\ | JUMP \"10\" | \\ |\n" +
                        "| 8 | \\ | MOVE_IN \"11\" | \\ | MOVE_IN \"6\" | \\ | \\ | \\ | \\ | \\ |\n" +
                        "| 9 | \\ | REDUCTION \"E → E + T\" | MOVE_IN \"7\" | REDUCTION \"E → E + T\" | REDUCTION \"E → E + "
                        + "T\" | \\ | \\ | \\ | \\ |\n"
                        +
                        "| 10 | \\ | REDUCTION \"T → T * F\" | REDUCTION \"T → T * F\" | REDUCTION \"T → T * F\" | REDUCTION "
                        + "\"T → T * F\" | \\ | \\ | \\ | \\ |\n"
                        +
                        "| 11 | \\ | REDUCTION \"F → ( E )\" | REDUCTION \"F → ( E )\" | REDUCTION \"F → ( E )\" | REDUCTION "
                        + "\"F → ( E )\" | \\ | \\ | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );
    }

    @Test
    public void testSLRStatus2() {
        LRCompiler compiler = new SLR(GrammarCase.SLR_2.GRAMMAR, GrammarCase.SLR_2.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        assertEquals(
                "{\"nonTerminator\":{\"B\":\"d\",\"T\":\"b,__$__\",\"__S__\":\"__$__\"}}",
                compiler.getFollowJSONString()
        );

        assertEquals(
                "{\"1\":\"[0, a] → 1\",\"2\":\"[0, T] → 2\",\"3\":\"[1, a] → 1\",\"4\":\"[1, B] → 4\",\"5\":\"[1, T] → "
                        + "3\",\"6\":\"[3, b] → 5\",\"7\":\"[4, d] → 6\"}",
                compiler.getClosureTransferTableJSONString()
        );

        assertEquals(
                "{\"closures:\":[{\"id\":\"0\",\"coreItems\":{\"1\":\"__S__ → · T\"},\"equalItems\":{\"1\":\"T → __ε__ "
                        + "·\",\"2\":\"T → · a B d\"}}, {\"id\":\"1\",\"coreItems\":{\"1\":\"T → a · B d\"},"
                        + "\"equalItems\":{\"1\":\"B → __ε__ ·\",\"2\":\"B → · T b\",\"3\":\"T → __ε__ ·\",\"4\":\"T → · a B "
                        + "d\"}}, {\"id\":\"2\",\"coreItems\":{\"1\":\"__S__ → T ·\"},\"equalItems\":{}}, {\"id\":\"3\","
                        + "\"coreItems\":{\"1\":\"B → T · b\"},\"equalItems\":{}}, {\"id\":\"4\",\"coreItems\":{\"1\":\"T → a"
                        + " B · d\"},\"equalItems\":{}}, {\"id\":\"5\",\"coreItems\":{\"1\":\"B → T b ·\"},"
                        + "\"equalItems\":{}}, {\"id\":\"6\",\"coreItems\":{\"1\":\"T → a B d ·\"},\"equalItems\":{}}]}",
                compiler.getClosureJSONString()
        );

        assertEquals(
                "| 状态\\文法符号 | __$__ | a | b | d | B | T |\n" +
                        "|:--|:--|:--|:--|:--|:--|:--|\n" +
                        "| 0 | REDUCTION \"T → __ε__\" | MOVE_IN \"1\" | REDUCTION \"T → __ε__\" | \\ | \\ | JUMP \"2\" |\n" +
                        "| 1 | REDUCTION \"T → __ε__\" | MOVE_IN \"1\" | REDUCTION \"T → __ε__\" | REDUCTION \"B → __ε__\" | "
                        + "JUMP \"4\" | JUMP \"3\" |\n"
                        +
                        "| 2 | ACCEPT \"__S__ → T\" | \\ | \\ | \\ | \\ | \\ |\n" +
                        "| 3 | \\ | \\ | MOVE_IN \"5\" | \\ | \\ | \\ |\n" +
                        "| 4 | \\ | \\ | \\ | MOVE_IN \"6\" | \\ | \\ |\n" +
                        "| 5 | \\ | \\ | \\ | REDUCTION \"B → T b\" | \\ | \\ |\n" +
                        "| 6 | REDUCTION \"T → a B d\" | \\ | REDUCTION \"T → a B d\" | \\ | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );
    }

    @Test
    public void testLR1Status1() {
        LRCompiler compiler = new SLR(GrammarCase.LR1_1.GRAMMAR, GrammarCase.LR1_1.LEXICAL_ANALYZER);

        assertFalse(compiler.isLegal());

        assertEquals(
                "{\"nonTerminator\":{\"L\":\"__$__,=\",\"R\":\"__$__,=\",\"S\":\"__$__\",\"__S__\":\"__$__\"}}",
                compiler.getFollowJSONString()
        );

        assertEquals(
                "{\"1\":\"[0, *] → 1\",\"2\":\"[0, id] → 2\",\"3\":\"[0, L] → 3\",\"4\":\"[0, R] → 4\",\"5\":\"[0, S] → "
                        + "5\",\"6\":\"[1, *] → 1\",\"7\":\"[1, id] → 2\",\"8\":\"[1, L] → 7\",\"9\":\"[1, R] → 6\","
                        + "\"10\":\"[3, =] → 8\",\"11\":\"[8, *] → 1\",\"12\":\"[8, id] → 2\",\"13\":\"[8, L] → 7\","
                        + "\"14\":\"[8, R] → 9\"}",
                compiler.getClosureTransferTableJSONString()
        );

        assertEquals(
                "{\"closures:\":[{\"id\":\"0\",\"coreItems\":{\"1\":\"__S__ → · S\"},\"equalItems\":{\"1\":\"L → · * R\","
                        + "\"2\":\"L → · id\",\"3\":\"R → · L\",\"4\":\"S → · L = R\",\"5\":\"S → · R\"}}, {\"id\":\"1\","
                        + "\"coreItems\":{\"1\":\"L → * · R\"},\"equalItems\":{\"1\":\"L → · * R\",\"2\":\"L → · id\","
                        + "\"3\":\"R → · L\"}}, {\"id\":\"2\",\"coreItems\":{\"1\":\"L → id ·\"},\"equalItems\":{}}, "
                        + "{\"id\":\"3\",\"coreItems\":{\"1\":\"R → L ·\",\"2\":\"S → L · = R\"},\"equalItems\":{}}, "
                        + "{\"id\":\"4\",\"coreItems\":{\"1\":\"S → R ·\"},\"equalItems\":{}}, {\"id\":\"5\","
                        + "\"coreItems\":{\"1\":\"__S__ → S ·\"},\"equalItems\":{}}, {\"id\":\"6\",\"coreItems\":{\"1\":\"L →"
                        + " * R ·\"},\"equalItems\":{}}, {\"id\":\"7\",\"coreItems\":{\"1\":\"R → L ·\"},\"equalItems\":{}}, "
                        + "{\"id\":\"8\",\"coreItems\":{\"1\":\"S → L = · R\"},\"equalItems\":{\"1\":\"L → · * R\",\"2\":\"L "
                        + "→ · id\",\"3\":\"R → · L\"}}, {\"id\":\"9\",\"coreItems\":{\"1\":\"S → L = R ·\"},"
                        + "\"equalItems\":{}}]}",
                compiler.getClosureJSONString()
        );

        assertEquals(
                "| 状态\\文法符号 | * | = | __$__ | id | L | R | S |\n" +
                        "|:--|:--|:--|:--|:--|:--|:--|:--|\n" +
                        "| 0 | MOVE_IN \"1\" | \\ | \\ | MOVE_IN \"2\" | JUMP \"3\" | JUMP \"4\" | JUMP \"5\" |\n" +
                        "| 1 | MOVE_IN \"1\" | \\ | \\ | MOVE_IN \"2\" | JUMP \"7\" | JUMP \"6\" | \\ |\n" +
                        "| 2 | \\ | REDUCTION \"L → id\" | REDUCTION \"L → id\" | \\ | \\ | \\ | \\ |\n" +
                        "| 3 | \\ | REDUCTION \"R → L\" / MOVE_IN \"8\" | REDUCTION \"R → L\" | \\ | \\ | \\ | \\ |\n" +
                        "| 4 | \\ | \\ | REDUCTION \"S → R\" | \\ | \\ | \\ | \\ |\n" +
                        "| 5 | \\ | \\ | ACCEPT \"__S__ → S\" | \\ | \\ | \\ | \\ |\n" +
                        "| 6 | \\ | REDUCTION \"L → * R\" | REDUCTION \"L → * R\" | \\ | \\ | \\ | \\ |\n" +
                        "| 7 | \\ | REDUCTION \"R → L\" | REDUCTION \"R → L\" | \\ | \\ | \\ | \\ |\n" +
                        "| 8 | MOVE_IN \"1\" | \\ | \\ | MOVE_IN \"2\" | JUMP \"7\" | JUMP \"9\" | \\ |\n" +
                        "| 9 | \\ | \\ | REDUCTION \"S → L = R\" | \\ | \\ | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );
    }

    @Test
    public void testLL1Case1() {
        LRCompiler compiler = new SLR(GrammarCase.LL1_1.GRAMMAR, GrammarCase.LL1_1.LEXICAL_ANALYZER);

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
        LRCompiler compiler = new SLR(GrammarCase.LL1_2.GRAMMAR, GrammarCase.LL1_2.LEXICAL_ANALYZER);

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
        LRCompiler compiler = new SLR(GrammarCase.LL1_3.GRAMMAR, GrammarCase.LL1_3.LEXICAL_ANALYZER);

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
        LRCompiler compiler = new SLR(GrammarCase.LR0_1.GRAMMAR, GrammarCase.LR0_1.LEXICAL_ANALYZER);

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
        LRCompiler compiler = new SLR(GrammarCase.SLR_1.GRAMMAR, GrammarCase.SLR_1.LEXICAL_ANALYZER);

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
        LRCompiler compiler = new SLR(GrammarCase.SLR_2.GRAMMAR, GrammarCase.SLR_2.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.SLR_2.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.SLR_2.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }
}
