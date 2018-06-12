package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class AddParamTypeInfo extends AbstractSemanticAction {
    private final int listStackOffset;

    private final int paramStackOffset;

    public AddParamTypeInfo(int listStackOffset, int paramStackOffset) {
        this.listStackOffset = listStackOffset;
        this.paramStackOffset = paramStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        Type type = context.getStack().get(paramStackOffset).get(AttrName.TYPE.name());

        List<Type> paramTypeList = context.getStack().get(listStackOffset).get(AttrName.PARAMETER_LIST.name());
        paramTypeList.add(type);
    }
}
