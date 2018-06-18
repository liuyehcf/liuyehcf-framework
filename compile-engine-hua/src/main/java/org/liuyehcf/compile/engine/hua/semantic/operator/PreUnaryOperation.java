package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.bytecode.cp._iinc;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.model.UnaryOperator;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.model.Type.TYPE_INT;

/**
 * 前置一元运算
 *
 * @author chenlu
 * @date 2018/6/18
 */
public class PreUnaryOperation extends AbstractSemanticAction {

    /**
     * 运算子-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int stackOffset;

    /**
     * 操作符
     */
    private final UnaryOperator operator;

    public PreUnaryOperation(int stackOffset, UnaryOperator operator) {
        this.stackOffset = stackOffset;
        this.operator = operator;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {

        Type type = context.getStack().get(stackOffset).get(AttrName.TYPE.name());
        String identifierName = context.getStack().get(stackOffset).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        switch (operator) {
            case INCREMENT:
                if (!TYPE_INT.equals(type)) {
                    throw new RuntimeException("pre \'" + operator.getSign() + "\' 运算子必须是整型");
                }
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iinc(variableSymbol.getOffset(), 1));
                break;
            case DECREMENT:
                if (!TYPE_INT.equals(type)) {
                    throw new RuntimeException("pre \'" + operator.getSign() + "\' 运算子必须是整型");
                }
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iinc(variableSymbol.getOffset(), -1));
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
