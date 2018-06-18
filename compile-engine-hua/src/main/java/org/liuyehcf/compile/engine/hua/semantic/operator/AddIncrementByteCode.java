package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.bytecode.cp._iinc;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 添加 ++ -- 的字节码
 *
 * @author chenlu
 * @date 2018/6/18
 */
public class AddIncrementByteCode extends AbstractSemanticAction {

    /**
     * 用于存放回填iinc字节码的节点-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    public AddIncrementByteCode(int backFillStackOffset) {
        this.backFillStackOffset = backFillStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        _iinc code = new _iinc();

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code);

        context.getStack().get(backFillStackOffset).put(AttrName.IINC_BYTE_CODE.name(), code);
    }
}
