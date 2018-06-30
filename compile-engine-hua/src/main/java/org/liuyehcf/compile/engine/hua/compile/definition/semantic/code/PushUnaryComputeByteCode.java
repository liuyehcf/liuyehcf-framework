package org.liuyehcf.compile.engine.hua.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._ineg;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp._lneg;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * 添加一元运算指令
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class PushUnaryComputeByteCode extends AbstractSemanticAction {
    /**
     * 操作符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int operatorStackOffset;

    /**
     * 右运算子-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int rightStackOffset;

    public PushUnaryComputeByteCode(int operatorStackOffset, int rightStackOffset) {
        this.operatorStackOffset = operatorStackOffset;
        this.rightStackOffset = rightStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type rightType = context.getAttr(rightStackOffset, AttrName.TYPE);
        String operator = context.getValue(operatorStackOffset);

        String typeName = rightType.getTypeName();
        switch (operator) {
            case NORMAL_SUB:
                switch (typeName) {
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _ineg());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lneg());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                break;
            case NORMAL_ADD:
                break;
            default:
                throw new UnsupportedOperationException();
        }

        context.setAttrToLeftNode(AttrName.TYPE, rightType);
    }
}
