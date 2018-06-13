package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;

/**
 * @author chenlu
 * @date 2018/6/13
 */
public class ExitCondition extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        assertTrue(context.getHuaEngine().getStatusInfo().isCondition());
        context.getHuaEngine().getStatusInfo().setCondition(false);
    }
}
