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
public class TrueBackFill extends AbstractSemanticAction {
    private static final int EXPRESSION_STACK_OFFSET = -1;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        ControlTransfer code = context.getStack().get(EXPRESSION_STACK_OFFSET).get(AttrName.TRUE_BYTE_CODE.name());

        /*
         * 允许 `if(a) {...} ` 直接往下走，不需要回填值
         */
        if (code != null) {
            code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
        }
    }
}
