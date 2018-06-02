package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public class CopySynAttr extends AbstractSemanticAction {
    /**
     * 源属性-偏移量（相对于产生式，从产生式左部为0，产生式右部为1、2、3...）
     */
    private final int fromPos;

    /**
     * 源属性-名称
     */
    private final String fromAttrName;

    /**
     * 宿属性-偏移量（相对于产生式，从产生式左部为0，产生式右部为1、2、3...）
     */
    private final int toPos;

    /**
     * 宿属性-名称
     */
    private final String toAttrName;

    public CopySynAttr(int fromPos, String fromAttrName, int toPos, String toAttrName) {
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
