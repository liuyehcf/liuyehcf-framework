package org.liuyehcf.compile.engine.hua.semantic.load;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.bytecode.sl._iload;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.model.AttrName;
import org.liuyehcf.compile.engine.hua.model.StatementType;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;

/**
 * 移除多余的load字节码
 *
 * @author hechenfeng
 * @date 2018/6/18
 */
public class RemoveRedundantLoadByteCode extends AbstractSemanticAction {
    /**
     * 语句-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int statementStackOffset;

    public RemoveRedundantLoadByteCode(int statementStackOffset) {
        this.statementStackOffset = statementStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        StatementType type = context.getStack().get(statementStackOffset).get(AttrName.STATEMTNT_TYPE.name());

        assertNotNull(type);


        List<ByteCode> byteCodes = context.getHuaEngine().getMethodInfoTable().getCurMethodInfo().getByteCodes();
        ByteCode code;

        switch (type) {
            case PRE_DECREMENT:
            case PRE_INCREMENT:
                /*
                 * 对于 ++i; 这样的语句，iload指令是多余的，删掉即可
                 */
                code = byteCodes.get(byteCodes.size() - 1);
                assertTrue(code instanceof _iload);
                byteCodes.remove(byteCodes.size() - 1);
                break;
            case POST_DECREMENT:
            case POST_INCREMENT:
                /*
                 * 对于 i++; 这样的语句，iload指令是多余的，删掉即可
                 */
                code = byteCodes.get(byteCodes.size() - 2);
                assertTrue(code instanceof _iload);
                byteCodes.remove(byteCodes.size() - 2);
                break;
            case ASSIGNMENT:
                break;
            case METHOD_INVOCATION:
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
