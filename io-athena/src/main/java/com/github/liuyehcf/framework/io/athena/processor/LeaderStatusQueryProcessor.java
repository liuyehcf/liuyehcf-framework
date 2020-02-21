package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.LeaderStatusQuery;
import com.github.liuyehcf.framework.io.athena.event.LeaderStatusQueryResponse;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class LeaderStatusQueryProcessor extends AbstractProcessor<LeaderStatusQuery> {

    public LeaderStatusQueryProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<LeaderStatusQuery> context) {
        LeaderStatusQuery event = context.getEvent();
        boolean isLeaderHealthy = Member.isValidLeader(cluster.getLeader());
        LeaderStatusQueryResponse response = new LeaderStatusQueryResponse(event.getId(), event.getVersion(), isLeaderHealthy);
        envoy.whisper(context.getPeer(), response);
    }
}
