package com.github.liuyehcf.framework.language.hua.core.bytecode.ir;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.liuyehcf.framework.language.hua.core.bytecode.ByteCode;

/**
 * 返回字节码的基类
 *
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
