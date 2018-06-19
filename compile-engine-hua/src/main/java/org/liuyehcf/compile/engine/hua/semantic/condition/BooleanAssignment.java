package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.bytecode.cf._ifeq;
import org.liuyehcf.compile.engine.hua.bytecode.sl._iconst;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.ControlTransferType;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.List;

/**
 * 布尔赋值语句
 *
 * @author hechenfeng
 * @date 2018/6/15
 */
public class BooleanAssignment extends AbstractSemanticAction {

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
        Object conditionObject = context.getStack().get(booleanExpressionStackOffset).get(AttrName.IS_COMPLEX_BOOLEAN_EXPRESSION.name());

        /*
         * 对于普通的boolean字面值，或者boolean类型的变量，直接赋值即可
         */
        if (conditionObject == null) {
            return;
        }

        ControlTransferType type = context.getStack().get(booleanExpressionStackOffset).get(AttrName.BOOLEAN_EXPRESSION_TYPE.name());

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
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(__code);

        /*
         * TRUE回填
         */
        List<ControlTransfer> codes;
        codes = context.getStack().get(booleanExpressionStackOffset).get(AttrName.TRUE_BYTE_CODE.name());
        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
            }
            codes.clear();
        }

        /*
         * 压入1，代表true
         */
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iconst(1));

        /*
         * 压入goto
         */
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(__goto);

        /*
         * 回填__code
         */
        __code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());

        /*
         * FALSE回填
         */
        codes = context.getStack().get(booleanExpressionStackOffset).get(AttrName.FALSE_BYTE_CODE.name());
        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
            }
            codes.clear();
        }

        /*
         * 压入0，代表false
         */
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iconst(0));

        /*
         * 回填__goto
         */
        __goto.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
    }
}
