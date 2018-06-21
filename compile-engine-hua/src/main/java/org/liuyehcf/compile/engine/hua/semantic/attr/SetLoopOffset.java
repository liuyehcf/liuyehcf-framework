package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 设置循环偏移量，即code编号
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class SetLoopOffset extends AbstractSemanticAction {

    /**
     * 存储循环起始代码偏移量的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int loopStackOffset;

    public SetLoopOffset(int loopStackOffset) {
        this.loopStackOffset = loopStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        int codeOffset = context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().size();
        context.getStack().get(loopStackOffset).put(AttrName.CODE_OFFSET.name(), codeOffset);
    }
}
