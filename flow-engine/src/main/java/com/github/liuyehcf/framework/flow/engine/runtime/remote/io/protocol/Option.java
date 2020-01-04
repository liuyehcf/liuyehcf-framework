package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.protocol;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;

/**
 * @author hechenfeng
 * @date 2019/9/4
 */
public class Option {

    /**
     * option key
     * 2 bytes, 1 byte for type, 1 byte for length
     */
    private final OptionType type;

    /**
     * option value
     */
    private final byte[] value;

    Option(OptionType type, byte[] value) {
        Assert.assertNotNull(type, "type");
        this.type = type;
        if (type.getLength() > 0) {
            Assert.assertNotNull(value, "value");
            Assert.assertEquals(type.getLength(), value.length);
            this.value = value;
        } else {
            this.value = null;
        }
    }

    public OptionType getType() {
        return type;
    }

    public byte[] getValue() {
        return value;
    }
}
