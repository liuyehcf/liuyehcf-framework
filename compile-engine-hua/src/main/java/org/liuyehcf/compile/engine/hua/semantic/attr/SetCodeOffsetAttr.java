package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 设置代码偏移量
 *
 * @author hechenfeng
 * @date 2018/6/19
 */
public class SetCodeOffsetAttr extends AbstractSemanticAction {
    @Override
    public void onAction(HuaContext context) {
        context.setAttrToLeftNode(AttrName.CODE_OFFSET, context.getByteCodeSizeOfCurrentMethod());
    }
}
