package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.attr;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.bytecode.cp._iinc;

/**
 * 设置 递增/递减 属性
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public class SetIINCAttr extends AbstractSemanticAction {

    @Override
    public void onAction(CompilerContext context) {
        _iinc code = new _iinc();

        context.addByteCodeToCurrentMethod(code);

        context.setAttrToLeftNode(AttrName.IINC_BYTE_CODE, code);
    }
}
