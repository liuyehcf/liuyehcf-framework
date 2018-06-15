package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.bytecode.sl._iconst;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/15
 */
public class BooleanAssign extends AbstractSemanticAction {

    private static final int EXPRESSION_STACK_OFFSET = 0;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        Object obj = context.getStack().get(EXPRESSION_STACK_OFFSET).get(AttrName.COMPLEX_BOOLEAN_EXPRESSION.name());

        if (obj == null) {
            return;
        }

        List<ControlTransfer> codes;

        /*
         * TRUE回填
         */
        codes = context.getStack().get(EXPRESSION_STACK_OFFSET).get(AttrName.TRUE_BYTE_CODE.name());
        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
            }
            codes.clear();
        }

        /*
         * 压入1，代表true
         */
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iconst(1));


        /*
         * 压入goto
         */
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _goto());

        /*
         * FALSE回填
         */
        codes = context.getStack().get(EXPRESSION_STACK_OFFSET).get(AttrName.FALSE_BYTE_CODE.name());
        if (codes != null) {
            for (ControlTransfer code : codes) {
                code.setCodeOffset(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size());
            }
            codes.clear();
        }

        /*
         * 压入0，代表false
         */
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iconst(0));
    }
}
