package org.liuyehcf.compile.engine.hua.core.bytecode.cp;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;

/**
 * 运算指令的抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/13
 */
public abstract class Compute extends ByteCode {

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
