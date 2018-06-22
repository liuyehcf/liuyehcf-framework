package org.liuyehcf.compile.engine.hua.semantic.statement;

import org.liuyehcf.compile.engine.hua.bytecode.sl._astore;
import org.liuyehcf.compile.engine.hua.bytecode.sl._istore;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;

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
    public void onAction(HuaContext context) {
        Type expressionType = context.getStack().get(initializationExpressionStackOffset).get(AttrName.TYPE.name());
        String identifierName = context.getStack().get(identifierStackOffset).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        assertNotNull(expressionType);
        assertNotNull(variableSymbol);
        if (!expressionType.equals(variableSymbol.getType())) {
            throw new RuntimeException("变量初始化语句两侧类型不匹配");
        }

        Type identifierType = variableSymbol.getType();

        /*
         * 数组类型
         */
        if (identifierType.isArrayType()) {
            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _astore(variableSymbol.getOffset()));
        }
        /*
         * 非数组类型
         */
        else {
            switch (identifierType.getTypeName()) {
                case NORMAL_BOOLEAN:
                case NORMAL_INT:
                    context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _istore(variableSymbol.getOffset()));
                    break;
                default:
                    throw new RuntimeException("尚不支持类型 \'" + identifierType + "\'");
            }
        }
    }
}
