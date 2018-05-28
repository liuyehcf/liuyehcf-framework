package org.liuyehcf.compile.engine.core;

/**
 * 编译结果
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class CompileResult {
    /**
     * 编译是否成功
     */
    private final boolean success;

    /**
     * 编译结果
     */
    private final Object result;

    public CompileResult(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getResult() {
        return result;
    }
}
