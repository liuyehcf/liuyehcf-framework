package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class AddCondition extends AddExecutable {

    public AddCondition(int nameStackOffset, int argumentListStackOffset) {
        super(nameStackOffset, argumentListStackOffset);
    }

    @Override
    public void onAction(CompilerContext context) {
        String conditionName = context.getAttr(nameStackOffset, AttrName.CONDITION_NAME);
        List<String> argumentNameList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_NAME_LIST);
        List<Object> argumentValueList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_VALUE_LIST);

        Condition condition = context.addCondition(conditionName, argumentNameList, argumentValueList);
        context.setAttrToLeftNode(AttrName.NODE, condition);
    }
}
