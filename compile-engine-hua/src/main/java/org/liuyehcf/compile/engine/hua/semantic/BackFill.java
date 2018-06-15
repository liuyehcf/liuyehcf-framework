package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;

import java.util.List;

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

    /**
     * 是否确定回填
     * true：本次即完成回填
     * false：还可以被再次回填
     */
    private final boolean isCertain;

    public BackFill(int backFillStackOffset, BackFillType backFillType, boolean isCertain) {
        this.backFillStackOffset = backFillStackOffset;
        this.backFillType = backFillType;
        this.isCertain = isCertain;
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

        /*
         * 允许以下情况不进行回填
         * 1. `if(a) {...} ` 直接往下走TRUE代码块，不需要回填TRUE
         * 2. `a||b` 当a不成立的时候，直接往下走，不需要回填FALSE
         */
        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
            }

            /*
             * 解决的是如下情况
             *  if(a){
             *      if(b){  // 这里的回填值，需要被二次回填（第一次回填的是else部分的语句）
             *          ...
             *      }
             *  } else {
             *      ...
             *  }
             */
            if (!isCertain && hasOuterIfStatement(context)) {
                context.getHuaEngine().getStatusInfo().getUncertainCodes().addAll(codes);
            }

            /*
             * 已经回填的就删除，避免二次回填
             */
            codes.clear();
        }
    }

    private boolean hasOuterIfStatement(HuaCompiler.HuaContext context) {
        return context.getHuaEngine().getStatusInfo().getIfNestedLevel() > 0;
    }
}
