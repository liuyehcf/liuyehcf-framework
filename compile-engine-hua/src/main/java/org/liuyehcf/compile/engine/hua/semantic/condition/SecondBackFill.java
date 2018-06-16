package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 跳转指令的二次回填
 *
 * @author chenlu
 * @date 2018/6/15
 */
public class SecondBackFill extends AbstractSemanticAction {

    /**
     * if-then-else 的栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int ifElseStatementStackOffset;

    public SecondBackFill(int ifElseStatementStackOffset) {
        this.ifElseStatementStackOffset = ifElseStatementStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        if (context.getHuaEngine().getStatusInfo().getUncertainCodes().isEmpty()) {
            return;
        }

        List<ControlTransfer> toCodes = context.getStack().get(ifElseStatementStackOffset).get(AttrName.NEXT_BYTE_CODE.name());
        if (toCodes == null) {
            toCodes = new ArrayList<>();
            context.getStack().get(ifElseStatementStackOffset).put(AttrName.NEXT_BYTE_CODE.name(), toCodes);
        }

        for (ControlTransfer transferCode : context.getHuaEngine().getStatusInfo().getUncertainCodes()) {
            if (isValidTransferCodeOffset(context, transferCode)) {
                continue;
            }
            /*
             * 需要二次回填
             */
            toCodes.add(transferCode);
        }

        /*
         * 1. 第一次回填即有效回填，无需二次回填，清空缓存
         * 2. 二次回填已经记录了，这里同样清空即可
         */
        context.getHuaEngine().getStatusInfo().getUncertainCodes().clear();
    }

    private boolean isValidTransferCodeOffset(HuaCompiler.HuaContext context, ControlTransfer transferCode) {
        int transferCodeOffset = transferCode.getCodeOffset();
        return !(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().get(transferCodeOffset) instanceof _goto);
    }
}
