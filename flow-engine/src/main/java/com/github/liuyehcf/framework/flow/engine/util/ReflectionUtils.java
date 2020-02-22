package com.github.liuyehcf.framework.flow.engine.util;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.Delegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public abstract class ReflectionUtils {

    private static final String SET = "set";
    private static final String EVENT = "event";

    public static Field getDelegateField(Delegate delegate, String fieldName) {
        try {
            Class<? extends Delegate> clazz = delegate.getClass();

            Field field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            return field;
        } catch (NoSuchFieldException e) {
            throw new FlowException(FlowErrorCode.DELEGATE_FIELD, e);
        }
    }

    public static Method getDelegateFieldSetMethod(Delegate delegate, String fieldName) {
        try {
            Class<? extends Delegate> clazz = delegate.getClass();

            String setMethod = toSetMethod(fieldName);

            return clazz.getMethod(setMethod, DelegateField.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Set<String> getAllDelegateFieldNames(Delegate delegate) {
        List<Method> methods = getAllSetMethods(delegate.getClass());
        List<Field> fields = getAllDelegateFields(delegate.getClass());

        Set<String> delegateFieldNames = Sets.newHashSet();

        for (Method method : methods) {
            delegateFieldNames.add(toFieldName(method.getName()));
        }

        for (Field field : fields) {
            delegateFieldNames.add(field.getName());
        }

        if (delegate instanceof ListenerDelegate) {
            Assert.assertFalse(delegateFieldNames.contains(EVENT), "ListenerDelegate cannot define DelegateField named 'event'");
            delegateFieldNames.add(EVENT);
        }

        return delegateFieldNames;
    }

    private static List<Method> getAllSetMethods(Class<?> clazz) {
        List<Method> setMethods = Lists.newArrayList();

        for (Method method : clazz.getMethods()) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }

            if (!DelegateField.class.equals(parameterTypes[0])) {
                continue;
            }

            if (!method.getName().startsWith(SET)) {
                continue;
            }

            setMethods.add(method);
        }

        return setMethods;
    }

    private static List<Field> getAllDelegateFields(Class<?> clazz) {
        List<Field> fields = Lists.newArrayList();

        for (Field field : clazz.getDeclaredFields()) {
            if (DelegateField.class.equals(field.getType())) {
                fields.add(field);
            }
        }

        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllDelegateFields(clazz.getSuperclass()));
        }

        return fields;
    }

    private static String toSetMethod(String fieldName) {
        return SET + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String toFieldName(String setMethodName) {
        return setMethodName.substring(SET.length(), SET.length() + 1).toLowerCase()
                + setMethodName.substring(SET.length() + 1);
    }
}
