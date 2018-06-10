package org.liuyehcf.compile.engine.core.test.cfg.ll;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.cfg.ll.LL1;
import org.liuyehcf.compile.engine.core.cfg.ll.LLCompiler;
import org.liuyehcf.compile.engine.core.test.GrammarCase;

import static org.junit.Assert.*;

public class TestLL1 {
    @Test
    public void testLL1Status1() {
        LLCompiler compiler = new LL1(GrammarCase.GRAMMAR_1.GRAMMAR, GrammarCase.GRAMMAR_1.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        assertEquals(
                "{\"productions\":[\"E → ( E ) T′ E′ | id T′ E′\",\"E′ → + T E′ | __ε__\",\"F → ( E ) | id\",\"T → ( E ) "
                        + "T′ | id T′\",\"T′ → * F T′ | __ε__\"]}",
                compiler.getGrammar().toString()
        );

        assertEquals(
                "{\"terminator\":{\"(\":\"(\",\")\":\")\",\"*\":\"*\",\"+\":\"+\",\"__ε__\":\"__ε__\",\"id\":\"id\"},"
                        + "\"nonTerminator\":{\"E\":\"id,(\",\"E′\":\"__ε__,+\",\"F\":\"id,(\",\"T\":\"id,(\",\"T′\":\"__ε__,"
                        + "*\"}}",
                compiler.getFirstJSONString()
        );

        assertEquals(
                "{\"nonTerminator\":{\"E\":\"__$__,)\",\"E′\":\"__$__,)\",\"F\":\"__$__,),*,+\",\"T\":\"__$__,),+\","
                        + "\"T′\":\"__$__,),+\"}}",
                compiler.getFollowJSONString()
        );

        assertEquals(
                "{\"E′\":{\"E′ → + T E′\":\"+\",\"E′ → __ε__\":\"__$__,)\"},\"T′\":{\"T′ → * F T′\":\"*\",\"T′ → "
                        + "__ε__\":\"__$__,),+\"},\"T\":{\"T → ( E ) T′\":\"(\",\"T → id T′\":\"id\"},\"E\":{\"E → ( E ) T′ "
                        + "E′\":\"(\",\"E → id T′ E′\":\"id\"},\"F\":{\"F → ( E )\":\"(\",\"F → id\":\"id\"}}",
                compiler.getSelectJSONString()
        );

        assertEquals(
                "| 非终结符\\终结符 | ( | ) | * | + | __ε__ | id |\n" +
                        "|:--|:--|:--|:--|:--|:--|:--|\n" +
                        "| E | E → ( E ) T′ E′ | \\ | \\ | \\ | \\ | E → id T′ E′ |\n" +
                        "| E′ | \\ | E′ → __ε__ | \\ | E′ → + T E′ | \\ | \\ |\n" +
                        "| F | F → ( E ) | \\ | \\ | \\ | \\ | F → id |\n" +
                        "| T | T → ( E ) T′ | \\ | \\ | \\ | \\ | T → id T′ |\n" +
                        "| T′ | \\ | T′ → __ε__ | T′ → * F T′ | T′ → __ε__ | \\ | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );
    }

    @Test
    public void testLL1Status2() {
        LLCompiler compiler = new LL1(GrammarCase.GRAMMAR_2.GRAMMAR, GrammarCase.GRAMMAR_2.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        assertEquals(
                "{\"productions\":[\"DECLIST → id DECLISTN\",\"DECLISTN → , id DECLISTN | __ε__\",\"PROGRAM → program  "
                        + "DECLIST : TYPE ; STLIST  end\",\"STLIST → s STLISTN\",\"STLISTN → ; s STLISTN | __ε__\",\"TYPE → "
                        + "int | real\"]}",
                compiler.getGrammar().toString()
        );

        assertEquals(
                "{\"terminator\":{\" end\":\" end\",\",\":\",\",\":\":\":\",\";\":\";\",\"__ε__\":\"__ε__\","
                        + "\"id\":\"id\",\"int\":\"int\",\"program \":\"program \",\"real\":\"real\",\"s\":\"s\"},"
                        + "\"nonTerminator\":{\"DECLIST\":\"id\",\"DECLISTN\":\"__ε__,,\",\"PROGRAM\":\"program \","
                        + "\"STLIST\":\"s\",\"STLISTN\":\"__ε__,;\",\"TYPE\":\"real,int\"}}",
                compiler.getFirstJSONString()
        );

        assertEquals(
                "{\"nonTerminator\":{\"DECLIST\":\":\",\"DECLISTN\":\":\",\"PROGRAM\":\"__$__\",\"STLIST\":\" end\","
                        + "\"STLISTN\":\" end\",\"TYPE\":\";\"}}",
                compiler.getFollowJSONString()
        );

        assertEquals(
                "{\"DECLISTN\":{\"DECLISTN → __ε__\":\":\",\"DECLISTN → , id DECLISTN\":\",\"},\"PROGRAM\":{\"PROGRAM → "
                        + "program  DECLIST : TYPE ; STLIST  end\":\"program \"},\"STLIST\":{\"STLIST → s STLISTN\":\"s\"},"
                        + "\"TYPE\":{\"TYPE → real\":\"real\",\"TYPE → int\":\"int\"},\"STLISTN\":{\"STLISTN → ; s "
                        + "STLISTN\":\";\",\"STLISTN → __ε__\":\" end\"},\"DECLIST\":{\"DECLIST → id DECLISTN\":\"id\"}}",
                compiler.getSelectJSONString()
        );

        assertEquals(
                "| 非终结符\\终结符 |  end | , | : | ; | __ε__ | id | int | program  | real | s |\n" +
                        "|:--|:--|:--|:--|:--|:--|:--|:--|:--|:--|:--|\n" +
                        "| DECLIST | \\ | \\ | \\ | \\ | \\ | DECLIST → id DECLISTN | \\ | \\ | \\ | \\ |\n" +
                        "| DECLISTN | \\ | DECLISTN → , id DECLISTN | DECLISTN → __ε__ | \\ | \\ | \\ | \\ | \\ | \\ | \\ |\n" +
                        "| PROGRAM | \\ | \\ | \\ | \\ | \\ | \\ | \\ | PROGRAM → program  DECLIST : TYPE ; STLIST  end | \\ "
                        + "| \\ |\n"
                        +
                        "| STLIST | \\ | \\ | \\ | \\ | \\ | \\ | \\ | \\ | \\ | STLIST → s STLISTN |\n" +
                        "| STLISTN | STLISTN → __ε__ | \\ | \\ | STLISTN → ; s STLISTN | \\ | \\ | \\ | \\ | \\ | \\ |\n" +
                        "| TYPE | \\ | \\ | \\ | \\ | \\ | \\ | TYPE → int | \\ | TYPE → real | \\ |\n",
                compiler.getAnalysisTableMarkdownString()
        );
    }

    @Test
    public void testLL1Case1() {
        LLCompiler compiler = new LL1(GrammarCase.GRAMMAR_1.GRAMMAR, GrammarCase.GRAMMAR_1.LEXICAL_ANALYZER);

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
        LLCompiler compiler = new LL1(GrammarCase.GRAMMAR_2.GRAMMAR, GrammarCase.GRAMMAR_2.LEXICAL_ANALYZER);

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
        LLCompiler compiler = new LL1(GrammarCase.GRAMMAR_3.GRAMMAR, GrammarCase.GRAMMAR_3.LEXICAL_ANALYZER);

        assertTrue(compiler.isLegal());

        for (String input : GrammarCase.GRAMMAR_3.TRUE_CASES) {
            assertTrue(compiler.compile(input).isSuccess());
        }

        for (String input : GrammarCase.GRAMMAR_3.FALSE_CASES) {
            assertFalse(compiler.compile(input).isSuccess());
        }
    }
}

