package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.backfill;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.VariableSymbol;
import com.github.liuyehcf.framework.language.hua.core.bytecode.cp._iinc;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertEquals;
import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull;

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
    public void onAction(CompilerContext context) {
        _iinc code = context.getAttr(backFillStackOffset, AttrName.IINC_BYTE_CODE);

        String identifierName = context.getAttr(expressionStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        assertNotNull(variableSymbol, "[SYNTAX_ERROR] - Pre-increment/decrement operator cannot act on 'value'");
        assertEquals(Type.TYPE_INT, variableSymbol.getType(), "[SYNTAX_ERROR] - Pre-increment/decrement operator does not work on 'non-int variables'");

        code.setOrder(variableSymbol.getOrder());
        code.setIncrement(increment);

        context.setAttr(backFillStackOffset, AttrName.IINC_BYTE_CODE, null);
    }
}
