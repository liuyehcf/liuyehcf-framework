package org.liuyehcf.compile.engine.core.test.cfg;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.cfg.DefaultLexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;

public class TestLexicalAnalyzer {
    public static String getIdRegex() {
        String digit = "[0-9]";
        String letter = "[a-zA-Z_]";
        return letter + "(" + letter + "|" + digit + ")*";
    }

    public static String getDigitRegex() {
        return "(\\+|-)?[0-9][0-9]*(\\.[0-9][0-9]*)?";
    }

    public static String getIntegerRegex() {
        return "(\\+)?[0-9][0-9]*";
    }

    private static String getUnsignedIntegerRegex() {
        return "[0-9]+";
    }

    @Test
    public void testLexicalAnalyze1() {
        LexicalAnalyzer analyzer = DefaultLexicalAnalyzer.Builder.builder()
                .addNormalMorpheme(Symbol.createTerminator("int"), "int")
                .addRegexMorpheme(Symbol.createRegexTerminator("id"), getIdRegex())
                .addNormalMorpheme(Symbol.createTerminator("="), "=")
                .addNormalMorpheme(Symbol.createTerminator("+"), "+")
                .addNormalMorpheme(Symbol.createTerminator("*"), "*")
                .addRegexMorpheme(Symbol.createRegexTerminator("unsignedInteger"), getUnsignedIntegerRegex())
                .build();

        LexicalAnalyzer.TokenIterator iterator = analyzer.iterator("1+2*3=7");

        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next().getValue());
        }

        assertEquals(
                "1+2*3=7",
                sb.toString()
        );
    }

    @Test
    public void testChar() {
        LexicalAnalyzer analyzer = DefaultLexicalAnalyzer.Builder.builder()
                .addCharMorpheme(Symbol.createTerminator("test"))
                .build();

        LexicalAnalyzer.TokenIterator iterator = analyzer.iterator("'\\n' 'a'  'b'  '1'  '\\''  ");

        assertTrue(iterator.hasNext());
        assertEquals("'\n'", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("'a'", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("'b'", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("'1'", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("'\\''", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testString() {
        LexicalAnalyzer analyzer = DefaultLexicalAnalyzer.Builder.builder()
                .addStringMorpheme(Symbol.createTerminator("test"))
                .build();

        LexicalAnalyzer.TokenIterator iterator = analyzer.iterator("\"\"  \"asdfasdf\"  \"\\n\"  \"\\\"\"");

        assertTrue(iterator.hasNext());
        assertEquals("\"\"", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("\"asdfasdf\"", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("\"\\n\"", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("\"\\\"\"", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

}
