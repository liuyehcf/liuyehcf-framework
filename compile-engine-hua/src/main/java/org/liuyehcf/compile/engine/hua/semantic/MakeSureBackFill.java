package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/15
 */
public class MakeSureBackFill extends AbstractSemanticAction {

    private static final int HEAD_STACK_OFFSET = -7;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        if (context.getHuaEngine().getStatusInfo().getUncertainCodes().isEmpty()) {
            return;
        }

        ControlTransfer transferCode = context.getHuaEngine().getStatusInfo().getUncertainCodes().get(0);
        int transferCodeOffset = transferCode.getCodeOffset();

        if (context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().get(transferCodeOffset) instanceof _goto) {
            /*
             * 需要二次回填
             */
            List<ControlTransfer> toCodes = context.getStack().get(HEAD_STACK_OFFSET).get(AttrName.NEXT_BYTE_CODE.name());
            if (toCodes == null) {
                toCodes = new ArrayList<>();
                context.getStack().get(HEAD_STACK_OFFSET).put(AttrName.NEXT_BYTE_CODE.name(), toCodes);
            }

            toCodes.addAll(context.getHuaEngine().getStatusInfo().getUncertainCodes());
        }

        /*
         * 1. 第一次回填即有效回填，无需二次回填，清空缓存
         * 2. 二次回填已经记录了，这里同样清空即可
         */
        context.getHuaEngine().getStatusInfo().getUncertainCodes().clear();
    }
}
