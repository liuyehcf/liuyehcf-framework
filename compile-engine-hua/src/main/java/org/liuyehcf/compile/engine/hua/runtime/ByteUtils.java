package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;

/**
 * Byte工具类
 *
 * @author hechenfeng
 * @date 2018/6/26
 */
abstract class ByteUtils {
    /**
     * 加载boolean
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return boolean值
     */
    static int loadBoolean(byte[] memory, int offset) {
        int res = 0;
        for (int i = 0; i < Type.BOOLEAN_TYPE_WIDTH; i++) {
            res |= (memory[offset + i] & 0xff) << (8 * i);
        }
        return res;
    }

    /**
     * 存储boolean值
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @param value  boolean值
     */
    static void storeBoolean(byte[] memory, int offset, int value) {
        for (int i = 0; i < Type.BOOLEAN_TYPE_WIDTH; i++) {
            memory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载char
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return char值
     */
    static int loadChar(byte[] memory, int offset) {
        int res = 0;
        for (int i = 0; i < Type.CHAR_TYPE_WIDTH; i++) {
            res |= (memory[offset + i] & 0xff) << (8 * i);
        }
        return res;
    }

    /**
     * 存储char值
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @param value  char值
     */
    static void storeChar(byte[] memory, int offset, int value) {
        for (int i = 0; i < Type.CHAR_TYPE_WIDTH; i++) {
            memory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载int
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return int值
     */
    static int loadInt(byte[] memory, int offset) {
        int res = 0;
        for (int i = 0; i < Type.INT_TYPE_WIDTH; i++) {
            res |= (memory[offset + i] & 0xff) << (8 * i);
        }
        return res;
    }

    /**
     * 存储int
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @param value  int值
     */
    static void storeInt(byte[] memory, int offset, int value) {
        for (int i = 0; i < Type.INT_TYPE_WIDTH; i++) {
            memory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载long
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return long值
     */
    static long loadLong(byte[] memory, int offset) {
        long res = 0;
        for (int i = 0; i < Type.LONG_TYPE_WIDTH; i++) {
            res |= (long) (memory[offset + i] & 0xff) << (8 * i);
        }
        return res;
    }

    /**
     * 存储long
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @param value  long值
     */
    static void storeLong(byte[] memory, int offset, long value) {
        for (int i = 0; i < Type.LONG_TYPE_WIDTH; i++) {
            memory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载float
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return float值
     */
    static float loadFloat(byte[] memory, int offset) {
        int res = 0;
        for (int i = 0; i < Type.INT_TYPE_WIDTH; i++) {
            res |= (memory[offset + i] & 0xff) << (8 * i);
        }
        return Float.intBitsToFloat(res);
    }

    /**
     * 存储float
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @param value  float值
     */
    static void storeFloat(byte[] memory, int offset, float value) {
        int intValue = Float.floatToIntBits(value);
        for (int i = 0; i < Type.INT_TYPE_WIDTH; i++) {
            memory[offset + i] = (byte) (intValue >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载double
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return double值
     */
    static double loadDouble(byte[] memory, int offset) {
        long res = 0;
        for (int i = 0; i < Type.LONG_TYPE_WIDTH; i++) {
            res |= (long) (memory[offset + i] & 0xff) << (8 * i);
        }
        return Double.longBitsToDouble(res);
    }

    /**
     * 存储double
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @param value  double值
     */
    static void storeDouble(byte[] memory, int offset, double value) {
        long longValue = Double.doubleToLongBits(value);
        for (int i = 0; i < Type.LONG_TYPE_WIDTH; i++) {
            memory[offset + i] = (byte) (longValue >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载reference
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return 引用
     */
    static Reference loadReference(byte[] memory, int offset) {
        int address = loadInt(memory, offset);
        int size = loadInt(memory, offset + 4);
        return new Reference(address, size);
    }

    /**
     * 存储reference
     *
     * @param memory    内存
     * @param offset    地址偏移量
     * @param reference 引用
     */
    static void storeReference(byte[] memory, int offset, Reference reference) {
        storeInt(memory, offset, reference.getAddress());
        storeInt(memory, offset + 4, reference.getSize());
    }
}
