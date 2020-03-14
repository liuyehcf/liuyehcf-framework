package com.github.liuyehcf.framework.compile.engine.test;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.DefaultLexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.*;
import com.github.liuyehcf.framework.compile.engine.test.cfg.TestLexicalAnalyzer;

public abstract class GrammarCase {

    /**
     * LL1文法
     */
    public static abstract class LL1_1 {

        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator("E"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("T"),
                                        Symbol.createNonTerminator("E′")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E′"),
                                SymbolString.create(
                                        Symbol.createTerminator("+"),
                                        Symbol.createNonTerminator("T"),
                                        Symbol.createNonTerminator("E′")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E′"),
                                SymbolString.create(
                                        Symbol.EPSILON
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("T"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("F"),
                                        Symbol.createNonTerminator("T′")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("T′"),
                                SymbolString.create(
                                        Symbol.createTerminator("*"),
                                        Symbol.createNonTerminator("F"),
                                        Symbol.createNonTerminator("T′")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("T′"),
                                SymbolString.create(
                                        Symbol.EPSILON
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("F"),
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
                                Symbol.createNonTerminator("F"),
                                SymbolString.create(
                                        Symbol.createRegexTerminator("id")
                                ),
                                null
                        )
                )
        );

        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("("), "(")
                .addNormalMorpheme(Symbol.createTerminator(")"), ")")
                .addNormalMorpheme(Symbol.createTerminator("+"), "+")
                .addNormalMorpheme(Symbol.createTerminator("*"), "*")
                .addRegexMorpheme(Symbol.createRegexTerminator("id"), TestLexicalAnalyzer.getIdRegex())
                .build();

        public static String[] TRUE_CASES = new String[]{
                "(a)",
                "a+b*c",
                "(a+b)*c",
                "(a+b*c+(d+e)*(f*g))",
                "(a+b*cA+(name+e)*(age*hello))",
        };

        public static String[] FALSE_CASES = new String[]{
                "",
                "a+0*c",
                "(a+b)*1",
                "(a+b*2B+(d+e)*(3*g))",
                "(a+b*1A+(name+e)*(age*hello))",
                "()",
        };
    }

    /**
     * LL1文法
     */
    public static abstract class LL1_2 {

        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("program "), "program ")
                .addNormalMorpheme(Symbol.createTerminator(":"), ":")
                .addNormalMorpheme(Symbol.createTerminator(";"), ";")
                .addNormalMorpheme(Symbol.createTerminator(" end"), " end")
                .addNormalMorpheme(Symbol.createTerminator("id"), "id")
                .addNormalMorpheme(Symbol.createTerminator(","), ",")
                .addNormalMorpheme(Symbol.createTerminator("s"), "s")
                .addNormalMorpheme(Symbol.createTerminator("real"), "real")
                .addNormalMorpheme(Symbol.createTerminator("int"), "int")
                .build();

        public static String[] TRUE_CASES = new String[]{
                "program id,id,id:real;s;s end",
                "program id:int;s;s end",
                "program id,id:int;s end",
        };

        public static String[] FALSE_CASES = new String[]{
                "",
                "id,id,id:real;s;s end",
                "program :real;s;s end",
                "program id,id,id:double;s;s end",
                "program id,id,id:real;s;s",
                "program id,id,id:real;s,s end",
        };

        private static String PROGRAM = "PROGRAM";
        private static String DECLIST = "DECLIST";
        private static String DECLISTN = "DECLISTN";
        private static String STLIST = "STLIST";
        private static String STLISTN = "STLISTN";
        private static String TYPE = "TYPE";

        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator(PROGRAM),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(PROGRAM),
                                SymbolString.create(
                                        Symbol.createTerminator("program "),
                                        Symbol.createNonTerminator(DECLIST),
                                        Symbol.createTerminator(":"),
                                        Symbol.createNonTerminator(TYPE),
                                        Symbol.createTerminator(";"),
                                        Symbol.createNonTerminator(STLIST),
                                        Symbol.createTerminator(" end")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(DECLIST),
                                SymbolString.create(
                                        Symbol.createTerminator("id"),
                                        Symbol.createNonTerminator(DECLISTN)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(DECLISTN),
                                SymbolString.create(
                                        Symbol.createTerminator(","),
                                        Symbol.createTerminator("id"),
                                        Symbol.createNonTerminator(DECLISTN)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(DECLISTN),
                                SymbolString.create(
                                        Symbol.EPSILON
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(STLIST),
                                SymbolString.create(
                                        Symbol.createTerminator("s"),
                                        Symbol.createNonTerminator(STLISTN)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(STLISTN),
                                SymbolString.create(
                                        Symbol.createTerminator(";"),
                                        Symbol.createTerminator("s"),
                                        Symbol.createNonTerminator(STLISTN)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(STLISTN),
                                SymbolString.create(
                                        Symbol.EPSILON
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(TYPE),
                                SymbolString.create(
                                        Symbol.createTerminator("real")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(TYPE),
                                SymbolString.create(
                                        Symbol.createTerminator("int")
                                ),
                                null
                        )
                )
        );

    }

    /**
     * LL1文法
     */
    public static abstract class LL1_3 {

        public static Grammar GRAMMAR = Grammar.create(
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

        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("a"), "a")
                .addNormalMorpheme(Symbol.createTerminator("ab"), "ab")
                .addNormalMorpheme(Symbol.createTerminator("abc"), "abc")
                .addNormalMorpheme(Symbol.createTerminator("abcd"), "abcd")
                .addNormalMorpheme(Symbol.createTerminator("b"), "b")
                .addNormalMorpheme(Symbol.createTerminator("bc"), "bc")
                .addNormalMorpheme(Symbol.createTerminator("bcd"), "bcd")
                .addNormalMorpheme(Symbol.createTerminator("c"), "c")
                .addNormalMorpheme(Symbol.createTerminator("cd"), "cd")
                .addNormalMorpheme(Symbol.createTerminator("d"), "d")
                .build();

        public static String[] TRUE_CASES = new String[]{
                "a",
                "ab",
                "abc",
                "abcd",
                "b",
                "bc",
                "bcd",
                "c",
                "cd",
                "d",
        };

        public static String[] FALSE_CASES = new String[]{
                "",
                "e",
                "ba",
                "ac",
                "bdc",
        };
    }

    /**
     * LR0文法
     */
    public static abstract class LR0_1 {

        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator("S"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("S"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("B"),
                                        Symbol.createNonTerminator("B")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("B"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createNonTerminator("B")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("B"),
                                SymbolString.create(
                                        Symbol.createTerminator("b")
                                ),
                                null
                        )
                )
        );

        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("a"), "a")
                .addNormalMorpheme(Symbol.createTerminator("b"), "b")
                .build();

        public static String[] TRUE_CASES = new String[]{
                "bab",
                "bb",
                "aaaabab",
        };

        public static String[] FALSE_CASES = new String[]{
                "",
                "a",
                "b",
                "aba",
        };
    }

    /**
     * SLR文法
     */
    public static abstract class SLR_1 {

        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator("E"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("E"),
                                        Symbol.createTerminator("+"),
                                        Symbol.createNonTerminator("T")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("E"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("T")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("T"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("T"),
                                        Symbol.createTerminator("*"),
                                        Symbol.createNonTerminator("F")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("T"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("F")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("F"),
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
                                Symbol.createNonTerminator("F"),
                                SymbolString.create(
                                        Symbol.createRegexTerminator("id")
                                ),
                                null
                        )
                )
        );

        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("("), "(")
                .addNormalMorpheme(Symbol.createTerminator(")"), ")")
                .addNormalMorpheme(Symbol.createTerminator("*"), "*")
                .addNormalMorpheme(Symbol.createTerminator("+"), "+")
                .addRegexMorpheme(Symbol.createRegexTerminator("id"), TestLexicalAnalyzer.getIdRegex())
                .build();

        public static String[] TRUE_CASES = new String[]{
                "a",
                "a1+a2",
                "a+b*c+d",
                "a+b+c+d",
                "a+(b*c)+d",
                "(a+b*c)+d",
                "(a+b+c+d)",
        };

        public static String[] FALSE_CASES = new String[]{
                "",
                "a1-a2",
                "a-b*c+d",
                "a+(b-c)+d",
                "(a+b-c)+d",
        };
    }

    /**
     * SLR文法
     */
    public static abstract class SLR_2 {

        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator("T"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("T"),
                                SymbolString.create(
                                        Symbol.createTerminator("a"),
                                        Symbol.createNonTerminator("B"),
                                        Symbol.createTerminator("d")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("T"),
                                SymbolString.create(
                                        Symbol.EPSILON
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("B"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("T"),
                                        Symbol.createTerminator("b")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("B"),
                                SymbolString.create(
                                        Symbol.EPSILON
                                ),
                                null
                        )
                )
        );

        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("a"), "a")
                .addNormalMorpheme(Symbol.createTerminator("b"), "b")
                .addNormalMorpheme(Symbol.createTerminator("d"), "d")
                .build();

        public static String[] TRUE_CASES = new String[]{
                "",
                "ad",
                "aadbd",
                "aaadbdbd",
        };

        public static String[] FALSE_CASES = new String[]{
                "a",
                "aadd",
        };
    }

    /**
     * LR1文法
     */
    public static abstract class LR1_1 {

        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator("S"),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("S"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("L"),
                                        Symbol.createTerminator("="),
                                        Symbol.createNonTerminator("R")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("S"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("R")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("L"),
                                SymbolString.create(
                                        Symbol.createTerminator("*"),
                                        Symbol.createNonTerminator("R")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("L"),
                                SymbolString.create(
                                        Symbol.createRegexTerminator("id")
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator("R"),
                                SymbolString.create(
                                        Symbol.createNonTerminator("L")
                                ),
                                null
                        )
                )
        );

        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("*"), "*")
                .addNormalMorpheme(Symbol.createTerminator("="), "=")
                .addRegexMorpheme(Symbol.createRegexTerminator("id"), TestLexicalAnalyzer.getIdRegex())
                .build();

        public static String[] TRUE_CASES = new String[]{
                "a1=b2",
                "a=*b",
                "a=**b",
                "*a=b",
                "*a=*b",
                "*a=**b",
                "**a=b",
                "**a=*b",
                "**a=**b",
        };

        public static String[] FALSE_CASES = new String[]{
                "",
                "*",
                "a==*b",
                "a*b"
        };
    }

    /**
     * LR1文法
     */
    public static abstract class LR1_2 {

        private static final String SYNTAX = "syntax";
        private static final String EXPRESSION = "expression";
        private static final String BOOL_EXPRESSION = "bool_expression";
        private static final String COMPARABLE_EXPRESSION = "comparable_expression";
        private static final String COMPARABLE_OPERATOR = "comparable_operator";
        private static final String RANGE_EXPRESSION = "range_expression";
        private static final String RANGE_OPERATOR = "range_operator";
        private static final String EXPRESSION_LIST = "expression_list";
        private static final String FACTORY = "factory";
        private static final String CALL_EXPRESSION = "call_expression";
        private static final String EXPRESSIONS = "expressions";
        private static final String ID = "id";
        private static final String NUMBER = "number";
        private static final String STRING = "string";
        private static final String PROPERTY_EXPRESSION = "property_expression";

        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator(SYNTAX),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(SYNTAX),
                                SymbolString.create(
                                        Symbol.createNonTerminator(EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(EXPRESSION),
                                        Symbol.createTerminator("||"),
                                        Symbol.createNonTerminator(BOOL_EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(BOOL_EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(BOOL_EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(BOOL_EXPRESSION),
                                        Symbol.createTerminator("&&"),
                                        Symbol.createNonTerminator(COMPARABLE_EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(BOOL_EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(COMPARABLE_EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(COMPARABLE_EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(COMPARABLE_EXPRESSION),
                                        Symbol.createRegexTerminator(COMPARABLE_OPERATOR),
                                        Symbol.createNonTerminator(RANGE_EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(COMPARABLE_EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(RANGE_EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(RANGE_EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(RANGE_EXPRESSION),
                                        Symbol.createRegexTerminator(RANGE_OPERATOR),
                                        Symbol.createTerminator('['),
                                        Symbol.createNonTerminator(EXPRESSION_LIST),
                                        Symbol.createTerminator(']')
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(RANGE_EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(FACTORY)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(FACTORY),
                                SymbolString.create(
                                        Symbol.createTerminator('('),
                                        Symbol.createNonTerminator(EXPRESSION),
                                        Symbol.createTerminator(')')
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(FACTORY),
                                SymbolString.create(
                                        Symbol.createNonTerminator(CALL_EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(CALL_EXPRESSION),
                                SymbolString.create(
                                        Symbol.createRegexTerminator(ID),
                                        Symbol.createTerminator('('),
                                        Symbol.createNonTerminator(EXPRESSIONS),
                                        Symbol.createTerminator(')')
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSIONS),
                                SymbolString.create(
                                        Symbol.createNonTerminator(EXPRESSION_LIST)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSIONS),
                                SymbolString.create(
                                        Symbol.EPSILON
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSION_LIST),
                                SymbolString.create(
                                        Symbol.createNonTerminator(EXPRESSION_LIST),
                                        Symbol.createTerminator(','),
                                        Symbol.createNonTerminator(EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSION_LIST),
                                SymbolString.create(
                                        Symbol.createNonTerminator(EXPRESSION)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(FACTORY),
                                SymbolString.create(
                                        Symbol.createRegexTerminator(NUMBER)
                                ),
                                null
                        )
                ),
                Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(FACTORY),
                                SymbolString.create(
                                        Symbol.createRegexTerminator(STRING)
                                ),
                                null
                        )
                ), Production.create(
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(FACTORY),
                                SymbolString.create(
                                        Symbol.createRegexTerminator(PROPERTY_EXPRESSION)
                                ),
                                null
                        )
                )
        );
        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("&&"), "&&")
                .addNormalMorpheme(Symbol.createTerminator("||"), "||")
                .addNormalMorpheme(Symbol.createTerminator("("), "(")
                .addNormalMorpheme(Symbol.createTerminator(")"), ")")
                .addNormalMorpheme(Symbol.createTerminator("["), "[")
                .addNormalMorpheme(Symbol.createTerminator("]"), "]")
                .addNormalMorpheme(Symbol.createTerminator(","), ",")
                .addRegexMorpheme(Symbol.createRegexTerminator(COMPARABLE_OPERATOR), "(>=?+)|(<=?+)|(==)|(!=)|like")
                .addRegexMorpheme(Symbol.createRegexTerminator(RANGE_OPERATOR), "(in)|(between)")
                .addRegexMorpheme(Symbol.createRegexTerminator(ID), TestLexicalAnalyzer.getIdRegex())
                .addRegexMorpheme(Symbol.createRegexTerminator(NUMBER), TestLexicalAnalyzer.getDigitRegex())
                .addRegexMorpheme(Symbol.createRegexTerminator(PROPERTY_EXPRESSION), "\\$\\{ *[a-zA-Z]+(\\.[a-zA-Z]+)* *\\}")
                .addRegexMorpheme(Symbol.createRegexTerminator(STRING), "\"[^\"]*\"")
                .build();

        public static String[] TRUE_CASES = new String[]{
                "func()",
                "func(1)",
                "func(\"hehe\")",
                "func(   \"hehe\",\"xixi\")",
                "func(1,2)",
                "func(1,2,3,4,5,6)",
                "func(1,2,\"hehe\",9)",
                "func(  min(1,2),max(2,3),  3,\"xixi\",func(min(1,2),max(2,3),3,\"xixi\"))",
                "1<=  2",
                "${xxx.yyy.zzz} in [1]",
                "${xxx.yyy.zzz} in [1,  3,  4]",
                "${xxx.yyy.zzz} between [1.2,2]",
                "${xxx.yyy.zzz} between [1.2 ,5.6]",
                "${xxx.yyy.zzz} like \"asdfasdf\"",
                "${xxx.yyy.zzz} in [1,  3,  4] || ${xxx.yyy.zzz} between [1.2,2] && ${xxx.yyy.zzz} like \"asdfasdf\"",
                "2==2",
                "1<=2||(2==3)&&4!=5",
                "1<=2&&2==3||4!=5",
                "func(1<=2||2==3&&4!=5,2)",
                "func(1<2||2==3&&4 !=  5,2)||(func(1<=2||2==3&&4!=5,2))  &&(func(1<=2||2==3&&4!=5,2))",
                "func(1<=2||2==3&&4 !=5,2)||    func(1>2||2==3&&4!=5,2)&&(func(1>=2||2==3&&4!=5,2))",
                "func(1<=2||2==3&&4 !=5,2)||    func(1<2||2==3&&4!=5,2|| ${xxx.yyy.zzz} in [1,  ${xxx.yyy.zzz} in [1,  "
                        + "3,  4] || ${xxx.yyy.zzz} between [1.2,2] && ${xxx.yyy.zzz} like \"asdfasdf\",  4] || ${xxx.yyy.zzz}"
                        + " between [1.2,func(1<=2||2==3&&4!=5,2)] && ${xxx.yyy.zzz} like \"asdfasdf\")&&(func"
                        + "(1<=2||2==3&&4!=5,2))",
        };

        public static String[] FALSE_CASES = new String[]{
                "",
                "abc",
                "func())",
                "func(1,2,)",
                "func(,)"
        };
    }

    /**
     * LR1文法
     */
    public static abstract class LR1_3 {

        private static final String EXPRESSION_NAME = "<expression name>"; // 294
        private static final String EXPRESSION = "<expression>"; // 218
        private static final String ASSIGNMENT = "<assignment>"; // 222
        private static final String LEFT_HAND_SIDE = "<left hand side>"; // 224
        private static final String ASSIGNMENT_OPERATOR = "<assignment operator>"; // 226
        private static final String PRIMARY_NO_NEW_ARRAY = "<primary no new array>"; // 272
        private static final String NORMAL_ASSIGN = "=";
        private static final String NORMAL_MUL_ASSIGN = "*=";
        private static final String NORMAL_SMALL_LEFT_PARENTHESES = "(";
        private static final String NORMAL_SMALL_RIGHT_PARENTHESES = ")";
        private static final String REGEX_IDENTIFIER = "@identifier";
        private static final String MARK_222_1_1 = "<mark 222_1_1>";
        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator(EXPRESSION),
                /*
                 * <expression> 218
                 * SAME
                 */
                Production.create(
                        /*
                         * <expression> → <primary no new array>
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY)
                                ),
                                null
                        ),
                        /*
                         * <expression> → <assignment>
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSION),
                                SymbolString.create(
                                        Symbol.createNonTerminator(ASSIGNMENT)
                                ),
                                null
                        )
                ),


                /*
                 * <assignment> 222
                 * SAME
                 */
                Production.create(
                        /*
                         * <assignment> → <left hand side> <assignment operator> <mark 222_1_1> <expression>
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(ASSIGNMENT),
                                SymbolString.create(
                                        Symbol.createNonTerminator(LEFT_HAND_SIDE),
                                        Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                                        Symbol.createNonTerminator(MARK_222_1_1),
                                        Symbol.createNonTerminator(EXPRESSION)
                                ),
                                null
                        )
                ),


                /*
                 * <mark 222_1_1>
                 */
                Production.create(
                        /*
                         * <mark 222_1_1> → ε
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(MARK_222_1_1),
                                SymbolString.create(
                                        Symbol.EPSILON
                                ),
                                null
                        )
                ),


                /*
                 * <left hand side> 224
                 * LACK
                 */
                Production.create(
                        /*
                         * <left hand side> → <expression name>
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(LEFT_HAND_SIDE),
                                SymbolString.create(
                                        Symbol.createNonTerminator(EXPRESSION_NAME)
                                ),
                                null
                        )
                ),


                /*
                 * <assignment operator> 226
                 * SAME
                 */
                Production.create(
                        /*
                         * <assignment operator> → *=
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                                SymbolString.create(
                                        Symbol.createTerminator(NORMAL_MUL_ASSIGN)
                                ),
                                null
                        )
                ),


                /*
                 * <primary no new array> 272
                 * LACK
                 */
                Production.create(
                        /*
                         * <primary no new array> → ( <expression> )
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                                SymbolString.create(
                                        Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                        Symbol.createNonTerminator(EXPRESSION),
                                        Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                                ),
                                null
                        ),
                        /*
                         * <primary no new array> → <expression name>
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                                SymbolString.create(
                                        Symbol.createNonTerminator(EXPRESSION_NAME)
                                ),
                                null
                        )
                ),


                /*
                 * <expression name> 294
                 * LACK
                 */
                Production.create(
                        /*
                         * <expression name> → @identifier
                         */
                        PrimaryProduction.create(
                                Symbol.createNonTerminator(EXPRESSION_NAME),
                                SymbolString.create(
                                        Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                                ),
                                null
                        )
                )

        );

        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES), "(")
                .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES), ")")
                .addNormalMorpheme(Symbol.createTerminator(NORMAL_MUL_ASSIGN), "*=")
                .addNormalMorpheme(Symbol.createTerminator(NORMAL_ASSIGN), "=")
                .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*")
                .build();

        public static String[] TRUE_CASES = new String[]{
                "a *= b"
        };

        public static String[] FALSE_CASES = new String[]{
        };
    }

    /**
     * 二义性文法
     */
    public static abstract class Ambiguity_1 {

        public static Grammar GRAMMAR = Grammar.create(
                Symbol.createNonTerminator("E"),
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
                                        Symbol.createRegexTerminator("id")
                                ),
                                null
                        )
                )
        );
        public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
                .addNormalMorpheme(Symbol.createTerminator("*"), "*")
                .addNormalMorpheme(Symbol.createTerminator("="), "=")
                .addRegexMorpheme(Symbol.createRegexTerminator("id"), TestLexicalAnalyzer.getIdRegex())
                .build();
    }
}

