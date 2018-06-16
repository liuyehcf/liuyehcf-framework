package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 合并属性值，仅用于合并数值字面值
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class CombineAttr extends AbstractSemanticAction {

    /**
     * 主值-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int mainStackOffset;

    /**
     * 增值-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int subStackOffset;

    /**
     * 属性-名称
     */
    private final String attrName;

    public CombineAttr(int mainStackOffset, int subStackOffset, String attrName) {
        this.mainStackOffset = mainStackOffset;
        this.subStackOffset = subStackOffset;
        this.attrName = attrName;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String main = context.getStack().get(mainStackOffset).get(attrName);
        String sub = context.getStack().get(subStackOffset).get(attrName);

        main = main + sub;

        context.getStack().get(mainStackOffset).put(attrName, main);
    }
}
