package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode._istore;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.production.AttrName;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.NORMAL_ASSIGN;

/**
 * @author chenlu
 * @date 2018/6/10
 */
public class Assignment extends AbstractSemanticAction {

    private static final int EXPRESSION_STACK_OFFSET = 0;

    private static final int OPERATOR_STACK_OFFSET = -1;

    private static final int LEFT_HAND_STACK_OFFSET = -2;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String operator = context.getStack().get(Assignment.OPERATOR_STACK_OFFSET).get(AttrName.ASSIGN_OPERATOR.name());
        String identifierName = context.getStack().get(LEFT_HAND_STACK_OFFSET).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        String expressionType = context.getStack().get(EXPRESSION_STACK_OFFSET).get(AttrName.TYPE.name());

        if (!expressionType.equals(variableSymbol.getType())) {
            throw new RuntimeException("赋值运算符左右侧类型不匹配");
        }

        switch (operator) {
            case NORMAL_ASSIGN:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _istore(variableSymbol.getOffset()));
                break;
        }
    }
}
