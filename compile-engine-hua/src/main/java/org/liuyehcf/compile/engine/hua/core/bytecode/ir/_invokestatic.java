package org.liuyehcf.compile.engine.hua.core.bytecode.ir;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeContext;

/**
 * 方法调用指令
 *
 * @author hechenfeng
 * @date 2018/6/10
 */
public class _invokestatic extends Invoke {

    /**
     * 唯一操作码
     */
    public static final int OPERATOR_CODE = 0xb8;

    /**
     * 操作数类型
     */
    public static final Class<?>[] OPERATOR_CLASSES = new Class<?>[]{int.class};

    /**
     * 常量池偏移量
     */
    private final int constantPoolOffset;

    public _invokestatic(int constantPoolOffset) {
        this.constantPoolOffset = constantPoolOffset;
    }

    public int getConstantPoolOffset() {
        return constantPoolOffset;
    }

    @Override
    public void operate(RuntimeContext context) {

    }

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return new Object[]{constantPoolOffset};
    }
}
