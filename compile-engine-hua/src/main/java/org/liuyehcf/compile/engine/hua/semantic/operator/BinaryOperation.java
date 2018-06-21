package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.bytecode.cp.*;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.definition.Constant.*;

/**
 * 双目运算
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class BinaryOperation extends AbstractSemanticAction {

    /**
     * 左运算子-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int leftStackOffset;

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


    public BinaryOperation(int leftStackOffset, int operatorStackOffset, int rightStackOffset) {
        this.leftStackOffset = leftStackOffset;
        this.operatorStackOffset = operatorStackOffset;
        this.rightStackOffset = rightStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type leftType = context.getStack().get(leftStackOffset).get(AttrName.TYPE.name());
        Type rightType = context.getStack().get(rightStackOffset).get(AttrName.TYPE.name());
        String operator = context.getStack().get(operatorStackOffset).getValue();

        switch (operator) {
            case NORMAL_BIT_OR:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ior());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_BIT_XOR:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ixor());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_BIT_AND:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iand());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_SHL:
                checkIntegralType(rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ishl());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_SHR:
                checkIntegralType(rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ishr());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_USHR:
                checkIntegralType(rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iushr());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_ADD:
                checkEqualType(leftType, rightType, operator);


                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iadd());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_SUB:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _isub());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_MUL:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _imul());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_DIV:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _idiv());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            case NORMAL_REM:
                checkEqualType(leftType, rightType, operator);

                switch (leftType.getTypeName()) {
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _irem());
                        context.getLeftNode().put(AttrName.TYPE.name(), leftType);
                        break;
                    default:
                        throw new RuntimeException(leftType + "类型不支持 \'" + operator + "\' 运算");
                }

                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void checkEqualType(Type type1, Type type2, String operator) {
        if (!type1.equals(type2)) {
            throw new RuntimeException(" \'" + operator + "\' 运算符两侧运算子类型不一致");
        }
    }

    private void checkIntegralType(Type type, String operator) {
        if (!NORMAL_INT.equals(type.getTypeName())) {
            throw new RuntimeException(" \'" + operator + "\' 运算符右侧必须是整型");
        }
    }
}
