package com.github.liuyehcf.framework.rpc.maple.test.common;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
public interface GreetService {
    /**
     * simple greet
     */
    String sayHello(String name);

    /**
     * complex request
     */
    MapleResult<BizResponse> request(BizRequest request);

    /**
     * throw Exception
     */
    void throwException() throws Throwable;
}