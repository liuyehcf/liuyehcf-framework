package org.liuyehcf.compile.engine.hua.compile.definition.semantic.load;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.VariableSymbol;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.sm._dup2;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition.NORMAL_ASSIGN;

/**
 * 必要时添加load指令
 *
 * @author hechenfeng
 * @date 2018/6/20
 */
public class VariableLoadIfNecessary extends AbstractSemanticAction {
    /**
     * 赋值左侧-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int leftHandStackOffset;

    /**
     * 赋值运算符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int operatorStackOffset;

    public VariableLoadIfNecessary(int leftHandStackOffset, int operatorStackOffset) {
        this.leftHandStackOffset = leftHandStackOffset;
        this.operatorStackOffset = operatorStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        String operator = context.getAttr(operatorStackOffset, AttrName.ASSIGN_OPERATOR);
        String identifierName = context.getAttr(leftHandStackOffset, AttrName.IDENTIFIER_NAME);
        Type leftHandType = context.getAttr(leftHandStackOffset, AttrName.TYPE);

        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        assertNotNull(variableSymbol, "[SYNTAX_ERROR] - Non-variables cannot be assigned");

        Type identifierType = variableSymbol.getType();

        switch (operator) {
            case NORMAL_ASSIGN:
                break;
            case NORMAL_MUL_ASSIGN:
            case NORMAL_DIV_ASSIGN:
            case NORMAL_REM_ASSIGN:
            case NORMAL_ADD_ASSIGN:
            case NORMAL_SUB_ASSIGN:
            case NORMAL_SHL_ASSIGN:
            case NORMAL_SHR_ASSIGN:
            case NORMAL_USHR_ASSIGN:
            case NORMAL_BIT_AND_ASSIGN:
            case NORMAL_BIT_XOR_ASSIGN:
            case NORMAL_BIT_OR_ASSIGN:
                assertFalse(leftHandType.isArrayType(), "[SYSTEM_ERROR] - Complex assign cannot support array type");

                /*
                 * 标志符为数组类型
                 */
                if (identifierType.isArrayType()) {

                    context.addByteCodeToCurrentMethod(new _dup2());

                    switch (leftHandType.getTypeName()) {
                        case NORMAL_BOOLEAN:
                            context.addByteCodeToCurrentMethod(new _baload());
                            break;
                        case NORMAL_CHAR:
                            context.addByteCodeToCurrentMethod(new _caload());
                            break;
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _iaload());
                            break;
                        case NORMAL_LONG:
                            context.addByteCodeToCurrentMethod(new _laload());
                            break;
                        case NORMAL_FLOAT:
                            context.addByteCodeToCurrentMethod(new _faload());
                            break;
                        case NORMAL_DOUBLE:
                            context.addByteCodeToCurrentMethod(new _daload());
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                    context.setAttr(leftHandStackOffset, AttrName.TYPE, leftHandType);
                }
                /*
                 * 标志符为非数组类型
                 */
                else {
                    switch (leftHandType.getTypeName()) {
                        case NORMAL_CHAR:
                        case NORMAL_INT:
                            context.addByteCodeToCurrentMethod(new _iload(variableSymbol.getOrder()));
                            break;
                        case NORMAL_LONG:
                            context.addByteCodeToCurrentMethod(new _lload(variableSymbol.getOrder()));
                            break;
                        case NORMAL_FLOAT:
                            context.addByteCodeToCurrentMethod(new _fload(variableSymbol.getOrder()));
                            break;
                        case NORMAL_DOUBLE:
                            context.addByteCodeToCurrentMethod(new _dload(variableSymbol.getOrder()));
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                    context.setAttr(leftHandStackOffset, AttrName.TYPE, leftHandType);
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
