package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/10
 */
public interface AtomicExecutor {

    /**
     * start observer
     */
    void atomicOperate(Operation operation);

    interface Operation {

        /**
         * operation
         *
         * @throws Exception exception may occur
         */
        void operate() throws Exception;
    }
}
