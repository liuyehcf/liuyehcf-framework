package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.code;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf.ControlTransfer;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 合并待回填字节码属性
 *
 * @author hechenfeng
 * @date 2018/9/25
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
    public void onAction(CompilerContext context) {
        doMerge(context, AttrName.TRUE_BYTE_CODE);
        doMerge(context, AttrName.FALSE_BYTE_CODE);
        doMerge(context, AttrName.NEXT_BYTE_CODE);
    }

    private void doMerge(CompilerContext context, AttrName attrName) {
        List<ControlTransfer> fromCodes = context.getAttr(fromStackOffset, attrName);
        List<ControlTransfer> toCodes = context.getAttr(toStackOffset, attrName);

        if (fromCodes == null) {
            return;
        }

        if (toCodes == null) {
            toCodes = Lists.newArrayList();
            context.setAttr(toStackOffset, attrName, toCodes);
        }

        toCodes.addAll(fromCodes);
    }
}
