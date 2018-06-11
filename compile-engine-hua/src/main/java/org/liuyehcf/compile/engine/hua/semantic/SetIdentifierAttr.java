package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.production.AttrName;

/**
 * @author chenlu
 * @date 2018/6/11
 */
public class SetIdentifierAttr extends AbstractSemanticAction {

    private static final int VARIABLE_STACK_OFFSET = 0;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String identifierName = context.getStack().get(VARIABLE_STACK_OFFSET).getValue();
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        context.getLeftNode().put(AttrName.IDENTIFIER_NAME.name(), identifierName);
        context.getLeftNode().put(AttrName.TYPE.name(), variableSymbol.getType());
    }
}
