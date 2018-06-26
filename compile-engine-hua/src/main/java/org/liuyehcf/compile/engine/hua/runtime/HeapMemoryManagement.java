package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;

/**
 * 堆内存管理
 *
 * @author chenlu
 * @date 2018/6/26
 */
public class HeapMemoryManagement {

    /**
     * 堆内存
     */
    private static byte[] heapMemory = null;

    /**
     * 未分配内存起始地址
     */
    private static int unAllocatedOffset;

    /**
     * 初始化
     */
    public static void init() {
        if (heapMemory == null) {
            heapMemory = new byte[64 * 1000000];
        }
    }

    /**
     * 分配内存
     *
     * @param width 类型宽度
     * @param dim   维度
     * @return 起始地址
     */
    public static int allocate(int width, int dim) {
        int total = width * dim;

        /*
         * 宽度为0时，分配一个byte
         * 例如new int[0]时
         */
        if (total == 0) {
            int address = unAllocatedOffset;
            unAllocatedOffset += 1;
            return address;
        } else {
            int address = unAllocatedOffset;
            unAllocatedOffset += total;
            return address;
        }
    }

    /**
     * 加载byte数组
     *
     * @param offset 地址偏移量
     * @return int值
     */
    public static int loadInt(int offset) {
        int res = 0;
        for (int i = 0; i < Type.TYPE_INT.getTypeWidth(); i++) {
            res |= (heapMemory[offset + i] & 0xff) << (8 * i);
        }
        return res;
    }

    /**
     * 存储int值
     *
     * @param offset 地址偏移量
     * @param value  int值
     */
    public static void storeInt(int offset, int value) {
        for (int i = 0; i < Type.TYPE_INT.getTypeWidth(); i++) {
            heapMemory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }

    /**
     * 加载Boolean
     *
     * @param offset 地址偏移量
     * @return boolean值
     */
    public static int loadBoolean(int offset) {
        int res = 0;
        for (int i = 0; i < Type.TYPE_BOOLEAN.getTypeWidth(); i++) {
            res |= (heapMemory[offset + i] & 0xff) << (8 * i);
        }
        return res;
    }

    /**
     * 存储Boolean
     *
     * @param offset 地址偏移量
     * @param value  boolean值
     */
    public static void storeBoolean(int offset, int value) {
        for (int i = 0; i < Type.TYPE_BOOLEAN.getTypeWidth(); i++) {
            System.out.println((value >> (8 * i) & 0xff));
            heapMemory[offset + i] = (byte) (value >> (8 * i) & 0xff);
        }
    }
}
