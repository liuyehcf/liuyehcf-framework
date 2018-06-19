package org.liuyehcf.compile.engine.hua.semantic.variable;

import org.liuyehcf.compile.engine.core.cfg.lr.SyntaxNode;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

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
    public void onAction(HuaContext context) {

        SyntaxNode node = context.getStack().get(identifierStackOffset);

        String name = node.getValue();
        Type type = node.get(AttrName.TYPE.name());

        if (context.getHuaEngine().getVariableSymbolTable().enter(
                context.getHuaEngine().getOffset(), name, type) == null) {
            throw new RuntimeException("标志符 " + name + " 已存在，请勿重复定义");
        }

        context.getHuaEngine().increaseOffset(type.getTypeWidth());
    }
}
