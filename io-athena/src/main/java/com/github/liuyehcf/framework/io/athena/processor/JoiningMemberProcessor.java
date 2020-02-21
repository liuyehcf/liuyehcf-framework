package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.MemberStatus;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import com.github.liuyehcf.framework.io.athena.event.JoiningMember;
import com.github.liuyehcf.framework.io.athena.event.MemberJoiningEvent;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class JoiningMemberProcessor extends AbstractProcessor<JoiningMember> {

    public JoiningMemberProcessor(UnsafeEnvoy envoy) {
        super(envoy);
    }

    @Override
    protected void doProcess(EventContext<JoiningMember> context) {
        JoiningMember event = context.getEvent();

        Member member = event.getMember();

        cluster.atomicOperate(() -> {
            if (!Objects.equals(self, member)) {
                if (!cluster.addMember(member)) {
                    LOGGER.info("[{}] ignore 'AddMember' due to [{}] is already in the cluster", self, member);
                    return;
                }
            }
            LOGGER.info("[{}] add [{}] to the cluster, and update it's status to [{}]", self, member, MemberStatus.joining);
            cluster.updateMemberStatus(member, MemberStatus.joining);
            envoy.enqueue(new MemberJoiningEvent(member));
        });
    }
}
