package com.github.liuyehcf.framework.expression.engine.runtime;

import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.model.ComparableValue;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public final class ExpressionValueImpl implements ExpressionValue {

    private static final ExpressionValueImpl NULL = new ExpressionValueImpl();

    private final ExpressionType type;

    private final Object value;

    private ExpressionValueImpl() {
        type = ExpressionType.NULL;
        value = null;
    }

    private ExpressionValueImpl(Object originValue) {
        final ExpressionType actualType;
        final Object actualValue;

        if (originValue instanceof Boolean) {
            actualType = ExpressionType.BOOLEAN;
            actualValue = (boolean) (Boolean) originValue ? 1L : 0L;
        } else if (originValue instanceof Byte) {
            actualType = ExpressionType.LONG;
            actualValue = (long) (byte) (Byte) originValue;
        } else if (originValue instanceof Character) {
            actualType = ExpressionType.STRING;
            actualValue = "" + (char) originValue;
        } else if (originValue instanceof Short) {
            actualType = ExpressionType.LONG;
            actualValue = (long) (short) (Short) originValue;
        } else if (originValue instanceof Integer) {
            actualType = ExpressionType.LONG;
            actualValue = (long) (int) (Integer) originValue;
        } else if (originValue instanceof Long) {
            actualType = ExpressionType.LONG;
            actualValue = originValue;
        } else if (originValue instanceof Float) {
            actualType = ExpressionType.DOUBLE;
            actualValue = (double) (float) (Float) originValue;
        } else if (originValue instanceof Double) {
            actualType = ExpressionType.DOUBLE;
            actualValue = originValue;
        } else if (originValue instanceof ComparableValue) {
            actualType = ExpressionType.COMPARABLE_VALUE;
            actualValue = originValue;
        } else {
            if (originValue instanceof String) {
                actualType = ExpressionType.STRING;
            } else if (originValue.getClass().isArray()) {
                actualType = ExpressionType.ARRAY;
            } else if (originValue instanceof List) {
                actualType = ExpressionType.LIST;
            } else {
                actualType = ExpressionType.OBJECT;
            }
            actualValue = originValue;
        }
        this.value = actualValue;
        this.type = actualType;
    }

    public static ExpressionValueImpl valueOf(Object object) {
        if (object == null) {
            return NULL;
        }
        return new ExpressionValueImpl(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) getValue0();
    }

    private Object getValue0() {
        switch (type) {
            case BOOLEAN:
                return ((long) value == 1);
            case NULL:
            case COMPARABLE_VALUE:
            case LONG:
            case DOUBLE:
            case STRING:
            case ARRAY:
            case LIST:
            case OBJECT:
                return value;
            default:
                throw new ExpressionException("unexpected expressionType='" + type + "'");
        }
    }

    @Override
    public String toString() {
        return "ExpressionValueImpl{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
