package org.liuyehcf.compile.engine.hua.compile.definition.semantic.statement;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.VariableSymbol;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._aastore;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._iastore;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._istore;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertEquals;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition.NORMAL_ASSIGN;

/**
 * 赋值
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class Assignment extends AbstractSemanticAction implements Serializable {

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
    public void onAction(HuaContext context) {
        String operator = context.getAttr(operatorStackOffset, AttrName.ASSIGN_OPERATOR);
        String identifierName = context.getAttr(leftHandStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        Type expressionType = context.getAttr(expressionStackOffset, AttrName.TYPE);
        Type leftHandType = context.getAttr(leftHandStackOffset, AttrName.TYPE);

        if (!expressionType.equals(leftHandType)) {
            throw new RuntimeException("赋值运算符左右侧类型不匹配");
        }

        Type identifierType = variableSymbol.getType();

        /*
         * 标志符的类型可能是数组类型，但是其类型名必须与赋值运算符左侧一致
         */
        assertEquals(leftHandType.getTypeName(), identifierType.getTypeName());

        /*
         * 当左侧表达式类型是数组
         */
        if (leftHandType.isArrayType()) {
            if (!NORMAL_ASSIGN.equals(operator)) {
                throw new RuntimeException("Compound assignment operator does not support array types");
            }

            context.addByteCodeToCurrentMethod(new _aastore());
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
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _iastore());

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
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _iastore());
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
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _istore(variableSymbol.getOrder()));
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
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _istore(variableSymbol.getOrder()));
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }
            }
        }

        context.setAttrToLeftNode(AttrName.TYPE, leftHandType);
    }

    private void addComputeByteCode(HuaContext context, String typeName, String operator) {
        Compute code;
        switch (operator) {
            case NORMAL_MUL_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _imul();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_DIV_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _idiv();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_REM_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _irem();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_ADD_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _iadd();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_SUB_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _isub();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_SHL_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _ishl();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_SHR_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _ishr();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_USHR_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _iushr();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_BIT_AND_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _iand();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_BIT_XOR_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _ixor();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_BIT_OR_ASSIGN:
                switch (typeName) {
                    case NORMAL_INT:
                        code = new _ior();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            default:
                throw new RuntimeException("Does not support assignment operator '" + operator + "' yet");
        }
        context.addByteCodeToCurrentMethod(code);
    }
}
