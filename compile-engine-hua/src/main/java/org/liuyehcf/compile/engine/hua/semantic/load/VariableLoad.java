package org.liuyehcf.compile.engine.hua.semantic.load;

import org.liuyehcf.compile.engine.hua.bytecode.sl._aload;
import org.liuyehcf.compile.engine.hua.bytecode.sl._iload;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;

/**
 * 将标志符压入操作数栈
 *
 * @author hechenfeng
 * @date 2018/6/4
 */
public class VariableLoad extends AbstractSemanticAction {

    /**
     * 标志符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int identifierNameStackOffset;

    public VariableLoad(int identifierNameStackOffset) {
        this.identifierNameStackOffset = identifierNameStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        String identifierName = context.getStack().get(identifierNameStackOffset).get(AttrName.IDENTIFIER_NAME.name());
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
