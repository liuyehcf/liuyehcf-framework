package org.liuyehcf.compile.engine.hua.compile.definition.semantic.attr;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

/**
 * 设置代码偏移量
 *
 * @author hechenfeng
 * @date 2018/6/19
 */
public class SetCodeOffsetAttr extends AbstractSemanticAction {
    @Override
    public void onAction(CompilerContext context) {
        context.setAttrToLeftNode(AttrName.CODE_OFFSET, context.getByteCodeSizeOfCurrentMethod());
    }
}
