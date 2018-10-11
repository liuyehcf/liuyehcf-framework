package org.liuyehcf.compile.engine.expression.compile.definition.semantic.attr;

import org.liuyehcf.compile.engine.expression.compile.CompilerContext;
import org.liuyehcf.compile.engine.expression.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;

/**
 * 设置综合属性，来源于词法分析器
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class SetAttrFromLexical extends AbstractSemanticAction {
    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromStackOffset;

    /**
     * 宿属性-名称
     */
    private final AttrName toAttrName;

    /**
     * 宿属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int toStackOffset;


    public SetAttrFromLexical(int fromStackOffset, AttrName toAttrName, int toStackOffset) {
        this.fromStackOffset = fromStackOffset;
        this.toAttrName = toAttrName;
        this.toStackOffset = toStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Object fromValue = context.getValue(fromStackOffset);

        assertNotNull(fromValue, "[SYSTEM_ERROR] - From value of SemanticAction 'SetAttrFromLexical' cannot be null");
        context.setAttr(toStackOffset, toAttrName, fromValue);
    }
}
