package com.github.liuyehcf.framework.compile.engine.test.example.calculator.code;

public class Load implements Code {

    private final long value;

    public Load(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
