package org.liuyehcf.compile.engine.expression.core.bytecode.ir;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.expression.core.bytecode.ByteCode;

/**
 * 返回字节码的基类
 *
 * @author hechenfeng
 * @date 2018/9/30
 */
public abstract class Return extends ByteCode {

    /**
     * 操作数
     */
    private static final Object[] OPERATORS = new Object[0];

    @Override
    @JSONField(serialize = false)
    public final Object[] getOperators() {
        return OPERATORS;
    }
}

