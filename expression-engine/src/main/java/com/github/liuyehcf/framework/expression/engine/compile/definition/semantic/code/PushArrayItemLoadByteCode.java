package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.code;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.sl._aaload;

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
