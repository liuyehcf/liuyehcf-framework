package org.liuyehcf.compile.engine.hua.bytecode.oc;

/**
 * 多维数组创建指令，指定的维度是第一维
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _anewarray extends ObjectCreate {

    /**
     * 类型
     */
    private final String type;

    public _anewarray(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void operate() {

    }
}
