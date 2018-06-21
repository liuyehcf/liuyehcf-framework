package org.liuyehcf.compile.engine.core.test.converter;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.grammar.converter.GrammarConverterPipeline;
import org.liuyehcf.compile.engine.core.grammar.converter.GrammarConverterPipelineImpl;
import org.liuyehcf.compile.engine.core.grammar.converter.MergeGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.converter.StatusExpandGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.definition.*;
import org.liuyehcf.compile.engine.core.test.GrammarCase;

import static org.junit.Assert.assertEquals;

public class TestStatusExpandGrammarConverter {

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

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(grammar);

        assertEquals(
                "{\"productions\":[\"S → · b B B\",\"S → b · B B\",\"S → b B · B\",\"S → b B B ·\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase2() {

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(GrammarCase.Ambiguity_1.GRAMMAR);

        assertEquals(
                "{\"productions\":[\"E → · ( E )\",\"E → ( · E )\",\"E → ( E · )\",\"E → ( E ) ·\",\"E → · id\",\"E → id "
                        + "·\",\"E → · E * E\",\"E → E · * E\",\"E → E * · E\",\"E → E * E ·\",\"E → · E + E\",\"E → E · + "
                        + "E\",\"E → E + · E\",\"E → E + E ·\"]}",
                convertedGrammar.toString()
        );
    }

    private GrammarConverterPipeline getGrammarConverterPipeline() {
        return GrammarConverterPipelineImpl
                .builder()
                .registerGrammarConverter(MergeGrammarConverter.class)
                .registerGrammarConverter(StatusExpandGrammarConverter.class)
                .build();
    }
}
