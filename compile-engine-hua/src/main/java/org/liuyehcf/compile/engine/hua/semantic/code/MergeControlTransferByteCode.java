package org.liuyehcf.compile.engine.hua.semantic.code;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 合并待回填字节码属性
 *
 * @author hechenfeng
 * @date 2018/6/15
 */
public class MergeControlTransferByteCode extends AbstractSemanticAction {

    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromStackOffset;

    /**
     * 宿属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示即将入栈的元素，以此类推
     */
    private final int toStackOffset;

    public MergeControlTransferByteCode(int fromStackOffset, int toStackOffset) {
        this.fromStackOffset = fromStackOffset;
        this.toStackOffset = toStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        doMerge(context, AttrName.TRUE_BYTE_CODE);
        doMerge(context, AttrName.FALSE_BYTE_CODE);
        doMerge(context, AttrName.NEXT_BYTE_CODE);
    }

    private void doMerge(HuaContext context, AttrName attrName) {
        List<ControlTransfer> fromCodes = context.getAttr(fromStackOffset, attrName);
        List<ControlTransfer> toCodes = context.getAttr(toStackOffset, attrName);

        if (fromCodes == null) {
            return;
        }

        if (toCodes == null) {
            toCodes = new ArrayList<>();
            context.setAttr(toStackOffset, attrName, toCodes);
        }

        toCodes.addAll(fromCodes);
    }
}
