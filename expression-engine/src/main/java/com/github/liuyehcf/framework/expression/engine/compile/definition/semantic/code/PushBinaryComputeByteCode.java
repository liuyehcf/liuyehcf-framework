package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.code;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cp.*;

import static com.github.liuyehcf.framework.expression.engine.compile.definition.Constant.*;

/**
 * 添加双目运算
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class PushBinaryComputeByteCode extends AbstractSemanticAction {

    /**
     * 操作符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int operatorStackOffset;

    public PushBinaryComputeByteCode(int operatorStackOffset) {
        this.operatorStackOffset = operatorStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        String operator = context.getValue(operatorStackOffset);

        switch (operator) {
            case NORMAL_BIT_OR:
                context.addByteCode(new _or());
                break;
            case NORMAL_BIT_XOR:
                context.addByteCode(new _xor());
                break;
            case NORMAL_BIT_AND:
                context.addByteCode(new _and());
                break;
            case NORMAL_SHL:
                context.addByteCode(new _shl());
                break;
            case NORMAL_SHR:
                context.addByteCode(new _shr());
                break;
            case NORMAL_USHR:
                context.addByteCode(new _ushr());
                break;
            case NORMAL_ADD:
                context.addByteCode(new _add());
                break;
            case NORMAL_SUB:
                context.addByteCode(new _sub());
                break;
            case NORMAL_MUL:
                context.addByteCode(new _mul());
                break;
            case NORMAL_DIV:
                context.addByteCode(new _div());
                break;
            case NORMAL_REM:
                context.addByteCode(new _rem());
                break;
            default:
                throw new ExpressionException("unexpected operator='" + operator + "'");
        }
    }
}
