package com.github.liuyehcf.framework.compile.engine.test.cfg;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.DefaultLexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}
