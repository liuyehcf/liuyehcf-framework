package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.statement;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.VariableSymbol;
import com.github.liuyehcf.framework.language.hua.core.bytecode.cp.*;
import com.github.liuyehcf.framework.language.hua.core.bytecode.sl.*;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertEquals;
import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.*;
import static com.github.liuyehcf.framework.language.hua.compile.definition.GrammarDefinition.NORMAL_ASSIGN;

/**
 * 赋值
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class Assignment extends AbstractSemanticAction {

    /**
     * 赋值左侧-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int leftHandStackOffset;

    /**
     * 赋值运算符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int operatorStackOffset;

    /**
     * 表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionStackOffset;

    public Assignment(int leftHandStackOffset, int operatorStackOffset, int expressionStackOffset) {
        this.leftHandStackOffset = leftHandStackOffset;
        this.operatorStackOffset = operatorStackOffset;
        this.expressionStackOffset = expressionStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        String operator = context.getAttr(operatorStackOffset, AttrName.ASSIGN_OPERATOR);
        String identifierName = context.getAttr(leftHandStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        Type expressionType = context.getAttr(expressionStackOffset, AttrName.TYPE);
        Type leftHandType = context.getAttr(leftHandStackOffset, AttrName.TYPE);

        assertTrue(Type.isCompatible(leftHandType, expressionType),
                "[SYNTAX_ERROR] - Assign operator left and right types do not match, " +
                        "leftHand type is '" + leftHandType.toTypeDescription() + "', " +
                        "expression type is '" + expressionType.toTypeDescription() + "'");

        Type identifierType = variableSymbol.getType();

        /*
         * 标志符的类型可能是数组类型，但是其类型名必须与赋值运算符左侧一致
         */
        assertEquals(leftHandType.getTypeName(), identifierType.getTypeName(),
                "[SYSTEM_ERROR] - Type name of leftHand expression must be consistent with type name of leftHand identifier");

        /*
         * 当左侧表达式类型是数组
         */
        if (leftHandType.isArrayType()) {
            assertTrue(NORMAL_ASSIGN.equals(operator), "[SYNTAX_ERROR] - Complex assignment cannot support array type");

            /*
             * 当表达式类型与标志符类型的数组维度一致时，说明是对数组本身赋值，需要用astore
             */
            if (leftHandType.getDim() == identifierType.getDim()) {
                context.addByteCodeToCurrentMethod(new _astore(variableSymbol.getOrder()));
            } else {
                context.addByteCodeToCurrentMethod(new _aastore());
            }
        }
        /*
         * 当左侧表达式类型不是数组
         */
        else {
            /*
             * 当标志符是数组
             */
            if (identifierType.isArrayType()) {
                /*
                 * 普通赋值语句
                 */
                if (NORMAL_ASSIGN.equals(operator)) {
                    switch (identifierType.getTypeName()) {
                        case NORMAL_BOOLEAN:
                            context.addByteCodeToCurrentMethod(new _bastore());
                            break;
                        case NORMAL_CHAR:
                            context.addByteCodeToCurrentMethod(new _castore());
                            break;
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _iastore());
                            break;
                        case NORMAL_LONG:
                            context.addByteCodeToCurrentMethod(new _lastore());
                            break;
                        case NORMAL_FLOAT:
                            context.addByteCodeToCurrentMethod(new _fastore());
                            break;
                        case NORMAL_DOUBLE:
                            context.addByteCodeToCurrentMethod(new _dastore());
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }
                /*
                 * 复合赋值语句
                 */
                else {
                    addComputeByteCode(context, identifierType.getTypeName(), operator);

                    switch (identifierType.getTypeName()) {
                        case NORMAL_CHAR:
                            context.addByteCodeToCurrentMethod(new _castore());
                            break;
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _iastore());
                            break;
                        case NORMAL_LONG:
                            context.addByteCodeToCurrentMethod(new _lastore());
                            break;
                        case NORMAL_FLOAT:
                            context.addByteCodeToCurrentMethod(new _fastore());
                            break;
                        case NORMAL_DOUBLE:
                            context.addByteCodeToCurrentMethod(new _dastore());
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }
            }
            /*
             * 当标志符不是数组
             */
            else {
                /*
                 * 普通赋值语句
                 */
                if (NORMAL_ASSIGN.equals(operator)) {
                    switch (identifierType.getTypeName()) {
                        case NORMAL_BOOLEAN:
                        case NORMAL_CHAR:
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _istore(variableSymbol.getOrder()));
                            break;
                        case NORMAL_LONG:
                            context.addByteCodeToCurrentMethod(new _lstore(variableSymbol.getOrder()));
                            break;
                        case NORMAL_FLOAT:
                            context.addByteCodeToCurrentMethod(new _fstore(variableSymbol.getOrder()));
                            break;
                        case NORMAL_DOUBLE:
                            context.addByteCodeToCurrentMethod(new _dstore(variableSymbol.getOrder()));
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }
                /*
                 * 复合赋值语句
                 */
                else {
                    addComputeByteCode(context, identifierType.getTypeName(), operator);

                    switch (identifierType.getTypeName()) {
                        case NORMAL_CHAR:
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _istore(variableSymbol.getOrder()));
                            break;
                        case NORMAL_LONG:
                            context.addByteCodeToCurrentMethod(new _lstore(variableSymbol.getOrder()));
                            break;
                        case NORMAL_FLOAT:
                            context.addByteCodeToCurrentMethod(new _fstore(variableSymbol.getOrder()));
                            break;
                        case NORMAL_DOUBLE:
                            context.addByteCodeToCurrentMethod(new _dstore(variableSymbol.getOrder()));
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }
            }
        }

        context.setAttrToLeftNode(AttrName.TYPE, leftHandType);
    }

    private void addComputeByteCode(CompilerContext context, String typeName, String operator) {
        Compute code;
        switch (operator) {
            case NORMAL_MUL_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _imul();
                        break;
                    case NORMAL_LONG:
                        code = new _lmul();
                        break;
                    case NORMAL_FLOAT:
                        code = new _fmul();
                        break;
                    case NORMAL_DOUBLE:
                        code = new _dmul();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_DIV_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _idiv();
                        break;
                    case NORMAL_LONG:
                        code = new _ldiv();
                        break;
                    case NORMAL_FLOAT:
                        code = new _fdiv();
                        break;
                    case NORMAL_DOUBLE:
                        code = new _ddiv();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_REM_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _irem();
                        break;
                    case NORMAL_LONG:
                        code = new _lrem();
                        break;
                    case NORMAL_FLOAT:
                        code = new _frem();
                        break;
                    case NORMAL_DOUBLE:
                        code = new _drem();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_ADD_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _iadd();
                        break;
                    case NORMAL_LONG:
                        code = new _ladd();
                        break;
                    case NORMAL_FLOAT:
                        code = new _fadd();
                        break;
                    case NORMAL_DOUBLE:
                        code = new _dadd();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_SUB_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _isub();
                        break;
                    case NORMAL_LONG:
                        code = new _lsub();
                        break;
                    case NORMAL_FLOAT:
                        code = new _fsub();
                        break;
                    case NORMAL_DOUBLE:
                        code = new _dsub();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_SHL_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _ishl();
                        break;
                    case NORMAL_LONG:
                        code = new _lshl();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_SHR_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _ishr();
                        break;
                    case NORMAL_LONG:
                        code = new _lshr();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_USHR_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _iushr();
                        break;
                    case NORMAL_LONG:
                        code = new _lushr();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_BIT_AND_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _iand();
                        break;
                    case NORMAL_LONG:
                        code = new _land();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_BIT_XOR_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _ixor();
                        break;
                    case NORMAL_LONG:
                        code = new _lxor();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_BIT_OR_ASSIGN:
                switch (typeName) {
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        code = new _ior();
                        break;
                    case NORMAL_LONG:
                        code = new _lor();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        context.addByteCodeToCurrentMethod(code);
    }
}
