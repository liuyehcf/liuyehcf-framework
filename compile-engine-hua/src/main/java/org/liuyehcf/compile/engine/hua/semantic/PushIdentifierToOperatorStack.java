package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode.sl._aload;
import org.liuyehcf.compile.engine.hua.bytecode.sl._iload;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;

import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;

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

        Type type = variableSymbol.getType();

        if (type.isArrayType()) {

            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _aload(variableSymbol.getOffset()));
            context.getLeftNode().put(AttrName.TYPE.name(), type);

        } else {
            switch (type.getTypeName()) {
                case NORMAL_INT:
                    context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                    context.getLeftNode().put(AttrName.TYPE.name(), type);
                    break;
                case NORMAL_BOOLEAN:
                    context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iload(variableSymbol.getOffset()));
                    context.getLeftNode().put(AttrName.TYPE.name(), type);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }
}
