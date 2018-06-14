package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

/**
 * @author chenlu
 * @date 2018/6/14
 */
public class NextBackFill extends AbstractSemanticAction {

    private static final int EXPRESSION_STACK_OFFSET = -9;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        ControlTransfer code = context.getStack().get(EXPRESSION_STACK_OFFSET).get(AttrName.NEXT_BYTE_CODE.name());

        code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
    }
}
