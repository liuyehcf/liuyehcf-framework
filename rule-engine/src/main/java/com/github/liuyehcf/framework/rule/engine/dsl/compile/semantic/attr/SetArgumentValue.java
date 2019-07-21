package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.attr;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.ArgumentValueType;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class SetArgumentValue extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int valueStackOffset;

    private final AttrName valueAttrName;

    private final ArgumentValueType argumentValueType;

    public SetArgumentValue(int valueStackOffset, AttrName valueAttrName, ArgumentValueType argumentValueType) {
        this.valueStackOffset = valueStackOffset;
        this.valueAttrName = valueAttrName;
        this.argumentValueType = argumentValueType;
    }

    @Override
    public void onAction(CompilerContext context) {
        switch (argumentValueType) {
            case LITERAL:
                context.setAttrToLeftNode(AttrName.ARGUMENT_VALUE, context.getAttr(valueStackOffset, valueAttrName));
                break;
            case PLACE_HOLDER:
                context.setAttrToLeftNode(AttrName.ARGUMENT_VALUE, "${" + context.getAttr(valueStackOffset, valueAttrName) + "}");
                break;
        }
    }
}
