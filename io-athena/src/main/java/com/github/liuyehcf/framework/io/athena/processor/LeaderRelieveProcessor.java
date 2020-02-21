package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.LeaderRelieve;
import com.github.liuyehcf.framework.io.athena.event.ReJoinCluster;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/10
 */
public class LeaderRelieveProcessor extends AbstractProcessor<LeaderRelieve> {

    public LeaderRelieveProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<LeaderRelieve> context) {
        Member localLeader = cluster.getLeader();
        if (localLeader != null) {
            localLeader = localLeader.copy();
        }

        if (localLeader == null) {
            LOGGER.info("[{}] ignore 'LeaderRelieve' due to self is not leader", self);
            return;
        }

        // event must be create before check
        ReJoinCluster reJoinCluster = new ReJoinCluster(localLeader, cluster.getVersion());

        if (!Objects.equals(self, localLeader)) {
            LOGGER.info("[{}] ignore 'LeaderRelieve' due to self is not leader", self);
            return;
        }
        if (!Member.isValidLeader(localLeader)) {
            LOGGER.info("[{}] ignore 'LeaderRelieve' due to self is not in valid leader status", self);
            return;
        }

        LOGGER.info("[{}] forced to relieve", self);

        envoy.roar(reJoinCluster, true);
    }
}
