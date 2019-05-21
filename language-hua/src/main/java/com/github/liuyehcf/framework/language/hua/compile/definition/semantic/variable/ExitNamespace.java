package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.variable;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;

/**
 * 退出当前命名空间
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class ExitNamespace extends AbstractSemanticAction {
    @Override
    public void onAction(CompilerContext context) {
        context.exitNamespace();
    }
}
