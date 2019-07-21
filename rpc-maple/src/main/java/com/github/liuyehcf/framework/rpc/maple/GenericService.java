package com.github.liuyehcf.framework.rpc.maple;

/**
 * @author hechenfeng
 * @date 2019/4/3
 */
public interface GenericService {

    /**
     * generic invoke
     *
     * @param methodName     method name
     * @param parameterTypes type array of args
     * @param args           args
     * @return result
     */
    Object genericInvoke(String methodName, String[] parameterTypes, Object[] args);
}
