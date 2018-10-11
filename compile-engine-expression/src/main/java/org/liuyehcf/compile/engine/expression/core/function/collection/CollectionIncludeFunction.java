package org.liuyehcf.compile.engine.expression.core.function.collection;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class CollectionIncludeFunction extends Function {
    @Override
    public String getName() {
        return "collection.include";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {

        Object array = arg1.getValue();
        Object value = arg2.getValue();

        if (array == null) {
            return ExpressionValue.valueOf(false);
        }

        if (array.getClass().isArray()) {
            int size = Array.getLength(array);
            for (int i = 0; i < size; i++) {
                Object item = Array.get(array, i);
                if (Objects.equals(item, value)) {
                    return ExpressionValue.valueOf(true);
                }
            }
            return ExpressionValue.valueOf(false);
        } else if (array instanceof Collection) {
            return ExpressionValue.valueOf(((Collection) array).contains(value));
        } else {
            throw createTypeIllegalException(1, array);
        }
    }
}
