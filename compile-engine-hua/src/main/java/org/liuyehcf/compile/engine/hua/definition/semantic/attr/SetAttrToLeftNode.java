package org.liuyehcf.compile.engine.hua.definition.semantic.attr;

import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 为产生式左部的语法树节点设定属性值
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class SetAttrToLeftNode extends AbstractSemanticAction implements Serializable {

    /**
     * 属性-名称
     */
    private final AttrName attrName;

    /**
     * 属性值
     */
    private final Object value;

    public SetAttrToLeftNode(AttrName attrName, Object value) {
        this.attrName = attrName;
        this.value = value;
    }

    @Override
    public void onAction(HuaContext context) {
        context.setAttrToLeftNode(attrName, value);
    }
}
