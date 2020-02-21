package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.*;
import com.github.liuyehcf.framework.io.athena.event.*;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class JoinClusterRequestProcessor extends AbstractProcessor<JoinClusterRequest> {

    public JoinClusterRequestProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<JoinClusterRequest> context) {
        JoinClusterRequest event = context.getEvent();

        Identifier identifier = event.getIdentifier();
        if (Member.isValidLeader(self)) {
            cluster.atomicOperate(() -> {
                Member member = new Member(
                        identifier.getName(),
                        identifier.getHost(),
                        identifier.getPort(),
                        event.isSeed(),
                        MemberRole.follower,
                        MemberStatus.joining
                );
                if (!cluster.addMember(member)) {
                    LOGGER.info("[{}] reject [{}]'s joining request due to duplicate address", self, context.getPeer());
                    envoy.whisperIgnoreStatus(member, new JoinClusterResponse(false, null, cluster.getVersion(), "duplicate member name"), false);
                    return;
                }

                LOGGER.info("[{}] add [{}] to the cluster, and moving it's status to [{}]", self, context.getPeer(), MemberStatus.joining);
                envoy.whisperIgnoreStatus(member, new JoinClusterResponse(true, cluster.getLeader(), cluster.getVersion(), "success"), false);

                LOGGER.info("[{}] is moving [{}]'s status to [{}]", self, context.getPeer(), MemberStatus.active);
                cluster.updateMemberStatus(member, MemberStatus.active);

                envoy.roar(new JoiningMember(member), false);
                envoy.enqueue(new MemberJoiningEvent(member));

                envoy.roar(new MemberStatusUpdate(member, MemberStatus.active), false);
                envoy.enqueue(new MemberActiveEvent(member));

                envoy.whisper(member, new ClusterAlignment(cluster));
                envoy.receiveLeaderKeepAlive(member);
            });
        } else {
            LOGGER.info("[{}] is not leader, reject the join request from [{}]", self, context.getPeer());
            envoy.whisperIgnoreStatus(identifier, new JoinClusterResponse(false, null, cluster.getVersion(), "not leader"), false);
        }
    }
}
