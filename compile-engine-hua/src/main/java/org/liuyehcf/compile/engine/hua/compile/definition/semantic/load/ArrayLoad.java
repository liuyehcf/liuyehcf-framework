package org.liuyehcf.compile.engine.hua.compile.definition.semantic.load;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl.*;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * 加载数组
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class ArrayLoad extends AbstractSemanticAction implements Serializable {

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
                    context.addByteCodeToCurrentMethod(new _baload());
                    break;
                case NORMAL_CHAR:
                    context.addByteCodeToCurrentMethod(new _caload());
                    break;
                case NORMAL_INT:
                    context.addByteCodeToCurrentMethod(new _iaload());
                    break;
                case NORMAL_LONG:
                    context.addByteCodeToCurrentMethod(new _laload());
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }
}
