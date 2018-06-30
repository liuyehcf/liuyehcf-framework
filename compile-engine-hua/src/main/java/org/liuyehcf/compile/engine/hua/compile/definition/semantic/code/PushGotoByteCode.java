package org.liuyehcf.compile.engine.hua.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf._goto;

import java.io.Serializable;

/**
 * 添加goto指令，跳转到指定的代码偏移量
 *
 * @author hechenfeng
 * @date 2018/6/16
 */
public class PushGotoByteCode extends AbstractSemanticAction implements Serializable {

    /**
     * 存储循环起始代码偏移量的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int loopStackOffset;

    public PushGotoByteCode(int loopStackOffset) {
        this.loopStackOffset = loopStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        ControlTransfer __goto = new _goto();
        __goto.setCodeOffset(context.getAttr(loopStackOffset, AttrName.CODE_OFFSET));
        context.addByteCodeToCurrentMethod(__goto);
    }
}
