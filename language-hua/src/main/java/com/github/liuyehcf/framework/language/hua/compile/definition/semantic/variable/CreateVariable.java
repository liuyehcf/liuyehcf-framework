package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.variable;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.VariableSymbol;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull;

/**
 * 创建一个变量，记录类型、宽度、偏移量等信息
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public class CreateVariable extends AbstractSemanticAction {
    /**
     * 标志符-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int identifierStackOffset;

    public CreateVariable(int identifierStackOffset) {
        this.identifierStackOffset = identifierStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        String name = context.getValue(identifierStackOffset);
        Type type = context.getAttr(identifierStackOffset, AttrName.TYPE);

        VariableSymbol newVariableSymbol = context.createVariableSymbol(name, type);
        assertNotNull(newVariableSymbol, "[SYNTAX_ERROR] - Identifier '" + name + "' already exist, do not repeat definitions");
    }
}
