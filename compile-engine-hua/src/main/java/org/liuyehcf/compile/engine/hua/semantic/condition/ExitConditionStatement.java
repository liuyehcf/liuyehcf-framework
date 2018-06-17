package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 退出if语句，包括if语句、while、do while语句
 *
 * @author hechenfeng
 * @date 2018/6/15
 */
public class ExitConditionStatement extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getHuaEngine().getStatusInfo().exitConditionStatement();
    }
}
