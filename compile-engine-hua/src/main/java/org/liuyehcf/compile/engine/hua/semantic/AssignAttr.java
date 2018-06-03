package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * 属性赋值
 *
 * @author chenlu
 * @date 2018/6/2
 */
public class AssignAttr extends AbstractSemanticAction {
    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromStackOffset;

    /**
     * 源属性-名称
     */
    private final String fromAttrName;

    /**
     * 宿属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示即将入栈的元素，以此类推
     */
    private final int toStackOffset;

    /**
     * 宿属性-名称
     */
    private final String toAttrName;

    public AssignAttr(int fromStackOffset, String fromAttrName, int toStackOffset, String toAttrName) {
        this.fromStackOffset = fromStackOffset;
        this.fromAttrName = fromAttrName;
        this.toStackOffset = toStackOffset;
        this.toAttrName = toAttrName;
    }

    public int getFromStackOffset() {
        return fromStackOffset;
    }

    public String getFromAttrName() {
        return fromAttrName;
    }

    public int getToStackOffset() {
        return toStackOffset;
    }

    public String getToAttrName() {
        return toAttrName;
    }
}
