package org.liuyehcf.compile.engine.hua.core.bytecode.ir;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;

/**
 * @author hechenfeng
 * @date 2018/6/25
 */
public abstract class Return extends ByteCode {

    /**
     * 操作数
     */
    private static final Object[] OPERATORS = new Object[0];

    @Override
    @JSONField(serialize = false)
    public Object[] getOperators() {
        return OPERATORS;
    }

}
