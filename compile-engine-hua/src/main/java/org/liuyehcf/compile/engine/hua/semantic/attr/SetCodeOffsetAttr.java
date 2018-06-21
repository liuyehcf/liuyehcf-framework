package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 设置代码偏移量
 *
 * @author chenlu
 * @date 2018/6/19
 */
public class SetCodeOffsetAttr extends AbstractSemanticAction {
    @Override
    public void onAction(HuaContext context) {
        context.getLeftNode().put(AttrName.CODE_OFFSET.name(),
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
    }
}
