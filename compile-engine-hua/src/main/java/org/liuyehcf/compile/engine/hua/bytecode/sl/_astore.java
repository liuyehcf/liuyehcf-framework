package org.liuyehcf.compile.engine.hua.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 存储对象（包括数组）
 * < before → after >
 * < objectref → >
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public class _astore extends StoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x3a;

    /**
     * 操作数数量
     */
    private static final int OPERATOR_NUM = 1;

    /**
     * 操作数类型
     */
    private static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 标志符偏移量
     */
    private final int offset;

    public _astore(int offset) {
        super(OPERATOR_CODE, OPERATOR_NUM, OPERATOR_CLASSES);
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate() {

    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{offset};
    }
}
