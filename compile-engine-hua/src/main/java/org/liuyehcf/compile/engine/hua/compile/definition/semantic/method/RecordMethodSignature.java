package org.liuyehcf.compile.engine.hua.compile.definition.semantic.method;

import org.liuyehcf.compile.engine.hua.compile.CompilerContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录方法签名
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class RecordMethodSignature extends AbstractSemanticAction {

    /**
     * 返回类型-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int resultTypeStackOffset;

    /**
     * 方法描述-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int methodDeclaratorStackOffset;

    public RecordMethodSignature(int resultTypeStackOffset, int methodDeclaratorStackOffset) {
        this.resultTypeStackOffset = resultTypeStackOffset;
        this.methodDeclaratorStackOffset = methodDeclaratorStackOffset;
    }

    @Override
    public void onAction(CompilerContext context) {
        Type resultType = context.getAttr(resultTypeStackOffset, AttrName.TYPE);
        context.setResultTypeOfCurrentMethod(resultType);

        String methodName = context.getAttr(methodDeclaratorStackOffset, AttrName.METHOD_NAME);
        context.setMethodNameOfCurrentMethod(methodName);

        List<Type> paramTypeList = context.getAttr(methodDeclaratorStackOffset, AttrName.PARAMETER_LIST);
        if (paramTypeList == null) {
            paramTypeList = new ArrayList<>();
            context.setAttr(methodDeclaratorStackOffset, AttrName.PARAMETER_LIST, paramTypeList);
        }
        context.setParamTypeListOfCurrentMethod(paramTypeList);

        context.addMethodSignatureToConstantPool();
    }
}
