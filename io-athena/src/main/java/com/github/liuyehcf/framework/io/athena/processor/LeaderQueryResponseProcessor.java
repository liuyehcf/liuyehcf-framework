package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Identifier;
import com.github.liuyehcf.framework.io.athena.LeaderQueryNotifier;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.JoinClusterRequest;
import com.github.liuyehcf.framework.io.athena.event.LeaderQueryResponse;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class LeaderQueryResponseProcessor extends AbstractProcessor<LeaderQueryResponse> {

    public LeaderQueryResponseProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<LeaderQueryResponse> context) {
        LeaderQueryResponse event = context.getEvent();

        LOGGER.debug("[{}] receive 'LeaderQueryResponse' from [{}], leader=[{}]",
                self, context.getPeer(), event.getIdentifier());

        LeaderQueryNotifier.receive(event);

        Identifier identifier = event.getIdentifier();
        if (identifier != null) {
            envoy.whisperIgnoreStatus(identifier, new JoinClusterRequest(self), true);
        }
    }

    @Override
    protected boolean needLog() {
        return false;
    }
}
