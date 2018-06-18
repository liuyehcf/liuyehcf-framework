package org.liuyehcf.compile.engine.hua.definition;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AddFutureSyntaxNode;
import org.liuyehcf.compile.engine.hua.semantic.attr.*;
import org.liuyehcf.compile.engine.hua.semantic.condition.BooleanAssignment;
import org.liuyehcf.compile.engine.hua.semantic.method.EnterMethod;
import org.liuyehcf.compile.engine.hua.semantic.method.ExitMethod;
import org.liuyehcf.compile.engine.hua.semantic.method.RecordMethodDescription;
import org.liuyehcf.compile.engine.hua.semantic.method.RecordParamTypeInfo;
import org.liuyehcf.compile.engine.hua.semantic.operator.VariableInitialization;
import org.liuyehcf.compile.engine.hua.semantic.variable.CreateVariable;
import org.liuyehcf.compile.engine.hua.semantic.variable.EnterNamespace;
import org.liuyehcf.compile.engine.hua.semantic.variable.ExitNamespace;

import static org.liuyehcf.compile.engine.hua.definition.BlockProductions.BLOCK;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_VOID;
import static org.liuyehcf.compile.engine.hua.definition.ExpressionProductions.EXPRESSION;
import static org.liuyehcf.compile.engine.hua.definition.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.hua.definition.TypeProductions.TYPE;

/**
 * Program相关的产生式
 *
 * @author hechenfeng
 * @date 2018/5/31
 */
abstract class ProgramProductions {

    /**
     * 非终结符
     */
    public static final String PROGRAMS = "<programs>";
    public static final String METHOD_DECLARATIONS = "<method declarations>"; // new
    public static final String EPSILON_OR_FORMAL_PARAMETER_LIST = "<epsilon or formal parameter list>"; // new
    public static final String FORMAL_PARAMETER_LIST = "<formal parameter list>"; // 48
    public static final String FORMAL_PARAMETER = "<formal parameter>"; // 50
    public static final String VARIABLE_DECLARATORS = "<variable declarators>"; // 66
    public static final String VARIABLE_DECLARATOR = "<variable declarator>"; // 68
    public static final String VARIABLE_DECLARATOR_ID = "<variable declarator id>"; // 70
    public static final String VARIABLE_INITIALIZER = "<variable initializer>"; // 72
    public static final String METHOD_DECLARATION = "<method declaration>"; // 74
    public static final String METHOD_HEADER = "<method header>"; // 76
    public static final String RESULT_TYPE = "<result type>"; // 78
    public static final String METHOD_DECLARATOR = "<method declarator>"; // 84
    public static final String METHOD_BODY = "<method body>"; // 86

    private static final String MARK_50_1_1 = "<mark 50_1_1>";
    private static final String MARK_74_1_1 = "<mark 74_1_1>";
    private static final String MARK_66_2_1 = "<mark 66_2_1>";
    public static final Production[] PRODUCTIONS = {
            /*
             * <programs>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <programs> → <method declarations>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PROGRAMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATIONS)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <method declarations>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <method declarations> → <method declarations>  <method declaration>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATIONS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATIONS),
                                    Symbol.createNonTerminator(METHOD_DECLARATION)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <method declarations> → <method declaration>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATIONS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_DECLARATION)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <formal parameter list> 48
             * SAME
             */
            Production.create(
                    /*
                     * <formal parameter list> → <formal parameter list> , <formal parameter>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(FORMAL_PARAMETER)
                            ),
                            new RecordParamTypeInfo(-2, 0),
                            new AttrFilter(AttrName.PARAMETER_LIST)
                    ),
                    /*
                     * <formal parameter list> → <formal parameter>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER)
                            ),
                            new SetAttrFromSystem(0, AttrName.PARAMETER_LIST.name(), null),
                            new RecordParamTypeInfo(0, 0),
                            new AttrFilter(AttrName.PARAMETER_LIST)
                    )
            ),


            /*
             * <formal parameter> 50
             * SAME
             */
            Production.create(
                    /*
                     * (1) <formal parameter> → <type> <mark 50_1_1> <variable declarator id>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FORMAL_PARAMETER),
                            SymbolString.create(
                                    Symbol.createNonTerminator(TYPE),
                                    Symbol.createNonTerminator(MARK_50_1_1),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <mark 50_1_1>
             */
            Production.create(
                    /*
                     * <mark 50_1_1> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_50_1_1),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AddFutureSyntaxNode(1),
                            new AssignAttr(0, 1, AttrName.TYPE.name()),
                            new AttrFilter()
                    )
            ),


            /*
             * <variable declarators> 66
             * SAME
             */
            Production.create(
                    /*
                     * (1) <variable declarators> → <variable declarator>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * (2) <variable declarators> → <variable declarators> , <mark 66_2_1> <variable declarator>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATORS),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(MARK_66_2_1),
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <mark 66_2_1>
             */
            Production.create(
                    /*
                     * <mark 66_2_1> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_66_2_1),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AddFutureSyntaxNode(1),
                            new AssignAttr(-1, 1, AttrName.TYPE.name()),
                            new AttrFilter()
                    )
            ),


            /*
             * <variable declarator> 68
             * SAME
             */
            Production.create(
                    /*
                     * <variable declarator> → <variable declarator id>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * <variable declarator> → <variable declarator id> = <variable initializer>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                                    Symbol.createTerminator(NORMAL_ASSIGN),
                                    Symbol.createNonTerminator(VARIABLE_INITIALIZER)
                            ),
                            new VariableInitialization(0, -2),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <variable declarator id> 70
             * SAME
             */
            Production.create(
                    /*
                     * <variable declarator id> → @identifier
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER)
                            ),
                            new CreateVariable(0),
                            new SetAttrFromLexical(0, AttrName.IDENTIFIER_NAME.name(), 0),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
                    ),
                    /*
                     * <variable declarator id> → <variable declarator id> []
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                            SymbolString.create(
                                    Symbol.createNonTerminator(VARIABLE_DECLARATOR_ID),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
                    )
            ),


            /*
             * <variable initializer> 72
             * LACK
             */
            Production.create(
                    /*
                     * <variable initializer> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(VARIABLE_INITIALIZER),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            ),
                            new BooleanAssignment(0),
                            new AttrFilter(AttrName.TYPE)
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <variable initializer> → <array initializer>
                     */
            ),


            /*
             * <method declaration> 74
             * SAME
             */
            Production.create(
                    /*
                     * <method declaration> → <mark 74_1_1> <method header> <method body>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MARK_74_1_1),
                                    Symbol.createNonTerminator(METHOD_HEADER),
                                    Symbol.createNonTerminator(METHOD_BODY)
                            ),
                            new ExitNamespace(),
                            new ExitMethod(),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark 74_1_1>
             */
            Production.create(
                    /*
                     * <mark 74_1_1> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_74_1_1),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new EnterNamespace(),
                            new EnterMethod(),
                            new AttrFilter()
                    )

            ),


            /*
             * <method header> 76
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <method header> → <result type> <method declarator>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_HEADER),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RESULT_TYPE),
                                    Symbol.createNonTerminator(METHOD_DECLARATOR)
                            ),
                            new RecordMethodDescription(-1, 0),
                            new AttrFilter()
                    )
            ),


            /*
             * <result type> 78
             * SAME
             */
            Production.create(
                    /*
                     * <result type> → <type>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RESULT_TYPE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(TYPE)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * <result type> → void
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RESULT_TYPE),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_VOID)
                            ),
                            new SetAttrFromSystem(0, AttrName.TYPE.name(), Type.TYPE_VOID),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <method declarator> 84
             * SAME
             */
            Production.create(
                    /*
                     * <method declarator> → @identifier ( <formal parameter list>? )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_DECLARATOR),
                            SymbolString.create(
                                    Symbol.createRegexTerminator(REGEX_IDENTIFIER),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_FORMAL_PARAMETER_LIST),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            ),
                            new SetAttrFromLexical(-3, AttrName.METHOD_NAME.name(), -3),
                            new AssignAttr(-1, -3, AttrName.PARAMETER_LIST.name()),
                            new AttrFilter(AttrName.METHOD_NAME, AttrName.PARAMETER_LIST)
                    )
            ),


            /*
             * <epsilon or formal parameter list>
             * DIFFERENCE
             */
            Production.create(
                    /*
                     * (1) <epsilon or formal parameter list> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FORMAL_PARAMETER_LIST),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetAttrToLeftNode(AttrName.PARAMETER_LIST.name(), null),
                            new AttrFilter(AttrName.PARAMETER_LIST)
                    ),
                    /*
                     * (2) <epsilon or formal parameter list> → <formal parameter list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_FORMAL_PARAMETER_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FORMAL_PARAMETER_LIST)
                            ),
                            new AttrFilter(AttrName.PARAMETER_LIST)
                    )
            ),


            /*
             * <method body> 86
             * SAME
             */
            Production.create(
                    /*
                     * <method body> → <block>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_BODY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(BLOCK)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <method body> → ;
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_BODY),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SEMICOLON)
                            ),
                            new AttrFilter()
                    )
            ),
    };
}
