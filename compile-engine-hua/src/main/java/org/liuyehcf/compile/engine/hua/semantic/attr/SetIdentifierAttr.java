package org.liuyehcf.compile.engine.hua.semantic.attr;

import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.compiler.VariableSymbol;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

/**
 * 根据标志符名称，设置IDENTIFIER_NAME、TYPE属性
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class SetIdentifierAttr extends AbstractSemanticAction {

    /**
     * 标志符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int identifierStackOffset;

    public SetIdentifierAttr(int identifierStackOffset) {
        this.identifierStackOffset = identifierStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        String identifierName = context.getValue(identifierStackOffset);
        VariableSymbol variableSymbol = context.getVariableSymbolByName(identifierName);

        context.setAttrToLeftNode(AttrName.IDENTIFIER_NAME, identifierName);
        context.setAttrToLeftNode(AttrName.TYPE, variableSymbol.getType());
    }
}
