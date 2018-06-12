package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode._iastore;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * @author chenlu
 * @date 2018/6/12
 */
public class ArrayStore extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        // todo 类型校验
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iastore());
    }
}
