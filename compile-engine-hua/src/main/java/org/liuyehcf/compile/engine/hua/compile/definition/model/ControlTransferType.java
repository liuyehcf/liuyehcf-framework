package org.liuyehcf.compile.engine.hua.compile.definition.model;

import org.liuyehcf.compile.engine.hua.core.bytecode.cf.*;

/**
 * 跳转指令的类型
 *
 * @author hechenfeng
 * @date 2018/6/14
 */
public enum ControlTransferType {
    /**
     * 等于0时跳转
     */
    IFEQ,

    /**
     * 不等于0时跳转
     */
    IFNE,

    /**
     * 大于等于0时跳转
     */
    IFGE,

    /**
     * 大于0时跳转
     */
    IFGT,

    /**
     * 小于等于0时跳转
     */
    IFLE,

    /**
     * 小于0时跳转
     */
    IFLT,

    /**
     * 无条件跳转
     */
    GOTO,

    /**
     * 比较，不等于时跳转
     */
    IF_ICMPNE,

    /**
     * 比较，相等时跳转
     */
    IF_ICMPEQ,

    /**
     * 比较，大于等于时跳转
     */
    IF_ICMPGE,

    /**
     * 比较，小于等于时跳转
     */
    IF_ICMPLE,

    /**
     * 比较，大于时跳转
     */
    IF_ICMPGT,

    /**
     * 比较，小于时跳转
     */
    IF_ICMPLT,;

    public static ControlTransfer getControlTransferByType(ControlTransferType type) {
        ControlTransfer code;

        switch (type) {
            case IFEQ:
                code = new _ifeq();
                break;
            case IFGE:
                code = new _ifge();
                break;
            case IFGT:
                code = new _ifgt();
                break;
            case IFLE:
                code = new _ifle();
                break;
            case IFLT:
                code = new _iflt();
                break;
            case IFNE:
                code = new _ifne();
                break;
            case GOTO:
                code = new _goto();
                break;
            case IF_ICMPNE:
                code = new _if_icmpne();
                break;
            case IF_ICMPEQ:
                code = new _if_icmpeq();
                break;
            case IF_ICMPGE:
                code = new _if_icmpge();
                break;
            case IF_ICMPLE:
                code = new _if_icmple();
                break;
            case IF_ICMPGT:
                code = new _if_icmpgt();
                break;
            case IF_ICMPLT:
                code = new _if_icmplt();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return code;
    }

    public static ControlTransfer getOppositeControlTransferByType(ControlTransferType type) {
        ControlTransfer code;

        switch (type) {
            case IFEQ:
                code = new _ifne();
                break;
            case IFGE:
                code = new _iflt();
                break;
            case IFGT:
                code = new _ifle();
                break;
            case IFLE:
                code = new _ifgt();
                break;
            case IFLT:
                code = new _ifge();
                break;
            case IFNE:
                code = new _ifeq();
                break;
            case IF_ICMPNE:
                code = new _if_icmpeq();
                break;
            case IF_ICMPEQ:
                code = new _if_icmpne();
                break;
            case IF_ICMPGE:
                code = new _if_icmplt();
                break;
            case IF_ICMPLE:
                code = new _if_icmpgt();
                break;
            case IF_ICMPGT:
                code = new _if_icmple();
                break;
            case IF_ICMPLT:
                code = new _if_icmpge();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return code;
    }

    public ControlTransferType getOppositeType() {
        switch (this) {
            case IFLT:
                return IFGE;
            case IFLE:
                return IFGT;
            case IFGT:
                return IFLE;
            case IFGE:
                return IFLT;
            case IFNE:
                return IFEQ;
            case IFEQ:
                return IFNE;
            case IF_ICMPEQ:
                return IF_ICMPNE;
            case IF_ICMPGE:
                return IF_ICMPLT;
            case IF_ICMPGT:
                return IF_ICMPLE;
            case IF_ICMPLE:
                return IF_ICMPGT;
            case IF_ICMPLT:
                return IF_ICMPGE;
            case IF_ICMPNE:
                return IF_ICMPEQ;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
