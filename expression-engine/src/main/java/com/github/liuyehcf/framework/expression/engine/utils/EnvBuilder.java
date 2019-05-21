package com.github.liuyehcf.framework.expression.engine.utils;

import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2018/8/13
 */
public class EnvBuilder {

    private Map<String, Object> map = Maps.newHashMap();

    private EnvBuilder() {

    }

    public static EnvBuilder builder() {
        return new EnvBuilder();
    }

    @SuppressWarnings("unchecked")
    public EnvBuilder put(String key, Object value) {
        if (value == null) {
            return this;
        }
        String[] segments = key.split("\\.");

        Map<String, Object> cur = map;
        final int size = segments.length;

        for (int i = 0; i < size - 1; i++) {
            String segment = segments[i];
            if (!cur.containsKey(segment)) {
                cur.put(segment, Maps.newHashMap());
            }

            Object entryValue = cur.get(segment);
            try {
                cur = (Map<String, Object>) entryValue;
            } catch (ClassCastException e) {
                String propertyPrefix = getPropertyPrefix(segments, i);
                throw new ExpressionException(propertyPrefix + "='" + (entryValue == null ? null : entryValue.getClass().getName()) + "' is incompatible with 'java.util.Map<String, Object>'");
            }
        }

        cur.put(segments[size - 1], value);
        return this;
    }

    private String getPropertyPrefix(final String[] segments, final int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(segments[0]);

        for (int i = 1; i <= index; i++) {
            sb.append('.').append(segments[i]);
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> build() {
        return map;
    }
}
