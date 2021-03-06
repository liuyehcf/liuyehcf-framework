package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.statement;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.VariableSymbol;
import com.github.liuyehcf.framework.language.hua.core.bytecode.cp._sizeof;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;

/**
 * sizeof语句
 *
 * @author hechenfeng
 * @date 2018/6/28
 */
public class SizeofStatement extends AbstractSemanticAction {

    /**
     * 标志符名称-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int identifierNameStackOffset;

    public SizeofStatement(int identifierNameStackOffset) {
        this.identifierNameStackOffset = identifierNameStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        String identifierName = context.getAttr(identifierNameStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        Type type = variableSymbol.getType();

        assertTrue(type.isArrayType(), "[SYNTAX_ERROR] - Operator 'sizeof' cannot work on non-array type");

        context.addByteCodeToCurrentMethod(new _sizeof(variableSymbol.getOrder()));

        context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_INT);
    }
}
