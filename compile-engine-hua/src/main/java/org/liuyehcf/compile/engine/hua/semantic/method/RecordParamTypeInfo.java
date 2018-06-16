package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录形参信息
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class RecordParamTypeInfo extends AbstractSemanticAction {

    /**
     * 形参列表-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int paramListStackOffset;

    /**
     * 形参-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int paramStackOffset;

    public RecordParamTypeInfo(int paramListStackOffset, int paramStackOffset) {
        this.paramListStackOffset = paramListStackOffset;
        this.paramStackOffset = paramStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        Type type = context.getStack().get(paramStackOffset).get(AttrName.TYPE.name());

        List<Type> paramTypeList = context.getStack().get(paramListStackOffset).get(AttrName.PARAMETER_LIST.name());
        if (paramTypeList == null) {
            paramTypeList = new ArrayList<>();
            context.getStack().get(paramListStackOffset).put(AttrName.PARAMETER_LIST.name(), paramTypeList);
        }
        paramTypeList.add(type);
    }
}
