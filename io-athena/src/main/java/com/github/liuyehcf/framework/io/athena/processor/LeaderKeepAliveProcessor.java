package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.MemberStatus;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.*;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class LeaderKeepAliveProcessor extends AbstractProcessor<LeaderKeepAlive> {

    public LeaderKeepAliveProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<LeaderKeepAlive> context) {
        LeaderKeepAlive event = context.getEvent();

        String abstractInfo = event.getClusterAbstractInfo();
        long version = event.getVersion();
        if (!Objects.equals(cluster.getAbstractInfo(), abstractInfo)) {
            // todo 检查有下面这个注释的地方
            // event must be create before check
            cluster.atomicOperate(() -> {
                if (Member.isValidLeader(self) && cluster.getVersion() >= version) {
                    ClusterAlignment alignment = new ClusterAlignment(cluster);
                    envoy.whisper(context.getPeer(), alignment);
                }
            });
        }

        if (Member.isValidLeader(self)) {
            Member member = cluster.getMember(context.getPeer());
            if (member != null) {
                envoy.receiveLeaderKeepAlive(member);

                if (member.getStatus().isUnreachable()) {
                    cluster.atomicOperate(() -> {
                        if (member.getStatus().isUnreachable()) {
                            cluster.updateMemberStatus(member, MemberStatus.active);
                            envoy.roar(new MemberStatusUpdate(member, MemberStatus.active), false);
                            envoy.enqueue(new MemberActiveEvent(member));
                        }
                    });
                }
            }
            envoy.whisper(context.getPeer(), new LeaderKeepAliveAck());
        }
    }
}
