package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.bytecode.cp._iinc;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 设置 递增/递减 属性
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public class SetIINCAttr extends AbstractSemanticAction implements Serializable {

    @Override
    public void onAction(HuaContext context) {
        _iinc code = new _iinc();

        context.addByteCodeToCurrentMethod(code);

        context.setAttrToLeftNode(AttrName.IINC_BYTE_CODE, code);
    }
}
