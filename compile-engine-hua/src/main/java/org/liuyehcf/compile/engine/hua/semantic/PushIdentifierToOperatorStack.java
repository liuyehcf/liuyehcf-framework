package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode._iload;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.production.AttrName;

import static org.liuyehcf.compile.engine.hua.production.Type.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.production.Type.NORMAL_INT;

/**
 * 将标志符压入操作数栈
 *
 * @author chenlu
 * @date 2018/6/4
 */
public class PushIdentifierToOperatorStack extends AbstractSemanticAction {

    private static final int IDENTIFIER_NAME_STACK_OFFSET = 0;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String identifierName = context.getStack().get(IDENTIFIER_NAME_STACK_OFFSET).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);
        if (variableSymbol == null) {
            throw new RuntimeException("标志符 " + identifierName + " 尚未定义");
        }

        switch (variableSymbol.getType()) {
            case NORMAL_INT:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                context.getLeftNode().put(AttrName.TYPE.name(), NORMAL_INT);
                break;
            case NORMAL_BOOLEAN:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                context.getLeftNode().put(AttrName.TYPE.name(), NORMAL_BOOLEAN);
                break;
        }
    }
}
