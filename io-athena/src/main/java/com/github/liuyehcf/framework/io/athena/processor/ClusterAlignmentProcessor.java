package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Address;
import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.MemberStatus;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.ClusterAlignment;
import com.github.liuyehcf.framework.io.athena.event.MemberActiveEvent;
import com.github.liuyehcf.framework.io.athena.event.MemberRemovedEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class ClusterAlignmentProcessor extends AbstractProcessor<ClusterAlignment> {

    public ClusterAlignmentProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<ClusterAlignment> context) {
        ClusterAlignment event = context.getEvent();

        LOGGER.info("[{}] receive 'ClusterAlignment' from [{}], localCluster={}; expectedCluster={}",
                self, context.getPeer(), cluster.toReadableString(), event.toReadableString());

        Member leader = event.getLeader();
        long version = event.getVersion();

        cluster.atomicOperate(() -> {
            if (!Objects.equals(leader, cluster.getLeader())) {
                LOGGER.debug("[{}] ignore 'ClusterAlignment' due to leader not match, localLeader=[{}]; expectedLeader=[{}]",
                        self, cluster.getLeader(), leader);
                return;
            }

            if (!Objects.equals(version, cluster.getVersion())) {
                LOGGER.debug("[{}] ignore 'ClusterAlignment' due to version not match, localVersion=[{}]; expectedVersion=[{}]",
                        self, cluster.getVersion(), version);
                cluster.setLeader(null);
                return;
            }

            Map<Address, Member> members = event.getMembers();

            // add members not in the existedMembers
            for (Member member : members.values()) {
                MemberStatus status = member.getStatus();
                if (!cluster.hasMember(member)) {
                    if (cluster.addMember(member)) {
                        LOGGER.info("[{}] add [{}] to the cluster, and update it's status to [{}] according to 'ClusterAlignment'",
                                self, member, member.getStatus());
                        if (status.isActive()) {
                            envoy.enqueue(new MemberActiveEvent(member));
                        }
                    }
                }
            }

            // remove members not int the activeMembers
            List<Member> existedMembers = cluster.getMembers();
            for (Member member : existedMembers) {
                if (!members.containsKey(member)) {
                    if (cluster.removeMember(member)) {
                        LOGGER.info("[{}] remove [{}] from the cluster according to 'ClusterAlignment'", self, member);
                        envoy.enqueue(new MemberRemovedEvent(member));
                    }
                }
            }
        });
    }

    @Override
    protected boolean needLog() {
        return false;
    }
}
