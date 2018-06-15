package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * @author chenlu
 * @date 2018/6/15
 */
public class ExitIfStatement extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getHuaEngine().getStatusInfo().exitIfStatement();
    }
}
