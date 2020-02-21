package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Identifier;
import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.MemberStatus;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.*;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class MemberStatusUpdateProcessor extends AbstractProcessor<MemberStatusUpdate> {

    public MemberStatusUpdateProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<MemberStatusUpdate> context) {
        MemberStatusUpdate event = context.getEvent();

        Identifier identifier = event.getIdentifier();
        MemberStatus status = event.getStatus();

        cluster.atomicOperate(() -> {
            Member member = cluster.getMember(identifier);
            if (member != null && cluster.updateMemberStatus(identifier, status)) {
                LOGGER.info("[{}] update [{}]'s status to [{}]", self, identifier, status);

                if (status.isActive()) {
                    envoy.enqueue(new MemberActiveEvent(member));
                } else if (status.isUnreachable()) {
                    envoy.enqueue(new MemberUnreachableEvent(member));
                } else if (status.isLeaving()) {
                    envoy.enqueue(new MemberLeavingEvent(member));
                } else if (status.isRemoved()) {
                    if (cluster.removeMember(member)) {
                        LOGGER.info("[{}] remove [{}] from the cluster", self, identifier);
                        envoy.enqueue(new MemberRemovedEvent(member));
                    }
                }
            } else {
                LOGGER.info("[{}]'s status is already up-to-date", identifier);
            }
        });
    }
}
