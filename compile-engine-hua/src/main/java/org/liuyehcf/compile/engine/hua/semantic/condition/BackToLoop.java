package org.liuyehcf.compile.engine.hua.semantic.condition;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.BackFillType;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.List;

/**
 * 回填到循环开始处
 *
 * @author chenlu
 * @date 2018/6/17
 */
public class BackToLoop extends AbstractSemanticAction {
    /**
     * 存储待回填字节码的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int backFillStackOffset;

    /**
     * 存储循环起始代码偏移量的节点-栈偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int loopStackOffset;

    /**
     * 回填类型
     */
    private final BackFillType backFillType;

    public BackToLoop(int backFillStackOffset, int loopStackOffset, BackFillType backFillType) {
        this.backFillStackOffset = backFillStackOffset;
        this.loopStackOffset = loopStackOffset;
        this.backFillType = backFillType;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        List<ControlTransfer> codes;

        switch (backFillType) {
            case TRUE:
                codes = context.getStack().get(backFillStackOffset).get(AttrName.TRUE_BYTE_CODE.name());
                break;
            default:
                throw new UnsupportedOperationException();
        }

        int codeOffset = context.getStack().get(loopStackOffset).get(AttrName.LOOP_CODE_OFFSET.name());

        for (ControlTransfer code : codes) {
            code.setCodeOffset(codeOffset);
        }
    }
}
