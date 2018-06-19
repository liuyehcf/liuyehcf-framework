package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 添加goto指令，跳转到指定的代码偏移量
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class GotoLoop extends AbstractSemanticAction {

    /**
     * 存储循环起始代码偏移量的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int loopStackOffset;

    public GotoLoop(int loopStackOffset) {
        this.loopStackOffset = loopStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        ControlTransfer __goto = new _goto();
        __goto.setCodeOffset(context.getStack().get(loopStackOffset).get(AttrName.LOOP_CODE_OFFSET.name()));
        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(__goto);
    }
}
