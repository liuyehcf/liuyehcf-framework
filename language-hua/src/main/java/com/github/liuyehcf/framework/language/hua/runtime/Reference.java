package com.github.liuyehcf.framework.language.hua.runtime;

/**
 * 引用
 *
 * @author hechenfeng
 * @date 2018/6/27
 */
public class Reference {

    /**
     * 地址
     */
    private final int address;

    /**
     * 数组的大小，非数组为1
     */
    private final int size;

    public Reference(int address, int size) {
        this.address = address;
        this.size = size;
    }

    public int getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }
}
