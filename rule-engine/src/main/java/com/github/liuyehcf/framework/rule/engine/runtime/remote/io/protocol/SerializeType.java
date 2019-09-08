package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public enum SerializeType {

    /**
     * java serialization
     *
     * @see com.github.liuyehcf.framework.rule.engine.util.CloneUtils#javaSerialize(Object)
     */
    java(0),

    /**
     * hessian serialization
     *
     * @see com.github.liuyehcf.framework.rule.engine.util.CloneUtils#hessianSerialize(Object)
     */
    hessian(1);

    private final int type;

    SerializeType(int type) {
        this.type = type;
    }

    public static SerializeType typeOf(int type) {
        for (SerializeType serializeType : values()) {
            if (serializeType.getType() == type) {
                return serializeType;
            }
        }
        throw new UnsupportedOperationException(String.format("Unsupported serialize type='%s'", type));
    }

    public int getType() {
        return type;
    }
}
