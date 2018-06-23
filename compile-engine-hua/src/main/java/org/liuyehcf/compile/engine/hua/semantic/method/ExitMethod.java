package org.liuyehcf.compile.engine.hua.semantic.method;

import org.liuyehcf.compile.engine.hua.bytecode.ir._return;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 退出方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class ExitMethod extends AbstractSemanticAction {
    @Override
    public void onAction(HuaContext context) {
        if (Type.TYPE_VOID.equals(context.getResultTypeOfCurrentMethod())) {
            context.addByteCodeToCurrentMethod(new _return());
        }

        context.exitMethod();
        context.resetOffset();
    }
}
