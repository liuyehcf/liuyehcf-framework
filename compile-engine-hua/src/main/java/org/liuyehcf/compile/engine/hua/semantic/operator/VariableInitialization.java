package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.bytecode.sl._istore;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;

/**
 * 变量初始化
 *
 * @author chenlu
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
    public void onAction(HuaCompiler.HuaContext context) {
        Type expressionType = context.getStack().get(initializationExpressionStackOffset).get(AttrName.TYPE.name());
        String identifierName = context.getStack().get(identifierStackOffset).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        if (!expressionType.equals(variableSymbol.getType())) {
            throw new RuntimeException("变量初始化语句两侧类型不匹配");
        }

        switch (variableSymbol.getType().getTypeName()) {
            case NORMAL_INT:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _istore(variableSymbol.getOffset()));
                break;
            case NORMAL_BOOLEAN:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _istore(variableSymbol.getOffset()));
                break;
            default:
                throw new RuntimeException("尚不支持类型 \'" + variableSymbol.getType() + "\'");
        }
    }
}
