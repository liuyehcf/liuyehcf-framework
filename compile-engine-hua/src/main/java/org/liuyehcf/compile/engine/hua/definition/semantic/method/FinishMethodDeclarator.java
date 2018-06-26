package org.liuyehcf.compile.engine.hua.definition.semantic.method;

import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 完成方法描述信息
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class FinishMethodDeclarator extends AbstractSemanticAction implements Serializable {
    @Override
    public void onAction(HuaContext context) {
        context.finishMethodDeclarator();
    }
}
