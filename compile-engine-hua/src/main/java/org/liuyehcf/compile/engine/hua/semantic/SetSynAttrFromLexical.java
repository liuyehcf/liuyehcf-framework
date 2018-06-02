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
    private final int fromPos;

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
    private final int toPos;


    public SetSynAttrFromLexical(int fromPos, String toAttrName, int toPos) {
        this.fromPos = fromPos;
        this.toAttrName = toAttrName;
        this.toPos = toPos;
    }

    public int getFromPos() {
        return fromPos;
    }

    public String getToAttrName() {
        return toAttrName;
    }

    public int getToPos() {
        return toPos;
    }
}
