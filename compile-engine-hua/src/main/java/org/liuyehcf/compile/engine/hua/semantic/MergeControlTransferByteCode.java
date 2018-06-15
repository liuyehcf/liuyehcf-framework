package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/15
 */
public class MergeControlTransferByteCode extends AbstractSemanticAction {
    private final int fromStackOffset;

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

    void doMerge(HuaCompiler.HuaContext context, String attrName) {
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
