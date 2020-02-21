package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.LeaderKeepAliveAck;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class LeaderKeepAliveAckProcessor extends AbstractProcessor<LeaderKeepAliveAck> {

    public LeaderKeepAliveAckProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<LeaderKeepAliveAck> context) {
        envoy.receiveLeaderKeepAliveAck();
    }
}
