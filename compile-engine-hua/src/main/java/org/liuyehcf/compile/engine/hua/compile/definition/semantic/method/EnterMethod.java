package org.liuyehcf.compile.engine.hua.compile.definition.semantic.method;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 进入方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class EnterMethod extends AbstractSemanticAction implements Serializable {
    @Override
    public void onAction(CompilerContext context) {
        context.enterMethod();
    }
}
