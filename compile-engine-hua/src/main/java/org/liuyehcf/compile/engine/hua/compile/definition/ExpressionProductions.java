package org.liuyehcf.compile.engine.hua.compile.definition;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;
import org.liuyehcf.compile.engine.hua.compile.definition.model.*;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.attr.*;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.backfill.ControlTransferByteCodeBackFill;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.backfill.IncrementBackFill;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.code.*;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.expression.ConditionalExpression;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.load.ArrayLoad;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.load.VariableLoad;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.load.VariableLoadIfNecessary;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.method.MethodInvocation;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.method.RecordArgumentType;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.statement.Assignment;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.statement.BooleanExpressionEnding;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.statement.SizeofStatement;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable.ArrayTypeDimDecrease;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable.CheckExpressionDimType;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable.NewPrimaryArray;

import static org.liuyehcf.compile.engine.hua.compile.definition.BlockProductions.MARK_TRUE_BLOCK;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.TokenProductions.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.TypeProductions.PRIMITIVE_TYPE;
import static org.liuyehcf.compile.engine.hua.compile.definition.TypeProductions.REFERENCE_TYPE;
import static org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction.NOT_NULL;

/**
 * Expression相关的产生式
 *
 * @author hechenfeng
 * @date 2018/5/31
 */
abstract class ExpressionProductions {

    public static final String EXPRESSION = "<expression>"; // 218
    public static final String ASSIGNMENT_EXPRESSION = "<assignment expression>"; // 220
    public static final String ASSIGNMENT = "<assignment>"; // 222
    public static final String LEFT_HAND_SIDE = "<left hand side>"; // 224
    public static final String ASSIGNMENT_OPERATOR = "<assignment operator>"; // 226
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
    public static final String CAST_EXPRESSION = "<cast expression>"; // 250
    public static final String UNARY_EXPRESSION = "<unary expression>"; // 252
    public static final String PREDECREMENT_EXPRESSION = "<predecrement expression>"; // 254
    public static final String PREINCREMENT_EXPRESSION = "<preincrement expression>"; // 256
    public static final String UNARY_EXPRESSION_NOT_PLUS_MINUS = "<unary expression not plus minus>"; // 258
    public static final String POSTDECREMENT_EXPRESSION = "<postdecrement expression>"; // 260
    public static final String POSTINCREMENT_EXPRESSION = "<postincrement expression>"; // 262
    public static final String POSTFIX_EXPRESSION = "<postfix expression>"; // 264
    public static final String METHOD_INVOCATION = "<method invocation>"; // 266
    public static final String PRIMARY = "<primary>"; // 270
    public static final String PRIMARY_NO_NEW_ARRAY = "<primary no new array>"; // 272
    public static final String EPSILON_OR_ARGUMENT_LIST = "<epsilon or argument list>"; // new
    public static final String ARGUMENT_LIST = "<argument list>"; // 276
    public static final String ARRAY_CREATION_EXPRESSION = "<array creation expression>"; // 278
    public static final String DIM_EXPRS = "<dim exprs>"; // 280
    public static final String DIM_EXPR = "<dim expr>"; // 282
    public static final String EPSILON_OR_DIMS = "<epsilon or dims>"; // new
    public static final String DIMS = "<dims>"; // 284
    public static final String ARRAY_ACCESS = "<array access>"; // 286

    private static final String MARK_222_1_1 = "<mark 222_1_1>";
    private static final String MARK_230_2_1 = "<mark 230_2_1>";
    private static final String MARK_232_2_1 = "<mark 232_2_1>";
    private static final String MARK_PREFIX_EXPRESSION = "<mark prefix expression>";
    private static final String MARK_CONDITIONAL_FALSE_BLOCK = "<mark conditional false block>";
    private static final String MARK_286_1_1 = "<mark 286_1_1>";

    public static final Production[] PRODUCTIONS = {
            /*
             * <expression> 218
             * SAME
             */
            Production.create(
                    /*
                     * <expression> → <assignment expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    )
            ),


            /*
             * <assignment expression> 220
             * SAME
             */
            Production.create(
                    /*
                     * <assignment expression> → <conditional expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <assignment expression> → <assignment>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ASSIGNMENT)
                            ),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
                    )

            ),


            /*
             * <assignment> 222
             * SAME
             */
            Production.create(
                    /*
                     * <assignment> → <left hand side> <assignment operator> <mark 222_1_1> <assignment expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT),
                            SymbolString.create(
                                    Symbol.createNonTerminator(LEFT_HAND_SIDE),
                                    Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                                    Symbol.createNonTerminator(MARK_222_1_1),
                                    Symbol.createNonTerminator(ASSIGNMENT_EXPRESSION)
                            ),
                            new BooleanExpressionEnding(0),
                            new Assignment(-3, -2, 0),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
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
                            new VariableLoadIfNecessary(-1, 0),
                            new AttrFilter()
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
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
                    ),
                    /*
                     * <left hand side> → <array access>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(LEFT_HAND_SIDE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_ACCESS)
                            ),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE) // TODO 现在数组赋值只支持变量，因此需要IDENTIFIER_NAME属性
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <left hand side> → <field access>
                     */
            ),


            /*
             * <assignment operator> 226
             * SAME
             */
            Production.create(
                    /*
                     * <assignment operator> → =
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → *=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MUL_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_MUL_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → \=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DIV_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_DIV_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → %=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_REM_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_REM_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → +=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_ADD_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_ADD_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → -=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SUB_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_SUB_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → <<=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SHL_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_SHL_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → >>=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SHR_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_SHR_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → >>>=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_USHR_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_USHR_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → &=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_AND_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_BIT_AND_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → ^=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_XOR_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_BIT_XOR_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    ),
                    /*
                     * <assignment operator> → |=
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ASSIGNMENT_OPERATOR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BIT_OR_ASSIGN)
                            ),
                            new SetAttrToLeftNode(AttrName.ASSIGN_OPERATOR, NORMAL_BIT_OR_ASSIGN),
                            new AttrFilter(AttrName.ASSIGN_OPERATOR)
                    )
            ),


            /*
             * <conditional expression> 228
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <conditional expression> → <conditional or expression> ? <mark true block> <expression> : <mark conditional false block> <conditional expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CONDITIONAL_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CONDITIONAL_OR_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_QUESTION_MARK),
                                    Symbol.createNonTerminator(MARK_TRUE_BLOCK),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_COLON),
                                    Symbol.createNonTerminator(MARK_CONDITIONAL_FALSE_BLOCK),
                                    Symbol.createNonTerminator(CONDITIONAL_EXPRESSION)
                            ),
                            new ControlTransferByteCodeBackFill(-6, BackFillType.NEXT),
                            new ConditionalExpression(-3, 0),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <mark conditional false block>
             */
            Production.create(
                    /*
                     * <mark conditional false block> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_CONDITIONAL_FALSE_BLOCK),
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
             * <conditional or expression> 230
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new AssignAttrsToLeftNode(0, AttrName.CONTROL_TRANSFER_TYPE),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new MergeControlTransferByteCode(0, -3),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushControlTransferByteCodeByType(-1, -1, BackFillType.TRUE, true),
                            new ControlTransferByteCodeBackFill(-1, BackFillType.FALSE),
                            new AttrFilter()
                    )
            ),


            /*
             * <conditional and expression> 232
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new AssignAttrsToLeftNode(0, AttrName.CONTROL_TRANSFER_TYPE),
                            new SetAttrToLeftNode(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, NOT_NULL),
                            new MergeControlTransferByteCode(0, -3),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushControlTransferByteCodeByType(-1, -1, BackFillType.FALSE, false),
                            new ControlTransferByteCodeBackFill(-1, BackFillType.TRUE),
                            new AttrFilter()
                    )
            ),


            /*
             * <inclusive or expression> 234
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <exclusive or expression> 236
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <and expression> 238
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <equality expression> 240
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushCompareTransferByteCode(-2, CompareOperatorType.EQUAL, 0),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
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
                            new PushCompareTransferByteCode(-2, CompareOperatorType.NOT_EQUAL, 0),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
                    )
            ),


            /*
             * <relational expression> 242
             * LACK
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushCompareTransferByteCode(-2, CompareOperatorType.LESS_THAN, 0),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
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
                            new PushCompareTransferByteCode(-2, CompareOperatorType.LARGE_THEN, 0),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
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
                            new PushCompareTransferByteCode(-2, CompareOperatorType.LESS_EQUAL, 0),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
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
                            new PushCompareTransferByteCode(-2, CompareOperatorType.LARGE_EQUAL, 0),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION)
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <relational expression> → <relational expression> instanceof <reference type>
                     */
            ),


            /*
             * <shift expression> 244
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <additive expression> 246
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <multiplicative expression> 248
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
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
                            new PushBinaryComputeByteCode(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <cast expression> 250
             * SAME
             */
            Production.create(
                    /*
                     * <cast expression> → ( <primitive type> ) <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CAST_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(PRIMITIVE_TYPE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new AttrFilter() // TODO 尚不支持
                    ),
                    /*
                     * <cast expression> → ( <reference type> ) <unary expression not plus minus>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(CAST_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(REFERENCE_TYPE),
                                    Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS)
                            ),
                            new AttrFilter() // TODO 尚不支持
                    )
            ),


            /*
             * <unary expression> 252
             * SAME
             */
            Production.create(
                    /*
                     * <unary expression> → <preincrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PREINCREMENT_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * <unary expression> → <predecrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PREDECREMENT_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    ),
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
                            new AttrFilter(AttrName.TYPE)
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
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * <unary expression> → <unary expression not plus minus>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS)
                            ),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    )
            ),


            /*
             * <mark prefix expression>
             */
            Production.create(
                    /*
                     * <mark prefix expression> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(MARK_PREFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetIINCAttr(),
                            new AttrFilter(AttrName.IINC_BYTE_CODE)
                    )
            ),


            /*
             * <predecrement expression> 254
             * SAME
             */
            Production.create(
                    /*
                     * <predecrement expression> → -- <mark prefix expression> <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PREDECREMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DOUBLE_MINUS),
                                    Symbol.createNonTerminator(MARK_PREFIX_EXPRESSION),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new IncrementBackFill(-1, 0, -1),
                            new SetAttrToLeftNode(AttrName.TYPE, Type.TYPE_INT),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <preincrement expression> 256
             * SAME
             */
            Production.create(
                    /*
                     * <preincrement expression> → ++ <mark prefix expression> <unary expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PREINCREMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_DOUBLE_PLUS),
                                    Symbol.createNonTerminator(MARK_PREFIX_EXPRESSION),
                                    Symbol.createNonTerminator(UNARY_EXPRESSION)
                            ),
                            new IncrementBackFill(-1, 0, 1),
                            new SetAttrToLeftNode(AttrName.TYPE, Type.TYPE_INT),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <unary expression not plus minus> 258
             * SAME
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
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
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
                            new AttrFilter(AttrName.TYPE)
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <unary expression not plus minus> → <cast expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(UNARY_EXPRESSION_NOT_PLUS_MINUS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(CAST_EXPRESSION)
                            ),
                            new AttrFilter() // TODO 尚不支持
                    )
            ),


            /*
             * <postdecrement expression> 260
             * SAME
             */
            Production.create(
                    /*
                     * (1) <postdecrement expression> → <postfix expression> --
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTDECREMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_DOUBLE_MINUS)
                            ),
                            new PushPostIINCByteCode(-1, -1),
                            new SetAttrToLeftNode(AttrName.TYPE, Type.TYPE_INT),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <postincrement expression> 262
             * SAME
             */
            Production.create(
                    /*
                     * (1) <postincrement expression> → <postfix expression> ++
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTINCREMENT_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                                    Symbol.createTerminator(NORMAL_DOUBLE_PLUS)
                            ),
                            new PushPostIINCByteCode(-1, 1),
                            new SetAttrToLeftNode(AttrName.TYPE, Type.TYPE_INT),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <postfix expression> 264
             * DIFFERENT
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * (2) <postfix expression> → <expression name>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(EXPRESSION_NAME)
                            ),
                            new SetBooleanAttrIfNecessary(0),
                            new VariableLoad(0),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE)
                    ),
                    /*
                     * (3) <postfix expression> → <postincrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTINCREMENT_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * (4) <postfix expression> → <postdecrement expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createNonTerminator(POSTDECREMENT_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * (5) <postfix expression> → sizeof <expression name>
                     * NEW
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(POSTFIX_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_SIZEOF),
                                    Symbol.createNonTerminator(EXPRESSION_NAME)
                            ),
                            new SizeofStatement(0),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <method invocation> 266
             * LACK
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
                            new MethodInvocation(-3, -1),
                            new AttrFilter(AttrName.TYPE)
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <method invocation> → <primary> . <identifier> ( <argument list>? )
                     * <method invocation> → super . <identifier> ( <argument list>? )
                     */
            ),


            /*
             * <epsilon or argument list>
             * DIFFERENT
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
                            new AttrFilter(AttrName.ARGUMENT_TYPE_LIST)
                    ),
                    /*
                     * <epsilon or argument list> → <argument list>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_ARGUMENT_LIST),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARGUMENT_LIST)
                            ),
                            new AttrFilter(AttrName.ARGUMENT_TYPE_LIST)
                    )
            ),


            /*
             * <primary> 270
             * SAME
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
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <primary> → <array creation expression>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_CREATION_EXPRESSION)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <primary no new array> 272
             * LACK
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
                            new AttrFilter(AttrName.TYPE)
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
                            new AssignAttrsToLeftNode(-1, AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE),
                            new AttrFilter(AttrName.TYPE, AttrName.CONTROL_TRANSFER_TYPE, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, AttrName.TRUE_BYTE_CODE, AttrName.FALSE_BYTE_CODE)
                    ),
                    /*
                     * <primary no new array> → <method invocation>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(METHOD_INVOCATION)
                            ),
                            new AttrFilter(AttrName.TYPE)
                    ),
                    /*
                     * <primary no new array> → <array access>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_ACCESS)
                            ),
                            new ArrayLoad(0),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <primary no new array> → this
                     * <primary no new array> → <class instance creation expression>
                     * <primary no new array> → <field access>
                     */
            ),


            /*
             * <argument list> 276
             * SAME
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
                            new RecordArgumentType(0, 0),
                            new AttrFilter(AttrName.ARGUMENT_TYPE_LIST)
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
                            new RecordArgumentType(-2, 0),
                            new AttrFilter(AttrName.ARGUMENT_TYPE_LIST)
                    )
            ),


            /*
             * <array creation expression> 278
             * LACK
             */
            Production.create(
                    /*
                     * <array creation expression> → new <primitive type> <dim exprs> <dims>?
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_CREATION_EXPRESSION),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_NEW),
                                    Symbol.createNonTerminator(PRIMITIVE_TYPE),
                                    Symbol.createNonTerminator(DIM_EXPRS),
                                    Symbol.createNonTerminator(EPSILON_OR_DIMS)
                            ),
                            new NewPrimaryArray(-2, -1, 0),
                            new AttrFilter(AttrName.TYPE)
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <array creation expression> → new <class or interface type> <dim exprs> <dims>?
                     */
            ),


            /*
             * <epsilon or dims>
             * DIFFERENT
             */
            Production.create(
                    /*
                     * <epsilon or dims> → ε
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_DIMS),
                            SymbolString.create(
                                    Symbol.EPSILON
                            ),
                            new SetAttrToLeftNode(AttrName.EMPTY_DIM_SIZE, 0),
                            new AttrFilter(AttrName.EMPTY_DIM_SIZE)
                    ),
                    /*
                     * <epsilon or dims> → <dims>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(EPSILON_OR_DIMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIMS)
                            ),
                            new AttrFilter(AttrName.EMPTY_DIM_SIZE)
                    )
            ),


            /*
             * <dim exprs> 280
             * SAME
             */
            Production.create(
                    /*
                     * <dim exprs> → <dim expr>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIM_EXPRS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIM_EXPR)
                            ),
                            new CheckExpressionDimType(0),
                            new SetAttrToLeftNode(AttrName.EXPRESSION_DIM_SIZE, 1),
                            new AttrFilter(AttrName.EXPRESSION_DIM_SIZE)
                    ),
                    /*
                     * <dim exprs> → <dim exprs> <dim expr>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIM_EXPRS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIM_EXPRS),
                                    Symbol.createNonTerminator(DIM_EXPR)
                            ),
                            new CheckExpressionDimType(0),
                            new IncreaseIntAttr(-1, AttrName.EXPRESSION_DIM_SIZE),
                            new AttrFilter(AttrName.EXPRESSION_DIM_SIZE)
                    )
            ),


            /*
             * <dim expr> 282
             * SAME
             */
            Production.create(
                    /*
                     * <dim expr> → [ <expression> ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIM_EXPR),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new AssignAttrsToLeftNode(-1, AttrName.TYPE),
                            new AttrFilter(AttrName.TYPE)
                    )
            ),


            /*
             * <dims> 284
             * SAME
             */
            Production.create(
                    /*
                     * <dims> → [ ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIMS),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new SetAttrToLeftNode(AttrName.EMPTY_DIM_SIZE, 1),
                            new AttrFilter(AttrName.EMPTY_DIM_SIZE)
                    ),
                    /*
                     * <dims> → <dims> [ ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(DIMS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(DIMS),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new IncreaseIntAttr(-2, AttrName.EMPTY_DIM_SIZE),
                            new AttrFilter(AttrName.EMPTY_DIM_SIZE)
                    )
            ),


            /*
             * <array access> 286
             * SAME
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
                            new ArrayTypeDimDecrease(-4),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
                    ),
                    /*
                     * (2) <array access> → <primary no new array> [ <expression> ]
                     * 这里保留AttrName.IDENTIFIER_NAME是由于形如  (a)[1] 这样的语法
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_ACCESS),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMARY_NO_NEW_ARRAY),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createNonTerminator(EXPRESSION),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            ),
                            new ArrayTypeDimDecrease(-3),
                            new AttrFilter(AttrName.IDENTIFIER_NAME, AttrName.TYPE)
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
                            new VariableLoad(0),
                            new AttrFilter()
                    )
            )
    };
}
