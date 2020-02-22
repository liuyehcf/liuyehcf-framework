package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.variable;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;

/**
 * 数组类型降维
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class ArrayTypeDimDecrease extends AbstractSemanticAction {

    /**
     * 表达式名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionNameStackOffset;

    public ArrayTypeDimDecrease(int expressionNameStackOffset) {
        this.expressionNameStackOffset = expressionNameStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Type type = context.getAttr(expressionNameStackOffset, AttrName.TYPE);

        assertTrue(type.isArrayType(), "[SYNTAX_ERROR] - Non-array can not be reduced dimension operation");

        context.setAttrToLeftNode(AttrName.TYPE, type.toDimDecreasedType());
    }
}
