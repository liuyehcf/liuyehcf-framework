package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * @author hechenfeng
 * @date 2018/6/16
 */
public class BackToLoop extends AbstractSemanticAction {

    /**
     * 存储循环起始代码偏移量的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int loopStackOffset;

    public BackToLoop(int loopStackOffset) {
        this.loopStackOffset = loopStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        ControlTransfer __goto = new _goto();
        __goto.setCodeOffset(context.getStack().get(loopStackOffset).get(AttrName.LOOP_CODE_OFFSET.name()));
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(__goto);
    }
}
