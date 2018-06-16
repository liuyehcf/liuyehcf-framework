package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 为产生式左部的语法树节点设定属性值
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class SetAttrToLeftNode extends AbstractSemanticAction {

    /**
     * 属性-名称
     */
    private final String attrName;

    /**
     * 属性值
     */
    private final Object value;

    public SetAttrToLeftNode(String attrName, Object value) {
        this.attrName = attrName;
        this.value = value;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getLeftNode().put(attrName, value);
    }
}
