package com.github.liuyehcf.framework.compile.engine.test.example.calculator;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.DefaultLexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.impl.IntegerIdentifier;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Production;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SymbolString;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Add;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Div;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Mul;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Sub;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.semantic.AddComputeCode;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.semantic.AddLoad;

class CalculatorGrammar {
    static final String PROGRAM = "<program>";
    private static final String EXPRESSION = "<expression>";
    private static final String ADDITIVE_EXPRESSION = "<additive expression>";
    private static final String MULTIPLICATIVE_EXPRESSION = "<multiplicative expression>";
    private static final String PRIMARY = "<primary>";

    private static final String IDENTIFIER_INTEGER_LITERAL = "#integerLiteral";

    private static final String NORMAL_SMALL_LEFT_PARENTHESES = "(";
    private static final String NORMAL_SMALL_RIGHT_PARENTHESES = ")";

    private static final String NORMAL_MUL = "*";
    private static final String NORMAL_DIV = "/";
    private static final String NORMAL_ADD = "+";
    private static final String NORMAL_SUB = "-";
    static final Production[] PRODUCTIONS = {
            /*
             * <program>
             */
            Production.create(
                    /*
                     * <program> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAM),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            ),
                            null
                    )
            ),


            /*
             * <expression>
             */
            Production.create(
                    /*
                     * <expression> → <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            ),
                            null
                    )
            ),


            /*
             * <additive expression>
             */
            Production.create(
                    /*
                     * <additive expression> → <multiplicative expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION)
                            ),
                            null
                    ),
                    /*
                     * <additive expression> → <additive expression> + <multiplicative expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_ADD),
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION)
                            ),
                            new AddComputeCode(new Add())
                    ),
                    /*
                     * <additive expression> → <additive expression> - <multiplicative expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SUB),
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION)
                            ),
                            new AddComputeCode(new Sub())
                    )
            ),


            /*
             * <multiplicative expression>
             */
            Production.create(
                    /*
                     * <multiplicative expression> → <primary>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMARY)
                            ),
                            null
                    ),
                    /*
                     * <multiplicative expression> → <multiplicative expression> * <primary>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MUL),
                                    Symbol.createNonTerminator(PRIMARY)
                            ),
                            new AddComputeCode(new Mul())
                    ),
                    /*
                     * <multiplicative expression> → <multiplicative expression> / <primary>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_DIV),
                                    Symbol.createNonTerminator(PRIMARY)
                            ),
                            new AddComputeCode(new Div())
                    )
            ),


            /*
             * <primary>
             */
            Production.create(
                    /*
                     * <primary> → #integerLiteral
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY),
                            SymbolString.create(
                                    Symbol.createIdentifierTerminator(IDENTIFIER_INTEGER_LITERAL)
                            ),
                            new AddLoad()
                    ),
                    /*
                     * <primary> → ( <expression> )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            ),
                            null
                    )
            )
    };
    static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addTokenOperator(Symbol.createIdentifierTerminator(IDENTIFIER_INTEGER_LITERAL), new IntegerIdentifier())
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MUL), "*")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DIV), "/")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ADD), "+")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SUB), "-")
            .build();
}
