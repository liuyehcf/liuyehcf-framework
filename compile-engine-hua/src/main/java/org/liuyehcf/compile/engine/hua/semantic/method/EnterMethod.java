package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 进入方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class EnterMethod extends AbstractSemanticAction implements Serializable {
    @Override
    public void onAction(HuaContext context) {
        context.enterMethod();
    }
}
