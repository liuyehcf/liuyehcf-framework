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

    public static final int OPERATOR_CODE = 0xbc;
    /**
     * 类型
     * todo 这里应该是一个常量池引用
     */
    private final String type;

    public _newarray(String type) {
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
