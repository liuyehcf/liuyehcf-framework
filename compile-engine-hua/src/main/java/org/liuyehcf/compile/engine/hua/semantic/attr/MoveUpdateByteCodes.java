package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 将for-update中的语句移动到尾部
 *
 * @author chenlu
 * @date 2018/6/19
 */
public class MoveUpdateByteCodes extends AbstractSemanticAction {
    /**
     * 存储开始代码偏移量的语法树节点-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int startCodeOffsetStackOffset;

    /**
     * 存储结束代码偏移量的语法树节点-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int endCodeOffsetStackOffset;

    public MoveUpdateByteCodes(int startCodeOffsetStackOffset, int endCodeOffsetStackOffset) {
        this.startCodeOffsetStackOffset = startCodeOffsetStackOffset;
        this.endCodeOffsetStackOffset = endCodeOffsetStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        int start = context.getStack().get(startCodeOffsetStackOffset).get(AttrName.CODE_OFFSET.name());
        int end = context.getStack().get(endCodeOffsetStackOffset).get(AttrName.CODE_OFFSET.name());

        /*
         * update部分为空
         */
        if (start == end) {
            return;
        }

        List<ByteCode> updateCodes = new ArrayList<>();

        /*
         * 取出这部分ByteCode
         */
        for (int i = start; i < end; i++) {
            updateCodes.add(context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().get(start));
            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes().remove(start);
        }

        /*
         * 追加到尾部
         */
        updateCodes.forEach((code) -> context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(code));
    }
}
