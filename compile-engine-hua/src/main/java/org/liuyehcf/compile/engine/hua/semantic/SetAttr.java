package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public class SetAttr extends AbstractSemanticAction {
    /**
     * 属性-偏移量（相对于产生式，从产生式左部为0，产生式右部为1、2、3...）
     */
    private final int pos;

    /**
     * 属性-名称
     */
    private final String attrName;

    /**
     * 属性-值
     */
    private final String attrValue;

    public SetAttr(int pos, String attrName, String attrValue) {
        this.pos = pos;
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    public int getPos() {
        return pos;
    }

    public String getAttrName() {
        return attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }
}
