package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录方法描述符
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class RecordMethodDescription extends AbstractSemanticAction {

    /**
     * 返回类型-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int resultTypeStackOffset;

    /**
     * 方法描述-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int methodDeclaratorStackOffset;

    public RecordMethodDescription(int resultTypeStackOffset, int methodDeclaratorStackOffset) {
        this.resultTypeStackOffset = resultTypeStackOffset;
        this.methodDeclaratorStackOffset = methodDeclaratorStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        Type resultType = context.getStack().get(resultTypeStackOffset).get(AttrName.TYPE.name());
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().setResultType(resultType);

        String methodName = context.getStack().get(methodDeclaratorStackOffset).get(AttrName.METHOD_NAME.name());
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().setMethodName(methodName);

        List<Type> paramTypeList = context.getStack().get(methodDeclaratorStackOffset).get(AttrName.PARAMETER_LIST.name());
        if (paramTypeList == null) {
            paramTypeList = new ArrayList<>();
            context.getStack().get(methodDeclaratorStackOffset).put(AttrName.PARAMETER_LIST.name(), paramTypeList);
        }
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().setParamTypeList(paramTypeList);
    }
}
