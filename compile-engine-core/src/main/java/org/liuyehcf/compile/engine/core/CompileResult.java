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
     * 错误信息
     */
    private final String errorMsg;

    /**
     * 编译结果
     */
    private final T result;

    public CompileResult(boolean success, String errorMsg, T result) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public T getResult() {
        return result;
    }
}
