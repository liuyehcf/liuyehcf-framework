package org.liuyehcf.compile.engine.hua.compile.definition.semantic.backfill;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.BackFillType;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf.ControlTransfer;

import java.io.Serializable;
import java.util.List;

/**
 * 跳转指令的回填
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public class ControlTransferByteCodeBackFill extends AbstractSemanticAction implements Serializable {

    /**
     * 存储待回填字节码的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    /**
     * 回填类型
     */
    private final BackFillType backFillType;

    public ControlTransferByteCodeBackFill(int backFillStackOffset, BackFillType backFillType) {
        this.backFillStackOffset = backFillStackOffset;
        this.backFillType = backFillType;
    }

    @Override
    public void onAction(CompilerContext context) {
        List<ControlTransfer> codes;

        switch (backFillType) {
            case TRUE:
                codes = context.getAttr(backFillStackOffset, AttrName.TRUE_BYTE_CODE);
                break;
            case FALSE:
                codes = context.getAttr(backFillStackOffset, AttrName.FALSE_BYTE_CODE);
                break;
            case NEXT:
                codes = context.getAttr(backFillStackOffset, AttrName.NEXT_BYTE_CODE);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        /*
         * 允许以下情况不进行回填
         * 1. `if(a) {...} ` 直接往下走TRUE代码块，不需要回填TRUE
         * 2. `a||b` 当a不成立的时候，直接往下走，不需要回填FALSE
         */
        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getByteCodeSizeOfCurrentMethod());
            }

            /*
             * 已经回填的就删除，避免二次回填
             */
            codes.clear();
        }
    }
}
