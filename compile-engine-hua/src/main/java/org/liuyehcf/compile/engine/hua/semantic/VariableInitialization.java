package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode._istore;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.production.AttrName;

/**
 * @author chenlu
 * @date 2018/6/11
 */
public class VariableInitialization extends AbstractSemanticAction {
    private static final int INITIALIZATION_EXPRESSION_STACK_OFFSET = 0;
    private static final int VARIABLE_ID_STACK_OFFSET = -2;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String expressionType = context.getStack().get(INITIALIZATION_EXPRESSION_STACK_OFFSET).get(AttrName.TYPE.name());
        String identifierName = context.getStack().get(VARIABLE_ID_STACK_OFFSET).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        if (!expressionType.equals(variableSymbol.getType())) {
            throw new RuntimeException("变量初始化语句两侧类型不匹配");
        }

        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _istore(variableSymbol.getOffset()));
    }
}
