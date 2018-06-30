package org.liuyehcf.compile.engine.hua.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.VariableSymbol;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._iinc;

import java.io.Serializable;

/**
 * 后置 递增/递减
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public class PushPostIINCByteCode extends AbstractSemanticAction implements Serializable {
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

    public PushPostIINCByteCode(int expressionStackOffset, int increment) {
        this.expressionStackOffset = expressionStackOffset;
        this.increment = increment;
    }

    @Override
    public void onAction(CompilerContext context) {
        String identifierName = context.getAttr(expressionStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        if (variableSymbol == null) {
            throw new RuntimeException("Pre-increment/decrement operator cannot act on 'value'");
        }

        if (!Type.TYPE_INT.equals(variableSymbol.getType())) {
            throw new RuntimeException("Pre-increment/decrement operator does not work on 'non-int variables'");
        }

        _iinc code = new _iinc();

        code.setOrder(variableSymbol.getOrder());
        code.setIncrement(increment);

        context.addByteCodeToCurrentMethod(code);
    }
}
