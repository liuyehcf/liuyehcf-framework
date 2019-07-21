package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class AddListener extends AddExecutable {

    public AddListener(int nameStackOffset, int argumentListStackOffset) {
        super(nameStackOffset, argumentListStackOffset);
    }

    @Override
    public void onAction(CompilerContext context) {
        String listenerName = context.getAttr(nameStackOffset, AttrName.LISTENER_NAME);
        List<String> argumentNameList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_NAME_LIST);
        List<Object> argumentValueList = context.getAttr(argumentListStackOffset, AttrName.ARGUMENT_VALUE_LIST);

        Listener listener = context.addListener(listenerName, argumentNameList, argumentValueList);
        context.setAttrToLeftNode(AttrName.LISTENER, listener);
    }
}
