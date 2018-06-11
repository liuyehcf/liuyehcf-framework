package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.bytecode._istore;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.production.AttrName;

/**
 * @author chenlu
 * @date 2018/6/10
 */
public class Assignment extends AbstractSemanticAction {
    private static final int OPERATOR_STACK_OFFSET = -1;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        SetAssignOperator.Operator operator = context.getStack().get(Assignment.OPERATOR_STACK_OFFSET).get(AttrName.ASSIGN_OPERATOR.name());
        switch (operator) {
            case NORMAL_ASSIGN:
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _istore());
                break;
        }
    }
}
