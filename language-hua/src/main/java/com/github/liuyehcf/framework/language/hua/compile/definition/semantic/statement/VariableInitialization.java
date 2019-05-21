package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.statement;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.VariableSymbol;
import com.github.liuyehcf.framework.language.hua.core.bytecode.sl.*;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertNotNull;
import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertTrue;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.*;

/**
 * 变量初始化
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class VariableInitialization extends AbstractSemanticAction {

    /**
     * 初始化表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int initializationExpressionStackOffset;

    /**
     * 标志符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int identifierStackOffset;

    public VariableInitialization(int initializationExpressionStackOffset, int identifierStackOffset) {
        this.initializationExpressionStackOffset = initializationExpressionStackOffset;
        this.identifierStackOffset = identifierStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Type expressionType = context.getAttr(initializationExpressionStackOffset, AttrName.TYPE);
        String identifierName = context.getAttr(identifierStackOffset, AttrName.IDENTIFIER_NAME);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        assertNotNull(expressionType, "[SYSTEM_ERROR] - Expression type of SemanticAction 'VariableInitialization' cannot be null");
        assertNotNull(variableSymbol, "[SYSTEM_ERROR] - VariableSymbol type of SemanticAction 'VariableInitialization' cannot be null");
        assertTrue(Type.isCompatible(variableSymbol.getType(), expressionType),
                "[SYNTAX_ERROR] - Variable initialization statement on both sides of the type does not match, " +
                        "variableSymbol type is '" + variableSymbol.getType().toTypeDescription() + "', " +
                        "expression type is '" + expressionType.toTypeDescription() + "'");

        Type identifierType = variableSymbol.getType();

        /*
         * 数组类型
         */
        if (identifierType.isArrayType()) {
            context.addByteCodeToCurrentMethod(new _astore(variableSymbol.getOrder()));
        }
        /*
         * 非数组类型
         */
        else {
            switch (identifierType.getTypeName()) {
                case NORMAL_BOOLEAN:
                case NORMAL_CHAR:
                case NORMAL_INT:
                    context.addByteCodeToCurrentMethod(new _istore(variableSymbol.getOrder()));
                    break;
                case NORMAL_LONG:
                    context.addByteCodeToCurrentMethod(new _lstore(variableSymbol.getOrder()));
                    break;
                case NORMAL_FLOAT:
                    context.addByteCodeToCurrentMethod(new _fstore(variableSymbol.getOrder()));
                    break;
                case NORMAL_DOUBLE:
                    context.addByteCodeToCurrentMethod(new _dstore(variableSymbol.getOrder()));
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }
}
