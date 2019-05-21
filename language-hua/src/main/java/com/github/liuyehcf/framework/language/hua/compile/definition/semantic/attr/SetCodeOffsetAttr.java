package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.attr;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;

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
