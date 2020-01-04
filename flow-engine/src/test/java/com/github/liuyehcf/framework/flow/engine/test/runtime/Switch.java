package com.github.liuyehcf.framework.flow.engine.test.runtime;

import java.util.concurrent.Callable;

public class Switch {

    private final Callable<Boolean> callable;

    public Switch(Callable<Boolean> callable) {
        this.callable = callable;
    }

    public boolean get() {
        try {
            return callable.call();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }
}
