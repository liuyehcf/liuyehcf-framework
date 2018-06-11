package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.SemanticAction;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * @author chenlu
 * @date 2018/6/11
 */
public abstract class AbstractSemanticAction implements SemanticAction<HuaCompiler.HuaContext> {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        
    }
}
