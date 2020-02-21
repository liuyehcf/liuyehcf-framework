package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.SeedKeepAliveAck;

/**
 * @author hechenfeng
 * @date 2020/2/10
 */
public class SeedKeepAliveAckProcessor extends AbstractProcessor<SeedKeepAliveAck> {

    public SeedKeepAliveAckProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<SeedKeepAliveAck> context) {
    }
}
