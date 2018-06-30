package org.liuyehcf.compile.engine.hua.compile.definition.semantic.statement;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.VariableSymbol;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._astore;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._istore;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._lstore;

import java.io.Serializable;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * 变量初始化
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class VariableInitialization extends AbstractSemanticAction implements Serializable {

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

        assertNotNull(expressionType);
        assertNotNull(variableSymbol);
        if (!Type.isCompatible(variableSymbol.getType(), expressionType)) {
            throw new RuntimeException("Variable initialization statement on both sides of the type does not match");
        }

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
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }
}
