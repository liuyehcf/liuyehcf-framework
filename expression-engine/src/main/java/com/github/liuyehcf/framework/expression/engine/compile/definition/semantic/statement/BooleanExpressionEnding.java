package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.statement;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.ControlTransferType;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf.ControlTransfer;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf._goto;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.sl._bconst;

import java.util.List;

/**
 * 布尔表达式收尾
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class BooleanExpressionEnding extends AbstractSemanticAction {

    /**
     * 布尔表达式-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int booleanExpressionStackOffset;

    public BooleanExpressionEnding(int booleanExpressionStackOffset) {
        this.booleanExpressionStackOffset = booleanExpressionStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Object conditionObject = context.getAttr(booleanExpressionStackOffset, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION);

        /*
         * 对于普通的boolean字面值，或者boolean类型的变量，直接赋值即可
         */
        if (conditionObject == null) {
            return;
        }

        ControlTransferType type = context.getAttr(booleanExpressionStackOffset, AttrName.CONTROL_TRANSFER_TYPE);
        ControlTransfer __code = ControlTransferType.getControlTransferByType(type);

        ControlTransfer __goto = new _goto();

        /*
         * 插入一个__code
         */
        context.addByteCode(__code);

        /*
         * TRUE回填
         */
        List<ControlTransfer> controlTransfers;
        controlTransfers = context.getAttr(booleanExpressionStackOffset, AttrName.TRUE_BYTE_CODE);
        if (controlTransfers != null) {
            for (ControlTransfer controlTransfer : controlTransfers) {
                controlTransfer.setCodeOffset(context.getByteCodeSize());
            }
            controlTransfers.clear();
        }

        /*
         * 压入true
         */
        context.addByteCode(new _bconst(true));

        /*
         * 压入goto
         */
        context.addByteCode(__goto);

        /*
         * 回填__code
         */
        __code.setCodeOffset(context.getByteCodeSize());

        /*
         * FALSE回填
         */
        controlTransfers = context.getAttr(booleanExpressionStackOffset, AttrName.FALSE_BYTE_CODE);
        if (controlTransfers != null) {
            for (ControlTransfer controlTransfer : controlTransfers) {
                controlTransfer.setCodeOffset(context.getByteCodeSize());
            }
            controlTransfers.clear();
        }

        /*
         * 压入false
         */
        context.addByteCode(new _bconst(false));

        /*
         * 回填__goto
         */
        __goto.setCodeOffset(context.getByteCodeSize());

        /*
         * 表达式已补上后缀，因此将属性置空
         */
        context.setAttr(booleanExpressionStackOffset, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, null);
    }
}
