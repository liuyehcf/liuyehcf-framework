package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.attr;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;

/**
 * 为产生式左部的语法树节点设定属性值
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class SetAttrToLeftNode extends AbstractSemanticAction {

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
    public void onAction(CompilerContext context) {
        context.setAttrToLeftNode(attrName, value);
    }
}
