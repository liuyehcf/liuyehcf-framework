package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;

/**
 * 设置综合属性，来源于词法分析器
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public class SetAttrFromLexical extends AbstractSemanticAction implements Serializable {
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
    public void onAction(HuaContext context) {
        Object fromValue = context.getValue(fromStackOffset);

        assertNotNull(fromValue);
        context.setAttr(toStackOffset, toAttrName, fromValue);
    }
}
