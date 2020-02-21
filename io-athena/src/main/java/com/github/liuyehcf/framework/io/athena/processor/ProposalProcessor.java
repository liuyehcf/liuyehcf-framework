package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.Proposal;
import com.github.liuyehcf.framework.io.athena.event.Vote;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class ProposalProcessor extends AbstractProcessor<Proposal> {

    public ProposalProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<Proposal> context) {
        Proposal event = context.getEvent();

        LOGGER.info("[{}] receive 'Proposal' from [{}], id={}; stage={}; proposer={}; version={}; candidate={}",
                self, context.getPeer(), event.getId(), event.getStage(), event.getProposer(), event.getVersion(), event.getCandidate());

        if (event.getStage().isPre()) {
            Vote vote = envoy.voteForPreProposal(event);
            envoy.whisper(context.getPeer(), vote);
        } else {
            Vote vote = envoy.voteForFormalProposal(event);
            envoy.whisper(context.getPeer(), vote);
        }
    }

    @Override
    protected boolean needLog() {
        return false;
    }
}