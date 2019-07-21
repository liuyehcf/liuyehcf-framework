package com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.element;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.rule.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.rule.engine.model.AbstractAttachable;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/30
 */
public class AddGlobalListeners extends AbstractSemanticAction {

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int LISTENER_LISTS_TACK_OFFSET = 0;

    @Override
    public void onAction(CompilerContext context) {
        List<Listener> listeners = context.getAttr(LISTENER_LISTS_TACK_OFFSET, AttrName.LISTENER_LIST);

        for (Listener listener : listeners) {
            modifyScope(listener);
        }
    }

    private void modifyScope(Listener listener) {
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            Class<?> clazz = listener.getClass();

            Field field = clazz.getDeclaredField("scope");
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.setAccessible(true);
            field.set(listener, ListenerScope.GLOBAL);
            modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
            field.setAccessible(false);

            while (!AbstractAttachable.class.equals(clazz)) {
                clazz = clazz.getSuperclass();
                Assert.assertNotNull(clazz);
            }

            field = clazz.getDeclaredField("attachedId");
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.setAccessible(true);
            field.set(listener, null);
            modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
            field.setAccessible(false);

        } catch (Exception e) {
            throw new RuleException(RuleErrorCode.COMPILE, e);
        }
    }
}
