package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * 复制属性给产生式左部的节点
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class AssignAttrsToLeftNode extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromStackOffset;

    /**
     * 属性-名称
     */
    private final AttrName[] attrNames;

    public AssignAttrsToLeftNode(int fromStackOffset, AttrName... attrNames) {
        this.fromStackOffset = fromStackOffset;
        this.attrNames = attrNames;
    }

    @Override
    public void onAction(CompilerContext context) {
        for (AttrName attrName : attrNames) {
            Object value = context.getAttr(fromStackOffset, attrName);
            context.setAttrToLeftNode(attrName, value);
        }
    }
}
