package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.JoinClusterResponse;
import com.github.liuyehcf.framework.io.athena.event.MemberActiveEvent;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class JoinClusterResponseProcessor extends AbstractProcessor<JoinClusterResponse> {

    public JoinClusterResponseProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<JoinClusterResponse> context) {
        JoinClusterResponse event = context.getEvent();

        if (event.isAccept()) {
            LOGGER.info("[{}]'s request of join cluster has been accepted by [{}]", self, context.getPeer());
            cluster.atomicOperate(() -> {
                Member leader = event.getLeader();
                long version = event.getVersion();
                if (cluster.addMember(leader)) {
                    envoy.enqueue(new MemberActiveEvent(leader));
                }
                envoy.acceptLeader(leader, version);
            });
            envoy.receiveLeaderKeepAliveAck();
        } else {
            LOGGER.info("[{}]'s request of join cluster has been rejected by [{}], reason={}", self, context.getPeer(), event.getReason());
        }
    }
}
