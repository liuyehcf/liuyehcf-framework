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
     * @param count 连续元素的个数
     * @return 起始地址
     */
    public static int allocate(int width, int count) {
        int total = width * count;

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
     * 加载int
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
     * 加载reference
     *
     * @param offset 地址偏移量
     * @return int值
     */
    public static int loadReference(int offset) {
        return ByteUtil.loadReference(heapMemory, offset);
    }

    /**
     * 存储reference
     *
     * @param offset 地址偏移量
     * @param value  int值
     */
    public static void storeReference(int offset, int value) {
        ByteUtil.storeReference(heapMemory, offset, value);
    }
}
