package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * 创建一个变量，记录类型、宽度、偏移量等信息
 *
 * @author chenlu
 * @date 2018/6/2
 */
public class CreateVariable extends AbstractSemanticAction {
    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int offset;

    public CreateVariable(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
