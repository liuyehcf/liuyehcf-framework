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
    private static final String METHOD_DECLARATIONS = "<method declarations>";
    private static final String METHOD_DECLARATION = "<method declaration>";
    private static final String METHOD_BODY = "<method body>";
    private static final String FORMAL_PARAMETERS = "<formal parameters>";
    private static final String FORMAL_PARAMETER_LIST = "<formal parameter list>";
    private static final String FORMAL_PARAMETER = "<formal parameter>";
    private static final String BLOCK = "<block>";
    private static final String BLOCK_STATEMENTS = "<block statements>";
    private static final String BLOCK_STATEMENT = "<block statement>";
    private static final String LOCAL_VARIABLE_DECLARATION_STATEMENT = "<local variable declaration statement>";
    private static final String LOCAL_VARIABLE_DECLARATION = "<local variable declaration>";
    private static final String VARIABLE_DECLARATORS = "<variable declarators>";
    private static final String VARIABLE_DECLARATOR = "<variable declarator>";
    private static final String VARIABLE_DECLARATOR_ID = "<variable declarator id>";
    private static final String VARIABLE_INITIALIZER = "<variable initializer>";
    private static final String ARRAY_INITIALIZER = "<array initializer>";
    private static final String EXPRESSION = "<expression>";
    private static final String STATEMENT = "<statement>";
    private static final String STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT = "<statement without trailing substatement>";
    private static final String EMPTY_STATEMENT = "<empty statement>";
    private static final String EXPRESSION_STATEMENT = "<expression statement>";
    private static final String RETURN_STATEMENT = "<return statement>";

    private static final String STATEMENT_EXPRESSION = "<statement expression>";
    private static final String ASSIGNMENT = "<assignment>";
    private static final String LEFT_HAND_SIDE = "<left hand side>";
    private static final String ASSIGNMENT_OPERATOR = "<assignment operator>";
    private static final String ASSIGNMENT_EXPRESSION = "<assignment expression>";
    private static final String EXPRESSION_NAME = "<expression name>";

    /**
     * 正则表达式的终结符
     */
    private static final String IDENTIFIER = "@identifier";
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
    private static final String ASSIGN = "=";
    private static final String COMMA = "comma";
    private static final String SEMICOLON = ";";

    public static final Grammar GRAMMAR = Grammar.create(
            Symbol.createNonTerminator(PROGRAMS),
            /*
             * <programs> → <method declarations>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATIONS)
                            )
                            , null
                    )
            ),
            /*
             * <method declarations> →  <method declarations>  <method declaration>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATIONS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATIONS),
                                    Symbol.createNonTerminator(METHOD_DECLARATION)
                            )
                            , null
                    )
            ),
            /*
             * <method declarations> → <method declaration>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATIONS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATION)
                            )
                            , null
                    )
            ),
            /*
             * <method declaration> → @type @identifier ( <formal parameters> ) <method body>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATION),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(TYPE),
                                    Symbol.createRegexTerminator(IDENTIFIER),
                                    Symbol.createTerminator(SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(FORMAL_PARAMETERS),
                                    Symbol.createTerminator(SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(METHOD_BODY)
                            )
                            , null
                    )
            ),
            /*
             * <formal parameters> → ε
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETERS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    )
            ),
            /*
             * <formal parameters> → <formal parameter list>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETERS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER_LIST)
                            )
                            , null
                    )
            ),
            /*
             * <formal parameter list> → <formal parameter list> , <formal parameter>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                                    Symbol.createTerminator(COMMA),
                                    Symbol.createNonTerminator(FORMAL_PARAMETER)
                            )
                            , null
                    )
            ),
            /*
             * <formal parameter list> → <formal parameter>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER)
                            )
                            , null
                    )
            ),
            /*
             * <formal parameter> → @type @identifier
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(TYPE),
                                    Symbol.createRegexTerminator(IDENTIFIER)
                            )
                            , null
                    )
            ),
            /*
             * <method body> → <block>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_BODY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            )
                            , null
                    )
            ),
            /*
             * <block> → { }
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(LARGE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(LARGE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),
            /*
             * <block> → { <block statements> }
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK),
                            SymbolString.create(
                                    Symbol.createTerminator(LARGE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS),
                                    Symbol.createTerminator(LARGE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),
            /*
             * <block statements> → <block statement>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENT)
                            )
                            , null
                    )
            ),
            /*
             * <block statements> → <block statements> <block statement>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENTS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK_STATEMENTS),
                                    Symbol.createNonTerminator(BLOCK_STATEMENT)
                            )
                            , null
                    )
            ),
            /*
             * <block statement> → <local variable declaration statement>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION_STATEMENT)
                            )
                            , null
                    )
            ),
            /*
             * <block statement> → <statement>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(BLOCK_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT)
                            )
                            , null
                    )
            ),
            /*
             * <local variable declaration statement> → <local variable declaration> ;
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION_STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                                    Symbol.createTerminator(SEMICOLON)
                            )
                            , null
                    )
            ),
            /*
             * <local variable declaration> → @type <variable declarators>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LOCAL_VARIABLE_DECLARATION),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(TYPE),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATORS)
                            )
                            , null
                    )
            ),
            /*
             * <variable declarators> → <variable declarator>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR)
                            )
                            , null
                    )
            ),
            /*
             * <variable declarators> → <variable declarators> , <variable declarator>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                                    Symbol.createTerminator(COMMA),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR)
                            )
                            , null
                    )
            ),
            /*
             * <variable declarator> → <variable declarator id>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID)
                            )
                            , null
                    )
            ),
            /*
             * <variable declarator> → <variable declarator id> = <variable initializer>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                                    Symbol.createTerminator(ASSIGN),
                                    Symbol.createNonTerminator(VARIABLE_INITIALIZER)
                            )
                            , null
                    )
            ),
            /*
             * <variable declarator id> →  @identifier
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(IDENTIFIER)
                            )
                            , null
                    )
            ),
            /*
             * <variable declarator id> →  <variable declarator id> []
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                                    Symbol.createTerminator(MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(MIDDLE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),
            /*
             * TODO 删掉这个
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_INITIALIZER),
                            SymbolString.create(
                                    Symbol.EPSILON
                            )
                            , null
                    )
            ),
//            /*
//             * <variable initializer> → <expression>
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(VARIABLE_INITIALIZER),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(EXPRESSION)
//                            )
//                            , null
//                    )
//            ),
//            /*
//             * <variable initializer> → <array initializer>
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(VARIABLE_INITIALIZER),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(ARRAY_INITIALIZER)
//                            )
//                            , null
//                    )
//            ),
            /*
             * <statement> →  <statement without trailing substatement>
             * TODO 可以扩展更为复杂的语法
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT)
                            )
                            , null
                    )
            ),
            /*
             * <statement without trailing substatement> → <block>
             * TODO 可以扩展更为复杂的语法
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            )
                            , null
                    )
            ),
            /*
             * <statement without trailing substatement> → <empty statement>
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EMPTY_STATEMENT)
                            )
                            , null
                    )
            ),
//            /*
//             * <statement without trailing substatement> → <expression statement>
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(EXPRESSION_STATEMENT)
//                            )
//                            , null
//                    )
//            ),
//            /*
//             * <statement without trailing substatement> → <return statement>
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(RETURN_STATEMENT)
//                            )
//                            , null
//                    )
//            ),
            /*
             * <empty statement> → ;
             */
            Production.create(
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EMPTY_STATEMENT),
                            SymbolString.create(
                                    Symbol.createTerminator(SEMICOLON)
                            )
                            , null
                    )
            )
//            /*
//             * <expression statement> → <statement expression> ;
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(EXPRESSION_STATEMENT),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(STATEMENT_EXPRESSION),
//                                    Symbol.createTerminator(SEMICOLON)
//                            )
//                            , null
//                    )
//            ),
//            /*
//             * <statement expression> → <assignment>
//             * TODO 可以扩展更为复杂的语法
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(STATEMENT_EXPRESSION),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(ASSIGNMENT)
//                            )
//                            , null
//                    )
//            ),
//            /*
//             * <assignment> → <left hand side> <assignment operator> <assignment expression>
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(ASSIGNMENT),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(LEFT_HAND_SIDE),
//                                    Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
//                                    Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION)
//                            )
//                            , null
//                    )
//            ),
//            /*
//             * <left hand side> → <expression name>
//             * TODO 可以扩展更为复杂的语法
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(LEFT_HAND_SIDE),
//                            SymbolString.create(
//                                    Symbol.createNonTerminator(EXPRESSION_NAME)
//                            )
//                            , null
//                    )
//            ),
//            /*
//             * <expression name> → @identifier
//             * TODO 可以扩展更为复杂的语法
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(EXPRESSION_NAME),
//                            SymbolString.create(
//                                    Symbol.createRegexTerminator(IDENTIFIER)
//                            )
//                            , null
//                    )
//            ),
//            /*
//             * <assignment operator> → =
//             * TODO 可以扩展更为复杂的语法
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
//                            SymbolString.create(
//                                    Symbol.createRegexTerminator(ASSIGN)
//                            )
//                            , null
//                    )
//            ),
//            /*
//             * <assignment expression> → <expression>
//             * TODO 可以扩展更为复杂的语法
//             */
//            Production.create(
//                    PrimaryProduction.create(
//                            Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION),
//                            SymbolString.create(
//                                    Symbol.createRegexTerminator(EXPRESSION)
//                            )
//                            , null
//                    )
//            )
    );


    public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addNormalMorpheme(Symbol.createTerminator(SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(LARGE_LEFT_PARENTHESES), "{")
            .addNormalMorpheme(Symbol.createTerminator(LARGE_RIGHT_PARENTHESES), "}")
            .addNormalMorpheme(Symbol.createTerminator(COMMA), ",")
            .addRegexMorpheme(Symbol.createRegexTerminator(TYPE), "(void)|(int)|(float)|(char)|(bool)")
            .addRegexMorpheme(Symbol.createRegexTerminator(IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*")
            .build();

    public static void main(String[] args) {
        LRCompiler compiler = LR1.create(LEXICAL_ANALYZER, GRAMMAR);
        System.out.println(compiler.isLegal());
        System.out.println(compiler.compile("void sort(int a){}\n" +
                "\n" +
                "int add(int a,int b){}\n" +
                "\n" +
                "void exchange(int a,int b){\n" +
                "    int a, b[][];\n" +
                "}"));
    }
}
