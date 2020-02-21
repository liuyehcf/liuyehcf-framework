package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.LeaderQuery;
import com.github.liuyehcf.framework.io.athena.event.LeaderQueryResponse;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class LeaderQueryProcessor extends AbstractProcessor<LeaderQuery> {

    public LeaderQueryProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<LeaderQuery> context) {
        LeaderQuery event = context.getEvent();

        LeaderQueryResponse response = new LeaderQueryResponse(event.getId(), cluster.getLeader());
        envoy.whisperIgnoreStatus(context.getPeer(), response, false);
    }
}
