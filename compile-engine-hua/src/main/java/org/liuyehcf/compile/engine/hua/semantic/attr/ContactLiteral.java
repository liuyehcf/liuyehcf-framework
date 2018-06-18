package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 拼接字面值
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class ContactLiteral extends AbstractSemanticAction {

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
    private final AttrName attrName;

    public ContactLiteral(int mainStackOffset, int subStackOffset, AttrName attrName) {
        this.mainStackOffset = mainStackOffset;
        this.subStackOffset = subStackOffset;
        this.attrName = attrName;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String main = context.getStack().get(mainStackOffset).get(attrName.name());
        String sub = context.getStack().get(subStackOffset).get(attrName.name());

        main = main + sub;

        context.getStack().get(mainStackOffset).put(attrName.name(), main);
    }
}