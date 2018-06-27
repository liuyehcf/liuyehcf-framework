package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;

/**
 * @author chenlu
 * @date 2018/6/26
 */
public abstract class ByteUtil {
    /**
     * 加载byte数组
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
     * 加载byte数组
     *
     * @param memory 内存
     * @param offset 地址偏移量
     * @return int值
     */
    static int loadReference(byte[] memory, int offset) {
        int res = 0;
        for (int i = 0; i < Type.REFERENCE_TYPE_WIDTH; i++) {
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
    static void storeReference(byte[] memory, int offset, int value) {
        for (int i = 0; i < Type.REFERENCE_TYPE_WIDTH; i++) {
            memory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }
}
