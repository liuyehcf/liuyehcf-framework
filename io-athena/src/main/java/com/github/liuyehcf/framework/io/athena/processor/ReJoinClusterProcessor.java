package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.ReJoinCluster;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/10
 */
public class ReJoinClusterProcessor extends AbstractProcessor<ReJoinCluster> {

    public ReJoinClusterProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<ReJoinCluster> context) {
        ReJoinCluster event = context.getEvent();

        Member leader = event.getLeader();
        long version = event.getVersion();

        cluster.atomicOperate(() -> {
            if (Objects.equals(leader, cluster.getLeader())
                    && Objects.equals(version, cluster.getVersion())) {
                LOGGER.info("[{}] forced to rejoin the cluster", self);
                cluster.reset();
            }
        });
    }
}
