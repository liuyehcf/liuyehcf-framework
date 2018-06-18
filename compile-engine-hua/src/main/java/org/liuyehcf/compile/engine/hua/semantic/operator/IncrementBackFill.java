package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.bytecode.cp._iinc;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 自增字节码回填
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public class IncrementBackFill extends AbstractSemanticAction {
    /**
     * 用于存放回填iinc字节码的节点-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    /**
     * 表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionStackOffset;

    /**
     * 增量
     */
    private final int increment;

    public IncrementBackFill(int backFillStackOffset, int expressionStackOffset, int increment) {
        this.backFillStackOffset = backFillStackOffset;
        this.expressionStackOffset = expressionStackOffset;
        this.increment = increment;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        _iinc code = context.getStack().get(backFillStackOffset).get(AttrName.IINC_BYTE_CODE.name());

        String identifierName = context.getStack().get(expressionStackOffset).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        if (variableSymbol == null) {
            throw new RuntimeException("前置递增/递减运算符不能作用于 '值'");
        }

        if (!Type.TYPE_INT.equals(variableSymbol.getType())) {
            throw new RuntimeException("前置递增/递减运算符不能作用于 '非int类型的变量'");
        }

        code.setOffset(variableSymbol.getOffset());
        code.setIncrement(increment);

        context.getStack().get(backFillStackOffset).put(AttrName.IINC_BYTE_CODE.name(), null);
    }
}
