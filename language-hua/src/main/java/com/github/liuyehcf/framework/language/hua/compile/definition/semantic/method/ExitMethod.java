package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.method;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ByteCode;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ir.Return;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ir._return;

import java.util.List;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertTrue;

/**
 * 退出方法标记
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class ExitMethod extends AbstractSemanticAction {

    /**
     * 没有方法体
     */
    private final boolean hasMethodBody;

    public ExitMethod(boolean hasMethodBody) {
        this.hasMethodBody = hasMethodBody;
    }

    @Override
    public void onAction(CompilerContext context) {
        /*
         * 对于方法声明，跳过即可
         */
        if (hasMethodBody) {
            if (Type.TYPE_VOID.equals(context.getResultTypeOfCurrentMethod())) {
                context.addByteCodeToCurrentMethod(new _return());
            } else {
                List<ByteCode> byteCodes = context.getByteCodesOfOfCurrentMethod();
                assertTrue(!byteCodes.isEmpty() && (byteCodes.get(byteCodes.size() - 1) instanceof Return),
                        "[SYNTAX_ERROR] - Method lacks return statement");
            }
        }
        context.exitMethod(hasMethodBody);
    }
}
