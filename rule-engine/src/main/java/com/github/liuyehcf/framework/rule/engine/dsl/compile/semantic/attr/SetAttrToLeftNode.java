package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/5/10
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
