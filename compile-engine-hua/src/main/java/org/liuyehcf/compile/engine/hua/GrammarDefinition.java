package org.liuyehcf.compile.engine.hua;

import org.liuyehcf.compile.engine.core.grammar.definition.*;

/**
 * @author chenlu
 * @date 2018/5/28
 */
public class GrammarDefinition {

    private static final String PROGRAMS = "<programs>";
    private static final String VARIABLE_DEFINITION = "<variable definition>";
    private static final String TYPE = "<type>";
    private static final String SEMICOLON = ";";
    private static final String ID = "<id>";

    public static final Grammar GRAMMAR = Grammar.create(
            Symbol.createNonTerminator(PROGRAMS),
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

    public static void main(String[] args) {
        System.out.println(GRAMMAR);
    }
}
