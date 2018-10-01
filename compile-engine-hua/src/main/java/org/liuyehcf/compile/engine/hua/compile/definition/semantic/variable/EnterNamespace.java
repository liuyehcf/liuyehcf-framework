package org.liuyehcf.compile.engine.hua.compile.definition.semantic.variable;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

/**
 * 进入新的命名空间
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class EnterNamespace extends AbstractSemanticAction {
    @Override
    public void onAction(CompilerContext context) {
        context.enterNamespace();
    }
}
