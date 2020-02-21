package com.github.liuyehcf.framework.io.athena.test;

import com.github.liuyehcf.framework.io.athena.Address;
import com.github.liuyehcf.framework.io.athena.AthenaConfig;
import com.github.liuyehcf.framework.io.athena.Envoy;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.processor.AbstractProcessor;
import com.github.liuyehcf.framework.io.athena.processor.Processor;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class TestTypeMatcher {

    @Test
    public void testObject() {
        Processor<Object> processor = new AbstractProcessor<Object>(buildSpy()) {
        };

        Assert.assertTrue(processor.match(1));
        Assert.assertTrue(processor.match("a"));
    }

    @Test
    public void testGeneric() {
        Processor<List<String>> processor = new AbstractProcessor<List<String>>(buildSpy()) {
        };

        Assert.assertFalse(processor.match(1));
        Assert.assertFalse(processor.match("a"));
        Assert.assertTrue(processor.match(new ArrayList<Integer>()));
        Assert.assertTrue(processor.match(new ArrayList<>()));
    }

    private UnsafeEnvoy buildSpy() {
        AthenaConfig config = new AthenaConfig();
        config.setPort(10001);
        config.setHeartbeatTimeout(3);
        config.setSeeds(Lists.newArrayList(Address.of("127.0.0.1", 10001)));
        config.setWorkerId(1);

        return (UnsafeEnvoy) Envoy.create(config);
    }
}
