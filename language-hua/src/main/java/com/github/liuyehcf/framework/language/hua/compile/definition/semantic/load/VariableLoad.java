package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.load;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.VariableSymbol;
import com.github.liuyehcf.framework.language.hua.core.bytecode.sl.*;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.*;

/**
 * 将标志符压入操作数栈
 *
 * @author hechenfeng
 * @date 2018/6/4
 */
public class VariableLoad extends AbstractSemanticAction {

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
    public void onAction(CompilerContext context) {
        String identifierName = context.getAttr(identifierNameStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);
        assertNotNull(variableSymbol, "[SYNTAX_ERROR] - Identifier " + identifierName + " undefined");

        Type type = variableSymbol.getType();

        if (type.isArrayType()) {

            context.addByteCodeToCurrentMethod(new _aload(variableSymbol.getOrder()));
            context.setAttrToLeftNode(AttrName.TYPE, type);

        } else {
            switch (type.getTypeName()) {
                case NORMAL_BOOLEAN:
                case NORMAL_CHAR:
                case NORMAL_INT:
                    context.addByteCodeToCurrentMethod(new _iload(variableSymbol.getOrder()));
                    break;
                case NORMAL_LONG:
                    context.addByteCodeToCurrentMethod(new _lload(variableSymbol.getOrder()));
                    break;
                case NORMAL_FLOAT:
                    context.addByteCodeToCurrentMethod(new _fload(variableSymbol.getOrder()));
                    break;
                case NORMAL_DOUBLE:
                    context.addByteCodeToCurrentMethod(new _dload(variableSymbol.getOrder()));
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
            context.setAttrToLeftNode(AttrName.TYPE, type);
        }
    }
}
