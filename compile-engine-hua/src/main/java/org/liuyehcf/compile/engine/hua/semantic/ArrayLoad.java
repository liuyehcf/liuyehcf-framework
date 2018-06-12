package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode._iaload;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * @author chenlu
 * @date 2018/6/12
 */
public class ArrayLoad extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        //todo 类型判断
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iaload());
    }
}
