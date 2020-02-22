package com.github.liuyehcf.framework.flow.engine.runtime.config;

import com.github.liuyehcf.framework.flow.engine.runtime.constant.EnvCloneType;

import java.util.HashMap;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class FlowProperties extends HashMap<String, Object> {

    public String getName() {
        return getDefault(Property.NAME);
    }

    public void setName(String name) {
        put(Property.NAME, name);
    }

    public EnvCloneType getEnvCloneType() {
        return getDefault(Property.ENV_CLONE_TYPE);
    }

    public void setEnvCloneType(EnvCloneType envCloneType) {
        put(Property.ENV_CLONE_TYPE, envCloneType);
    }

    @SuppressWarnings("unchecked")
    private <T> T getDefault(Property property) {
        return (T) getOrDefault(property.getKey(), property.getDefaultValue());
    }

    private void put(Property property, Object value) {
        put(property.getKey(), value);
    }

    private enum Property {
        // common
        NAME("name", "flowEngine"),
        ENV_CLONE_TYPE("envCloneType", EnvCloneType.hessian),
        ;

        private final String key;
        private final Object defaultValue;

        Property(String key, Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return key;
        }

        @SuppressWarnings("unchecked")
        public <T> T getDefaultValue() {
            return (T) defaultValue;
        }
    }
}
