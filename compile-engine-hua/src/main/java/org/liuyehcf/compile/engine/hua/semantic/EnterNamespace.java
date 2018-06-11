package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * 进入新的命名空间
 *
 * @author chenlu
 * @date 2018/6/3
 */
public class EnterNamespace extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getHuaEngine().getVariableSymbolTable().enterNamespace();
    }
}
