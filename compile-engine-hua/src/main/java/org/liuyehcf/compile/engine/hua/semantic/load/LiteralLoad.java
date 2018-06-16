package org.liuyehcf.compile.engine.hua.semantic.load;

import org.liuyehcf.compile.engine.hua.bytecode.sl._iconst;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.hua.definition.Constant.*;

/**
 * 加载字面值
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class LiteralLoad extends AbstractSemanticAction {

    /**
     * 字面值-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int literalStackOffset;

    /**
     * 字面值类型
     */
    private final String type;

    public LiteralLoad(int literalStackOffset, String type) {
        this.literalStackOffset = literalStackOffset;
        this.type = type;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String literal;
        switch (type) {
            case NORMAL_INT:
                literal = context.getStack().get(literalStackOffset).get(AttrName.LITERAL_VALUE.name());
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iconst(Integer.parseInt(literal)));
                break;
            case NORMAL_BOOLEAN:
                /*
                 * 布尔字面值也作为int处理
                 */
                literal = context.getStack().get(literalStackOffset).get(AttrName.LITERAL_VALUE.name());
                assertTrue(NORMAL_BOOLEAN_TRUE.equals(literal) || NORMAL_BOOLEAN_FALSE.equals(literal));
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iconst(NORMAL_BOOLEAN_TRUE.equals(literal) ? 1 : 0));
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
