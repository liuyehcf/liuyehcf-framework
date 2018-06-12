package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.production.AttrName;

import static org.liuyehcf.compile.engine.hua.util.TypeUtil.isArrayType;

/**
 * 数组类型降维
 *
 * @author chenlu
 * @date 2018/6/12
 */
public class ArrayTypeDimDecrease extends AbstractSemanticAction {

    private final static int EXPRESSION_NAME_STACK_OFFSET = -4;

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String type = context.getStack().get(EXPRESSION_NAME_STACK_OFFSET).get(AttrName.TYPE.name());

        if (!isArrayType(type)) {
            throw new RuntimeException("数组维度不足");
        }

        context.getLeftNode().put(AttrName.TYPE.name(), type.substring(0, type.length() - 2));
    }
}
