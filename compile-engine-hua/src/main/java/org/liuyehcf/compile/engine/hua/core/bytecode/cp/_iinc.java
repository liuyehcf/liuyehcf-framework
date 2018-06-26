package org.liuyehcf.compile.engine.hua.core.bytecode.cp;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * int 自增
 * < before → after >
 * <  →  >
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class _iinc extends Compute {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x84;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class, int.class, int.class};

    /**
     * 标志符序号
     */
    private int order;

    /**
     * 标志符偏移量
     */
    @JSONField(serialize = false)
    private int offset;

    /**
     * 增量
     */
    private int increment;

    public _iinc() {
    }

    public _iinc(int order, int increment) {
        this.order = order;
        this.increment = increment;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void operate(RuntimeContext context) {

    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{order, increment};
    }
}
