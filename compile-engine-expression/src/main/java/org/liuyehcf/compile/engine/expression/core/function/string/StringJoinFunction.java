package org.liuyehcf.compile.engine.expression.core.function.string;

import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;
import org.liuyehcf.compile.engine.expression.utils.ToStringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author hechenfeng
 * @date 2018/9/27
 */
public class StringJoinFunction extends Function {

    private static final String EMPTY = "";

    @Override
    public String getName() {
        return "string.join";
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Object segments = arg1.getValue();
        Object separator = arg2.getValue();

        if (segments == null) {
            return ExpressionValue.valueOf(EMPTY);
        }
        if (separator == null) {
            separator = EMPTY;
        }

        StringBuilder sb = new StringBuilder();

        if (segments instanceof Collection) {
            if (((Collection) segments).size() == 0) {
                return ExpressionValue.valueOf(EMPTY);
            }

            Iterator iterator = ((Collection) segments).iterator();
            sb.append(ToStringUtils.toString(iterator.next()));
            while (iterator.hasNext()) {
                sb.append(ToStringUtils.toString(separator))
                        .append(ToStringUtils.toString(iterator.next()));
            }
        } else if (segments.getClass().isArray()) {
            if (Array.getLength(segments) == 0) {
                return ExpressionValue.valueOf(EMPTY);
            }
            sb.append(ToStringUtils.toString(Array.get(segments, 0)));
            for (int i = 1; i < Array.getLength(segments); i++) {
                sb.append(ToStringUtils.toString(separator))
                        .append(ToStringUtils.toString(Array.get(segments, i)));
            }
        } else {
            throw createTypeIllegalException(1, segments);
        }

        return ExpressionValue.valueOf(sb.toString());
    }
}
