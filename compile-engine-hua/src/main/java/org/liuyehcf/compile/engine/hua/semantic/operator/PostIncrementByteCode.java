package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.bytecode.cp._iinc;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.model.Type.TYPE_INT;

/**
 * 后置 递增/递减
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public class PostIncrementByteCode extends AbstractSemanticAction {
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

    public PostIncrementByteCode(int expressionStackOffset, int increment) {
        this.expressionStackOffset = expressionStackOffset;
        this.increment = increment;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String identifierName = context.getStack().get(expressionStackOffset).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        if (variableSymbol == null) {
            throw new RuntimeException("前置递增/递减运算符不能作用于 '值'");
        }

        if (!TYPE_INT.equals(variableSymbol.getType())) {
            throw new RuntimeException("前置递增/递减运算符不能作用于 '非int类型的变量'");
        }

        _iinc code = new _iinc();

        code.setOffset(variableSymbol.getOffset());
        code.setIncrement(increment);

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code);
    }
}
