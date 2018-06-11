package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.ParamInfo;
import org.liuyehcf.compile.engine.hua.production.AttrName;

import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class AddParamInfo extends AbstractSemanticAction {
    private final int listStackOffset;

    private final int paramStackOffset;

    public AddParamInfo(int listStackOffset, int paramStackOffset) {
        this.listStackOffset = listStackOffset;
        this.paramStackOffset = paramStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String type = context.getStack().get(paramStackOffset).get(AttrName.TYPE.name());
        int width = context.getStack().get(paramStackOffset).get(AttrName.WIDTH.name());

        List<ParamInfo> paramInfoList = context.getStack().get(listStackOffset).get(AttrName.PARAMETER_LIST.name());
        paramInfoList.add(new ParamInfo(type, width));
    }
}
