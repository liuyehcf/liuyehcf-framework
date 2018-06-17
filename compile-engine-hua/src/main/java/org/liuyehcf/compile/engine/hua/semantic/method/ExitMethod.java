package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.bytecode.rt._return;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;

/**
 * 退出方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class ExitMethod extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        if (Type.TYPE_VOID.equals(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getResultType())) {
            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _return());
        }

        context.getHuaEngine().getMethodInfoTable().exitMethod();
        context.getHuaEngine().resetOffset();
        context.getHuaEngine().getStatusInfo().getUncertainCodes().clear();
        assertTrue(context.getHuaEngine().getStatusInfo().getIfNestedLevel() == 0);
    }
}
