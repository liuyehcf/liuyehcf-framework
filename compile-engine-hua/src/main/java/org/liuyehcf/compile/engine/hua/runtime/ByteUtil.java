package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;

/**
 * Byte工具类
 *
 * @author hechenfeng
 * @date 2018/6/26
 */
abstract class ByteUtil {
    /**
     * 加载int
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return int值
     */
    static int loadInt(byte[] memory, int offset) {
        int res = 0;
        for (int i = 0; i < Type.TYPE_INT.getTypeWidth(); i++) {
            res |= (memory[offset + i] & 0xff) << (8 * i);
        }
        return res;
    }

    /**
     * 存储int值
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @param value  int值
     */
    static void storeInt(byte[] memory, int offset, int value) {
        for (int i = 0; i < Type.TYPE_INT.getTypeWidth(); i++) {
            memory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载boolean
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return boolean值
     */
    static int loadBoolean(byte[] memory, int offset) {
        int res = 0;
        for (int i = 0; i < Type.TYPE_BOOLEAN.getTypeWidth(); i++) {
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
        for (int i = 0; i < Type.TYPE_BOOLEAN.getTypeWidth(); i++) {
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
        for (int i = 0; i < Type.TYPE_CHAR.getTypeWidth(); i++) {
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
        for (int i = 0; i < Type.TYPE_CHAR.getTypeWidth(); i++) {
            memory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载reference
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return reference值
     */
    static int loadReference(byte[] memory, int offset) {
        int res = 0;
        for (int i = 0; i < Type.REFERENCE_TYPE_WIDTH; i++) {
            res |= (memory[offset + i] & 0xff) << (8 * i);
        }
        return res;
    }

    /**
     * 存储reference
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @param value  reference值
     */
    static void storeReference(byte[] memory, int offset, int value) {
        for (int i = 0; i < Type.REFERENCE_TYPE_WIDTH; i++) {
            memory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }
}
