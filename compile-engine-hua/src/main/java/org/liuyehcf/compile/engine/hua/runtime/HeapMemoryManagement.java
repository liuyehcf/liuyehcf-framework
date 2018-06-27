package org.liuyehcf.compile.engine.hua.runtime;

/**
 * 堆内存管理
 *
 * @author hechenfeng
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
     * @param size  连续元素的个数
     * @return 起始地址
     */
    public static Reference allocate(int width, int size) {
        int total = width * size;

        int address = unAllocatedOffset;
        unAllocatedOffset += total;

        return new Reference(address, size);
    }

    /**
     * 加载int值
     *
     * @param offset 地址偏移量
     * @return int值
     */
    public static int loadInt(int offset) {
        return ByteUtil.loadInt(heapMemory, offset);
    }

    /**
     * 存储int值
     *
     * @param offset 地址偏移量
     * @param value  int值
     */
    public static void storeInt(int offset, int value) {
        ByteUtil.storeInt(heapMemory, offset, value);
    }

    /**
     * 加载boolean
     *
     * @param offset 地址偏移量
     * @return boolean值
     */
    public static int loadBoolean(int offset) {
        return ByteUtil.loadBoolean(heapMemory, offset);
    }

    /**
     * 存储char值
     *
     * @param offset 地址偏移量
     * @param value  char值
     */
    public static void storeBoolean(int offset, int value) {
        ByteUtil.storeBoolean(heapMemory, offset, value);
    }

    /**
     * 加载char值
     *
     * @param offset 地址偏移量
     * @return char值
     */
    public static int loadChar(int offset) {
        return ByteUtil.loadChar(heapMemory, offset);
    }

    /**
     * 存储char值
     *
     * @param offset 地址偏移量
     * @param value  char值
     */
    public static void storeChar(int offset, int value) {
        ByteUtil.storeChar(heapMemory, offset, value);
    }

    /**
     * 加载引用
     *
     * @param offset 地址偏移量
     * @return 引用
     */
    public static Reference loadReference(int offset) {
        return ByteUtil.loadReference(heapMemory, offset);
    }

    /**
     * 存储引用
     *
     * @param offset    地址偏移量
     * @param reference 引用
     */
    public static void storeReference(int offset, Reference reference) {
        ByteUtil.storeReference(heapMemory, offset, reference);
    }
}
