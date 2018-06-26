package org.liuyehcf.compile.engine.hua.definition.semantic.load;

import org.liuyehcf.compile.engine.hua.bytecode.sl._iaload;
import org.liuyehcf.compile.engine.hua.bytecode.sl._iload;
import org.liuyehcf.compile.engine.hua.bytecode.sm._dup2;
import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.core.VariableSymbol;
import org.liuyehcf.compile.engine.hua.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.definition.model.Type;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;
import static org.liuyehcf.compile.engine.hua.definition.Constant.*;
import static org.liuyehcf.compile.engine.hua.definition.GrammarDefinition.NORMAL_ASSIGN;

/**
 * 必要时添加load指令
 *
 * @author hechenfeng
 * @date 2018/6/20
 */
public class VariableLoadIfNecessary extends AbstractSemanticAction implements Serializable {
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

    public VariableLoadIfNecessary(int leftHandStackOffset, int operatorStackOffset) {
        this.leftHandStackOffset = leftHandStackOffset;
        this.operatorStackOffset = operatorStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        String operator = context.getAttr(operatorStackOffset, AttrName.ASSIGN_OPERATOR);
        String identifierName = context.getAttr(leftHandStackOffset, AttrName.IDENTIFIER_NAME);
        Type leftHandType = context.getAttr(leftHandStackOffset, AttrName.TYPE);

        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        if (variableSymbol == null) {
            throw new RuntimeException("非变量不能进行赋值操作");
        }

        Type identifierType = variableSymbol.getType();

        switch (operator) {
            case NORMAL_ASSIGN:
                break;
            case NORMAL_MUL_ASSIGN:
            case NORMAL_DIV_ASSIGN:
            case NORMAL_REM_ASSIGN:
            case NORMAL_ADD_ASSIGN:
            case NORMAL_SUB_ASSIGN:
            case NORMAL_SHL_ASSIGN:
            case NORMAL_SHR_ASSIGN:
            case NORMAL_USHR_ASSIGN:
            case NORMAL_BIT_AND_ASSIGN:
            case NORMAL_BIT_XOR_ASSIGN:
            case NORMAL_BIT_OR_ASSIGN:
                assertFalse(leftHandType.isArrayType());

                /*
                 * 标志符为数组类型
                 */
                if (identifierType.isArrayType()) {

                    context.addByteCodeToCurrentMethod(new _dup2());

                    switch (leftHandType.getTypeName()) {
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _iaload());
                            context.setAttr(leftHandStackOffset, AttrName.TYPE, leftHandType);
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }
                /*
                 * 标志符为非数组类型
                 */
                else {
                    switch (leftHandType.getTypeName()) {
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _iload(variableSymbol.getOrder()));
                            context.setAttr(leftHandStackOffset, AttrName.TYPE, leftHandType);
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                }
                break;
            default:
                throw new RuntimeException("尚不支持赋值运算符 \'" + operator + "\'");
        }
    }
}
