package com.github.liuyehcf.framework.rule.engine.util;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;

/**
 * @author hechenfeng
 * @date 2019/9/4
 */
public abstract class ByteUtils {

    public static byte toByte(int val) {
        return (byte) (val & 0xff);
    }

    public static byte[] toBytes(int val, int num) {
        Assert.assertTrue(1 <= num && num <= 4);
        byte[] bytes = new byte[num];
        if (num == 1) {
            bytes[0] = toByte(val);
        } else if (num == 2) {
            bytes[0] = toByte(val >> 8);
            bytes[1] = toByte(val);
        } else if (num == 3) {
            bytes[0] = toByte(val >> 16);
            bytes[1] = toByte(val >> 8);
            bytes[2] = toByte(val);
        } else {
            bytes[0] = toByte(val >> 24);
            bytes[1] = toByte(val >> 16);
            bytes[2] = toByte(val >> 8);
            bytes[3] = toByte(val);
        }
        return bytes;
    }

    public static int toInt(byte b) {
        return b & 0xff;
    }

    public static int toInt(byte b1, byte b2) {
        return toInt(b1) << 8 | toInt(b2);
    }
}
