package org.liuyehcf.compile.engine.hua.definition.semantic.attr;

import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;

/**
 * 拼接字面值
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class ContactLiteral extends AbstractSemanticAction implements Serializable {

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
    public void onAction(HuaContext context) {
        String main = context.getAttr(mainStackOffset, attrName);
        String sub = context.getAttr(subStackOffset, attrName);

        main = main + sub;

        context.setAttr(mainStackOffset, attrName, main);
    }
}
