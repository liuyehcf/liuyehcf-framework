package org.liuyehcf.compile.engine.hua.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * 堆内存管理
 *
 * @author hechenfeng
 * @date 2018/6/26
 */
public class HeapMemoryManagement {

    /**
     * offset -> size
     */
    private static final Map<Integer, Integer> offsetMap = new HashMap<>();
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
    public static int allocate(int width, int size) {
        int total = width * size;

        int address = unAllocatedOffset;
        unAllocatedOffset += total;

        offsetMap.put(address, size);

        return address;
    }

    /**
     * 返回地址对应的size
     *
     * @param address 地址
     * @return size
     */
    public static int sizeOf(int address) {
        return offsetMap.get(address);
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
     * 加载reference值
     *
     * @param offset 地址偏移量
     * @return reference值
     */
    public static int loadReference(int offset) {
        return ByteUtil.loadReference(heapMemory, offset);
    }

    /**
     * 存储reference值
     *
     * @param offset 地址偏移量
     * @param value  reference值
     */
    public static void storeReference(int offset, int value) {
        ByteUtil.storeReference(heapMemory, offset, value);
    }
}
