package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.load;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.bytecode.sl.*;

import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.*;

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
    public void onAction(CompilerContext context) {
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
                case NORMAL_FLOAT:
                    context.addByteCodeToCurrentMethod(new _faload());
                    break;
                case NORMAL_DOUBLE:
                    context.addByteCodeToCurrentMethod(new _daload());
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }
}
