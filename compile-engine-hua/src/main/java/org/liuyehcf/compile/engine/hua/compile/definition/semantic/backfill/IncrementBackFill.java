package org.liuyehcf.compile.engine.hua.compile.definition.semantic.backfill;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.VariableSymbol;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._iinc;

import java.io.Serializable;

/**
 * 自增字节码回填
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public class IncrementBackFill extends AbstractSemanticAction implements Serializable {
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
    public void onAction(HuaContext context) {
        _iinc code = context.getAttr(backFillStackOffset, AttrName.IINC_BYTE_CODE);

        String identifierName = context.getAttr(expressionStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        if (variableSymbol == null) {
            throw new RuntimeException("前置递增/递减运算符不能作用于 '值'");
        }

        if (!Type.TYPE_INT.equals(variableSymbol.getType())) {
            throw new RuntimeException("前置递增/递减运算符不能作用于 '非int类型的变量'");
        }

        code.setOrder(variableSymbol.getOrder());
        code.setIncrement(increment);

        context.setAttr(backFillStackOffset, AttrName.IINC_BYTE_CODE, null);
    }
}
