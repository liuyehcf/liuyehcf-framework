package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;

/**
 * @author chenlu
 * @date 2018/6/13
 */
public class EnterCondition extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        assertFalse(context.getHuaEngine().getStatusInfo().isCondition());
        context.getHuaEngine().getStatusInfo().setCondition(true);
    }
}
