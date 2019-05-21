package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.code;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.BackFillType;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf.ControlTransfer;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 跳转指令抽象基类
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
abstract class AbstractControlTransferByteCode extends AbstractSemanticAction {
    /**
     * 待回填字节码所处的语法树节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    /**
     * 回填类型
     */
    private final BackFillType backFillType;

    AbstractControlTransferByteCode(int backFillStackOffset, BackFillType backFillType) {
        this.backFillStackOffset = backFillStackOffset;
        this.backFillType = backFillType;
    }

    void doAddCode(CompilerContext context, ControlTransfer code) {
        switch (backFillType) {
            case TRUE:
                doAddCode(context, AttrName.TRUE_BYTE_CODE, code);
                break;
            case FALSE:
                doAddCode(context, AttrName.FALSE_BYTE_CODE, code);
                break;
            case NEXT:
                doAddCode(context, AttrName.NEXT_BYTE_CODE, code);
                break;
            default:
                throw new ExpressionException("unexpected backFillType='" + backFillType + "'");
        }
    }

    private void doAddCode(CompilerContext context, AttrName attrName, ControlTransfer code) {
        List<ControlTransfer> controlTransfers = context.getAttr(backFillStackOffset, attrName);

        if (controlTransfers == null) {
            controlTransfers = Lists.newArrayList();
            context.setAttr(backFillStackOffset, attrName, controlTransfers);
        }

        controlTransfers.add(code);
    }
}
