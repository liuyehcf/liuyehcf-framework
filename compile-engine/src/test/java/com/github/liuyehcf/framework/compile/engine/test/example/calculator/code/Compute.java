package com.github.liuyehcf.framework.compile.engine.test.example.calculator.code;

public interface Compute extends Code {

    /**
     * 计算操作数栈顶的两个数字
     *
     * @param val1 操作数1
     * @param val2 操作数2
     * @return 结果
     */
    long execute(long val1, long val2);
}
