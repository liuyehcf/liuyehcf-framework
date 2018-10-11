package org.liuyehcf.compile.engine.expression.compile.definition;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;
import org.liuyehcf.compile.engine.expression.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.expression.compile.definition.model.BackFillType;
import org.liuyehcf.compile.engine.expression.compile.definition.model.CompareOperatorType;
import org.liuyehcf.compile.engine.expression.compile.definition.model.ControlTransferType;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.attr.AssignAttrsToLeftNode;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.attr.AttrFilter;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.attr.SetAttrToLeftNode;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.attr.SetControlTransferTypeIfNecessary;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.backfill.ControlTransferByteCodeBackFill;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.code.*;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.function.FunctionInvocation;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.function.IncreaseArgumentSize;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.function.InitArgumentSizeIfNecesssary;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.statement.BooleanExpressionEnding;

import static org.liuyehcf.compile.engine.expression.compile.definition.Constant.*;
import static org.liuyehcf.compile.engine.expression.compile.definition.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.expression.compile.definition.ProgramProductions.ARRAY_INITIALIZER;
import static org.liuyehcf.compile.engine.expression.compile.definition.TokenProductions.*;
import static org.liuyehcf.compile.engine.expression.compile.definition.semantic.AbstractSemanticAction.NOT_NULL;

/**
 * Expression相关的产生式
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
abstract class ExpressionProductions {

    public static final String EXPRESSION = "<expression>"; // 218
    public static final String CONDITIONAL_EXPRESSION = "<conditional expression>"; // 228
    public static final String CONDITIONAL_OR_EXPRESSION = "<conditional or expression>"; // 230
    public static final String CONDITIONAL_AND_EXPRESSION = "<conditional and expression>"; // 232
    public static final String INCLUSIVE_OR_EXPRESSION = "<inclusive or expression>"; // 234
    public static final String EXCLUSIVE_OR_EXPRESSION = "<exclusive or expression>"; // 236
    public static final String AND_EXPRESSION = "<and expression>"; // 238
    public static final String EQUALITY_EXPRESSION = "<equality expression>"; // 240
    public static final String RELATIONAL_EXPRESSION = "<relational expression>"; // 242
    public static final String SHIFT_EXPRESSION = "<shift expression>"; // 244
    public static final String ADDITIVE_EXPRESSION = "<additive expression>"; // 246
    public static final String MULTIPLICATIVE_EXPRESSION = "<multiplicative expression>"; // 248
    public static final String UNARY_EXPRESSION = "<unary expression>"; // 252
    public static final String UNARY_EXPRESSION_NOT_PLUS_MINUS = "<unary expression not plus minus>"; // 258
    public static final String POSTFIX_EXPRESSION = "<postfix expression>"; // 264
    public static final String METHOD_INVOCATION = "<method invocation>"; // 266
    public static final String PRIMARY = "<primary>"; // 270
    public static final String PRIMARY_NO_NEW_ARRAY = "<primary no new array>"; // 272
    public static final String EPSILON_OR_ARGUMENT_LIST = "<epsilon or argument list>"; // new
    public static final String ARGUMENT_LIST = "<argument list>"; // 276
    public static final String ARRAY_ACCESS = "<array access>"; // 286


    public static final String MARK_TRUE_BLOCK = "<mark true block>";
    public static final String MARK_FALSE_BLOCK = "<mark false block>";
    private static final String MARK_230_2_1 = "<mark 230_2_1>";
    private static final String MARK_232_2_1 = "<mark 232_2_1>";
    private static final String MARK_286_1_1 = "<mark 286_1_1>";

    public static final Production[] PRODUCTIONS = {

            /*
             * <mark true block>
             */
            Production.create(
                    /*
                     * <mark true block> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetControlTransferTypeIfNecessary(-1),
                            new PushControlTransferByteCodeByType(-1, -1, BackFillType.FALSE, false),
                            new ControlTransferByteCodeBackFill(-1, BackFillType.TRUE),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark false block>
             */
            Production.create(
                    /*
                     * <mark false block> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_FALSE_BLOCK),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new BooleanExpressionEnding(-1),
                            new PushControlTransferByteCode(-4, ControlTransferType.GOTO, BackFillType.NEXT),
                            new ControlTransferByteCodeBackFill(-4, BackFillType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
             * <expression> 218
             */
            Production.create(
                    /*
                     * <expression> → <conditional expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    )
            ),


            /*
             * <conditional expression> 228
             */
            Production.create(
                    /*
                     * <conditional expression> → <conditional or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <conditional expression> → <conditional or expression> ? <mark true block> <expression> : <mark false block> <conditional expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_QUESTION_MARK),
                                    Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_COLON),
                                    Symbol.createNonTerminator(MARK_FALSE_BLOCK),
                                    Symbol.createNonTerminator(CONDITIONAL_EXPRESSION)
                            ),
                            new BooleanExpressionEnding(0),
                            new ControlTransferByteCodeBackFill(-6, BackFillType.NEXT),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE)
                    )
            ),


            /*
             * <conditional or expression> 230
             */
            Production.create(
                    /*
                     * (1) <conditional or expression> → <conditional and expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * (2) <conditional or expression> → <conditional or expression> || <mark 230_2_1> <conditional and expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LOGICAL_OR),
                                    Symbol.createNonTerminator(MARK_230_2_1),
                                    Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION)
                            ),
                            new SetControlTransferTypeIfNecessary(0),
                            new AssignAttrsToLeftNode(0, AttrName.CONTROL_TRANSFER_TYPE),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new MergeControlTransferByteCode(0, -3),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    )
            ),


            /*
             * <mark 230_2_1>
             */
            Production.create(
                    /*
                     * <mark 230_2_1> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_230_2_1),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetControlTransferTypeIfNecessary(-1),
                            new PushControlTransferByteCodeByType(-1, -1, BackFillType.TRUE, true),
                            new ControlTransferByteCodeBackFill(-1, BackFillType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
             * <conditional and expression> 232
             */
            Production.create(
                    /*
                     * <conditional and expression> → <inclusive or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <conditional and expression> → <conditional and expression> && <mark 232_2_1> <inclusive or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_AND_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LOGICAL_AND),
                                    Symbol.createNonTerminator(MARK_232_2_1),
                                    Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION)
                            ),
                            new SetControlTransferTypeIfNecessary(0),
                            new AssignAttrsToLeftNode(0, AttrName.CONTROL_TRANSFER_TYPE),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new MergeControlTransferByteCode(0, -3),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    )
            ),


            /*
             * <mark 232_2_1>
             */
            Production.create(
                    /*
                     * <mark 232_2_1> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_232_2_1),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetControlTransferTypeIfNecessary(-1),
                            new PushControlTransferByteCodeByType(-1, -1, BackFillType.FALSE, false),
                            new ControlTransferByteCodeBackFill(-1, BackFillType.TRUE),
                            new AttrFilter()
                    )
            ),


            /*
             * <inclusive or expression> 234
             */
            Production.create(
                    /*
                     * <inclusive or expression> → <exclusive or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <inclusive or expression> → <inclusive or expression> | <exclusive or expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INCLUSIVE_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_BIT_OR),
                                    Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    )
            ),


            /*
             * <exclusive or expression> 236
             */
            Production.create(
                    /*
                     * <exclusive or expression> → <and expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(AND_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <exclusive or expression> → <exclusive or expression> ^ <and expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXCLUSIVE_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_BIT_XOR),
                                    Symbol.createNonTerminator(AND_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    )
            ),


            /*
             * <and expression> 238
             */
            Production.create(
                    /*
                     * <and expression> → <equality expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(AND_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EQUALITY_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <and expression> → <and expression> & <equality expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(AND_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(AND_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_BIT_AND),
                                    Symbol.createNonTerminator(EQUALITY_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    )
            ),


            /*
             * <equality expression> 240
             */
            Production.create(
                    /*
                     * <equality expression> → <relational expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <equality expression> → <equality expression> == <relational expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_EQ),
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION)
                            ),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new PushCompareTransferByteCode(CompareOperatorType.EQUAL),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
                    ),
                    /*
                     * <equality expression> → <equality expression> != <relational expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EQUALITY_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_NE),
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION)
                            ),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new PushCompareTransferByteCode(CompareOperatorType.NOT_EQUAL),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
                    )
            ),


            /*
             * <relational expression> 242
             */
            Production.create(
                    /*
                     * <relational expression> → <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <relational expression> → <relational expression> < <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LT),
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            ),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new PushCompareTransferByteCode(CompareOperatorType.LESS_THAN),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
                    ),
                    /*
                     * <relational expression> → <relational expression> > <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_GT),
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            ),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new PushCompareTransferByteCode(CompareOperatorType.LARGE_THEN),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
                    ),
                    /*
                     * <relational expression> → <relational expression> <= <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_LE),
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            ),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new PushCompareTransferByteCode(CompareOperatorType.LESS_EQUAL),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
                    ),
                    /*
                     * <relational expression> → <relational expression> >= <shift expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(RELATIONAL_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_GE),
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION)
                            ),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new PushCompareTransferByteCode(CompareOperatorType.LARGE_EQUAL),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
                    )
            ),


            /*
             * <shift expression> 244
             */
            Production.create(
                    /*
                     * <shift expression> → <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SHIFT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <shift expression> → <shift expression> << <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SHIFT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SHL),
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    ),
                    /*
                     * <shift expression> → <shift expression> >> <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SHIFT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_SHR),
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    ),
                    /*
                     * <shift expression> → <shift expression> >>> <additive expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(SHIFT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(SHIFT_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_USHR),
                                    Symbol.createNonTerminator(ADDITIVE_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    )
            ),


            /*
             * <additive expression> 246
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
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
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
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    )
            ),


            /*
             * <multiplicative expression> 248
             */
            Production.create(
                    /*
                     * <multiplicative expression> → <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <multiplicative expression> → <multiplicative expression> * <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MUL),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    ),
                    /*
                     * <multiplicative expression> → <multiplicative expression> / <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_DIV),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    ),
                    /*
                     * <multiplicative expression> → <multiplicative expression> % <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(MULTIPLICATIVE_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_REM),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new PushBinaryComputeByteCode(-1),
                            new AttrFilter()
                    )
            ),


            /*
             * <unary expression> 252
             */
            Production.create(
                    /*
                     * <unary expression> → + <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_ADD),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new PushUnaryComputeByteCode(-1, 0),
                            new AttrFilter()
                    ),
                    /*
                     * <unary expression> → - <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SUB),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new PushUnaryComputeByteCode(-1, 0),
                            new AttrFilter()
                    ),
                    /*
                     * <unary expression> → <unary expression not plus minus>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS)
                            ),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    )
            ),


            /*
             * <unary expression not plus minus> 258
             */
            Production.create(
                    /*
                     * <unary expression not plus minus> → <postfix expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTFIX_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <unary expression not plus minus> → ~ <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_REVERSED),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new PushUnaryComputeByteCode(-1, 0),
                            new AttrFilter()
                    ),
                    /*
                     * <unary expression not plus minus> → ! <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_LOGICAL_NOT),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new AssignAttrsToLeftNode(0, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION),
                            new PushUnaryComputeByteCode(-1, 0),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    )
            ),


            /*
             * <postfix expression> 264
             */
            Production.create(
                    /*
                     * (1) <postfix expression> → <primary>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMARY)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * (2) <postfix expression> → <expression name>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_NAME)
                            ),
                            new PushPropertyLoadByteCode(0),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.CONTROL_TRANSFER_TYPE)
                    )
            ),


            /*
             * <method invocation> 266
             */
            Production.create(
                    /*
                     * <method invocation> → <method name> ( <argument list>? )
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(METHOD_INVOCATION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_NAME),
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES)
                            ),
                            new InitArgumentSizeIfNecesssary(-1, 0),
                            new FunctionInvocation(-3, -1),
                            new AttrFilter()
                    )
            ),


            /*
             * <epsilon or argument list>
             */
            Production.create(
                    /*
                     * <epsilon or argument list> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <epsilon or argument list> → <argument list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT_LIST)
                            ),
                            new AttrFilter(AttrName.ARGUMENT_SIZE)
                    )
            ),


            /*
             * <primary> 270
             */
            Production.create(
                    /*
                     * <primary> → <primary no new array>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY)
                            ),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    )
            ),


            /*
             * <primary no new array> 272
             */
            Production.create(
                    /*
                     * <primary no new array> → <literal>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LITERAL)
                            ),
                            new AttrFilter()
                    ),
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
                            new AssignAttrsToLeftNode(-1, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE),
                            new AttrFilter(AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <primary no new array> → <method invocation>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_INVOCATION)
                            ),
                            new AttrFilter()
                    ),
                    /*
                     * <primary no new array> → <array access>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_ACCESS)
                            ),
                            new AttrFilter(AttrName.IDENTIFIER_NAME)
                    ),
                    /*
                     * <expression> → <array initializer>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_INITIALIZER)
                            ),
                            new AttrFilter()
                    )
            ),


            /*
             * <argument list> 276
             */
            Production.create(
                    /*
                     * <argument list> → <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION)
                            ),
                            new BooleanExpressionEnding(0),
                            new InitArgumentSizeIfNecesssary(0, 1),
                            new AttrFilter(AttrName.ARGUMENT_SIZE)
                    ),
                    /*
                     * <argument list> → <argument list> , <expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT_LIST),
                                    Symbol.createTerminator(NORMAL_COMMA),
                                    Symbol.createNonTerminator(EXPRESSION)
                            ),
                            new BooleanExpressionEnding(0),
                            new IncreaseArgumentSize(-2),
                            new AttrFilter(AttrName.ARGUMENT_SIZE)
                    )
            ),


            /*
             * <array access> 286
             */
            Production.create(
                    /*
                     * (1) <array access> → <expression name> <mark 286_1_1> [ <expression> ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_ACCESS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_NAME),
                                    Symbol.createNonTerminator(MARK_286_1_1),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new BooleanExpressionEnding(-1),
                            new PushArrayItemLoadByteCode(),
                            new AttrFilter(AttrName.IDENTIFIER_NAME)
                    ),
                    /*
                     * (2) <array access> → <primary no new array> [ <expression> ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_ACCESS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new BooleanExpressionEnding(-1),
                            new AttrFilter()
                    )
            ),


            /*
             * <mark 286_1_1>
             */
            Production.create(
                    /*
                     * <mark 286_1_1> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_286_1_1),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new PushPropertyLoadByteCode(0),
                            new AttrFilter()
                    )
            )
    };
}
