package com.github.liuyehcf.framework.expression.engine.compile.definition.model;

import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf.*;

/**
 * 跳转指令的类型
 *
 * @author hechenfeng
 * @date 2018/9/25
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
    ;

    public static ControlTransfer getControlTransferByType(ControlTransferType type) {
        ControlTransfer controlTransfer;

        switch (type) {
            case IFEQ:
                controlTransfer = new _ifeq();
                break;
            case IFGE:
                controlTransfer = new _ifge();
                break;
            case IFGT:
                controlTransfer = new _ifgt();
                break;
            case IFLE:
                controlTransfer = new _ifle();
                break;
            case IFLT:
                controlTransfer = new _iflt();
                break;
            case IFNE:
                controlTransfer = new _ifne();
                break;
            case GOTO:
                controlTransfer = new _goto();
                break;
            default:
                throw new ExpressionException("unexpected controlTransferType='" + type + "'");
        }

        return controlTransfer;
    }

    public static ControlTransfer getOppositeControlTransferByType(ControlTransferType type) {
        ControlTransfer controlTransfer;

        switch (type) {
            case IFEQ:
                controlTransfer = new _ifne();
                break;
            case IFGE:
                controlTransfer = new _iflt();
                break;
            case IFGT:
                controlTransfer = new _ifle();
                break;
            case IFLE:
                controlTransfer = new _ifgt();
                break;
            case IFLT:
                controlTransfer = new _ifge();
                break;
            case IFNE:
                controlTransfer = new _ifeq();
                break;
            default:
                throw new ExpressionException("unexpected controlTransferType='" + type + "'");
        }

        return controlTransfer;
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
            default:
                throw new ExpressionException("unexpected controlTransferType='" + this + "'");
        }
    }
}
