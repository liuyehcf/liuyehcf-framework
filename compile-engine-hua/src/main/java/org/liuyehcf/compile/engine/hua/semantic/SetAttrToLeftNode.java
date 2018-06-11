package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * 为产生式左部的语法树节点设定属性值
 *
 * @author chenlu
 * @date 2018/6/11
 */
public class SetAttrToLeftNode extends AbstractSemanticAction {
    private final String attrName;

    private final Object value;

    public SetAttrToLeftNode(String attrName, Object value) {
        this.attrName = attrName;
        this.value = value;
    }

    public String getAttrName() {
        return attrName;
    }

    public Object getValue() {
        return value;
    }
}
