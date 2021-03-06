package com.github.liuyehcf.framework.compile.engine.test.converter;

import com.github.liuyehcf.framework.compile.engine.grammar.converter.GrammarConverterPipeline;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.GrammarConverterPipelineImpl;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.MergeGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.SimplificationGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.*;
import com.github.liuyehcf.framework.compile.engine.rg.RGBuilder;
import com.github.liuyehcf.framework.compile.engine.rg.utils.GrammarUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSimplificationGrammarConverter {
    @Test
    public void convertCase1() {
        Symbol digit = Symbol.createNonTerminator("digit");
        Symbol letter_ = Symbol.createNonTerminator("letter_");
        Symbol id = Symbol.createNonTerminator("id");

        Grammar grammar = Grammar.create(
                id,
                Production.create(
                        PrimaryProduction.create(
                                digit,
                                GrammarUtils.createPrimaryProduction("[0123456789]"),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                letter_,
                                GrammarUtils.createPrimaryProduction("[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_]"),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                id,
                                SymbolString.create(
                                        letter_,
                                        Symbol.createTerminator("("),
                                        letter_,
                                        Symbol.createTerminator("|"),
                                        digit,
                                        Symbol.createTerminator(")"),
                                        Symbol.createTerminator("*")
                                ),
                                null
                        )
                )
        );

        Grammar convertedGrammar = RGBuilder.compile(grammar).getGrammar();

        assertEquals(
                "{\"productions\":[\"id → ( [ a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L"
                        + " M N O P Q R S T U V W X Y Z _ ] ) ( ( [ a b c d e f g h i j k l m n o p q r s t u v w x y z A B C"
                        + " D E F G H I J K L M N O P Q R S T U V W X Y Z _ ] ) | ( [ 0 1 2 3 4 5 6 7 8 9 ] ) ) *\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase2() {
        Symbol digit = Symbol.createNonTerminator("digit");
        Symbol letter_ = Symbol.createNonTerminator("letter_");
        Symbol id = Symbol.createNonTerminator("id");

        Grammar grammar = Grammar.create(
                id,
                Production.create(
                        PrimaryProduction.create(
                                id,
                                SymbolString.create(
                                        letter_,
                                        Symbol.createTerminator("("),
                                        letter_,
                                        Symbol.createTerminator("|"),
                                        digit,
                                        Symbol.createTerminator(")"),
                                        Symbol.createTerminator("*")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                letter_,
                                GrammarUtils.createPrimaryProduction("[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_]"),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                digit,
                                GrammarUtils.createPrimaryProduction("[0123456789]"),
                                null
                        )
                )
        );

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(grammar);

        assertEquals(
                "{\"productions\":[\"id → ( [ a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L"
                        + " M N O P Q R S T U V W X Y Z _ ] ) ( ( [ a b c d e f g h i j k l m n o p q r s t u v w x y z A B C"
                        + " D E F G H I J K L M N O P Q R S T U V W X Y Z _ ] ) | ( [ 0 1 2 3 4 5 6 7 8 9 ] ) ) *\"]}",
                convertedGrammar.toString()
        );
    }

    @Test
    public void convertCase3() {
        Symbol digit = Symbol.createNonTerminator("digit");
        Symbol letter_ = Symbol.createNonTerminator("letter_");
        Symbol id = Symbol.createNonTerminator("id");

        Grammar grammar = Grammar.create(
                id,
                Production.create(
                        PrimaryProduction.create(
                                id,
                                SymbolString.create(
                                        letter_,
                                        Symbol.createTerminator("("),
                                        letter_,
                                        Symbol.createTerminator("|"),
                                        digit,
                                        Symbol.createTerminator(")"),
                                        Symbol.createTerminator("*")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                digit,
                                GrammarUtils.createPrimaryProduction("[0123456789]"),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                letter_,
                                GrammarUtils.createPrimaryProduction("[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_]"),
                                null
                        )
                )
        );

        Grammar convertedGrammar = getGrammarConverterPipeline().convert(grammar);

        assertEquals(
                "{\"productions\":[\"id → ( [ a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L"
                        + " M N O P Q R S T U V W X Y Z _ ] ) ( ( [ a b c d e f g h i j k l m n o p q r s t u v w x y z A B C"
                        + " D E F G H I J K L M N O P Q R S T U V W X Y Z _ ] ) | ( [ 0 1 2 3 4 5 6 7 8 9 ] ) ) *\"]}",
                convertedGrammar.toString()
        );
    }

    private GrammarConverterPipeline getGrammarConverterPipeline() {
        return GrammarConverterPipelineImpl
                .builder()
                .registerGrammarConverter(MergeGrammarConverter.class)
                .registerGrammarConverter(SimplificationGrammarConverter.class)
                .build();
    }
}
