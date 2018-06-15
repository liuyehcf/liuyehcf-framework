package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;

import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertEquals;
import static org.liuyehcf.compile.engine.hua.model.BackFillType.TRUE;

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
        List<ControlTransfer> codes;

        switch (backFillType) {
            case TRUE:
                codes = context.getStack().get(backFillStackOffset).get(AttrName.TRUE_BYTE_CODE.name());
                break;
            case FALSE:
                codes = context.getStack().get(backFillStackOffset).get(AttrName.FALSE_BYTE_CODE.name());
                break;
            case NEXT:
                codes = context.getStack().get(backFillStackOffset).get(AttrName.NEXT_BYTE_CODE.name());
                break;
            default:
                throw new UnsupportedOperationException();
        }


        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
            }
        } else {
            /*
             * 允许 `if(a) {...} ` 直接往下走TRUE代码块，不需要回填值
             */
            assertEquals(backFillType, TRUE);
        }
    }
}
