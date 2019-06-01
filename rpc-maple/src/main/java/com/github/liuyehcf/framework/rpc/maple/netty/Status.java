package com.github.liuyehcf.framework.rpc.maple.netty;

import java.util.Objects;

/**
 * @author chenlu
 * @date 2019/4/3
 */
public enum Status {

    NORMAL((byte) 0),
    PROVIDER_EXCEPTION((byte) 1),

    ;
    private byte status;

    Status(byte status) {
        this.status = status;
    }

    public static Status of(byte status) {
        for (final Status value : values()) {
            if (Objects.equals(value.getStatus(), status)) {
                return value;
            }
        }
        throw new IllegalArgumentException();
    }

    public byte getStatus() {
        return status;
    }
}
