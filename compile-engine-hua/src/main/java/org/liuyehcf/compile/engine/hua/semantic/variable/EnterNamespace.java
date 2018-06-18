package org.liuyehcf.compile.engine.hua.semantic.variable;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 进入新的命名空间
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class EnterNamespace extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        context.getHuaEngine().getVariableSymbolTable().enterNamespace();
    }
}