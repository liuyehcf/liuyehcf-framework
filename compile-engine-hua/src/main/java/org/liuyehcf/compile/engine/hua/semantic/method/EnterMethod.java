package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 进入方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class EnterMethod extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getHuaEngine().getMethodInfoTable().enterMethod();
    }
}
