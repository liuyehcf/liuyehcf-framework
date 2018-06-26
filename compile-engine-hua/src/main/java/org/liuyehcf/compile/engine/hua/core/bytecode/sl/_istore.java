package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * int 存储
 * < before → after >
 * < value → >
 *
 * @author hechenfeng
 * @date 2018/6/8
 */
public class _istore extends StoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x36;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class, int.class};

    /**
     * 标志符序号
     */
    private final int order;

    /**
     * 标志符偏移量
     */
    @JSONField(serialize = false)
    private final int offset;

    public _istore(int order, int offset) {
        this.order = order;
        this.offset = offset;
    }

    public int getOrder() {
        return order;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public void operate(RuntimeContext context) {
        int value = context.pop();

        context.storeInt(offset, value);

        context.increaseCodeOffset();
    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{order, offset};
    }
}
