package org.liuyehcf.compile.engine.hua.bytecode.oc;

/**
 * 一维数组创建指令
 * < before → after >
 * < count → arrayref >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _newarray extends ObjectCreate {

    /**
     * 类型
     */
    private final String type;

    public _newarray(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void operate() {

    }
}
