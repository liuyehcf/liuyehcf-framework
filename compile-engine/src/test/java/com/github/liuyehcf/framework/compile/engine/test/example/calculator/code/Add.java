package com.github.liuyehcf.framework.compile.engine.test.example.calculator.code;

public class Add implements Compute {

    @Override
    public long execute(long val1, long val2) {
        return val1 + val2;
    }
}
