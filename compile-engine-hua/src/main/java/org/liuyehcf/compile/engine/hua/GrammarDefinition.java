package org.liuyehcf.compile.engine.hua;

import org.liuyehcf.compile.engine.core.cfg.DefaultLexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.grammar.definition.*;

/**
 * @author chenlu
 * @date 2018/5/28
 */
public class GrammarDefinition {

    private static final String PROGRAMS = "<programs>";
    private static final String VARIABLE_DEFINITION = "<variable definition>";
    private static final String TYPE = "<type>";
    private static final String SEMICOLON = "<;>";
    private static final String ID = "<id>";

    public static final Grammar GRAMMAR = Grammar.create(
            Symbol.createNonTerminator(PROGRAMS),
            /*
             * <programs> → <variable definition>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DEFINITION)
                            )
                            , null
                    )
            ),
            /*
             * <variable definition> → <type> <id> <;>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DEFINITION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(TYPE),
                                    Symbol.createRegexTerminator(ID),
                                    Symbol.createTerminator(SEMICOLON)
                            )
                            , null
                    )
            )
    );

    public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addNormalMorpheme(Symbol.createTerminator("("), "(")
            .addNormalMorpheme(Symbol.createTerminator(")"), ")")
            .addNormalMorpheme(Symbol.createTerminator("+"), "+")
            .addNormalMorpheme(Symbol.createTerminator("*"), "*")
            .addRegexMorpheme(Symbol.createRegexTerminator("id"), "[a-zA-Z_]([a-zA-Z_]|[0-9])*")
            .build();

    public static void main(String[] args) {
        System.out.println(GRAMMAR);
    }
}
