package com.github.liuyehcf.framework.rpc.ares.test;

import com.github.liuyehcf.framework.rpc.ares.constant.HttpMethod;
import com.github.liuyehcf.framework.rpc.ares.test.ares.MethodClient;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author hechenfeng
 * @date 2020/5/2
 */
public class TestMethod extends BaseConfig {

    @Resource
    private MethodClient methodClient;

    @Test
    public void testOptions() {
        String result = methodClient.methodOptions();
        Assert.assertEquals(HttpMethod.OPTIONS.name(), result);
    }

    @Test
    public void testGet() {
        String result = methodClient.methodGet();
        Assert.assertEquals(HttpMethod.GET.name(), result);
    }

    @Test
    public void testHead() {
        String result = methodClient.methodHead();
        Assert.assertNull(result);
    }

    @Test
    public void testPost() {
        String result = methodClient.methodPost();
        Assert.assertEquals(HttpMethod.POST.name(), result);
    }

    @Test
    public void testPut() {
        String result = methodClient.methodPut();
        Assert.assertEquals(HttpMethod.PUT.name(), result);
    }

    @Test
    public void testPatch() {
        String result = methodClient.methodPatch();
        Assert.assertEquals(HttpMethod.PATCH.name(), result);
    }

    @Test
    public void testDelete() {
        String result = methodClient.methodDelete();
        Assert.assertEquals(HttpMethod.DELETE.name(), result);
    }
}
