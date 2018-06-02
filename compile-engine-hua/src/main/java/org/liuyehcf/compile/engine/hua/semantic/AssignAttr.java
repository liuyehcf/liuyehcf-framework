package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public class AssignAttr extends AbstractSemanticAction {
    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示即将入栈的元素，以此类推
     */
    private final int fromPos;

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
    private final int toPos;

    /**
     * 宿属性-名称
     */
    private final String toAttrName;

    public AssignAttr(int fromPos, String fromAttrName, int toPos, String toAttrName) {
        this.fromPos = fromPos;
        this.fromAttrName = fromAttrName;
        this.toPos = toPos;
        this.toAttrName = toAttrName;
    }

    public int getFromPos() {
        return fromPos;
    }

    public String getFromAttrName() {
        return fromAttrName;
    }

    public int getToPos() {
        return toPos;
    }

    public String getToAttrName() {
        return toAttrName;
    }
}
