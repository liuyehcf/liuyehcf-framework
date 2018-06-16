package org.liuyehcf.compile.engine.hua.model;

import org.liuyehcf.compile.engine.hua.bytecode.cf.*;

/**
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
}
