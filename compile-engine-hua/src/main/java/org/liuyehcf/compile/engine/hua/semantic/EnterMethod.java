package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * 进入方法标记
 *
 * @author chenlu
 * @date 2018/6/7
 */
public class EnterMethod extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getHuaEngine().getMethodInfoTable().enterMethod();
    }
}
