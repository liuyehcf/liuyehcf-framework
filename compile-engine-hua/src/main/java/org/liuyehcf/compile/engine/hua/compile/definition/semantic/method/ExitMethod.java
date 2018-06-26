package org.liuyehcf.compile.engine.hua.compile.definition.semantic.method;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir._return;

import java.io.Serializable;

/**
 * 退出方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class ExitMethod extends AbstractSemanticAction implements Serializable {
    @Override
    public void onAction(HuaContext context) {
        if (Type.TYPE_VOID.equals(context.getResultTypeOfCurrentMethod())) {
            context.addByteCodeToCurrentMethod(new _return());
        }

        context.exitMethod();
    }
}
