package org.liuyehcf.compile.engine.hua.semantic.code;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 跳转指令抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public abstract class AbstractControlTransferByteCode extends AbstractSemanticAction {
    /**
     * 待回填字节码所处的语法树节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    /**
     * 回填类型
     */
    private final BackFillType backFillType;

    public AbstractControlTransferByteCode(int backFillStackOffset, BackFillType backFillType) {
        this.backFillStackOffset = backFillStackOffset;
        this.backFillType = backFillType;
    }

    void doAddCode(HuaContext context, ControlTransfer code) {
        switch (backFillType) {
            case TRUE:
                doAddCode(context, AttrName.TRUE_BYTE_CODE.name(), code);
                break;
            case FALSE:
                doAddCode(context, AttrName.FALSE_BYTE_CODE.name(), code);
                break;
            case NEXT:
                doAddCode(context, AttrName.NEXT_BYTE_CODE.name(), code);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void doAddCode(HuaContext context, String attrName, ControlTransfer code) {
        List<ControlTransfer> codes = context.getStack().get(backFillStackOffset).get(attrName);

        if (codes == null) {
            codes = new ArrayList<>();
            context.getStack().get(backFillStackOffset).put(attrName, codes);
        }

        codes.add(code);
    }
}
