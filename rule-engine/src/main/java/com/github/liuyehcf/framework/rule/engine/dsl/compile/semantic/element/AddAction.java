package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class AddAction extends AddExecutable {

    public AddAction(int nameStackOffset, int argumentListStackOffset) {
        super(nameStackOffset, argumentListStackOffset);
    }

    @Override
    public void onAction(CompilerContext context) {
        String actionName = context.getAttr(nameStackOffset, AttrName.ACTION_NAME);
        List<String> argumentNameList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_NAME_LIST);
        List<Object> argumentValueList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_VALUE_LIST);

        Action action = context.addAction(actionName, argumentNameList, argumentValueList);
        context.setAttrToLeftNode(AttrName.NODE, action);
    }
}
