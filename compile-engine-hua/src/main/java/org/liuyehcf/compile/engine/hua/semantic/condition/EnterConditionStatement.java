package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 进入条件语句，包括if语句、while语句
 *
 * @author chenlu
 * @date 2018/6/15
 */
public class EnterConditionStatement extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getHuaEngine().getStatusInfo().enterConditionStatement();
    }
}
