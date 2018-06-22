package org.liuyehcf.compile.engine.core.cfg.lr;

/**
 * LR自动机-转移码
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
enum NodeTransferOperator {
    /**
     * 移入
     */
    MOVE_IN,

    /**
     * 规约
     */
    REDUCTION,

    /**
     * 跳转
     */
    JUMP,

    /**
     * 接收
     */
    ACCEPT,
}
