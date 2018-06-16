package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.core.cfg.lr.AbstractLRCompiler;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;

/**
 * 设置综合属性，来源于词法分析器
 *
 * @author chenlu
 * @date 2018/6/2
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
    private final String toAttrName;

    /**
     * 宿属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int toStackOffset;


    public SetAttrFromLexical(int fromStackOffset, String toAttrName, int toStackOffset) {
        this.fromStackOffset = fromStackOffset;
        this.toAttrName = toAttrName;
        this.toStackOffset = toStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        AbstractLRCompiler.SyntaxNode fromNode = context.getStack().get(fromStackOffset);
        AbstractLRCompiler.SyntaxNode toNode = context.getStack().get(toStackOffset);

        assertNotNull(fromNode.getValue());
        toNode.put(toAttrName, fromNode.getValue());
    }
}
