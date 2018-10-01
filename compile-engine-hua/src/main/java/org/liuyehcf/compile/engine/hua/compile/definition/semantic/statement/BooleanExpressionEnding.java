package org.liuyehcf.compile.engine.hua.compile.definition.semantic.statement;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._iconst;

import java.util.List;

/**
 * 布尔表达式收尾
 *
 * @author hechenfeng
 * @date 2018/6/15
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
        context.addByteCodeToCurrentMethod(__code);

        /*
         * TRUE回填
         */
        List<ControlTransfer> codes;
        codes = context.getAttr(booleanExpressionStackOffset, AttrName.TRUE_BYTE_CODE);
        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getByteCodeSizeOfCurrentMethod());
            }
            codes.clear();
        }

        /*
         * 压入1，代表true
         */
        context.addByteCodeToCurrentMethod(new _iconst(1));

        /*
         * 压入goto
         */
        context.addByteCodeToCurrentMethod(__goto);

        /*
         * 回填__code
         */
        __code.setCodeOffset(context.getByteCodeSizeOfCurrentMethod());

        /*
         * FALSE回填
         */
        codes = context.getAttr(booleanExpressionStackOffset, AttrName.FALSE_BYTE_CODE);
        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getByteCodeSizeOfCurrentMethod());
            }
            codes.clear();
        }

        /*
         * 压入0，代表false
         */
        context.addByteCodeToCurrentMethod(new _iconst(0));

        /*
         * 回填__goto
         */
        __goto.setCodeOffset(context.getByteCodeSizeOfCurrentMethod());

        /*
         * 表达式已补上后缀，因此将属性置空
         */
        context.setAttr(booleanExpressionStackOffset, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION, null);
    }
}
