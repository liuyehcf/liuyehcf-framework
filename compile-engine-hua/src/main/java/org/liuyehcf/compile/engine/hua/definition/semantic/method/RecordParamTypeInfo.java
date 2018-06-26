package org.liuyehcf.compile.engine.hua.definition.semantic.method;

import org.liuyehcf.compile.engine.hua.core.HuaContext;
import org.liuyehcf.compile.engine.hua.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.definition.model.Type;
import org.liuyehcf.compile.engine.hua.definition.semantic.AbstractSemanticAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 记录形参信息
 *
 * @author hechenfeng
 * @date 2018/6/7
 */
public class RecordParamTypeInfo extends AbstractSemanticAction implements Serializable {

    /**
     * 形参列表-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int paramListStackOffset;

    /**
     * 形参-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int paramStackOffset;

    public RecordParamTypeInfo(int paramListStackOffset, int paramStackOffset) {
        this.paramListStackOffset = paramListStackOffset;
        this.paramStackOffset = paramStackOffset;
    }

    @Override
    public void onAction(HuaContext context) {
        Type type = context.getAttr(paramStackOffset, AttrName.TYPE);

        List<Type> paramTypeList = context.getAttr(paramListStackOffset, AttrName.PARAMETER_LIST);
        if (paramTypeList == null) {
            paramTypeList = new ArrayList<>();
            context.setAttr(paramListStackOffset, AttrName.PARAMETER_LIST, paramTypeList);
        }
        paramTypeList.add(type);
    }
}
