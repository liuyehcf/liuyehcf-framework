package org.liuyehcf.compile.engine.hua.bytecode.cp;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * int 自增
 * < before → after >
 * <  →  >
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class _iinc extends ByteCode {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x84;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class, int.class};

    /**
     * 偏移量
     */
    private int offset;

    /**
     * 增量
     */
    private int increment;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    @Override
    public void operate() {

    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{offset, increment};
    }
}
