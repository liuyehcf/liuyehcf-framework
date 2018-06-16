package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNull;

/**
 * 属性赋值
 *
 * @author chenlu
 * @date 2018/6/15
 */
public class AssignAttrs extends AbstractSemanticAction {

    /**
     * 源属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int fromStackOffset;

    /**
     * 宿属性-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示即将入栈的元素，以此类推
     */
    private final int toStackOffset;

    /**
     * 属性名称集合
     */
    private final AttrName[] attrNames;

    public AssignAttrs(int fromStackOffset, int toStackOffset, AttrName... attrNames) {
        this.fromStackOffset = fromStackOffset;
        this.toStackOffset = toStackOffset;
        this.attrNames = attrNames;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        for (AttrName attrName : attrNames) {
            Object value = context.getStack().get(fromStackOffset).get(attrName.name());
            if (value == null) {
                continue;
            }
            Object origin = context.getStack().get(toStackOffset).putIfAbsent(attrName.name(), value);
            assertNull(origin);
        }
    }
}
