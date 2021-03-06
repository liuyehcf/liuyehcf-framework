package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.variable;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.bytecode.oc._anewarray;
import com.github.liuyehcf.framework.language.hua.core.bytecode.oc._multianewarray;
import com.github.liuyehcf.framework.language.hua.core.bytecode.oc._newarray;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertFalse;
import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.*;

/**
 * 基本类型的数组创建
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class NewPrimaryArray extends AbstractSemanticAction {
    /**
     * 类型-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int typeStackOffset;

    /**
     * 维度表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionDimStackOffset;

    /**
     * 空维度-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int emptyDimStackOffset;

    public NewPrimaryArray(int typeStackOffset, int expressionDimStackOffset, int emptyDimStackOffset) {
        this.typeStackOffset = typeStackOffset;
        this.expressionDimStackOffset = expressionDimStackOffset;
        this.emptyDimStackOffset = emptyDimStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Type type = context.getAttr(typeStackOffset, AttrName.TYPE);
        int expressionDimSize = context.getAttr(expressionDimStackOffset, AttrName.EXPRESSION_DIM_SIZE);
        int emptyDimSize = context.getAttr(emptyDimStackOffset, AttrName.EMPTY_DIM_SIZE);

        assertTrue(expressionDimSize > 0, "[SYNTAX_ERROR] - Create an array must specify the size of the first dimension");
        assertFalse(type.isArrayType(), "[SYSTEM_ERROR] - Array type's basic type cannot be array-type");

        switch (type.getTypeName()) {
            case NORMAL_BOOLEAN:
            case NORMAL_CHAR:
            case NORMAL_INT:
            case NORMAL_LONG:
            case NORMAL_FLOAT:
            case NORMAL_DOUBLE:
                break;
            default:
                throw new UnsupportedOperationException();
        }

        Type arrayType = Type.createArrayType(type.getTypeName(), expressionDimSize + emptyDimSize);

        if (expressionDimSize == 1) {
            if (emptyDimSize == 0) {
                /*
                 * 一维数组
                 */
                context.addByteCodeToCurrentMethod(new _newarray(arrayType.toDimDecreasedType().toTypeDescription()));
            } else {
                /*
                 * 多维数组，但是只指定了第一维度
                 */
                context.addByteCodeToCurrentMethod(new _anewarray(arrayType.toDimDecreasedType().toTypeDescription()));
            }
        } else {
            context.addByteCodeToCurrentMethod(new _multianewarray(arrayType.toTypeDescription(), expressionDimSize));
        }

        context.setAttrToLeftNode(AttrName.TYPE, arrayType);
    }
}
