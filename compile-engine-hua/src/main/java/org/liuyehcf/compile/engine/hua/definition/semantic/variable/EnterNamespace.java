package org.liuyehcf.compile.engine.hua.definition.semantic.variable;

import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 进入新的命名空间
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class EnterNamespace extends AbstractSemanticAction implements Serializable {
    @Override
    public void onAction(HuaContext context) {
        context.enterNamespace();
    }
}
