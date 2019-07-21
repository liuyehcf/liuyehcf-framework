package com.github.liuyehcf.framework.rpc.maple;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/3/24
 */
public enum SerializeType {

    JAVA((byte) 0, "java"),
    HESSIAN((byte) 1, "hessian");

    private byte code;

    private String type;

    SerializeType(byte code, String type) {
        this.code = code;
        this.type = type;
    }

    public static SerializeType of(String type) {
        for (final SerializeType serializeType : values()) {
            if (Objects.equals(serializeType.getType(), type)) {
                return serializeType;
            }
        }

        throw new UnsupportedOperationException(String.format("illegal serialize type %s", type));
    }

    public static SerializeType of(byte code) {
        for (final SerializeType serializeType : values()) {
            if (Objects.equals(serializeType.getCode(), code)) {
                return serializeType;
            }
        }

        throw new UnsupportedOperationException(String.format("illegal serialize code %d", code));
    }

    public byte getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
