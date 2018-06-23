package org.liuyehcf.compile.engine.hua.semantic.code;

import org.liuyehcf.compile.engine.hua.bytecode.ir._areturn;
import org.liuyehcf.compile.engine.hua.bytecode.ir._ireturn;
import org.liuyehcf.compile.engine.hua.bytecode.ir._return;
import org.liuyehcf.compile.engine.hua.compiler.HuaContext;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.Type;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.definition.Constant.NORMAL_INT;

/**
 * 增加return语句
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class PushReturnByteCode extends AbstractSemanticAction {

    /**
     * 表达式-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int expressionStackOffset;

    public PushReturnByteCode(int expressionStackOffset) {
        this.expressionStackOffset = expressionStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Object object = context.getStack().get(expressionStackOffset).get(AttrName.IS_EMPTY_EXPRESSION.name());
        Type type = context.getStack().get(expressionStackOffset).get(AttrName.TYPE.name());

        if (object != null) {
            context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _return());
        } else {
            assertNotNull(type);

            if (type.isArrayType()) {
                context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _areturn());
            } else {
                switch (type.getTypeName()) {
                    case NORMAL_BOOLEAN:
                    case NORMAL_INT:
                        context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().addByteCode(new _ireturn());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            }
        }
    }
}
