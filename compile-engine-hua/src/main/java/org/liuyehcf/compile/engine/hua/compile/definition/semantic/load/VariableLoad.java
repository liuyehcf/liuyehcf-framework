package org.liuyehcf.compile.engine.hua.compile.definition.semantic.load;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.VariableSymbol;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._aload;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._iload;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.NORMAL_INT;

/**
 * 将标志符压入操作数栈
 *
 * @author hechenfeng
 * @date 2018/6/4
 */
public class VariableLoad extends AbstractSemanticAction implements Serializable {

    /**
     * 标志符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int identifierNameStackOffset;

    public VariableLoad(int identifierNameStackOffset) {
        this.identifierNameStackOffset = identifierNameStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        String identifierName = context.getAttr(identifierNameStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);
        if (variableSymbol == null) {
            throw new RuntimeException("Identifier " + identifierName + " undefined");
        }

        Type type = variableSymbol.getType();

        if (type.isArrayType()) {

            context.addByteCodeToCurrentMethod(new _aload(variableSymbol.getOrder()));
            context.setAttrToLeftNode(AttrName.TYPE, type);

        } else {
            switch (type.getTypeName()) {
                case NORMAL_BOOLEAN:
                case NORMAL_INT:
                    context.addByteCodeToCurrentMethod(new _iload(variableSymbol.getOrder()));
                    context.setAttrToLeftNode(AttrName.TYPE, type);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }
}
