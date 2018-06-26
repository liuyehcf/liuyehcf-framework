package org.liuyehcf.compile.engine.hua.definition.semantic.method;

import org.liuyehcf.compile.engine.hua.bytecode.ir._return;
import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.model.Type;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

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
