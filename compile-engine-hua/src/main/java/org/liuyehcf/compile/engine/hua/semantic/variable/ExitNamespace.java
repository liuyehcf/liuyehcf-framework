package org.liuyehcf.compile.engine.hua.semantic.variable;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 退出当前命名空间
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class ExitNamespace extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getHuaEngine().getVariableSymbolTable().exitNamespace();
    }
}
