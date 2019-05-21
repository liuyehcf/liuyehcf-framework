package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.method;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录方法调用参数的类型
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class RecordArgumentType extends AbstractSemanticAction {

    /**
     * 参数列表-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int argumentListStackOffset;

    /**
     * 参数表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionStackOffset;

    public RecordArgumentType(int argumentListStackOffset, int expressionStackOffset) {
        this.argumentListStackOffset = argumentListStackOffset;
        this.expressionStackOffset = expressionStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        List<Type> argumentTypeList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_TYPE_LIST);
        if (argumentTypeList == null) {
            argumentTypeList = new ArrayList<>();
            context.setAttr(argumentListStackOffset, AttrName.ARGUMENT_TYPE_LIST, argumentTypeList);
        }

        Type type = context.getAttr(expressionStackOffset, AttrName.TYPE);
        argumentTypeList.add(type);
    }
}
