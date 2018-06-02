package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * 设置综合属性，来源于词法分析器
 *
 * @author chenlu
 * @date 2018/6/2
 */
public class SetSynAttrFromLexical extends AbstractSemanticAction {
    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromOffset;

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
    private final int toOffset;


    public SetSynAttrFromLexical(int fromOffset, String toAttrName, int toOffset) {
        this.fromOffset = fromOffset;
        this.toAttrName = toAttrName;
        this.toOffset = toOffset;
    }

    public int getFromOffset() {
        return fromOffset;
    }

    public String getToAttrName() {
        return toAttrName;
    }

    public int getToOffset() {
        return toOffset;
    }
}
