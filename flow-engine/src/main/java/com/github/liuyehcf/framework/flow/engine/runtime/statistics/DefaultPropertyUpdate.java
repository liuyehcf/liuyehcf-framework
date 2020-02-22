package com.github.liuyehcf.framework.flow.engine.runtime.statistics;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class DefaultPropertyUpdate implements PropertyUpdate {

    /**
     * property update type
     *
     * @see PropertyUpdateType
     */
    private final PropertyUpdateType type;

    /**
     * property name
     */
    private final String name;

    /**
     * old property value
     */
    private final Object oldValue;

    /**
     * new property value
     */
    private final Object newValue;

    public DefaultPropertyUpdate(PropertyUpdateType type, String name, Object oldValue, Object newValue) {
        Assert.assertNotNull(type, "type");
        Assert.assertNotNull(name, "name");

        switch (type) {
            case CREATE:
                Assert.assertNull(oldValue);
                Assert.assertNotNull(newValue, "newValue");
                break;
            case DELETE:
                Assert.assertNotNull(oldValue, "oldValue");
                Assert.assertNull(newValue);
                break;
            case UPDATE:
                Assert.assertNotNull(oldValue, "oldValue");
                Assert.assertNotNull(newValue, "newValue");
                break;
            default:
                throw new UnsupportedOperationException();
        }

        this.type = type;
        this.name = name;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public final PropertyUpdateType getType() {
        return type;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Object getOldValue() {
        return oldValue;
    }

    @Override
    public final Object getNewValue() {
        return newValue;
    }
}
