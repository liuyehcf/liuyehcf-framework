package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.LeaderElected;
import com.github.liuyehcf.framework.io.athena.event.LeaderKeepAlive;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/12
 */
public class LeaderElectedProcessor extends AbstractProcessor<LeaderElected> {

    public LeaderElectedProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<LeaderElected> context) {
        LeaderElected event = context.getEvent();

        cluster.atomicOperate(() -> {
            Member leader = cluster.getMember(event.getLeader());
            long version = event.getVersion();
            if (leader != null
                    && leader.getStatus().isActive()
                    && leader.getRole().isFollower()
                    && version > cluster.getVersion()) {
                LOGGER.info("[{}] accept [{}] as the new leader", self, leader);
                envoy.acceptLeader(leader, version);

                if (Member.isValidLeader(self)) {
                    // if not do so, member may be moving to unreachable
                    // in some cases that heartbeatTimeout is small
                    // and other member receive LeaderElected first then send LeaderKeepAlive to new leader
                    // but the new leader did not receive the LeaderElected event so just ignore the LeaderKeepAlive
                    for (Member member : cluster.getMembers()) {
                        if (!Objects.equals(self, member)) {
                            envoy.receiveLeaderKeepAlive(member);
                        }
                    }
                } else {
                    envoy.receiveLeaderKeepAliveAck();
                    envoy.whisper(leader, new LeaderKeepAlive(cluster.getAbstractInfo(), cluster.getVersion()));
                    LOGGER.debug("[{}] send 'LeaderKeepAlive' to [{}] after leader election", self, leader);
                }
            }
        });
    }
}
