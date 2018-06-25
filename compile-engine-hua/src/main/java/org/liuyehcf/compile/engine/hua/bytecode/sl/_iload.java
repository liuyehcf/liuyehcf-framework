package org.liuyehcf.compile.engine.hua.bytecode.sl;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

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
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    static {
        ByteCode.register(OPERATOR_CODE, _iload.class);
    }

    /**
     * 偏移量
     */
    private final int offset;

    public _iload(int offset) {
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
