package org.liuyehcf.compile.engine.hua.semantic.code;

import org.liuyehcf.compile.engine.hua.bytecode.cp._iinc;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 前置 递增/递减
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public class PushPreIINCByteCode extends AbstractSemanticAction {

    /**
     * 用于存放回填iinc字节码的节点-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    public PushPreIINCByteCode(int backFillStackOffset) {
        this.backFillStackOffset = backFillStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        _iinc code = new _iinc();

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code);

        context.getStack().get(backFillStackOffset).put(AttrName.IINC_BYTE_CODE.name(), code);
    }
}
