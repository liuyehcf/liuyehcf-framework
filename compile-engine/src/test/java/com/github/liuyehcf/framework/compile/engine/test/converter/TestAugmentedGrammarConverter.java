package com.github.liuyehcf.framework.compile.engine.test.converter;

import com.github.liuyehcf.framework.compile.engine.grammar.converter.AugmentedGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.*;
import com.github.liuyehcf.framework.compile.engine.test.GrammarCase;
import org.junit.Test;

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

        Grammar convertedGrammar = new AugmentedGrammarConverter(GrammarCase.Ambiguity_1.GRAMMAR)
                .getConvertedGrammar();

        assertEquals(
                "{\"productions\":[\"E → ( E )\",\"E → id\",\"E → E * E\",\"E → E + E\",\"__S__ → E\"]}",
                convertedGrammar.toString()
        );
    }

}
