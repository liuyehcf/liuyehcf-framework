package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * 忽略下一个 进入命名空间的语义动作
 * 这是为了解决如下情况，方法/for循环的命名空间起始之处并不是{
 * void func(int a) {}
 * for(int a;;) {}
 *
 * @author chenlu
 * @date 2018/6/3
 */
public class IgnoreNextEnterNamespace extends AbstractSemanticAction {
    /**
     * 属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int stackOffset;

    public IgnoreNextEnterNamespace(int stackOffset) {
        this.stackOffset = stackOffset;
    }

    public int getStackOffset() {
        return stackOffset;
    }
}
