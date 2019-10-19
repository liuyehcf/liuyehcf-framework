package com.github.liuyehcf.framework.rule.engine.util;

import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author hechenfeng
 * @date 2019/10/18
 */
public abstract class BeanUtils {

    @SuppressWarnings("unchecked")
    public static <T> T clone(T bean) {
        try {
            return (T) doClone(bean, Lists.newLinkedList());
        } catch (Exception e) {
            throw new RuleException(RuleErrorCode.SERIALIZE, e);
        }
    }

    private static Object doClone(Object bean, LinkedList<Object> visited) throws Exception {
        if (bean == null) {
            return null;
        } else if (ClassUtils.isPrimitiveOrWrapper(bean.getClass())) {
            return bean;
        } else if (bean instanceof String) {
            return bean;
        } else if (bean instanceof Date) {
            return new Date(((Date) bean).getTime());
        } else if (bean instanceof BigInteger) {
            return new BigInteger(bean.toString());
        } else if (bean instanceof BigDecimal) {
            return new BigDecimal(bean.toString());
        } else if (bean.getClass().isArray()) {
            pushReference(visited, bean);
            Object clone = doCloneArray(bean, visited);
            popReference(visited);
            return clone;
        } else if (bean instanceof Map) {
            pushReference(visited, bean);
            Map clone = doCloneMap((Map) bean, visited);
            popReference(visited);
            return clone;
        } else if (bean instanceof Collection) {
            pushReference(visited, bean);
            Collection clone = doCloneCollection((Collection) bean, visited);
            popReference(visited);
            return clone;
        } else {
            pushReference(visited, bean);
            Object clone = doCloneBean(bean, visited);
            popReference(visited);
            return clone;
        }
    }

    private static Object doCloneArray(Object array, LinkedList<Object> visited) throws Exception {
        Class<?> componentType = array.getClass().getComponentType();

        int length = Array.getLength(array);
        Object clone = Array.newInstance(componentType, length);

        for (int i = 0; i < length; i++) {
            Object value = Array.get(array, i);

            Array.set(clone, i, doClone(value, visited));
        }

        return clone;
    }

    @SuppressWarnings("unchecked")
    private static Map doCloneMap(Map map, LinkedList<Object> visited) throws Exception {
        Map clone = map.getClass().newInstance();

        for (PropertyInfo property : getAllProperties(map)) {
            String key = property.name;
            Object value = property.value;

            clone.put(key, doClone(value, visited));
        }

        return clone;
    }

    @SuppressWarnings("unchecked")
    private static Collection doCloneCollection(Collection col, LinkedList<Object> visited) throws Exception {
        Collection clone = col.getClass().newInstance();

        for (Object value : col) {
            clone.add(doClone(value, visited));
        }

        return clone;
    }

    private static Object doCloneBean(Object bean, LinkedList<Object> visited) throws Exception {
        Object clone = bean.getClass().newInstance();

        for (PropertyInfo property : getAllProperties(bean)) {
            String propertyName = property.name;
            Object propertyValue = property.value;
            Class<?> propertyClass = property.clazz;

            Method setMethod = getSetMethod(bean, propertyName, propertyClass);

            if (setMethod == null) {
                continue;
            }

            setMethod.invoke(clone, doClone(propertyValue, visited));
        }

        return clone;
    }

    @SuppressWarnings("unchecked")
    private static List<PropertyInfo> getAllProperties(Object bean) throws Exception {
        List<PropertyInfo> properties = Lists.newArrayList();

        if (bean instanceof Map) {
            Set<Map.Entry> entrySet = ((Map) bean).entrySet();

            for (Map.Entry entry : entrySet) {
                Object key = entry.getKey();
                Object value = entry.getValue();

                Class<?> valueClass;
                if (value == null) {
                    valueClass = null;
                } else {
                    valueClass = value.getClass();
                }

                if (key == null) {
                    properties.add(new PropertyInfo(null, value, valueClass));
                } else if (key instanceof String) {
                    properties.add(new PropertyInfo((String) key, value, valueClass));
                }
            }
        } else {
            for (Method getMethod : getAllGetMethods(bean)) {
                String propertyName = getPropertyNameOfGetMethod(getMethod);

                Object value = getMethod.invoke(bean);

                properties.add(new PropertyInfo(propertyName, value, getMethod.getReturnType()));
            }
        }

        return properties;
    }

    private static List<Method> getAllGetMethods(Object bean) {
        List<Method> getMethods = Lists.newArrayList();
        if (bean == null) {
            return getMethods;
        }

        Method[] methods = bean.getClass().getMethods();
        if (ArrayUtils.isEmpty(methods)) {
            return getMethods;
        }

        for (Method method : methods) {
            if (!isGetMethod(method)) {
                continue;
            }

            String propertyName = getPropertyNameOfGetMethod(method);

            Method setMethod = getSetMethod(bean, propertyName, method.getReturnType());
            if (setMethod == null) {
                continue;
            }

            getMethods.add(method);
        }

        return getMethods;
    }

    private static Method getSetMethod(Object bean, String propertyName, Class<?> paramType) {
        if (bean == null || paramType == null) {
            return null;
        }

        String setMethodName = getSetMethodNameOfPropertyName(propertyName);

        Method setMethod;

        if (paramType.isPrimitive()) {
            setMethod = getMethod(bean, setMethodName, paramType);
            if (setMethod == null) {
                setMethod = getMethod(bean, setMethodName, ClassUtils.primitiveToWrapper(paramType));
            }

            if (setMethod == null) {
                return null;
            }

            if (!isVoid(setMethod.getReturnType())) {
                return null;
            }
        } else if (ClassUtils.isPrimitiveWrapper(paramType)) {
            setMethod = getMethod(bean, setMethodName, paramType);
            if (setMethod == null) {
                setMethod = getMethod(bean, setMethodName, ClassUtils.wrapperToPrimitive(paramType));
            }

            if (setMethod == null) {
                return null;
            }

            if (!isVoid(setMethod.getReturnType())) {
                return null;
            }
        } else {
            setMethod = getMethod(bean, setMethodName, paramType);

            if (setMethod == null) {
                return null;
            }

            if (!isVoid(setMethod.getReturnType())) {
                return null;
            }
        }

        return setMethod;
    }

    private static String getPropertyNameOfGetMethod(Method getMethod) {
        String methodName = getMethod.getName();
        if (isBoolean(getMethod.getReturnType())) {
            if (methodName.startsWith("is")) {
                return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
            } else {
                return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            }
        } else {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }
    }

    private static String getSetMethodNameOfPropertyName(String propertyName) {
        String setMethodName = "set";
        if (propertyName.length() == 1) {
            setMethodName += propertyName.toUpperCase();
        } else {
            setMethodName += propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        }

        return setMethodName;
    }

    private static boolean isGetMethod(Method method) {
        if (method.getParameterCount() != 0) {
            return false;
        }

        Class<?> returnType = method.getReturnType();
        if (isVoid(returnType)) {
            return false;
        }

        String methodName = method.getName();
        if (isBoolean(method.getReturnType())) {
            if (methodName.startsWith("is")) {
                return methodName.length() > 2;
            } else if (methodName.startsWith("get")) {
                return methodName.length() > 3;
            } else {
                return false;
            }
        } else {
            if (methodName.startsWith("get")) {
                return methodName.length() > 3;
            } else {
                return false;
            }
        }
    }

    private static boolean isVoid(Class<?> clazz) {
        return void.class.equals(clazz);
    }

    private static boolean isBoolean(Class<?> clazz) {
        return boolean.class.equals(clazz) ||
                Boolean.class.equals(clazz);
    }

    private static Method getMethod(Object obj, String name, Class<?>... parameterTypes) {
        try {
            return obj.getClass().getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static void pushReference(LinkedList<Object> visited, Object bean) {
        if (containsReference(visited, bean)) {
            throw new RuleException(RuleErrorCode.SERIALIZE, "bean clone does not support mutual reference");
        }

        visited.push(bean);
    }

    private static void popReference(LinkedList<Object> visited) {
        visited.pop();
    }

    private static boolean containsReference(LinkedList<Object> visited, Object obj) {
        for (Object visitedObj : visited) {
            if (visitedObj == obj) {
                return true;
            }
        }
        return false;
    }

    private static final class PropertyInfo {
        private final String name;
        private final Object value;
        private final Class<?> clazz;

        private PropertyInfo(String name, Object value, Class<?> clazz) {
            this.name = name;
            this.value = value;
            this.clazz = clazz;
        }
    }
}
