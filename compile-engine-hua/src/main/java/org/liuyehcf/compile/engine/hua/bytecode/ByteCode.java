package org.liuyehcf.compile.engine.hua.bytecode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;

/**
 * 字节码抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public abstract class ByteCode {

    private static final Map<Integer, Class<? extends ByteCode>> operatorCodePool = new HashMap<>();
    private static final String FILED_NAME_OPERATOR_CODE = "OPERATOR_CODE";
    private static final String FILED_NAME_OPERATOR_CLASSES = "OPERATOR_CLASSES";

    public static int getOperatorCode(ByteCode code) {
        return (int) getStaticField(code, FILED_NAME_OPERATOR_CODE);
    }

    public static Class<?>[] getOperatorClasses(ByteCode code) {
        return (Class<?>[]) getStaticField(code, FILED_NAME_OPERATOR_CLASSES);
    }

    private static Object getStaticField(ByteCode code, String fieldName) {
        try {
            Class clazz = code.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            return field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(int operatorCode, Class<? extends ByteCode> clazz) {
        assertFalse(operatorCodePool.containsKey(operatorCode));
        operatorCodePool.put(operatorCode, clazz);
    }

    public static Class<? extends ByteCode> getByteCodeByOperatorCode(int operatorCode) {
        return operatorCodePool.get(operatorCode);
    }

    public final String getName() {
        return getClass().getSimpleName();
    }

    public abstract void operate();

    public abstract Object[] getOperators();
}
