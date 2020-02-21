package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.LeaderRelieve;
import com.github.liuyehcf.framework.io.athena.event.SeedKeepAlive;
import com.github.liuyehcf.framework.io.athena.event.SeedKeepAliveAck;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/10
 */
public class SeedKeepAliveProcessor extends AbstractProcessor<SeedKeepAlive> {

    public SeedKeepAliveProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<SeedKeepAlive> context) {
        SeedKeepAlive event = context.getEvent();

        if (!self.isSeed()) {
            LOGGER.info("[{}] ignore 'SeedKeepAlive' due to self is not seed", self);
            return;
        }
        long peerVersion = event.getVersion();
        long localVersion = cluster.getVersion();
        Member peerLeader = event.getLeader();
        Member localLeader = cluster.getLeader();
        if (localLeader != null) {
            localLeader = localLeader.copy();
        }

        if (peerLeader == null || localLeader == null) {
            envoy.whisperIgnoreStatus(context.getPeer(), new SeedKeepAliveAck(), false);
            return;
        }

        if (Objects.equals(peerLeader, localLeader)) {
            envoy.whisperIgnoreStatus(context.getPeer(), new SeedKeepAliveAck(), false);
            return;
        }

        if (Member.isValidLeader(peerLeader) && Member.isValidLeader(localLeader)) {
            if (peerVersion < localVersion) {
                LOGGER.info("leader conflict, winner=[{}], loser=[{}]", localLeader, peerLeader);
                envoy.whisperIgnoreStatus(peerLeader, new LeaderRelieve(), true);
            } else if (peerVersion > localVersion) {
                LOGGER.info("leader conflict, winner=[{}], loser=[{}]", peerLeader, localLeader);
                envoy.whisperIgnoreStatus(localLeader, new LeaderRelieve(), true);
            } else {
                if (peerLeader.compareTo(localLeader) < 0) {
                    LOGGER.info("leader conflict, winner=[{}], loser=[{}]", localLeader, peerLeader);
                    envoy.whisperIgnoreStatus(peerLeader, new LeaderRelieve(), true);
                } else {
                    LOGGER.info("leader conflict, winner=[{}], loser=[{}]", peerLeader, localLeader);
                    envoy.whisperIgnoreStatus(localLeader, new LeaderRelieve(), true);
                }
            }
        }
        envoy.whisperIgnoreStatus(context.getPeer(), new SeedKeepAliveAck(), false);
    }
}
