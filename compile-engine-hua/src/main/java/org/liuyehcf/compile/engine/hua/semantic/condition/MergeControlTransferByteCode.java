package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
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
    public void onAction(HuaCompiler.HuaContext context) {
        doMerge(context, AttrName.TRUE_BYTE_CODE.name());
        doMerge(context, AttrName.FALSE_BYTE_CODE.name());
        doMerge(context, AttrName.NEXT_BYTE_CODE.name());
    }

    private void doMerge(HuaCompiler.HuaContext context, String attrName) {
        List<ControlTransfer> fromCodes = context.getStack().get(fromStackOffset).get(attrName);
        List<ControlTransfer> toCodes = context.getStack().get(toStackOffset).get(attrName);

        if (fromCodes == null) {
            return;
        }

        if (toCodes == null) {
            toCodes = new ArrayList<>();
            context.getStack().get(toStackOffset).put(attrName, toCodes);
        }

        toCodes.addAll(fromCodes);
    }
}
