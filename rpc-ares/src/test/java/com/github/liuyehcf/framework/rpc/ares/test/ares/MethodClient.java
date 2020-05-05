package com.github.liuyehcf.framework.rpc.ares.test.ares;

import com.github.liuyehcf.framework.rpc.ares.AresMethod;
import com.github.liuyehcf.framework.rpc.ares.constant.HttpMethod;

/**
 * @author chenfeng.hcf
 * @date 2020/5/2
 */
public interface MethodClient {

    @AresMethod(path = "/method/options", method = HttpMethod.OPTIONS)
    String methodOptions();

    @AresMethod(path = "/method/get", method = HttpMethod.GET)
    String methodGet();

    @AresMethod(path = "/method/head", method = HttpMethod.HEAD)
    String methodHead();

    @AresMethod(path = "/method/post", method = HttpMethod.POST)
    String methodPost();

    @AresMethod(path = "/method/put", method = HttpMethod.PUT)
    String methodPut();

    @AresMethod(path = "/method/patch", method = HttpMethod.PATCH)
    String methodPatch();

    @AresMethod(path = "/method/delete", method = HttpMethod.DELETE)
    String methodDelete();
}
