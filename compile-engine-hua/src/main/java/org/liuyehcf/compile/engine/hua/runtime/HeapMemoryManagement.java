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
     * 常量引用
     */
    private static final Map<String, Reference> constantReferences = new HashMap<>();
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
     * 注册常量引用
     *
     * @param constant  常量内容
     * @param reference 常量引用
     */
    static void registerConstant(String constant, Reference reference) {
        constantReferences.put(constant, reference);
    }

    /**
     * 获取常量引用
     *
     * @param constant 常量
     * @return 常量引用
     */
    public static Reference getConstantReference(String constant) {
        return constantReferences.get(constant);
    }

    /**
     * 加载boolean
     *
     * @param offset 地址偏移量
     * @return boolean值
     */
    public static int loadBoolean(int offset) {
        return ByteUtils.loadBoolean(heapMemory, offset);
    }

    /**
     * 存储boolean
     *
     * @param offset 地址偏移量
     * @param value  boolean值
     */
    public static void storeBoolean(int offset, int value) {
        ByteUtils.storeBoolean(heapMemory, offset, value);
    }

    /**
     * 加载char
     *
     * @param offset 地址偏移量
     * @return char值
     */
    public static int loadChar(int offset) {
        return ByteUtils.loadChar(heapMemory, offset);
    }

    /**
     * 存储char
     *
     * @param offset 地址偏移量
     * @param value  char值
     */
    public static void storeChar(int offset, int value) {
        ByteUtils.storeChar(heapMemory, offset, value);
    }

    /**
     * 加载int
     *
     * @param offset 地址偏移量
     * @return int值
     */
    public static int loadInt(int offset) {
        return ByteUtils.loadInt(heapMemory, offset);
    }

    /**
     * 存储int
     *
     * @param offset 地址偏移量
     * @param value  int值
     */
    public static void storeInt(int offset, int value) {
        ByteUtils.storeInt(heapMemory, offset, value);
    }

    /**
     * 加载long
     *
     * @param offset 地址偏移量
     * @return long值
     */
    public static long loadLong(int offset) {
        return ByteUtils.loadLong(heapMemory, offset);
    }

    /**
     * 存储long
     *
     * @param offset 地址偏移量
     * @param value  long值
     */
    public static void storeLong(int offset, long value) {
        ByteUtils.storeLong(heapMemory, offset, value);
    }

    /**
     * 加载引用
     *
     * @param offset 地址偏移量
     * @return 引用
     */
    public static Reference loadReference(int offset) {
        return ByteUtils.loadReference(heapMemory, offset);
    }

    /**
     * 存储引用
     *
     * @param offset    地址偏移量
     * @param reference 引用
     */
    public static void storeReference(int offset, Reference reference) {
        ByteUtils.storeReference(heapMemory, offset, reference);
    }
}
