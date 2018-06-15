package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.sl._iconst;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.hua.definition.Constant.*;

/**
 * @author chenlu
 * @date 2018/6/11
 */
public class PushLiteralToOperatorStack extends AbstractSemanticAction {

    private final static int LITERAL_STACK_OFFSET = 0;

    private final String type;

    public PushLiteralToOperatorStack(String type) {
        this.type = type;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String literal;
        switch (type) {
            case NORMAL_INT:
                literal = context.getStack().get(LITERAL_STACK_OFFSET).get(AttrName.LITERAL_VALUE.name());
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iconst(Integer.parseInt(literal)));
                break;
            case NORMAL_BOOLEAN:
                /*
                 * 布尔字面值也作为int处理
                 */
                literal = context.getStack().get(LITERAL_STACK_OFFSET).get(AttrName.LITERAL_VALUE.name());
                assertTrue(NORMAL_BOOLEAN_TRUE.equals(literal) || NORMAL_BOOLEAN_FALSE.equals(literal));
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iconst(NORMAL_BOOLEAN_TRUE.equals(literal) ? 1 : 0));
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
