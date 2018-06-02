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
    private final int fromOffset;

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
    private final int toOffset;

    /**
     * 宿属性-名称
     */
    private final String toAttrName;

    public AssignAttr(int fromOffset, String fromAttrName, int toOffset, String toAttrName) {
        this.fromOffset = fromOffset;
        this.fromAttrName = fromAttrName;
        this.toOffset = toOffset;
        this.toAttrName = toAttrName;
    }

    public int getFromOffset() {
        return fromOffset;
    }

    public String getFromAttrName() {
        return fromAttrName;
    }

    public int getToOffset() {
        return toOffset;
    }

    public String getToAttrName() {
        return toAttrName;
    }
}
