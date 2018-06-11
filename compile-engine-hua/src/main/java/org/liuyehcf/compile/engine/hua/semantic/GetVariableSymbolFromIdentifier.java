package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode._iload;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;

/**
 * 查找标志符对应的地址，并将其作为属性值存入语法树节点
 *
 * @author chenlu
 * @date 2018/6/4
 */
public class GetVariableSymbolFromIdentifier extends AbstractSemanticAction {
    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String identifierName = context.getStack().get(0).getValue();
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);
        if (variableSymbol == null) {
            throw new RuntimeException("标志符 " + identifierName + " 尚未定义");
        }

        switch (variableSymbol.getType()) {
            case "int":
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                break;
            case "boolean":
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                break;
        }
    }
}
