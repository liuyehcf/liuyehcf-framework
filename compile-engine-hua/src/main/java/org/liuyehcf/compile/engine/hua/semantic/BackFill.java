package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;

/**
 * 布尔值回填
 *
 * @author chenlu
 * @date 2018/6/13
 */
public class BackFill extends AbstractSemanticAction {

    /**
     * 回填节点的栈偏移量
     */
    private final int backFillStackOffset;

    /**
     * 回填类型
     */
    private final BackFillType backFillType;

    public BackFill(int backFillStackOffset, BackFillType backFillType) {
        this.backFillStackOffset = backFillStackOffset;
        this.backFillType = backFillType;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        ControlTransfer code;

        switch (backFillType) {
            case TRUE:
                code = context.getStack().get(backFillStackOffset).get(AttrName.TRUE_BYTE_CODE.name());
                break;
            case FALSE:
                code = context.getStack().get(backFillStackOffset).get(AttrName.FALSE_BYTE_CODE.name());
                break;
            case NEXT:
                code = context.getStack().get(backFillStackOffset).get(AttrName.NEXT_BYTE_CODE.name());
                break;
            default:
                throw new UnsupportedOperationException();
        }

        /*
         * 允许 `if(a) {...} ` 直接往下走，不需要回填值
         */
        if (code != null) {
            code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
        }
    }
}
