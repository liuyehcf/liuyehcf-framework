package org.liuyehcf.compile.engine.hua.bytecode.oc;

/**
 * 多维数组创建指令，指定的维度是第一维
 * < before → after >
 * < count → arrayref >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _anewarray extends ObjectCreate {

    public static final int OPERATOR_CODE = 0xbd;
    /**
     * 类型
     * todo 这里应该是一个常量池引用
     */
    private final String type;

    public _anewarray(String type) {
        super(OPERATOR_CODE, 1, new int[]{4});
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void operate() {

    }
}
