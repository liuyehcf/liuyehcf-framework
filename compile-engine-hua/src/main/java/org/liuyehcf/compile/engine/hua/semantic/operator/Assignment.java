package org.liuyehcf.compile.engine.hua.semantic.operator;

import org.liuyehcf.compile.engine.hua.bytecode.sl._iastore;
import org.liuyehcf.compile.engine.hua.bytecode.sl._istore;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;
import static org.liuyehcf.compile.engine.hua.definition.GrammarDefinition.NORMAL_ASSIGN;

/**
 * 赋值
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class Assignment extends AbstractSemanticAction {

    /**
     * 表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionStackOffset;

    /**
     * 赋值运算符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int operatorStackOffset;

    /**
     * 赋值左侧-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int leftHandStackOffset;

    public Assignment(int expressionStackOffset, int operatorStackOffset, int leftHandStackOffset) {
        this.expressionStackOffset = expressionStackOffset;
        this.operatorStackOffset = operatorStackOffset;
        this.leftHandStackOffset = leftHandStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        String operator = context.getStack().get(operatorStackOffset).get(AttrName.ASSIGN_OPERATOR.name());
        String identifierName = context.getStack().get(leftHandStackOffset).get(AttrName.IDENTIFIER_NAME.name());
        VariableSymbol variableSymbol = context.getHuaEngine().getVariableSymbolTable().getVariableSymbolByName(identifierName);

        Type expressionType = context.getStack().get(expressionStackOffset).get(AttrName.TYPE.name());
        Type leftHandType = context.getStack().get(leftHandStackOffset).get(AttrName.TYPE.name());

        if (!expressionType.equals(leftHandType)) {
            throw new RuntimeException("赋值运算符左右侧类型不匹配");
        }

        if (variableSymbol.getType().isArrayType()) {
            switch (operator) {
                case NORMAL_ASSIGN:
                    switch (variableSymbol.getType().getTypeName()) {
                        case NORMAL_INT:
                            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _iastore());
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                    break;
                default:
                    throw new RuntimeException("尚不支持赋值运算符 \'" + operator + "\'");
            }

        } else {
            switch (operator) {
                case NORMAL_ASSIGN:
                    switch (variableSymbol.getType().getTypeName()) {
                        case NORMAL_INT:
                            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _istore(variableSymbol.getOffset()));
                            break;
                        case NORMAL_BOOLEAN:
                            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _istore(variableSymbol.getOffset()));
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                    break;
                default:
                    throw new RuntimeException("尚不支持赋值运算符 \'" + operator + "\'");
            }
        }
    }
}
