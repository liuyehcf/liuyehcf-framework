package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.StatisticsResult;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.LeaderStatusQueryResponse;
import com.github.liuyehcf.framework.io.athena.event.Proposal;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class LeaderStatusQueryResponseProcessor extends AbstractProcessor<LeaderStatusQueryResponse> {

    public LeaderStatusQueryResponseProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<LeaderStatusQueryResponse> context) {
        LeaderStatusQueryResponse event = context.getEvent();

        LOGGER.info("[{}] receive 'LeaderStatusQueryResponse' from [{}], isLeaderHealthy={}",
                self, context.getPeer(), event.isHealthy());

        StatisticsResult<Void> result = envoy.statisticsLeaderStatusQuery(event);
        StatisticsResult.Status status = result.getStatus();
        if (status.isAccepted()) {
            // event must be create before check
            Proposal proposal = new Proposal(envoy.nextId(), Proposal.Stage.pre, self, event.getVersion() + 1, self);
            if (self.getStatus().isActive()
                    // this following two conditions guarantee that
                    // the status of cluster is the same as start leader status query process
                    && cluster.getLeader() == null
                    && Objects.equals(event.getVersion(), cluster.getVersion())) {
                LOGGER.info("[{}] start leader election and recommend self as leader, id={}; stage={}; proposer={}; version={}; candidate={}",
                        self, proposal.getId(), proposal.getStage(), proposal.getProposer(), proposal.getVersion(), proposal.getCandidate());
                envoy.roar(proposal, true);
            }
        } else if (status.isAlreadyAccepted()) {
            LOGGER.debug("[{}] has already started leader election", self);
        } else if (status.isRejected()) {
            LOGGER.info("[{}] has been forbidden to start leader election, reason={}", self, result.getReason());
        } else if (status.isAlreadyRejected()) {
            LOGGER.debug("[{}] has already been forbidden to start leader election", self);
        } else {
            LOGGER.debug("[{}] is waiting for more 'LeaderStatusQueryResponse'", self);
        }
    }

    @Override
    protected boolean needLog() {
        return false;
    }
}
