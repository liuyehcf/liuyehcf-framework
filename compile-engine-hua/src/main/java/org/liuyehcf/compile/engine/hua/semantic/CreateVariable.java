package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.cfg.lr.AbstractLRCompiler;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;

/**
 * 创建一个变量，记录类型、宽度、偏移量等信息
 *
 * @author chenlu
 * @date 2018/6/2
 */
public class CreateVariable extends AbstractSemanticAction {
    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int stackOffset;

    public CreateVariable(int stackOffset) {
        this.stackOffset = stackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {

        AbstractLRCompiler.SyntaxNode node = context.getStack().get(stackOffset);

        String name = node.getValue();
        Type type = node.get(AttrName.TYPE.name());

        if (context.getHuaEngine().getVariableSymbolTable().enter(
                context.getHuaEngine().getOffset(), name, type) == null) {
            throw new RuntimeException("标志符 " + name + " 已存在，请勿重复定义");
        }

        context.getHuaEngine().increaseOffset(type.getTypeWidth());
    }
}
