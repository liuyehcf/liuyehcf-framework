package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.code;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.StatementType;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ByteCode;
import com.github.liuyehcf.framework.language.hua.core.bytecode.sl._iload;

import java.util.List;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull;
import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;

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
    public void onAction(CompilerContext context) {
        StatementType type = context.getAttr(statementStackOffset, AttrName.STATEMENT_TYPE);

        assertNotNull(type, "[SYSTEM_ERROR] - Statement type of SemanticAction 'RemoveRedundantLoadByteCode' cannot be null");


        List<ByteCode> byteCodes = context.getByteCodesOfOfCurrentMethod();
        ByteCode code;

        switch (type) {
            case PRE_DECREMENT:
            case PRE_INCREMENT:
                /*
                 * 对于 ++i; 这样的语句，iload指令是多余的，删掉即可
                 */
                code = byteCodes.get(byteCodes.size() - 1);
                assertTrue(code instanceof _iload, "[SYSTEM_ERROR] - Redundant bytecode is not 'iload'");
                byteCodes.remove(byteCodes.size() - 1);
                break;
            case POST_DECREMENT:
            case POST_INCREMENT:
                /*
                 * 对于 i++; 这样的语句，iload指令是多余的，删掉即可
                 */
                code = byteCodes.get(byteCodes.size() - 2);
                assertTrue(code instanceof _iload, "[SYSTEM_ERROR] - Redundant bytecode is not 'iload'");
                byteCodes.remove(byteCodes.size() - 2);
                break;
            case ASSIGNMENT:
            case METHOD_INVOCATION:
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
