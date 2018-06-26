package org.liuyehcf.compile.engine.hua.core.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * int 加载
 * < before → after >
 * < → value >
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _iload extends StoreLoad {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0x15;

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

    public _iload(int order, int offset) {
        this.order = order;
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public void operate(RuntimeContext context) {

    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{order};
    }
}
