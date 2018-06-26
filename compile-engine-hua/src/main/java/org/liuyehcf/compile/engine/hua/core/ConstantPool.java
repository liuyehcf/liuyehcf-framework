package org.liuyehcf.compile.engine.hua.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量池
 *
 * @author hechenfeng
 * @date 2018/6/24
 */
public class ConstantPool {

    /**
     * 常量池
     */
    private final List<String> constants = new ArrayList<>();

    /**
     * 常量 -> 常量对应的偏移量
     */
    private final Map<String, Integer> constantPool = new LinkedHashMap<>();

    /**
     * 常量偏移量计数值
     */
    private int offsetCnt;

    public ConstantPool() {
        this.offsetCnt = 0;
    }

    public void addConstant(String constant) {
        if (constantPool.containsKey(constant)) {
            return;
        }
        constants.add(constant);
        constantPool.put(constant, offsetCnt++);
    }

    int getConstantOffset(String constant) {
        if (!constantPool.containsKey(constant)) {
            return -1;
        }
        return constantPool.get(constant);
    }

    public List<String> getConstants() {
        return constants;
    }
}
