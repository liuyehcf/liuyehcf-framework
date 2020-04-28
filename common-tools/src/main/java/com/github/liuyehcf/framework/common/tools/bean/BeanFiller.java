package com.github.liuyehcf.framework.common.tools.bean;

import java.lang.reflect.*;
import java.util.*;

public class BeanFiller {
    private static final Byte BYTE_DEFAULT_VALUE = 1;
    private static final Character CHAR_DEFAULT_VALUE = 'a';
    private static final Short SHORT_DEFAULT_VALUE = 2;
    private static final Integer INTEGER_DEFAULT_VALUE = 3;
    private static final Long LONG_DEFAULT_VALUE = 6L;
    private static final Float FLOAT_DEFAULT_VALUE = 1.0F;
    private static final Double DOUBLE_DEFAULT_VALUE = 2.0D;
    private static final String STRING_DEFAULT_VALUE = "default";

    private static final Map<Class<?>, Object> DEFAULT_VALUE_OF_BASIC_CLASS = new HashMap<>();

    private static final String SET_METHOD_PREFIX = "set";
    private static final Integer SET_METHOD_PARAM_COUNT = 1;
    private static final Class<?> SET_METHOD_RETURN_TYPE = void.class;

    private static final Set<Class<?>> CONTAINER_CLASS_SET = new HashSet<>();
    private static final Integer CONTAINER_DEFAULT_SIZE = 3;

    static {
        DEFAULT_VALUE_OF_BASIC_CLASS.put(Byte.class, BYTE_DEFAULT_VALUE);
        DEFAULT_VALUE_OF_BASIC_CLASS.put(byte.class, BYTE_DEFAULT_VALUE);

        DEFAULT_VALUE_OF_BASIC_CLASS.put(Character.class, CHAR_DEFAULT_VALUE);
        DEFAULT_VALUE_OF_BASIC_CLASS.put(char.class, CHAR_DEFAULT_VALUE);

        DEFAULT_VALUE_OF_BASIC_CLASS.put(Short.class, SHORT_DEFAULT_VALUE);
        DEFAULT_VALUE_OF_BASIC_CLASS.put(short.class, SHORT_DEFAULT_VALUE);

        DEFAULT_VALUE_OF_BASIC_CLASS.put(Integer.class, INTEGER_DEFAULT_VALUE);
        DEFAULT_VALUE_OF_BASIC_CLASS.put(int.class, INTEGER_DEFAULT_VALUE);

        DEFAULT_VALUE_OF_BASIC_CLASS.put(Long.class, LONG_DEFAULT_VALUE);
        DEFAULT_VALUE_OF_BASIC_CLASS.put(long.class, LONG_DEFAULT_VALUE);

        DEFAULT_VALUE_OF_BASIC_CLASS.put(Float.class, FLOAT_DEFAULT_VALUE);
        DEFAULT_VALUE_OF_BASIC_CLASS.put(float.class, FLOAT_DEFAULT_VALUE);

        DEFAULT_VALUE_OF_BASIC_CLASS.put(Double.class, DOUBLE_DEFAULT_VALUE);
        DEFAULT_VALUE_OF_BASIC_CLASS.put(double.class, DOUBLE_DEFAULT_VALUE);

        DEFAULT_VALUE_OF_BASIC_CLASS.put(Boolean.class, false);
        DEFAULT_VALUE_OF_BASIC_CLASS.put(boolean.class, false);
    }

    static {
        CONTAINER_CLASS_SET.add(List.class);
        CONTAINER_CLASS_SET.add(Map.class);
        CONTAINER_CLASS_SET.add(Set.class);
        CONTAINER_CLASS_SET.add(Queue.class);
    }

    /**
     * type of bean to be initialized
     */
    private final Type type;

    /**
     * generic type -> actual type
     */
    private Map<String, Type> genericTypes;

    private BeanFiller(Type type, Map<String, Type> superClassGenericTypes) {
        this.type = type;
        genericTypes = new HashMap<>();
        initGenericTypes(superClassGenericTypes);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fill(TypeReference<T> typeReference) {
        if (typeReference == null) {
            throw new NullPointerException();
        }
        return (T) fill(typeReference.getType(), null, null);
    }

    private static Object fill(Type type, Map<String, Type> superClassGenericTypes, Type superType) {
        if (type == null) {
            throw new NullPointerException();
        }
        // avoiding dead loop if nested itself
        if (type.equals(superType)) {
            return null;
        }
        return new BeanFiller(type, superClassGenericTypes)
                .doCreateJavaBean();
    }

    private void initGenericTypes(Map<String, Type> superClassGenericTypes) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            Class<?> clazz = (Class<?>) parameterizedType.getRawType();

            // you can get generic parameters through Class, but you cannot get generic parameters
            TypeVariable<?>[] typeVariables = clazz.getTypeParameters();

            // you can get generic arguments through ParameterizedType, and retain generic arguments through inheritance structure
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type actualTypeArgument = actualTypeArguments[i];
                if (actualTypeArgument instanceof TypeVariable) {
                    if (superClassGenericTypes == null
                            || (actualTypeArgument = superClassGenericTypes.get(getNameOfTypeVariable(actualTypeArgument))) == null) {
                        throw new RuntimeException();
                    }
                    actualTypeArguments[i] = actualTypeArgument;
                }
            }

            for (int i = 0; i < typeVariables.length; i++) {
                genericTypes.put(
                        // you need to splice the generic parameters so that they can be protected by a namespace
                        // otherwise, the generic parameters may be overwritten
                        getNameOfTypeVariable(typeVariables[i]),
                        actualTypeArguments[i]
                );
            }
        }
    }

    private String getNameOfTypeVariable(Type typeVariable) {
        return ((TypeVariable<?>) typeVariable).getName();
    }

    private Object doCreateJavaBean() {
        if (type instanceof Class) {
            return createJavaBeanWithClass((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            return createJavaBeanWithGenericType((ParameterizedType) type);
        } else {
            throw new UnsupportedOperationException("unsupported type: " + type);
        }
    }

    private Object createJavaBeanWithClass(Class<?> clazz) {

        if (DEFAULT_VALUE_OF_BASIC_CLASS.containsKey(clazz)) {
            return DEFAULT_VALUE_OF_BASIC_CLASS.get(clazz);
        } else if (String.class.equals(clazz)) {
            return STRING_DEFAULT_VALUE;
        }

        Object obj = createInstance(clazz);

        for (Method setMethod : getSetMethods(clazz)) {
            Type paramType = setMethod.getGenericParameterTypes()[0];

            setDefaultValue(obj, setMethod, paramType);
        }

        return obj;
    }

    private Object createJavaBeanWithGenericType(ParameterizedType type) {

        Class<?> clazz = (Class<?>) type.getRawType();

        Object obj = createInstance(clazz);

        for (Method setMethod : getSetMethods(clazz)) {
            Type paramType = setMethod.getGenericParameterTypes()[0];

            if (paramType instanceof TypeVariable) {
                // if the parameter type is a generic parameter, find the generic parameter corresponding to the generic parameter according to the mapping relationship
                Type actualType = genericTypes.get(getNameOfTypeVariable(paramType));
                setDefaultValue(obj, setMethod, actualType);
            } else {
                // parameter type is the exact type, which may be class or parameterizedType
                setDefaultValue(obj, setMethod, paramType);
            }
        }

        return obj;
    }

    private Object createInstance(Class<?> clazz) {
        Object obj;
        try {
            obj = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Cannot instantiate without no args constructor");
        }
        return obj;
    }

    private List<Method> getSetMethods(Class<?> clazz) {
        List<Method> setMethods = new ArrayList<>();
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.getName().startsWith(SET_METHOD_PREFIX)
                    && SET_METHOD_PARAM_COUNT.equals(method.getParameterCount())
                    && SET_METHOD_RETURN_TYPE.equals(method.getReturnType())) {
                setMethods.add(method);
            }
        }
        return setMethods;
    }

    private void setDefaultValue(Object obj, Method method, Type paramType) {
        try {
            if (paramType instanceof Class) {
                setDefaultValueOfNormal(obj, method, (Class<?>) paramType);
            } else if (paramType instanceof ParameterizedType) {
                setDefaultValueOfGeneric(obj, method, (ParameterizedType) paramType);
            } else {
                throw new UnsupportedOperationException();
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException();
        }
    }

    private String getFieldName(Method method) {
        return method.getName().substring(3);
    }

    private void setDefaultValueOfNormal(Object obj, Method method, Class<?> paramClass) throws IllegalAccessException, InvocationTargetException {
        if (DEFAULT_VALUE_OF_BASIC_CLASS.containsKey(paramClass)) {
            method.invoke(obj, DEFAULT_VALUE_OF_BASIC_CLASS.get(paramClass));
        } else if (String.class.equals(paramClass)) {
            method.invoke(obj, STRING_DEFAULT_VALUE + getFieldName(method));
        } else {
            method.invoke(obj, fill(paramClass, genericTypes, type));
        }
    }

    private void setDefaultValueOfGeneric(Object obj, Method method, ParameterizedType paramType) throws IllegalAccessException, InvocationTargetException {
        Class<?> clazz = (Class<?>) paramType.getRawType();

        if (instanceOfContainer(clazz)) {
            setDefaultValueForContainer(obj, method, paramType);
        } else {
            method.invoke(obj, fill(paramType, genericTypes, type));
        }
    }

    private boolean instanceOfContainer(Class<?> clazz) {
        return CONTAINER_CLASS_SET.contains(clazz);
    }

    private void setDefaultValueForContainer(Object obj, Method method, ParameterizedType paramType) throws IllegalAccessException, InvocationTargetException {
        Class<?> clazz = (Class<?>) paramType.getRawType();

        if (List.class.equals(clazz)) {
            List<Object> list = new ArrayList<>();

            Type genericParam = paramType.getActualTypeArguments()[0];

            for (int i = 0; i < CONTAINER_DEFAULT_SIZE; i++) {
                list.add(createJavaBeanWithTypeVariable(genericParam));
            }

            method.invoke(obj, list);
        } else if (Set.class.equals(clazz)) {
            Set<Object> set = new HashSet<>();

            Type genericParam = paramType.getActualTypeArguments()[0];

            for (int i = 0; i < CONTAINER_DEFAULT_SIZE; i++) {
                set.add(createJavaBeanWithTypeVariable(genericParam));
            }

            method.invoke(obj, set);
        } else if (Queue.class.equals(clazz)) {
            Queue<Object> queue = new LinkedList<>();

            Type genericParam = paramType.getActualTypeArguments()[0];

            for (int i = 0; i < CONTAINER_DEFAULT_SIZE; i++) {
                queue.add(createJavaBeanWithTypeVariable(genericParam));
            }

            method.invoke(obj, queue);
        } else if (Map.class.equals(clazz)) {
            Map<Object, Object> map = new HashMap<>();

            Type genericParam1 = paramType.getActualTypeArguments()[0];
            Type genericParam2 = paramType.getActualTypeArguments()[1];

            Object key;
            Object value;

            for (int i = 0; i < CONTAINER_DEFAULT_SIZE; i++) {
                key = createJavaBeanWithTypeVariable(genericParam1);
                value = createJavaBeanWithTypeVariable(genericParam2);

                map.put(key, value);
            }

            method.invoke(obj, map);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private Object createJavaBeanWithTypeVariable(Type type) {
        if (type instanceof TypeVariable) {
            return fill(genericTypes.get(getNameOfTypeVariable(type)), genericTypes, this.type);
        } else {
            return fill(type, genericTypes, this.type);
        }
    }

    public static abstract class TypeReference<T> {
        private final Type type;

        protected TypeReference() {
            Type superClass = getClass().getGenericSuperclass();

            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }

        public final Type getType() {
            return type;
        }
    }
}
