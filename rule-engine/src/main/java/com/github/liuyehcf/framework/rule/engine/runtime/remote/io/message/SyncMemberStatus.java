package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Member;

/**
 * @author hechenfeng
 * @date 2019/9/7
 */
public class SyncMemberStatus implements Message {

    private final Member member;

    public SyncMemberStatus(Member member) {
        this.member = member;
    }

    @Override
    public final MessageType getType() {
        return MessageType.SYNC_MEMBER;
    }

    public Member getMember() {
        return member;
    }
}
