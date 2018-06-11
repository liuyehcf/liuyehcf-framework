package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.bytecode._iinc;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.production.AttrName;

import java.util.ArrayList;
import java.util.List;

/**
 * 后置自减运算符
 *
 * @author chenlu
 * @date 2018/6/4
 */
public class PostDecrement extends AbstractSemanticAction {
    @Override
    @SuppressWarnings("unchecked")
    public void onAction(HuaCompiler.HuaContext context) {
        if (context.getStack().get(0).get(AttrName.CODES.name()) == null) {
            context.getStack().get(0).put(AttrName.CODES.name(), new ArrayList<>());
        }
        ((List<ByteCode>) context.getStack().get(0).get(AttrName.CODES.name())).add(new _iinc(1));
    }
}
