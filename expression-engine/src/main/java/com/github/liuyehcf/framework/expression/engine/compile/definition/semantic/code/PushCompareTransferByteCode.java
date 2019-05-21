package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.code;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.CompareOperatorType;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.ControlTransferType;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cp._cmp;

/**
 * 根据比较运算符的类型，添加对应的转义字节码
 * 转移字节码延迟加载
 * 因为不知道需要的是正向逻辑还是反向逻辑，例如if(expression)就是正向逻辑，do{}while(expression)就是反向逻辑
 * 但是这样一来，就需要注意必须要添加转移字节码(例如赋值语句、初始化语句、方法参数列表)
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class PushCompareTransferByteCode extends AbstractSemanticAction {

    /**
     * 比较运算符类型
     */
    private final CompareOperatorType compareOperatorType;

    public PushCompareTransferByteCode(CompareOperatorType compareOperatorType) {
        this.compareOperatorType = compareOperatorType;
    }

    @Override
    public void onAction(CompilerContext context) {
        context.addByteCode(new _cmp());

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
                throw new ExpressionException("unexpected compareOperatorType='" + compareOperatorType + "'");
        }
    }
}
