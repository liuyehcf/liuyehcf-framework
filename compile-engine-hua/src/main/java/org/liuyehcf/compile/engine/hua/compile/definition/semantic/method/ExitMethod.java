package org.liuyehcf.compile.engine.hua.compile.definition.semantic.method;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir.Return;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir._return;

import java.io.Serializable;
import java.util.List;

/**
 * 退出方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class ExitMethod extends AbstractSemanticAction implements Serializable {

    /**
     * 没有方法体
     */
    private final boolean hasMethodBody;

    public ExitMethod(boolean hasMethodBody) {
        this.hasMethodBody = hasMethodBody;
    }

    @Override
    public void onAction(HuaContext context) {
        if (Type.TYPE_VOID.equals(context.getResultTypeOfCurrentMethod())) {
            context.addByteCodeToCurrentMethod(new _return());
        } else {
            List<ByteCode> byteCodes = context.getByteCodesOfOfCurrentMethod();
            if (byteCodes.isEmpty() || !(byteCodes.get(byteCodes.size() - 1) instanceof Return)) {
                throw new RuntimeException("Method lacks return statement");
            }
        }

        context.exitMethod(hasMethodBody);
    }
}
