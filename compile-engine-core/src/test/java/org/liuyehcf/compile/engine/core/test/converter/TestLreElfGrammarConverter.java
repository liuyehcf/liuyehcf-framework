package org.liuyehcf.compile.engine.core.test.converter;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.grammar.converter.GrammarConverterPipeline;
import org.liuyehcf.compile.engine.core.grammar.converter.GrammarConverterPipelineImpl;
import org.liuyehcf.compile.engine.core.grammar.converter.LreElfGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.converter.MergeGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.definition.*;
import org.liuyehcf.compile.engine.core.test.GrammarCase;

import static org.junit.Assert.assertEquals;

public class TestLreElfGrammarConverter {
    @Test
    public void convertCase1() {

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(GrammarCase.GRAMMAR_8.GRAMMAR);

        assertEquals(
                "{\"productions\":[\"E → ( E ) (E)′ | id (E)′\",\"(E)′ → * E (E)′ | + E (E)′ | __ε__\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase2() {
        Grammar grammar = Grammar.create(
                Symbol.createNonTerminator("D"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("D"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("E")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("E"),
                                        Symbol.createTerminator("+"),
                                        Symbol.createNonTerminator("E")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("E"),
                                        Symbol.createTerminator("*"),
                                        Symbol.createNonTerminator("E")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createTerminator("("),
                                        Symbol.createNonTerminator("E"),
                                        Symbol.createTerminator(")")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createTerminator("id")
                                ),
                                null
                        )
                )
        );

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(grammar);

        assertEquals(
                "{\"productions\":[\"D → ( E ) (E)′ | id (E)′\",\"E → ( E ) (E)′ | id (E)′\",\"(E)′ → * E (E)′ | + E (E)′"
                        + " | __ε__\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase3() {
        Grammar grammar = Grammar.create(
                Symbol.createNonTerminator("D"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("D"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("E"),
                                        Symbol.createTerminator("e")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("D"),
                                SymbolString.create(
                                        Symbol.createTerminator("e"),
                                        Symbol.createNonTerminator("E")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("E"),
                                        Symbol.createTerminator("+"),
                                        Symbol.createNonTerminator("E")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("E"),
                                        Symbol.createTerminator("*"),
                                        Symbol.createNonTerminator("E")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createTerminator("("),
                                        Symbol.createNonTerminator("E"),
                                        Symbol.createTerminator(")")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createTerminator("id")
                                ),
                                null
                        )
                )
        );

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(grammar);

        assertEquals(
                "{\"productions\":[\"D → ( E ) (E)′ e | e E | id (E)′ e\",\"E → ( E ) (E)′ | id (E)′\",\"(E)′ → * E (E)′ "
                        + "| + E (E)′ | __ε__\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase4() {
        Grammar grammar = Grammar.create(
                Symbol.createNonTerminator("A"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createTerminator("b")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createTerminator("b"),
                                        Symbol.createTerminator("c")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("b"),
                                        Symbol.createTerminator("d")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("b"),
                                        Symbol.createTerminator("c")
                                ),
                                null
                        )
                )
        );

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(grammar);

        assertEquals(
                "{\"productions\":[\"A → a (A)′ | b (A)′′\",\"(A)′ → b (A)′′′\",\"(A)′′ → c | d\",\"(A)′′′ → __ε__ | c\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase5() {
        Grammar grammar = Grammar.create(
                Symbol.createNonTerminator("A"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createTerminator("β1")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createTerminator("β2")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createTerminator("βn")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("γ1")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("γ2")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("γm")
                                ),
                                null
                        )
                )
        );

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(grammar);

        assertEquals(
                "{\"productions\":[\"A → a (A)′ | γ1 | γ2 | γm\",\"(A)′ → β1 | β2 | βn\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase6() {
        Grammar grammar = Grammar.create(
                Symbol.createNonTerminator("A"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createTerminator("b")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createTerminator("b"),
                                        Symbol.createTerminator("c")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createTerminator("b"),
                                        Symbol.createTerminator("c"),
                                        Symbol.createTerminator("d")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("b")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("b"),
                                        Symbol.createTerminator("c")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("b"),
                                        Symbol.createTerminator("c"),
                                        Symbol.createTerminator("d")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("c")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("c"),
                                        Symbol.createTerminator("d")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("A"),
                                SymbolString.create(
                                        Symbol.createTerminator("d")
                                ),
                                null
                        )
                )
        );

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(grammar);

        assertEquals(
                "{\"productions\":[\"A → a (A)′ | b (A)′′ | c (A)′′′ | d\",\"(A)′ → __ε__ | b (A)′′′′′\",\"(A)′′ → __ε__ "
                        + "| c (A)′′′′\",\"(A)′′′ → __ε__ | d\",\"(A)′′′′ → __ε__ | d\",\"(A)′′′′′ → __ε__ | c (A)′′′′′′\",\""
                        + "(A)′′′′′′ → __ε__ | d\"]}",
                convertedGrammar.toString()
        );
    }

    private GrammarConverterPipeline getGrammarConverterPipeline() {
        return GrammarConverterPipelineImpl
                .builder()
                .registerGrammarConverter(MergeGrammarConverter.class)
                .registerGrammarConverter(LreElfGrammarConverter.class)
                .build();
    }
}
