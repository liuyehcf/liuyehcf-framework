package org.liuyehcf.compile.engine.hua.bytecode.oc;

/**
 * 多维数组创建指令，指定的维度有多个
 * < before → after >
 * < count1, [count2,...] → arrayref >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _multianewarray extends ObjectCreate {

    /**
     * 类型
     */
    private final String type;

    /**
     * 维度表达式大小
     */
    private final int expressionDimSize;

    public _multianewarray(String type, int expressionDimSize) {
        this.type = type;
        this.expressionDimSize = expressionDimSize;
    }

    public String getType() {
        return type;
    }

    public int getExpressionDimSize() {
        return expressionDimSize;
    }

    @Override
    public void operate() {

    }
}
