package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.production.AttrName;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class IncreaseArrayTypeDim extends AbstractSemanticAction {
    private final int stackOffset;

    public IncreaseArrayTypeDim(int stackOffset) {
        this.stackOffset = stackOffset;
    }


    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String originType = context.getStack().get(stackOffset).get(AttrName.TYPE.name());
        originType = originType + "[]";
        context.getStack().get(stackOffset).put(AttrName.TYPE.name(), originType);
    }
}
