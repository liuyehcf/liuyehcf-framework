package org.liuyehcf.compile.engine.expression.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.expression.compile.CompilerContext;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.expression.core.bytecode.sl._aaload;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class PushArrayItemLoadByteCode extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        context.addByteCode(new _aaload());
    }
}
