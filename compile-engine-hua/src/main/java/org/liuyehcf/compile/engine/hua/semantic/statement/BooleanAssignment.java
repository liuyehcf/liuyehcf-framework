package org.liuyehcf.compile.engine.hua.semantic.statement;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.bytecode.cf._ifeq;
import org.liuyehcf.compile.engine.hua.bytecode.sl._iconst;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.io.Serializable;
import java.util.List;

/**
 * 布尔赋值语句
 *
 * @author hechenfeng
 * @date 2018/6/15
 */
public class BooleanAssignment extends AbstractSemanticAction implements Serializable {

    /**
     * 布尔表达式-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int booleanExpressionStackOffset;

    public BooleanAssignment(int booleanExpressionStackOffset) {
        this.booleanExpressionStackOffset = booleanExpressionStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Object conditionObject = context.getAttr(booleanExpressionStackOffset, AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION);

        /*
         * 对于普通的boolean字面值，或者boolean类型的变量，直接赋值即可
         */
        if (conditionObject == null) {
            return;
        }

        ControlTransferType type = context.getAttr(booleanExpressionStackOffset, AttrName.BOOLEAN_EXPRESSION_TYPE);

        ControlTransfer __code;

        if (type == null) {
            __code = new _ifeq();
        } else {
            __code = ControlTransferType.getControlTransferByType(type);
        }

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
    }
}
