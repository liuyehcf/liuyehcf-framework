package org.liuyehcf.compile.engine.hua.semantic.load;

import org.liuyehcf.compile.engine.hua.bytecode.sl._iaload;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 加载数组
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class ArrayLoad extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        //todo 类型判断
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iaload());
    }
}
