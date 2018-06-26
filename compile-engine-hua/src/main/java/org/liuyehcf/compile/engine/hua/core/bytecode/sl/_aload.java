package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 加载对象（包括数组）
 * < before → after >
 * < → objectref >
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class _aload extends StoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x19;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 标志符序号
     */
    private final int order;

    public _aload(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public void operate() {

    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{order};
    }
}
