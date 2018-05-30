package org.liuyehcf.compile.engine.hua;

import org.liuyehcf.compile.engine.core.cfg.DefaultLexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.*;

/**
 * @author chenlu
 * @date 2018/5/28
 */
public class GrammarDefinition {

    /**
     * 非终结符
     */
    private static final String PROGRAMS = "<programs>";
    private static final String FUNCTION_DEFINITIONS = "<function definitions>";
    private static final String FUNCTION_DEFINITION = "<function definition>";
    private static final String PARAMS = "<params>";
    private static final String PARAM_LIST = "<param list>";
    private static final String PARAM = "<param>";
    private static final String FUNCTION_BODY = "<function body>";
    private static final String VARIABLE_DEFINITION = "<variable definition>";


    /**
     * 正则表达式的终结符
     */
    private static final String ID = "@id";
    private static final String TYPE = "@type";

    /**
     * 普通终结符
     */
    private static final String SMALL_LEFT_PARENTHESES = "(";
    private static final String SMALL_RIGHT_PARENTHESES = ")";
    private static final String MIDDLE_LEFT_PARENTHESES = "[";
    private static final String MIDDLE_RIGHT_PARENTHESES = "]";
    private static final String LARGE_LEFT_PARENTHESES = "{";
    private static final String LARGE_RIGHT_PARENTHESES = "}";
    private static final String COMMA = "comma";
    private static final String SEMICOLON = ";";

    public static final Grammar GRAMMAR = Grammar.create(
            Symbol.createNonTerminator(PROGRAMS),
            /*
             * <programs> → <function definitions>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FUNCTION_DEFINITIONS)
                            )
                            , null
                    )
            ),
            /*
             * <function definitions> → <function definitions> <function definition>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FUNCTION_DEFINITIONS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FUNCTION_DEFINITIONS),
                                    Symbol.createNonTerminator(FUNCTION_DEFINITION)
                            )
                            , null
                    )
            ),
            /*
             * <function definitions> → <function definition>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FUNCTION_DEFINITIONS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FUNCTION_DEFINITION)
                            )
                            , null
                    )
            ),
            /*
             * <function definition> → @type @id ( <params> ) { <function body> }
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FUNCTION_DEFINITION),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(TYPE),
                                    Symbol.createRegexTerminator(ID),
                                    Symbol.createTerminator(SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(PARAMS),
                                    Symbol.createTerminator(SMALL_RIGHT_PARENTHESES),
                                    Symbol.createTerminator(LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(FUNCTION_BODY),
                                    Symbol.createTerminator(LARGE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),
            /*
             * <params> → ε
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PARAMS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    )
            ),
            /*
             * <params> → <param list>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PARAMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PARAM_LIST)
                            )
                            , null
                    )
            ),
            /*
             * <param list> → <param list> , <param>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PARAM_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PARAM_LIST),
                                    Symbol.createTerminator(COMMA),
                                    Symbol.createNonTerminator(PARAM)
                            )
                            , null
                    )
            ),
            /*
             * <param list> → <param>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PARAM_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PARAM)
                            )
                            , null
                    )
            ),
            /*
             * <param> → @type @id
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PARAM),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(TYPE),
                                    Symbol.createRegexTerminator(ID)
                            )
                            , null
                    )
            ),
            /*
             * <function body> → ε
             * TODO
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FUNCTION_BODY),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    )
            )
    );

    public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addNormalMorpheme(Symbol.createTerminator(SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(LARGE_LEFT_PARENTHESES), "{")
            .addNormalMorpheme(Symbol.createTerminator(LARGE_RIGHT_PARENTHESES), "}")
            .addNormalMorpheme(Symbol.createTerminator(COMMA), ",")
            .addRegexMorpheme(Symbol.createRegexTerminator(TYPE), "(void)|(int)|(float)|(char)|(bool)")
            .addRegexMorpheme(Symbol.createRegexTerminator(ID), "[a-zA-Z_]([a-zA-Z_]|[0-9])*")
            .build();

    public static void main(String[] args) {
        LRCompiler compiler = LR1.create(LEXICAL_ANALYZER, GRAMMAR);
        System.out.println(compiler.isLegal());

        System.out.println(compiler.compile("void sort(int a){}\n" +
                "\n" +
                "int add(int a,int b){}\n" +
                "\n" +
                "void exchange(int a,int b){}"));
    }
}
