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
     * 要初始化的类型
     */
    private final Type type;
    /**
     * 从泛型参数名映射到实际的类型
     */
    private Map<String, Type> genericTypes;

    private BeanFiller(Type type, Map<String, Type> superClassGenericTypes) {
        this.type = type;
        genericTypes = new HashMap<>();
        init(superClassGenericTypes);
    }

    /**
     * 唯一对外接口
     */
    @SuppressWarnings("unchecked")
    public static <T> T fill(TypeReference<T> typeReference) {
        if (typeReference == null) {
            throw new NullPointerException();
        }
        return (T) fill(typeReference.getType(), null, null);
    }

    /**
     * 初始化JavaBean
     */
    private static Object fill(Type type, Map<String, Type> superClassGenericTypes, Type superType) {
        if (type == null) {
            throw new NullPointerException();
        }
        // 如果一个DTO嵌套了自己，避免死循环
        if (type.equals(superType)) {
            return null;
        }
        return new BeanFiller(type, superClassGenericTypes)
                .doCreateJavaBean();
    }

    /**
     * 对于泛型类型，初始化泛型参数描述与实际泛型参数的映射关系
     * 例如List有一个泛型参数T，如果传入的是List<String>类型，那么建立 "T"->java.lang.String 的映射
     */
    private void init(Map<String, Type> superClassGenericTypes) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            Class<?> clazz = (Class<?>) parameterizedType.getRawType();

            // 通过Class可以拿到泛型形参，但无法拿到泛型实参
            TypeVariable<?>[] typeVariables = clazz.getTypeParameters();

            // 通过ParameterizedType可以拿到泛型实参，通过继承结构保留泛型实参
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

            // 维护泛型形参到泛型实参的映射关系
            for (int i = 0; i < typeVariables.length; i++) {
                genericTypes.put(
                        // 这里需要拼接一下，使得泛型形参有一个命名空间的保护，否则泛型形参可能会出现覆盖的情况
                        getNameOfTypeVariable(typeVariables[i]),
                        actualTypeArguments[i]
                );
            }
        }
    }

    private String getNameOfTypeVariable(Type typeVariable) {
        return ((TypeVariable<?>) typeVariable).getName();
    }

    /**
     * 创建JavaBean，根据type的实际类型进行分发
     */
    private Object doCreateJavaBean() {
        if (type instanceof Class) {
            // 创建非泛型实例
            return createJavaBeanWithClass((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            // 创建泛型实例
            return createJavaBeanWithGenericType((ParameterizedType) type);
        } else {
            throw new UnsupportedOperationException("暂不支持此类型的默认初始化，type: " + type);
        }
    }

    /**
     * 通过普通的Class创建JavaBean
     */
    private Object createJavaBeanWithClass(Class<?> clazz) {

        if (DEFAULT_VALUE_OF_BASIC_CLASS.containsKey(clazz)) {
            return DEFAULT_VALUE_OF_BASIC_CLASS.get(clazz);
        } else if (String.class.equals(clazz)) {
            return STRING_DEFAULT_VALUE;
        }

        Object obj = createInstance(clazz);

        for (Method setMethod : getSetMethods(clazz)) {

            // 拿到set方法的参数类型
            Type paramType = setMethod.getGenericParameterTypes()[0];

            // 填充默认值
            setDefaultValue(obj, setMethod, paramType);
        }

        return obj;
    }

    /**
     * 通过带有泛型实参的ParameterizedType创建JavaBean
     */
    private Object createJavaBeanWithGenericType(ParameterizedType type) {

        Class<?> clazz = (Class<?>) type.getRawType();

        Object obj = createInstance(clazz);

        for (Method setMethod : getSetMethods(clazz)) {
            // 拿到set方法的参数类型
            Type paramType = setMethod.getGenericParameterTypes()[0];

            if (paramType instanceof TypeVariable) {
                // 如果参数类型是泛型形参，根据映射关系找到泛型形参对应的泛型实参
                Type actualType = genericTypes.get(getNameOfTypeVariable(paramType));
                setDefaultValue(obj, setMethod, actualType);
            } else {
                // 参数类型是确切的类型，可能是Class，也可能是ParameterizedType
                setDefaultValue(obj, setMethod, paramType);
            }
        }

        return obj;
    }

    /**
     * 通过反射创建实例
     */
    private Object createInstance(Class<?> clazz) {
        Object obj;
        try {
            obj = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("不能实例化接口/抽象类/没有无参构造方法的类");
        }
        return obj;
    }

    /**
     * 返回所有set方法
     */
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

    /**
     * 为属性设置默认值，根据参数类型进行分发
     */
    private void setDefaultValue(Object obj, Method method, Type paramType) {
        try {
            if (paramType instanceof Class) {
                // 普通参数
                setDefaultValueOfNormal(obj, method, (Class<?>) paramType);
            } else if (paramType instanceof ParameterizedType) {
                // 泛型实参
                setDefaultValueOfGeneric(obj, method, (ParameterizedType) paramType);
            } else {
                throw new UnsupportedOperationException();
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取属性名
     */
    private String getFieldName(Method method) {
        return method.getName().substring(3);
    }

    /**
     * set方法参数是普通的类型
     */
    private void setDefaultValueOfNormal(Object obj, Method method, Class<?> paramClass) throws IllegalAccessException, InvocationTargetException {
        if (DEFAULT_VALUE_OF_BASIC_CLASS.containsKey(paramClass)) {
            // 填充基本类型
            method.invoke(obj, DEFAULT_VALUE_OF_BASIC_CLASS.get(paramClass));
        } else if (String.class.equals(paramClass)) {
            // 填充String类型
            method.invoke(obj, STRING_DEFAULT_VALUE + getFieldName(method));
        } else {
            // 填充其他类型
            method.invoke(obj, fill(paramClass, genericTypes, type));
        }
    }

    /**
     * set方法的参数是泛型
     */
    private void setDefaultValueOfGeneric(Object obj, Method method, ParameterizedType paramType) throws IllegalAccessException, InvocationTargetException {
        Class<?> clazz = (Class<?>) paramType.getRawType();

        if (instanceOfContainer(clazz)) {
            // 如果是容器的话，特殊处理一下
            setDefaultValueForContainer(obj, method, paramType);
        } else {
            // 其他类型
            method.invoke(obj, fill(paramType, genericTypes, type));
        }
    }

    /**
     * 判断是否是容器类型
     */
    private boolean instanceOfContainer(Class<?> clazz) {
        return CONTAINER_CLASS_SET.contains(clazz);
    }

    /**
     * 为几种不同的容器设置默认值，由于容器没有set方法，走默认逻辑就会得到一个空的容器。因此为容器填充一个值
     */
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
