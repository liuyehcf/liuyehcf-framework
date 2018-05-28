package org.liuyehcf.compile.engine.core.test.converter;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.grammar.converter.AugmentedGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.definition.*;
import org.liuyehcf.compile.engine.core.test.GrammarCase;

import static org.junit.Assert.assertEquals;

public class TestAugmentedGrammarConverter {
    @Test
    public void convertCase1() {
        Grammar grammar = Grammar.create(
                Symbol.createNonTerminator("S"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("S"),
                                SymbolString.create(
                                        Symbol.createTerminator("b"),
                                        Symbol.createNonTerminator("B"),
                                        Symbol.createNonTerminator("B")
                                ),
                                null
                        )
                )
        );

        Grammar convertedGrammar = new AugmentedGrammarConverter(grammar).getConvertedGrammar();

        assertEquals(
                "{\"productions\":[\"S → b B B\",\"__S__ → S\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase2() {

        Grammar convertedGrammar = new AugmentedGrammarConverter(GrammarCase.GRAMMAR_8.GRAMMAR)
                .getConvertedGrammar();

        assertEquals(
                "{\"productions\":[\"E → ( E )\",\"E → id\",\"E → E * E\",\"E → E + E\",\"__S__ → E\"]}",
                convertedGrammar.toString()
        );
    }

}
