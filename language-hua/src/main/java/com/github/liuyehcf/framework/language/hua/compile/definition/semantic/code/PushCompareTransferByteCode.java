package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.code;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.CompareOperatorType;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.ControlTransferType;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.bytecode.cp._dcmp;
import com.github.liuyehcf.framework.language.hua.core.bytecode.cp._fcmp;
import com.github.liuyehcf.framework.language.hua.core.bytecode.cp._lcmp;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertFalse;
import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.*;

/**
 * 根据比较运算符的类型，添加对应的转义字节码
 * 转移字节码延迟加载
 * 因为不知道需要的是正向逻辑还是反向逻辑，例如if(expression)就是正向逻辑，do{}while(expression)就是反向逻辑
 * 但是这样一来，就需要注意必须要添加转移字节码(例如赋值语句、初始化语句、方法参数列表)
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class PushCompareTransferByteCode extends AbstractSemanticAction {

    /**
     * 左运算子-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int leftOperatorStackOffset;

    /**
     * 比较运算符类型
     */
    private final CompareOperatorType compareOperatorType;

    /**
     * 右运算子-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int rightOperatorStackOffset;

    public PushCompareTransferByteCode(int leftOperatorStackOffset, CompareOperatorType compareOperatorType, int rightOperatorStackOffset) {
        this.leftOperatorStackOffset = leftOperatorStackOffset;
        this.compareOperatorType = compareOperatorType;
        this.rightOperatorStackOffset = rightOperatorStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Type leftType = context.getAttr(leftOperatorStackOffset, AttrName.TYPE);
        Type rightType = context.getAttr(rightOperatorStackOffset, AttrName.TYPE);

        assertTrue(Type.isCompatible(leftType, rightType) || Type.isCompatible(rightType, leftType),
                "[SYNTAX_ERROR] - Comparable operator left and right type do not match");
        assertFalse(leftType.isArrayType(), "[SYSTEM_ERROR] - Comparable operator cannot support array type");

        Type finalType;

        if (Type.isCompatible(leftType, rightType)) {
            finalType = leftType;
        } else {
            finalType = rightType;
        }


        String typeName = finalType.getTypeName();

        if (NORMAL_CHAR.equals(typeName) || NORMAL_INT.equals(typeName)) {
            switch (compareOperatorType) {
                case EQUAL:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IF_ICMPNE);
                    break;
                case NOT_EQUAL:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IF_ICMPEQ);
                    break;
                case LESS_THAN:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IF_ICMPGE);
                    break;
                case LARGE_THEN:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IF_ICMPLE);
                    break;
                case LESS_EQUAL:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IF_ICMPGT);
                    break;
                case LARGE_EQUAL:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IF_ICMPLT);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        } else {
            if (NORMAL_LONG.equals(typeName)) {
                context.addByteCodeToCurrentMethod(new _lcmp());
            } else if (NORMAL_FLOAT.equals(typeName)) {
                context.addByteCodeToCurrentMethod(new _fcmp());
            } else if (NORMAL_DOUBLE.equals(typeName)) {
                context.addByteCodeToCurrentMethod(new _dcmp());
            } else {
                throw new UnsupportedOperationException();
            }

            switch (compareOperatorType) {
                case EQUAL:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IFNE);
                    break;
                case NOT_EQUAL:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IFEQ);
                    break;
                case LESS_THAN:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IFGE);
                    break;
                case LARGE_THEN:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IFLE);
                    break;
                case LESS_EQUAL:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IFGT);
                    break;
                case LARGE_EQUAL:
                    context.setAttrToLeftNode(AttrName.CONTROL_TRANSFER_TYPE, ControlTransferType.IFLT);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_BOOLEAN);
    }
}
