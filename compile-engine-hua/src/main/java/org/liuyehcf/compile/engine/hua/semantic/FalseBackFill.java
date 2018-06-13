package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

/**
 * 布尔值回填
 *
 * @author chenlu
 * @date 2018/6/13
 */
public class FalseBackFill extends AbstractSemanticAction {

    private static final int EXPRESSION_STACK_OFFSET = -4;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        ControlTransfer code = context.getStack().get(EXPRESSION_STACK_OFFSET).get(AttrName.FALSE_BYTE_CODE.name());
        code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
    }
}
