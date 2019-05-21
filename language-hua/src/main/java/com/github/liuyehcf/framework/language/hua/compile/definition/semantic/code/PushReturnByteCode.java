package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.code;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ir.*;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertEquals;
import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertNotNull;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.*;

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
    public void onAction(CompilerContext context) {
        Object object = context.getAttr(expressionStackOffset, AttrName.IS_EMPTY_EXPRESSION);
        Type type = context.getAttr(expressionStackOffset, AttrName.TYPE);
        Type resultType = context.getResultTypeOfCurrentMethod();

        assertEquals(type, resultType, "[SYNTAX_ERROR] - The return type returned by the return statement does not match the return type of the method declaration");

        if (object != null) {
            context.addByteCodeToCurrentMethod(new _return());
        } else {
            assertNotNull(type, "[SYSTEM_ERROR] - Expression type of SemanticAction 'PushReturnByteCode' cannot be null");

            if (type.isArrayType()) {
                context.addByteCodeToCurrentMethod(new _areturn());
            } else {
                switch (type.getTypeName()) {
                    case NORMAL_BOOLEAN:
                    case NORMAL_CHAR:
                    case NORMAL_INT:
                        context.addByteCodeToCurrentMethod(new _ireturn());
                        break;
                    case NORMAL_LONG:
                        context.addByteCodeToCurrentMethod(new _lreturn());
                        break;
                    case NORMAL_FLOAT:
                        context.addByteCodeToCurrentMethod(new _freturn());
                        break;
                    case NORMAL_DOUBLE:
                        context.addByteCodeToCurrentMethod(new _dreturn());
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            }
        }
    }
}
