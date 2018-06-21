package org.liuyehcf.compile.engine.hua.semantic.load;

import org.liuyehcf.compile.engine.hua.bytecode.sl._iload;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.definition.Constant.*;
import static org.liuyehcf.compile.engine.hua.definition.GrammarDefinition.NORMAL_ASSIGN;

/**
 * @author chenlu
 * @date 2018/6/20
 */
public class VariableLoadIfNecessary extends AbstractSemanticAction {
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
        String operator = context.getStack().get(operatorStackOffset).get(AttrName.ASSIGN_OPERATOR.name());
        String identifierName = context.getStack().get(leftHandStackOffset).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        if (variableSymbol == null) {
            throw new RuntimeException("非变量不能进行赋值操作");
        }

        Type type = variableSymbol.getType();

        switch (operator) {
            case NORMAL_ASSIGN:
                break;
            case NORMAL_MUL_ASSIGN:
            case NORMAL_DIV_ASSIGN:
            case NORMAL_MOD_ASSIGN:
            case NORMAL_ADD_ASSIGN:
            case NORMAL_MINUS_ASSIGN:
            case NORMAL_SHIFT_LEFT_ASSIGN:
            case NORMAL_SHIFT_RIGHT_ASSIGN:
            case NORMAL_UNSIGNED_SHIFT_RIGHT_ASSIGN:
            case NORMAL_BIT_AND_ASSIGN:
            case NORMAL_BIT_EXCLUSIVE_OR_ASSIGN:
            case NORMAL_BIT_OR_ASSIGN:
                switch (type.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                        context.getStack().get(leftHandStackOffset).put(AttrName.TYPE.name(), type);
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            default:
                throw new RuntimeException("尚不支持赋值运算符 \'" + operator + "\'");
        }
    }
}
