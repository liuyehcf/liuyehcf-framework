package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
abstract class AddExecutable extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    final int nameStackOffset;

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    final int argumentListStackOffset;

    AddExecutable(int nameStackOffset, int argumentListStackOffset) {
        this.nameStackOffset = nameStackOffset;
        this.argumentListStackOffset = argumentListStackOffset;
    }
}
