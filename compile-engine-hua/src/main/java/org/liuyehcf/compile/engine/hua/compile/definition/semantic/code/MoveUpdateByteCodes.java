package org.liuyehcf.compile.engine.hua.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf.ControlTransfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 将for-update中的语句移动到尾部
 *
 * @author hechenfeng
 * @date 2018/6/19
 */
public class MoveUpdateByteCodes extends AbstractSemanticAction implements Serializable {
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
    public void onAction(CompilerContext context) {
        int start = context.getAttr(startCodeOffsetStackOffset, AttrName.CODE_OFFSET);
        int end = context.getAttr(endCodeOffsetStackOffset, AttrName.CODE_OFFSET);

        List<ByteCode> codes = context.getByteCodesOfOfCurrentMethod();

        /*
         * update部分为空
         */
        if (start == end) {
            return;
        }

        List<ByteCode> updateCodes = new ArrayList<>();


        /*
         * 取出这部分ByteCode  [start,end)
         */
        for (int i = start; i < end; i++) {
            updateCodes.add(codes.get(start));
            codes.remove(start);
        }

        /*
         * 追加到尾部
         */
        codes.addAll(updateCodes);


        /*
         * 维护跳转字节码的跳转偏移量
         */
        for (ByteCode code : codes) {
            if (!(code instanceof ControlTransfer)) {
                continue;
            }

            ControlTransfer controlTransfer = (ControlTransfer) code;

            if (controlTransfer.getCodeOffset() < start) {
                // 跳转偏移量在 [0,start)，不受影响
                continue;
            }

            if (controlTransfer.getCodeOffset() >= end) {
                // 跳转偏移量在 [start,end)，那么减少 L = end — start
                controlTransfer.setCodeOffset(controlTransfer.getCodeOffset() - (end - start));
            } else {
                // 跳转偏移量在 [start,end)，那么增加 L = size() - end
                controlTransfer.setCodeOffset(controlTransfer.getCodeOffset() + (codes.size() - end));
            }
        }
    }
}
