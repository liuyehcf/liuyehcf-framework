package org.liuyehcf.compile.engine.core;

/**
 * 编译结果
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class CompileResult<T> {
    /**
     * 编译是否成功
     */
    private final boolean success;

    /**
     * 编译结果
     */
    private final T result;

    public CompileResult(boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }
}
