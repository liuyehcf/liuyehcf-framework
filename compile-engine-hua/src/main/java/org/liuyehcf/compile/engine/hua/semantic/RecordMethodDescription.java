package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/10
 */
public class RecordMethodDescription extends AbstractSemanticAction {
    private static final int RESULT_TYPE_STACK_OFFSET = -1;
    private static final int METHOD_DECLARATOR_STACK_OFFSET = 0;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        Type resultType = context.getStack().get(RESULT_TYPE_STACK_OFFSET).get(AttrName.TYPE.name());
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().setResultType(resultType);

        String methodName = context.getStack().get(METHOD_DECLARATOR_STACK_OFFSET).get(AttrName.METHOD_NAME.name());
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().setMethodName(methodName);

        List<Type> paramTypeList = context.getStack().get(METHOD_DECLARATOR_STACK_OFFSET).get(AttrName.PARAMETER_LIST.name());
        if (paramTypeList == null) {
            paramTypeList = new ArrayList<>();
            context.getStack().get(METHOD_DECLARATOR_STACK_OFFSET).put(AttrName.PARAMETER_LIST.name(), paramTypeList);
        }
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().setParamTypeList(paramTypeList);
    }
}
