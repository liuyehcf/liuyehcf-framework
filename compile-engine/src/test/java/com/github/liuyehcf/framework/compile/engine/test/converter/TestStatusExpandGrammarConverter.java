package com.github.liuyehcf.framework.compile.engine.test.converter;

import com.github.liuyehcf.framework.compile.engine.grammar.converter.GrammarConverterPipeline;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.GrammarConverterPipelineImpl;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.MergeGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.StatusExpandGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.*;
import com.github.liuyehcf.framework.compile.engine.test.GrammarCase;
import org.junit.Test;

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
