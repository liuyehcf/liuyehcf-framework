package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.StatisticsResult;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.LeaderElected;
import com.github.liuyehcf.framework.io.athena.event.Proposal;
import com.github.liuyehcf.framework.io.athena.event.Vote;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class VoteProcessor extends AbstractProcessor<Vote> {

    public VoteProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<Vote> context) {
        Vote event = context.getEvent();

        LOGGER.info("[{}] receive 'Vote' from [{}], id={}; stage={}; accept={}; proposer={}; version={}; candidate={}",
                self, context.getPeer(), event.getId(), event.getStage(), event.isAccept(), event.getProposer(), event.getVersion(), event.getCandidate());

        if (event.getStage().isPre()) {
            StatisticsResult<Member> result = envoy.statisticsPreProposal(event);
            StatisticsResult.Status status = result.getStatus();
            if (status.isAccepted()) {
                Member candidate = result.getValue();
                LOGGER.info("[{}]'s pre-proposal has been accepted, now start formal-proposal, id={}; candidate=[{}]", self, event.getId(), candidate);
                envoy.roar(new Proposal(
                        event.getId(),
                        Proposal.Stage.formal,
                        self,
                        event.getVersion(),
                        candidate
                ), true);
            } else if (status.isAlreadyAccepted()) {
                LOGGER.debug("[{}]'s pre-proposal has already been accepted, id={}", self, event.getId());
            } else if (status.isRejected()) {
                LOGGER.info("[{}]'s pre-proposal has been rejected, id={}; reason={}", self, event.getId(), result.getReason());
            } else if (status.isAlreadyRejected()) {
                LOGGER.debug("[{}]'s pre-proposal has already been rejected, id={}", self, event.getId());
            } else {
                LOGGER.debug("[{}]'s pre-proposal is undetermined, id={}", self, event.getId());
            }
        } else {
            StatisticsResult<Member> result = envoy.statisticsFormalProposal(event);
            StatisticsResult.Status status = result.getStatus();
            if (status.isAccepted()) {
                Member candidate = result.getValue();
                LOGGER.info("[{}]'s formal-proposal has been accepted, [{}] becomes the leader, id={}", self, candidate, event.getId());
                envoy.roar(new LeaderElected(candidate, event.getVersion()), true);
            } else if (status.isAlreadyAccepted()) {
                LOGGER.debug("[{}]'s formal-proposal has already been accepted, id={}", self, event.getId());
            } else if (status.isRejected()) {
                LOGGER.info("[{}]'s formal-proposal has been rejected, id={}; reason={}", self, event.getId(), result.getReason());
            } else if (status.isAlreadyRejected()) {
                LOGGER.debug("[{}]'s formal-proposal has already been rejected, id={}", self, event.getId());
            } else {
                LOGGER.debug("[{}]'s formal-proposal is undetermined, id={}", self, event.getId());
            }
        }
    }

    @Override
    protected boolean needLog() {
        return false;
    }
}
