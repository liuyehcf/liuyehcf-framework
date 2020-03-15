package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.method;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;

/**
 * 进入方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class EnterMethod extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        context.enterMethod();
    }
}
