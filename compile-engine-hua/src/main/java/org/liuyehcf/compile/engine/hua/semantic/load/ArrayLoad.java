package org.liuyehcf.compile.engine.hua.semantic.load;

import org.liuyehcf.compile.engine.hua.bytecode.sl._aaload;
import org.liuyehcf.compile.engine.hua.bytecode.sl._iaload;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;

/**
 * 加载数组
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class ArrayLoad extends AbstractSemanticAction {

    /**
     * 数组访问表达式的语法树节点-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int arrayExpressionStackOffset;

    public ArrayLoad(int arrayExpressionStackOffset) {
        this.arrayExpressionStackOffset = arrayExpressionStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type type = context.getAttr(arrayExpressionStackOffset, AttrName.TYPE);

        if (type.isArrayType()) {
            context.addByteCodeToCurrentMethod(new _aaload());
        } else {
            switch (type.getTypeName()) {
                case NORMAL_BOOLEAN:
                case NORMAL_INT:
                    context.addByteCodeToCurrentMethod(new _iaload());
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }
}
