package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 完成方法描述信息
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class FinishMethodDeclarator extends AbstractSemanticAction {
    @Override
    public void onAction(HuaContext context) {
        context.finishMethodDeclarator();
    }
}
