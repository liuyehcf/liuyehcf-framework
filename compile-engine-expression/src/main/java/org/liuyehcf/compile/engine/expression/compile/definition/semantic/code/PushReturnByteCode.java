package org.liuyehcf.compile.engine.expression.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.expression.compile.CompilerContext;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.expression.core.bytecode.ir._return;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public class PushReturnByteCode extends AbstractSemanticAction {
    @Override
    public void onAction(CompilerContext context) {
        context.addByteCode(new _return());
    }
}
