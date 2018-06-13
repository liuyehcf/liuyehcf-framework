package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.sl._ldc;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;

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
        switch (type) {
            case NORMAL_INT:
            case NORMAL_BOOLEAN:
                String literal = context.getStack().get(LITERAL_STACK_OFFSET).get(AttrName.LITERAL_VALUE.name());
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ldc(type, literal));
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
